package cn.edu.bit.BITKill.dao;

import cn.edu.bit.BITKill.model.UserParam;

import java.util.List;

public interface UserDao {

    public void saveUser(UserParam user);

    public UserParam getUser(long id);

    public UserParam getUser(String username);
    public List<UserParam> getUsers();

    public List<UserParam> getAllUsers();

}
