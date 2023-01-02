package cn.ywrby.service;

import cn.ywrby.domain.*;

import java.util.List;

public interface VcService {
    int addVc(VideoCol videoCol);

    List<VideoCol> getVc();

    List<VideoInfo> getVideoCover(VideoCol videoCol,String savePath);

    VideoInfo getSpecifiedVideoCover(Video video, String savePath);

    VideoCol getFirstVC();

    List<Tag> getVideoTag(int videoId);

    boolean refreshVcData(int vcID);
}
