package com.zucc.ldh1135.secretary.BalanceManager;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zucc.ldh1135.secretary.DataBase.Database;
import com.zucc.ldh1135.secretary.DateManager.AddDateActivity;
import com.zucc.ldh1135.secretary.R;
import com.zucc.ldh1135.secretary.Util.CashierInputFilter;
import com.zucc.ldh1135.secretary.Util.CurrentTime;

public class AddBalanceActivity extends AppCompatActivity {

    private Toolbar toolbar;

    Database dbHelper;
    SQLiteDatabase db;

    EditText et_content;
    EditText et_number;
    TextView tv_date;
    TextView tv_type;

    FrameLayout frame_type;
    FrameLayout frame_date;

    Intent intent;

    int database_version = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_balance);

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

        InputFilter[] filters = {new CashierInputFilter()};
        et_number.setFilters(filters);

        tv_date = (TextView) findViewById(R.id.balance_date);
        tv_date.setText(new CurrentTime().getYear() + "-" + new CurrentTime().getsMonth() + "-" + new CurrentTime().getsDay());
        tv_type = (TextView) findViewById(R.id.add_balance_type);

        frame_type = (FrameLayout) findViewById(R.id.frame_type);
        frame_date = (FrameLayout) findViewById(R.id.frame_date);

        frame_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddBalanceActivity.this);
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
                            tv_date.setText(year + "-" + (++month) + "-" + day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                        }
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog
                        (AddBalanceActivity.this,0,listener
                                ,Integer.parseInt(tv_date.getText().toString().substring(0,4))
                                ,Integer.parseInt(tv_date.getText().toString().substring(5,7))-1
                                ,Integer.parseInt(tv_date.getText().toString().substring(8)));
                datePickerDialog.show();
                datePickerDialog.setCanceledOnTouchOutside(false);
            }
        });

        dbHelper = new Database(this,"Database.db",null,database_version);
    }

    //
    //
    //
    //
    //
    // create

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_add_balance,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            case R.id.save:

                if(TextUtils.isEmpty(et_content.getText()))
                {
                    new AlertDialog.Builder(this).setTitle("提示").setMessage("请输入收支内容！").setPositiveButton("好的",null)
                            .show().setCanceledOnTouchOutside(false);
                }

                if(TextUtils.isEmpty(et_number.getText()))
                {
                    new AlertDialog.Builder(this).setTitle("提示").setMessage("请输入收支金额！").setPositiveButton("好的",null)
                            .show().setCanceledOnTouchOutside(false);
                }

                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("content",et_content.getText().toString());
                values.put("number",et_number.getText().toString());
                values.put("type",tv_type.getText().toString());
                values.put("date",tv_date.getText().toString());

                db.insert("Notepad",null,values);
                Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                finish();
                intent = new Intent(AddBalanceActivity.this,BalanceActivity.class);
                startActivity(intent);


                break;
        }
        return true;
    }
}
