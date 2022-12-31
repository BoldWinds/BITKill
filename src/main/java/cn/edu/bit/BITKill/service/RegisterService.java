package cn.edu.bit.BITKill.service;

import cn.edu.bit.BITKill.dao.UserDaoImpl;
import cn.edu.bit.BITKill.model.RegisterParam;
import cn.edu.bit.BITKill.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    private UserDaoImpl userDaoImpl;

    public void register(RegisterParam registerParam){
        //io可使用虚拟线程，但是虚拟线程的抽象层级还没想好，后面再加
        userDaoImpl.saveUser(new User(registerParam));
    }

}
