package cn.ywrby.mapper;

import cn.ywrby.domain.ImgCol;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IcMapper {

    int addIc(ImgCol imgCol);

    public List<ImgCol> getIc();
}
