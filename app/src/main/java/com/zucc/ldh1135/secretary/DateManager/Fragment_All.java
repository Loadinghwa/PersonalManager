package com.zucc.ldh1135.secretary.DateManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.melnykov.fab.FloatingActionButton;
import com.zucc.ldh1135.secretary.AlarmClockManager.AlarmService;
import com.zucc.ldh1135.secretary.DataBase.Database;
import com.zucc.ldh1135.secretary.MainActivity;
import com.zucc.ldh1135.secretary.R;
import com.zucc.ldh1135.secretary.Util.CurrentTime;


import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.List;

import java.text.SimpleDateFormat;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Loading~ on 2017/7/4.
 */

public class Fragment_All extends Fragment {

    private int database_version = 1;
    private Database dbHelper;

    String title,date,type;
    int id;
    long days_dif;
    DateNote date_event;
    private Calendar cal;
    private int year,month,day,hour,minute;
    int rings,shake,flag;
    String alarm_time;

    Date date_now,date_note;

    private List<DateNote> dateList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_all,container,false);

        getDate();

        dateList.clear();
        dbHelper = new Database(getActivity(),"Database.db",null,database_version);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Date",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{

                id = cursor.getInt(cursor.getColumnIndex("id"));
                title = cursor.getString(cursor.getColumnIndex("title"));
                date = cursor.getString(cursor.getColumnIndex("time"));
                rings = cursor.getInt(cursor.getColumnIndex("rings"));
                shake = cursor.getInt(cursor.getColumnIndex("shake"));
                flag = cursor.getInt(cursor.getColumnIndex("flag"));
                alarm_time = cursor.getString(cursor.getColumnIndex("alarm_time"));

                try
                {

                    SimpleDateFormat formart= new SimpleDateFormat("yyyy-MM-dd");
                    //记录时间
                    java.util.Date date_note = formart.parse(date);
                    java.util.Date date_now = new Date();
                    //当前时间
                    if(month<10&&day<10)
                    {
                        date_now = formart.parse(String.valueOf(year)+"-0"+String.valueOf(month)+"-0"+String.valueOf(day));
                    }
                    else if(month<10&&day>=10)
                    {
                        date_now = formart.parse(String.valueOf(year)+"-0"+String.valueOf(month)+"-"+String.valueOf(day));
                    }
                    else if(month>=10&&day<10)
                    {
                        date_now = formart.parse(String.valueOf(year)+"-"+String.valueOf(month)+"-0"+String.valueOf(day));
                    }
                    else if(month>=10&&day>=10)
                    {
                        date_now = formart.parse(String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day));
                    }

                    days_dif =  (date_note.getTime() - date_now.getTime()) / (1000 * 60 * 60 * 24);

                }
                catch (Exception e)
                {

                }


                date_event = new DateNote(id,title,date,days_dif);
                dateList.add(date_event);

            }while(cursor.moveToNext());
        }

        cursor.close();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_all);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToRecyclerView(recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DateAdapter dateAdapter = new DateAdapter(dateList);
        recyclerView.setAdapter(dateAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddDateActivity.class);
                startActivity(intent);
            }
        });




        return view;
    }

    //
    //
    //
    //
    //
    //
    // create

    //获取当前日期
    private void getDate() {
        cal= Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        month=cal.get(Calendar.MONTH)+1;   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
    }


    private class DateNote{
        String title;
        String date;
        int id;
        long dif;

        private DateNote(int id,String title,String date,long dif){
            this.id = id;
            this.title = title;
            this.date = date;
            this.dif = dif;
        }

        public String getTitle(){
            return title;
        }

        public String getDate(){
            return date;
        }

        public int getId(){
            return id;
        }

        public long getDif(){
            return dif;
        }
    }

    public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder>{

        private Context mContext;

        private List<DateNote> mDateList;

        public class ViewHolder extends RecyclerView.ViewHolder{
            CardView cardView;
            View dateView;
            TextView tv_title;
            TextView tv_date;
            TextView tv_dif;

            public ViewHolder(View view){
                super(view);
                cardView = (CardView) view;
                dateView = view;
                tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_date = (TextView) view.findViewById(R.id.tv_date);
                tv_dif = (TextView) view.findViewById(R.id.tv_time_difference);
            }
        }

        public DateAdapter(List<DateNote> dateList){
            mDateList = dateList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
            if(mContext == null){
                mContext = parent.getContext();
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_all,parent,false);
            final ViewHolder holder = new ViewHolder(view);
            holder.dateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    DateNote date = mDateList.get(position);
                    Intent intent = new Intent(getActivity(),DetailDateActivity.class);
                    intent.putExtra("id",String.valueOf(date.getId()));
                    startActivity(intent);
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,int position){
            DateNote date = mDateList.get(position);
            holder.tv_title.setText(date.getTitle());
            holder.tv_date.setText(date.getDate());
            if(date.getDif()<0)
            {
                holder.tv_dif.setText("过去"+ (-date.getDif()) +"天");
            }
            else if(date.getDif()>0)
            {
                holder.tv_dif.setText("还有"+ date.getDif() +"天");
            }
            else
            {
                holder.tv_dif.setText("就是今天");
            }
            //holder.tv_date.setText(date.getId());
            //Glide.with(mContext).load(date.getImageId()).into(holder.dateImage);
        }

        @Override
        public int getItemCount(){
            return mDateList.size();
        }
    }


}
