package com.zucc.ldh1135.secretary.DateManager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zucc.ldh1135.secretary.DataBase.Database;
import com.zucc.ldh1135.secretary.R;

import java.util.ArrayList;
import java.util.List;


public class SearchDateActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SearchView searchView;
    private ListView listView;

    private List<Event> eventList = new ArrayList<>();

    String[] dataList = new String[1000];
    int length;

    String title;
    int id;
    int i;

    private int database_version = 1;
    private Database dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_date);

        searchView = (SearchView) findViewById(R.id.searchView);
        listView = (ListView) findViewById(R.id.listView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }


        dbHelper = new Database(SearchDateActivity.this,"Database.db",null,database_version);
        /*
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Date",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"));
                title = cursor.getString(cursor.getColumnIndex("title"));

                eventList.add(new Event(id,title));

            }while(cursor.moveToNext());
        }
        cursor.close();
        */

        EventAdapter eventAdapter = new EventAdapter(SearchDateActivity.this,R.layout.listview_date_search,eventList);
        listView.setTextFilterEnabled(true);
        listView.setAdapter(eventAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = String.valueOf(eventList.get(position).getId());
                Intent intent = new Intent(SearchDateActivity.this,DetailDateActivity.class);
                intent.putExtra("id",str);
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {

                if (!TextUtils.isEmpty(newText)){
                    //listView.setFilterText(newText);
                    SQLiteDatabase newDb = dbHelper.getWritableDatabase();
                    Cursor newCursor = newDb.query("Date",new String[]{"id","title"},"title like '%" + newText + "%'",null,null,null,null);
                    eventList.clear();
                    if(newCursor.moveToFirst()){
                        do{
                            id = newCursor.getInt(newCursor.getColumnIndex("id"));
                            title = newCursor.getString(newCursor.getColumnIndex("title"));

                            eventList.add(new Event(id,title));

                        }while(newCursor.moveToNext());
                    }
                    newCursor.close();

                }
                else
                {
                    listView.clearTextFilter();
                    eventList.clear();
                }
                return false;
            }

        });
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

    public class Event{
        int id;
        String title;

        public Event(int id,String title)
        {
            this.id = id;
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }

    public class EventAdapter extends ArrayAdapter<Event> {
        private int resourceId;

        public EventAdapter(Context context, int textViewResourceId, List<Event> objects)
        {
            super(context,textViewResourceId,objects);
            resourceId = textViewResourceId;
        }

        @Override
        public View getView(int position,View convertView,ViewGroup parent)
        {
            Event event = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            TextView textView = (TextView) view.findViewById(R.id.tv_title);

            textView.setText(event.getTitle());

            return view;
        }

    }

}
