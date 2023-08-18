package cn.ywrby.controller;

import cn.ywrby.domain.Image;
import cn.ywrby.domain.ImgCol;
import cn.ywrby.service.IcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    IcService icService;

    @GetMapping("/image")
    public ResponseEntity<Resource> getImageURL(@RequestParam Integer imageID) throws IOException {
        Image image = icService.getImageDetails(imageID);
        String imagePath=image.getImagePath();
        // 构建图片文件对象
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            // 文件不存在时返回 404
            return ResponseEntity.notFound().build();
        }

        // 构建文件系统资源
        Resource resource = new FileSystemResource(imageFile);

        // 返回图片资源
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // 根据图片类型设置 MediaType
                .body(resource);
    }

    @GetMapping("/thumb")
    public ResponseEntity<Resource> getThumbImageURL(@RequestParam Integer imageID) throws IOException {
        Image image = icService.getImageDetails(imageID);
        String imagePath=image.getThumbnail();
        // 构建图片文件对象
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            // 文件不存在时返回 404
            return ResponseEntity.notFound().build();
        }

        // 构建文件系统资源
        Resource resource = new FileSystemResource(imageFile);

        // 返回图片资源
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // 根据图片类型设置 MediaType
                .body(resource);
    }

    @GetMapping("/cover")
    public ResponseEntity<Resource> getCoverURL(@RequestParam Integer ic_id) throws IOException {
        int defaultPage=1;
        int defaultSize=1;
        ImgCol imgCol = icService.getIcByID(ic_id,defaultPage,defaultSize);
        String coverPath=imgCol.getIc_cover();
        // 构建图片文件对象
        File imageFile = new File(coverPath);
        if (!imageFile.exists()) {
            // 文件不存在时返回 404
            return ResponseEntity.notFound().build();
        }

        // 构建文件系统资源
        Resource resource = new FileSystemResource(imageFile);

        // 返回图片资源
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // 根据图片类型设置 MediaType
                .body(resource);
    }
}