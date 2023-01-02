package cn.edu.bit.BITKill.repo;


import cn.edu.bit.BITKill.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper{

    //插入
    @Insert(value = "INSERT INTO user_table(username, password, sex, age) VALUES(#{username}, #{password}, #{sex}, #{age})")
    Integer insert_user(User user);

    //查询
    @Select(value = "SELECT * FROM user_table WHERE username=#{username}")
    User select_name(String username);

    @Select(value = "SELECT * FROM user_table")
    List<User> select_all();

    //修改
    @Update(value = "UPDATE user_table SET password=#{password}, sex=#{sex}, age=#{age} WHERE username=#{username}")
    Integer update_user(User user);

    //删除
    @Delete(value = "DELETE FROM user_table WHERE username=#{username}")
    Integer delete_name(String username);

}
