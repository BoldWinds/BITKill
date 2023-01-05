package cn.edu.bit.BITKill.handler;


import cn.edu.bit.BITKill.model.CommonParam;
import cn.edu.bit.BITKill.model.CommonResp;
import cn.edu.bit.BITKill.model.GlobalData;
import cn.edu.bit.BITKill.model.UserParam;
import cn.edu.bit.BITKill.service.GameService;
import cn.edu.bit.BITKill.service.LoginService;
import cn.edu.bit.BITKill.service.RegisterService;
import cn.edu.bit.BITKill.service.RoomService;
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
                    handleRegister(session,paramJson);
                    break;
                case "login":
                    handleLogin(session,paramJson);
                    break;
                case "create room":
                    break;

                case  "get rooms":

                    break;

                case "get a room":

                    break;


                case "join room":

                    break;

                case "leave room":

                    break;
                default:
            }


        }catch (Exception e) {
            CommonResp commonResp = new CommonResp();
            byte[] respJson = objectMapper.writeValueAsBytes(commonResp);
            session.sendMessage(new TextMessage(respJson));
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("There is an exception in session: "+session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("session(id : "+session.getId()+") has closed!\n");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void handleRegister(WebSocketSession session,String paramJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CommonParam<UserParam> registerCommonParam = objectMapper.readValue(paramJson, new TypeReference<CommonParam<UserParam>>(){});
        UserParam userParam = registerCommonParam.getContent();
        if(registerService.register(userParam)){
            //注册成功
            CommonResp<Object> regSucResp = new CommonResp<>("register", true, "Registration successful", null);
            String regSucRespStr = objectMapper.writeValueAsString(regSucResp);
            //System.out.println(regSucRespStr);
            session.sendMessage(new TextMessage(regSucRespStr));
        }else{
            //注册失败
            CommonResp<Object> regFailResp = new CommonResp<>("register", false, "Duplicate username", null);
            String regFailRespStr = objectMapper.writeValueAsString(regFailResp);
            //System.out.println(regFailRespStr);
            session.sendMessage(new TextMessage(regFailRespStr));
        }

    }

    private void handleLogin(WebSocketSession session,String paramJson) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        CommonParam<UserParam> loginCommonParam = objectMapper.readValue(paramJson, new TypeReference<CommonParam<UserParam>>(){});
        UserParam userParam1 = loginCommonParam.getContent();
        CommonResp<Object> loginResp = new CommonResp<>("login", true, "Login successful", null);
        switch (loginService.login(userParam1))
        {
            //登录成功
            case 0:
                GlobalData.userLogin(userParam1.getUsername(),session);
                break;
            //用户名错误
            case 1:
                loginResp.setSuccess(false);
                loginResp.setMessage("Wrong username");
                break;
            //密码错误
            case 2:
                loginResp.setSuccess(false);
                loginResp.setMessage("Wrong password");
                break;
            //重复登录
            case 3:
                loginResp.setSuccess(false);
                loginResp.setMessage("Already login");
                break;
        }
        String loginRespStr = objectMapper.writeValueAsString(loginResp);
        session.sendMessage(new TextMessage(loginRespStr));
    }
}
