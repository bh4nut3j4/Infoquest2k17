package in.errorlabs.infoquest2k17.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import in.errorlabs.infoquest2k17.Adapters.Featured_Adapter;
import in.errorlabs.infoquest2k17.Configs.SharedPrefs;
import in.errorlabs.infoquest2k17.Models.Featured_Model;
import in.errorlabs.infoquest2k17.R;
import in.errorlabs.infoquest2k17.Utils.Connection;
import in.errorlabs.infoquest2k17.Utils.Onitemtouchlistener;

public class FeaturedEvents extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Context context;
    Featured_Adapter adapter;
    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.featured_events);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Featured Events");
        recyclerView= (RecyclerView) findViewById(R.id.featured_recyclerview);
        Connection connection = new Connection(this);
        sharedPrefs=new SharedPrefs(this);
        Boolean checkinternet =(connection.isInternet());
        if (checkinternet) {
            Log.d("TAG","online startted");
            // getData();
        } else {
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),Home.class));
            finish();
        }
        ArrayList<Featured_Model> arrayList = new ArrayList<>();
        /*arrayList.add(new Featured_Model(R.drawable.hackathon));
        arrayList.add(new Featured_Model(R.drawable.paperpresentation));
        arrayList.add(new Featured_Model(R.drawable.codejam));
        arrayList.add(new Featured_Model(R.drawable.gaming));*/
      // arrayList.add(new Featured_Model("http://jbgroup.org.in/sync/sync_mapp/iq_php/qrcode/qr.php?data=sdbhahbvsbvjbasjbvsdb&size=200x200"));
        arrayList.add(new Featured_Model("https://jbiet-my.sharepoint.com/personal/acm_jbiet_edu_in/_layouts/15/guestaccess.aspx?guestaccesstoken=99RCikpPpE9Z7TunRy2xIlpLrdKPbYM7HtjU3akMbC4%3d&docid=0107a9234af674bce8ada7d085f75fc85&rev=1"));
        arrayList.add(new Featured_Model("https://jbiet-my.sharepoint.com/personal/acm_jbiet_edu_in/_layouts/15/guestaccess.aspx?guestaccesstoken=WyjX0pJiblz5eShZm9G3%2buhxwdECQs5z%2fZ7a6Xgsybo%3d&docid=094daac32927e49bbacb5a42c527d4265&rev=1"));
        arrayList.add(new Featured_Model("https://jbiet-my.sharepoint.com/personal/acm_jbiet_edu_in/_layouts/15/guestaccess.aspx?guestaccesstoken=XhWWmuQ8rvDRqW%2fKyq0Cky9AofzUVFTlVhrxsFQipnU%3d&docid=07f70d7bff6d14564b06c071e9c765741&rev=1"));
        //arrayList.add(new Featured_Model("https://jbiet-my.sharepoint.com/personal/acm_jbiet_edu_in/_layouts/15/guestaccess.aspx?guestaccesstoken=VRuX0KrddD0xBUtxlurxFBVQe0KTOLqLuJdM7%2f%2fxSd4%3d&docid=017f0243ef4dd4d9c9d9de03bcbb32329&rev=1"));
        arrayList.add(new Featured_Model("https://jbiet-my.sharepoint.com/personal/acm_jbiet_edu_in/_layouts/15/guestaccess.aspx?guestaccesstoken=KVCF6M1OfI09mWNl58YGQqRiZxOyYuOL1yVulQrB5AM%3d&docid=07217da05dd594c0caddc4803c2483092&rev=1"));
        adapter=new Featured_Adapter(arrayList,this);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new Onitemtouchlistener(context, new Onitemtouchlistener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                switch (position){

                    case 0:   Intent i1 = new Intent(getApplicationContext(),EventRegistration.class);
                        i1.putExtra("name","Hack@IQ");
                        startActivity(i1);
                        break;
                    case 1:   if(sharedPrefs.getLogedInKey()==null||sharedPrefs.getLogedInUserName()==null||sharedPrefs.getEmail()==null){
                                    Intent i2 = new Intent(getApplicationContext(),EventRegistration.class);
                                    i2.putExtra("name","PaperPresentation");
                                    startActivity(i2);
                                }else {
                                    Intent ii = new Intent(getApplicationContext(),Ppt_options.class);
                                    startActivity(ii);
                                }

                        break;
                    case 2:   Intent i3 = new Intent(getApplicationContext(),EventRegistration.class);
                        i3.putExtra("name","Code JAM");
                        startActivity(i3);
                        break;
                    /*case 3:   Intent i4 = new Intent(getApplicationContext(),EventRegistration.class);
                        i4.putExtra("name","Gears of Rampage");
                        startActivity(i4);
                        break;*/
                    case 3:   Intent i5 = new Intent(getApplicationContext(),Gaming.class);
                        i5.putExtra("name","Gaming");
                        startActivity(i5);
                        break;
                }

            }
        }));

    }
}
