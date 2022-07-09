package cn.ywrby.service.impl;

import cn.ywrby.service.ExeService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ExeServiceImpl implements ExeService {


    @Override
    @Async //异步处理
    public void openExe(String fileName) throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();//启动程序最主要的类 RunTime
        runtime.exec(fileName); //程序所在位置
        System.out.println(fileName);
    }
}
