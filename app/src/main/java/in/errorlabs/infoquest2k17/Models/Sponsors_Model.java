package in.errorlabs.infoquest2k17.Models;

/**
 * Created by root on 1/9/17.
 */

public class Sponsors_Model {



    public String pro_pic;
    public String desctiption;

    public Sponsors_Model(String pro_pic,String desctiption){
        this.pro_pic=pro_pic;
        this.desctiption=desctiption;
    }

    public Sponsors_Model() {

    }

    public String getPro_pic() {
        return pro_pic;
    }

    public void setPro_pic(String pro_pic) {
        this.pro_pic = pro_pic;
    }

    public String getDesctiption() {
        return desctiption;
    }

    public void setDesctiption(String desctiption) {
        this.desctiption = desctiption;
    }
}
