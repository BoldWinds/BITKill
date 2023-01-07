package cn.edu.bit.BITKill.handler;


import cn.edu.bit.BITKill.model.CommonParam;
import cn.edu.bit.BITKill.model.CommonResp;
import cn.edu.bit.BITKill.model.GlobalData;
import cn.edu.bit.BITKill.model.UserParam;
import cn.edu.bit.BITKill.service.GameService;
import cn.edu.bit.BITKill.service.LoginService;
import cn.edu.bit.BITKill.service.RegisterService;
import cn.edu.bit.BITKill.service.RoomService;
import cn.edu.bit.BITKill.util.SendHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class GameHandler extends TextWebSocketHandler {

    private final RegisterService registerService;
    private final LoginService loginService;

    private final RoomService roomService;

    private final GameService gameService;

    public GameHandler(RegisterService registerService, LoginService loginService, RoomService roomService, GameService gameService) {
        this.registerService = registerService;
        this.loginService = loginService;
        this.roomService = roomService;
        this.gameService = gameService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("session(id : "+session.getId()+") has connected!\n");
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 进行具体的消息处理, 包括json解析等
        String paramJson = message.getPayload();

        // 进行json解析，得到param
        ObjectMapper objectMapper = new ObjectMapper();
        CommonParam param = objectMapper.readValue(paramJson,CommonParam.class);

        // 获取消息类型并进行处理
        String type = param.getType();
        try{
            switch (type){
                case "register":
                    registerService.register(session,paramJson);
                    break;
                case "login":
                    loginService.login(session,paramJson);
                    break;
                case "create room":
                    roomService.createRoom(session, paramJson);
                    break;
                case  "get rooms":
                    roomService.getRooms(session, paramJson);
                    break;
                case "get a room":
                    roomService.getARoom(session, paramJson);
                    break;
                case "join room":
                    roomService.joinRoom(session, paramJson);
                    break;
                case "leave room":
                    roomService.leaveRoom(session, paramJson);
                    break;
                case "game start":
                    gameService.startGame(session,paramJson);
                    break;
                default:
            }


        }catch (Exception e) {
            SendHelper.sendMessageBySession(session,new CommonResp());
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("There is an exception in session: "+session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("session(id : "+session.getId()+") has closed!");

        //因一些原因断开连接时，需要查询被断开的连接是否时某个用户对应的，若是，则将该用户登出
        String user = GlobalData.getUsernameBySession(session);
        if(user != null)
        {
            GlobalData.userLogout(user, session);
            System.out.println("user " + user + " is logged out due to closing session.");
        }

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
