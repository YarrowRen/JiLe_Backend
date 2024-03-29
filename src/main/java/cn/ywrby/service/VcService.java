package cn.ywrby.service;

import cn.ywrby.domain.*;

import java.util.List;

public interface VcService {

    public boolean copyFile(String source, String dest);
    int addVc(VideoCol videoCol);

    List<VideoCol> getVc();

    List<VideoInfo> getVideoCover(VideoCol videoCol,String savePath);

    VideoInfo getSpecifiedVideoCover(Video video, String savePath);

    VideoCol getFirstVC();

    List<Tag> getVideoTag(int videoId);

    boolean refreshVcData(int vcID);

    Video videoRename(String newName,int videoID);

    boolean videoDelete(int videoID);

    void editVideoCover(int videoID, String coverPath);

    Video autoGetCover(int videoID);

    Video getVideoDetails(int videoID);

    boolean updateVideoDetails(Video video);

    void changeFollowedState(int videoID);

    VideoInfo getVideoMediaInfo(int videoID);

    VideoCol getVcByID(Integer vc_id, Integer page, Integer pageSize);

    List<Video> getRandomVideo(int num);

    boolean deleteVC(int vc_id);

    boolean updateVC(VideoCol videoCol);
}
