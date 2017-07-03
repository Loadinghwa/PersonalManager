package com.zucc.ldh1135.secretary.DateManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zucc.ldh1135.secretary.Database;
import com.zucc.ldh1135.secretary.R;

/**
 * Created by Loading~ on 2017/7/1.
 */

public class Fragment_All extends Fragment {

    private Database dbHelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_all,container,false);

        dbHelper = new Database(getActivity(),"Database.db",null,2);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Date",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String event = cursor.getString(cursor.getColumnIndex("event"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
            }while(cursor.moveToNext());
        }
        cursor.close();

        return view;
    }

}
