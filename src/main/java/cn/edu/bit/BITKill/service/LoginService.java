package cn.edu.bit.BITKill.service;

import cn.edu.bit.BITKill.dao.UserDaoImpl;
import cn.edu.bit.BITKill.model.params.CommonParam;
import cn.edu.bit.BITKill.model.params.CommonResp;
import cn.edu.bit.BITKill.model.GlobalData;
import cn.edu.bit.BITKill.model.params.UserParam;
import cn.edu.bit.BITKill.util.SendHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
public class LoginService {
    private final UserDaoImpl userDaoImpl;

    public LoginService(UserDaoImpl userDaoImpl) {
        this.userDaoImpl = userDaoImpl;
    }

    @Async
    public void sendSalt(WebSocketSession session, String paramJson) throws IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        CommonParam<String> getSaltParam = objectMapper.readValue(paramJson, new TypeReference<CommonParam<String>>(){});
        String userName = getSaltParam.getContent();

        UserParam dbUser = userDaoImpl.getUser(userName);
        if(dbUser == null)
        {
            CommonResp<String> errorResp = new CommonResp<>("login salt", false, "Invalid username", null);

            //发送
            if(!SendHelper.sendMessageBySession(session, errorResp)){
                System.out.println("Error at sendSalt: Respond error.\n");
            };

            return;
        }

        CommonResp<String> sendSaltResp = new CommonResp<>("login salt", true, "Salt", dbUser.getSalt());
        //发送
        if(!SendHelper.sendMessageBySession(session, sendSaltResp)){
            System.out.println("Error at sendSalt: Respond error.\n");
        };
        return;
    }

    @Async
    public void login(WebSocketSession session, String paramJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CommonParam<UserParam> loginCommonParam = objectMapper.readValue(paramJson, new TypeReference<CommonParam<UserParam>>(){});
        UserParam userParam = loginCommonParam.getContent();
        CommonResp<Object> loginResp = new CommonResp<>("login", true, "Login successful", null);
        switch (checkLogin(userParam)){
            //登录成功
            case 0:
                GlobalData.userLogin(userParam.getUsername(),session);
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

    private int checkLogin(UserParam userParam) {
        UserParam dbUser = userDaoImpl.getUser(userParam.getUsername());
        if (dbUser == null) {
            //用户名错误
            return 1;
        }
        if (!dbUser.getPassword().equals(userParam.getPassword())) {
            //密码错误
            return 2;
        }
        if (GlobalData.getSessionByUsername(userParam.getUsername()) != null) {
            //重复登录，视为登录失败
            return 3;
        }
        //登录成功
        return 0;
    }
}
