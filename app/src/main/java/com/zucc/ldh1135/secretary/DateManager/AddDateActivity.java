package com.zucc.ldh1135.secretary.DateManager;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.zucc.ldh1135.secretary.Database;
import com.zucc.ldh1135.secretary.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddDateActivity extends AppCompatActivity {

    private Database dbHelper;
    private Toolbar toolbar;
    private TextView textView_time;
    private TextView textView_title;
    private TextView textView_event;
    private Calendar cal;
    private int year,month,day;
    Date date;
    SimpleDateFormat sDate;
    String dateNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_date);
        dbHelper = new Database(this,"Database.db",null,2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }

        date = new Date();
        sDate = new SimpleDateFormat("yyyy-MM-dd");
        dateNow = sDate.format(date);

        textView_title = (TextView) findViewById(R.id.add_date_title);
        textView_event = (TextView) findViewById(R.id.add_date_event);
        textView_time = (TextView) findViewById(R.id.current_date);
        textView_time.setText(dateNow);

        getDate();
        textView_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.current_date:
                        OnDateSetListener listener = new OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                                textView_time.setText(year + "-" + (++month) + "-" + day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                            }
                        };
                        DatePickerDialog dialog = new DatePickerDialog(AddDateActivity.this, 0, listener, year, month, day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                        dialog.show();
                        break;

                    default:
                        break;
                }
            }
        });



        findViewById(R.id.frame_type).setOnClickListener(new View.OnClickListener() {
            TextView text_event = (TextView) findViewById(R.id.add_date_event);
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddDateActivity.this);
                builder.setIcon(R.drawable.icon_type);
                builder.setTitle("分类");
                //    指定下拉列表的显示数据
                final String[] cities = {"计划", "工作", "事件"};
                //    设置一个下拉的列表选择项
                builder.setItems(cities, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch(which){
                            case 0:
                                text_event.setText("计划");
                                break;
                            case 1:
                                text_event.setText("工作");
                                break;
                            case 2:
                                text_event.setText("事件");
                                break;
                        }
                    }
                });
                builder.show();
            }
        });


    }

    //获取当前日期
    private void getDate() {
        cal= Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        Log.i("wxy","year"+year);
        month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);
        textView_time.setText(year+"-"+month+"-"+day);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_add_date,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.save:
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                String event = textView_event.getText().toString();
                String title = textView_title.getText().toString();
                String time = textView_time.getText().toString();
                values.put("title",title);
                values.put("time",time);
                values.put("event",event);
                db.insert("Date",null,values);
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
