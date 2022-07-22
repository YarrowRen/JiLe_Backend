package cn.ywrby.controller;

import cn.ywrby.domain.ImgCol;
import cn.ywrby.domain.User;
import cn.ywrby.res.ResultResponse;
import cn.ywrby.service.IcService;
import cn.ywrby.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ic")
public class IcController {

    @Autowired
    IcService icService;

    @PostMapping("/addIc")
    public ResultResponse addIc(@RequestBody ImgCol imgCol){
        System.out.println(imgCol);
        ResultResponse res=new ResultResponse();
        int id=icService.addIc(imgCol);
        if (id!=0){
            res.setCode(Constants.STATUS_OK);
            res.setData(id);
            res.setMessage("图集创建成功。");
        }else {
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage("图集创建失败！");
        }
        return res;
    }

    @GetMapping("/getIc")
    public ResultResponse getIc(){
        ResultResponse res=new ResultResponse();
        List<ImgCol> icList = icService.getIc();
        res.setMessage("获取成功");
        res.setData(icList);
        res.setCode(Constants.STATUS_OK);
        return res;
    }
}
