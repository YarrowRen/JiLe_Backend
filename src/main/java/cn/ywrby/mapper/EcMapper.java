package cn.ywrby.mapper;

import cn.ywrby.domain.EBook;
import cn.ywrby.domain.EBookCol;
import cn.ywrby.domain.Person;
import cn.ywrby.domain.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EcMapper {
    public String getEcPathByID(Integer ecID) ;

    List<EBook> getEcByID(Integer ecID);

    void deleteEBook(int eBookID);

    void addEBook(EBook eBook);

    EBook getEBookByID(int eBookID);

    List<EBookCol> getEc();

    int addEc(EBookCol eBookCol);

    //判断是否存在指定人物名
    //使用子查询 如果存在指定人物就返回personID否则返回0
    int havePerson(String personName);


    int addPerson(Person person);


    void addEBookAuthor(int eBookID, int personID);

    EBookCol getEBookCol(Integer ec_id);

    List<EBook> getECEBookInfo(Integer ec_id);

    List<Person> getAuthorsByEBookID(int eBookID);

    List<Tag> getTagsByEBookID(int eBookID);

    void updateEBookInfo(EBook eBook);

    void deleteEBookAuthorByEBookID(int eBookID);

    int haveTag(String tag_name);

    int addTag(Tag tag);

    void deleteEBookTagByEBookID(int eBookID);

    void addEBookTag(int eBookID, int tagID);

    List<EBook> getRandomEBook(int num);

    void deleteEC(int ec_id);

    void updateEC(EBookCol eBookCol);

    List<Integer> searchEBookTitle(int ecID,List<String> searchList);
    List<Integer> searchEBookPublisher(int ecID,List<String> searchList);
    List<Integer> searchEBookAuthor(int ecID,List<String> searchList);
    List<Integer> searchEBookTag(int ecID,List<String> searchList);

    int getMaxEBookID();
}
