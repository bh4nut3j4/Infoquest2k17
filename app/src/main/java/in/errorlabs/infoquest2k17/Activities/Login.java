package in.errorlabs.infoquest2k17.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

import in.errorlabs.infoquest2k17.Configs.LoginConfig;
import in.errorlabs.infoquest2k17.Configs.SharedPrefs;
import in.errorlabs.infoquest2k17.R;
import in.errorlabs.infoquest2k17.Utils.Connection;
import okhttp3.OkHttpClient;


public class Login extends AppCompatActivity {

        private EditText mEmailView, mPasswordView;

        TextView registertext;
        TextView skip,forgotpassword;
        LoadToast loadToast;
        SharedPrefs sharedPrefs;
        Connection connection;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login);
            registertext= (TextView) findViewById(R.id.registertext);
            skip= (TextView) findViewById(R.id.loginskip);
            ActionBar actionBar = getSupportActionBar();
            mEmailView = (EditText) findViewById(R.id.loginemail);
            loadToast=new LoadToast(this);
            loadToast.setText("Loading...");
            sharedPrefs=new SharedPrefs(this);
            connection = new Connection(this);
            AndroidNetworking.initialize(getApplicationContext());

            mPasswordView = (EditText) findViewById(R.id.loginpassword);

            Button login = (Button) findViewById(R.id.loginbutton);
            login.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean checkinternet =(connection.isInternet());
                    if (checkinternet) {
                        Log.d("TAG","online startted");
                        attemptLogin();
                    } else {
                        Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Login.class));
                        finish();
                    }
                }
            });
            forgotpassword= (TextView) findViewById(R.id.forgotpassword);
            forgotpassword.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),ForgotPassword.class));
                }
            });

            registertext.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),Register.class));
                }
            });

            skip.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),Home.class));
                }
            });

        }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

        private void attemptLogin() {
            String getemail = mEmailView.getText().toString();
            String getpassword = mPasswordView.getText().toString();
            String appkey = getString(R.string.APPKEY);

            if (isValidEmail(getemail)){
                if(getpassword.length()>=8){
                    UserLoginTask(appkey,getemail,getpassword);
                }else {
                    Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
            }

        }


        public void UserLoginTask(final String appkey,final String email, final String password) {

            loadToast.show();
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    . writeTimeout(120, TimeUnit.SECONDS)
                    .build();
            AndroidNetworking.post(LoginConfig.LoginUrl)
                    .addBodyParameter("APPKEY", appkey)
                    .addBodyParameter("email", email)
                    .addBodyParameter("password", password)
                    .setPriority(Priority.MEDIUM)
                    .setOkHttpClient(okHttpClient)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            loadToast.success();
                            Log.d("LOG", "RESPONSE" + response);
                           // Toast.makeText(getApplicationContext(), "Response" + response.toString(), Toast.LENGTH_SHORT).show();
                            int j = response.length();
                            for (int i = 0; i < j; i++) {
                                JSONObject json;
                                try {
                                    json = response.getJSONObject(i);
                                    if(!json.has("AppKeyError")){

                                        if (!json.has("AuthenticationError")){
                                            String LogedInUserName = json.getString(SharedPrefs.LogedInUserName);
                                            String LogedInEmail = json.getString(SharedPrefs.LogedInEmail);
                                            String LogedInAuthKey = json.getString(SharedPrefs.LogedInKey);
                                           // Toast.makeText(getApplicationContext(), "Details"+LogedInAuthKey+LogedInUserName+LogedInEmail, Toast.LENGTH_SHORT).show();
                                            sharedPrefs.saveprefs(LogedInUserName, LogedInEmail, LogedInAuthKey);
                                           // Toast.makeText(getApplicationContext(), "sharedprfs"+sharedPrefs.getEmail()+sharedPrefs.getLogedInKey()+sharedPrefs.getLogedInUserName(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),Home.class));
                                            finish();
                                        }else {
                                            mEmailView.setText(null);
                                            mPasswordView.setText(null);
                                            Toast.makeText(getApplicationContext(), "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                                        }

                                    }else {
                                        Toast.makeText(getApplicationContext(), "AppKeyAuthenticationFailure", Toast.LENGTH_SHORT).show();
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
                            Log.d("LOG", "RESPONSE" + anError);
                            Toast.makeText(getApplicationContext(), "Check Your Network Connection", Toast.LENGTH_SHORT).show();

                        }
                    });
        }

        public  static boolean isValidEmail(CharSequence target) {

            return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }




    }

