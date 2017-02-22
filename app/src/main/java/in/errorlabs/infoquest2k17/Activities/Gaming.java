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

import in.errorlabs.infoquest2k17.Adapters.Gaming_Adapter;
import in.errorlabs.infoquest2k17.Models.Gaming_Model;
import in.errorlabs.infoquest2k17.R;
import in.errorlabs.infoquest2k17.Utils.Connection;
import in.errorlabs.infoquest2k17.Utils.Onitemtouchlistener;

public class Gaming extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Context context;
    Gaming_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);
         Bundle bundle = getIntent().getExtras();
        String name =bundle.getString("name");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(name);
        recyclerView= (RecyclerView) findViewById(R.id.game_recyclerview);
        Connection connection = new Connection(this);
        Boolean checkinternet =(connection.isInternet());
        if (checkinternet) {
            Log.d("TAG","online startted");
           // getData();
        } else {
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),Home.class));
        }
        ArrayList<Gaming_Model> arrayList = new ArrayList<>();
        /*arrayList.add(new Gaming_Model(R.drawable.blur));
        arrayList.add(new Gaming_Model(R.drawable.cs));
        arrayList.add(new Gaming_Model(R.drawable.coc));
        arrayList.add(new Gaming_Model(R.drawable.cr));
        arrayList.add(new Gaming_Model(R.drawable.pgo));*/
        arrayList.add(new Gaming_Model("https://jbiet-my.sharepoint.com/personal/acm_jbiet_edu_in/_layouts/15/guestaccess.aspx?guestaccesstoken=MFj3JJ5WMuIEs8yU%2fMnk3iB7ZMD7cS%2b6I1wKFFd1UR8%3d&docid=0cc1863c4be264e53a8d01643ed069477&rev=1"));
       // arrayList.add(new Gaming_Model("https://jbiet-my.sharepoint.com/personal/acm_jbiet_edu_in/_layouts/15/guestaccess.aspx?guestaccesstoken=D069Obe4%2fBbdi0utnn8JTtXaSw6ZzXI4Lfv4nt6rkdk%3d&docid=0ecdeca48b1be40ffa8ff907dfbff3de7&rev=1"));
        arrayList.add(new Gaming_Model("https://jbiet-my.sharepoint.com/personal/acm_jbiet_edu_in/_layouts/15/guestaccess.aspx?guestaccesstoken=hNVQmvMBcfEYAr4GaiEzNCdcahjSdt60HB2ROT3xyFU%3d&docid=08f7974dce174416fab747f3ab83d9f89&rev=1"));
      //  arrayList.add(new Gaming_Model("https://jbiet-my.sharepoint.com/personal/acm_jbiet_edu_in/_layouts/15/guestaccess.aspx?guestaccesstoken=g%2b3NR8yo0%2bcoRnDrzxpCGGsJr1yWaDkbCNU0LjWBJZg%3d&docid=05e8767d28ca04ac5a8ee5120d68c0978&rev=1"));
      //  arrayList.add(new Gaming_Model("https://jbiet-my.sharepoint.com/personal/acm_jbiet_edu_in/_layouts/15/guestaccess.aspx?guestaccesstoken=FMghfXs4WVzSb1ADTsMnRnsqkrmDUYZ54bwc2lI1sYQ%3d&docid=03be79b617fc84fbbb971a155a7dbdf64&rev=1"));
        adapter=new Gaming_Adapter(arrayList,this);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new Onitemtouchlistener(context, new Onitemtouchlistener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                switch (position){

                    case 0:   Intent i1 = new Intent(getApplicationContext(),EventRegistration.class);
                        i1.putExtra("name","Blur");
                        startActivity(i1);
                        break;
                    case 1:   Intent i2 = new Intent(getApplicationContext(),EventRegistration.class);
                        i2.putExtra("name","Clash of Clans");
                        startActivity(i2);
                        break;

                }

            }
        }));



    }
}
