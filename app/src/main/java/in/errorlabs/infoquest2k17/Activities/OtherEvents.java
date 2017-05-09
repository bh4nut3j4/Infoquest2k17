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

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;

import in.errorlabs.infoquest2k17.Adapters.Others_Adapter;
import in.errorlabs.infoquest2k17.Models.Others_Model;
import in.errorlabs.infoquest2k17.R;
import in.errorlabs.infoquest2k17.Utils.Connection;
import in.errorlabs.infoquest2k17.Utils.Onitemtouchlistener;

public class OtherEvents extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Context context;
    Others_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_events);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Other Events");
        recyclerView= (RecyclerView) findViewById(R.id.others_recyclerview);
        LoadToast loadToast = new LoadToast(this);
        loadToast.setText("Loading...");
        loadToast.show();
        Connection connection = new Connection(this);
        Boolean checkinternet =(connection.isInternet());
        if (checkinternet) {
            Log.d("TAG","online startted");
            // getData();
        } else {
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),Home.class));
        }
        ArrayList<Others_Model> arrayList = new ArrayList<>();
//        arrayList.add(new Others_Model(R.drawable.techmaze));
//        arrayList.add(new Others_Model(R.drawable.quizzard));
//        arrayList.add(new Others_Model(R.drawable.backtrack));
        arrayList.add(new Others_Model("https://jbiet-my.sharepoint.com/personal/acm_jbiet_edu_in/_layouts/15/guestaccess.aspx?guestaccesstoken=5ya3p8MM9ZrtK03iwembC%2fhMX1Bx70dwHHBYYdRynPM%3d&docid=0d3f5f5ed4ff84a5d94181fa9ff56cd44&rev=1"));
        arrayList.add(new Others_Model("https://jbiet-my.sharepoint.com/personal/infoquest_jbiet_edu_in/_layouts/15/guestaccess.aspx?docid=0b43779bcb8e5486b9d2610ddbd982e56&authkey=AYGnyb_JT-lUjsO4Im4OU_Q"));
        arrayList.add(new Others_Model("https://jbiet-my.sharepoint.com/personal/acm_jbiet_edu_in/_layouts/15/guestaccess.aspx?guestaccesstoken=XiTcIflbBvvFaK10vtGSoCteRdDHsAgO2gwh7yhmWFw%3d&docid=0337303b652194353b61a6d552cf127ea&rev=1"));
        arrayList.add(new Others_Model("https://jbiet-my.sharepoint.com/personal/acm_jbiet_edu_in/_layouts/15/guestaccess.aspx?guestaccesstoken=0tcsTwkFOQ1EPiPeQwkRIdEbl9%2bunV6BOnHa81DgZYY%3d&docid=0d31a1efcc9cf476c9cd3922f7b3a04fa&rev=1"));
        arrayList.add(new Others_Model("https://jbiet-my.sharepoint.com/personal/acm_jbiet_edu_in/_layouts/15/guestaccess.aspx?guestaccesstoken=00ZKG7Sg1Uk6f5%2bJ8v%2f%2b3a5ikFMmprb7Jmqp7HZpRRg%3d&docid=0dbe989f8917544878dfd7af6d0f2b1f3&rev=1"));
        /* arrayList.add(new Others_Model(R.drawable.cr));
        arrayList.add(new Others_Model(R.drawable.pgo));*/
        adapter=new Others_Adapter(arrayList,this);
        loadToast.success();
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new Onitemtouchlistener(context, new Onitemtouchlistener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                switch (position){

                    case 0:   Intent i1 = new Intent(getApplicationContext(),EventRegistration.class);
                        i1.putExtra("name","Tech Maze");
                        startActivity(i1);
                        break;
                    case 1:   Intent i5 = new Intent(getApplicationContext(),EventRegistration.class);
                        i5.putExtra("name","Can you C");
                        startActivity(i5);
                        break;
                    case 2:   Intent i2 = new Intent(getApplicationContext(),EventRegistration.class);
                        i2.putExtra("name","Quizzard");
                        startActivity(i2);
                        break;
                    case 3:   Intent i3 = new Intent(getApplicationContext(),EventRegistration.class);
                        i3.putExtra("name","BackTrack");
                        startActivity(i3);
                        break;
                    case 4:   Intent i4 = new Intent(getApplicationContext(),EventRegistration.class);
                        i4.putExtra("name","Googled");
                        startActivity(i4);
                        break;
                }

            }
        }));
    }
}
