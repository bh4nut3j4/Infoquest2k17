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

import in.errorlabs.infoquest2k17.Adapters.Sponsors_Adapter;
import in.errorlabs.infoquest2k17.Configs.Sponses_Config;
import in.errorlabs.infoquest2k17.Models.Sponsors_Model;
import in.errorlabs.infoquest2k17.R;
import in.errorlabs.infoquest2k17.Utils.Connection;
import okhttp3.OkHttpClient;

public class Sponsors extends AppCompatActivity {
    Connection connection;
    Context context;
    List<Sponsors_Model> list;
    Sponsors_Adapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LoadToast loadToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sponsors);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sponsors");
        recyclerView= (RecyclerView) findViewById(R.id.sponsers_recyclerview);
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

        AndroidNetworking.post(Sponses_Config.sponser_url)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
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
            Sponsors_Model model = new Sponsors_Model();
            JSONObject json;
            try {
                json = array.getJSONObject(i);
                model.setPro_pic(json.getString(Sponses_Config.image));
                model.setDesctiption(json.getString(Sponses_Config.description));
                list.add(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new Sponsors_Adapter(list,this);
        recyclerView.setAdapter(adapter);


    }

}
