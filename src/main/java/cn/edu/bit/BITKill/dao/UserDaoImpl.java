package cn.edu.bit.BITKill.dao;

import cn.edu.bit.BITKill.model.User;
import cn.edu.bit.BITKill.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
    public void saveUser(User user) {
        userRepository.insert(user);
    }

    @Override
    public User getUser(long id) {
        return null;
    }

    @Override
    public User getUser(String username) {

        return userRepository.selectName(username);
    }

    @Override
    public List<User> getUsers() {

        return null;
    }

    @Override
    public List<User> getAllUsers() {

        return userRepository.selectAll();
    }
}
