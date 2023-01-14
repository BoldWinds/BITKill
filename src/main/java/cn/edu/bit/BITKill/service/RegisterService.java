package cn.edu.bit.BITKill.service;

import cn.edu.bit.BITKill.dao.UserDaoImpl;
import cn.edu.bit.BITKill.model.params.CommonParam;
import cn.edu.bit.BITKill.model.params.CommonResp;
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
public class RegisterService {

    private final UserDaoImpl userDaoImpl;

    public RegisterService(UserDaoImpl userDaoImpl) {
        this.userDaoImpl = userDaoImpl;
    }

    @Async
    public void register(WebSocketSession session, String paramJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CommonParam<UserParam> registerCommonParam = objectMapper.readValue(paramJson, new TypeReference<CommonParam<UserParam>>(){});
        UserParam userParam = registerCommonParam.getContent();
        if (userParam.getUsername().equals("")){
            // 不能起空字符串作为用户名
            CommonResp<Object> regFailResp = new CommonResp<>("register", false, "Can not use this username", null);
            SendHelper.sendMessageBySession(session,regFailResp);
        }
        if(userDaoImpl.saveUser(userParam)){
            //注册成功
            CommonResp<Object> regSucResp = new CommonResp<>("register", true, "Registration successful", null);
            SendHelper.sendMessageBySession(session,regSucResp);
        }else{
            //注册失败
            CommonResp<Object> regFailResp = new CommonResp<>("register", false, "Duplicate username", null);
            SendHelper.sendMessageBySession(session,regFailResp);
        }
    }

}
