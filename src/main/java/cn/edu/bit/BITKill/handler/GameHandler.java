package cn.edu.bit.BITKill.handler;


import cn.edu.bit.BITKill.model.CommonParam;
import cn.edu.bit.BITKill.model.CommonResp;
import cn.edu.bit.BITKill.model.GlobalData;
import cn.edu.bit.BITKill.model.UserParam;
import cn.edu.bit.BITKill.service.LoginService;
import cn.edu.bit.BITKill.service.RegisterService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.*;

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
                break;
            case "login":
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
                /*
                if(loginService.login(userParam1)){
                    // 登录成功
                    GlobalData.userLogin(userParam1.getUsername(),session);

                    CommonResp<Object> loginSucResp = new CommonResp<>("login", true, "Login successful", null);
                    String loginSucRespStr = objectMapper.writeValueAsString(loginSucResp);
                    session.sendMessage(new TextMessage(loginSucRespStr));
                }else{
                    // 登录失败
                    CommonResp<Object> loginFailResp = new CommonResp<>("login", true, "Login successful", null);
                    String loginFailRespStr = objectMapper.writeValueAsString(loginFailResp);
                    session.sendMessage(new TextMessage(loginFailRespStr));
                }
                 */
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
