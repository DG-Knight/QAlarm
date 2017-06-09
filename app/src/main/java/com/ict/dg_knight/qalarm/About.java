package com.ict.dg_knight.qalarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        String [] strTitle ={getString(R.string.title_version)
                ,getString(R.string.title_dev)
                ,getString(R.string.title_contact)};
        String [] strSub ={"0.0.01"
                ,getString(R.string.apiwatdindang)
                ,"Email : darkeye_whan@hotmail.com"};

        ListSetAbout listSetAbout = new ListSetAbout(getApplicationContext(),strTitle,strSub);
        ListView listAbout = (ListView)findViewById(R.id.list_about);
        listAbout.setAdapter(listSetAbout);
        listAbout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==2){
                    Intent i = new Intent(getApplicationContext(),SendToEmail.class);
                    startActivity(i);
                }
            }
        });




        //แบบใช้ ArrayList
//        ArrayList dirAbout = new ArrayList();
//        for (int i=0;i<3;i++){
//            if (i==0){
//                dirAbout.add("Version\n"+"0.01");
//            }else if (i==1){
//                dirAbout.add("Developper\n"+"Apiwat Dindang");
//            }else {
//                dirAbout.add("Email\n"+"darkeye.whan@gmail.com");
//            }
//        }
//
//        ArrayAdapter<String> adapterAbout = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dirAbout);
//        listView2.setAdapter(adapterAbout);
    }
}
