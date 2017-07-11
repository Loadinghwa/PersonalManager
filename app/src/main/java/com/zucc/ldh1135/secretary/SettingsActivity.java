package com.zucc.ldh1135.secretary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zucc.ldh1135.secretary.DataBase.Database;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;

    Database dbHelper;
    int database_version = 1;

    String language;

    FrameLayout frame_language;
    TextView app_language;
    TextView tv_language;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dbHelper = new Database(SettingsActivity.this,"Database.db",null,database_version);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();

        values.put("lang","Simple Chinese");
        db.insert("Language",null,values);
        values.clear();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }

        frame_language = (FrameLayout) findViewById(R.id.frame_language);

        setLanguage();

        frame_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("语种");
                //    指定下拉列表的显示数据
                final String[] cities = {"简体中文", "English"};
                //    设置一个下拉的列表选择项
                builder.setItems(cities, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        switch(which){
                            case 0:
                                values.put("lang","Simple Chinese");
                                db.update("Language",values,"id = ?",new String[]{"0"});
                                values.clear();
                                break;
                            case 1:
                                values.put("lang","English");
                                db.update("Language",values,"id = ?",new String[]{"0"});
                                values.clear();
                                break;

                        }
                    }
                });
                builder.show();
            }
        });

        setLanguage();
    }

    private void setLanguage()
    {
        app_language = (TextView) findViewById(R.id.app_language);
        tv_language = (TextView) findViewById(R.id.tv_language);

        dbHelper = new Database(SettingsActivity.this,"Database.db",null,database_version);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Language",null,null,null,null,null,null);
        if(cursor.moveToFirst())
        {
            do{
                language = cursor.getString(cursor.getColumnIndex("lang"));

            }while(cursor.moveToNext());
        }
        cursor.close();
        if(language.equals("English")) {
            app_language.setText("Language");
            tv_language.setText("English");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();

                break;
        }
        return true;
    }
}
