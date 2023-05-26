package cn.ywrby.service.impl;

import cn.ywrby.domain.*;
import cn.ywrby.mapper.UserMapper;
import cn.ywrby.service.UserService;
import cn.ywrby.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public boolean copyFile(String source, String dest) {
        File sourceFile=new File(source);
        File destFile=new File(dest);
        try {
            FileUtils.copyFileUsingChannel(sourceFile,destFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
