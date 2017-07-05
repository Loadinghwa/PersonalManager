package com.zucc.ldh1135.secretary;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.zucc.ldh1135.secretary.DateManager.AddDateActivity;
import com.zucc.ldh1135.secretary.DateManager.Fragment_Affairs;
import com.zucc.ldh1135.secretary.DateManager.Fragment_All;
import com.zucc.ldh1135.secretary.DateManager.Fragment_Plan;
import com.zucc.ldh1135.secretary.DateManager.Fragment_Work;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private NavigationView navView;
    private Database database;
    private FloatingActionButton fab_add;
    private int database_version = 1;

    private TabLayout tabs;
    private ViewPager viewPager;
    private List<String> mTitle = new ArrayList<>();
    private List<Fragment> mFragment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager(), mTitle, mFragment);
        viewPager.setOffscreenPageLimit(4);     //禁止切换页面时重新加载

        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        tabs.setTabsFromPagerAdapter(adapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_menu);
        }
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch(item.getItemId()){
                    case R.id.nav_scan:
                        intent = new Intent(MainActivity.this,ScanActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_note:
                        intent = new Intent(MainActivity.this,BalanceActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_alarm:
                        intent = new Intent(MainActivity.this,AlarmActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_settings:
                        intent = new Intent(MainActivity.this,SettingsActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        //数据库初始化
        database = new Database(this,"Database.db",null,database_version);
        database.getWritableDatabase();

        fab_add = (FloatingActionButton) findViewById(R.id.fab);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddDateActivity.class);
                startActivity(intent);
            }
        });
    }

    //更改背景
    public void changeBackground(View v){
        new BottomSheet.Builder(MainActivity.this).title("title").sheet(R.menu.bottomsheet_menu).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.bottomsheet_image:
                        //Toast.makeText(getApplicationContext(),"success!",Toast.LENGTH_SHORT).show();

                        break;
                }
            }
        }).show();
    }

    private void initView() {

        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mTitle.add("全部");
        mTitle.add("计划");
        mTitle.add("工作");
        mTitle.add("事务");

        mFragment.add(new Fragment_All());
        mFragment.add(new Fragment_Plan());
        mFragment.add(new Fragment_Work());
        mFragment.add(new Fragment_Affairs());
    }

    private class MyAdapter extends FragmentPagerAdapter {
        private List<String> title;
        private List<Fragment> views;

        private MyAdapter(FragmentManager fm, List<String> title, List<Fragment> views) {
            super(fm);
            this.title = title;
            this.views = views;
        }

        @Override
        public Fragment getItem(int position) {
            return views.get(position);
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //toolbar

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.toolbar_search:

                break;
            default:
        }
        return true;
    }

}
