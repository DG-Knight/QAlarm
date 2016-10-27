package com.ict.dg_knight.qalarm;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.ict.dg_knight.qalarm.DbHelper.TABLE_LAST;
import static com.ict.dg_knight.qalarm.DbHelper.TABLE_TOTAL;
import static com.ict.dg_knight.qalarm.DbHelper.TIME_HOUR_L;
import static com.ict.dg_knight.qalarm.DbHelper.TIME_LAST_DAY;
import static com.ict.dg_knight.qalarm.DbHelper.TIME_MINUTE_L;
import static com.ict.dg_knight.qalarm.DbHelper.TOTAL_TIME;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowEventShakeFragment extends Fragment {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private TextView shakeit;

    final static int RQS_1 = 1;
    DbHelper mHelper;
    SQLiteDatabase mDb;
    Cursor mCursor;
    public static final Calendar cal = Calendar.getInstance();
    int closeHour = cal.get(Calendar.HOUR_OF_DAY);//รับค่าช่วยโมงปัจจุบัน
    int closeMinute = cal.get(Calendar.MINUTE);//รับค่านาทีปัจจุบัน
    int dHour;
    int dMinute;

    public ShowEventShakeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_show_event_shake, container, false);

        shakeit = (TextView)rootView.findViewById(R.id.shakeIt);
        // ShakeDetector initialization
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                SharedPreferences sharedPref;
                sharedPref =getActivity().getSharedPreferences("numShake",Context.MODE_PRIVATE);
                int num1 = 20,num2 = 25,num3 = 30,num4 = 35,num5= 40;
                final int a;
                a = sharedPref.getInt("numShake",-1);//get วิทีเลือกนาฬิกาจาก sharedPref
                Log.i("DataOnSharedNumShake :",String.valueOf(a));
              if (a == 0 && a == -1){
                if (count==num1){
                    Toast.makeText(getActivity(), "เสร็จสิ้นภารกิจ", Toast.LENGTH_SHORT).show();
                    cancelAlarm();
                    saveTime();
                    getActivity().finish();//ปิด fragment พร้อม actitvity
                }
                else {

                    RoundCornerProgressBar progress1 = (RoundCornerProgressBar)rootView.findViewById(R.id.progess_1);
                    progress1.setProgressColor(Color.parseColor("#38ffd0"));
                    progress1.setProgressBackgroundColor(Color.parseColor("#f2f2f2"));
                    progress1.setMax(num1);
                    progress1.setProgress(count);
                }
              }else if (a == 1){
                  if (count==num2){
                      Toast.makeText(getActivity(), "เสร็จสิ้นภารกิจ", Toast.LENGTH_SHORT).show();
                      cancelAlarm();
                      saveTime();
                      getActivity().finish();//ปิด fragment พร้อม actitvity
                  }
                  else {

                      RoundCornerProgressBar progress1 = (RoundCornerProgressBar)rootView.findViewById(R.id.progess_1);
                      progress1.setProgressColor(Color.parseColor("#38ffd0"));
                      progress1.setProgressBackgroundColor(Color.parseColor("#f2f2f2"));
                      progress1.setMax(num2);
                      progress1.setProgress(count);
                  }

              }else if (a == 2){
                  if (count==num3){
                      Toast.makeText(getActivity(), "เสร็จสิ้นภารกิจ", Toast.LENGTH_SHORT).show();
                      cancelAlarm();
                      saveTime();
                      getActivity().finish();//ปิด fragment พร้อม actitvity
                  }
                  else {

                      RoundCornerProgressBar progress1 = (RoundCornerProgressBar)rootView.findViewById(R.id.progess_1);
                      progress1.setProgressColor(Color.parseColor("#38ffd0"));
                      progress1.setProgressBackgroundColor(Color.parseColor("#f2f2f2"));
                      progress1.setMax(num3);
                      progress1.setProgress(count);
                  }

              }else if (a == 3){
                  if (count==num4){
                      Toast.makeText(getActivity(), "เสร็จสิ้นภารกิจ", Toast.LENGTH_SHORT).show();
                      cancelAlarm();
                      saveTime();
                      getActivity().finish();//ปิด fragment พร้อม actitvity
                  }
                  else {

                      RoundCornerProgressBar progress1 = (RoundCornerProgressBar)rootView.findViewById(R.id.progess_1);
                      progress1.setProgressColor(Color.parseColor("#38ffd0"));
                      progress1.setProgressBackgroundColor(Color.parseColor("#f2f2f2"));
                      progress1.setMax(num4);
                      progress1.setProgress(count);
                  }

              }else{
                  if (count==num5){
                      Toast.makeText(getActivity(), "เสร็จสิ้นภารกิจ", Toast.LENGTH_SHORT).show();
                      cancelAlarm();
                      saveTime();
                      getDateTime();
                      getActivity().finish();//ปิด fragment พร้อม actitvity
                  }
                  else {
                      RoundCornerProgressBar progress1 = (RoundCornerProgressBar)rootView.findViewById(R.id.progess_1);
                      progress1.setProgressColor(Color.parseColor("#38ffd0"));
                      progress1.setProgressBackgroundColor(Color.parseColor("#f2f2f2"));
                      progress1.setMax(num5);
                      progress1.setProgress(count);
                  }
              }
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
    public void saveTime(){
        openDb();//เปิดฐานข้อมูล

        DateFormat mDate = DateFormat.getDateInstance(DateFormat.LONG);//ทำไมต้อง ใช้ DateFormat.LONG ?
        Date date = cal.getTime();
        String txtDate = mDate.format(date);
        Log.i("Present Time: ", closeHour+":"+closeMinute);
        Log.i("Present Date: ",txtDate);

        ContentValues values = new ContentValues();
        values.put(TIME_LAST_DAY,txtDate);
        values.put(TIME_HOUR_L,closeHour);
        values.put(TIME_MINUTE_L,closeMinute);
        mDb.insert(TABLE_LAST,null,values);
        Log.i("Insert Data: ",String.valueOf(mDb));
        closeDb();// เรียกใช้ฟังก์ชันปิดฐานข้อมูล
    }
    private void getDateTime(){

        openDb();//เปิดฐานข้อมูล
        mCursor = mDb.rawQuery("SELECT * FROM "
                + DbHelper.TABLE_TODAY, null);//เลือกคิวรี่ข้อมูลจากตาราง TABLE_TODAY
        if (mCursor.isBeforeFirst()){
            mCursor.moveToLast();
            dHour = mCursor.getInt(mCursor.getColumnIndex(DbHelper.TIME_HOUR));//ดึงข้อมูลในแถวสุดท้าย คอลัมที่มีชื่อว่า TIME_HOUR
            dMinute = mCursor.getInt(mCursor.getColumnIndex(DbHelper.TIME_MINUTE));//ดึงข้อมูลในแถวสุดท้าย คอลัมที่มีชื่อว่า TIME_MINUTE
            calTime();
        }else {
            mCursor.moveToLast();
            dHour = mCursor.getInt(mCursor.getColumnIndex(DbHelper.TIME_HOUR));//ดึงข้อมูลในแถวสุดท้าย คอลัมที่มีชื่อว่า TIME_HOUR
            dMinute = mCursor.getInt(mCursor.getColumnIndex(DbHelper.TIME_MINUTE));//ดึงข้อมูลในแถวสุดท้าย คอลัมที่มีชื่อว่า TIME_MINUTE
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

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mShakeDetector);
    }
}
