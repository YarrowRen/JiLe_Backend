package cn.ywrby.utils;

import cn.ywrby.domain.VideoInfo;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VideoUtils {

    /**
     * 获取视频信息
     * @param file
     * @return
     */
    public static VideoInfo getVideoInfo(File file) {
        VideoInfo videoInfo = new VideoInfo();
        FFmpegFrameGrabber grabber = null;
        try {
            grabber = new FFmpegFrameGrabber(file);
            // 启动 FFmpeg
            grabber.start();

            // 读取视频帧数
            videoInfo.setLengthInFrames(grabber.getLengthInVideoFrames());

            // 读取视频帧率
            videoInfo.setFrameRate(grabber.getVideoFrameRate());

            // 读取视频秒数
            videoInfo.setDuration(grabber.getLengthInTime() / 1000000.00);

            // 读取视频宽度
            videoInfo.setWidth(grabber.getImageWidth());

            // 读取视频高度
            videoInfo.setHeight(grabber.getImageHeight());


            videoInfo.setAudioChannel(grabber.getAudioChannels());

            videoInfo.setVideoCode(grabber.getVideoCodecName());

            videoInfo.setAudioCode(grabber.getAudioCodecName());
            // String md5 = MD5Util.getMD5ByInputStream(new FileInputStream(file));

            videoInfo.setSampleRate(grabber.getSampleRate());
            return videoInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (grabber != null) {
                    // 此处代码非常重要，如果没有，可能造成 FFmpeg 无法关闭
                    grabber.stop();
                    grabber.release();
                }
            } catch (FFmpegFrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 随机获取视频截图
     * */
    public static VideoInfo randomGrabberFFmpegImage(File videoFile,String outPath) {
        FFmpegFrameGrabber grabber = null;
        VideoInfo info=new VideoInfo();
        info.setVideoName(videoFile.getName());
        try {
            grabber = new FFmpegFrameGrabber(videoFile);
            grabber.start();
            // 获取视频总帧数
            // int lengthInVideoFrames = grabber.getLengthInVideoFrames();
            // 获取视频时长， / 1000000 将单位转换为秒
            long delayedTime = grabber.getLengthInTime() / 1000000;

            Random random = new Random();
                // 跳转到响应时间
                grabber.setTimestamp((random.nextInt((int)delayedTime - 1) + 1) * 1000000L);
                Frame f = grabber.grabImage();
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage bi = converter.getBufferedImage(f);
                //用时间戳代替输出名称保证不出现重复名称现象
                String outName= String.valueOf(System.currentTimeMillis());
                File out = Paths.get(outPath, outName+".jpg").toFile();
                ImageIO.write(bi, "jpg", out);
                info.setCoverPath(outPath+"\\"+outName+".jpg");
            return info;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (grabber != null) {
                    grabber.stop();
                    grabber.release();
                }
            } catch (FFmpegFrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }
    }

    //读文件夹下的所有 文件名字
    public static List<String> getFiles(String path) {
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            //若文件为非文件夹文件
            if (tempList[i].isFile()) {
                files.add(tempList[i].toString());
                //文件名，不包含路径
                //String fileName = tempList[i].getName();
            }
            //若文件是文件夹
            if (tempList[i].isDirectory()) {
                //这里就不递归了，
            }
        }
        return files;
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
