package cn.ywrby.controller;

import cn.ywrby.domain.*;
import cn.ywrby.res.ResultResponse;
import cn.ywrby.service.UserService;
import cn.ywrby.utils.Constants;
import cn.ywrby.utils.FileUtils;
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

    /**
     * 只进行文件复制操作，并且只处理单个文件的复制
     * @param source 源文件路径
     * @param dest 复制文件路径
     * @return
     */
    @GetMapping("/copyFile")
    public ResultResponse copyFile(@RequestParam String source,@RequestParam String dest){
        ResultResponse res=new ResultResponse();
        boolean result=userService.copyFile(source,dest);

        if(result){
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"复制失败！");
        }
        return res;

    }




}
