package com.ict.dg_knight.qalarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by DG-Knight on 26/9/2559.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();//แสดงหัวเรื่อง log
    private static final String DB_NAME = "HISTORY";
    private static final int DB_VERSION = 1;

    public static final String TABLE_TODAY = "tb_awake";
    public static final String WEEK_DAY = "day_week"; // เชีน วัน จ
    public static final String TIME_DAY = "date_t"; // วันที่เช่น 18 ตุลาคม 2016
    public static final String TIME_HOUR = "hour_t";
    public static final String TIME_MINUTE = "minute_t";

//    public static final String TABLE_TOTAL = "tb_total";
    public static final String TOTAL_TIME = "total_time";//หาที่ลงยังไม่ได้

    public static final String TABLE_LAST = "tb_last_awake";
    public static final String TIME_LAST_DAY ="date_l";
    public static final String TIME_HOUR_L = "hour_l";
    public static final String TIME_MINUTE_L = "minute_l";

    // คอนสตรักเตอร์
    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }// nullคือ cursor factory

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_TODAY +" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                           + WEEK_DAY + " TEXT, "
                           + TIME_DAY + " TEXT, "
                           + TIME_HOUR + " INTEGER, "
                           + TIME_MINUTE + " INTEGER,"
                            + TOTAL_TIME + " TEXT);");

        Log.i(TAG, "Create Table tb_today Successfully.");

        db.execSQL("CREATE TABLE " + TABLE_LAST +" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                           + TIME_LAST_DAY + " TEXT,"
                           + TIME_HOUR_L   + " INTEGER,"
                           + TIME_MINUTE_L + " INTEGER);");

//        db.execSQL("CREATE TABLE " + TABLE_TOTAL +" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + TOTAL_TIME + " TEXT);");
//        Log.i(TAG, "Create Table tb_last Successfully.");
//        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + TIME_DAY + ", " + TIME_HOUR
//                + ", " + TIME_MINUS + ") VALUES ('2016:09:26',10, 20);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODAY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LAST);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOTAL);
        onCreate(db);
    }
}
