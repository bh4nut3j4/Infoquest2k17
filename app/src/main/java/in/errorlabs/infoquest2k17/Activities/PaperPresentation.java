package in.errorlabs.infoquest2k17.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import in.errorlabs.infoquest2k17.Configs.EventDetailsConfig;
import in.errorlabs.infoquest2k17.Configs.RegisteredEventConfig;
import in.errorlabs.infoquest2k17.Configs.SharedPrefs;
import in.errorlabs.infoquest2k17.R;
import okhttp3.OkHttpClient;

import static com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype.Fall;

public class PaperPresentation extends AppCompatActivity {

    CheckBox checkBox;
    Button button;
    SharedPrefs sharedPrefs;
    LoadToast loadToast;
    TextView eventdescription, eventrules, no_of_participants, price_per_head, cod1, cod1_no, cod2, cod2_no;
    ImageView eventimg;
    ProgressBar progressBar;
    RelativeLayout rel_lay;
    EditText referrer;
    NiftyDialogBuilder dialogBuilder,eventhold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.event_registration);
        setContentView(R.layout.paper_presentation);
        final Bundle bundle = getIntent().getExtras();
        final String eventname = "PaperPresentation";
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(eventname);
        loadToast = new LoadToast(this);
        loadToast.setText("Loading...");
        sharedPrefs = new SharedPrefs(this);
        eventimg = (ImageView) findViewById(R.id.eventimg);
        eventdescription = (TextView) findViewById(R.id.eventdescription);
        eventrules = (TextView) findViewById(R.id.eventrules);
        no_of_participants = (TextView) findViewById(R.id.noparticipants);
        price_per_head = (TextView) findViewById(R.id.price_per_head);
        cod1 = (TextView) findViewById(R.id.CO_NAME_1);
        cod1_no = (TextView) findViewById(R.id.CO_NO_1);
        cod2 = (TextView) findViewById(R.id.CO_NAME_2);
        cod2_no = (TextView) findViewById(R.id.CO_NO_2);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        rel_lay = (RelativeLayout) findViewById(R.id.rel_lay);
        String appkey = getString(R.string.APPKEY);
        geteventdetails(appkey, eventname);
        checkBox = (CheckBox) findViewById(R.id.terms_checkbox);
        button = (Button) findViewById(R.id.eventslotbtn);
        referrer= (EditText) findViewById(R.id.referrer);
        dialogBuilder=new NiftyDialogBuilder(this,R.style.AppTheme);
        eventhold=new NiftyDialogBuilder(this,R.style.AppTheme);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if(sharedPrefs.getLogedInKey()==null||sharedPrefs.getLogedInUserName()==null||sharedPrefs.getEmail()==null){
                        sharedPrefs.clearprefs();
                        sharedPrefs.clearResetEmail();
                        Toast.makeText(getApplicationContext(), "Please Login To Proceed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Login.class));
                        finish();
                    }else {
                        button.setEnabled(true);
                    }

                } else {
                    button.setEnabled(false);
                }

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sharedPrefs.getLogedInKey()==null||sharedPrefs.getLogedInUserName()==null||sharedPrefs.getEmail()==null){
                    Toast.makeText(getApplicationContext(), "Please Login To Proceed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Login.class));
                    finish();
                }else {
                    String name = eventname;
                    String amount = price_per_head.getText().toString();
                    String no_of_prtcpnts =no_of_participants.getText().toString();
                    int amount_to_pay=Integer.parseInt(no_of_prtcpnts)*Integer.parseInt(amount);
                    int amountoff=amount_to_pay/10;
                    int finalamount = amount_to_pay-amountoff;
                    if(Integer.parseInt(no_of_prtcpnts)==1){
                        String msg ="Proceed to register for "+name+"? \n\n" +
                                "Amount to be paid at the venue on the day of IQ'17 is Rs: "+amount_to_pay+".\n\n" +
                                "Pay through PAYTM(at the venue) and get 10% OFF on the total amount.\n";
                        proceedtoregister(name,msg );
                    }else {
                        String msg="Proceed to register for "+name+"? \n\n" +
                                "Amount to be paid at the venue on the day of IQ'17 is Rs: "+amount_to_pay+" (FOR TOTAL TEAM) .\n\n" +
                                "Pay through PAYTM(at the venue) and get 10% OFF on the total amount.\n";
                        proceedtoregister(name,msg);
                    }

                }

            }
        });

        cod1_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number= cod1_no.getText().toString();
                call(number);
            }
        });
        cod2_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number= cod2_no.getText().toString();
                call(number);
            }
        });

    }

    public void geteventdetails(String appkey, String eventname) {
        loadToast.show();
        rel_lay.setVisibility(View.GONE);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.post(EventDetailsConfig.NewEventDetailsUrl)
                .addBodyParameter("APPKEY", appkey)
                .addBodyParameter("EventName", eventname)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("LOG", "RESPONSE" + response);
                        loadToast.success();
                        parsedata(response);
                    }
                    @Override
                    public void onError(ANError anError) {
                        loadToast.error();
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        finish();

                    }
                });
    }

    public void parsedata(JSONArray array) {
        rel_lay.setVisibility(View.VISIBLE);
        int j = array.length();
        for (int i = 0; i < j; i++) {
            JSONObject json;
            try {
                json = array.getJSONObject(i);
                if (!json.has("AppKeyError")) {

                    if (!json.has("NoEventName")) {

                        Glide.with(getApplicationContext()).load(json.getString(EventDetailsConfig.EventImgUrl)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(android.R.drawable.ic_dialog_alert).listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(eventimg);
                        eventdescription.setText(json.getString(EventDetailsConfig.EventDescription));
                        eventrules.setText(json.getString(EventDetailsConfig.EventRules));
                        cod1.setText(json.getString(EventDetailsConfig.EventCoordinator1));
                        cod1_no.setText(json.getString(EventDetailsConfig.EventCoNumber1));
                        cod2.setText(json.getString(EventDetailsConfig.EventCoordinator2));
                        cod2_no.setText(json.getString(EventDetailsConfig.EventCoNumber2));
                        price_per_head.setText(json.getString(EventDetailsConfig.EventPricePerHead));
                        no_of_participants.setText(json.getString(EventDetailsConfig.EventParticipantNo));
                    } else {
                        Toast.makeText(getApplicationContext(), "No Event Details Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "AppKeyAuthenticationFailure", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void call(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }


    public void proceedtoregister(final String eventname, String msg){

        final String key = sharedPrefs.getLogedInKey();
        final String uname = sharedPrefs.getLogedInUserName();
        String amount = price_per_head.getText().toString();
        String no_of_prtcpnts =no_of_participants.getText().toString();
        final int amount_to_pay=Integer.parseInt(no_of_prtcpnts)*Integer.parseInt(amount);
        int amountoff=amount_to_pay/10;
        int finalamount = amount_to_pay-amountoff;
        final String referrerdetails=referrer.getText().toString();
        dialogBuilder
                .withTitle(eventname)
                // .withTitleColor("#FFFFFF")
                // .withDividerColor("#11000000")
                .withMessage(msg)
                //.withMessageColor("FFFFFFFF")
                .withDialogColor("#455a64")
                .withIcon(getResources().getDrawable(R.drawable.applogo))
                .withDuration(500)
                .withEffect(Fall)
                .withButton1Text("Cancel")
                .withButton2Text("Proceed")
                .isCancelableOnTouchOutside(true)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.cancel();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadToast.show();
                        String amount = String.valueOf(amount_to_pay);
                        // String email=sharedPrefs.getEmail();
                        registerevent(key,uname,eventname,amount,referrerdetails);
                    }
                })
                .show();
    }

    public void registerevent(String key,String uname,String eventname,String amount,String referrer){
        dialogBuilder.dismiss();
        loadToast.setText("Registering...");
        loadToast.show();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                . writeTimeout(120, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(RegisteredEventConfig.newfinaleventregistration)
                .addBodyParameter("AUTHKEY",key)
                .addBodyParameter("UserName", uname)
                .addBodyParameter("Email", sharedPrefs.getEmail())
                .addBodyParameter("EventName", eventname)
                .addBodyParameter("Amount", amount)
                .addBodyParameter("Referrer", referrer)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                       // Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                        loadToast.success();

                        int j = response.length();
                        for (int i = 0; i < j; i++) {
                            JSONObject json;
                            try {
                                json = response.getJSONObject(i);

                                if(!json.has("AppKeyError")){

                                    if (!json.has("ExceptionOccured")){

                                        if (!json.has("RegistrationFailed")){

                                            if (!json.has("EventInHold")){

                                                if (json.has("RegistrationSuccess")){

                                                    Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(),RegisteredEvents.class));
                                                    finish();

                                                }else{
                                                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(),Home.class));
                                                    finish();
                                                }

                                            }else{
                                                if (dialogBuilder.isShowing()){
                                                    dialogBuilder.cancel();
                                                }

                                                dialogBuilder
                                                        .withTitle("EVENT ON HOLD")
//                                                        .withTitleColor("#FFFFFF")
                                                        //.withDividerColor("#11000000")
                                                        .withMessage(getString(R.string.eventhold))
                                                        .withMessageColor("#FFFFFFFF")
                                                        .withDialogColor("#455a64")
                                                        .withIcon(getResources().getDrawable(R.drawable.applogo))
                                                        .withDuration(500)
                                                        .withEffect(Fall)
                                                        .withButton1Text("SHOW ME")
                                                        .isCancelableOnTouchOutside(false)
                                                        .setButton1Click(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                startActivity(new Intent(getApplicationContext(),RegisteredEvents.class));
                                                                finish();
                                                            }
                                                        })
                                                        .show();
                                            }

                                        }else {

                                            Toast.makeText(getApplicationContext(), "Registration Failed,Try Again", Toast.LENGTH_SHORT).show();
                                        }

                                    }else{
                                        Toast.makeText(getApplicationContext(), "Exception Occured, Try Again", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(),"error"+anError.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

}

