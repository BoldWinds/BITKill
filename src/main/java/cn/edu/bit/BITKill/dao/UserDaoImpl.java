package cn.edu.bit.BITKill.dao;

import cn.edu.bit.BITKill.model.UserParam;
import cn.edu.bit.BITKill.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDaoImpl implements UserDao{

    private final UserRepository userRepository;

    @Autowired
    public UserDaoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(UserParam user) {
        userRepository.insert(user);
    }

    @Override
    public UserParam getUser(long id) {
        return null;
    }

    @Override
    public UserParam getUser(String username) {

        return userRepository.selectName(username);
    }

    @Override
    public List<UserParam> getUsers() {

        return null;
    }

    @Override
    public List<UserParam> getAllUsers() {

        return userRepository.selectAll();
    }
}
