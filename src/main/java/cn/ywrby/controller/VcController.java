package cn.ywrby.controller;

import cn.ywrby.domain.ImgCol;
import cn.ywrby.domain.Video;
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

    @PostMapping("/videoRename")
    public ResultResponse videoRename(@RequestParam String newName,@RequestParam int videoID){
        ResultResponse res=new ResultResponse();
        Video video = vcService.videoRename(newName, videoID);
        if(video==null){
            //返回值为空，说明重命名过程中出现错误
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"修改失败，文件名未变化或存在同名文件，请检查后重新命名。");
        }else {
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
            res.setData(video);
        }
        return res;
    }

    @PostMapping("/videoDelete")
    public ResultResponse videoDelete(@RequestParam int videoID){
        ResultResponse res=new ResultResponse();
        boolean result=vcService.videoDelete(videoID);
        if (result){
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"删除失败，请刷新数据库后重试。");
        }
        return res;
    }

    @PostMapping("/editVideoCover")
    public ResultResponse editVideoCover(@RequestParam int videoID,@RequestParam String coverPath){
        ResultResponse res=new ResultResponse();
        vcService.editVideoCover(videoID,coverPath);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;
    }

    @PostMapping("/autoGetCover")
    public ResultResponse autoGetCover(@RequestParam int videoID){
        ResultResponse res=new ResultResponse();
        Video video=vcService.autoGetCover(videoID);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        res.setData(video);
        return res;
    }

    @GetMapping("/getVideoDetails")
    public ResultResponse getVideoDetails(@RequestParam int videoID){
        ResultResponse res=new ResultResponse();
        Video video=vcService.getVideoDetails(videoID);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        res.setData(video);
        return res;
    }

    @PostMapping("/updateVideoDetails")
    public ResultResponse updateVideoDetails(@RequestBody Video video){
        ResultResponse res=new ResultResponse();
        boolean result=vcService.updateVideoDetails(video);
        if(result){
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"修改失败，请检查后重新提交。");
        }

        return res;
    }

    @PostMapping("/changeFollowedState")
    public ResultResponse changeFollowedState(@RequestParam int videoID){
        ResultResponse res=new ResultResponse();
        vcService.changeFollowedState(videoID);

        res.setCode(Constants.STATUS_OK);
        res.setData(videoID);
        return res;
    }

    @GetMapping("/getVideoMediaInfo")
    public ResultResponse getVideoMediaInfo(@RequestParam int videoID){
        ResultResponse res=new ResultResponse();
        VideoInfo info= vcService.getVideoMediaInfo(videoID);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        res.setData(info);

        return res;
    }
}
