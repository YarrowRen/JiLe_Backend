package cn.ywrby.mapper;

import cn.ywrby.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 通过用户的校园号与密码验证是否存在用户并返回，主要用于登录
     * @return
     */
    public User findUserByUserAndPwd(@Param("username") String username, @Param("pwd") String pwd);


    public User findUserByUsername(@Param("username") String username);


    List<Integer> getUserPermission(int userId);

    int insertUser(User user);

    void insertUserPermission(int userId, int permissionId);

    Integer getCourseId(String courseNum);

    void insertUserCourse(int userId,int courseId);

    List<Account> showData();
    void insertData(String account,String name,int epochSec);

}
