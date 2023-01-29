package cn.ywrby.mapper;

import cn.ywrby.domain.Image;
import cn.ywrby.domain.ImgCol;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IcMapper {

    int addIc(ImgCol imgCol);

    public List<ImgCol> getIc();

    String getIcPathByID(int icID);

    List<Image> getIcByID(int icID);

    void deleteImage(int imageID);

    void addImage(Image image);

    ImgCol getImgCol(Integer ic_id);

    List<Image> getICImageInfo(Integer ic_id);
}
