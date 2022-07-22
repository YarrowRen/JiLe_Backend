package cn.ywrby.service.impl;

import cn.ywrby.domain.ImgCol;
import cn.ywrby.mapper.IcMapper;
import cn.ywrby.service.IcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IcServiceImpl implements IcService {

    @Autowired
    IcMapper icMapper;


    @Override
    public int addIc(ImgCol imgCol) {
        int id=icMapper.addIc(imgCol);
        return id;
    }

    @Override
    public List<ImgCol> getIc() {
        List<ImgCol> icList = icMapper.getIc();

        return icList;
    }
}
