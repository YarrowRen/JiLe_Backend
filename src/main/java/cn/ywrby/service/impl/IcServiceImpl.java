package cn.ywrby.service.impl;

import cn.ywrby.domain.*;
import cn.ywrby.mapper.IcMapper;
import cn.ywrby.service.IcService;
import cn.ywrby.utils.Constants;
import cn.ywrby.utils.FileUtils;
import cn.ywrby.utils.ImageUtils;
import cn.ywrby.utils.VideoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class IcServiceImpl implements IcService {
    String[] CommonImageLastName={"jpg","png","jpeg","gif","bmp","JPG","PNG","JPEG","GIF","BMP"};

    @Autowired
    IcMapper icMapper;


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
    public int addIc(ImgCol imgCol) {

        int id=0;
        //判断用户是否上传了封面图
        if(imgCol.getIc_cover()!=null && !Objects.equals(imgCol.getIc_cover(), "")){
            //首先将封面图复制到缓存文件夹，并按缓存文件夹存储
            String ic_cover = imgCol.getIc_cover();
            File source=new File(ic_cover);
            String destPath=Constants.IC_COVER_SAVE_PATH+"\\"+System.currentTimeMillis()+"-"+source.getName();
            boolean result = copyFile(ic_cover, destPath);
            if(result){
                //封面图复制成功，重写封面图信息后保存
                imgCol.setIc_cover(destPath);
                id=icMapper.addIc(imgCol);
            }
        }else {
            //用户未上传封面图 直接添加
            id=icMapper.addIc(imgCol);
        }
        return id;
    }

    @Override
    public List<ImgCol> getIc() {
        List<ImgCol> icList = icMapper.getIc();

        return icList;
    }

    @Override
    public boolean refreshIcData(int icID) {
        //根据icID获取当前视频合集所在路径
        String icPath=icMapper.getIcPathByID(icID);
        //利用获取到的文件夹路径获取当前路径下的文件
        File file=new File(icPath);
        String[] list = file.list();
        List<String> imageNameList=new ArrayList<>();
        //判断文件类型
        for(int i=0;i<list.length;i++){
            File file1=new File(icPath+"/"+list[i]);
            //排除文件夹
            if(file1.isFile()){
                //排除非图片文件
                if(Arrays.asList(CommonImageLastName).contains(FileUtils.lastName(file1))){
                    //将所有图片文件名称加入列表中进行数据比对
                    imageNameList.add(file1.getName());
                }
            }
        }
        //先判断数据库中所保存文件信息是否仍然存在，如果存在则不需要重新添加 如果不存在则说明该文件已经修改或删除可以删除该信息
        List<Image> ic = icMapper.getIcByID(icID);
        List<Image> ic_delete=new ArrayList<>();

        for (int i=0;i<ic.size();i++){
            if(imageNameList.contains(ic.get(i).getImageName())){
                imageNameList.remove(ic.get(i).getImageName());
            }
            else {
                ic_delete.add(ic.get(i));
            }
        }

        //删除已经不存在的图片文件信息
        for (Image image:ic_delete){
            image=icMapper.getImageByID(image.getImageID());
            icMapper.deleteImage(image.getImageID());
            //删除数据库信息后 删除本地存储的缓存缩略图
            try {
                Files.delete(Path.of(image.getThumbnail()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //添加新增图片文件
        for (int i=0;i<imageNameList.size();i++){
            Image image=new Image();
            image.setIc_id(icID);
            image.setImageName(imageNameList.get(i));
            //生成缩略图，为防止同名文件出现冲突问题，将压缩后文件存入压缩文件夹并根据图集信息创建文件夹保存
            String thumbnail_path = ImageUtils.PIC_Compression(icPath + "\\" + imageNameList.get(i), Constants.THUMBNAIL_SAVE_PATH,icID);
            image.setThumbnail(thumbnail_path);

            icMapper.addImage(image);
        }

        return true;
    }

    @Override
    public ImgCol getIcByID(Integer ic_id, Integer page, Integer pageSize){


        //获取图像合集基本信息
        ImgCol ic=icMapper.getImgCol(ic_id);


        //获取分页插件对象
        PageHelper pageHelper=new PageHelper();
        //开始分页，指定分页参数
        PageMethod.startPage(page,pageSize);

        List<Image> imageInfo = icMapper.getICImageInfo(ic_id);
        String icPath=ic.getIc_path();
        for(Image image: imageInfo){
            image.setImagePath(icPath+"\\"+image.getImageName());
        }

        //获取分页信息
        PageInfo<Image> info=new PageInfo<Image>(imageInfo);
        //存入分页信息
        ic.setIc_info(info);
        return ic;
    }

    @Override
    public List<Tag> getImageTag(int imageID) {
        List<Tag> tagList=icMapper.getImageTag(imageID);
        return tagList;
    }

    @Override
    public boolean updateImageInfo(Image image) {
        //首先更新image_info表单内容
        icMapper.updateImageInfo(image);
        //最后处理图片tag信息
        //首先清空图片所有关联tag
        icMapper.deleteImageTagByImageID(image.getImageID());
        List<Tag> tagList = image.getTags();
        //然后判断Tag是否创建过
        //然后判断tag是否已经创建
        for (int i=0;i<tagList.size();i++){
            Tag tag=tagList.get(i);
            int result = icMapper.haveTag(tag.getTag_name());
            if(result==0){
                //tag不存在 首先创建tag
                icMapper.addTag(tag);
            }else {
                //tag存在 结果值就是tagID
                tag.setId(result);
            }
            //处理video_tag
            icMapper.addImageTag(image.getImageID(),tag.getId());
        }

        return true;

    }

    @Override
    public Image getImageDetails(Integer imageID) {
        //获取image基本信息
        Image image=icMapper.getImageByID(imageID);
        //获取tag列表
        List<Tag> imageTag = icMapper.getImageTag(imageID);
        image.setTags(imageTag);
        //获取imagePath
        String rootPath = icMapper.getIcPathByID(image.getIc_id());
        String imagePath=rootPath+"\\"+image.getImageName();
        image.setImagePath(imagePath);

        return image;
    }

    @Override
    public void changeFollowedState(int imageID) {
        icMapper.changeFollowedState(imageID);
    }

    @Override
    public boolean deleteImage(int imageID) {
        //删除本地文件
        Image image = icMapper.getImageByID(imageID);
        ImgCol ic=icMapper.getImgCol(image.getIc_id());
        File file=new File(icMapper.getIcPathByID(ic.getId())+"\\"+image.getImageName());
        file.delete();
        //删除数据库文件
        icMapper.deleteImage(imageID);
        return true;
    }

    @Override
    public List<Image> getRandomImage(int num) {
        List<Image> imageList=icMapper.getRandomImage(num);
        //由于是随机获取电子书，所以需要为每本电子书填入文件路径
        for(Image image:imageList){
            int ic_id = image.getIc_id();
            String icPath = icMapper.getIcPathByID(ic_id);
            image.setImagePath(icPath+"\\"+image.getImageName());
        }
        return imageList;
    }

    @Override
    public boolean deleteIC(int ic_id) {
        //删除IC前首先删除其封面图缓存文件
        ImgCol rawIC = icMapper.getImgCol(ic_id);
        if(rawIC.getIc_cover()!=null && !Objects.equals(rawIC.getIc_cover(), "")){
            File source=new File(rawIC.getIc_cover());
            source.delete();
        }
        icMapper.deleteIC(ic_id);
        return true;
    }

    @Override
    public boolean updateIC(ImgCol imgCol) {
        //判断用户是否上传了封面图
        if(imgCol.getIc_cover()!=null && !Objects.equals(imgCol.getIc_cover(), "")){
            ImgCol rawIC = icMapper.getImgCol(imgCol.getId());
            String ic_cover = imgCol.getIc_cover();
            String rowCover=rawIC.getIc_cover();
            boolean result=false;
            //判断原封面和新封面是否相同（没有更换封面）
            if(!ic_cover.equals(rowCover)){
                //封面发生变化 需要修改
                //首先将封面图复制到缓存文件夹，并按缓存文件夹存储
                File source=new File(ic_cover);
                String destPath=Constants.IC_COVER_SAVE_PATH+"\\"+System.currentTimeMillis()+"-"+source.getName();
                result = copyFile(ic_cover, destPath);
                if(result){
                    //封面图复制成功，判断原数据是否存在封面图 如果有则删除
                    if(rawIC.getIc_cover()!=null && !Objects.equals(rawIC.getIc_cover(), "")){
                        //原数据中保存了封面图，所以要删除该封面图文件
                        File temp=new File(rawIC.getIc_cover());
                        temp.delete();
                    }
                    //存入新封面图
                    imgCol.setIc_cover(destPath);
                }else {
                    return false;
                }
            }
            icMapper.updateIC(imgCol);
            return true;
        }
        else {
            //用户未上传封面图 判断之前是否保存了封面图
            ImgCol rawIC = icMapper.getImgCol(imgCol.getId());
            if(rawIC.getIc_cover()!=null && !Objects.equals(rawIC.getIc_cover(), "")){
                //原数据中保存了封面图，所以要删除该封面图文件
                File source=new File(rawIC.getIc_cover());
                source.delete();
            }

            icMapper.updateIC(imgCol);
            return true;
        }
    }

}
