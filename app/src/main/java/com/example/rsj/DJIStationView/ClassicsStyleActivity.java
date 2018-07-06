package com.example.rsj.DJIStationView;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import static android.R.layout.simple_list_item_2;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import okhttp3.Call;


/**
 * Created by rsj on 2018/1/22.
 */

public class ClassicsStyleActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public String stationid;


    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private static boolean isFirstEnter = true;

    public Gson gson = new Gson();
    public List<LogBean> lists;
    private ClassicsHeader mClassicsHeader;
    private Drawable mDrawableProgress;
    public BaseRecyclerAdapter<LogBean> mAdpater;
    public String title1;
    public String content1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_classics);
        int i=0;
        Bundle bundle = this.getIntent().getExtras();
        stationid=bundle.getString("stationid");
        Toast.makeText(getApplicationContext(),stationid, Toast.LENGTH_SHORT).show();
//        for(Item e:Item.values())
//        {
//            Toast.makeText(getApplicationContext(),e.name(), Toast.LENGTH_SHORT).show();
//        }

        request();
//        Toast.makeText(getApplicationContext(),mAdpater, Toast.LENGTH_SHORT).show();

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        Item.("hh");
        mRefreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();//提示用户登录失败
                request();
                RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
                refreshlayout.finishRefresh(50/*,false*/);//传入false表示刷新失败
            }
        });
        int deta = new Random().nextInt(7 * 24 * 60 * 60 * 1000);
        mClassicsHeader = (ClassicsHeader)mRefreshLayout.getRefreshHeader();
        mClassicsHeader.setLastUpdateTime(new Date(System.currentTimeMillis()-deta));
        mClassicsHeader.setTimeFormat(new SimpleDateFormat("更新于 MM-dd HH:mm", Locale.CHINA));
//        mClassicsHeader.setTimeFormat(new DynamicTimeFormat("更新于 %s"));

        mDrawableProgress = mClassicsHeader.getProgressView().getDrawable();
        if (mDrawableProgress instanceof LayerDrawable) {
            mDrawableProgress = ((LayerDrawable) mDrawableProgress).getDrawable(0);
        }



        if (isFirstEnter) {
            isFirstEnter = false;
            //触发自动刷新
            mRefreshLayout.autoRefresh();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    private void request(){
        String url = "http://139.196.138.204/tp5/public/station/readlog";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("stationid", stationid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(getApplicationContext(), "连接错误，请查看网络连接", Toast.LENGTH_SHORT).show();
                        //                        ToastUtil.show("连接错误，请查看网络连接");
                    }

                    @Override
                    public void onResponse(String _response) {
                        //                        Toast.makeText(getApplicationContext(), _response, Toast.LENGTH_SHORT).show();
//                        Gson gson = new Gson();

                        List<LogBean> lists=gson.fromJson(_response,new   TypeToken<List<LogBean>>(){}.getType());
                        //                        System.out.println("反序列之后");
                        for(LogBean pp:lists){

//                            Toast.makeText(getApplicationContext(), pp.getCreate_time(), Toast.LENGTH_SHORT).show();
                            //设置坐标点

                        }
                        View view = findViewById(R.id.recyclerView);
                        if (view instanceof RecyclerView) {
                            RecyclerView recyclerView = (RecyclerView) view;
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), VERTICAL));
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
//                            recyclerView.setAdapter();
                            recyclerView.setAdapter(mAdpater = new BaseRecyclerAdapter<LogBean>(lists, simple_list_item_2,ClassicsStyleActivity.this) {
                                @Override
                                protected void onBindViewHolder(SmartViewHolder holder, LogBean model, int position) {
                                    if(model.getStationstatus()==0)
                                    {
                                        title1="无人机飞离";
                                    }
                                    else if(model.getStationstatus()==1){
                                        title1="无人机"+model.getUavid()+" 开始充电";
                                    }
                                    else if(model.getStationstatus()==2){
                                        title1="无人机"+model.getUavid()+" 充电完成";
                                    }
                                    holder.text(android.R.id.text1, title1);
                                    holder.text(android.R.id.text2, "                                                             "+model.getUpdate_time());
                                    //                    holder.textColorId(android.R.id.text2, R.color.colorTextAssistant);
                                }
                            });
                            mRecyclerView = recyclerView;
                        }
                    }
                });
    }

}