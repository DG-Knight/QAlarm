package com.ict.dg_knight.qalarm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

//    public final Calendar cal = Calendar.getInstance();
    private TextView textShow;
    private Button btn_showSetTime;
    SharedPreferences sharedPref;
    SQLiteDatabase mDb;
    DbHelper mHelper;
    Cursor mCursor;
    String dayOfWeek;
    String dayOfMount;

    public boolean isFirstStart;
    Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_showSetTime = (Button)findViewById(R.id.btn_setTime);
        textShow=(TextView) findViewById(R.id.txtShow);

        mHelper = new DbHelper(this);
        mDb = mHelper.getWritableDatabase();
        mCursor = mDb.rawQuery("SELECT * FROM " + DbHelper.TABLE_TODAY, null);

            if (mCursor.isBeforeFirst()){
            Log.i("CursorIsBeforeFirst : ", String.valueOf(mCursor.isBeforeFirst()));
                mCursor.moveToLast();
                if (!mCursor.isBeforeFirst()){
                    dayOfWeek = mCursor.getString(mCursor.getColumnIndex(DbHelper.WEEK_DAY));
                    dayOfMount = mCursor.getString(mCursor.getColumnIndex(DbHelper.TIME_DAY));
                    Log.i("Column",String.valueOf(mCursor.getColumnCount()));
                }else {
                    Log.d("mCursor","NUll");
                }
        }
        textShow.setVisibility(View.VISIBLE);
        if (dayOfMount!=null){
            if (dayOfWeek!=null){
                textShow.setText("วัน\t\t"+dayOfWeek+"\t\t"+dayOfMount);
            }else{
                Log.e("Message1:","ไม่มีข้อมูลวะนที่");
            }
        }else {
            Log.e("Message2:","ไม่มีข้อมูลวะนที่");
        }
        sharedPref = getSharedPreferences("timeAlarm", Context.MODE_PRIVATE);
        int hour = sharedPref.getInt("Hour",0);
        int minute = sharedPref.getInt("Minute",0);
        if (hour==0&&minute==0){
            btn_showSetTime.setVisibility(View.VISIBLE);
        }else {
            btn_showSetTime.setText(hour+":"+minute+"\tนาที");
        }

        btn_showSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SetAlarm.class);
                startActivity(i);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SetAlarm.class);
                startActivity(intent);

            }
        });

        //-----------------------------slider intro---------------------
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Intro App Initialize SharedPreferences
                SharedPreferences getSharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());
                //  Create a new boolean and preference and set it to true
                isFirstStart = getSharedPreferences.getBoolean("firstStart", true);
                //  Check either activity or app is open very first time or not and do action
                if (isFirstStart) {

                    //  Launch application introduction screen
                    Intent i = new Intent(MainActivity.this, MyIntro.class);
                    startActivity(i);
                    SharedPreferences.Editor e = getSharedPreferences.edit();
                    e.putBoolean("firstStart", false);
                    e.apply();
                }
            }
        });
        t.start();

    }
//ถามก่อนออกจากแอป
    public void onBackPressed(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Exit");
        dialog.setIcon(R.drawable.ic_launcher);
        dialog.setCancelable(true);
        dialog.setMessage("Do you want to exit ?");
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent i = new Intent(getApplicationContext(),About.class);
            startActivity(i);
        }else if (id == R.id.action_history){
            Intent i = new Intent(getApplicationContext(),History.class);
            startActivity(i);
        }else{
        }

        return super.onOptionsItemSelected(item);
    }
    public void onPause() {
        super.onPause();
        if (mDb!=null){
            mHelper.close();
            mDb.close();
        }
    }
}
