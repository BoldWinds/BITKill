package cn.edu.bit.BITKill.repo;

import cn.edu.bit.BITKill.model.params.UserParam;
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

    public boolean insert(UserParam user)
    {
        return user_mapper.insert_user(user) > 0;
    }

    public UserParam selectName(String username)
    {
        return user_mapper.select_name(username);
    }

    public List<UserParam> selectAll()
    {
        return user_mapper.select_all();
    }

    public boolean updateUser(UserParam user)
    {
        return user_mapper.update_user(user) > 0;
    }

    public boolean deleteUser(String username)
    {
        return user_mapper.delete_name(username) > 0;
    }
}
