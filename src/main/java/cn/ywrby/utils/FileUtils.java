package cn.ywrby.utils;


import de.androidpit.colorthief.ColorThief;
import de.androidpit.colorthief.MMCQ;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    public static String lastName(String fileName){
        // split用的是正则，所以需要用 //. 来做分隔符
        String[] split = fileName.split("\\.");
        //注意判断截取后的数组长度，数组最后一个元素是后缀名
        if (split.length > 1) {
            return split[split.length - 1];
        } else {
            return "";
        }
    }

    /**
     * 将Byte数组转换成文件
     * @param bytes byte数组
     * @param filePath 文件路径
     * @param fileName  文件名
     */
    public static void bytesToFile(byte[] bytes, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {

            file = new File(filePath +"\\"+ fileName);
            if (!file.getParentFile().exists()){
                //文件夹不存在 生成
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 不解压情况下遍历压缩文件夹（ZIP或CBZ）
     * @param filePath
     * @return
     * @throws IOException
     */
    public static List<String> traverseZip(String filePath) throws IOException {
        List<String> fileNameList=new ArrayList<>();

        //获取文件输入流
        FileInputStream input = new FileInputStream(filePath);

        //获取ZIP输入流(一定要指定字符集Charset.forName("GBK")否则会报java.lang.IllegalArgumentException: MALFORMED)
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(input), Charset.forName("GBK"));

        //定义ZipEntry置为null,避免由于重复调用zipInputStream.getNextEntry造成的不必要的问题
        ZipEntry ze = null;

        //循环遍历
        while ((ze = zipInputStream.getNextEntry()) != null) {
            if(!ze.isDirectory()) {
//                System.out.println(String.format("文件名：" + ze.getName() + " 文件大小：" + ze.getSize() + " bytes", Charset.forName("GBK")));
                fileNameList.add(String.format(ze.getName(), Charset.forName("GBK")));
            }
        }

        //一定记得关闭流
        zipInputStream.closeEntry();
        input.close();

        return fileNameList;
    }


    /**
     * 获取压缩文件（zip或cbz）的第一张图片作为电子书封面
     * @param filePath 压缩文件路径（包含文件名）
     * @Param savePath 保存路径（不包含文件名）
     * @return 封面在压缩文件中的相对路径
     * @throws IOException
     */
    public static String getZipCover(String filePath,String savePath) throws IOException {
        String[] CommonImageLastName={"jpg","png","jpeg","gif","bmp","JPG","PNG","JPEG","GIF","BMP"};
        String fileName=null;
        //获取文件输入流
        FileInputStream input = new FileInputStream(filePath);

        //获取ZIP输入流(一定要指定字符集Charset.forName("GBK")否则会报java.lang.IllegalArgumentException: MALFORMED)
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(input), Charset.forName("GBK"));

        //定义ZipEntry置为null,避免由于重复调用zipInputStream.getNextEntry造成的不必要的问题
        ZipEntry ze = null;

        //循环遍历
        while ((ze = zipInputStream.getNextEntry()) != null) {
            if(!ze.isDirectory()) {
//                System.out.println(String.format("文件名：" + ze.getName() + " 文件大小：" + ze.getSize() + " bytes", Charset.forName("GBK")));
                String temp_fileName=String.format(ze.getName(), Charset.forName("GBK"));
                String lastName = FileUtils.lastName(temp_fileName);
                //遍历直到找到第一个图片文件设为封面图
                if(Arrays.asList(CommonImageLastName).contains(lastName)) {
                    //保存这张图片作为封面
                    File tempFile=new File(temp_fileName);
                    String name = tempFile.getName();
                    fileName=savePath+"\\"+name;
                    FileOutputStream fileOutputStream = new FileOutputStream(fileName);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);int n;
                    byte[] bytes = new byte[1024];
                    while ((n = zipInputStream.read(bytes)) != -1) {
                        bufferedOutputStream.write(bytes, 0, n);
                    }
                    //关闭流
                    bufferedOutputStream.close();
                    fileOutputStream.close();
                    break;
                }
            }
        }


        //一定记得关闭流
        zipInputStream.closeEntry();
        input.close();
        return fileName;
    }

    /**
     * 去掉文件名的扩展名
     * @param filename
     * @return
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 复制文件
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFileUsingChannel(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }finally{
            sourceChannel.close();
            destChannel.close();
        }
    }



    /**
     * 获取图片主色调
     * @param pathname 文件路径
     * @return
     * @throws IOException
     */
    public static int[][] getMainColor(String pathname,int colorCount) throws IOException {
        BufferedImage img = ImageIO.read(new File(pathname));

        MMCQ.CMap result = ColorThief.getColorMap(img, colorCount);
        int[][] colorMap=new int[colorCount][3];
        for(int i=0;i<result.vboxes.size();i++){
            MMCQ.VBox vBox=result.vboxes.get(i);
            int[] rgb = vBox.avg(false);
            colorMap[i]=rgb;
        }
        return colorMap;
    }

    public static void main(String[] args) throws IOException {
        long startTime=System.currentTimeMillis(); //获取开始时间
        int[][] mainColor = getMainColor("I:\\JiLeFile\\test1\\01.jpg", 5);
        for (int[] rgb:mainColor){
            System.out.println(rgb[0]+":"+rgb[1]+":"+rgb[2]);
        }
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");

    }
}
