package cn.ywrby.controller;

import cn.ywrby.res.ResultResponse;
import cn.ywrby.service.ExeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/exe")
public class ExeController {

    @Autowired
    ExeService exeService;

    /**
     * 启动exe  启动前端Electron程序
     * @param fileName
     * @return
     * @throws IOException
     */
    @PostMapping("/openFrontend")
    public ResultResponse openExe(@RequestParam String fileName){
        ResultResponse res = new ResultResponse();
        try {
            exeService.openExe(fileName);
            res.setMessage("打开成功");
        } catch (IOException | InterruptedException e) {
            res.setMessage("打开失败");
        }
        res.setCode(200);
        res.setData(true);
        return res;
    }
}
