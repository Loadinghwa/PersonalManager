package com.zucc.ldh1135.secretary.DateManager;

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

import com.bumptech.glide.Glide;
import com.zucc.ldh1135.secretary.Database;
import com.zucc.ldh1135.secretary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loading~ on 2017/7/4.
 */

public class Fragment_All extends Fragment {

    private int database_version = 1;
    private Database dbHelper;

    String title,date,type;
    int id;
    Date date_event;

    private List<Date> dateList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_all,container,false);

        dateList.clear();
        dbHelper = new Database(getActivity(),"Database.db",null,database_version);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Date",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"));
                title = cursor.getString(cursor.getColumnIndex("title"));
                date = cursor.getString(cursor.getColumnIndex("time"));
                date_event = new Date(id,title,date);
                dateList.add(date_event);

            }while(cursor.moveToNext());
        }

        cursor.close();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_all);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DateAdapter dateAdapter = new DateAdapter(dateList);
        recyclerView.setAdapter(dateAdapter);

        return view;
    }

    private class Date{
        String title;
        String date;
        String type;
        String event;
        int id;

        private Date(int id,String title,String date){
            this.id = id;
            this.title = title;
            this.date = date;
        }

        private Date(String title,String date,String type,String event){
            this.title = title;
            this.date = date;
            this.type = type;
            this.event = event;
        }

        public String getTitle(){
            return title;
        }

        public String getDate(){
            return date;
        }

        public String getType(){
            return type;
        }

        public String getEvent(){
            return event;
        }

        public int getId(){
            return id;
        }
    }

    public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder>{

        private Context mContext;

        private List<Date> mDateList;

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

        public DateAdapter(List<Date> dateList){
            mDateList = dateList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
            if(mContext == null){
                mContext = parent.getContext();
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_all,parent,false);
            final ViewHolder holder = new ViewHolder(view);
            holder.dateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Date date = mDateList.get(position);
                    Intent intent = new Intent(getActivity(),DetailDateActivity.class);
                    intent.putExtra("id",String.valueOf(date.getId()));
                    startActivity(intent);
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,int position){
            Date date = mDateList.get(position);
            holder.tv_title.setText(date.getTitle());
            holder.tv_date.setText(date.getDate());
            //holder.tv_date.setText(date.getId());
            //Glide.with(mContext).load(date.getImageId()).into(holder.dateImage);
        }

        @Override
        public int getItemCount(){
            return mDateList.size();
        }
    }


}
