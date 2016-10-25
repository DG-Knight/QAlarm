package com.ict.dg_knight.qalarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by DG-Knight on 18/9/2559.
 */
public class ListSetAdapter extends BaseAdapter {
    Context mContext;
    String[] strTitle;
    String[] strSub;
    int[] resId;

    //Constructer
    public ListSetAdapter(Context context, int[] resId, String[] strTitle,String[]strSub) {
        this.mContext= context;
        this.strTitle = strTitle;
        this.strSub = strSub;
        this.resId = resId;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView==null){
            convertView = mInflater.inflate(R.layout.list_menu_set_alarm,parent,false);


            TextView textTitle = (TextView)convertView.findViewById(R.id.textTitle);
            textTitle.setText(strTitle[position]);
            TextView textSub = (TextView)convertView.findViewById(R.id.textSub);
            textSub.setText(strSub[position]);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
            imageView.setBackgroundResource(resId[position]);
        }
        return convertView;
    }
}
