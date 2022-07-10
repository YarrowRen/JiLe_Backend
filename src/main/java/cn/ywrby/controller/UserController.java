package cn.ywrby.controller;

import cn.ywrby.domain.*;
import cn.ywrby.res.ResultResponse;
import cn.ywrby.service.UserService;
import cn.ywrby.utils.Constants;
import cn.ywrby.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResultResponse login(@RequestBody User user){
        System.out.println(user);
        ResultResponse res=new ResultResponse();
        //调用Service层的UserService完成对用户名和密码的验证
        try {
            User u=userService.verify(user);
            //根据验证结果，组成响应对象返回
            if (u!=null){
                //验证成功，创建一个Token，封装到res对象中
                String token = JwtUtils.sign(user.getUsername(), "-1");
                res.setCode(Constants.STATUS_OK);
                res.setMessage(Constants.MESSAGE_OK);
                res.setData(new Token(token));
            }else {
                res.setCode(Constants.STATUS_FAIL);
                res.setMessage(Constants.MESSAGE_FAIL+"用户名与密码不匹配");
                res.setData("fail");
            }
        }catch (Exception e){
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+e.getMessage());
            res.setData("fail");
            e.printStackTrace();
        }
        return res;
    }

    @GetMapping("/frontendState")
    public ResultResponse frontendState(){
        ResultResponse res=new ResultResponse();
        res.setData("后端正常启动中...");
        res.setCode(Constants.STATUS_OK);
        return res;
    }




}
