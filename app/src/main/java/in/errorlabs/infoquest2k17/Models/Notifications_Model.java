package in.errorlabs.infoquest2k17.Models;

/**
 * Created by root on 1/22/17.
 */

public class Notifications_Model {

    private String imageurl;
    private String heading;
    private String timestamp;
    private String tag;
    private String content;
    private String content_img;

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent_img() {
        return content_img;
    }

    public void setContent_img(String content_img) {
        this.content_img = content_img;
    }
}
