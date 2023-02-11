package cn.ywrby.controller;

import cn.ywrby.domain.*;
import cn.ywrby.res.ResultResponse;
import cn.ywrby.service.EcService;
import cn.ywrby.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ec")
public class EcController {

    @Autowired
    EcService ecService;

    @GetMapping("/refreshEcData")
    public ResultResponse refreshEcData(@RequestParam Integer ecID){
        ResultResponse res=new ResultResponse();

        boolean result=ecService.refreshEcData(ecID);
        if(result){
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }

        return res;
    }


    @GetMapping("/getEc")
    public ResultResponse getEc(){
        ResultResponse res=new ResultResponse();
        List<EBookCol> ecList = ecService.getEc();
        res.setMessage("获取成功");
        res.setData(ecList);
        res.setCode(Constants.STATUS_OK);
        return res;
    }

    @PostMapping("/addEc")
    public ResultResponse addEc(@RequestBody EBookCol eBookCol){
        System.out.println(eBookCol);
        ResultResponse res=new ResultResponse();
        int id=ecService.addEc(eBookCol);
        if (id!=0){
            res.setCode(Constants.STATUS_OK);
            res.setData(id);
            res.setMessage("书库创建成功。");
        }else {
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage("书库创建失败！");
        }
        return res;
    }

    /**
     * 获取指定ec_id的书库合集的电子书信息并分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/getEBookCol")
    public ResultResponse getEBookCol(@RequestParam(required=true, defaultValue = "0")Integer ec_id,@RequestParam(required=true, defaultValue = "1")Integer page, @RequestParam(required=false,defaultValue="80")Integer pageSize){
        ResultResponse res=new ResultResponse();

        EBookCol ec = ecService.getEcByID(ec_id,page,pageSize);

        res.setData(ec);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);

        return res;
    }

    @PostMapping("/updateEBookDetails")
    public ResultResponse updateEBookDetails(@RequestBody EBook eBook){
        ResultResponse res=new ResultResponse();
        boolean result=ecService.updateEBookDetails(eBook);
        if(result){
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"修改失败，请检查后重新提交。");
        }

        return res;
    }

}
