package cn.ywrby.domain;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ImgCol {
    private int id;
    private String ic_name;
    private String ic_path;
    private String ic_desc;
    private List<Image> image_list;
    private PageInfo<Image> ic_info;
    private String ic_cover;
}
