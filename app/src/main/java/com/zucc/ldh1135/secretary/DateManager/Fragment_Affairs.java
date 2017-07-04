package com.zucc.ldh1135.secretary.DateManager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zucc.ldh1135.secretary.Database;
import com.zucc.ldh1135.secretary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loading~ on 2017/7/1.
 */

public class Fragment_Affairs extends Fragment {

    private int database_version = 1;
    private Database dbHelper;

    String title,date,type;
    Date date_event;

    private List<Date> dateList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_affairs,container,false);

        dbHelper = new Database(getActivity(),"Database.db",null,database_version);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Date",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                title = cursor.getString(cursor.getColumnIndex("title"));
                date = cursor.getString(cursor.getColumnIndex("time"));
                type = cursor.getString(cursor.getColumnIndex("type"));
                if(type.equals("事务"))
                {
                    date_event = new Date(title,date);
                    dateList.add(date_event);
                }
            }while(cursor.moveToNext());
        }

        cursor.close();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_affairs);
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

        private Date(String title,String date){
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
    }

    public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder>{

        private List<Date> mDateList;

        public class ViewHolder extends RecyclerView.ViewHolder{
            View dateView;
            TextView tv_title;
            TextView tv_date;

            public ViewHolder(View view){
                super(view);
                dateView = view;
                tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_date = (TextView) view.findViewById(R.id.tv_date);
            }
        }

        public DateAdapter(List<Date> dateList){
            mDateList = dateList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_all,parent,false);
            final ViewHolder holder = new ViewHolder(view);
            holder.dateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Date date = mDateList.get(position);
                    Toast.makeText(v.getContext(),date.getTitle(),Toast.LENGTH_SHORT).show();
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,int position){
            Date date = mDateList.get(position);
            holder.tv_title.setText(date.getTitle());
            holder.tv_date.setText(date.getDate());
        }

        @Override
        public int getItemCount(){
            return mDateList.size();
        }
    }


}
