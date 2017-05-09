package in.errorlabs.infoquest2k17.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import in.errorlabs.infoquest2k17.Adapters.NotificationsAdapter;
import in.errorlabs.infoquest2k17.Configs.Notifications_Config;
import in.errorlabs.infoquest2k17.Models.Notifications_Model;
import in.errorlabs.infoquest2k17.R;
import in.errorlabs.infoquest2k17.Utils.Connection;
import in.errorlabs.infoquest2k17.Utils.Onitemtouchlistener;
import okhttp3.OkHttpClient;

public class Notifications extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LoadToast loadToast;
    Connection connection;
    Context context;
    NotificationsAdapter adapter;
    List<Notifications_Model> list;
    TextView nonotifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);
        recyclerView= (RecyclerView) findViewById(R.id.notifications_f_recyclerview);
        nonotifi= (TextView) findViewById(R.id.nonotifications);
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
            finish();
        }

        recyclerView.addOnItemTouchListener(new Onitemtouchlistener(context, new Onitemtouchlistener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView headtxt= (TextView) view.findViewById(R.id.home_card_heading);
                String  heading = headtxt.getText().toString();
                Intent expand = new Intent(getApplicationContext(), Notification_Expand.class);
                expand.putExtra("heading",heading);
                startActivity(expand);
            }
        }));

    }

    public void getData() {
        loadToast.show();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                . writeTimeout(120, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(Notifications_Config.NOTIFICATION_URL)
                .addBodyParameter("APPKEY",getString(R.string.APPKEY))
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
        for (int i=j-1 ;i>=0;i--) {
            Notifications_Model model = new Notifications_Model();
            JSONObject json;
            try {
                json = array.getJSONObject(i);

                if(!json.has("AppKeyError")){

                    if (!json.has("NoNotifications")){

                        json = array.getJSONObject(i);
                        model.setImageurl(json.getString(Notifications_Config.TAG_PRO_IMAGE_URL));
                        model.setHeading(json.getString(Notifications_Config.TAG_HEADING));
                        model.setTimestamp(json.getString(Notifications_Config.TAG_TIMESTAMP));
                        model.setTag(json.getString(Notifications_Config.TAG_TAGNAME));
                        list.add(model);

                    }else{
                        Toast.makeText(getApplicationContext(), "No Notifications", Toast.LENGTH_SHORT).show();recyclerView.setVisibility(View.GONE);
                        nonotifi.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "please try after sometime", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Home.class));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "please try after sometime", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),Home.class));
            }
        }

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new NotificationsAdapter(list,this);
        recyclerView.setAdapter(adapter);

    }
}
