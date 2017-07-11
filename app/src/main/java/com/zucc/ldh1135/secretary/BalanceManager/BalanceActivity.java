package com.zucc.ldh1135.secretary.BalanceManager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zucc.ldh1135.secretary.DataBase.Database;
import com.zucc.ldh1135.secretary.DateManager.AddDateActivity;
import com.zucc.ldh1135.secretary.DateManager.DetailDateActivity;
import com.zucc.ldh1135.secretary.R;
import com.zucc.ldh1135.secretary.Util.CurrentTime;

import java.util.ArrayList;
import java.util.List;

public class BalanceActivity extends AppCompatActivity {
    private Toolbar toolbar;

    FloatingActionButton fab;

    private int database_version = 1;
    private Database dbHelper;
    Intent intent;

    int id;
    String content,bDate,number,type;

    private List<Balance> balanceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(BalanceActivity.this, AddBalanceActivity.class);
                finish();
                startActivity(intent);
            }
        });

        balanceList.clear();

        dbHelper = new Database(this,"Database.db",null,database_version);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Notepad",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{

                id = cursor.getInt(cursor.getColumnIndex("id"));
                content = cursor.getString(cursor.getColumnIndex("content"));
                bDate = cursor.getString(cursor.getColumnIndex("date"));
                number = cursor.getString(cursor.getColumnIndex("number"));
                type = cursor.getString(cursor.getColumnIndex("type"));

                balanceList.add(new Balance(id,content,number,type,bDate));

            }while(cursor.moveToNext());
        }

        cursor.close();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_balance);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        BalanceAdapter balanceAdapter = new BalanceAdapter(balanceList);
        recyclerView.setAdapter(balanceAdapter);


    }

    public class Balance{
        int id;
        String content;
        String date;
        String number;
        String type;

        public Balance(int id,String content,String number,String type,String date)
        {
            this.id = id;
            this.content = content;
            this.number = number;
            this.type = type;
            this.date = date;
        }

        public String getContent() {
            return content;
        }

        public String getDate() {
            return date;
        }

        public String getNumber() {
            return number;
        }

        public String getType() {
            return type;
        }

        public int getId() {
            return id;
        }
    }

    public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.ViewHolder>{

        private Context mContext;

        private List<Balance> mBalanceList;

        public class ViewHolder extends RecyclerView.ViewHolder{
            CardView cardView;
            View dateView;

            TextView tv_content;
            TextView tv_number;
            TextView tv_date;

            public ViewHolder(View view)
            {
                super(view);
                cardView = (CardView) view;
                dateView = view;

                tv_content = (TextView) view.findViewById(R.id.tv_content);
                tv_number = (TextView) view.findViewById(R.id.tv_number);
                tv_date = (TextView) view.findViewById(R.id.tv_date);

            }

        }

        public BalanceAdapter(List<Balance> balanceList)
        {
            mBalanceList = balanceList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_balance,parent,false);
            final ViewHolder holder = new ViewHolder(view);
            holder.dateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    intent = new Intent(BalanceActivity.this, DetailBalanceActivity.class);
                    intent.putExtra("id",mBalanceList.get(position).getId());
                    intent.putExtra("content",mBalanceList.get(position).getContent());
                    intent.putExtra("number",mBalanceList.get(position).getNumber());
                    intent.putExtra("date",mBalanceList.get(position).getDate());
                    intent.putExtra("type",mBalanceList.get(position).getType());
                    finish();
                    startActivity(intent);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Balance balance = mBalanceList.get(position);
            holder.tv_content.setText(balance.getContent());
            if(balance.getType().equals("收入"))
            {
                holder.tv_number.setText("+" + balance.getNumber());
            }
            else
            {
                holder.tv_number.setText("-" + balance.getNumber());
            }
            holder.tv_date.setText(balance.getDate());
        }

        @Override
        public int getItemCount() {
            return mBalanceList.size();
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
