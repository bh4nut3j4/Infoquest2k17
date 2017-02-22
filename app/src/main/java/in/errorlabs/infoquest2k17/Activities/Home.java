package in.errorlabs.infoquest2k17.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.ArrayList;

import in.errorlabs.infoquest2k17.Adapters.Home_Adapter;
import in.errorlabs.infoquest2k17.Configs.SharedPrefs;
import in.errorlabs.infoquest2k17.Models.Home_Model;
import in.errorlabs.infoquest2k17.R;
import in.errorlabs.infoquest2k17.Utils.Onitemtouchlistener;

import static com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype.Fall;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OneSignal.NotificationOpenedHandler {

    TextView errorlabs,iq_site;
    Home_Adapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Context context;
    SharedPrefs sharedPrefs;
    NiftyDialogBuilder dialogBuilder;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.InAppAlert)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
               .init();
        errorlabs= (TextView) findViewById(R.id.errorlabs);
        iq_site= (TextView) findViewById(R.id.iq_site_name);
         dialogBuilder= NiftyDialogBuilder.getInstance(this);

        errorlabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent i1= new Intent(getApplicationContext(),ELWebView.class);
                i1.putExtra("name","errorlabs");
                startActivity(i1);
            }
        });
        sharedPrefs = new SharedPrefs(this);
        if (sharedPrefs.getopened()==null){
            sharedPrefs.setopened();
        }


        /*iq_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1= new Intent(getApplicationContext(),ELWebView.class);
                i1.putExtra("name","iqsite  ");
                startActivity(i1);
            }
        });*/
        recyclerView= (RecyclerView) findViewById(R.id.home_recyclerview);
        ArrayList<Home_Model> arrayList = new ArrayList<>();
        arrayList.add(new Home_Model(R.drawable.featuredevents));
        arrayList.add(new Home_Model(R.drawable.otherevents));
        arrayList.add(new Home_Model(R.drawable.sponsors));
        arrayList.add(new Home_Model(R.drawable.teaminfoquest));
        /*arrayList.add(new Home_Model("https://jbiet-my.sharepoint.com/personal/acm_jbiet_edu_in/_layouts/15/guestaccess.aspx?guestaccesstoken=vki67mtPb6h4EbrJ%2b9BtM5mWe5mkUvBTBnxxo9lW2ak%3d&docid=05bd04c61f773482b9007d29bb5e0647e&rev=1"));
        arrayList.add(new Home_Model("https://jbiet-my.sharepoint.com/personal/acm_jbiet_edu_in/_layouts/15/guestaccess.aspx?guestaccesstoken=g%2frfDnhVTceJXTwDYqeNPXcEMzL5Abf1s%2fdGGqIYIz8%3d&docid=0cd682a6b1b1a479aba374b9af7c1b754&rev=1"));
        arrayList.add(new Home_Model("https://jbiet-my.sharepoint.com/personal/acm_jbiet_edu_in/_layouts/15/guestaccess.aspx?guestaccesstoken=UeXt3YJ1yCvC6pGow0%2fgoLu63gx2LdY3Sy7mqKDNqBA%3d&docid=02015d39fd0634ee4abebf8710c5b621d&rev=1"));
        arrayList.add(new Home_Model("https://jbiet-my.sharepoint.com/personal/acm_jbiet_edu_in/_layouts/15/guestaccess.aspx?guestaccesstoken=ttb6YHhHaUaE8IkeO%2fpyLfCQ6F7l6QN1%2fuUyaLnEWn8%3d&docid=0365c7d66afdc4dcc8dc6f07af45ebc2b&rev=1"));
      */  adapter=new Home_Adapter(arrayList,this);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);




        recyclerView.addOnItemTouchListener(new Onitemtouchlistener(context, new Onitemtouchlistener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                switch (position){

                    case 0:
                        startActivity(new Intent(getApplicationContext(),FeaturedEvents.class));
                        break;
                    case 1:
                       startActivity(new Intent(getApplicationContext(),OtherEvents.class));
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(),Sponsors.class));
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(),Team_IQ.class));
                        break;

                }

            }
        }));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegisteredEvents.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (sharedPrefs.getLogedInUserName()==null || sharedPrefs.getLogedInKey()==null || sharedPrefs.getEmail()==null){
            Menu menu= navigationView.getMenu();
            menu.findItem(R.id.nav_track).setVisible(false);
        }else {
            Menu menu= navigationView.getMenu();
            menu.findItem(R.id.nav_track).setVisible(true);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            if (sharedPrefs.getLogedInUserName()==null){
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }else {
                finish();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(sharedPrefs.getLogedInKey()==null||sharedPrefs.getEmail()==null||sharedPrefs.getLogedInUserName()==null){
            getMenuInflater().inflate(R.menu.home_nologin, menu);
        }else{
            getMenuInflater().inflate(R.menu.home_login, menu);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.login) {
            sharedPrefs.clearprefs();
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }
        if (id == R.id.logout) {
            dialogBuilder
                    .withTitle("Infoquest2k17")
                    .withTitleColor("#FFFFFF")
                    .withDividerColor("#11000000")
                    .withMessage("Are you sure, to logout?")
                    .withMessageColor("#FFFFFFFF")
                    .withDialogColor("#455a64")
                    .withIcon(getResources().getDrawable(R.drawable.applogo))
                    .withDuration(500)
                    .withEffect(Fall)
                    .withButton1Text("Cancel")
                    .withButton2Text("Logout")
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
                            sharedPrefs.clearprefs();
                            startActivity(new Intent(getApplicationContext(),Login.class));
                            finish();
                        }
                    })
                    .show();

        }
        if (id==R.id.share){
            final String myappPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try
            { Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Infoquest2k17");
                String sAux = "\nLet me recommend you this application\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id="+myappPackageName+"\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i,"Share Via"));
            }
            catch(Exception e)
            {
                Toast.makeText(getApplicationContext(),"Not Found",Toast.LENGTH_SHORT).show();
            }
        }



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.nav_profile) {
            startActivity(new Intent(getApplicationContext(),Profile.class));
        } else if (id == R.id.nav_registeredevents) {
            startActivity(new Intent(getApplicationContext(),RegisteredEvents.class));

        } else if (id == R.id.nav_map) {
            startActivity(new Intent(getApplicationContext(),MapsActivity.class));
        }else if (id==R.id.nav_track){
            startActivity(new Intent(getApplicationContext(),TrackMypaper.class));
        }
        else if (id == R.id.nav_notifications) {
            startActivity(new Intent(getApplicationContext(),Notifications.class));
        } else if (id == R.id.nav_aboutus) {

            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setTitle(String.format("%1$s", getString(R.string.app_name)));
            builder.setMessage(getResources().getText(R.string.about_infoquest));
            builder.setPositiveButton("OK", null);
            builder.setIcon(R.drawable.applogo);
            AlertDialog welcomeAlert = builder.create();
            welcomeAlert.show();
            ((TextView) welcomeAlert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());


        } else if (id == R.id.nav_contributers) {


            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setTitle(String.format("%1$s", getString(R.string.app_name)));
            builder.setMessage(getResources().getText(R.string.contributers));
            builder.setPositiveButton("OK", null);
            builder.setIcon(R.drawable.applogo);
            AlertDialog welcomeAlert = builder.create();
            welcomeAlert.show();
            ((TextView) welcomeAlert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());


        } else if (id == R.id.nav_reportabug) {
            Intent i = new Intent(Intent.ACTION_SENDTO);
            String uriText = "mailto:" + Uri.encode("bhanuteja@errorlabs.in") + "?subject=" +
                    Uri.encode("Reporting A Bug/Feedback") + "&body=" + Uri.encode("Hello, Team Infoquest \nI want to report a bug/give feedback corresponding to the app Infoquest2017...\n\n-Your name");
            Uri uri = Uri.parse(uriText);
            i.setData(uri);
            startActivity(Intent.createChooser(i, "Send Email"));

        } else if (id == R.id.nav_licenses) {

            dialogBuilder
                    .withTitle("Open Source Licenses")
                    .withTitleColor("#FFFFFF")
                    .withDividerColor("#11000000")
                    .withMessage(R.string.licenses_text)
                    .withMessageColor("#FFFFFFFF")
                    .withDialogColor("#455a64")
                    .withIcon(getResources().getDrawable(R.drawable.applogo))
                    .withDuration(500)
                    .withEffect(Fall)
                    .withButton1Text("OK")
                    .isCancelableOnTouchOutside(true)
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.cancel();
                        }
                    })
                    .show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String customKey;
Toast.makeText(getApplicationContext(),data.toString(),Toast.LENGTH_SHORT).show();
        if (data != null) {
            customKey = data.optString("customkey", null);
            if (customKey != null)
                Log.i("OneSignalExample", "customkey set with value: " + customKey);
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);


        // The following can be used to open an Activity of your choice.
        // Replace - getApplicationContext() - with any Android Context.
         Intent intent = new Intent(getApplicationContext(), Team_IQ.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivity(intent);

        // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
        //   if you are calling startActivity above.
     /*
        <application ...>
          <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
        </application>
     /*

      */
    }



    class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            /*OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String customKey;
            String id =result.notification.payload.notificationID;
            String title=result.notification.payload.title;
            String small_icon=result.notification.payload.smallIcon;
            String large_icon=result.notification.payload.largeIcon;
            String launch_url=result.notification.payload.launchURL;
            String description = result.notification.payload.body  ;
            String image = result.notification.payload.bigPicture;
            Toast.makeText(getApplicationContext(),id+title+small_icon+large_icon+launch_url+description+image,Toast.LENGTH_LONG).show();
            if (data != null) {
                customKey = data.optString("customkey", null);
                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);
            }

            if (actionType == OSNotificationAction.ActionType.ActionTaken)
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);*/

            // The following can be used to open an Activity of your choice.
            // Replace - getApplicationContext() - with any Android Context.
            Intent intent = new Intent(getApplicationContext(),Notifications.class);
           // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
            //   if you are calling startActivity above.
            /*
        <application ...>
          <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
        </application>
     */
        }
    }


}
