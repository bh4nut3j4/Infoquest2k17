package in.errorlabs.infoquest2k17.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import in.errorlabs.infoquest2k17.Adapters.Team_IQ_Adapter;
import in.errorlabs.infoquest2k17.Configs.Team_IQ_Config;
import in.errorlabs.infoquest2k17.Models.Team_IQ_Model;
import in.errorlabs.infoquest2k17.R;
import in.errorlabs.infoquest2k17.Utils.Connection;
import okhttp3.OkHttpClient;

public class Team_IQ extends AppCompatActivity {
    Connection connection;
    Context context;
    List<Team_IQ_Model> list;
    Team_IQ_Adapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LoadToast loadToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_iq);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Team Infoquest");
        recyclerView= (RecyclerView) findViewById(R.id.teamiq_recyclerview);
        list = new ArrayList<>();
        loadToast=new LoadToast(this);
        loadToast.setText("Loading...");
        connection=new Connection(getApplicationContext());
        Boolean checkinternet =(connection.isInternet());
        if (checkinternet) {
            Log.d("TAG","online startted");
            getData();
        } else {
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),Home.class));
        }
    }

    public void getData() {
        loadToast.show();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                . writeTimeout(120, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.post(Team_IQ_Config.Team_IQ_Url)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
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
        for (int i=0;i<j;i++) {
            Team_IQ_Model model = new Team_IQ_Model();
            JSONObject json;
            try {
                json = array.getJSONObject(i);
                model.setTeam_IQ_Image(json.getString(Team_IQ_Config.Image));
                model.setTeam_IQ_Name(json.getString(Team_IQ_Config.Name));
                list.add(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new Team_IQ_Adapter(list,this);
        recyclerView.setAdapter(adapter);

    }

}
