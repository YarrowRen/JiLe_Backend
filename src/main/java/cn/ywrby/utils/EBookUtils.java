package cn.ywrby.utils;

import cn.ywrby.domain.EBook;
import cn.ywrby.domain.Person;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 相关解答：https://blog.csdn.net/ycf921244819/article/details/104734886/
 */
public class EBookUtils {

    /**
     * 使用该方法前应该先进行确认，确认文件是epub文件后再使用
     * @param epubFile
     * @return
     */
    public static EBook getEpubInfo(File epubFile){
        EBook eBook=new EBook();

        InputStream in = null;
        try {
            //从输入流当中读取epub格式文件
            EpubReader reader = new EpubReader();
            in = new FileInputStream(epubFile);
            Book book = reader.readEpub(in);
            //获取到书本的头部信息并存入eBook对象
            Metadata metadata = book.getMetadata();

            eBook.setEBookTitle(metadata.getFirstTitle());
            eBook.setLanguage(metadata.getLanguage());
            eBook.setFormat(metadata.getFormat());
            //处理作者信息
            List<Author> authors = metadata.getAuthors();
            List<Person> authorList=new ArrayList<>();
            //遍历生成角色ID为作者的Person对象后再插入集合
            for (int i=0;i<authors.size();i++){
                Person person=new Person();
                person.setName(authors.get(i).getFirstname()+authors.get(i).getLastname());
                person.setRoleID(Constants.AUTHOR_E_ROLE_ID);
                authorList.add(person);
            }
            eBook.setAuthorList(authorList);

            if(metadata.getIdentifiers().size()!=0){
                eBook.setIdentifier(metadata.getIdentifiers().get(0).toString());
            }
            if(metadata.getPublishers().size()!=0){
                eBook.setPublisher(metadata.getPublishers().get(0));
            }
            //获取到书本的页数
            List<Resource> contents = book.getContents();
            eBook.setPages(contents.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //一定要关闭资源
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return eBook;
    }


    /**
     * 获取压缩文件（zip或cbz）的第一张图片作为电子书封面
     * @param filePath 压缩文件路径（包含文件名）
     * @Param savePath 保存路径（不包含文件名）
     * @return 保存后的封面文件路径
     * @throws IOException
     */
    public static String getZipCover(String filePath, String savePath) throws IOException {

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
                    //储存文件时插入时间戳避免名称重复
                    fileName=savePath+"\\"+System.currentTimeMillis()+"-"+name;
                    //如果不存在指定文件夹则创建
                    File f1 = new File(fileName);
                    if (!f1.getParentFile().exists()) {
                        f1.getParentFile().mkdirs();
                    }
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


    public static void main(String[] args) {
//        File file = new File("I:\\JiLeFile\\book\\[tsdm@夜空][輝夜姬想讓人告白_天才們的戀愛]卷01.epub");
//        String epubCover = getEpubCover(file, "D:\\photo\\data\\boostore");
//        System.out.println(epubCover);

    }

    public static String getEpubCover(File epubFile, String savePath) {
        String epubCover=null;
        InputStream in = null;
        try {
            //从输入流当中读取epub格式文件
            EpubReader reader = new EpubReader();
            in = new FileInputStream(epubFile);
            Book book = reader.readEpub(in);
            //获取到书本的头部信息并存入eBook对象
            Metadata metadata = book.getMetadata();

            //存储封面图
            Resource coverImage = book.getCoverImage();
            if(coverImage!=null){
                byte[] coverByte = coverImage.getData();
                String fileName=metadata.getFirstTitle()+"-"+System.currentTimeMillis()+coverImage.getMediaType().getDefaultExtension();
                //将byte数组转为图片文件并保存到缓存文件夹作为封面路径
                FileUtils.bytesToFile(coverByte,savePath,fileName);
                epubCover=savePath+"\\"+fileName;
            }else {
                epubCover=null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //一定要关闭资源
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return epubCover;
    }
}
