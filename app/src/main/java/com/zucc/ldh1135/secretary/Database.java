package com.zucc.ldh1135.secretary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.sql.SQLData;

/**
 * Created by Loading~ on 2017/7/1.
 */

public class Database extends SQLiteOpenHelper {

    public static final String CREATE_DATE = "create table Date ("
            + "id integer primary key autoincrement, "
            + "event text, "
            + "time text, "
            + "notice blob, "
            + "note text, "   //备注
            + "top blob)";
    public static final String CREATE_NOTEPAD = "create table Notepad ("
            + "id integer primary key autoincrement, "
            + "balance integer, "  //收支数额
            + "type text, "   //收支类型
            + "date text)";     //收支日期

    private Context mContext;

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_DATE);
        db.execSQL(CREATE_NOTEPAD);
        Toast.makeText(mContext,"success!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}