package cn.edu.bit.BITKill.service;

import cn.edu.bit.BITKill.dao.UserDaoImpl;
import cn.edu.bit.BITKill.model.UserParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final UserDaoImpl userDaoImpl;

    public RegisterService(UserDaoImpl userDaoImpl) {
        this.userDaoImpl = userDaoImpl;
    }

    // 注册成功返回true，失败返回false
    public boolean register(UserParam userParam){
        //io可使用虚拟线程，但是虚拟线程的抽象层级还没想好，后面再加
        return userDaoImpl.saveUser(userParam);
    }

}
