package in.errorlabs.infoquest2k17.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.errorlabs.infoquest2k17.Configs.RegisterConfig;
import in.errorlabs.infoquest2k17.R;
import in.errorlabs.infoquest2k17.Utils.Connection;
import okhttp3.OkHttpClient;

public class Register extends AppCompatActivity {

    EditText FirstName,LastName,Email,CollegeName,PhoneNumber,Password,ConfirmPassword;
    Button registerbtn;
    LoadToast loadToast;
    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        ActionBar actionBar = getSupportActionBar();
          actionBar.setTitle("Sign Up");
        FirstName= (EditText) findViewById(R.id.FirstName);
        LastName= (EditText) findViewById(R.id.LastName);
        Email= (EditText) findViewById(R.id.Email);
        CollegeName= (EditText) findViewById(R.id.CollegeName);
        PhoneNumber= (EditText) findViewById(R.id.PhoneNumber);
        Password= (EditText) findViewById(R.id.Password);
        ConfirmPassword= (EditText) findViewById(R.id.ConfirmPassword);
        loadToast= new LoadToast(this);
        loadToast.setText("Loading...");
        registerbtn= (Button) findViewById(R.id.registerbtn);
        connection= new Connection(this);
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname=FirstName.getText().toString();
                String lname=LastName.getText().toString();
                String email=Email.getText().toString();
                String clgname=CollegeName.getText().toString();
                String phone=PhoneNumber.getText().toString();
                String pass=Password.getText().toString();
                String cpass=ConfirmPassword.getText().toString();

                if (namevalidator(fname)){
                    Log.d("LOGG","FNAME");

                    if (namevalidator(lname)){
                        Log.d("LOGG","LNAME");

                        if(isValidEmail(email)){
                            Log.d("LOGG","Email");

                            if (namevalidator(clgname)){
                                Log.d("LOGG","CLGNAME");

                                if (numbervalidator(phone)){
                                    Log.d("LOGG","CLGNAME");

                                    if (Password.length()>=8){
                                        Log.d("LOGG","CLGNAME");

                                        if (ConfirmPassword.length()>=8){
                                            Log.d("LOGG","CLGNAME");

                                            if (pass.equals(cpass)){
                                                Log.d("LOGG","CLGNAME");

                                                Boolean checkinternet =(connection.isInternet());
                                                if (checkinternet) {
                                                    Log.d("TAG","online startted");
                                                    String appkey= getString(R.string.APPKEY);
                                                    register(appkey,fname,lname,email,clgname,phone,pass,cpass);
                                                } else {
                                                    Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(),Login.class));
                                                    finish();
                                                }
                                            }else {
                                                Password.setText(null);
                                                ConfirmPassword.setText(null);
                                                Toast.makeText(getApplicationContext(),"Passwords dont match",Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            ConfirmPassword.setText(null);
                                            ConfirmPassword.setError("Min 8 Characters");
                                        }

                                    }else {
                                        Password.setText(null);
                                        Password.setError("Min 8 Characters");
                                    }

                                }else {
                                    PhoneNumber.setText(null);
                                    PhoneNumber.setError("Enter Valid Number");
                                }
                            }else {
                                CollegeName.setText(null);
                                CollegeName.setError("Enter Valid Name(no spaces)");
                            }
                        }else {
                            Email.setText(null);
                            Email.setError("Enter Valid Email");
                        }

                    }else {
                        LastName.setText(null);
                        LastName.setError("Enter Valid Name (no spaces)");
                    }

                }else {
                    FirstName.setText(null);
                    FirstName.setError("Enter Valid Name (no spaces)");
                }

            }
        });


    }

    public  static boolean isValidEmail(CharSequence target) {

        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public boolean namevalidator(String name){
        Pattern pattern;
         Matcher matcher;
        final String USERNAME_PATTERN = "^[a-zA-Z ]{2,25}$";
        pattern = Pattern.compile(USERNAME_PATTERN);
        matcher = pattern.matcher(name);
        return matcher.matches();
    }
    public boolean numbervalidator(String number){
        Pattern pattern;
        Matcher matcher;
        final String USERNAME_PATTERN = "^[0-9]{10,10}$";
        pattern = Pattern.compile(USERNAME_PATTERN);
        matcher = pattern.matcher(number);
        return matcher.matches();
    }

    public void register(final String AppKey,final String FirstName, final String LastName, final String Email, final String CollegeName, final String PhoneNumber, final String Password, final String ConfirmPassword){

        loadToast.show();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                . writeTimeout(120, TimeUnit.SECONDS)
                .build();
       
        AndroidNetworking.post(RegisterConfig.RegisterUrl)
                .addBodyParameter("APPKEY",AppKey)
                .addBodyParameter("fname", FirstName)
                .addBodyParameter("lname", LastName)
                .addBodyParameter("email", Email)
                .addBodyParameter("collegename", CollegeName)
                .addBodyParameter("phonenumber", PhoneNumber)
                .addBodyParameter("password", Password)
                .addBodyParameter("cpassword", ConfirmPassword)
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

                                    if (!json.has("FNameError")){

                                        if (!json.has("LNameError")){

                                            if (!json.has("EmailError")){

                                                if (!json.has("PasswordError")){

                                                    if (!json.has("PasswordMatchError")){

                                                    Toast.makeText(getApplicationContext(),"Successfully Registered, Please Login In to Continue",Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(),Login.class));
                                                        finish();

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
                                            Toast.makeText(getApplicationContext(), "Invalid LastName", Toast.LENGTH_SHORT).show();
                                        }

                                    }else {

                                        Toast.makeText(getApplicationContext(), "Invalid FirstName", Toast.LENGTH_SHORT).show();
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
                        loadToast.error();
                        Log.d("LOG", "RESPONSE" + anError);

                        Toast.makeText(getApplicationContext(), "Check Your Network Connection", Toast.LENGTH_SHORT).show();
                    }
                });

    }


}
