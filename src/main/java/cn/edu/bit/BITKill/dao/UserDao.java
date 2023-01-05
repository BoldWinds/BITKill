package cn.edu.bit.BITKill.dao;

import cn.edu.bit.BITKill.model.UserParam;

import java.util.List;

public interface UserDao {

    public boolean saveUser(UserParam user);

    public UserParam getUser(String username);

    public List<UserParam> getAllUsers();

    public boolean updateUser(UserParam user);

    public boolean deleteUser(String username);
}
