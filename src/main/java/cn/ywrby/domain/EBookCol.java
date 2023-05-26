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
public class EBookCol {
    private int id;
    private String ec_name;
    private String ec_path;
    private String ec_desc;
    private List<EBook> EBook_list;
    private PageInfo<EBook> ec_info;

    //展示用封面图
    private String ec_cover;
}
