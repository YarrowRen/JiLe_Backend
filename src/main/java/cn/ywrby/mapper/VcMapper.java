package cn.ywrby.mapper;

import cn.ywrby.domain.ImgCol;
import cn.ywrby.domain.Tag;
import cn.ywrby.domain.Video;
import cn.ywrby.domain.VideoCol;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VcMapper {
    int addVc(VideoCol videoCol);
    List<VideoCol> getVc();

    VideoCol getFirstVC();

    List<Video> getFirstVCVideoInfo(int id);

    List<Tag> getVideoTag(int videoId);

    String getVcPathByID(int vcID);

    List<Video> getVcByID(int vcID);

    void deleteVideo(int videoID);

    int addVideo(Video video);
}
