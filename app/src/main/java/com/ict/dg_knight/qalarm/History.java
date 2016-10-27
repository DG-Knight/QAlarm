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


import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class History extends AppCompatActivity {
    private DbHelper mHelper;
    private SQLiteDatabase mDb;
    private Cursor mCursor;
    private Cursor nCursor;
    private static final Calendar c = Calendar.getInstance();

    Float changSum;
    ArrayList<String> dirArray = new ArrayList<String>();
    ArrayList<BarEntry> entries = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        ListView listView1 = (ListView) findViewById(R.id.listhistory);
        BarChart barChart = (BarChart) findViewById(R.id.chart);// garph
//        Entity คือ แท่งๆ อันนึงในกราฟ หรือก็คือข้อมูล 1 ตัว
//        Dataset คือ เอา แต่ละแท่งมาจับเป็นกลุ่มๆ
//        Data คือ เอา Dataset มารวมกันจะได้กราฟทั้งหมด
        mHelper = new DbHelper(this);
        mDb = mHelper.getWritableDatabase();
        nCursor = mDb.rawQuery("SELECT * FROM " + DbHelper.TABLE_TOTAL, null);
        String total ;
        if (nCursor.isBeforeFirst()){
                nCursor.moveToLast();
            if (nCursor.getPosition()>=1){
                total= nCursor.getString(nCursor.getColumnIndex(DbHelper.TOTAL_TIME));
                changSum = Float.parseFloat(total);//แปลงเป็น float
                Log.v("Total Time", String.valueOf(total));
                Log.e("แปลงTotal",String.valueOf(changSum));
            }else{
                Log.e("tb_total:","NULL");
            }

        }else {
            nCursor.moveToLast();
            total= nCursor.getString(nCursor.getColumnIndex(DbHelper.TOTAL_TIME));
            changSum = Float.parseFloat(total);//แปลงเป็น float
            Log.e("Total Time", String.valueOf(total));
            Log.e("แปลงTotal",String.valueOf(changSum));
        }

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);//ตรวจสอบว่าเป็นวันไหนของสัปดาห์ เช่นวันจันทร์ เป็นวันที่ 2 ของสัปดาห์
        String weekday = new DateFormatSymbols().getWeekdays()[dayOfWeek]; //แปลงวันเป็นตัว
        Log.e("วันนี้วัน",weekday);

        if (changSum!=null){
            entries.add(new BarEntry(changSum, 5));
            if (weekday=="Sunday"){
            entries.add(new BarEntry(0f, 0));
            }else if (weekday=="Monday"){
            entries.add(new BarEntry(4f, 1));
            }else if (weekday=="Tuesday"){
            entries.add(new BarEntry(7f, 2));
            }else if (weekday=="Wednesday"){
            entries.add(new BarEntry(3f, 3));
            }else if (weekday=="Thursday"){
            entries.add(new BarEntry(6f, 4));
            }else if (weekday=="Friday"){
            entries.add(new BarEntry(0f, 5));
            }else if (weekday=="Saturday"){
            entries.add(new BarEntry(0f, 6));
            }
        }else {
            Log.e("changSum","Null");
        }

//        entries.add(new BarEntry(total, 0));
        // พารามิเตอร์สุดท้าย คือ 0 = ตำแหน่งของกราฟที่จะแสดง เช่น 0 = Sunday
//        entries.add(new BarEntry(8f, 1));// พารามิเตอร์หน้า 8f คือความสูงของแท่งกราฟ
//        entries.add(new BarEntry(6f, 2));
//        entries.add(new BarEntry(12f, 3));
//        entries.add(new BarEntry(18f, 4));
//        entries.add(new BarEntry(20f, 5));
//        entries.add(new BarEntry(10f, 6));

        BarDataSet dataset = new BarDataSet(entries, "# of days");
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Sunday");
        labels.add("Monday");
        labels.add("Tuesday");
        labels.add("Wednesday");
        labels.add("Thursday");
        labels.add("Friday");
        labels.add("Thursday");

        BarData data = new BarData(labels, dataset);
//        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        barChart.setData(data);
        barChart.animateY(5000);

        mCursor = mDb.rawQuery("SELECT * FROM " + DbHelper.TABLE_TODAY, null);
        Log.i("Data Count", String.valueOf(mCursor.getCount()));//เป็นคำสั่งเช็คว่ามีข้อมูลที่ดึงมาเก็บใน mCursor อยู่กี่ตัว

        if (mCursor.isBeforeFirst()){
            Log.i("CursorIsBeforeFirst : ", String.valueOf(mCursor.isBeforeFirst()));
//            mCursor.moveToFirst(); //อยากได้ข้อมูลทั้งหมด
            mCursor.moveToLast(); // อยากได้ข้อมูลเดี่ยว
            queryData();
            Log.i("Position :",String.valueOf(mCursor.getPosition()));
            if( mCursor.getPosition() >= 1){ //ยาากได้ 3แถว
//            mCursor.move(-2);
            }
        }else if (mCursor==null){
            Log.i("DATA : ","Not data");
            closeDb();
        }
        ArrayAdapter<String> adapterDir = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dirArray);
        listView1.setAdapter(adapterDir);
    }
    private void queryData() {
        while (!mCursor.isAfterLast()) {
            dirArray.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.WEEK_DAY))+"\t\t|\t\t"
                                 + mCursor.getString(mCursor.getColumnIndex(DbHelper.TIME_DAY))+"\t\t"
                                 + mCursor.getString(mCursor.getColumnIndex(DbHelper.TIME_HOUR))+"\t\t:"
                                 + mCursor.getString(mCursor.getColumnIndex(DbHelper.TIME_MINUTE))+"\t\tน.");//+"\t\t|\t\t"+i+"\t\tชม."
            mCursor.moveToNext();
            Log.i("Data in dirArray : ", String.valueOf(dirArray));
        }
    }

    private void closeDb(){
        if (mDb!=null){
            mHelper.close();
            mDb.close();
        }
    }
    public void onPause() {
        super.onPause();
        if (mDb!=null){
            closeDb();
        }
    }
}