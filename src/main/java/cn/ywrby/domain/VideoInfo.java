package cn.ywrby.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VideoInfo {
    private String videoName;
    /**
     * 总帧数
     **/
    private int lengthInFrames;

    /**
     * 帧率
     **/
    private double frameRate;

    /**
     * 时长
     **/
    private double duration;

    /**
     * 视频编码
     */
    private String videoCode;
    /**
     * 音频编码
     */
    private String audioCode;

    private int width;
    private int height;
    private int audioChannel;
    private String md5;
    /**
     * 音频采样率
     */
    private Integer sampleRate;

    /**
     * 封面图路径
     */
    private String coverPath;
}
