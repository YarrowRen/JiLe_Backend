package cn.ywrby.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static boolean fileRename(String oldName,String newName) {
        // 旧的文件或目录
        File oldNameFile = new File(oldName);
        // 新的文件或目录
        File newNameFile = new File(newName);
        if (newNameFile.exists()) {  //  确保新的文件名不存在
            return false;
        }
        if(oldNameFile.renameTo(newNameFile)) {
            return true;
        } else {
            return false;
        }
    }

    // split截取后缀名
    public static String lastName(File file){
        if (file == null) return null;
        String filename = file.getName();
        // split用的是正则，所以需要用 //. 来做分隔符
        String[] split = filename.split("\\.");
        //注意判断截取后的数组长度，数组最后一个元素是后缀名
        if (split.length > 1) {
            return split[split.length - 1];
        } else {
            return "";
        }
    }
}
