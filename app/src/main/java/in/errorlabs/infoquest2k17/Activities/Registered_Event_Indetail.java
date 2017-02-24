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

public class Registered_Event_Indetail extends AppCompatActivity {
    ImageView qrcode,g1,g2,g3,g4,r1,r2,r3,r4;
    LoadToast loadToast;
    SharedPrefs sharedPrefs;
    Connection connection;
    ProgressBar progressBar;
    RelativeLayout lay1,lay2;
    TextView lay2_eventname,desctext;
    EditText lay2_teamid;
    Button lay2_submit;
    String eventname,event_reg_id,team_id,key,uname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered_event_indetail);
        Bundle bundle = getIntent().getExtras();
         eventname =bundle.getString("eventname");
         event_reg_id =bundle.getString("event_reg_id");
         team_id =bundle.getString("teamid");
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
        desctext= (TextView) findViewById(R.id.desctext);
        if (eventname.equals("PaperPresentation")){
            desctext.setText("1) TeamID will be mailed to your Email ID after you submit your abstract. \n\n2) If you are a member of the team and have not submitted the abstract, then you can use the TeamID send to your partner who has submitted the abstract. \n\n" +
                    "3) Team can have maximum of 2 participants.\n\n " +
                    "4) For any queries call +918686632890 or mail us at- infoquest@jbiet.edu.in");
            lay2_teamid.setHint("Enter the Team ID");
        }
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
               // Toast.makeText(getApplicationContext(),"TeamID"+team_id,Toast.LENGTH_SHORT).show();
                if (team_id.isEmpty() || team_id.length()<=0 || team_id.equals("NULL") || team_id.equals("")){
                    lay1.setVisibility(View.GONE);
                    lay2.setVisibility(View.VISIBLE);
                    lay2_eventname.setText(eventname);
                }else {
                    lay2.setVisibility(View.GONE);
                    lay1.setVisibility(View.VISIBLE);
                    key=sharedPrefs.getLogedInKey();
                    uname=sharedPrefs.getLogedInUserName();
                    getData(key,uname,eventname,event_reg_id,team_id);
                }

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            }
        }

        lay2_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = lay2_teamid.getText().toString();
                if (id.length()<=0){
                    Toast.makeText(getApplicationContext(), "Please Enter Your Team ID", Toast.LENGTH_SHORT).show();
                }else {
                    String key=sharedPrefs.getLogedInKey();
                    String uname=sharedPrefs.getLogedInUserName();
                    String newteamid=eventname+id;
                    Toast.makeText(getApplicationContext(), "id"+newteamid, Toast.LENGTH_SHORT).show();
                    getTeamID(key,uname,eventname,event_reg_id,newteamid);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.registered_single_menu, menu);
        MenuItem item = menu.findItem(R.id.refresh);

        if (team_id.isEmpty() || team_id.length()<=0 || team_id.equals("NULL") || team_id.equals("")){
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh) {
            getData(key,uname,eventname,event_reg_id,team_id);
        }

        return super.onOptionsItemSelected(item);
    }

    public void getTeamID(String key,String uname,String eventname,String event_reg_id,String teamid){
        loadToast.show();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                . writeTimeout(120, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.post(RegisteredEventConfig.new_get_into_team)
                .addBodyParameter(RegisteredEventConfig.event_authkey,key)
                .addBodyParameter(RegisteredEventConfig.event_username,uname)
                .addBodyParameter(RegisteredEventConfig.eventname,eventname)
                .setOkHttpClient(okHttpClient)
                .addBodyParameter(RegisteredEventConfig.event_reg_id,event_reg_id)
                .addBodyParameter(RegisteredEventConfig.event_team_id,teamid)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        loadToast.success();
                        //Toast.makeText(getApplicationContext(),"Response"+response,Toast.LENGTH_SHORT).show();


                        int j = response.length();
                        for (int i = 0; i < j; i++) {
                            JSONObject json;
                            try {
                                json = response.getJSONObject(i);
                                if (!json.has("AuthKeyError")) {
                                    if (!json.has("InvalidTeamID")) {
                                        if (!json.has("MaxNotFoundError")) {
                                            if (!json.has("LimitExceededError")){
                                                if(!json.has("InsertionError")){
                                                    if (json.has("SuccessfullyAdded")){

                                                        Toast.makeText(getApplicationContext(), "Successfully Added To Team", Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(getApplicationContext(),RegisteredEvents.class));
                                                        finish();

                                                    }else {
                                                        Toast.makeText(getApplicationContext(), "Falied, Try Again", Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(getApplicationContext(),RegisteredEvents.class));
                                                        finish();
                                                    }

                                                }else{
                                                    Toast.makeText(getApplicationContext(), "Falied, Try Again", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(getApplicationContext(),RegisteredEvents.class));
                                                    finish();
                                                }


                                            }else {
                                                Toast.makeText(getApplicationContext(), "You have already reached the maximum members for this event", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(getApplicationContext(),RegisteredEvents.class));
                                                finish();
                                            }
                                        }else {
                                            Toast.makeText(getApplicationContext(), "Please Try Again Later", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),RegisteredEvents.class));
                                            finish();
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Invalid Team ID", Toast.LENGTH_SHORT).show();
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
                    public void onError(ANError anError) {
                        loadToast.error();
                        Toast.makeText(getApplicationContext(),"Please check your internet connection".toString(),Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Home.class));
                    }
                });

    }

    public void getData(String key,String uname,String eventname,String event_reg_id,String team_id) {

        loadToast.show();
        AndroidNetworking.post(RegisteredEventConfig.new_event_indetails_url)
                .addBodyParameter(RegisteredEventConfig.event_authkey,key)
                .addBodyParameter(RegisteredEventConfig.event_username,uname)
                .addBodyParameter(RegisteredEventConfig.eventname,eventname)
                .addBodyParameter(RegisteredEventConfig.event_reg_id,event_reg_id)
                .addBodyParameter(RegisteredEventConfig.event_team_id,team_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        loadToast.success();
                       // Toast.makeText(getApplicationContext(),"Response"+response,Toast.LENGTH_SHORT).show();
                        parsedata(response);
                    }
                    @Override
                    public void onError(ANError anError) {
                        loadToast.error();
                        Toast.makeText(getApplicationContext(),"Please check your internet connection".toString(),Toast.LENGTH_SHORT).show();
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
                        String url = json.getString(RegisteredEventConfig.Team_Event_Qr_Url);
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
