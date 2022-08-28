package cn.ywrby.mapper;

import cn.ywrby.domain.ImgCol;
import cn.ywrby.domain.VideoCol;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VcMapper {
    int addVc(VideoCol videoCol);
    List<VideoCol> getVc();

    VideoCol getFirstVC();
}
