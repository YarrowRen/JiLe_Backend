package cn.ywrby.service.impl;

import cn.ywrby.domain.*;
import cn.ywrby.mapper.EcMapper;
import cn.ywrby.service.EcService;
import cn.ywrby.utils.Constants;
import cn.ywrby.utils.EBookUtils;
import cn.ywrby.utils.FileUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EcServiceImpl implements EcService {
    String[] CommonEBookLastName={"zip","cbz","epub","ZIP","CBZ","EPUB"};
    String[] WithoutInfoLastName={"zip","cbz","ZIP","CBZ"};

    @Autowired
    EcMapper ecMapper;

    @Override
    public boolean refreshEcData(Integer ecID) {
        //根据ecID获取当前合集所在路径
        String ecPath=ecMapper.getEcPathByID(ecID);
        //利用获取到的文件夹路径获取当前路径下的所有文件
        File file=new File(ecPath);
        String[] list = file.list();
        List<String> eBookNameList=new ArrayList<>();
        //判断文件类型
        for(int i=0;i<list.length;i++){
            File file1=new File(ecPath+"/"+list[i]);
            //排除文件夹
            if(file1.isFile()){
                //排除非电子书文件
                if(Arrays.asList(CommonEBookLastName).contains(FileUtils.lastName(file1))){
                    //将所有电子书文件名称加入列表中进行数据比对
                    eBookNameList.add(file1.getName());
                }
            }
        }
        //先判断数据库中所保存文件信息是否仍然存在，如果存在则不需要重新添加 如果不存在则说明该文件已经修改或删除可以删除该信息
        List<EBook> ec = ecMapper.getEcByID(ecID);
        List<EBook> ec_delete=new ArrayList<>();

        for (int i=0;i<ec.size();i++){
            if(eBookNameList.contains(ec.get(i).getEBookName())){
                eBookNameList.remove(ec.get(i).getEBookName());
            }
            else {
                ec_delete.add(ec.get(i));
            }
        }

        //删除已经不存在的电子书文件信息
        for (EBook eBook:ec_delete){
            eBook= ecMapper.getEBookByID(eBook.getEBookID());
            ecMapper.deleteEBook(eBook.getEBookID());
            //删除数据库信息后 删除本地存储的缓存封面图
            try {
                Files.delete(Path.of(eBook.getCoverPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //添加新增电子书文件
        for (int i=0;i<eBookNameList.size();i++){
            EBook eBook=new EBook();
            eBook.setEc_id(ecID);
            eBook.setEBookName(eBookNameList.get(i));
            /**
             * 判断电子书类型（zip,cbz,epub）
             * 对于zip,cbz这类不保存电子书基本信息的文件格式不做进一步处理
             * 对于epub这种可以保存电子书基本信息的文件进行进一步信息的获取，并在插入时进行保存
             */

            File file1=new File(ecPath+"/"+eBook.getEBookName());
            String savePath=Constants.THUMBNAIL_SAVE_PATH+"\\"+"eBook"+"\\"+eBook.getEc_id();
            if(Arrays.asList(WithoutInfoLastName).contains(FileUtils.lastName(file1))){
                /**
                 * 为zip或cbz格式
                 * 遍历压缩文件内容 直到获得压缩文件中的第一个图片文件作为电子书封面
                 * 如果整个压缩文件内不存在图片，则不设置封面
                 */
                try {
                    String zipCover = EBookUtils.getZipCover(file1.getPath(), savePath);
                    eBook.setCoverPath(zipCover);
                    //存储后缀(全部大写)
                    eBook.setExtension(FileUtils.lastName(file1).toUpperCase());
                    //存储电子书标题
                    eBook.setEBookTitle(FileUtils.getFileNameNoEx(file1.getName()));

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }else {
                /**
                 * epub格式文件，存储了基本文件信息 先获取文件信息
                 */
                EBook epubBookInfo = EBookUtils.getEpubInfo(file1);
                //逐个插入保存数据
                eBook.setEBookTitle(epubBookInfo.getEBookTitle());
                eBook.setLanguage(epubBookInfo.getLanguage());
                eBook.setFormat(epubBookInfo.getFormat());
                eBook.setPublishDate(epubBookInfo.getPublishDate());
                eBook.setIdentifier(epubBookInfo.getIdentifier());
                eBook.setPublisher(epubBookInfo.getPublisher());
                eBook.setPages(epubBookInfo.getPages());
                eBook.setAuthorList(epubBookInfo.getAuthorList());

                //保存封面图
                String epubCover=EBookUtils.getEpubCover(file1,savePath);
                eBook.setCoverPath(epubCover);
                eBook.setExtension("EPUB");
            }

            ecMapper.addEBook(eBook);
            /**
             * 由于作者信息需要进行电子书与作者的关联，所以必须在保存电子书信息后单独处理
             */
            List<Person> authorList = eBook.getAuthorList();
            if(authorList!=null) {
                for (int j = 0; j < authorList.size(); j++) {
                    Person person = authorList.get(j);
                    //首先要判断角色信息是否存在还是用户新增的角色
                    int result = ecMapper.havePerson(person.getName());
                    if (result == 0) {
                        //如果返回值等于0说明数据库中不存在指定人物，需要先创建人物再添加人物与视频关联
                        ecMapper.addPerson(person);
                    } else {
                        //如果返回值不等于0说明数据库中已经存在指定人物并且返回值就是人物ID 不需要进行其他处理
                        person.setId(result);
                    }
                    //处理person_role
                    ecMapper.addEBookAuthor(eBook.getEBookID(), person.getId());
                }
            }
        }

        return true;
    }

    @Override
    public List<EBookCol> getEc() {
        List<EBookCol> eBookColList=ecMapper.getEc();
        return eBookColList;
    }

    @Override
    public int addEc(EBookCol eBookCol) {
        int id=ecMapper.addEc(eBookCol);
        return id;
    }

    @Override
    public EBookCol getEcByID(Integer ec_id, Integer page, Integer pageSize) {

        //获取书库基本信息
        EBookCol ec=ecMapper.getEBookCol(ec_id);


        //获取分页插件对象
        PageHelper pageHelper=new PageHelper();
        //开始分页，指定分页参数
        PageMethod.startPage(page,pageSize);

        List<EBook> EBookList = ecMapper.getECEBookInfo(ec_id);
        //存入作者信息(后续存入Tag也在该循坏下加入)
        for(int i = 0; i< EBookList.size(); i++){
            EBook eBook= EBookList.get(i);
            List<Person> authorList=ecMapper.getAuthorsByEBookID(eBook.getEBookID());
            eBook.setAuthorList(authorList);
            List<Tag> tagList=ecMapper.getTagsByEBookID(eBook.getEBookID());
            eBook.setTags(tagList);

            EBookList.set(i,eBook);
        }

        //获取分页信息
        PageInfo<EBook> info=new PageInfo<EBook>(EBookList);
        //存入分页信息
        ec.setEc_info(info);
        return ec;
    }

    @Override
    public boolean updateEBookDetails(EBook eBook) {
        //逐个修改表单内容
        //先处理位于ebook_info表内的所有信息
        ecMapper.updateEBookInfo(eBook);
        //再处理作者信息内容
        //首先清空对应书籍的作者关系表
        ecMapper.deleteEBookAuthorByEBookID(eBook.getEBookID());
        List<Person> authorList=eBook.getAuthorList();
        for (int i=0;i<authorList.size();i++){
            Person author=authorList.get(i);
            //首先要判断角色信息是否存在还是用户新增的角色
            int result = ecMapper.havePerson(author.getName());
            if(result==0){
                //如果返回值等于0说明数据库中不存在指定人物，需要先创建人物再添加人物与视频关联
                ecMapper.addPerson(author);
            }else {
                //如果返回值不等于0说明数据库中已经存在指定人物并且返回值就是人物ID 不需要进行其他处理
                author.setId(result);
            }
            //处理person_role
            ecMapper.addEBookAuthor(eBook.getEBookID(),author.getId());
        }

        //最后处理书籍tag信息
        //首先清空书籍所有关联tag
        ecMapper.deleteEBookTagByEBookID(eBook.getEBookID());
        List<Tag> tagList=eBook.getTags();
        //然后判断tag是否已经创建
        for (int i=0;i<tagList.size();i++){
            Tag tag=tagList.get(i);
            int result = ecMapper.haveTag(tag.getTag_name());
            if(result==0){
                //tag不存在 首先创建tag
                ecMapper.addTag(tag);
            }else {
                //tag存在 结果值就是tagID
                tag.setId(result);
            }
            //处理ebook_tag
            ecMapper.addEBookTag(eBook.getEBookID(),tag.getId());
        }

        return true;
    }

}
