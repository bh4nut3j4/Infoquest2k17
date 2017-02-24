package in.errorlabs.infoquest2k17.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import in.errorlabs.infoquest2k17.Configs.RegisteredEventConfig;
import in.errorlabs.infoquest2k17.Configs.SharedPrefs;
import in.errorlabs.infoquest2k17.R;
import in.errorlabs.infoquest2k17.Utils.Connection;
import okhttp3.OkHttpClient;

/**
 * Created by root on 2/19/17.
 */

public class Registered_Events_Single_Indetail extends AppCompatActivity {
    ImageView qrcode,g1,g2,g3,g4,r1,r2,r3,r4;
    LoadToast loadToast;
    SharedPrefs sharedPrefs;
    Connection connection;
    ProgressBar progressBar;
    RelativeLayout lay1,lay2;
    TextView lay2_eventname;
    EditText lay2_teamid;
    Button lay2_submit;
    String eventname,event_reg_id,key,uname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered_event_indetail);
        Bundle bundle = getIntent().getExtras();
         eventname =bundle.getString("eventname");
        event_reg_id =bundle.getString("event_reg_id");
        qrcode= (ImageView) findViewById(R.id.qrcode_img);
        g1= (ImageView) findViewById(R.id.green1);
        g2= (ImageView) findViewById(R.id.green_2);
        g3= (ImageView) findViewById(R.id.green_3);
        g4= (ImageView) findViewById(R.id.green_4);
        r1= (ImageView) findViewById(R.id.red_1);
        r2= (ImageView) findViewById(R.id.red_2);
        r3= (ImageView) findViewById(R.id.red_3);
        r4= (ImageView) findViewById(R.id.red_4);
        lay1= (RelativeLayout) findViewById(R.id.lay1);
        lay2= (RelativeLayout) findViewById(R.id.lay2);
        lay2_eventname= (TextView) findViewById(R.id.event__name);
        lay2_teamid= (EditText) findViewById(R.id.teamid);
        lay2_submit= (Button) findViewById(R.id.submit);
        progressBar= (ProgressBar) findViewById(R.id.progress);
        connection=new Connection(getApplicationContext());
        sharedPrefs = new SharedPrefs(this);
        loadToast = new LoadToast(this);
        loadToast.setText("Loading...");
        if (sharedPrefs.getLogedInKey()==null || sharedPrefs.getEmail()==null || sharedPrefs.getLogedInUserName()==null){
            sharedPrefs.clearprefs();
            Toast.makeText(getApplicationContext(),"Please Login",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }else {
            Boolean checkinternet = (connection.isInternet());
            if (checkinternet) {
                     key=sharedPrefs.getLogedInKey();
                     uname=sharedPrefs.getLogedInUserName();
                    getData(key,uname,eventname,event_reg_id);
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.registered_single_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh) {
            getData(key,uname,eventname,event_reg_id);
        }

        return super.onOptionsItemSelected(item);
    }

    public void getData(String key,String uname,String eventname,String event_reg_id) {

        loadToast.show();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                . writeTimeout(120, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.post(RegisteredEventConfig.event_indetails_url)
                .addBodyParameter(RegisteredEventConfig.event_authkey,key)
                .addBodyParameter(RegisteredEventConfig.event_username,uname)
                .addBodyParameter(RegisteredEventConfig.eventname,eventname)
                .addBodyParameter(RegisteredEventConfig.event_reg_id,event_reg_id)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        loadToast.success();
                        //Toast.makeText(getApplicationContext(),"Response"+response,Toast.LENGTH_SHORT).show();
                        parsedata(response);
                    }
                    @Override
                    public void onError(ANError anError) {
                        loadToast.error();
                        Toast.makeText(getApplicationContext(),"Please check your internet connection"+anError.toString(),Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Home.class));
                    }
                });
    }

    public void parsedata(JSONArray array){

        int j = array.length();
        for (int i = 0; i < j; i++) {
            JSONObject json;
            try {
                json = array.getJSONObject(i);
                if (!json.has("AuthKeyError")) {
                    if (!json.has("NoDetailsFound")) {
                        String url = json.getString(RegisteredEventConfig.Event_Qr_Url);
                       // Toast.makeText(getApplicationContext(),"Response"+url,Toast.LENGTH_SHORT).show();
                        String paidstatus = json.getString(RegisteredEventConfig.event_paidstatus);
                        String attendedstatus = json.getString(RegisteredEventConfig.event_attendedstatus);
                        String paybackstatus = json.getString(RegisteredEventConfig.event_paybackstatus);
                        String certificatestatus = json.getString(RegisteredEventConfig.event_certificatestatus);
                        //Toast.makeText(getApplicationContext(),url+paidstatus+attendedstatus+paidstatus+certificatestatus,Toast.LENGTH_SHORT).show();
                        Glide.with(getApplicationContext()).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.applogo).listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(qrcode);
                        if (paidstatus.equals("0")){
                            r1.setVisibility(View.VISIBLE);
                            g1.setVisibility(View.INVISIBLE);
                        }else {
                            r1.setVisibility(View.INVISIBLE);
                            g1.setVisibility(View.VISIBLE);
                        }
                        if (attendedstatus.equals("0")){
                            r2.setVisibility(View.VISIBLE);
                            g2.setVisibility(View.INVISIBLE);
                        }else {
                            r2.setVisibility(View.INVISIBLE);
                            g2.setVisibility(View.VISIBLE);
                        }
                        if (paybackstatus.equals("0")){
                            r3.setVisibility(View.VISIBLE);
                            g3.setVisibility(View.INVISIBLE);
                        }else {
                            r3.setVisibility(View.INVISIBLE);
                            g3.setVisibility(View.VISIBLE);
                        }
                        if (certificatestatus.equals("0")){
                            r4.setVisibility(View.VISIBLE);
                            g4.setVisibility(View.INVISIBLE);
                        }else {
                            r4.setVisibility(View.INVISIBLE);
                            g4.setVisibility(View.VISIBLE);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "No Details Found", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),RegisteredEvents.class));
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "AppKeyAuthenticationFailure", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),RegisteredEvents.class));
                    finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),RegisteredEvents.class));
        finish();
    }

}
