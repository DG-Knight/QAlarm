package com.ict.dg_knight.qalarm;


import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.RingtonePreference;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class ShowEvent extends AppCompatActivity  {

    PowerManager pm;
    PowerManager.WakeLock wl;
    KeyguardManager km;
    KeyguardManager.KeyguardLock kl;
    Ringtone r;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    public static final String MY_PREFS = "my_prefs";
    public static final String MY_VIBRATE ="my_vibrate";
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.i("ShowEvent","onCreate() int DismissLock");
        pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        km = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
        kl = km.newKeyguardLock("ShowEvent");
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK|PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.ON_AFTER_RELEASE,"ShowEvent");
        wl.acquire();//รับค่า WakeLock
        kl.disableKeyguard();

        setContentView(R.layout.activity_show_event);
        vibrator  = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);// get Vibrate การสั่น

        sharedPref = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        final int a;
         a = sharedPref.getInt("selected",-1);//get วิทีเลือกนาฬิกาจาก sharedPref
        Log.i("getDataOnSharedPref :",String.valueOf(a));
        if (a == 0|a == -1){
            // close type default
            ShowEventFragment eventFragment = new ShowEventFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container,eventFragment);
            transaction.replace(R.id.fragment_container, eventFragment);
            transaction.commit();
            clearPreFer();
        }else if (a == 1){
            // close type random image
            ShowEventRandomImageFragment randomImageFragment = new ShowEventRandomImageFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container,randomImageFragment);
            transaction.replace(R.id.fragment_container,randomImageFragment);
            transaction.commit();
            clearPreFer();
        }
        else if (a == 2){
            // cloase type shake
            ShowEventShakeFragment shakeFragment = new ShowEventShakeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container,shakeFragment);
            transaction.replace(R.id.fragment_container, shakeFragment);
            transaction.commit();
            clearPreFer();
        }
        sharedPref = getSharedPreferences(MY_VIBRATE, Context.MODE_PRIVATE);
        final int v;
        v = sharedPref.getInt("vibrate",-1);//get วิทีเลือกนาฬิกาจาก sharedPref
        Log.v("sharePrefVibrate",String.valueOf(v));
        if (v !=1 ){
            long[] pattern = { 0, 200 , 500 , 200 }; //0 คือ เริ่มสั่นทันที 200 คือ สั่น 200 milliseconds 500 คือ หยุดสั่น 500 milliseconds 200 คือ สั่น 200 milliseconds
            vibrator.vibrate(pattern,0);//สั่งให้สั่นจนกว่าจะกดหยุด
            Log.i("Vibrater Status","on");
        }else if (v==1){
            Log.i("Vibrater Status","off");
        }
        Uri notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        if (notif == null){
            notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (notif==null){
                notif= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
        }
        r = RingtoneManager.getRingtone(getApplicationContext(),notif);
        r.play();
    }
    private void clearPreFer(){
        sharedPref = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.clear();// clear ข้อมูลใน sharedPref ก่อนเพิ่มใหม่
        editor.commit();
    }
    @Override
    protected void onResume() {
        super.onResume();

        wl.acquire();

    }
    @Override
    protected void onPause() {
        super.onPause();
        wl.release();
        if (r.isPlaying()){
            r.stop();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        vibrator.cancel();
    }
}
