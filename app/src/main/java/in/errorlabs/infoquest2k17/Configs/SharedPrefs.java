package in.errorlabs.infoquest2k17.Configs;

import android.content.Context;
import android.content.SharedPreferences;

import in.errorlabs.infoquest2k17.Utils.CheckLogin;

/**
 * Created by root on 1/11/17.
 */

public class SharedPrefs  {

    Context context;

    public static final String myprefs = "myprefs";
    public static final String LogedInUserName = "UserName";
    public static final String LogedInEmail = "Email";
    public static final String LogedInKey = "Key";
    public static final String ResetEmail = "Email";
    public static final String firstopen=null;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public SharedPrefs(Context context) {
        this.context = context;
        sharedpreferences = context.getSharedPreferences(myprefs, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    public SharedPrefs(CheckLogin checkLogin) {
    }

    public void saveprefs(String UserName,String Email,String Key){
        editor.putString(LogedInUserName, UserName);
        editor.putString(LogedInEmail, Email);
        editor.putString(LogedInKey,Key);
        editor.commit();
    }

    public void clearprefs() {
        editor.putString(LogedInUserName, null);
        editor.putString(LogedInEmail, null);
        editor.putString(LogedInKey,null);
        editor.commit();
    }

    public  String getLogedInUserName() {
         return sharedpreferences.getString(LogedInUserName,null);
    }

    public  String getEmail() {
        return sharedpreferences.getString(LogedInEmail,null);
    }

    public String getLogedInKey() {
        return sharedpreferences.getString(LogedInKey,null);
    }

    public  void setResetEmail(String email) {
        editor.putString(ResetEmail,email);
        editor.commit();
    }

    public  String getResetEmail() {
        return sharedpreferences.getString(ResetEmail,null);
    }

    public void clearResetEmail(){
        editor.putString(ResetEmail,null);
        editor.commit();
    }

    public void setopened(){
        editor.putString(firstopen,"true");
        editor.commit();
    }

    public String getopened(){
        return sharedpreferences.getString(firstopen,null);
    }


}
