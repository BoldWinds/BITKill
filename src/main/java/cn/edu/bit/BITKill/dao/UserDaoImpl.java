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
    public boolean saveUser(UserParam user) {
        if(getUser(user.getUsername()) != null)
        {
            return false;
        }
        return userRepository.insert(user);
    }

    @Override
    public UserParam getUser(String username) {

        return userRepository.selectName(username);
    }

    @Override
    public List<UserParam> getAllUsers() {
        return userRepository.selectAll();
    }

    @Override
    public boolean updateUser(UserParam user) {
        return userRepository.updateUser(user);
    }

    @Override
    public boolean deleteUser(String username) {
        return userRepository.deleteUser(username);
    }
}
