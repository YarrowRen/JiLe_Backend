package cn.ywrby.domain;

import cn.hutool.json.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Video {
    private int vc_id;
    private int videoID;
    private String videoName;
    private String videoPath;
    private String videoCover;
    private int videoScore;


}
