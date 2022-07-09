package cn.ywrby.service.impl;

import cn.ywrby.domain.*;
import cn.ywrby.mapper.UserMapper;
import cn.ywrby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;



    @Override
    public User verify(User user) {
        String username=user.getUsername();
        String pwd= user.getPassword();
        user = userMapper.findUserByUserAndPwd(username, pwd);
        if(user!=null){
            return user;
        }else {
            return null;
        }
    }

    @Override
    public User findUserByUsername(String username) {
        User user = userMapper.findUserByUsername(username);
        if(user!=null) {
            List<Integer> permissionList = userMapper.getUserPermission(user.getId());
            List<String> roles = new ArrayList<>();
            for (int i : permissionList) {
                if (i == 0) {
                    roles.add("student");
                } else if (i == 1) {
                    roles.add("teacher");
                }
            }
            //List<String> roles= Arrays.asList("admin","editor");
            user.setRoles(roles);
            return user;
        }
        return null;
    }




    @Override
    public boolean insertUser(User user,String courseNum) {
        Integer courseId=userMapper.getCourseId(courseNum);
        if(courseId!=null || courseNum.isEmpty() ) {
            int id = userMapper.insertUser(user);
            if (id != 0) {
                //成功插入学生信息，继续插入学生权限信息
                int permissionId = 0;
                userMapper.insertUserPermission(user.getId(), permissionId);
                //如果学生号不为空，则继续将学生加入课程
                if(!courseNum.isEmpty()){
                    userMapper.insertUserCourse(user.getId(),courseId);
                }
                return true;
            }
        }
        return false;
    }


}
