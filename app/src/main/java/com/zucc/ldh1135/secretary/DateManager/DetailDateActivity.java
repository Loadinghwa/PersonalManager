package com.zucc.ldh1135.secretary.DateManager;

import android.app.DatePickerDialog;
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


import java.util.Calendar;


public class DetailDateActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private int database_version = 1;
    private Database dbHelper;
    String title,date,event,type,intent_data;
    int priority;
    private Calendar cal;
    private int year,month,day;

    EditText et_title;
    TextView tv_date;
    TextView tv_type;
    EditText et_event;

    private RadioButton rbtn_none;
    private RadioButton rbtn_first;
    private RadioButton rbtn_second;
    private RadioButton rbtn_third;

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

                et_title.setText(title);
                tv_date.setText(date);
                tv_type.setText(type);
                et_event.setText(event);

                if(priority == 0)
                {
                    rbtn_none.setChecked(true);
                }
                else if(priority == 1)
                {
                    rbtn_first.setChecked(true);
                }
                else if(priority == 2)
                {
                    rbtn_second.setChecked(true);
                }
                else if(priority == 3)
                {
                    rbtn_third.setChecked(true);
                }


            }while(cursor.moveToNext());
        }

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
                builder.show();
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
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                String event = et_event.getText().toString();
                String title = et_title.getText().toString();
                String time = tv_date.getText().toString();
                String type = tv_type.getText().toString();

                values.put("title",title);
                values.put("time",time);
                values.put("event",event);
                values.put("type",type);
                values.put("priority",priority);
                db.update("Date",values,"id = ?",new String[]{intent_data});
                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(DetailDateActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
