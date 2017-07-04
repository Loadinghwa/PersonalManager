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
import android.widget.ArrayAdapter;
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
    private TextView textView_date;

    private List<Date> dateList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_all,container,false);

        textView_date = (TextView) view.findViewById(R.id.date);
        dbHelper = new Database(getActivity(),"Database.db",null,database_version);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Date",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String event = cursor.getString(cursor.getColumnIndex("event"));
                String date = cursor.getString(cursor.getColumnIndex("time"));
                textView_date.setText(date);
            }while(cursor.moveToNext());
        }

        cursor.close();

        return view;
    }


    public class Date{
        String title;
        String date;
        String type;
        String event;

        Date(String title,String date,String type,String event){
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

            //

            return view;
        }
    }

}
