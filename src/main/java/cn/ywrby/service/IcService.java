package cn.ywrby.service;

import cn.ywrby.domain.Image;
import cn.ywrby.domain.ImgCol;
import cn.ywrby.domain.Tag;

import java.util.List;

public interface IcService {
    int addIc(ImgCol imgCol);
    List<ImgCol> getIc();
    public boolean refreshIcData(int icID);
    public ImgCol getIcByID(Integer ic_id, Integer page, Integer pageSize);

    List<Tag> getImageTag(int imageID);

    boolean updateImageInfo(Image image);

    Image getImageDetails(Integer imageID);

    void changeFollowedState(int imageID);
}
