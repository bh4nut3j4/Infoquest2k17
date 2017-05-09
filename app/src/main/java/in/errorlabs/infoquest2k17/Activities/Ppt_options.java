package in.errorlabs.infoquest2k17.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import in.errorlabs.infoquest2k17.Configs.SharedPrefs;
import in.errorlabs.infoquest2k17.R;
import okhttp3.OkHttpClient;

public class Ppt_options extends AppCompatActivity {

    ImageView img;
    Button upload,email;
    String url="https://jbiet-my.sharepoint.com/personal/infoquest_jbiet_edu_in/_layouts/15/guestaccess.aspx?docid=156e0c580747c415c85a91168c5987559&authkey=AVbWry9ANlLAupR168Dsyec";
    String uri="https://forms.zohopublic.com/bhanutejar07/form/Infoquest17/formperma/4g4BdjBg_4ddm6Ca623g8530G";
    ProgressBar progressBar;
    LoadToast loadToast;
    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppt_options);
        img= (ImageView) findViewById(R.id.home_imgview);
        upload= (Button) findViewById(R.id.upload);
        email= (Button) findViewById(R.id.email);
        loadToast=new LoadToast(this);
        sharedPrefs = new SharedPrefs(this);
        progressBar= (ProgressBar) findViewById(R.id.progress);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PaperPresentation");
        Glide.with(getApplicationContext()).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(android.R.drawable.ic_dialog_alert).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }
            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(img);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),EventRegistration.class);
                i.putExtra("name","PaperPresentation");
                startActivity(i);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("LOG","LOGG"+sharedPrefs.getLogedInUserName()+"--"+sharedPrefs.getLogedInKey());

                loadToast.show();
                OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                        .connectTimeout(120, TimeUnit.SECONDS)
                        .readTimeout(120, TimeUnit.SECONDS)
                        . writeTimeout(120, TimeUnit.SECONDS)
                        .build();

                AndroidNetworking.post("https://jbgroup.org.in/sync/sync_mapp/iq_php/new_check_ppt_exists.php")
                        .addBodyParameter("AUTHKEY",sharedPrefs.getLogedInKey())
                        .addBodyParameter("UserName",sharedPrefs.getLogedInUserName())
                        .addBodyParameter("EventName","PaperPresentation")
                        .setPriority(Priority.MEDIUM)
                        .setOkHttpClient(okHttpClient)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                loadToast.success();
                                Log.d("LOG","LOGG"+response.toString());
                                int j = response.length();
                                for (int i = 0; i < j; i++) {
                                    JSONObject json;
                                    try {
                                        json = response.getJSONObject(i);

                                        if(!json.has("AuthKeyError")){

                                            if (!json.has("NotRegistered")){

                                                if (json.has("Registered")){

                                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                    startActivity(intent);

                                                }else {
                                                    Toast.makeText(getApplicationContext(), "Registration Failed,Try Again", Toast.LENGTH_SHORT).show();
                                                }

                                            }else{
                                                final AlertDialog.Builder builder = new AlertDialog.Builder(Ppt_options.this);
                                                builder.setTitle("PaperPresentation");
                                                builder.setMessage("You have not registered for PaperPresentation!" +
                                                        "\n\nRegister First (by tapping the banner above). Only then you can upload your abstract along with your Paper Presentation Event Reg ID");
                                                builder.setPositiveButton("Ok",null);
                                                builder.setIcon(R.drawable.applogo);
                                                AlertDialog welcomeAlert = builder.create();
                                                welcomeAlert.show();
                                                ((TextView) welcomeAlert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                                            }

                                        }else{
                                            Toast.makeText(getApplicationContext(), "please try after sometime", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "Please try after sometime", Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }

                            @Override
                            public void onError(ANError anError) {
                                loadToast.error();
                                Toast.makeText(getApplicationContext(), "Please try again after sometime", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Home.class));
                                finish();
                            }
                        });



            }
        });

       /* email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  startActivity(new Intent(getApplicationContext(),Ppt_reg_ids.class));
               *//* Intent i = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto:" + Uri.encode("infoquest@jbiet.edu.in") + "?subject=" +
                        Uri.encode("IQ'17 Abstract Submission") + "&body=" + Uri.encode("Hello, Team Infoquest \n\n\n Name :\n\nContact Number :\n\n College Name :\n\n Abstract Title : \n\n Event Reg ID :  \n\n\nAttach your abstract as an attachment.");
                Uri uri = Uri.parse(uriText);
                i.setData(uri);
                startActivity(Intent.createChooser(i, "Send Email"));*//*
            }
        });*/
    }
}
