package com.ict.dg_knight.qalarm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.charts.BarChart;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.util.ArrayList;

public class History extends AppCompatActivity {
    SQLiteDatabase mDb;
    DbHelper mHelper;
    Cursor mCursor;
    ArrayList<String> dirArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        ListView listView1 = (ListView) findViewById(R.id.listhistory);
        BarChart barChart = (BarChart) findViewById(R.id.chart);// garph

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4f, 0)); // พารามิเตอร์สุดท้าย คือ 0 = ตำแหน่งของกราฟที่จะแสดง เช่น 0 = Sunday
        entries.add(new BarEntry(8f, 1));// พารามิเตอร์หน้า 8f คือความสูงของแท่งกราฟ
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(18f, 4));
        entries.add(new BarEntry(20f, 5));
        entries.add(new BarEntry(10f, 6));

        BarDataSet dataset = new BarDataSet(entries, "# of Calls");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Sunday");
        labels.add("Monday");
        labels.add("Tuesday");
        labels.add("wednesday");
        labels.add("Thursday");
        labels.add("Friday");
        labels.add("Thursday");

        BarData data = new BarData(labels, dataset);
         dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        barChart.setData(data);
        barChart.animateY(5000);

        mHelper = new DbHelper(this);
        mDb = mHelper.getWritableDatabase();
        mCursor = mDb.rawQuery("SELECT * FROM " + DbHelper.TABLE_TODAY, null);
        Log.i("Data Count", String.valueOf(mCursor.getCount()));//เป็นคำสั่งเช็คว่ามีข้อมูลที่ดึงมาเก็บใน mCursor อยู่กี่ตัว

        if (mCursor.isBeforeFirst()){
            Log.i("CursorIsBeforeFirst : ", String.valueOf(mCursor.isBeforeFirst()));
            mCursor.moveToLast();
//            mCursor.moveToFirst();
            Log.i("Position :",String.valueOf(mCursor.getPosition()));
            if( mCursor.getPosition() >= 2){
                mCursor.move(-2);
                queryData();
            }
        }else if (mCursor==null){
            Log.i("DATA : ","Not data");
        }

        ArrayAdapter<String> adapterDir = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dirArray);
        listView1.setAdapter(adapterDir);
    }
    private void queryData() {
        int i = 10; // ฮาดโค้ดไปก่อน ที่ถูก ต้อง เวลาตั้ง - เวลานอน
        while (!mCursor.isAfterLast()) {
            dirArray.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.WEEK_DAY))+"\t\t|\t\t"
                                 + mCursor.getString(mCursor.getColumnIndex(DbHelper.TIME_DAY))
                                 +"\t\t|\t\t"+i+"\t\tชม.");
            mCursor.moveToNext();
            Log.i("Data in dirArray : ", String.valueOf(dirArray));
        }
    }
    public void onPause() {
        super.onPause();
        if (mDb!=null){
            mHelper.close();
            mDb.close();
        }
    }
}