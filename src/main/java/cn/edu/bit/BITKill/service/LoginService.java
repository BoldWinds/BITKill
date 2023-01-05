package cn.edu.bit.BITKill.service;

import cn.edu.bit.BITKill.dao.UserDaoImpl;
import cn.edu.bit.BITKill.model.GlobalData;
import cn.edu.bit.BITKill.model.UserParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final UserDaoImpl userDaoImpl;

    public LoginService(UserDaoImpl userDaoImpl) {
        this.userDaoImpl = userDaoImpl;
    }

    public int login(UserParam userParam){
        UserParam dbUser = userDaoImpl.getUser(userParam.getUsername());
        if(dbUser == null)
        {
            //用户名错误
            return 1;
        }
        if(!dbUser.getPassword().equals(userParam.getPassword()))
        {
            //密码错误
            return 2;
        }
        if(GlobalData.getSessionByUsername(userParam.getUsername()) != null)
        {
            //重复登录，视为登录失败
            return 3;
        }
        //登录成功
        return 0;
    }
}
