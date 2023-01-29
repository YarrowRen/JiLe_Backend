package cn.ywrby.service;

import cn.ywrby.domain.ImgCol;

import java.util.List;

public interface IcService {
    int addIc(ImgCol imgCol);
    List<ImgCol> getIc();
    public boolean refreshIcData(int icID);
    public ImgCol getIcByID(Integer ic_id, Integer page, Integer pageSize);
}
