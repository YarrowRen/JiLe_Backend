package cn.ywrby.service;

import cn.ywrby.domain.ImgCol;
import cn.ywrby.domain.VideoCol;
import cn.ywrby.domain.VideoInfo;

import java.util.List;

public interface VcService {
    int addVc(VideoCol videoCol);

    List<VideoCol> getVc();

    List<VideoInfo> getVideoCover(VideoCol videoCol,String savePath);

    VideoCol getFirstVC();
}
