package cn.edu.bit.BITKill.dao;

import cn.edu.bit.BITKill.model.User;

import java.util.List;

public interface UserDao {

    public void saveUser(User user);

    public User getUser(long id);

    public User getUser(String username);
    public List<User> getUsers();

    public List<User> getAllUsers();

}
