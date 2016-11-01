package atguigu.mobileplayer.domain;

import java.io.Serializable;

/**
 * 作者：尚硅谷-杨光福 on 2016/9/28 14:57
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：代表一个视频或者一个音频
 */
public class MediaItem implements Serializable {

    private String name;
    private long duration;
    private long size;
    private String artist;
    private String data;
    private String imgUrl;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MediaItem{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", artist='" + artist + '\'' +
                ", data='" + data + '\'' +
                '}';
    }



}
