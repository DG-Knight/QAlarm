package com.ict.dg_knight.qalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

import static com.ict.dg_knight.qalarm.DbHelper.TABLE_TODAY;
import static com.ict.dg_knight.qalarm.DbHelper.TIME_DAY;
import static com.ict.dg_knight.qalarm.DbHelper.TIME_HOUR;
import static com.ict.dg_knight.qalarm.DbHelper.TIME_MINUTE;
import static com.ict.dg_knight.qalarm.DbHelper.WEEK_DAY;

public class SetAlarm extends AppCompatActivity {
    public static final String MY_PREFS = "my_prefs";
    public static final String NUM_SHAKE = "my_num_shake";
    public static final String MY_VIBRATE ="my_vibrate";

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    private TimePickerDialog mTimePicker;

    private String  mSelected; //ตัวแปรที่รับค่าว่าเลือกอะไร
    private int mSelected1;
    String mSelected2 = "selected";

    private String nSelected;//จำนวนเขย่า
    private int nSelected2;//ตำแหน่งจำนวนเย่า
    String nSelected3 = "numShake";

    private String vSelected;
    private int vSelected2;
    String vSelected3 = "vibrate";

    final static int RQS_1 = 1;
    Calendar cal = Calendar.getInstance();
    Calendar calSet = (Calendar) cal.clone();//ตัวแปรปลุกพรุ้งนี้
    DbHelper mHelper;
    SQLiteDatabase mDb;

    Ringtone ringtone ;
    final int RQS_RINGTONEPICKER = 1;
//    private Button btnStart;
//    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_alarm);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int [] resId ={R.drawable.alarm_clock2,
                        R.drawable.musical_note,
                        R.drawable.method,R.drawable.shake_one,R.drawable.vibration};
        String [] strTitle ={"เวลา","ริงโทน","วิธีปิดเสียงนาฬิกา","จำนวนเขย่า","การสั่น"};
        String [] strSub ={"ตั้งเวลาปลุก","เลือกเสียงปลุก","เลือกวิธีปิด","ตั้งจำนวนครั้งการเขย่า","เปิดปิดการสั่น"};

        ListSetAdapter listSetAdapter = new ListSetAdapter(getApplicationContext(),resId, strTitle,strSub);
        ListView listView = (ListView)findViewById(R.id.listView_set);
        listView.setAdapter(listSetAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                if (position == 0){
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    int minus = cal.get(Calendar.MINUTE);
                    mTimePicker=TimePickerDialog.newInstance(onTimeSetListener
                            ,hour // หน่วยเข็มชั่วโมง
                            ,minus  // เข็มนาที
                            ,true   // ใช้ระบบนับแบบ 24-Hr หรือไม่? (0 - 23 นาฬิกา)
                            ,false); // ให้สั่นหรือไม่?
                    mTimePicker.show(getSupportFragmentManager(),"timePicker");

                }else if (position == 1){
                    startRingTonePicker();
                    return;
//                    Toast.makeText(SetAlarm.this, "ริงโทน", Toast.LENGTH_SHORT).show();
                }else if (position == 2){
                    setCloseAlarm();
                }else if (position == 3){
                    setNumShake();//ฟังชันเลือกจำนวนเขย่า
                }else{
                    setVibrate();
                }
            }
        });

    }
 //   ใช้วิธีสร้าง Interface ขึ้นมาใหม่
    public TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
            Toast.makeText(SetAlarm.this, "ตั้งปลุกในเวลา"+hourOfDay+" : "+minute+"นาที", Toast.LENGTH_SHORT).show();
            //เก็บค่าเวลาที่ตั้งปลุกลง sharedPref
            sharedPref = getSharedPreferences("timeAlarm",MODE_PRIVATE);
            editor=sharedPref.edit();
            editor.putInt("Hour",hourOfDay);
            editor.putInt("Minute",minute);
            editor.commit();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND,0);
            calSet.set(Calendar.MILLISECOND,0);
            Log.i("Awake today : ",String.valueOf(calSet));
            Log.i("Awake today : ",String.valueOf(calSet.get(Calendar.HOUR_OF_DAY)
                                                          +":"+calSet.get(Calendar.MINUTE)
                                                          +"นาที\t|"+"วันที่ : "
                                                          +calSet.get(Calendar.DAY_OF_MONTH)));
            if(calSet.compareTo(cal) <= 0){
                //Today Set time passed, count to tomorrow ไปลุกวันพรุ้งนี้
                calSet.add(Calendar.DATE, 1);
                Log.i("Awake tomorrow : ",String.valueOf(calSet));
                Log.i("Awake tomorrow : ",String.valueOf(calSet.get(Calendar.HOUR_OF_DAY)
                                                        +":"+calSet.get(Calendar.MINUTE)
                                                        +"นาที\t|"+"วันที่ : "
                                                        +calSet.get(Calendar.DAY_OF_MONTH)));
            }
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);//ตรวจสอบว่าเป็นวันไหนของสัปดาห์ เช่นวันจันทร์ เป็นวันที่ 2 ของสัปดาห์
            String weekday = new DateFormatSymbols().getShortWeekdays()[dayOfWeek]; //แปลงวันเป็นตัวย่อ เช่น วันจันทร์ ย่อเป็น จ.
            Log.i("วัน : ",weekday);

            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
            Date date = cal.getTime();
            String textDate = dateFormat.format(date);//วันเดือนปีแบบ 18 ตุลาคม 2016
            int presentHour = cal.get(Calendar.HOUR_OF_DAY);
            int presentMinute = cal.get(Calendar.MINUTE);
            insertData(weekday,textDate,presentHour,presentMinute); //บันทึกเวลาขณะตั้งปลุก
//            Log.i("DATE2 : ",String.valueOf(date)); //ค่าที่ได้เป็น DATE2 :: Tue Oct 18 22:46:50 GMT+07:00 2016
            Log.d("เวลาขนะตั้งปลุก",String.valueOf(cal.get(Calendar.HOUR_OF_DAY))+":"+cal.get(Calendar.MINUTE));
            setAlarm(calSet);
            }
    };

    public void setAlarm(Calendar targetCal){
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),RQS_1, intent, 0);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }
    private void insertData(String weekday,String mDate,int mHour,int mMinute){
        mHelper = new DbHelper(getApplicationContext());
        mDb = mHelper.getWritableDatabase();//เปิดฐานข้อมูล

        ContentValues values = new ContentValues();
        values.put(WEEK_DAY,weekday);
        values.put(TIME_DAY,mDate); //set data
        values.put(TIME_HOUR,mHour); //set data
        values.put(TIME_MINUTE,mMinute); //set data
        mDb.insert(TABLE_TODAY,null,values);
    }
    public void startRingTonePicker(){
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        startActivityForResult(intent, RQS_RINGTONEPICKER);
        Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
        String title = ringtone.getTitle(this);

//http://stackoverflow.com/questions/19187834/how-to-get-ringtone-name-in-android
//       Toast.makeText(this,"Ring Tone Name"+ title, Toast.LENGTH_SHORT).show();
        Log.i("Ring Tone Name",title);
    }
    private void setCloseAlarm(){

        final String[] items = { getString(R.string.basic),getString(R.string.mt_picture),getString(R.string.mt_sharke),getString(R.string.mt_playGame) };

        AlertDialog.Builder builder = new AlertDialog.Builder(SetAlarm.this);
        builder.setTitle(R.string.method_alarm);
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                            Log.i("Choose",String.valueOf(which));
                mSelected1 = which;//what Choose items
                mSelected = items[which];
            }
        });
        builder.setPositiveButton(R.string.dlgOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ส่วนนี้สำหรับเซฟค่าลง database หรือ SharedPreferences.
                sharedPref = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
                editor = sharedPref.edit();
                editor.clear();// clear ข้อมูลใน sharedPref ก่อนเพิ่มใหม่
                editor.commit();
                int a = sharedPref.getInt("selected",-1);
                Log.i("CLEAR DATA :",String.valueOf(a));
                editor.putInt(mSelected2, mSelected1);
                editor.commit();
                Log.i("Input Value :",mSelected+":"+String.valueOf(mSelected1));
//                            int user_id = sharedPref.getInt(getString(R.string.basic), -1);//อ่านข้อมูลใน sharePef จะได้ค่า value มา
//                            Log.i("MySetting",String.valueOf(user_id));

                Toast.makeText(SetAlarm.this, "คุณเลือก"+mSelected, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.dlgCancel,null);
        builder.create();
        builder.show();
    }
    private void setNumShake(){

        sharedPref =getSharedPreferences("my_prefs",Context.MODE_PRIVATE);//ใ้ชเพื่อเช็คว่าปิดด้วยการเขย่าไหม ถ้าปิดจะอนุญาตให้ตั้งค่าจำนวนได้
        final int a;
        a = sharedPref.getInt("selected",-1);//get วิทีเลือกนาฬิกาจาก sharedPref
        Log.e("Choose Num Shake:","ตั้งจัำนวนเขย่า");
        if (a==2){
            final String [] num = {"20 (พื้นฐาน)","25","30","35","40"};
            AlertDialog.Builder numDlg = new AlertDialog.Builder(SetAlarm.this);
            numDlg.setTitle(R.string.dlgChooseNumShake);
            numDlg.setSingleChoiceItems(num, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                  Log.i("Choose",String.valueOf(which));
                    nSelected2 = which;//ตำแหน่งจำนวนเขย่า
                    nSelected = num[which];//จำนวนเขย่า
                }
            });

            numDlg.setPositiveButton(R.string.dlgOk, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // ส่วนนี้สำหรับเซฟค่าลง database หรือ SharedPreferences.
                    sharedPref = getSharedPreferences(NUM_SHAKE, Context.MODE_PRIVATE);
                    editor = sharedPref.edit();
                    editor.clear();// clear ข้อมูลใน sharedPref ก่อนเพิ่มใหม่
                    editor.commit();
                    int a = sharedPref.getInt("numShake",-1);
                    Log.i("CLEAR DATA :",String.valueOf(a));
                    editor.putInt(nSelected3, nSelected2);
                    editor.commit();
                    Log.i("Input Value :", nSelected +":"+String.valueOf(nSelected2));
//              int user_id = sharedPref.getInt(getString(R.string.basic), -1);//อ่านข้อมูลใน sharePef จะได้ค่า value มา
//              Log.i("MySetting",String.valueOf(user_id));

                    Toast.makeText(SetAlarm.this, "คุณเลือกเย่า\t"+ nSelected + "\tครั้ง", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            numDlg.setNegativeButton(R.string.dlgCancel,null);
            numDlg.create();
            numDlg.show();
        }else {
            Toast.makeText(getApplicationContext(), "คุณไม่ได้ตั้งค่าวิธีปิดเป็นแบบเขย่า", Toast.LENGTH_SHORT).show();
        }

    }
    private void setVibrate(){
        final String [] onOf = {getString(R.string.vibrate_on),getString(R.string.vibrate_close)};
        AlertDialog.Builder setVibrate = new AlertDialog.Builder(SetAlarm.this);
        setVibrate.setTitle(R.string.set_vibrate);
        setVibrate.setSingleChoiceItems(onOf, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                vSelected2 = which;
                Log.v("vSelected2",String.valueOf(which));
                vSelected = onOf[which];
            }
        });
        setVibrate.setPositiveButton(R.string.dlgOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPref = getSharedPreferences(MY_VIBRATE,Context.MODE_PRIVATE);
                editor = sharedPref.edit();
                editor.putInt(vSelected3,vSelected2);
                editor.commit();
                dialog.dismiss();
            }
        });
        setVibrate.setNegativeButton(R.string.dlgCancel,null);
        setVibrate.create();
        setVibrate.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDb!=null){
            mHelper.close();
            mDb.close();
        }
    }
}
//            int day  = cal.get(Calendar.DAY_OF_MONTH);//ตรวจสอบว่าเป็นวันไหนของเดือน เช่น 18
//            Log.i("Day : ---",String.valueOf(day));
//            int day = cal.get(Calendar.DAY_OF_YEAR);//ตรวจสอบว่าเป็นวันที่เท่าไรของปีจาก 365วัน เช่น 292 จาก 365 วัน
//            Log.i("DAY-----:",String.valueOf(day));
//            int day  = cal.get(Calendar.DATE);//ตรวจสอบว่าเป็นวันไหนของเดือน เช่น 18 เหมือน DAY_OF_MONTH
//            Log.i("Day : ---",String.valueOf(day));
//            int day  = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);//ตรวจสอบว่าเป็นวันที่เท่าไรของสัปดาห์ เหมือน DAY_OF_WEEK
//            Log.i("Days : ---",String.valueOf(day));
//            int day  = cal.get(Calendar.YEAR);//ตรวจสอบว่าเป็นใดเช่น 2016
//            Log.i("Days : ---",String.valueOf(day));