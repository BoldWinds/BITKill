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

        // 将该game放入全局变量中管理
        game.setGameState(GameState.KILL);
        GlobalData.addGame(game,gameControl);

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
            gameControl.setKillTarget(result);
            game.setGameState(GameState.WITCH);

            // 告知狼人投票结果
            SendHelper.sendMessageByList(game.getPlayersByCharacter(Character.WOLF),new CommonResp<>("kill",true,"kill result",new TargetParam(result)));

            // 清除狼人投票map
            gameControl.setEmptyMap();

            // 等待5s
            try {
                Thread.sleep(5000);
            }catch (InterruptedException e){
                log.warn("exception happens when thread sleeps!");
            }

            // 向全体发送刀人结束
            SendHelper.sendMessageByList(game.getPlayers(),new CommonResp<>("finish",true,"kill finish",null));

            //--------------------------------------------------------------------------
            // 判断女巫、预言家状态, 处理这两位角色死亡的情况
            if(game.getAlivePlayersByCharacter(Character.WITCH).size() == 0){
                // 女巫已经死亡，需要跳过这一阶段
                witchFinish(game.getPlayers());
                if(game.getAlivePlayersByCharacter(Character.PROPHET).size() == 0){
                    // 预言家已经死亡
                    prophetFinish(game.getPlayers());
                    // 天亮了
                    game = wakeUp(gameControl,game);
                    // 判断游戏是否结束
                    if (gameEnd(game)){
                        // 因为游戏结束要把数据从内存中删除，所以不能执行下面的写回，要直接返回
                        return;
                    }
                }else{
                    game.setGameState(GameState.PROPHET);
                }
            }else{
                // 女巫存活，向女巫发送刀人结果
                SendHelper.sendMessageByList(game.getPlayersByCharacter(Character.WITCH),new CommonResp<>("kill result",true,"kill result",new TargetParam(result)));
            }
        }
        // 将更新过的数据写回
        GlobalData.writeBack(roomID,game,gameControl);
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
            switch (drug) {
                case POISON -> {
                    if (drugLeft == Drug.ALL) {
                        drugLeft = Drug.ANTIDOTE;
                    } else {
                        drugLeft = Drug.NONE;
                    }
                }
                case ANTIDOTE -> {
                    if (drugLeft == Drug.ALL) {
                        drugLeft = Drug.POISON;
                    } else {
                        drugLeft = Drug.NONE;
                    }
                }
            }
            game.setDrugs(drugLeft);
        }

        // 发送消息
        SendHelper.sendMessageBySession(session,new CommonResp<>("witch",true,"witch finish",null));

        // 等待5s向全体发送女巫结束
        witchFinish(game.getPlayers());
        game.setGameState(GameState.PROPHET);

        // 检查预言家状态
        if (game.getAlivePlayersByCharacter(Character.PROPHET).size() == 0){
            prophetFinish(game.getPlayers());
            game = wakeUp(gameControl,game);
            // 判断游戏是否结束
            if (gameEnd(game)){
                // 因为游戏结束要把数据从内存中删除，所以不能执行下面的写回，要直接返回
                return;
            }
        }

        // 写回数据
        GlobalData.writeBack(roomID,game,gameControl);
    }

    // 预言家阶段
    public void prophet(WebSocketSession session, String paramJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProphetParam prophetParam = objectMapper.readValue(paramJson, new TypeReference<CommonParam<ProphetParam>>() {}).getContent();
        long roomID = prophetParam.getRoomID();
        String target = prophetParam.getTarget();

        Game game = GlobalData.getGameByID(roomID);
        GameControl gameControl = GlobalData.getGameControlByID(roomID);

        // 获取该角色对应身份
        Character character = game.getPlayerCharacterMap().get(target);

        SendHelper.sendMessageBySession(session,new CommonResp<ProphetResult>("prophet",true,"check finish",new ProphetResult(target,character)));

        // 至此黑夜结束, 向client发送信息进入白天
        game = wakeUp(gameControl,game);
        // 判断游戏是否结束
        if (gameEnd(game)){
            // 因为游戏结束要把数据从内存中删除，所以不能执行下面的写回，要直接返回
            return;
        }
        GlobalData.writeBack(roomID,game,gameControl);
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
        if (target.equals(game.getCaptain())){
            // 投票人是警长
            gameControl.vote(voter,target,1.5);
        }else{
            gameControl.vote(voter,target,1);
        }

        if (gameControl.getVoterTargetMap().size() == game.getAlivePlayers().size()){
            // 选举完成
            String result = gameControl.getVoteResult();
            boolean tie = gameControl.isTie();
            // 更新game中的对应字段
            game.setCaptain(result);
            game.setElectCaptain(false);
            // 告知所有人警长选举完成
            SendHelper.sendMessageByList(game.getPlayers(),new CommonResp<>("elect",true,"elect result",new VoteResult(tie,result,gameControl.getVoterTargetMap())));
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

        if (gameControl.getVoterTargetMap().size() == game.getAlivePlayers().size()){
            // 投票完成
            String result = gameControl.getVoteResult();
            boolean tie = gameControl.isTie();
            gameControl.setBanishTarget(result);
            // 告知所有人放逐投票完成
            if(!SendHelper.sendMessageByList(game.getPlayers(),new CommonResp<>("vote",true,"vote result",new VoteResult(tie,result,gameControl.getVoterTargetMap())))){
                // 发送失败
                SendHelper.sendMessageBySession(session,new CommonResp<>());
            }
            // 清除投票相关的map
            gameControl.setEmptyMap();

            // 检测游戏是否结束
            if (gameEnd(game)){
                return;
            }
        }

        // 将更新过的数据写回
        GlobalData.writeBack(roomID,game,gameControl);
    }

    // 发送消息和遗言合并在这里
    public void sendMessage(WebSocketSession session,String paramJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CommonParam<MessageParam> commonParam = objectMapper.readValue(paramJson, new TypeReference<CommonParam<MessageParam>>(){});
        MessageParam chatMessage = commonParam.getContent();
        // 获取数据
        String type = commonParam.getType();
        long roomID = chatMessage.getRoomID();
        ChatChannel chatChannel = chatMessage.getChannel();

        Game game = GlobalData.getGameByID(roomID);

        // 根据不同的Channel向不同的玩家发送消息
        if(chatChannel == ChatChannel.ALL){
            if (!SendHelper.sendMessageByList(game.getPlayers(),new CommonResp<>( type,true,type+" successfully",chatMessage))){
                // 发送失败
                SendHelper.sendMessageBySession(session,new CommonResp<>());
            }
        }else if(chatChannel == ChatChannel.WOLVES){
            if (!SendHelper.sendMessageByList(game.getPlayersByCharacter(Character.WOLF),new CommonResp<>( type,true,type+" successfully",chatMessage))){
                // 发送失败
                SendHelper.sendMessageBySession(session,new CommonResp<>());
            }
        }
    }

    // 夜晚结束，更新game
    private Game wakeUp(GameControl gameControl,Game game) {
        // 更新夜晚所作的事情对游戏产生的影响
        if (gameControl.getDrugType() == Drug.POISON) {
            // 女巫使用毒药
            game.playerDie(gameControl.getKillTarget());
            game.playerDie(gameControl.getWitchTarget());
        } else if (gameControl.getDrugType() == Drug.ANTIDOTE && gameControl.getKillTarget().equals(gameControl.getDrugTarget())) {
            // 女巫使用解药,无人死亡
        } else {
            // 女巫不使用药品,狼人击杀目标
            game.playerDie(gameControl.getKillTarget());
        }
        // 设置下一个状态
        if(game.isElectCaptain()){
            game.setGameState(GameState.ELECT);
        }else{
            game.setGameState(GameState.VOTE);
        }
        // 清空gameControl
        gameControl.clear();
        // 发送消息
        CommonResp<Game> resp = new CommonResp<>("night end",true,"night end",game);
        SendHelper.sendMessageByList(game.getPlayers(),resp);
        return game;
    }

    private boolean gameEnd(Game game){
        long roomID = game.roomID;
        Character character = game.judgeEnd();
        if(character != Character.UNDEF){
            // 游戏结束
            SendHelper.sendMessageByList(game.getPlayers(),new CommonResp<>("game end",true,"game end",new WinnerParam(roomID,character)));
            // 将相应变量在内存中删除
            GlobalData.removeGame(roomID);
            // 将room状态改为不在游戏中
            Room room = GlobalData.getRoomByID(roomID);
            room.setGaming(false);
            GlobalData.setRoomByID(roomID,room);
            return true;
        }else {
            // 游戏未结束
            return false;
        }
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
