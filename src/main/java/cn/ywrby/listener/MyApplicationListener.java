package cn.ywrby.listener;

import cn.ywrby.controller.ExeController;
import cn.ywrby.service.ExeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationListener  {

    @Value("${Electron.path}")
    private String electronPath;


    @Autowired
    ExeController exeController;

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        //exeController.openExe(electronPath);
        System.out.println("接收到事件："+applicationReadyEvent.getClass());
    }
}
