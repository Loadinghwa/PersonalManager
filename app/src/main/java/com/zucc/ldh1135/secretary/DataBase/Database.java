package com.zucc.ldh1135.secretary.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.sql.SQLData;

/**
 * Created by Loading~ on 2017/7/1.
 */

public class Database extends SQLiteOpenHelper {

    private static final String CREATE_DATE = "create table Date ("
            + "id integer primary key autoincrement, "
            + "title text, "    //事件标题
            + "time text, "    //时间
            + "type text, "
            + "priority integer, " //优先级
            + "flag integer, "   //是否开启闹钟
            + "alarm_time text, "  //闹钟时间
            + "shake integer, "
            + "rings integer,"
            + "event text) ";   //事件内容

    private static final String CREATE_NOTEPAD = "create table Notepad ("
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
        db.execSQL("drop table if exists DATE");
        db.execSQL("drop table if exists NOTEPAD");
    }
}
