package cn.edu.bit.BITKill.service;

import cn.edu.bit.BITKill.model.*;
import cn.edu.bit.BITKill.model.Character;
import cn.edu.bit.BITKill.util.SendHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Service
public class GameService {
    // TODO: 完成游戏流程相关服务

    // 处理"game start"
    @Async
    public void startGame(WebSocketSession session,String paramJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        RoomIDParam roomIDParam = objectMapper.readValue(paramJson, new TypeReference<CommonParam<RoomIDParam>>(){}).getContent();
        long roomID = roomIDParam.getRoomID();

        Game game = new Game(roomID);
        Room room = GlobalData.getRoomByID(roomID);

        if(room==null){     // roomID错误的情况
            session.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(new CommonResp<>())));
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
        // 将该game放入全局变量中管理
        GlobalData.addGame(game);

        CommonResp<Game> resp = new CommonResp<>("game start",true,"game start!",game);
        if(!SendHelper.sendMessageByList(game.getPlayers(),resp)){
            // 发送失败
            SendHelper.sendErrorMessage(session);
        }

    }

    // 处理"刀人"
    public void kill(WebSocketSession session,String paramJson){

    }
}
