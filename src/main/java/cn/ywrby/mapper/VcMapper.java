package cn.ywrby.mapper;

import cn.ywrby.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    void updateVideoInfo(Video video);

    //判断是否存在指定人物名
    //使用子查询 如果存在指定人物就返回personID否则返回0
    int havePerson(String personName);

    int addPerson(Person person);

    void deletePersonRoleByVideoID(int videoID);

    void addPersonRole(int videoID, int personID, int roleID);

    void deleteVideoTagByVideoID(int videoID);

    int haveTag(String tag_name);

    int addTag(Tag tag);

    void addVideoTag(int videoID, int tagID);

    void changeFollowedState(int videoID);

    List<Video> getVCVideoInfo(int id);

    VideoCol getVideoCol(Integer vc_id);
}
