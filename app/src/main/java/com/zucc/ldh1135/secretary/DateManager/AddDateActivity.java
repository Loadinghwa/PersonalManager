package com.zucc.ldh1135.secretary.DateManager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.zucc.ldh1135.secretary.AlarmClockManager.AlarmActivity;
import com.zucc.ldh1135.secretary.DataBase.Database;
import com.zucc.ldh1135.secretary.MainActivity;
import com.zucc.ldh1135.secretary.R;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddDateActivity extends AppCompatActivity {

    private Database dbHelper;
    private Toolbar toolbar;
    private TextView textView_time;
    private EditText editText_title;
    private EditText editText_event;
    private TextView textView_type;
    private TextView textView_notice;
    private TextView textView_alarm;
    private Switch switch_notice;
    private FrameLayout frame_alarm;
    private RadioButton rbtn_none;
    private RadioButton rbtn_first;
    private RadioButton rbtn_second;
    private RadioButton rbtn_third;
    private Calendar cal;
    private int year,month,day,hour,minute,week;
    private int priority;

    SQLiteDatabase db;
    private int database_version = 1;
    String str;
    AlarmManager am;
    PendingIntent pi;
    int rings;
    int tp_hour;
    int tp_minute ;
    Intent intent;
    long startTime;
    Date date;
    int shake;

    AlarmManager aManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_date);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }



        editText_title = (EditText) findViewById(R.id.add_date_title);
        editText_event = (EditText) findViewById(R.id.add_date_event);
        textView_type = (TextView) findViewById(R.id.add_date_type);
        textView_time = (TextView) findViewById(R.id.current_date);

        rbtn_none = (RadioButton) findViewById(R.id.radiobtn_none);
        rbtn_first = (RadioButton) findViewById(R.id.radiobtn_first);
        rbtn_second = (RadioButton) findViewById(R.id.radiobtn_second);
        rbtn_third = (RadioButton) findViewById(R.id.radiobtn_third);

        switch_notice = (Switch) findViewById(R.id.switch_notice);

        textView_notice = (TextView) findViewById(R.id.tv_notice);
        textView_alarm = (TextView) findViewById(R.id.tv_alarm);

        frame_alarm = (FrameLayout) findViewById(R.id.frame_alarm);

        getDate();


        rbtn_none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priority = 0;
                rbtn_none.setChecked(true);
                rbtn_first.setChecked(false);
                rbtn_second.setChecked(false);
                rbtn_third.setChecked(false);
            }
        });

        rbtn_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priority = 1;
                rbtn_none.setChecked(false);
                rbtn_first.setChecked(true);
                rbtn_second.setChecked(false);
                rbtn_third.setChecked(false);
            }
        });

        rbtn_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priority = 2;
                rbtn_none.setChecked(false);
                rbtn_first.setChecked(false);
                rbtn_second.setChecked(true);
                rbtn_third.setChecked(false);
            }
        });

        rbtn_third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priority = 3;
                rbtn_none.setChecked(false);
                rbtn_first.setChecked(false);
                rbtn_second.setChecked(false);
                rbtn_third.setChecked(true);
            }
        });

        switch_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switch_notice.isChecked())
                {
                    Animation inFromLeft = new TranslateAnimation(
                            Animation.RELATIVE_TO_PARENT, -1.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f);

                    Animation inFromRight = new TranslateAnimation(
                            Animation.RELATIVE_TO_PARENT, 0.0f,
                            Animation.RELATIVE_TO_PARENT, -1.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f);

                    inFromLeft.setDuration(500);
                    inFromLeft.setInterpolator(new AccelerateInterpolator());
                    inFromRight.setDuration(500);
                    inFromRight.setInterpolator(new AccelerateInterpolator());
                    textView_notice.setAnimation(inFromLeft);
                    textView_alarm.setAnimation(inFromLeft);
                    inFromLeft.startNow();
                    //inFromRight.startNow();
                    frame_alarm.setVisibility(View.VISIBLE);
                }
                else
                {
                    frame_alarm.setVisibility(View.GONE);
                }
            }
        });

        frame_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDateActivity.this,AlarmActivity.class);
                intent.putExtra("year",textView_time.getText().toString().substring(0,4));
                intent.putExtra("month",textView_time.getText().toString().substring(5,7));
                intent.putExtra("day",textView_time.getText().toString().substring(8));
                intent.putExtra("hour",textView_alarm.getText().toString().substring(0,2));
                intent.putExtra("minute",textView_alarm.getText().toString().substring(3));
                intent.putExtra("shake",shake);
                intent.putExtra("rings",rings);
                startActivityForResult(intent,1);
            }
        });

        textView_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.current_date:
                        OnDateSetListener listener = new OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                                if((month+1)<10&&day<10)
                                {
                                    textView_time.setText(year + "-0" + (++month) + "-0" + day);
                                }
                                else if((month+1)<10&&day>=10)
                                {
                                    textView_time.setText(year + "-0" + (++month) + "-" + day);
                                }
                                else if((month+1)>=10&&day>=10)
                                {
                                    textView_time.setText(year + "-" + (++month) + "-" + day);
                                }
                                else if((month+1)>=10&&day<10)
                                {
                                    textView_time.setText(year + "-" + (++month) + "-" + day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                                }
                            }
                        };
                        String str = textView_time.getText().toString();
                        year = Integer.parseInt(str.substring(0,4));
                        month = Integer.parseInt(str.substring(5,7));
                        day = Integer.parseInt(str.substring(8));
                        DatePickerDialog dialog = new DatePickerDialog(AddDateActivity.this, 0, listener, year, month-1, day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);
                        break;

                    default:
                        break;
                }
            }
        });



        findViewById(R.id.frame_type).setOnClickListener(new View.OnClickListener() {
            TextView text_type = (TextView) findViewById(R.id.add_date_type);
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddDateActivity.this);
                builder.setIcon(R.drawable.icon_type);
                builder.setTitle("分类");
                //    指定下拉列表的显示数据
                final String[] cities = {"计划", "工作", "事务"};
                //    设置一个下拉的列表选择项
                builder.setItems(cities, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch(which){
                            case 0:
                                text_type.setText("计划");
                                break;
                            case 1:
                                text_type.setText("工作");
                                break;
                            case 2:
                                text_type.setText("事务");
                                break;
                        }
                    }
                });
                builder.show();
            }
        });

        dbHelper = new Database(this,"Database.db",null,database_version);
        db = dbHelper.getWritableDatabase();
    }

    //
    //
    //
    //
    //
    //
    //
    // create

    //获取当前日期
    private void getDate() {
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);       //获取年月日时分秒
        month = cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        week = cal.get(Calendar.DAY_OF_WEEK);
        String mWeek;
        if(week==1)
        {
            mWeek = "天";
        }
        else if(week==2)
        {
            mWeek = "一";
        }
        else if(week==3)
        {
            mWeek = "二";
        }
        else if(week==4)
        {
            mWeek = "三";
        }
        else if(week==5)
        {
            mWeek = "四";
        }
        else if(week==6)
        {
            mWeek = "五";
        }
        else
        {
            mWeek = "六";
        }
        if(minute<10&&hour<10)
        {
            textView_alarm.setText("0" + String.valueOf(hour) + ":0" + String.valueOf(minute));
        }
        else if(minute<10&&hour>=10)
        {
            textView_alarm.setText(String.valueOf(hour) + ":0" + String.valueOf(minute));
        }
        else if(minute>=10&&hour<10)
        {
            textView_alarm.setText("0" + String.valueOf(hour) + ":" + String.valueOf(minute));
        }
        else
        {
            textView_alarm.setText(String.valueOf(hour) + ":" + String.valueOf(minute));
        }

        if((month+1)<10&&day<10)
        {
            textView_time.setText(String.valueOf(year)+"-0"+String.valueOf(month+1)+"-0"+String.valueOf(day));
        }
        else if((month+1)<10&&day>=10)
        {
            textView_time.setText(String.valueOf(year)+"-0"+String.valueOf(month+1)+"-"+String.valueOf(day));
        }
        else if((month+1)>=10&&day<10)
        {
            textView_time.setText(String.valueOf(year)+"-"+String.valueOf(month+1)+"-0"+String.valueOf(day));
        }
        else if((month+1)>=10&&day>=10)
        {
            textView_time.setText(String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day));
        }
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
                if(TextUtils.isEmpty(editText_title.getText())){
                    new AlertDialog.Builder(this).setTitle("提示").setMessage("请输入标题！")
                            .setPositiveButton("好的",null).show().setCanceledOnTouchOutside(false);
                    break;
                }

                ContentValues values = new ContentValues();
                String event = editText_event.getText().toString();
                String title = editText_title.getText().toString();
                String time = textView_time.getText().toString();
                String type = textView_type.getText().toString();

                //闹钟部分
                tp_hour = Integer.parseInt(textView_alarm.getText().toString().substring(0,2));
                tp_minute = Integer.parseInt(textView_alarm.getText().toString().substring(3));

                if((month+1)<10&&day<10)
                {
                    str = year + "-0" + (++month) + "-0" + day + " " + String.valueOf(tp_hour) + ":" + String.valueOf(tp_minute);
                }
                else if((month+1)<10&&day>=10)
                {
                    str = year + "-0" + (++month) + "-" + day + " " + String.valueOf(tp_hour) + ":" + String.valueOf(tp_minute);
                }
                else if((month+1)>=10&&day<10)
                {
                    str = year + "-" + (++month) + "-0" + day + " " + String.valueOf(tp_hour) + ":" + String.valueOf(tp_minute);
                }
                else if((month+1)>=10&&day>=10)
                {
                    str = year + "-" + (++month) + "-" + day + " " + String.valueOf(tp_hour) + ":" + String.valueOf(tp_minute);
                }

                //Toast.makeText(this,str,Toast.LENGTH_SHORT).show();

                try
                {
                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(str);
                    startTime = date.getTime();
                    //Calendar c = Calendar.getInstance();
                    //c.set(Calendar.HOUR, tp_hour);
                    //c.set(Calendar.MINUTE, tp_minute);
                    //startTime = c.getTimeInMillis(); //long
                }
                catch (Exception e)
                {

                }

                dbHelper = new Database(AddDateActivity.this,"Database.db",null,database_version);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("Date",null,null,null,null,null,null);

                int id = 0;

                if(cursor.moveToFirst()){
                    do{
                        id = cursor.getInt(cursor.getColumnIndex("id"));

                    }while(cursor.moveToNext());
                    id++;
                }


                cursor.close();


                //判断是否开启提醒服务
                if(switch_notice.isChecked())
                {
                    //获取系统闹钟服务
                    intent = new Intent("ALARM_CLOCK");
                    intent.putExtra("title",title);
                    intent.putExtra("time",time);
                    intent.putExtra("rings",rings);
                    intent.putExtra("shake",shake);
                    intent.putExtra("id",String.valueOf(id));
                    //sendBroadcast(intent);
                    pi = PendingIntent.getBroadcast(this,0,intent,0);
                    am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP,startTime,pi);
                }


                values.put("title",title);
                values.put("time",time);
                values.put("alarm_time",textView_alarm.getText().toString());
                values.put("event",event);
                values.put("type",type);
                values.put("rings",rings);
                values.put("priority",priority);
                values.put("shake",shake);
                if(switch_notice.isChecked())
                {
                    values.put("flag",1);
                }
                else
                {
                    values.put("flag",0);
                }

                db.insert("Date",null,values);


                Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(AddDateActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch(requestCode){
            case 1:
                if(resultCode==RESULT_OK)
                {
                    tp_hour = Integer.parseInt(data.getStringExtra("hour"));
                    tp_minute = Integer.parseInt(data.getStringExtra("minute"));
                    rings = data.getIntExtra("rings",0);
                    shake = data.getIntExtra("shake",0);
                    if(tp_hour<10&&tp_minute<10)
                    {
                        textView_alarm.setText("0" + tp_hour + ":0" + tp_minute);
                    }
                    else if(tp_hour<10&&tp_minute>=10)
                    {
                        textView_alarm.setText("0" + tp_hour + ":" + tp_minute);
                    }
                    else if(tp_hour>=10&&tp_minute<10)
                    {
                        textView_alarm.setText(tp_hour + ":0" + tp_minute);
                    }
                    else
                    {
                        textView_alarm.setText(tp_hour + ":" + tp_minute);
                    }
                }
                break;
        }
    }
}
