package cn.edu.bit.BITKill.service;

import cn.edu.bit.BITKill.model.*;
import cn.edu.bit.BITKill.model.Character;
import cn.edu.bit.BITKill.model.params.*;
import cn.edu.bit.BITKill.util.SendHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class GameService {

    // 处理"game start"
    public void startGame(WebSocketSession session,String paramJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        RoomIDParam roomIDParam = objectMapper.readValue(paramJson, new TypeReference<CommonParam<RoomIDParam>>(){}).getContent();
        long roomID = roomIDParam.getRoomID();

        GameControl gameControl = new GameControl(roomID);
        Game game = new Game(roomID);
        Room room = GlobalData.getRoomByID(roomID);

        if(room==null){     // roomID错误的情况
            session.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(new CommonResp<>())));
            return;
        }

        // 正确获取到room, 对game进行设置
        List<String> players = room.getPlayers();
        int size = players.size();
        // 设置玩家
        game.setPlayers(players);
        // 设置玩家初始状态
        for (int i = 0; i < size; i++){
            game.addPlayerStateMap(players.get(i),true);
            game.addPlayerCharacterMap(players.get(i),Character.UNDEF);
        }
        // 分配身份
        game.assignCharacters();

        // 发送消息
        CommonResp<Game> resp = new CommonResp<>("game start",true,"game start!",game);
        if(!SendHelper.sendMessageByList(game.getPlayers(),resp)){
            // 发送失败
            SendHelper.sendErrorMessage(session);
        }

        // 将该gameControl放入全局变量中管理
        GlobalData.addGameControl(gameControl);
        game.setGameState(GameState.KILL);
        GlobalData.addGame(game);

    }

    // 处理"刀人"
    public void kill(WebSocketSession session,String paramJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        VoteParam voteParam = objectMapper.readValue(paramJson, new TypeReference<CommonParam<VoteParam>>(){}).getContent();
        // 从content中获取信息
        long roomID = voteParam.getRoomID();
        String voter = voteParam.getVoter();
        String target = voteParam.getTarget();

        // 获取gameControl和game
        GameControl gameControl = GlobalData.getGameControlByID(roomID);
        Game game = GlobalData.getGameByID(roomID);

        // 处理投票逻辑
        gameControl.vote(voter,target,1);

        // 检测投票是否完成
        if(gameControl.getVoterTargetMap().size() == game.getAlivePlayersByCharacter(Character.WOLF).size()){
            // 投票完成
            String result = gameControl.getVoteResult();
            // 告知狼人投票结果
            SendHelper.sendMessageByList(game.getPlayersByCharacter(Character.WOLF),new CommonResp<>("kill",true,"kill result",new TargetParam(result)));
            try {
                // 等待5s
                Thread.sleep(5000);
            }catch (InterruptedException e){
                log.warn("exception happens when thread sleeps!");
            }
            // 向全体发送刀人结束
            SendHelper.sendMessageByList(game.getPlayers(),new CommonResp<>("finish",true,"kill finish",null));

            // 清除狼人投票map
            gameControl.setEmptyMap();
            gameControl.setKillTarget(result);

            if(game.getAlivePlayersByCharacter(Character.WITCH).size() == 0){
                // 女巫已经死亡，需要跳过这一阶段
                witchFinish(game.getPlayers());
                if(game.getAlivePlayersByCharacter(Character.PROPHET).size() == 0){
                    // 预言家已经死亡
                    prophetFinish(game.getPlayers());
                    // TODO: 提醒天亮
                    wakeUp(game);
                }else{
                    game.setGameState(GameState.PROPHET);
                }
            }else{
                // 向女巫发送刀人结果
                SendHelper.sendMessageByList(game.getPlayersByCharacter(Character.WITCH),new CommonResp<>("kill result",true,"kill result",new TargetParam(result)));
            }
        }

        // 将更新过的数据写回
        GlobalData.setGameControlByID(roomID,gameControl);
        GlobalData.setGameByID(roomID,game);
    }

    // 处理女巫阶段
    public void witch(WebSocketSession session,String paramJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        WitchParam witchParam = objectMapper.readValue(paramJson, new TypeReference<CommonParam<WitchParam>>(){}).getContent();
        // 从content中获取信息
        long roomID = witchParam.getRoomID();
        String target = witchParam.getTarget();
        Drug drug = witchParam.getDrug();

        // 获取gameControl和game
        GameControl gameControl = GlobalData.getGameControlByID(roomID);
        Game game = GlobalData.getGameByID(roomID);
        Drug drugLeft = game.getDrugs();

        if (drug != Drug.NONE){
            // 更新gameControl中的内容
            gameControl.setWitchTarget(target);
            gameControl.setDrugType(drug);
            // 根据女巫使用的药，更新game中的剩余药
            switch (drug){
                case POISON:
                    if(drugLeft == Drug.ALL){
                        drugLeft = Drug.ANTIDOTE;
                    }else{
                        drugLeft = Drug.NONE;
                    }
                    break;
                case  ANTIDOTE:
                    if(drugLeft == Drug.ALL){
                        drugLeft = Drug.POISON;
                    }else{
                        drugLeft = Drug.NONE;
                    }
                    break;
            }
            game.setDrugs(drugLeft);
        }

        // 发送消息
        SendHelper.sendMessageBySession(session,new CommonResp<>("witch",true,"witch finish",null));

        // 等待5s向全体发送女巫结束
        witchFinish(game.getPlayers());
        // TODO: 预言家死亡情况, 提醒天亮
        if (game.getAlivePlayersByCharacter(Character.PROPHET).size() == 0){
            prophetFinish(game.getPlayers());
            wakeUp(game);
        }

        // 写回数据
        GlobalData.setGameControlByID(roomID,gameControl);
        game.setGameState(GameState.PROPHET);
        GlobalData.setGameByID(roomID,game);
    }

    // 预言家阶段
    public void prophet(WebSocketSession session, String paramJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProphetParam prophetParam = objectMapper.readValue(paramJson, new TypeReference<CommonParam<ProphetParam>>() {}).getContent();
        long roomID = prophetParam.getRoomID();
        String target = prophetParam.getTarget();

        Game game = GlobalData.getGameByID(roomID);
        // 获取该角色对应身份
        Character character = game.getPlayerCharacterMap().get(target);

        SendHelper.sendMessageBySession(session,new CommonResp<ProphetResult>("prophet",true,"check finish",new ProphetResult(target,character)));

        // 至此黑夜结束, 向client发送信息进入白天
        wakeUp(game);

    }

    // 选举警长
    public void elect(WebSocketSession session,String paramJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        VoteParam voteParam = objectMapper.readValue(paramJson, new TypeReference<CommonParam<VoteParam>>(){}).getContent();
        long roomID = voteParam.getRoomID();
        String voter = voteParam.getVoter();
        String target = voteParam.getTarget();

        // 获取gameControl和game
        GameControl gameControl = GlobalData.getGameControlByID(roomID);
        Game game = GlobalData.getGameByID(roomID);

        // 处理投票逻辑
        gameControl.vote(voter,target,1);

        if (gameControl.getVoterTargetMap().size() == game.getPlayers().size()){
            // 选举完成
            String result = gameControl.getVoteResult();
            // 更新game中的对应字段
            game.setCaptain(result);
            game.setElectCaptain(false);
            // 告知所有人警长选举完成
            SendHelper.sendMessageByList(game.getPlayers(),new CommonResp<>("elect",true,"elect result",new VoteResult(false,result,gameControl.getVoterTargetMap())));
            // 清除投票相关的map
            gameControl.setEmptyMap();
        }
        // 将更新过的数据写回
        GlobalData.setGameControlByID(roomID,gameControl);
        GlobalData.setGameByID(roomID,game);
    }

    // 投票淘汰
    public void vote(WebSocketSession session, String paramJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        VoteParam voteParam = objectMapper.readValue(paramJson, new TypeReference<CommonParam<VoteParam>>() {}).getContent();
        long roomID = voteParam.getRoomID();
        String voter = voteParam.getVoter();
        String target = voteParam.getTarget();

        // 获取gameControl和game
        GameControl gameControl = GlobalData.getGameControlByID(roomID);
        Game game = GlobalData.getGameByID(roomID);

        // 处理投票逻辑
        if(voter.equals(game.getCaptain())){
            gameControl.vote(voter,target,1.5);
        }else{
            gameControl.vote(voter,target,1);
        }

        if (gameControl.getVoterTargetMap().size() == game.getPlayers().size()){
            // 投票完成
            String result = gameControl.getVoteResult();
            gameControl.setBanishTarget(result);
            // 告知所有人放逐投票完成
            if(!SendHelper.sendMessageByList(game.getPlayers(),new CommonResp<>("vote",true,"vote result",new VoteResult(false,result,gameControl.getVoterTargetMap())))){
                // 发送失败
                SendHelper.sendMessageBySession(session,new CommonResp<>());
            }
            // 清除投票相关的map
            gameControl.setEmptyMap();
        }

        // 将更新过的数据写回
        GlobalData.setGameControlByID(roomID,gameControl);
        GlobalData.setGameByID(roomID,game);

    }

    // 发送消息
    public void sendMessage(WebSocketSession session,String paramJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        MessageParam chatMessage = objectMapper.readValue(paramJson, new TypeReference<CommonParam<MessageParam>>(){}).getContent();
        long roomID = chatMessage.getRoomID();
        ChatChannel chatChannel = chatMessage.getChannel();

        Game game = GlobalData.getGameByID(roomID);

        // 根据不同的Channel向不同的玩家发送消息
        if(chatChannel == ChatChannel.ALL){
            if (!SendHelper.sendMessageByList(game.getPlayers(),new CommonResp<>( "send message",true,"Send message successfully",chatMessage))){
                // 发送失败
                SendHelper.sendMessageBySession(session,new CommonResp<>());
            }
        }else if(chatChannel == ChatChannel.WOLVES){
            if (!SendHelper.sendMessageByList(game.getPlayersByCharacter(Character.WOLF),new CommonResp<>( "send message",true,"Send message successfully",chatMessage))){
                // 发送失败
                SendHelper.sendMessageBySession(session,new CommonResp<>());
            }
        }
    }

    // TODO:发送天亮提醒消息
    private void wakeUp(Game game){

    }

    // TODO:判断游戏是否结束
    private boolean judgeEnd(GameControl gameControl,Game game){
        return false;
    }

    // 女巫阶段结束
    private void witchFinish(List<String> players){
        try {
            Thread.sleep(5000);
        }catch (InterruptedException e){
            log.warn("exception happens when thread sleeps!");
        }
        SendHelper.sendMessageByList(players,new CommonResp<>("finish",true,"witch finish",null));
    }

    // 预言家阶段结束
    private void prophetFinish(List<String> players){
        try {
            Thread.sleep(5000);
        }catch (InterruptedException e){
            log.warn("exception happens when thread sleeps!");
            SendHelper.sendMessageByList(players,new CommonResp<>("finish",true,"Prophet finish",null));
        }
    }
}
