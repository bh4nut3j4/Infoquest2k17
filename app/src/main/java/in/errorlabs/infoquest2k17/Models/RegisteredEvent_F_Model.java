package in.errorlabs.infoquest2k17.Models;

/**
 * Created by root on 1/11/17.
 */

public class RegisteredEvent_F_Model {

    public  String eventname= "EventName";
    public  String event_reg_id= "EventRegID";

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public  String team_id= "TeamID";

    public String getEvent_reg_id() {
        return event_reg_id;
    }

    public void setEvent_reg_id(String event_reg_id) {
        this.event_reg_id = event_reg_id;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }
}
