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
}
