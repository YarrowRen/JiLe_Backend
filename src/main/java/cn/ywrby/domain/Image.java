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
public class Image {
    private int ic_id;
    private int imageID;
    private String imageName;
    private String imagePath;
    private String thumbnail;
    private int imageScore;
    private String remark;
    private int followed;
    private Date createDate;
    private String url;
    private List<Tag> tags;
    private String imageURL;
    private String thumbURL;
}
