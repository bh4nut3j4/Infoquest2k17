package in.errorlabs.infoquest2k17.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

import java.util.concurrent.TimeUnit;

import in.errorlabs.infoquest2k17.Configs.ForgotPasswordConfig;
import in.errorlabs.infoquest2k17.Configs.SharedPrefs;
import in.errorlabs.infoquest2k17.R;
import in.errorlabs.infoquest2k17.Utils.Connection;
import okhttp3.OkHttpClient;

public class ForgotPassword extends AppCompatActivity {

    EditText email,code,passwd,cpasswd;
    Button reset_email,reset_code,updatebtn;
    Connection connection;
    LoadToast loadToast;
    TextView note;
    RelativeLayout r1,r2,r3;
    SharedPrefs sharedPrefs;

    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            . writeTimeout(120, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        email= (EditText) findViewById(R.id.ed_fpasswd_email);
        code= (EditText) findViewById(R.id.ed_resetcode);
        passwd= (EditText) findViewById(R.id.passwd);
        cpasswd= (EditText) findViewById(R.id.cpasswd);
        reset_email= (Button) findViewById(R.id.resetbtn_email);
        reset_code= (Button) findViewById(R.id.resetbtn_code);
        updatebtn= (Button) findViewById(R.id.updatebtn);
        note= (TextView) findViewById(R.id.note);
        r1= (RelativeLayout) findViewById(R.id.r1);
        r2= (RelativeLayout) findViewById(R.id.r2);
        r3= (RelativeLayout) findViewById(R.id.r3);
        connection= new Connection(this);
        loadToast = new LoadToast(this);
        sharedPrefs=new SharedPrefs(this);
        loadToast.setText("Loading...");
        reset_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString();
                if(isValidEmail(mail)){
                    Boolean checkinternet =(connection.isInternet());
                    if (checkinternet) {
                        loadToast.show();
                        Log.d("TAG","online startted");
                        String appkey= getString(R.string.APPKEY);
                        sendresetcode(appkey,mail);
                    } else {
                        Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Login.class));
                        finish();
                    }

                }else {
                    email.setText(null);
                    email.setError("Enter Valid Email");
                }
            }
        });

        reset_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rcode = code.getText().toString();
                    Boolean checkinternet =(connection.isInternet());
                    if (checkinternet) {
                        loadToast.show();
                        Log.d("TAG","online startted");
                        String appkey= getString(R.string.APPKEY);
                        String resetemail=sharedPrefs.getResetEmail();
                        Toast.makeText(getApplicationContext(),resetemail,Toast.LENGTH_SHORT).show();
                        verifycode(appkey,rcode,resetemail);
                    } else {
                        Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Login.class));
                        finish();
                    }


            }
        });

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass=passwd.getText().toString();
                String cpass=cpasswd.getText().toString();
                if (pass.length()>=8){

                    if(cpass.length()>=8){
                        if (pass.equals(cpass)){
                            Boolean checkinternet =(connection.isInternet());
                            if (checkinternet) {
                                loadToast.show();
                                String email=sharedPrefs.getResetEmail();
                                String appkey=getString(R.string.APPKEY);
                                updatepasswords(appkey,email,pass,cpass);
                            } else {
                                Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Login.class));
                                finish();
                            }

                        }else {
                            passwd.setText(null);
                            cpasswd.setText(null);
                        }
                    }else {
                        cpasswd.setText(null);
                        cpasswd.setError("Min 8 Characters");
                        Toast.makeText(getApplicationContext(),"Passwords doesnt match",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    passwd.setText(null);
                    passwd.setError("Min 8 Characters");
                }
            }
        });


    }
    public  static boolean isValidEmail(CharSequence target) {

        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public void sendresetcode(String  appkey, final String mail_id){

       // Toast.makeText(getApplicationContext(),mail_id, Toast.LENGTH_SHORT).show();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                . writeTimeout(120, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(ForgotPasswordConfig.ForgotpasswdUrl)
                .addBodyParameter("APPKEY",appkey)
                .addBodyParameter("Email",mail_id)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                    loadToast.success();
                       // Toast.makeText(getApplicationContext(), "fafas"+response.toString(), Toast.LENGTH_SHORT).show();
                        int j = response.length();
                        for (int i = 0; i < j; i++) {
                            JSONObject json;
                            try {
                                json = response.getJSONObject(i);
                                if (!json.has("AppKeyError")) {

                                    if (!json.has("MailNotfoundError")) {

                                        if (!json.has("MailNotSentError")) {

                                            if (json.has("MailSent")) {
                                                sharedPrefs.setResetEmail(mail_id);
                                                r1.setVisibility(View.GONE);
                                                r2.setVisibility(View.VISIBLE);

                                            } else {
                                                Toast.makeText(getApplicationContext(), "verification code not sent", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Unable to send verificaition code", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),Login.class));
                                            finish();
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Email Not Found", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),Login.class));
                                        finish();
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "please try after sometime", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),Login.class));
                                    finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Please try after sometime", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Login.class));
                                finish();
                            }
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        loadToast.error();
                        Toast.makeText(getApplicationContext(), "Check Your Network Connection", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void verifycode(String appkey, final String rcode, String email){
        AndroidNetworking.post(ForgotPasswordConfig.VerifyCodeUrl)
                .addBodyParameter("APPKEY",appkey)
                .addBodyParameter("Email", email)
                .addBodyParameter("Code", rcode)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        loadToast.success();
                        int j = response.length();
                        for (int i = 0; i < j; i++) {
                            JSONObject json;
                            try {
                                json = response.getJSONObject(i);


                                if (!json.has("AppKeyError")) {

                                    if (!json.has("InvalidCode")) {

                                        if (json.has("CodeMatch")) {

                                            r3.setVisibility(View.VISIBLE);
                                            r2.setVisibility(View.GONE);

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Code didnt match", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),Login.class));
                                            finish();
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Invalid Verification Code", Toast.LENGTH_SHORT).show();
                                        code.setText(null);
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "please try after sometime", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),Login.class));
                                    finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Please try after sometime", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Login.class));
                                finish();
                            }
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        loadToast.error();
                        Toast.makeText(getApplicationContext(), "Check Your Network Connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updatepasswords(String appkey,String email,String pass,String cpass){
        AndroidNetworking.post(ForgotPasswordConfig.UpdatepassUrl)
                .addBodyParameter("APPKEY",appkey)
                .addBodyParameter("Email", email)
                .addBodyParameter("Pass", pass)
                .addBodyParameter("Cpass", cpass)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        loadToast.success();
                        //  Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_SHORT).show();
                        int j = response.length();
                        for (int i = 0; i < j; i++) {
                            JSONObject json;
                            try {
                                json = response.getJSONObject(i);

                                if(!json.has("AppKeyError")){

                                    if (!json.has("InsertionError")){

                                        if (!json.has("EmailError")){

                                            if (!json.has("PasswordError")){

                                                if (!json.has("PasswordMatchError")){

                                                    if(json.has("Updated")){
                                                        sharedPrefs.clearprefs();
                                                        Toast.makeText(getApplicationContext(),"Password Succesfully Changed",Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(),Login.class));
                                                        finish();
                                                    }else {
                                                        Toast.makeText(getApplicationContext(), "Could not reset", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(),Login.class));
                                                        finish();
                                                    }
                                                }else{
                                                            Toast.makeText(getApplicationContext(), "Passwords Doesnt Match", Toast.LENGTH_SHORT).show();
                                                }

                                            }else{
                                                        Toast.makeText(getApplicationContext(), "Min 8 Characters", Toast.LENGTH_SHORT).show();
                                            }

                                        }else{
                                                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                                        }

                                    }else{
                                        Toast.makeText(getApplicationContext(), "Server Maintenance, Please try after sometime", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), "Check Your Network Connection", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
