package com.zucc.ldh1135.secretary.DateManager;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.zucc.ldh1135.secretary.Database;
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
    private RadioButton rbtn_none;
    private RadioButton rbtn_first;
    private RadioButton rbtn_second;
    private RadioButton rbtn_third;
    private Calendar cal;
    private int year,month,day;
    private int priority;
    Date date;
    SimpleDateFormat sDate;
    String dateNow;
    SQLiteDatabase db;
    private int database_version = 1;

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

        /*
        date = new Date();
        sDate = new SimpleDateFormat("yyyy-MM-dd");
        dateNow = sDate.format(date);
        */

        editText_title = (EditText) findViewById(R.id.add_date_title);
        editText_event = (EditText) findViewById(R.id.add_date_event);
        textView_type = (TextView) findViewById(R.id.add_date_type);
        textView_time = (TextView) findViewById(R.id.current_date);

        rbtn_none = (RadioButton) findViewById(R.id.radiobtn_none);
        rbtn_first = (RadioButton) findViewById(R.id.radiobtn_first);
        rbtn_second = (RadioButton) findViewById(R.id.radiobtn_second);
        rbtn_third = (RadioButton) findViewById(R.id.radiobtn_third);

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

        getDate();

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

    //获取当前日期
    private void getDate() {
        cal= Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);
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
                ContentValues values = new ContentValues();
                String event = editText_event.getText().toString();
                String title = editText_title.getText().toString();
                String time = textView_time.getText().toString();
                String type = textView_type.getText().toString();
                values.put("title",title);
                values.put("time",time);
                values.put("event",event);
                values.put("type",type);
                values.put("priority",priority);
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
}
