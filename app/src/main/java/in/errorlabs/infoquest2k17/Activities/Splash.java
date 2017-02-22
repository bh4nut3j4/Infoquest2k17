package in.errorlabs.infoquest2k17.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import in.errorlabs.infoquest2k17.Configs.SharedPrefs;
import in.errorlabs.infoquest2k17.R;

public class Splash extends AppCompatActivity {
    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        sharedPrefs=new SharedPrefs(this);
        if (sharedPrefs.getopened()==null){
            startActivity(new Intent(getApplicationContext(),Intro.class));
            finish();

        }else if (sharedPrefs.getLogedInUserName()==null){
            //startActivity(new Intent(getApplicationContext(),Home.class));
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }else {
            startActivity(new Intent(getApplicationContext(),Home.class));
            finish();
        }
    }
}
