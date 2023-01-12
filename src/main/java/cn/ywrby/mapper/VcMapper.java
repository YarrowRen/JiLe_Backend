package cn.ywrby.mapper;

import cn.ywrby.domain.*;
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

    Video getVideo(int videoID);

    void videoRename(String newName, int videoID);

    void updateVideoCover(int videoID, String coverPath);

    List<Person> getVideoPersonList(int videoID);
}
