package cn.ywrby.service.impl;

import cn.ywrby.domain.*;
import cn.ywrby.mapper.VcMapper;
import cn.ywrby.service.VcService;
import cn.ywrby.utils.Constants;
import cn.ywrby.utils.FileUtils;
import cn.ywrby.utils.VideoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class VcServiceImpl implements VcService {

    String[] CommonVideoLastName={"mp4","flv","f4v","webm","m4v","mov","rm","rmvb","wmv","avi","mpg","mpeg","ts","vob"};

    @Autowired
    VcMapper vcMapper;


    @Override
    public boolean copyFile(String source, String dest) {
        File sourceFile=new File(source);
        File destFile=new File(dest);
        //如果不存在指定文件夹则创建
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        try {
            FileUtils.copyFileUsingChannel(sourceFile,destFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public int addVc(VideoCol videoCol) {
//        int id=vcMapper.addVc(videoCol);

        int id=0;
        //判断用户是否上传了封面图
        if(videoCol.getVc_cover()!=null && !Objects.equals(videoCol.getVc_cover(), "")){
            //首先将封面图复制到缓存文件夹，并按缓存文件夹存储
            String vc_cover = videoCol.getVc_cover();
            File source=new File(vc_cover);
            String destPath=Constants.VC_COVER_SAVE_PATH+"\\"+System.currentTimeMillis()+"-"+source.getName();
            boolean result = copyFile(vc_cover, destPath);
            if(result){
                //封面图复制成功，重写封面图信息后保存
                videoCol.setVc_cover(destPath);
                id=vcMapper.addVc(videoCol);
            }
        }else {
            //用户未上传封面图 直接添加
            id=vcMapper.addVc(videoCol);
        }
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
    public VideoInfo getSpecifiedVideoCover(Video video,String savePath) {
        String vcPath = vcMapper.getVcPathByID(video.getVc_id());

        File videoFile=new File(vcPath+"\\"+video.getVideoName());
        String outPath=savePath;
        VideoInfo info = VideoUtils.randomGrabberFFmpegImage(videoFile, outPath);

        return info;
    }


    @Override
    public VideoCol getFirstVC() {
        //获取视频合集基本信息
        VideoCol vc=vcMapper.getFirstVC();
        //获取视频合集中详细视频文件信息
        int id = vc.getId();
        List<Video> videoInfo = vcMapper.getFirstVCVideoInfo(id);
        vc.setVideo_list(videoInfo);
        return vc;
    }

    @Override
    public List<Tag> getVideoTag(int videoId) {
        List<Tag> videoTags=vcMapper.getVideoTag(videoId);
        return videoTags;
    }

    @Override
    public boolean refreshVcData(int vcID) {
        //根据vcID获取当前视频合集所在路径
        String vcPath=vcMapper.getVcPathByID(vcID);
        //利用获取到的文件夹路径获取当前路径下的文件
        File file=new File(vcPath);
        String[] list = file.list();
        List<String> videoNameList=new ArrayList<>();
        //判断文件类型
        for(int i=0;i<list.length;i++){
            File file1=new File(vcPath+"/"+list[i]);
            //System.out.println(list[i]+"  "+file1.isFile()+" 后缀： "+VideoUtils.lastName(file1));
            //排除文件夹
            if(file1.isFile()){
                //排除非视频文件
                if(Arrays.asList(CommonVideoLastName).contains(VideoUtils.lastName(file1))){
                    //将所有视频文件名称加入列表中进行数据比对
                    videoNameList.add(file1.getName());
                }
            }
        }
        //先判断数据库中所保存文件信息是否仍然存在，如果存在则不需要重新添加 如果不存在则说明该文件已经修改或删除可以删除该信息
        List<Video> vc = vcMapper.getVcByID(vcID);
        List<Video> vc_delete=new ArrayList<>();

        for (int i=0;i<vc.size();i++){
            if(videoNameList.contains(vc.get(i).getVideoName())){
                videoNameList.remove(vc.get(i).getVideoName());
            }
            else {
                vc_delete.add(vc.get(i));
            }
        }

        //删除已经不存在的视频文件信息
        for (Video video:vc_delete){
            video=vcMapper.getVideo(video.getVideoID());
            vcMapper.deleteVideo(video.getVideoID());
            //删除数据库信息后 删除本地存储的缓存封面图
            try {
                Files.delete(Path.of(video.getVideoCover()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //添加新增视频文件
        for (int i=0;i<videoNameList.size();i++){
            Video video=new Video();
            video.setVc_id(vcID);
            video.setVideoName(videoNameList.get(i));
//            video.setVideoPath(vcPath+"\\"+videoNameList.get(i));
            VideoInfo info = getSpecifiedVideoCover(video, Constants.COVER_SAVE_PATH);
            video.setVideoCover(info.getCoverPath());
            vcMapper.addVideo(video);
        }

        return true;
    }

    @Override
    public Video videoRename(String newName, int videoID) {
        //修改视频文件名主要分为两部分，首先要修改文件在系统中本身名称另一个就是修改数据库中保存的文件名
        //修改视频文件系统名称
        Video video=vcMapper.getVideo(videoID);
        String vcPath = vcMapper.getVcPathByID(video.getVc_id());
        String newNamePath=vcPath+"\\"+newName;
        String oldNamePath=vcPath+"\\"+video.getVideoName();
        //前后文件名一致的情况下直接返回错误
        if(oldNamePath.equals(newNamePath)){
            return null;
        }
        //修改真实文件名
        boolean result = FileUtils.fileRename(oldNamePath, newNamePath);
        //修改成功后修改数据库
        if(result){
            vcMapper.videoRename(newName,videoID);
            //返回修改后的视频文件对象
            return vcMapper.getVideo(videoID);
        }else {
            //修改失败说明存在与新名 同名的文件 返回错误
            return null;
        }
    }

    @Override
    public boolean videoDelete(int videoID) {
        Video video=vcMapper.getVideo(videoID);
        String vc_path=vcMapper.getVcPathByID(video.getVc_id());
        String filePath=vc_path+"\\"+video.getVideoName();

        File file=new File(filePath);
        //删除系统中视频文件
        file.delete();
        //删除数据库中视频文件
        vcMapper.deleteVideo(videoID);
        return true;
    }

    @Override
    public void editVideoCover(int videoID, String coverPath) {

        Video rowVideo=vcMapper.getVideo(videoID);
        String rowCover=rowVideo.getVideoCover();

        if(!coverPath.equals(rowCover)){
            //封面图发生变化，复制封面图到缓存文件夹并删除原封面
            //首先将封面图复制到缓存文件夹，并按缓存文件夹存储
            boolean result=false;
            File source=new File(coverPath);
            String destPath=Constants.COVER_SAVE_PATH+"\\"+System.currentTimeMillis()+"-"+source.getName();
            if(coverPath==null||coverPath.equals("")){
                //传回的封面为空，不需要复制，只需要删除原封面
                if(rowCover!=null && !Objects.equals(rowCover, "")){
                    //原数据中保存了封面图，所以要删除该封面图文件
                    File temp=new File(rowCover);
                    temp.delete();
                }
                //存入新封面图
                vcMapper.updateVideoCover(videoID,null);
            }else {
                result = copyFile(coverPath, destPath);

                if(result){
                    //封面图复制成功，判断原数据是否存在封面图 如果有则删除
                    if(rowCover!=null && !Objects.equals(rowCover, "")){
                        //原数据中保存了封面图，所以要删除该封面图文件
                        File temp=new File(rowCover);
                        temp.delete();
                    }
                    //存入新封面图
                    vcMapper.updateVideoCover(videoID,destPath);
                }
            }
        }
    }

    @Override
    public Video autoGetCover(int videoID) {
        Video video = vcMapper.getVideo(videoID);

        //获取视频封面并修改数据库
        VideoInfo videoInfo = getSpecifiedVideoCover(video, Constants.COVER_SAVE_PATH);
        String coverPath = videoInfo.getCoverPath();
        vcMapper.updateVideoCover(videoID,coverPath);
        //重新获取视频信息
        video=vcMapper.getVideo(videoID);
        return video;
    }

    @Override
    public Video getVideoDetails(int videoID) {
        Video video=vcMapper.getVideo(videoID);
        String vcPath = vcMapper.getVcPathByID(video.getVc_id());
        video.setVideoPath(vcPath+"\\"+video.getVideoName());
        List<Tag> videoTag = vcMapper.getVideoTag(videoID);
        video.setTags(videoTag);
        List<Person> personList=vcMapper.getVideoPersonList(videoID);
        video.setPersonList(personList);
        return video;
    }

    @Override
    public boolean updateVideoDetails(Video video) {
        //逐个修改表单内容
        //先处理位于video_info表内的所有信息
        //判断封面图是否更新
        Video rowVideo=vcMapper.getVideo(video.getVideoID());
        String cover=video.getVideoCover();
        String rowCover=rowVideo.getVideoCover();
        if(!cover.equals(rowCover)){
            //封面图发生变化，复制封面图到缓存文件夹并删除原封面
            //首先将封面图复制到缓存文件夹，并按缓存文件夹存储
            boolean result=false;
            File source=new File(cover);
            String destPath=Constants.COVER_SAVE_PATH+"\\"+System.currentTimeMillis()+"-"+source.getName();
            result = copyFile(cover, destPath);
            if(result){
                //封面图复制成功，判断原数据是否存在封面图 如果有则删除
                if(rowCover!=null && !Objects.equals(rowCover, "")){
                    //原数据中保存了封面图，所以要删除该封面图文件
                    File temp=new File(rowCover);
                    temp.delete();
                }
                //存入新封面图
                video.setVideoCover(destPath);
            }else {
                return false;
            }
        }
        vcMapper.updateVideoInfo(video);
        //再处理角色信息内容
        //首先清空对应视频的人物关系表
        vcMapper.deletePersonRoleByVideoID(video.getVideoID());
        List<Person> personList=video.getPersonList();
        for (int i=0;i<personList.size();i++){
            Person person=personList.get(i);
            //首先要判断角色信息是否存在还是用户新增的角色
            int result = vcMapper.havePerson(person.getName());
            if(result==0){
                //如果返回值等于0说明数据库中不存在指定人物，需要先创建人物再添加人物与视频关联
                vcMapper.addPerson(person);
            }else {
                //如果返回值不等于0说明数据库中已经存在指定人物并且返回值就是人物ID 不需要进行其他处理
                person.setId(result);
            }
            //处理person_role
            vcMapper.addPersonRole(video.getVideoID(),person.getId(),person.getRoleID());
        }

        //最后处理视频tag信息
        //首先清空视频所有关联tag
        vcMapper.deleteVideoTagByVideoID(video.getVideoID());
        List<Tag> tagList=video.getTags();
        //然后判断tag是否已经创建
        for (int i=0;i<tagList.size();i++){
            Tag tag=tagList.get(i);
            int result = vcMapper.haveTag(tag.getTag_name());
            if(result==0){
                //tag不存在 首先创建tag
                vcMapper.addTag(tag);
            }else {
                //tag存在 结果值就是tagID
                tag.setId(result);
            }
            //处理video_tag
            System.out.println(video.getVideoID()+" : "+tag.getId());
            vcMapper.addVideoTag(video.getVideoID(),tag.getId());
        }

        return true;
    }

    @Override
    public void changeFollowedState(int videoID) {
        vcMapper.changeFollowedState(videoID);
    }

    @Override
    public VideoInfo getVideoMediaInfo(int videoID) {

        Video video = vcMapper.getVideo(videoID);
        String vcPath = vcMapper.getVcPathByID(video.getVc_id());
        String videoPath=vcPath+"\\"+video.getVideoName();

        VideoUtils utils=new VideoUtils();

        File file=new File(videoPath);

        VideoInfo videoInfo = utils.getVideoInfo(file);

        return videoInfo;
    }

    @Override
    public VideoCol getVcByID(Integer vc_id, Integer page, Integer pageSize) {
        //获取视频合集基本信息
        VideoCol vc=vcMapper.getVideoCol(vc_id);

        //获取分页插件对象
        PageHelper pageHelper=new PageHelper();
        //开始分页，指定分页参数
        PageMethod.startPage(page,pageSize);

        List<Video> videoInfo = vcMapper.getVCVideoInfo(vc_id);

        //获取分页信息
        PageInfo<Video> info=new PageInfo<Video>(videoInfo);
        //存入分页信息
        vc.setVc_info(info);
        return vc;
    }

    @Override
    public List<Video> getRandomVideo(int num) {
        List<Video> videoList=vcMapper.getRandomVideo(num);
        //由于是随机获取电子书，所以需要为每本电子书填入文件路径
        for(Video video:videoList){
            int vc_id = video.getVc_id();
            String vcPath = vcMapper.getVcPathByID(vc_id);
            video.setVideoPath(vcPath+"\\"+video.getVideoName());
        }
        return videoList;
    }

    @Override
    public boolean deleteVC(int vc_id) {
        //删除VC前首先删除其封面图缓存文件
        VideoCol rawVC = vcMapper.getVideoCol(vc_id);
        if(rawVC.getVc_cover()!=null && !Objects.equals(rawVC.getVc_cover(), "")){
            File source=new File(rawVC.getVc_cover());
            source.delete();
        }
        vcMapper.deleteVC(vc_id);
        return true;
    }

    @Override
    public boolean updateVC(VideoCol videoCol) {
        //判断用户是否上传了封面图
        if(videoCol.getVc_cover()!=null && !Objects.equals(videoCol.getVc_cover(), "")){
            VideoCol rawVC = vcMapper.getVideoCol(videoCol.getId());
            String vc_cover = videoCol.getVc_cover();
            String rowCover=rawVC.getVc_cover();
            boolean result=false;
            //判断原封面和新封面是否相同（没有更换封面）
            if(!vc_cover.equals(rowCover)){
                //封面发生变化 需要修改
                //首先将封面图复制到缓存文件夹，并按缓存文件夹存储
                File source=new File(vc_cover);
                String destPath=Constants.VC_COVER_SAVE_PATH+"\\"+System.currentTimeMillis()+"-"+source.getName();
                result = copyFile(vc_cover, destPath);
                if(result){
                    //封面图复制成功，判断原数据是否存在封面图 如果有则删除
                    if(rawVC.getVc_cover()!=null && !Objects.equals(rawVC.getVc_cover(), "")){
                        //原数据中保存了封面图，所以要删除该封面图文件
                        File temp=new File(rawVC.getVc_cover());
                        temp.delete();
                    }
                    //存入新封面图
                    videoCol.setVc_cover(destPath);
                }else {
                    return false;
                }
            }
            vcMapper.updateVC(videoCol);
            return true;
        }
        else {
            //用户未上传封面图 判断之前是否保存了封面图
            VideoCol rawVC = vcMapper.getVideoCol(videoCol.getId());
            if(rawVC.getVc_cover()!=null && !Objects.equals(rawVC.getVc_cover(), "")){
                //原数据中保存了封面图，所以要删除该封面图文件
                File source=new File(rawVC.getVc_cover());
                source.delete();
            }

            vcMapper.updateVC(videoCol);
            return true;
        }
    }

}
