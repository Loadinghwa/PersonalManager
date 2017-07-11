package com.zucc.ldh1135.secretary.BalanceManager;

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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zucc.ldh1135.secretary.DataBase.Database;
import com.zucc.ldh1135.secretary.DateManager.DetailDateActivity;
import com.zucc.ldh1135.secretary.R;
import com.zucc.ldh1135.secretary.Util.CurrentTime;

public class DetailBalanceActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private Database dbHelper;
    SQLiteDatabase db;

    EditText et_content;
    EditText et_number;
    TextView tv_date;
    TextView tv_type;

    FrameLayout frame_type;
    FrameLayout frame_date;

    Button btn_del;

    Intent intent;

    int database_version = 1;

    int id;
    String content,bDate,number,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_balance);

        intent = getIntent();
        id = intent.getIntExtra("id",0);
        content = intent.getStringExtra("content");
        bDate = intent.getStringExtra("date");
        number = intent.getStringExtra("number");
        type = intent.getStringExtra("type");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }

        et_content = (EditText) findViewById(R.id.add_balance_content);
        et_number = (EditText) findViewById(R.id.add_balance_input_number);

        tv_date = (TextView) findViewById(R.id.balance_date);
        tv_type = (TextView) findViewById(R.id.add_balance_type);

        et_content.setText(content);
        et_number.setText(number);
        tv_date.setText(bDate);
        tv_type.setText(type);

        btn_del = (Button) findViewById(R.id.btn_del);

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailBalanceActivity.this);
                builder.setMessage("确认删除吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete("Date","id = ?",new String[]{String.valueOf(id)});
                        dialog.dismiss();
                        Toast.makeText(DetailBalanceActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(DetailBalanceActivity.this,BalanceActivity.class);
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

        frame_type = (FrameLayout) findViewById(R.id.frame_type);
        frame_date = (FrameLayout) findViewById(R.id.frame_date);

        frame_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailBalanceActivity.this);
                builder.setIcon(R.drawable.icon_type);
                builder.setTitle("类别");
                //    指定下拉列表的显示数据
                final String[] cities = {"收入","支出"};
                //    设置一个下拉的列表选择项
                builder.setItems(cities, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch(which){
                            case 0:
                                tv_type.setText("收入");
                                break;
                            case 1:
                                tv_type.setText("支出");
                                break;
                        }
                    }
                });
                builder.show();
            }
        });

        frame_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            tv_date.setText(year + "-" + (++month) + "-" + day);
                        }
                        //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog
                        (DetailBalanceActivity.this,0,listener
                                ,Integer.parseInt(tv_date.getText().toString().substring(0,4))
                                ,Integer.parseInt(tv_date.getText().toString().substring(5,7))-1
                                ,Integer.parseInt(tv_date.getText().toString().substring(8)));
                datePickerDialog.show();
                datePickerDialog.setCanceledOnTouchOutside(false);
            }
        });

        dbHelper = new Database(this,"Database.db",null,database_version);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_detail_balance,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                intent = new Intent(DetailBalanceActivity.this,BalanceActivity.class);
                startActivity(intent);
                break;
            case R.id.edit:
                if(TextUtils.isEmpty(et_content.getText()))
                {
                    new AlertDialog.Builder(this).setTitle("提示").setMessage("请输入收支内容！").setPositiveButton("好的",null)
                            .show().setCanceledOnTouchOutside(false);
                    break;
                }

                if(TextUtils.isEmpty(et_number.getText()))
                {
                    new AlertDialog.Builder(this).setTitle("提示").setMessage("请输入收支金额！").setPositiveButton("好的",null)
                            .show().setCanceledOnTouchOutside(false);
                    break;
                }

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                String content = et_content.getText().toString();
                String number = et_number.getText().toString();
                String date = tv_date.getText().toString();
                String type = tv_type.getText().toString();

                values.put("content",content);
                values.put("number",number);
                values.put("date",date);
                values.put("type",type);

                db.update("Notepad",values,"id = ?",new String[]{String.valueOf(id)});
                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                finish();
                intent = new Intent(DetailBalanceActivity.this,BalanceActivity.class);
                startActivity(intent);

                break;
        }
        return true;
    }
}
