package com.ict.dg_knight.qalarm;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import static com.ict.dg_knight.qalarm.DbHelper.TIME_HOUR_L;
import static com.ict.dg_knight.qalarm.DbHelper.TIME_LAST_DAY;
import static com.ict.dg_knight.qalarm.DbHelper.TIME_MINUTE_L;

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
				/*
				 * The following method, "handleShakeEvent(count):" is a stub //
				 * method you would use to setup whatever you want done once the
				 * device has been shook.
				 */
                if (count==20){
                    Toast.makeText(getActivity(), "เสร็จสิ้นภารกิจ", Toast.LENGTH_SHORT).show();
                    cancelAlarm();
                    saveTime();
                    getActivity().finish();//ปิด fragment พร้อม actitvity
                }
                else {
                    RoundCornerProgressBar progress1 = (RoundCornerProgressBar)rootView.findViewById(R.id.progess_1);
                    progress1.setProgressColor(Color.parseColor("#38ffd0"));
                    progress1.setProgressBackgroundColor(Color.parseColor("#f2f2f2"));
                    progress1.setMax(20);
                    progress1.setProgress(count);
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
    private void calTime(){
        int dHour;
        int dMinute;
        openDb();//เปิดฐานข้อมูล
        mCursor = mDb.rawQuery("SELECT * FROM "
                                       + DbHelper.TABLE_TODAY, null);//เลือกคิวรี่ข้อมูลจากตาราง TABLE_TODAY
        if (mCursor.isBeforeFirst()){
            mCursor.moveToLast();
            dHour = mCursor.getInt(mCursor.getColumnIndex(DbHelper.TIME_HOUR));//ดึงข้อมูลในแถวสุดท้าย คอลัมที่มีชื่อว่า TIME_HOUR
            dMinute = mCursor.getInt(mCursor.getColumnIndex(DbHelper.TIME_MINUTE));//ดึงข้อมูลในแถวสุดท้าย คอลัมที่มีชื่อว่า TIME_MINUTE
            Log.i("GetHour", String.valueOf(dHour));
            Log.i("GetMinute",String.valueOf(dMinute));

        }else {
            mCursor.moveToLast();
            dHour = mCursor.getInt(mCursor.getColumnIndex(DbHelper.TIME_HOUR));//ดึงข้อมูลในแถวสุดท้าย คอลัมที่มีชื่อว่า TIME_HOUR
            dMinute = mCursor.getInt(mCursor.getColumnIndex(DbHelper.TIME_MINUTE));//ดึงข้อมูลในแถวสุดท้าย คอลัมที่มีชื่อว่า TIME_MINUTE
            Log.i("GetHour", String.valueOf(dHour));
            Log.i("GetMinute",String.valueOf(dMinute));
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
