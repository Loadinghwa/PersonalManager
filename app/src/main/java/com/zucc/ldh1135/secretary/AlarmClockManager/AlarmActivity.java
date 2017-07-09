package com.zucc.ldh1135.secretary.AlarmClockManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.zucc.ldh1135.secretary.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private TimePicker tp_alarm;
    private FrameLayout frame_repeat;
    private FrameLayout frame_rings;
    private TextView tv_repeat;
    private TextView tv_rings;
    private Switch switch_shake;

    private int tp_hour,tp_minute;

    private Calendar cal;

    String[] week = new String[]{"0","0","0","0","0","0","0","0"};
    String str,year,month,day,hour,minute;
    int i,flag,rings,shake;

    Date date;
    long startTime;

    PendingIntent pi;
    AlarmManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }

        tv_repeat = (TextView) findViewById(R.id.tv_repeat);
        tv_rings = (TextView) findViewById(R.id.tv_ring);
        switch_shake = (Switch) findViewById(R.id.switch_shake);
        frame_rings = (FrameLayout) findViewById(R.id.frame_rings);

        frame_rings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AlarmActivity.this);
                builder.setTitle("铃声");
                builder.setItems(new String[]{"千军万马","古今无双","山霞","悲壮","无音阁","白色背景"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case 0:
                                tv_rings.setText("千军万马");
                                rings = R.raw.r1;
                                break;
                            case 1:
                                tv_rings.setText("古今无双");
                                rings = R.raw.r2;
                                break;
                            case 2:
                                tv_rings.setText("山霞");
                                rings = R.raw.r3;
                                break;
                            case 3:
                                tv_rings.setText("悲壮");
                                rings = R.raw.r4;
                                break;
                            case 4:
                                tv_rings.setText("无音阁");
                                rings = R.raw.r5;
                                break;
                            case 5:
                                tv_rings.setText("白色背景");
                                rings = R.raw.r6;
                                break;
                        }
                    }
                });
                builder.show().setCanceledOnTouchOutside(false);
            }
        });

        frame_repeat = (FrameLayout) findViewById(R.id.frame_repeat);
        frame_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AlarmActivity.this).setTitle("请选择").setMultiChoiceItems(
                        new String[]{"每周日", "每周一", "每周二", "每周三", "每周四", "每周五", "每周六","无"}, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                switch(which)
                                {
                                    case 0:
                                        week[0] = "每周日";
                                        break;
                                    case 1:
                                        week[1] = "每周一";
                                        break;
                                    case 2:
                                        week[2] = "每周二";
                                        break;
                                    case 3:
                                        week[3] = "每周三";
                                        break;
                                    case 4:
                                        week[4] = "每周四";
                                        break;
                                    case 5:
                                        week[5] = "每周五";
                                        break;
                                    case 6:
                                        week[6] = "每周六";
                                        break;
                                    case 7:
                                        week[7] = "无";
                                        break;
                                }
                            }
                        }
                ).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        flag = 0;   //没有选择
                        str = "";

                        //检查是否选择
                        for(i=0;i<8;i++)
                        {
                            if(!week[i].equals("0"))
                            {
                                flag = 1;
                                break;
                            }
                        }

                        if(flag==1)
                        {
                            for(i=0;i<7;i++)
                            {
                                if(!week[i].equals("0"))
                                {
                                    str = str + week[i] + ",";
                                }
                            }
                            flag = 0;
                            for(i=0;i<7;i++)
                            {
                                if(week[i].equals("0"))
                                {
                                    flag = 1;
                                }
                            }
                            if(flag==1)
                            {
                                str = str.substring(0,str.length()-1);
                            }
                            else
                            {
                                str = "每天";
                            }
                        }
                        else
                        {
                            str = "无";
                        }

                        tv_repeat.setText(str);

                        //结束
                        for(i=0;i<8;i++)
                        {
                            week[i] = "0";
                        }
                    }
                })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                flag = 0;
                                for(i=0;i<8;i++)
                                {
                                    week[i] = "0";
                                }
                            }
                        }).show().setCanceledOnTouchOutside(false);
            }
        });

        cal = Calendar.getInstance();
        tp_hour = cal.get(Calendar.HOUR_OF_DAY);
        tp_minute = cal.get(Calendar.MINUTE);

        Intent intent = getIntent();
        year = intent.getStringExtra("year");
        month = intent.getStringExtra("month");
        day = intent.getStringExtra("day");
        hour = intent.getStringExtra("hour");
        minute = intent.getStringExtra("minute");
        rings = intent.getIntExtra("rings",0);
        shake = intent.getIntExtra("shake",0);

        if(shake == 1)
        {
            switch_shake.setChecked(true);
        }
        else
        {
            switch_shake.setChecked(false);
        }

        switch (rings)
        {
            case R.raw.r1:
                tv_rings.setText("千军万马");
                break;
            case R.raw.r2:
                tv_rings.setText("古今无双");
                break;
            case R.raw.r3:
                tv_rings.setText("山霞");
                break;
            case R.raw.r4:
                tv_rings.setText("悲壮");
                break;
            case R.raw.r5:
                tv_rings.setText("无音歌");
                break;
            case R.raw.r6:
                tv_rings.setText("白色背景");
                break;
        }

        tp_alarm = (TimePicker) findViewById(R.id.tp_alarm);
        tp_alarm.setIs24HourView(true);
        tp_alarm.setCurrentHour(Integer.parseInt(hour));
        tp_alarm.setCurrentMinute(Integer.parseInt(minute));
        tp_alarm.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                tp_hour = hourOfDay;
                tp_minute = minute;
            }
        });



    }

    //
    //
    //
    //
    //
    //
    //
    // 以上为create


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_alarm,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.toolbar_save:
                Intent intent = new Intent();
                intent.putExtra("hour", String.valueOf(tp_hour));
                intent.putExtra("minute", String.valueOf(tp_minute));
                intent.putExtra("rings",rings);
                if(switch_shake.isChecked())
                {
                    shake = 1;
                }
                else
                {
                    shake = 0;
                }
                intent.putExtra("shake",shake);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
        return true;
    }
}
