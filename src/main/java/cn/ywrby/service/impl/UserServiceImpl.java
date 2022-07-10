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


}
