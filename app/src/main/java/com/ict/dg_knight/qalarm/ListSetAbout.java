package com.ict.dg_knight.qalarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by DG-Knight on 26/9/2559.
 */

public class ListSetAbout extends BaseAdapter {
    Context mContext;
    String[] strTitle;
    String[] strSub;

    public ListSetAbout(Context context, String[] strTitle,String[]strSub) {
        this.mContext= context;
        this.strTitle = strTitle;
        this.strSub = strSub;

    }


    @Override
    public int getCount() {
        return strTitle.length;
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
            convertView = mInflater.inflate(R.layout.list_menu_about,parent,false);

            TextView textTitle = (TextView)convertView.findViewById(R.id.about_title);
            textTitle.setText(strTitle[position]);
            TextView textSub = (TextView)convertView.findViewById(R.id.about_sub);
            textSub.setText(strSub[position]);

        }
        return convertView;
    }
}
