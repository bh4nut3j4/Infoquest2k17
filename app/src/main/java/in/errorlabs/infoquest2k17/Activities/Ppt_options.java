package in.errorlabs.infoquest2k17.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import in.errorlabs.infoquest2k17.R;

public class Ppt_options extends AppCompatActivity {

    ImageView img;
    Button upload,email;
    String url="https://jbiet-my.sharepoint.com/personal/infoquest_jbiet_edu_in/_layouts/15/guestaccess.aspx?docid=156e0c580747c415c85a91168c5987559&authkey=AVbWry9ANlLAupR168Dsyec";
    String uri="https://forms.zohopublic.com/bhanutejar07/form/Infoquest17/formperma/4g4BdjBg_4ddm6Ca623g8530G";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppt_options);
        img= (ImageView) findViewById(R.id.home_imgview);
        upload= (Button) findViewById(R.id.upload);
        email= (Button) findViewById(R.id.email);
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
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),Ppt_reg_ids.class));
               /* Intent i = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto:" + Uri.encode("infoquest@jbiet.edu.in") + "?subject=" +
                        Uri.encode("IQ'17 Abstract Submission") + "&body=" + Uri.encode("Hello, Team Infoquest \n\n\n Name :\n\nContact Number :\n\n College Name :\n\n Abstract Title : \n\n Event Reg ID :  \n\n\nAttach your abstract as an attachment.");
                Uri uri = Uri.parse(uriText);
                i.setData(uri);
                startActivity(Intent.createChooser(i, "Send Email"));*/
            }
        });
    }




}
