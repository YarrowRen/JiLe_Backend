package cn.ywrby.utils;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;

public class ImageUtils {

    public static String PIC_Compression(String pic_path,String out_path,int ic_id) {
        File file=new File(pic_path);
        try {
            //定义保存规则 输出文件夹规则
            String fileName=out_path+"\\"+"image"+"\\"+ic_id+"\\"+ic_id+"-"+System.currentTimeMillis()+"-"+file.getName();

            //如果不存在指定文件夹则创建
            File temp_file = new File(fileName);
            if (!temp_file.getParentFile().exists()) {
                temp_file.getParentFile().mkdirs();
            }

            Thumbnails.of(file)
                    .size(Constants.COMPR_WIDTH,Constants.COMPR_HEIGHT)
                    .toFile(fileName);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
