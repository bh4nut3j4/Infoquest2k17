package in.errorlabs.infoquest2k17.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import in.errorlabs.infoquest2k17.Configs.SharedPrefs;
import in.errorlabs.infoquest2k17.R;
import okhttp3.OkHttpClient;

public class TrackMypaper extends AppCompatActivity {

    TextView submitted,notsubmitted,verfied,notverified,remarks,allthebest;
    Spinner eventspinner;
    Button getstatus;
    String username,eventname;
    SharedPrefs sharedPrefs;
    LoadToast loadToast;
    List<String> list;
    ArrayAdapter<String> eventadapter;
    RelativeLayout r1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_mypaper);
        eventspinner= (Spinner) findViewById(R.id.eventnamespinner);
        submitted = (TextView) findViewById(R.id.submitted);
        notsubmitted = (TextView) findViewById(R.id.notsubmitted);
        verfied = (TextView) findViewById(R.id.verfied);
        notverified= (TextView) findViewById(R.id.notverified);
        remarks= (TextView) findViewById(R.id.remarks);
        allthebest= (TextView) findViewById(R.id.allthebeststatus);
        getstatus= (Button) findViewById(R.id.getstatusbtn);
        sharedPrefs=new SharedPrefs(this);
        loadToast = new LoadToast(this);
        r1= (RelativeLayout) findViewById(R.id.r1);
        r1.setVisibility(View.INVISIBLE);
        list = new ArrayList();
        list.add("Select Paper Title");
        loadToast.setText("Loading...");
        eventadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item,list);
       // eventadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventadapter.setDropDownViewResource(R.layout.spinner_dropdown);
        if (sharedPrefs.getLogedInUserName()==null || sharedPrefs.getLogedInKey()==null || sharedPrefs.getEmail()==null){
            sharedPrefs.clearprefs();
            Toast.makeText(getApplicationContext(),"Please Login To Continue",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }else {
            String authkey = sharedPrefs.getLogedInKey();
            username=sharedPrefs.getLogedInUserName();
            eventname = "PaperPresentation";
            geteventregids(authkey,username,eventname);


        }

        getstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = eventspinner.getSelectedItem().toString();
              //  Toast.makeText(getApplicationContext(),"PPT-"+id,Toast.LENGTH_SHORT).show();
                if (id.equals("Select Paper Title") || id.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Select Any Title",Toast.LENGTH_SHORT).show();
                }else{
                    r1.setVisibility(View.INVISIBLE);
                    getstatusupdate(id);
                }
            }
        });


    }



    private void geteventregids(String authkey,String username,String eventname) {

        loadToast.show();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                . writeTimeout(120, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post("https://jbgroup.org.in/sync/sync_mapp/iq_php/new_getall_ppt_regids.php")
                .addBodyParameter("AUTHKEY",sharedPrefs.getLogedInKey())
                .addBodyParameter("UserName",username)
                .addBodyParameter("EventName",eventname)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        loadToast.success();
                        parseeventid(response);
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

    private void parseeventid(JSONArray response) {

        int j = response.length();
        for (int i=0;i<j;i++) {
            JSONObject json;
            try {
                json = response.getJSONObject(i);
                if(!json.has("AuthKeyError")){
                    if(!json.has("NotRegistered")){

                        if(!json.has("NotSubmitted")){

                            if (!json.has("Contact")){

                                String event_reg_id= json.getString("PPT_Title");
                                list.add(event_reg_id);

                            }else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(TrackMypaper.this);
                                builder.setTitle(String.format("Track My Paper"));
                                builder.setMessage("There's been a problem. Please contact Team-Infoquest ASAP.");
                                builder.setPositiveButton("Email", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Intent k = new Intent(Intent.ACTION_SENDTO);
                                        String uriText = "mailto:" + Uri.encode("bhanuteja@errorlabs.in") + "?subject=" +
                                                Uri.encode("Reporting A Probelm Regarding PPT") + "&body=" + Uri.encode("Hello, Team Infoquest \nThere's been a problem regarding my paperpresentation submission. \n Please reslove the issue ASAP.\n\n" +
                                                "Ref: UserName: "+sharedPrefs.getLogedInUserName()+"\n EventName: PaperPresentation \n\n" +
                                                "Thankyou  ...\n\n-Your name");
                                        Uri uri = Uri.parse(uriText);
                                        k.setData(uri);
                                        startActivity(Intent.createChooser(k, "Send Email"));
                                    }
                                });
                                builder.setNegativeButton("Cancel",null);
                                builder.setIcon(R.drawable.applogo);
                                AlertDialog welcomeAlert = builder.create();
                                welcomeAlert.show();
                                ((TextView) welcomeAlert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                            }



                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(TrackMypaper.this);
                            builder.setTitle(String.format("Track My Paper"));
                            builder.setMessage(getResources().getText(R.string.notsubmitted));
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(getApplicationContext(),Home.class));
                                    finish();
                                }
                            });
                            builder.setIcon(R.drawable.applogo);
                            AlertDialog welcomeAlert = builder.create();
                            welcomeAlert.show();
                            ((TextView) welcomeAlert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                        }

                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TrackMypaper.this);
                        builder.setTitle(String.format("Track My Paper"));
                        builder.setMessage("You have not registered for PaperPresentation. Register and submit your abstract to track your paper.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(getApplicationContext(),Home.class));
                                finish();
                            }
                        });
                        builder.setIcon(R.drawable.applogo);
                        AlertDialog welcomeAlert = builder.create();
                        welcomeAlert.show();
                        ((TextView) welcomeAlert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
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
        r1.setVisibility(View.VISIBLE);
        eventspinner.setAdapter(eventadapter);

}

    private void getstatusupdate(String id) {

        loadToast.show();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                . writeTimeout(120, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post("https://jbgroup.org.in/sync/sync_mapp/iq_php/new_get_ppt_status.php")
                .addBodyParameter("AUTHKEY",sharedPrefs.getLogedInKey())
                .addBodyParameter("PPT_Title",id)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        loadToast.success();

                        int j = response.length();
                        for (int i=0;i<j;i++) {
                            JSONObject json;
                            try {
                                json = response.getJSONObject(i);
                                if(!json.has("AuthKeyError")){
                                    if(!json.has("NoDetailsFound")){

                                        r1.setVisibility(View.VISIBLE);
                                        String submittedstatus= json.getString("SubmittedStatus");
                                        String verifiedstatus= json.getString("VerifiedStatus");
                                        String remarks_data= json.getString("Remarks");
                                        remarks.setText(remarks_data);

                                        if (submittedstatus.equals("0")){
                                            submitted.setVisibility(View.INVISIBLE);
                                            notsubmitted.setVisibility(View.VISIBLE);
                                        }else {
                                            submitted.setVisibility(View.VISIBLE);
                                            notsubmitted.setVisibility(View.INVISIBLE);
                                        }
                                        if (verifiedstatus.equals("0")){
                                            verfied.setVisibility(View.INVISIBLE);
                                            notverified.setVisibility(View.VISIBLE);
                                        }else {
                                            verfied.setVisibility(View.VISIBLE);
                                            notverified.setVisibility(View.INVISIBLE);
                                        }
                                        if (submittedstatus.equals("1") && verifiedstatus.equals("1")){
                                            allthebest.setVisibility(View.VISIBLE);
                                        }else {
                                            allthebest.setVisibility(View.GONE);
                                        }
                                        if (remarks_data.isEmpty() || remarks_data.length()<0){
                                            remarks.setVisibility(View.GONE);
                                        }else {
                                            remarks.setVisibility(View.VISIBLE);
                                        }

                                    }else {
                                        Toast.makeText(getApplicationContext(), "No Details Found", Toast.LENGTH_SHORT).show();
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
                        loadToast.error();
                        Toast.makeText(getApplicationContext(), "Please try again after sometime", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Home.class));
                        finish();
                    }
                });
    }

}
