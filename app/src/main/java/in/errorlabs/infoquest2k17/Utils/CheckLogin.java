package in.errorlabs.infoquest2k17.Utils;

import android.content.Context;

import in.errorlabs.infoquest2k17.Configs.SharedPrefs;

/**
 * Created by root on 1/15/17.
 */

public class CheckLogin  {


    SharedPrefs sharedPrefs;

    Context context;

            public CheckLogin(Context context,SharedPrefs sharedPrefs){
                this.context=context;
                this.sharedPrefs=sharedPrefs;
            }




    public boolean check(){
        String key=sharedPrefs.getLogedInKey();
        String email=sharedPrefs.getEmail();
        String uname=sharedPrefs.getLogedInUserName();
        return !(key == null || email == null || uname == null);
    }

}
