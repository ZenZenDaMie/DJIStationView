package com.example.rsj.DJIStationView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
/**
 * Created by rsj on 2018/1/14.
 */

public class RefreshActivity  extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_refresh);
        final TextView tv=(TextView) findViewById(R.id.text1);
        tv.setText("现在时间---请看你的手机！！");
        RefreshLayout refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Toast.makeText(RefreshActivity.this, "连接错误，请查看网络连接", Toast.LENGTH_SHORT).show();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                Toast.makeText(RefreshActivity.this, "111111", Toast.LENGTH_SHORT).show();
                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
            }
        });

    }
}
