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
public class VideoCol {
    private int id;
    private String vc_name;
    private String vc_path;
    private String vc_desc;
    private List<Video> video_list;
    private PageInfo<Video> vc_info;
    private String vc_cover;
}
