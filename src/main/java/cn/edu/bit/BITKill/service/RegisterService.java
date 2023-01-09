package cn.edu.bit.BITKill.service;

import cn.edu.bit.BITKill.dao.UserDaoImpl;
import cn.edu.bit.BITKill.model.params.CommonParam;
import cn.edu.bit.BITKill.model.params.CommonResp;
import cn.edu.bit.BITKill.model.params.UserParam;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public void register(WebSocketSession session, String paramJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CommonParam<UserParam> registerCommonParam = objectMapper.readValue(paramJson, new TypeReference<CommonParam<UserParam>>(){});
        UserParam userParam = registerCommonParam.getContent();
        if(userDaoImpl.saveUser(userParam)){
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

}
