package cn.ywrby.controller;

import cn.ywrby.domain.ImgCol;
import cn.ywrby.domain.VideoCol;
import cn.ywrby.domain.VideoInfo;
import cn.ywrby.res.ResultResponse;
import cn.ywrby.service.VcService;
import cn.ywrby.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vc")
public class VcController {

    @Autowired
    VcService vcService;

    @PostMapping("/addVc")
    public ResultResponse addVc(@RequestBody VideoCol videoCol){
        System.out.println(videoCol);
        ResultResponse res=new ResultResponse();
        int id=vcService.addVc(videoCol);
        if (id!=0){
            res.setCode(Constants.STATUS_OK);
            res.setData(id);
            res.setMessage("影视集创建成功。");
        }else {
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage("影视集创建失败！");
        }
        return res;
    }

    @GetMapping("/getVc")
    public ResultResponse getVc(){
        ResultResponse res=new ResultResponse();
        List<VideoCol> vcList = vcService.getVc();
        res.setMessage("获取成功");
        res.setData(vcList);
        res.setCode(Constants.STATUS_OK);
        return res;
    }

    @PostMapping("/getVideoCover")
    public ResultResponse getVideoCover(@RequestBody VideoCol videoCol){
        System.out.println(videoCol);
        ResultResponse res=new ResultResponse();
        String savePath=Constants.COVER_SAVE_PATH;
        List<VideoInfo> videoCover = vcService.getVideoCover(videoCol,savePath);
        res.setMessage("获取视频封面成功！");
        res.setData(videoCover);
        res.setCode(Constants.STATUS_OK);

        return res;
    }


    /**
     * 仅作测试用，用来获取第一个VideoCollection
     * @return
     */
    @GetMapping("/getFirstVC")
    public ResultResponse getFirstVC(){
        ResultResponse res=new ResultResponse();
        VideoCol firstVC = vcService.getFirstVC();
        res.setCode(Constants.STATUS_OK);
        res.setMessage("成功获取影视集");
        res.setData(firstVC);
        return res;

    }


    @GetMapping("/refreshVcData")
    public ResultResponse refreshVcData(@RequestParam Integer vcID){
        ResultResponse res=new ResultResponse();

        boolean result=vcService.refreshVcData(vcID);
        if(result){
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }

        return res;
    }
}
