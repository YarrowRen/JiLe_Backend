package cn.ywrby.service.impl;

import cn.ywrby.domain.VideoCol;
import cn.ywrby.domain.VideoInfo;
import cn.ywrby.mapper.VcMapper;
import cn.ywrby.service.VcService;
import cn.ywrby.utils.VideoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class VcServiceImpl implements VcService {

    @Autowired
    VcMapper vcMapper;

    @Override
    public int addVc(VideoCol videoCol) {
        int id=vcMapper.addVc(videoCol);
        return id;
    }

    @Override
    public List<VideoCol> getVc() {
        List<VideoCol> vcList = vcMapper.getVc();
        return vcList;
    }

    @Override
    public List<VideoInfo> getVideoCover(VideoCol videoCol,String savePath) {

        List<VideoInfo> videoInfos=new ArrayList<>();

        String path=videoCol.getVc_path();

        List<String> filesList = VideoUtils.getFiles(path);

        for (String filePath:filesList){
            File videoFile=new File(filePath);
            String outPath=savePath;
            VideoInfo info = VideoUtils.randomGrabberFFmpegImage(videoFile, outPath);
            videoInfos.add(info);
        }

        return videoInfos;
    }

    @Override
    public VideoCol getFirstVC() {
        VideoCol vc=vcMapper.getFirstVC();
        return vc;
    }

}
