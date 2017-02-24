package in.errorlabs.infoquest2k17.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.errorlabs.infoquest2k17.Adapters.RegisteredEvent_F_Adapter;
import in.errorlabs.infoquest2k17.Configs.RegisteredEventConfig;
import in.errorlabs.infoquest2k17.Configs.SharedPrefs;
import in.errorlabs.infoquest2k17.Models.RegisteredEvent_F_Model;
import in.errorlabs.infoquest2k17.R;
import in.errorlabs.infoquest2k17.Utils.Connection;
import in.errorlabs.infoquest2k17.Utils.Onitemtouchlistener;
import okhttp3.OkHttpClient;

public class RegisteredEvents extends AppCompatActivity {

    Connection connection;
    TextView noreg;
    Context context;
    List<RegisteredEvent_F_Model> list;
    RegisteredEvent_F_Adapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LoadToast loadToast;
    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered_events);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Registered Events");
        recyclerView= (RecyclerView) findViewById(R.id.registered_f_recyclerview);
        noreg= (TextView) findViewById(R.id.noregistrationtext);
        list = new ArrayList<>();
        loadToast=new LoadToast(this);
        loadToast.setText("Loading...");
        connection=new Connection(getApplicationContext());
        sharedPrefs = new SharedPrefs(this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
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
                String email=sharedPrefs.getEmail();
                String uname=sharedPrefs.getLogedInUserName();
                getData(LogedInauthkey,LogedInusername,LogedInemail);
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            }
        }
        recyclerView.addOnItemTouchListener(new Onitemtouchlistener(context, new Onitemtouchlistener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                TextView e_name = (TextView) view.findViewById(R.id.eventname);
                TextView e_id = (TextView) view.findViewById(R.id.event_reg_id);
                TextView t_id = (TextView) view.findViewById(R.id.teamid);
                String eventname=e_name.getText().toString();
                String event_reg_id=e_id.getText().toString();
                String team_id=t_id.getText().toString();

                if (eventname.equals("Can you C")){
                    Intent expand = new Intent(getApplicationContext(), Registered_Events_Single_Indetail.class);
                    Log.d("afasdf","intent_putextrats"+pos);
                    expand.putExtra("eventname",eventname);
                    expand.putExtra("event_reg_id",event_reg_id);
                    startActivity(expand);

                }else if (eventname.equals("Blur")){
                    Intent expand = new Intent(getApplicationContext(), Registered_Events_Single_Indetail.class);
                    Log.d("afasdf","intent_putextrats"+pos);
                    expand.putExtra("eventname",eventname);
                    expand.putExtra("event_reg_id",event_reg_id);
                    startActivity(expand);
                }else if(eventname.equals("Googled")){
                    Intent expand = new Intent(getApplicationContext(), Registered_Events_Single_Indetail.class);
                    Log.d("afasdf","intent_putextrats"+pos);
                    expand.putExtra("eventname",eventname);
                    expand.putExtra("event_reg_id",event_reg_id);
                    startActivity(expand);
                }else if (eventname.equals("Gears of Rampage")){
                    Intent expand = new Intent(getApplicationContext(), Registered_Events_Single_Indetail.class);
                    Log.d("afasdf","intent_putextrats"+pos);
                    expand.putExtra("eventname",eventname);
                    expand.putExtra("event_reg_id",event_reg_id);
                    startActivity(expand);
                }else {
                    Intent expand = new Intent(getApplicationContext(), Registered_Event_Indetail.class);
                    Log.d("afasdf","intent_putextrats"+pos);
                    expand.putExtra("eventname",eventname);
                    expand.putExtra("event_reg_id",event_reg_id);
                    expand.putExtra("teamid",team_id);
                    startActivity(expand);
                }

            }
        }));
    }

    public void getData(String key,String uname,String email) {

        loadToast.show();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                . writeTimeout(120, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(RegisteredEventConfig.new_final_event_url)
                .addBodyParameter(RegisteredEventConfig.event_authkey,key)
                .addBodyParameter(RegisteredEventConfig.event_username,uname)
                .addBodyParameter(RegisteredEventConfig.event_email,email)
                .setOkHttpClient(okHttpClient)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadToast.success();
                        parsedata(response);

                    }
                    @Override
                    public void onError(ANError anError) {
                        loadToast.error();
                        Log.d("LOG","LOGG"+anError.toString());
                        Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Home.class));
                    }
                });
    }

    public void parsedata(JSONObject response){

           //gisteredEvent_F_Model model = new RegisteredEvent_F_Model();
            try {

                if(!response.has("AuthKeyError")){
                            if(!response.has("ResultEmptyError")){

                                JSONArray singleevents = response.getJSONArray("SingleEvents");
                                JSONObject json;
                                for (int k=0;k<singleevents.length();k++) {
                                    RegisteredEvent_F_Model model = new RegisteredEvent_F_Model();
                                    json = singleevents.getJSONObject(k);
                                    model.setEventname(json.getString(RegisteredEventConfig.eventname));
                                    model.setEvent_reg_id(json.getString(RegisteredEventConfig.event_reg_id));
                                    model.setTeam_id(json.getString(RegisteredEventConfig.event_team_id));
                                    list.add(model);
                                }

                                JSONArray teamevents = response.getJSONArray("TeamEvents");
                                JSONObject json2;
                                for (int n=0;n<teamevents.length();n++) {
                                    RegisteredEvent_F_Model model = new RegisteredEvent_F_Model();
                                    json2 = teamevents.getJSONObject(n);
                                    model.setEventname(json2.getString(RegisteredEventConfig.eventname));
                                    model.setEvent_reg_id(json2.getString(RegisteredEventConfig.event_reg_id));
                                    model.setTeam_id(json2.getString(RegisteredEventConfig.event_team_id));
                                    list.add(model);
                                }

                            }else {
                                Toast.makeText(getApplicationContext(), "No Registrations Done Yet", Toast.LENGTH_SHORT).show();
                                recyclerView.setVisibility(View.GONE);
                                noreg.setVisibility(View.VISIBLE);
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


        adapter = new RegisteredEvent_F_Adapter(list,this);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Home.class));
        finish();
    }

}
