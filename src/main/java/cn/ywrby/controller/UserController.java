package cn.ywrby.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.ywrby.domain.*;
import cn.ywrby.res.ResultResponse;
import cn.ywrby.service.UserService;
import cn.ywrby.utils.Constants;
import cn.ywrby.utils.JwtUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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


    @GetMapping("/info")
    public ResultResponse info(@RequestParam("token") String token){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)){
            //如果合法则返回用户信息
            String username=tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS,"");
            User user=userService.findUserByUsername(username);
            user.setUsername(username);
            user.setAvatar("https://ywrbyimg.oss-cn-chengdu.aliyuncs.com/img/ywrby.png");

            res.setData(user);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL);
        }

        return res;
    }


    @PostMapping("/logout")
    public ResultResponse logout(@RequestHeader("X-Token") String token){
        ResultResponse res=new ResultResponse();
        //验证token的合法性和有效性
        String tokenValue=JwtUtils.verify(token);
        //获取token中的用户名
        String username=tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS,"");
        //移除Session中的登陆标记（或redis中的登录标记）

        res.setMessage("Logout success");
        res.setData("Logout success");
        res.setCode(Constants.STATUS_OK);
        return res;
    }



    @PostMapping("/regist")
    public ResultResponse regist(@RequestParam("registForm") String registForm){
        ResultResponse res=new ResultResponse();
        //解析JSON字符串，取得用户名与密码信息以及登录token
        JSONObject jsonObject= JSONUtil.parseObj(registForm);

        //获取用户信息
        String courseNum= (String) jsonObject.get("classifyNum");
        String password= (String) jsonObject.get("password");
        String name = (String) jsonObject.get("name");
        String username=(String) jsonObject.get("username");
        String sex=(String) jsonObject.get("sex");

        //插入用户信息
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setSex(sex);

        //查询用户表单是否已经存在
        User searchUser = userService.findUserByUsername(username);
        if(searchUser!=null){
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+": 学号已注册，请根据学号与密码进行登录");
        }else {
            boolean result=userService.insertUser(user,courseNum);
            if(result){
                //验证成功，创建一个Token，封装到res对象中
                String token = JwtUtils.sign(user.getUsername(), "-1");
                res.setCode(Constants.STATUS_OK);
                res.setMessage(Constants.MESSAGE_OK);
                res.setData(new Token(token));
            }else {
                //否则返回错误状态码
                res.setCode(Constants.STATUS_FAIL);
                res.setMessage(Constants.MESSAGE_FAIL+": 注册失败，请检查课程码是否输入正确。");
            }
        }
        return res;
    }


}
