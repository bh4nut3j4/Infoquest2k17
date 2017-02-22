package in.errorlabs.infoquest2k17.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import in.errorlabs.infoquest2k17.Configs.RegisteredEventConfig;
import in.errorlabs.infoquest2k17.Configs.SharedPrefs;
import in.errorlabs.infoquest2k17.R;
import in.errorlabs.infoquest2k17.Utils.Connection;
import okhttp3.OkHttpClient;

public class DeleteEvent extends AppCompatActivity {
    LoadToast loadToast;
    SharedPrefs sharedPrefs;
    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_event);
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("eventname");
        String eventid = bundle.getString("eventid");
        loadToast=new LoadToast(this);
        loadToast.setText("Loading...");
        sharedPrefs=new SharedPrefs(this);
        connection = new Connection(this);
        loadToast.show();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                . writeTimeout(120, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(RegisteredEventConfig.delete_event_url)
                .addBodyParameter("AUTHKEY",sharedPrefs.getLogedInKey())
                .addBodyParameter("EventName",name)
                .addBodyParameter("EventRegID",eventid)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int j = response.length();
                        for (int i=0;i<j;i++) {
                            JSONObject json;
                            try {
                                json = response.getJSONObject(i);
                                if(!json.has("AuthKeyError")){
                                    if(!json.has("ErrorDeleting")){

                                        if (json.has("Deleted")){
                                            Toast.makeText(getApplicationContext(),"Delete", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),RegisteredEvents.class));
                                            finish();
                                        }else {
                                            Toast.makeText(getApplicationContext(), "Cannot Delete", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),RegisteredEvents.class));
                                            finish();
                                        }

                                    }else {
                                        Toast.makeText(getApplicationContext(), "Cannot Delete", Toast.LENGTH_SHORT).show();
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

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Please try again after sometime", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Home.class));
                        finish();
                    }
                });


    }
}
