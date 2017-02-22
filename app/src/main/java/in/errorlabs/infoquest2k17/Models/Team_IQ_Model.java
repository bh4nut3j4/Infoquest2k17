package in.errorlabs.infoquest2k17.Models;

/**
 * Created by root on 1/9/17.
 */

public class Team_IQ_Model {

    public String Team_IQ_Image;
    public String Team_IQ_Name;

    public Team_IQ_Model(String Team_IQ_Image,String Team_IQ_Name){
        this.Team_IQ_Image=Team_IQ_Image;
        this.Team_IQ_Name=Team_IQ_Name;
    }

    public Team_IQ_Model() {

    }


    public String getTeam_IQ_Image() {
        return Team_IQ_Image;
    }

    public void setTeam_IQ_Image(String team_IQ_Image) {
        Team_IQ_Image = team_IQ_Image;
    }

    public String getTeam_IQ_Name() {
        return Team_IQ_Name;
    }

    public void setTeam_IQ_Name(String team_IQ_Name) {
        Team_IQ_Name = team_IQ_Name;
    }
}
