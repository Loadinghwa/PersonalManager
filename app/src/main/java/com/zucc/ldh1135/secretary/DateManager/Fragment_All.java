package com.zucc.ldh1135.secretary.DateManager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
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

public class Fragment_All extends Fragment {
    private int database_version = 1;
    private Database dbHelper;

    Date date_event;

    private List<Date> dateList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_all,container,false);

        dbHelper = new Database(getActivity(),"Database.db",null,database_version);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Date",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String date = cursor.getString(cursor.getColumnIndex("time"));
                date_event = new Date(title,date);
                dateList.add(date_event);

            }while(cursor.moveToNext());
        }

        cursor.close();

        DateAdapter dateAdapter = new DateAdapter(getActivity(),R.layout.listview_all,dateList);
        ListView listView = (ListView) view.findViewById(R.id.listview_all);
        listView.setAdapter(dateAdapter);
        listView.setAdapter(dateAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Date date = dateList.get(position);
                Toast.makeText(getActivity(),date.getTitle(),Toast.LENGTH_SHORT).show();
            }
        });

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

    public class DateAdapter extends ArrayAdapter<Date>{

        private int resourceId;

        public DateAdapter(Context context, int textViewResourceId,List<Date> objects){
            super(context,textViewResourceId,objects);
            resourceId = textViewResourceId;
        }

        @Override
        public View getView(int position,View convertView,ViewGroup parent){
            Date date = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            if(convertView == null)
            {
                view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            }
            else
            {
                view = convertView;
            }
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            TextView tv_date = (TextView) view.findViewById(R.id.tv_date);

            tv_title.setText(date_event.getTitle());
            tv_date.setText(date_event.getDate());

            return view;
        }
    }

}
