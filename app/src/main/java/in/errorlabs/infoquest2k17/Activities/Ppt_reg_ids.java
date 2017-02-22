package in.errorlabs.infoquest2k17.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
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

import java.util.concurrent.TimeUnit;

import in.errorlabs.infoquest2k17.Configs.SharedPrefs;
import in.errorlabs.infoquest2k17.R;
import okhttp3.OkHttpClient;

public class Ppt_reg_ids extends AppCompatActivity {

    EditText regid1,regid2;
    Button submit;
    TextView help;
    String rid1,rid2;
    LoadToast loadToast;
    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ppt_reg_ids);
        regid1= (EditText) findViewById(R.id.regid1);
        regid2= (EditText) findViewById(R.id.regid2);
        submit = (Button) findViewById(R.id.submit);
        help = (TextView) findViewById(R.id.help);
        loadToast = new LoadToast(this);
        sharedPrefs=new SharedPrefs(this);
        loadToast.setText("Loading...");
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Ppt_reg_ids.this);
                builder.setTitle("PaperPresentation");
                builder.setMessage(getResources().getText(R.string.ppt_help));
                builder.setPositiveButton("OK", null);
                builder.setIcon(R.drawable.applogo);
                AlertDialog welcomeAlert = builder.create();
                welcomeAlert.show();
                ((TextView) welcomeAlert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rid1=regid1.getText().toString();
                rid2=regid2.getText().toString();
                if (rid1.isEmpty() || rid1.length()<0 || rid1.equals("")){
                    regid1.setText(null);
                    regid1.setError("required");
                    Toast.makeText(getApplicationContext(),"Please Enter a Valid Registration ID",Toast.LENGTH_SHORT).show();
                }else if (rid2.isEmpty() || rid2.length()<0 || rid2.equals("")){
                    rid2="NA";
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Ppt_reg_ids.this);
                    builder.setTitle("PaperPresentation");
                    builder.setMessage("Are you sure that the presentation is given by only 1 participant ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent k = new Intent(Intent.ACTION_SENDTO);
                            String uriText = "mailto:" + Uri.encode("infoquest@jbiet.edu.in") + "?subject=" +
                                    Uri.encode("IQ'17 Abstract Submission") + "&body=" + Uri.encode("Hello, Team Infoquest \n\n\n Name :\n\nContact Number :\n\n College Name :\n\n Abstract Title : \n\n Event Reg ID : "+rid1+"  \n\nEvent Reg ID(Partner) : "+rid2+" \n\n\nAttach your abstract as an attachment.");
                            Uri uri = Uri.parse(uriText);
                            k.setData(uri);
                            startActivity(Intent.createChooser(k, "Send Email"));
                        }
                    });
                    builder.setNegativeButton("No",null);
                    builder.setIcon(R.drawable.applogo);
                    AlertDialog welcomeAlert = builder.create();
                    welcomeAlert.show();
                    ((TextView) welcomeAlert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                }else {

                    loadToast.show();
                    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                            .connectTimeout(120, TimeUnit.SECONDS)
                            .readTimeout(120, TimeUnit.SECONDS)
                            . writeTimeout(120, TimeUnit.SECONDS)
                            .build();

                    AndroidNetworking.post("https://jbgroup.org.in/sync/sync_mapp/iq_php/new_getall_ppt_regids.php")
                            .addBodyParameter("AUTHKEY",sharedPrefs.getLogedInKey())
                            .addBodyParameter("EventRegID1",rid1)
                            .addBodyParameter("EventRegID2",rid2)
                            .setPriority(Priority.MEDIUM)
                            .setOkHttpClient(okHttpClient)
                            .build()
                            .getAsJSONArray(new JSONArrayRequestListener() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    loadToast.success();
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
        });
    }
}
