package cn.ywrby;


import cn.ywrby.domain.Person;
import cn.ywrby.domain.VideoInfo;
import cn.ywrby.mapper.UserMapper;
import cn.ywrby.mapper.VcMapper;
import cn.ywrby.utils.VideoUtils;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class BookStoreDemo2ApplicationTests {


    @Autowired
    UserMapper userMapper;

    @Autowired
    VcMapper vcMapper;



    @Test
    public void testShowDataH2(){
    }

    //读文件夹下的所有 文件名字
    public List<String> getFiles(String path) {
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

    @Test
    public void testInsertH2(){
        VideoUtils utils=new VideoUtils();
        String path= "I:\\JiLeFile\\video";
        String outPath="I:\\JiLeFile\\video\\cover";
        String outName="玫瑰人生封面图";
        List<String> files = getFiles(path);
        for(String file:files){
            System.out.println(file);
        }

//        VideoInfo videoInfo = utils.randomGrabberFFmpegImage(file,outPath);
//        System.out.println(videoInfo.getCoverPath());
    }



    @Test
    public void testPath(){

        List<String> videoNameList=new ArrayList<>();
        videoNameList.add("test1");
        videoNameList.add("test2");
        videoNameList.add("test3");
        videoNameList.add("test4");
        videoNameList.add("test4");
        System.out.println(videoNameList.toString());
        videoNameList.remove("test");
        System.out.println(videoNameList.toString());

    }

    @Test
    public void testRename() throws IOException {
        // 旧的文件或目录
        File oldName = new File("I:\\JiLeFile\\video\\【猫饼】最后雅致2-1080P.mp4");
        // 新的文件或目录
        File newName = new File("I:\\JiLeFile\\video\\【猫饼】最后雅致-1080P.mp4");
        if (newName.exists()) {  //  确保新的文件名不存在
            throw new java.io.IOException("file exists");
        }
        if(oldName.renameTo(newName)) {
            System.out.println("已重命名");
        } else {
            System.out.println("Error");
        }
    }

    @Test
    public void testHavePerson(){
        Person person=new Person();
        person.setName("D1");
        int i = vcMapper.addPerson(person);
        System.out.println("成功："+person.getId());
    }

}
