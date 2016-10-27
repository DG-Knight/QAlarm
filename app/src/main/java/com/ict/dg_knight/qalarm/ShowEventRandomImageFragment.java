package com.ict.dg_knight.qalarm;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

import static com.ict.dg_knight.qalarm.DbHelper.TABLE_TOTAL;
import static com.ict.dg_knight.qalarm.DbHelper.TOTAL_TIME;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowEventRandomImageFragment extends Fragment {
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private ImageView img6;
    private ImageView img7;
    private ImageView img8;
    private ImageView img9;
    private TextView txtQusetion;

    private String ANIMAIL = "animal";
    private String CAR = "car";
    private String HOME = "home";

    private Button btnRanOk;
    private Button btnRefresh;

    public static final Calendar cal = Calendar.getInstance();
    int closeHour = cal.get(Calendar.HOUR_OF_DAY);//รับค่าช่วยโมงปัจจุบัน
    int closeMinute = cal.get(Calendar.MINUTE);//รับค่านาทีปัจจุบัน
    public static int dHour;
    public static int dMinute;

    private DbHelper mHelper;
    private SQLiteDatabase mDb;
    private Cursor mCursor;

    final static int RQS_1 = 1;
    int n ;//เก็บตำแหน่งของรูปภาพที่สุ่มได้
    int q ;// เก็บตำแหน่งของคำถาม

    public ShowEventRandomImageFragment() {
        // Required empty public constructor
    }

    int [] img_ran1 = {R.drawable.animal1,R.drawable.animal2,R.drawable.animal3
            ,R.drawable.car1,R.drawable.car2,R.drawable.car3
            ,R.drawable.home1,R.drawable.home2,R.drawable.home3};

    int [] img_ran2 = {R.drawable.animal4,R.drawable.animal5,R.drawable.animal6
            ,R.drawable.car4,R.drawable.car5,R.drawable.car6
            ,R.drawable.home4,R.drawable.home5,R.drawable.home6};
//
    String[] question = {"ภาพใดบ้างเป็นภาพสัตว์ ?","ภาพใดบ้างเป็นภาพรถ ?","ภาพใดบ้างเป็นภาพบ้าน ?"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_show_event_random_image, container, false);

        img1 = (ImageView)rootView.findViewById(R.id.img1);
        img2 = (ImageView)rootView.findViewById(R.id.img2);
        img3 = (ImageView)rootView.findViewById(R.id.img3);
        img4 = (ImageView)rootView.findViewById(R.id.img4);
        img5 = (ImageView)rootView.findViewById(R.id.img5);
        img6 = (ImageView)rootView.findViewById(R.id.img6);
        img7 = (ImageView)rootView.findViewById(R.id.img7);
        img8 = (ImageView)rootView.findViewById(R.id.img8);
        img9 = (ImageView)rootView.findViewById(R.id.img9);

        img1.setVisibility(View.VISIBLE);
        img2.setVisibility(View.VISIBLE);
        img3.setVisibility(View.VISIBLE);
        img4.setVisibility(View.VISIBLE);
        img5.setVisibility(View.VISIBLE);
        img6.setVisibility(View.VISIBLE);
        img7.setVisibility(View.VISIBLE);
        img8.setVisibility(View.VISIBLE);
        img9.setVisibility(View.VISIBLE);

        txtQusetion = (TextView)rootView.findViewById(R.id.txtQuest);

        btnRanOk = (Button)rootView.findViewById(R.id.btn_ran_ok);
        btnRefresh = (Button)rootView.findViewById(R.id.btn_ran_cancel);

        randomQuestion();
        randomImg();
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                randomQuestion();
                randomImg();
            }
        });

        btnRanOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
                getDatTime();
                getActivity().finish();
            }
        });
        return rootView;
    }
    private void cancelAlarm() {
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
    private void randomQuestion(){
        Random r = new Random();

    }
    private void randomImg(){

        Random r = new Random();
        q = r.nextInt(question.length);
        if (q==0){
            txtQusetion.setText(question[q]);
        }else if (q==1){
            txtQusetion.setText(question[q]);
        }else{
            txtQusetion.setText(question[q]);
        }

        for (int i = 0; i < img_ran1.length; i++){
            if (i==0){
                n= r.nextInt(img_ran1.length);
                Log.e("Random1:",String.valueOf(n));
                img1.setImageResource(img_ran1[n]);
                img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("img1","is click");
                        if (q==1){
                            if (n==1|n==2|n==3){
                                Log.i("Image",String.valueOf(n));
                                Toast.makeText(getActivity(),ANIMAIL, Toast.LENGTH_SHORT).show();
                            }
                        }else if (q==2) {
                            if (n == 4 | n == 5 | n == 6) {
                                Log.i("CAR", String.valueOf(n));
                                Toast.makeText(getActivity(), CAR, Toast.LENGTH_SHORT).show();
                            }
                        }else if (q==3){
                            if (n==7|n==8|n==9){
                                Log.i("HOME", String.valueOf(n));
                                Toast.makeText(getActivity(), HOME, Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

            } else if (i==1){
                n= r.nextInt(img_ran2.length);
                Log.e("Random2:",String.valueOf(n));
                img2.setImageResource(img_ran2[n]);//ภาาชุดที่สอง
            }else if (i==2){
                n= r.nextInt(img_ran1.length);
                Log.e("Random3:",String.valueOf(n));
                img3.setImageResource(img_ran1[n]);
            }else if (i==3){
                n= r.nextInt(img_ran2.length);
                Log.e("Random4:",String.valueOf(n));
                img4.setImageResource(img_ran2[n]);
            }else if (i==4){
                n= r.nextInt(img_ran1.length);
                Log.e("Random5:",String.valueOf(n));
                img5.setImageResource(img_ran1[n]);
            }else if (i==5){
                n= r.nextInt(img_ran2.length);
                Log.e("Random6:",String.valueOf(n));
                img6.setImageResource(img_ran2[n]);
            }else if (i==6){
                n= r.nextInt(img_ran1.length);
                Log.e("Random7:",String.valueOf(n));
                img7.setImageResource(img_ran1[n]);
            }else if (i==7){
                n= r.nextInt(img_ran2.length);
                Log.e("Random8:",String.valueOf(n));
                img8.setImageResource(img_ran2[n]);
            }else if (i==8){
                n= r.nextInt(img_ran1.length);
                Log.e("Random9:",String.valueOf(n));
                img9.setImageResource(img_ran1[n]);
            }
        }
    }
    private void getDatTime(){

        openDb();//เปิดฐานข้อมูล
        mCursor = mDb.rawQuery("SELECT * FROM "
                + DbHelper.TABLE_TODAY, null);//เลือกคิวรี่ข้อมูลจากตาราง TABLE_TODAY
        if (mCursor.isBeforeFirst()){
            mCursor.moveToLast();
            dHour = mCursor.getInt(mCursor.getColumnIndex(DbHelper.TIME_HOUR));//ดึงข้อมูลในแถวสุดท้าย คอลัมที่มีชื่อว่า TIME_HOUR
            dMinute = mCursor.getInt(mCursor.getColumnIndex(DbHelper.TIME_MINUTE));//ดึงข้อมูลในแถวสุดท้าย คอลัมที่มีชื่อว่า TIME_MINUTE
//            Log.i("GetHour", String.valueOf(dHour));
//            Log.i("GetMinute",String.valueOf(dMinute));
            calTime();
        }else {
            mCursor.moveToLast();
            dHour = mCursor.getInt(mCursor.getColumnIndex(DbHelper.TIME_HOUR));//ดึงข้อมูลในแถวสุดท้าย คอลัมที่มีชื่อว่า TIME_HOUR
            dMinute = mCursor.getInt(mCursor.getColumnIndex(DbHelper.TIME_MINUTE));//ดึงข้อมูลในแถวสุดท้าย คอลัมที่มีชื่อว่า TIME_MINUTE
//            Log.i("GetHour", String.valueOf(dHour));
//            Log.i("GetMinute",String.valueOf(dMinute));
            calTime();
        }
        closeDb();
    }
    private void calTime(){
        openDb();
        int sumHour;
        int sumMinute;
        String strI;

        if (dHour<=closeHour&&dMinute<=closeMinute){

            Log.d("ลบเวลาเข้า Case","1");
            Log.d("เวลาปิดนาฬิกา",String.valueOf(closeHour)+""+String.valueOf(closeMinute));
            sumHour = closeHour - dHour;
            sumMinute = closeMinute - dMinute;
            strI = String.valueOf(sumHour)+"."+String.valueOf(sumMinute);
            Log.i("sumHour",strI);
            insertCalTime(strI);

        }else if (dHour<=closeHour&&dMinute>closeMinute){
            Log.i("Case","2");
            sumHour = closeHour - dHour ;
            sumMinute = (closeMinute+60)- dMinute ;
            strI = String.valueOf(sumHour)+"."+String.valueOf(sumMinute);
            Log.i("sumHour",strI);
            insertCalTime(strI);
        }else if (dHour>=closeHour&&dMinute>closeMinute){
            Log.i("Case","3");
            sumHour = (closeHour-1)+24-dHour;
            sumMinute = (closeHour+60)-dMinute;
            strI = String.valueOf(sumHour)+"."+String.valueOf(sumMinute);
            Log.i("sumHour",strI);
            insertCalTime(strI);
        }
    }
    private void insertCalTime(String cal){
        Log.d("insertCalTime: ", String.valueOf(cal));
        ContentValues v = new ContentValues();
        if (cal!=null){
            v.put(TOTAL_TIME,cal);
            mDb.insert(TABLE_TOTAL,null,v);
        }
    }
    private void openDb(){
        mHelper = new DbHelper(getActivity());
        mDb = mHelper.getWritableDatabase();
    }
    private void closeDb(){
        if (mDb!=null){
            mHelper.close();
            mDb.close();
        }
    }
}
