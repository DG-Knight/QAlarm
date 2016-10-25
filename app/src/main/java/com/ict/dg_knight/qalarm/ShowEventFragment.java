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
import android.widget.TextClock;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.ict.dg_knight.qalarm.DbHelper.TABLE_LAST;
import static com.ict.dg_knight.qalarm.DbHelper.TABLE_TODAY;
//import static com.ict.dg_knight.qalarm.DbHelper.TABLE_TOTAL;
import static com.ict.dg_knight.qalarm.DbHelper.TIME_HOUR_L;
import static com.ict.dg_knight.qalarm.DbHelper.TIME_LAST_DAY;
import static com.ict.dg_knight.qalarm.DbHelper.TIME_MINUTE_L;
import static com.ict.dg_knight.qalarm.DbHelper.TOTAL_TIME;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowEventFragment extends Fragment {

    private Button btnStop;
    final static int RQS_1 = 1;
    DbHelper mHelper;
    SQLiteDatabase mDb;
    Cursor mCursor;
    public static final Calendar cal = Calendar.getInstance();
    int closeHour = cal.get(Calendar.HOUR_OF_DAY);//รับค่าช่วยโมงปัจจุบัน
    int closeMinute = cal.get(Calendar.MINUTE);//รับค่านาทีปัจจุบัน
    int dHour;
    int dMinute;


    public ShowEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View rootView = inflater.inflate(R.layout.fragment_show_event, container, false);
        btnStop= (Button)rootView.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ใส่คำสั้งหยุด
                cancelAlarm();
                saveTime();
                getDatTime();
                calTime();
                getActivity().finish();
            }
        });
        TextClock textClock = (TextClock)rootView.findViewById(R.id.digitalClock);
        TextView wakeUp = (TextView)rootView.findViewById(R.id.txtWakeUp);

        return rootView;
    }
    private void cancelAlarm(){
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), RQS_1, intent, 0);
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
        Log.e("Insert Data: ",String.valueOf(mDb));
        closeDb();// เรียกใช้ฟังก์ชันปิดฐานข้อมูล
    }
    private void getDatTime(){

        int sum ;
        openDb();//เปิดฐานข้อมูล
        mCursor = mDb.rawQuery("SELECT * FROM "
                                       + DbHelper.TABLE_TODAY, null);//เลือกคิวรี่ข้อมูลจากตาราง TABLE_TODAY
        if (mCursor.isBeforeFirst()){
            mCursor.moveToLast();
            dHour = mCursor.getInt(mCursor.getColumnIndex(DbHelper.TIME_HOUR));//ดึงข้อมูลในแถวสุดท้าย คอลัมที่มีชื่อว่า TIME_HOUR
            dMinute = mCursor.getInt(mCursor.getColumnIndex(DbHelper.TIME_MINUTE));//ดึงข้อมูลในแถวสุดท้าย คอลัมที่มีชื่อว่า TIME_MINUTE
            Log.i("GetHour", String.valueOf(dHour));
            Log.i("GetMinute",String.valueOf(dMinute));
            calTime();
        }else {
            mCursor.moveToLast();
            dHour = mCursor.getInt(mCursor.getColumnIndex(DbHelper.TIME_HOUR));//ดึงข้อมูลในแถวสุดท้าย คอลัมที่มีชื่อว่า TIME_HOUR
            dMinute = mCursor.getInt(mCursor.getColumnIndex(DbHelper.TIME_MINUTE));//ดึงข้อมูลในแถวสุดท้าย คอลัมที่มีชื่อว่า TIME_MINUTE
            Log.i("GetHour", String.valueOf(dHour));
            Log.i("GetMinute",String.valueOf(dMinute));
            calTime();
        }
        closeDb();
    }
    private void calTime(){
        openDb();
        int sumHour;
        int sumMinute;
        String strI;
        if (dHour<=closeHour&&dHour<=closeMinute){
//                mCursor.moveToLast();
//                int posiont = mCursor.getPosition();
//                Log.d("Position",String.valueOf(mCursor.getPosition()));

            Log.d("Case","1");
            Log.d("เวลาปิดนาฬิกา",String.valueOf(closeHour)+""+String.valueOf(closeMinute));
            sumHour = closeHour-dHour;
            sumMinute = closeMinute-dMinute;
            strI = String.valueOf(sumHour)+"."+String.valueOf(sumMinute);
            Log.i("sumHour",strI);

//            ContentValues v = new ContentValues();
//            v.put(DbHelper.TOTAL_TIME,strI);
//            mDb.update(DbHelper.TABLE_TODAY,v,DbHelper.TOTAL_TIME+"id=?",new String[]{""});

        }else if (dHour<=closeHour&&dMinute>closeMinute){
            Log.i("Case","2");
        }else{
            Log.i("Case","3");
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
