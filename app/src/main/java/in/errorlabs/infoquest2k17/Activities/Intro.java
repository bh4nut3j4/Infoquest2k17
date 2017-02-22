package in.errorlabs.infoquest2k17.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import in.errorlabs.infoquest2k17.Configs.SharedPrefs;
import in.errorlabs.infoquest2k17.R;

/**
 * Created by root on 1/24/17.
 */

public class Intro  extends IntroActivity {
//public class Intro  extends AhoyOnboarderActivity {

    SharedPrefs sharedPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        setFullscreen(true);
        super.onCreate(savedInstanceState);
        sharedPrefs=new SharedPrefs(this);
        setFinishEnabled(true);
        setButtonBackVisible(false);
        setButtonNextVisible(false);

        setNavigationPolicy(new NavigationPolicy() {
            @Override
            public boolean canGoForward(int position) {
                return position != 5;
            }
            @Override
            public boolean canGoBackward(int position) {
                return position != 0;
            }
        });
        addSlide(new SimpleSlide.Builder()
                .title("InfoQuest'17")
                .description("Welcome")
                .image(R.drawable.applogo)
                .background(R.color.color_canteen)
                .backgroundDark(R.color.color_dark_canteen)
                .build());
        addSlide(new SimpleSlide.Builder()
               // .title("How this app works?")
                .description("How this app works?")
                .image(R.drawable.qstn)
                .background(R.color.color_custom_fragment_2)
                .backgroundDark(R.color.color_dark_custom_fragment_2)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Create an Account")
                .description("Register and Login")
                .image(R.drawable.login)
                .background(R.color.color_material_bold)
                .backgroundDark(R.color.color_dark_material_bold)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Register Events")
                .description("Go through the various events and register for the events you want to participate in.")
                .image(R.drawable.reg)
                .background(R.color.color_material_motion)
                .backgroundDark(R.color.color_dark_material_motion)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Get QR code")
                .description("Get an Unique Qr code for every event you register")
                .image(R.drawable.getqr)
                .background(R.color.color_custom_fragment_1)
                .backgroundDark(R.color.color_dark_custom_fragment_1)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Scan and Attend")
                .description("Show the Qr code at the venue and attend the event")
                .image(R.drawable.scan)
                .background(R.color.color_permissions)
                .backgroundDark(R.color.color_dark_permissions)
                .buttonCtaLabel("Get Started")
                .buttonCtaClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (sharedPrefs.getLogedInUserName()==null){
                            startActivity(new Intent(getApplicationContext(),Login.class));
                            finish();
                        }else{
                            startActivity(new Intent(getApplicationContext(),Home.class));
                            finish();
                        }
                    }
                })
                .build());
      /*  setGradientBackground();

        AhoyOnboarderCard c1 = new AhoyOnboarderCard("Infoquest'17", "Welcome", R.drawable.applogo);
        c1.setBackgroundColor(R.color.black_transparent_image);
        c1.setTitleColor(R.color.white);
        c1.setDescriptionColor(R.color.white);
        c1.setTitleTextSize(dpToPixels(15, this));
        c1.setDescriptionTextSize(dpToPixels(10, this));
        c1.setIconLayoutParams((int)dpToPixels(300,this),(int) (dpToPixels(300,this)),5,5,5,5);


        AhoyOnboarderCard c2 = new AhoyOnboarderCard("How this app works ?","",R.drawable.qstn);
        c2.setBackgroundColor(R.color.black_transparent_image);
        c2.setTitleColor(R.color.white);
        c2.setDescriptionColor(R.color.white);
        c2.setTitleTextSize(dpToPixels(15, this));
        c2.setDescriptionTextSize(dpToPixels(10, this));
        c1.setIconLayoutParams((int)dpToPixels(300,this),(int) (dpToPixels(300,this)),5,5,5,5);



        AhoyOnboarderCard c3 = new AhoyOnboarderCard("Create an Account", "Register and Login", R.drawable.login);
        c3.setBackgroundColor(R.color.black_transparent_image);
        c3.setTitleColor(R.color.white);
        c3.setDescriptionColor(R.color.white);
        c3.setTitleTextSize(dpToPixels(15, this));
        c3.setDescriptionTextSize(dpToPixels(10, this));
        c1.setIconLayoutParams((int)dpToPixels(300,this),(int) (dpToPixels(300,this)),5,5,5,5);

        AhoyOnboarderCard c4 = new AhoyOnboarderCard("Register Events ", "Go through the various events and register for the events you want to participate in.", R.drawable.reg);
        c4.setBackgroundColor(R.color.black_transparent_image);
        c4.setTitleColor(R.color.white);
        c4.setDescriptionColor(R.color.white);
        c4.setTitleTextSize(dpToPixels(15, this));
        c4.setDescriptionTextSize(dpToPixels(10, this));
        c1.setIconLayoutParams((int)dpToPixels(300,this),(int) (dpToPixels(300,this)),5,5,5,5);

        AhoyOnboarderCard c5 = new AhoyOnboarderCard("Get the Qr", "Get a Unique Qr code for every event you register", R.drawable.getqr);
        c5.setBackgroundColor(R.color.black_transparent_image);
        c5.setTitleColor(R.color.white);
        c5.setDescriptionColor(R.color.white);
        c5.setTitleTextSize(dpToPixels(15, this));
        c5.setDescriptionTextSize(dpToPixels(10, this));
        c1.setIconLayoutParams((int)dpToPixels(300,this),(int) (dpToPixels(300,this)),5,5,5,5);

        AhoyOnboarderCard c6 = new AhoyOnboarderCard("Scan and Attend", "Show the Qr code at the venue and attend the event", R.drawable.scan);
        c6.setBackgroundColor(R.color.black_transparent_image);
        c6.setTitleColor(R.color.white);
        c6.setDescriptionColor(R.color.white);
        c6.setTitleTextSize(dpToPixels(15, this));
        c6.setDescriptionTextSize(dpToPixels(10, this));
        c1.setIconLayoutParams((int)dpToPixels(300,this),(int) (dpToPixels(300,this)),5,5,5,5);


        List<AhoyOnboarderCard> pages = new ArrayList<>();
        pages.add(c1);
        pages.add(c2);
        pages.add(c3);
        pages.add(c4);
        pages.add(c5);
        pages.add(c6);


        setOnboardPages(pages);

    }

    @Override
    public void onFinishButtonPressed() {
        if (sharedPrefs.getLogedInUserName()==null){
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }else{
            startActivity(new Intent(getApplicationContext(),Home.class));
            finish();
        }




*/


    }


}
