package cn.ywrby.service.impl;

import cn.ywrby.domain.Tag;
import cn.ywrby.domain.Video;
import cn.ywrby.domain.VideoCol;
import cn.ywrby.domain.VideoInfo;
import cn.ywrby.mapper.VcMapper;
import cn.ywrby.service.VcService;
import cn.ywrby.utils.Constants;
import cn.ywrby.utils.VideoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class VcServiceImpl implements VcService {

    String[] CommonVideoLastName={"mp4","flv","f4v","webm","m4v","mov","rm","rmvb","wmv","avi","mpg","mpeg","ts","vob"};

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
            vcMapper.deleteVideo(video.getVideoID());
        }

        //添加新增视频文件
        for (int i=0;i<videoNameList.size();i++){
            Video video=new Video();
            video.setVc_id(vcID);
            video.setVideoName(videoNameList.get(i));
            video.setVideoPath(vcPath+"\\"+videoNameList.get(i));
            VideoInfo info = getSpecifiedVideoCover(video, Constants.COVER_SAVE_PATH);
            video.setVideoCover(info.getCoverPath());
            vcMapper.addVideo(video);
        }

        return true;
    }

}