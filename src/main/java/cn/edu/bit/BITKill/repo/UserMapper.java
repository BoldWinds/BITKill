package cn.edu.bit.BITKill.repo;


import cn.edu.bit.BITKill.model.params.UserParam;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper{

    //插入
    @Insert(value = "INSERT INTO user_table(username, password, salt) VALUES(#{username}, #{password}, #{salt})")
    Integer insert_user(UserParam user);

    //查询
    @Select(value = "SELECT * FROM user_table WHERE username=#{username}")
    UserParam select_name(String username);

    @Select(value = "SELECT * FROM user_table")
    List<UserParam> select_all();

    //修改
    @Update(value = "UPDATE user_table SET password=#{password}, salt=#{salt} WHERE username=#{username}")
    Integer update_user(UserParam user);

    //删除
    @Delete(value = "DELETE FROM user_table WHERE username=#{username}")
    Integer delete_name(String username);

}
