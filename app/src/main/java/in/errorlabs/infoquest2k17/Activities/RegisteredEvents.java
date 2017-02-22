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
import com.androidnetworking.interfaces.JSONArrayRequestListener;

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

        AndroidNetworking.post(RegisteredEventConfig.new_event_url)
                .addBodyParameter(RegisteredEventConfig.event_authkey,key)
                .addBodyParameter(RegisteredEventConfig.event_username,uname)
                .addBodyParameter(RegisteredEventConfig.event_email,email)
                .setOkHttpClient(okHttpClient)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        loadToast.success();
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

    public void parsedata(JSONArray array){

        int j = array.length();
        for (int i=j-1 ;i>=0;i--) {
            RegisteredEvent_F_Model model = new RegisteredEvent_F_Model();
            JSONObject json;
            try {
                json = array.getJSONObject(i);
                if(!json.has("AuthKeyError")){
                            if(!json.has("ResultEmptyError")){
                                model.setEventname(json.getString(RegisteredEventConfig.eventname));
                                model.setEvent_reg_id(json.getString(RegisteredEventConfig.event_reg_id));
                                model.setTeam_id(json.getString(RegisteredEventConfig.event_team_id));
                                list.add(model);
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
        }
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new RegisteredEvent_F_Adapter(list,this);
        recyclerView.setAdapter(adapter);


    }

}
