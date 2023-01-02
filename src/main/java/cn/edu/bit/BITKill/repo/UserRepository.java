package cn.edu.bit.BITKill.repo;

import cn.edu.bit.BITKill.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//负责与数据库直接交互
public class UserRepository {
    UserMapper user_mapper;

    @Autowired
    public UserRepository(UserMapper mapper)
    {
        this.user_mapper = mapper;
    }

    public boolean insert(User user)
    {
        return user_mapper.insert_user(user) > 0;
    }

    public User selectName(String username)
    {
        return user_mapper.select_name(username);
    }

    public List<User> selectAll()
    {
        return user_mapper.select_all();
    }

    public boolean updateUser(User user)
    {
        return user_mapper.update_user(user) > 0;
    }

    public boolean deleteUser(String username)
    {
        return user_mapper.delete_name(username) > 0;
    }
}
