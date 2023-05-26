package cn.ywrby.controller;

import cn.ywrby.domain.*;
import cn.ywrby.res.ResultResponse;
import cn.ywrby.service.IcService;
import cn.ywrby.utils.Constants;
import cn.ywrby.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @GetMapping("/refreshIcData")
    public ResultResponse refreshIcData(@RequestParam Integer icID){
        ResultResponse res=new ResultResponse();

        boolean result=icService.refreshIcData(icID);
        if(result){
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }

        return res;
    }

    @GetMapping("/getImgCol")
    public ResultResponse getImgCol(@RequestParam(required=true, defaultValue = "0")Integer ic_id,
                                    @RequestParam(required=true, defaultValue = "1")Integer page,
                                    @RequestParam(required=false,defaultValue="60")Integer pageSize){
        ResultResponse res=new ResultResponse();
        ImgCol ic = icService.getIcByID(ic_id,page,pageSize);
        res.setData(ic);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;
    }

    @GetMapping("/getImageTag")
    public ResultResponse getImageTag(@RequestParam Integer imageID){
        ResultResponse res=new ResultResponse();
        List<Tag> imageTag = icService.getImageTag(imageID);
        res.setData(imageTag);
        res.setMessage(Constants.MESSAGE_OK);
        res.setCode(Constants.STATUS_OK);
        return res;
    }

    @PostMapping("/updateImageInfo")
    public ResultResponse updateImageInfo(@RequestBody Image image){
        ResultResponse res=new ResultResponse();
        boolean result=icService.updateImageInfo(image);
        if(result){
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"修改失败，请检查后重新提交。");
        }

        return res;
    }

    @GetMapping("/getImageDetails")
    public ResultResponse getImageDetails(@RequestParam Integer imageID){
        ResultResponse res=new ResultResponse();
        Image image = icService.getImageDetails(imageID);
        res.setData(image);
        res.setMessage(Constants.MESSAGE_OK);
        res.setCode(Constants.STATUS_OK);
        return res;
    }

    @PostMapping("/changeFollowedState")
    public ResultResponse changeFollowedState(@RequestParam int imageID){
        ResultResponse res=new ResultResponse();
        icService.changeFollowedState(imageID);

        res.setCode(Constants.STATUS_OK);
        res.setData(imageID);
        return res;
    }

    @GetMapping("/getMainColor")
    public ResultResponse getMainColor(@RequestParam String filePath,@RequestParam int colorCount) throws IOException {
        ResultResponse res=new ResultResponse();
        int[][] mainColor = FileUtils.getMainColor(filePath, colorCount);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        res.setData(mainColor);
        return res;
    }

    @PostMapping("/deleteImage")
    public ResultResponse deleteImage(@RequestParam int imageID){
        ResultResponse res=new ResultResponse();
        boolean result=icService.deleteImage(imageID);
        if(result){
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"删除失败，请刷新后重试");
        }

        return res;
    }

    @GetMapping("/getRandomImage")
    public ResultResponse getRandomImage(@RequestParam int num){
        ResultResponse res=new ResultResponse();
        List<Image> imageList= icService.getRandomImage(num);
        res.setData(imageList);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;
    }
    @PostMapping("/deleteIC")
    public ResultResponse deleteIC(@RequestParam int ic_id){
        ResultResponse res=new ResultResponse();
        boolean result=icService.deleteIC(ic_id);
        if(result){
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"删除失败，请刷新后重试");
        }
        return res;
    }

    @PostMapping("/updateIC")
    public ResultResponse updateIC(@RequestBody ImgCol imgCol ){
        ResultResponse res=new ResultResponse();

        boolean result=icService.updateIC(imgCol);
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
