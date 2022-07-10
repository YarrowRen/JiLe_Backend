package cn.ywrby.mapper;

import cn.ywrby.domain.ImgCol;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IcMapper {
    int addIc(ImgCol imgCol);
}
