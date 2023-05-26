package cn.ywrby.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EBook {
    private int ec_id;
    private int eBookID;
    private String eBookName;  //书籍文件名
    private String eBookTitle; //书籍名称
    private String language;
    private String format;

    private List<Person> authorList;
    private Date publishDate;
    private int eBookScore;

    private String intro;
    private int followed;
    private String url;
    private List<Tag> tags;
    private String publisher;
    private String identifier;
    private String coverPath;

    private String extension;  //书籍扩展名（zip,cbz,epub）
    private int pages;  //页数

    private String filePath;  //文件路径 只在一些情况下使用


}
