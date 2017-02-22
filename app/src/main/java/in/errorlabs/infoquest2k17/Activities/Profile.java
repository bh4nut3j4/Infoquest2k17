package in.errorlabs.infoquest2k17.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import in.errorlabs.infoquest2k17.Configs.ProfileConfig;
import in.errorlabs.infoquest2k17.Configs.SharedPrefs;
import in.errorlabs.infoquest2k17.R;
import in.errorlabs.infoquest2k17.Utils.Connection;
import okhttp3.OkHttpClient;

public class Profile extends AppCompatActivity {

    TextView fname,lname,uname,email,phnumber,clgname;
    Connection connection;
    SharedPrefs sharedPrefs;
    LoadToast loadToast;
    RelativeLayout profile_lay;

    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            . writeTimeout(120, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        fname= (TextView) findViewById(R.id.profile_fname);
        lname= (TextView) findViewById(R.id.profile_lname);
        uname= (TextView) findViewById(R.id.profile_username);
        email= (TextView) findViewById(R.id.profile_email);
        phnumber= (TextView) findViewById(R.id.profile_phnumber);
        clgname= (TextView) findViewById(R.id.profile_clgname);
        profile_lay= (RelativeLayout) findViewById(R.id.profile_lay);
        sharedPrefs = new SharedPrefs(this);
        loadToast = new LoadToast(this);
        loadToast.setText("Loading...");
        connection=new Connection(this);
        String LogedInauthkey= sharedPrefs.getLogedInKey();
        String LogedInemail= sharedPrefs.getEmail();
        String LogedInusername= sharedPrefs.getLogedInUserName();
        if (LogedInauthkey==null || LogedInemail==null || LogedInusername==null){
            sharedPrefs.clearprefs();
            Toast.makeText(getApplicationContext(),"Please Login",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }else {
            Boolean checkinternet = (connection.isInternet());
            if (checkinternet) {
                Log.d("TAG", "online startted");
                getuserdetails(LogedInauthkey, LogedInusername, LogedInemail);
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            }
        }
    }
    public void getuserdetails(String authkey,String username,String email){
        loadToast.show();

        AndroidNetworking.post(ProfileConfig.ProfileUrl)
                .addBodyParameter(ProfileConfig.AuthKey,authkey)
                .addBodyParameter(ProfileConfig.UserName,username)
                .addBodyParameter(ProfileConfig.Email,email)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        loadToast.success();
                        Log.d("LOG","PROFILE"+response);
                        parsedata(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        loadToast.error();
                        Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Home.class));
                    }
                });
    }
    public void parsedata(JSONArray array) {
        int j = array.length();
        for (int i=0;i<j;i++) {
            JSONObject json;
            try {
                json = array.getJSONObject(i);
                if(!json.has("AuthKeyError")){
                    if(!json.has("ResultEmptyError")){
                        profile_lay.setVisibility(View.VISIBLE);
                        fname.setText(json.getString(ProfileConfig.FirstName));
                        lname.setText(json.getString(ProfileConfig.LastName));
                        uname.setText(json.getString(ProfileConfig.UserName));
                        email.setText(json.getString(ProfileConfig.Email));
                        phnumber.setText(json.getString(ProfileConfig.PhoneNumber));
                        clgname.setText(json.getString(ProfileConfig.CollegeName));
                    }else {
                        Toast.makeText(getApplicationContext(), "No Details Found", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Home.class));
                        finish();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Invalid Authentication, Please Login", Toast.LENGTH_SHORT).show();
                    sharedPrefs.clearprefs();
                    startActivity(new Intent(getApplicationContext(),Login.class));
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Please try again after sometime", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
