package com.ict.dg_knight.qalarm;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowEventRandomImageFragment extends Fragment {
    public ShowEventRandomImageFragment() {
        // Required empty public constructor
    }

    int[] img_ran1 = {R.drawable.animal1, R.drawable.animal2, R.drawable.animal3
            , R.drawable.animal4, R.drawable.animal5, R.drawable.animal6
            , R.drawable.car1, R.drawable.car2, R.drawable.car3
            , R.drawable.car4, R.drawable.car5, R.drawable.car6
            , R.drawable.home1, R.drawable.home2, R.drawable.home3
            , R.drawable.home4, R.drawable.home5, R.drawable.home6
    };

    String[] question = {"ภาพใดบ้างเป็นภาพสัตว์ ?", "ภาพใดบ้างเป็นภาพรถ ?", "ภาพใดบ้างเป็นภาพบ้าน ?"};

    public class ImageAdapter extends BaseAdapter{
        private Context mContext;
        public ImageAdapter(Context c){
            mContext =c;
        }

        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null){
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(4, 4, 4, 4);
            }else{
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(getRandomImage());
            return imageView;
        }
    }
    private int getRandomImage(){
        Random r = new Random();
        int select = r.nextInt(img_ran1.length);
        return img_ran1[select];
    }
    private String getRandomQuestion(){
        Random random = new Random();
        int numQuest =  random.nextInt(question.length);
        return question[numQuest];
    }
    private void cancelAlarm(){
        final int RQS_1 = 1;
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show_event_random_image, container, false);

        TextView textView = (TextView)rootView.findViewById(R.id.txtQuest);
        textView.setText(getRandomQuestion());

        final GridView gridview = (GridView)rootView.findViewById(R.id.gridView);
        gridview.setAdapter(new ImageAdapter(rootView.getContext()));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Your select "+getString(img_ran1[position]), Toast.LENGTH_SHORT).show();
            }
        });
        Button btnOK = (Button)rootView.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
                getActivity().finish();
            }
        });
        return rootView;
    }
}

