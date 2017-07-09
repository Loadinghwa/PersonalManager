package com.zucc.ldh1135.secretary.DateManager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zucc.ldh1135.secretary.DataBase.Database;
import com.zucc.ldh1135.secretary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loading~ on 2017/7/1.
 */

public class Fragment_Work extends Fragment {

    private int database_version = 1;
    private Database dbHelper;

    String title,date,type;
    DateNote date_event;

    private List<DateNote> dateList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_work,container,false);

        dateList.clear();
        dbHelper = new Database(getActivity(),"Database.db",null,database_version);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Date",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                title = cursor.getString(cursor.getColumnIndex("title"));
                date = cursor.getString(cursor.getColumnIndex("time"));
                type = cursor.getString(cursor.getColumnIndex("type"));
                if(type.equals("工作"))
                {
                    date_event = new DateNote(title,date);
                    dateList.add(date_event);
                }
            }while(cursor.moveToNext());
        }

        cursor.close();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_work);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DateAdapter dateAdapter = new DateAdapter(dateList);
        recyclerView.setAdapter(dateAdapter);

        return view;
    }

    private class DateNote{
        String title;
        String date;
        String type;
        String event;

        private DateNote(String title,String date){
            this.title = title;
            this.date = date;
        }

        private DateNote(String title,String date,String type,String event){
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

        private Context mContext;

        private List<DateNote> mDateList;

        public class ViewHolder extends RecyclerView.ViewHolder{
            CardView cardView;
            View dateView;
            TextView tv_title;
            TextView tv_date;

            public ViewHolder(View view){
                super(view);
                cardView = (CardView) view;
                dateView = view;
                tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_date = (TextView) view.findViewById(R.id.tv_date);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_work,parent,false);
            final ViewHolder holder = new ViewHolder(view);
            holder.dateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    DateNote date = mDateList.get(position);
                    Toast.makeText(v.getContext(),date.getTitle(),Toast.LENGTH_SHORT).show();
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,int position){
            DateNote date = mDateList.get(position);
            holder.tv_title.setText(date.getTitle());
            holder.tv_date.setText(date.getDate());
            //Glide.with(mContext).load(date.getImageId()).into(holder.dateImage);
        }

        @Override
        public int getItemCount(){
            return mDateList.size();
        }
    }

}
