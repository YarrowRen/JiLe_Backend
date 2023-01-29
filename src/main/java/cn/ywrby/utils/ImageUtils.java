package cn.ywrby.utils;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;

public class ImageUtils {

    public static String PIC_Compression(String pic_path,String out_path,int ic_id) {
        File file=new File(pic_path);
        try {
            String fileName=out_path+"\\"+ic_id+"-"+System.currentTimeMillis()+"-"+file.getName();
            Thumbnails.of(file)
                    .size(Constants.COMPR_WIDTH,Constants.COMPR_HEIGHT)
                    .toFile(fileName);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
