package cn.edu.bit.BITKill.handler;


import cn.edu.bit.BITKill.model.CommonParam;
import cn.edu.bit.BITKill.model.UserParam;
import cn.edu.bit.BITKill.service.LoginService;
import cn.edu.bit.BITKill.service.RegisterService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class GameHandler implements WebSocketHandler {

    private final RegisterService registerService;
    private final LoginService loginService;

    public GameHandler(RegisterService registerService, LoginService loginService) {
        this.registerService = registerService;
        this.loginService = loginService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("session(id : "+session.getId()+") has connected!\n");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // 进行具体的消息处理, 包括json解析等
        String paramJson = (String) message.getPayload();

        // 进行json解析，得到param
        ObjectMapper objectMapper = new ObjectMapper();
        CommonParam param = objectMapper.readValue(paramJson,CommonParam.class);

        // 获取消息类型并进行处理
        String type = param.getType();
        switch (type){
            case "register":
                CommonParam<UserParam> registerCommonParam = objectMapper.readValue(paramJson, new TypeReference<CommonParam<UserParam>>(){});
                UserParam userParam = registerCommonParam.getContent();
                if(registerService.register(userParam)){
                    //注册成功

                }else{
                    //注册失败

                }
                break;
            case "login":
                CommonParam<UserParam> loginCommonParam = objectMapper.readValue(paramJson, new TypeReference<CommonParam<UserParam>>(){});
                UserParam userParam1 = loginCommonParam.getContent();
                if(loginService.login(userParam1)){
                    // 登录成功

                }else{
                    // 登录失败
                }
                break;
            case "create room":
                break;

            default:
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
}
