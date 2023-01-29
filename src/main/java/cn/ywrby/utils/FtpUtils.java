package cn.ywrby.utils;

import cn.hutool.log.Log;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import java.io.IOException;


public class FtpUtils {
    protected final static Logger logger = Logger.getLogger(FtpUtils.class);

    public static FTPClient getFTPClient() throws IOException {
        //创建ftp客户端
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("UTF-8");
        //链接ftp服务器
        ftpClient.connect("192.168.0.105", 21);
        // 修改连接模式 服务器需要使用被动模式
        ftpClient.enterLocalPassiveMode();
        //登录ftp
        ftpClient.login("ywrby", "renboyu010214");
        int  reply = ftpClient.getReplyCode();
        //如果reply返回230就算成功了，如果返回530密码用户名错误或当前用户无权限下面有详细的解释。
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new IOException( "ftp连接失败");
        }
        return ftpClient;
    }


    /**
     * 关闭FTP方法
     * @param ftp
     * @return
     */
    public static void closeFTP(FTPClient ftp){
        try {
            ftp.logout();
        } catch (Exception e) {
            logger.error("FTP关闭失败");

        }finally{
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    logger.error("FTP关闭失败");
                }
            }
        }
    }


}
