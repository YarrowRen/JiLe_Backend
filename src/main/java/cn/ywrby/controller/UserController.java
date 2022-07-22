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

    @GetMapping("/frontendState")
    public ResultResponse frontendState(){
        ResultResponse res=new ResultResponse();
        res.setData("后端正常启动中...");
        res.setCode(Constants.STATUS_OK);
        return res;
    }




}
