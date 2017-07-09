package com.zucc.ldh1135.secretary.DateManager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class DetailDateActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private int database_version = 1;
    private Database dbHelper;
    String title,date,event,type,intent_data,alarm_time,str;
    int flag;
    int priority;
    long startTime;
    private Calendar cal;
    private int year,month,day,tp_hour,tp_minute,rings,shake;
    Intent intent;
    PendingIntent pi;
    AlarmManager am;

    EditText et_title;
    TextView tv_date;
    TextView tv_type;
    EditText et_event;

    private RadioButton rbtn_none;
    private RadioButton rbtn_first;
    private RadioButton rbtn_second;
    private RadioButton rbtn_third;

    private TextView tv_notice;
    private TextView tv_alarm;
    private Switch switch_notice;
    private FrameLayout frame_alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_date);

        Intent intent = getIntent();
        intent_data = intent.getStringExtra("id");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }

        et_title = (EditText) findViewById(R.id.add_date_title);
        tv_date = (TextView) findViewById(R.id.current_date);
        tv_type = (TextView) findViewById(R.id.add_date_type);
        et_event = (EditText) findViewById(R.id.add_date_event);

        rbtn_none = (RadioButton) findViewById(R.id.radiobtn_none);
        rbtn_first = (RadioButton) findViewById(R.id.radiobtn_first);
        rbtn_second = (RadioButton) findViewById(R.id.radiobtn_second);
        rbtn_third = (RadioButton) findViewById(R.id.radiobtn_third);

        tv_notice = (TextView) findViewById(R.id.tv_notice);
        tv_alarm = (TextView) findViewById(R.id.tv_alarm);
        switch_notice = (Switch) findViewById(R.id.switch_notice);
        frame_alarm = (FrameLayout) findViewById(R.id.frame_alarm);

        getDate();

        dbHelper = new Database(this,"Database.db",null,database_version);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Date",null,"id="+intent_data,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                //id = cursor.getInt(cursor.getColumnIndex("id"));
                title = cursor.getString(cursor.getColumnIndex("title"));
                date = cursor.getString(cursor.getColumnIndex("time"));
                event = cursor.getString(cursor.getColumnIndex("event"));
                type = cursor.getString(cursor.getColumnIndex("type"));
                priority = cursor.getInt(cursor.getColumnIndex("priority"));
                alarm_time = cursor.getString(cursor.getColumnIndex("alarm_time"));
                flag = cursor.getInt(cursor.getColumnIndex("flag"));
                rings = cursor.getInt(cursor.getColumnIndex("rings"));
                shake = cursor.getInt(cursor.getColumnIndex("shake"));

                et_title.setText(title);
                tv_date.setText(date);
                tv_type.setText(type);
                et_event.setText(event);
                tv_alarm.setText(alarm_time);

                if(flag==0)
                {
                    switch_notice.setChecked(false);
                }
                else
                {
                    switch_notice.setChecked(true);
                }

                if(switch_notice.isChecked())
                {
                    frame_alarm.setVisibility(VISIBLE);
                }
                else
                {
                    frame_alarm.setVisibility(GONE);
                }

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
                            tv_notice.setAnimation(inFromLeft);
                            tv_alarm.setAnimation(inFromLeft);
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

                if(priority == 0)
                {
                    rbtn_none.setChecked(true);
                    rbtn_first.setChecked(false);
                    rbtn_second.setChecked(false);
                    rbtn_third.setChecked(false);
                }
                else if(priority == 1)
                {
                    rbtn_none.setChecked(false);
                    rbtn_first.setChecked(true);
                    rbtn_second.setChecked(false);
                    rbtn_third.setChecked(false);
                }
                else if(priority == 2)
                {
                    rbtn_none.setChecked(false);
                    rbtn_first.setChecked(false);
                    rbtn_second.setChecked(true);
                    rbtn_third.setChecked(false);
                }
                else if(priority == 3)
                {
                    rbtn_none.setChecked(false);
                    rbtn_first.setChecked(false);
                    rbtn_second.setChecked(false);
                    rbtn_third.setChecked(true);
                }


            }while(cursor.moveToNext());
        }

        frame_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailDateActivity.this, AlarmActivity.class);
                intent.putExtra("hour",alarm_time.substring(0,2));
                intent.putExtra("minute",alarm_time.substring(3));
                intent.putExtra("rings",rings);
                intent.putExtra("shake",shake);
                startActivityForResult(intent,1);
            }
        });

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



        cursor.close();

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.current_date:
                        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                                if((month+1)<10&&day<10)
                                {
                                    tv_date.setText(year + "-0" + (++month) + "-0" + day);
                                }
                                else if((month+1)<10&&day>=10)
                                {
                                    tv_date.setText(year + "-0" + (++month) + "-" + day);
                                }
                                else if((month+1)>=10&&day>=10)
                                {
                                    tv_date.setText(year + "-" + (++month) + "-" + day);
                                }
                                else if((month+1)>=10&&day<10)
                                {
                                    tv_date.setText(year + "-" + (++month) + "-" + day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                                }
                            }
                        };
                        String str = tv_date.getText().toString();
                        year = Integer.parseInt(str.substring(0,4));
                        month = Integer.parseInt(str.substring(5,7));
                        day = Integer.parseInt(str.substring(8));
                        DatePickerDialog dialog = new DatePickerDialog(DetailDateActivity.this, 0, listener, year, month-1, day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
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
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailDateActivity.this);
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
                builder.show().setCanceledOnTouchOutside(false);
            }
        });

        findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailDateActivity.this);
                builder.setMessage("确认删除吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete("Date","id = ?",new String[]{intent_data});
                        dialog.dismiss();
                        Toast.makeText(DetailDateActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(DetailDateActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

    }

    //获取当前日期
    private void getDate() {
        cal= Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);
        if((month+1)<10&&day<10)
        {
            tv_date.setText(String.valueOf(year)+"-0"+String.valueOf(month)+"-0"+String.valueOf(day));
        }
        else if((month+1)<10&&day>=10)
        {
            tv_date.setText(String.valueOf(year)+"-0"+String.valueOf(month)+"-"+String.valueOf(day));
        }
        else if((month+1)>=10&&day<10)
        {
            tv_date.setText(String.valueOf(year)+"-"+String.valueOf(month)+"-0"+String.valueOf(day));
        }
        else if((month+1)>=10&&day>=10)
        {
            tv_date.setText(String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day));
        }
    }

    private class Date{
        String title;
        String date;
        String type;
        String event;
        int id;

        private Date(int id,String title,String date){
            this.id = id;
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

        public int getId(){
            return id;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_detail_date,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.edit:
                if(TextUtils.isEmpty(et_title.getText())){
                    new AlertDialog.Builder(this).setTitle("提示").setMessage("请输入标题！")
                            .setPositiveButton("好的",null).show().setCanceledOnTouchOutside(false);
                    break;
                }
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                String event = et_event.getText().toString();
                String title = et_title.getText().toString();
                String time = tv_date.getText().toString();
                String type = tv_type.getText().toString();

                //闹钟部分
                tp_hour = Integer.parseInt(tv_alarm.getText().toString().substring(0,2));
                tp_minute = Integer.parseInt(tv_alarm.getText().toString().substring(3));

                try
                {
                    java.util.Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(time + " " + tp_hour + ":" + tp_minute);
                    startTime = d.getTime();
                    //date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse();
                    //startTime = date.getTime();

                }
                catch (Exception e)
                {

                }

                /*
                //判断是否开启提醒服务
                if(switch_notice.isChecked())
                {
                    //获取系统闹钟服务
                    intent = new Intent("ALARM_CLOCK");
                    intent.putExtra("title",title);
                    intent.putExtra("time",time);
                    intent.putExtra("rings",rings);
                    intent.putExtra("shake",shake);
                    intent.putExtra("id",intent_data);
                    //sendBroadcast(intent);
                    pi = PendingIntent.getBroadcast(this,0,intent,0);
                    am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP,startTime,pi);
                }
                */

                values.put("title",title);
                values.put("time",time);
                values.put("event",event);
                values.put("type",type);
                values.put("priority",priority);
                values.put("flag",flag);
                values.put("rings",rings);
                values.put("alarm_time",tv_alarm.getText().toString());

                db.update("Date",values,"id = ?",new String[]{intent_data});
                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(DetailDateActivity.this,MainActivity.class);
                startActivity(intent);
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

                    tv_alarm.setText(data.getStringExtra("hour") + ":" + data.getStringExtra("minute"));
                }
                break;
        }
    }
}
