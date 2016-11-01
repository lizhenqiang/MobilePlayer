package atguigu.mobileplayer.domain;

import java.util.Arrays;

/**
 * Created by lzq on 2016/10/6.
 */
/*{"trailers":[{"id":62775,
        "movieName":"《逃出绝命镇》预告片",
        "coverImg":"http://img5.mtime.cn/mg/2016/10/05/175628.50274504.jpg",
        "movieId":228620,
        "url":"http://vfx.mtime.cn/Video/2016/10/05/mp4/161005105236188684_480.mp4",
        "hightUrl":"http://vfx.mtime.cn/Video/2016/10/05/mp4/161005105236188684.mp4",
        "videoTitle":"逃出绝命镇 预告片",
        "videoLength":152,
        "rating":-1,
        "type":["恐怖"],
        "summary":"去女友家度假却陷身阴谋之中"}*/
public class NetMediaItem {
  private int id;
    private String movieName;
    private String coverImg;
    private int movieId;
    private String url;
    private String hightUrl;
    private String videoTitle;
    private int videoLength;
    private int rating ;
    private String[] type;
    private String summary;

    public NetMediaItem() {
    }

    public NetMediaItem(int id, String movieName, String coverImg, int movieId, String url, String hightUrl, String videoTitle, int videoLength, int rating, String[] type, String summary) {
        this.id = id;
        this.movieName = movieName;
        this.coverImg = coverImg;
        this.movieId = movieId;
        this.url = url;
        this.hightUrl = hightUrl;
        this.videoTitle = videoTitle;
        this.videoLength = videoLength;
        this.rating = rating;
        this.type = type;
        this.summary = summary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHightUrl() {
        return hightUrl;
    }

    public void setHightUrl(String hightUrl) {
        this.hightUrl = hightUrl;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public int getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(int videoLength) {
        this.videoLength = videoLength;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "NetMediaItem{" +
                "id=" + id +
                ", movieName='" + movieName + '\'' +
                ", coverImg='" + coverImg + '\'' +
                ", movieId=" + movieId +
                ", url='" + url + '\'' +
                ", hightUrl='" + hightUrl + '\'' +
                ", videoTitle='" + videoTitle + '\'' +
                ", videoLength=" + videoLength +
                ", rating=" + rating +
                ", type=" + Arrays.toString(type) +
                ", summary='" + summary + '\'' +
                '}';
    }
}
