package in.errorlabs.infoquest2k17.Models;

/**
 * Created by root on 1/8/17.
 */

public class Featured_Model {

    public String featured_pic;

    public Featured_Model(String featured_pic) {
        this.featured_pic=featured_pic;
    }

    public String getFeatured_pic() {
        return featured_pic;
    }

    public void setFeatured_pic(String featured_pic) {
        this.featured_pic = featured_pic;
    }
}
