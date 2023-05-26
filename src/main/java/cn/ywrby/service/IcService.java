package cn.ywrby.service;

import cn.ywrby.domain.Image;
import cn.ywrby.domain.ImgCol;
import cn.ywrby.domain.Tag;

import java.util.List;

public interface IcService {
    public boolean copyFile(String source, String dest);
    int addIc(ImgCol imgCol);
    List<ImgCol> getIc();
    public boolean refreshIcData(int icID);
    public ImgCol getIcByID(Integer ic_id, Integer page, Integer pageSize);

    List<Tag> getImageTag(int imageID);

    boolean updateImageInfo(Image image);

    Image getImageDetails(Integer imageID);

    void changeFollowedState(int imageID);

    boolean deleteImage(int imageID);

    List<Image> getRandomImage(int num);

    boolean deleteIC(int ic_id);

    boolean updateIC(ImgCol imgCol);
}
