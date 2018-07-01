package com.example.rsj.DJIStationView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

public class MainActivity extends Activity  {
    private Button login;
    public Boolean root;
    public Boolean admin;
    public Boolean user;
    public String name;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);


        //新页面接收数据
//        Toast.makeText(MainActivity.this,"hhhhhh",Toast.LENGTH_SHORT).show();

//        login=(Button)findViewById(R.id.login_btn11);
//        Bundle bundle = this.getIntent().getExtras();
//
//        root = bundle.getBoolean("root");
//        admin = bundle.getBoolean("admin");
//        user = bundle.getBoolean("user");
//        name=bundle.getString("name");
//        login.setOnClickListener(new View.OnClickListener()//侦听登录点击事件
//        {
//            public void onClick(View v) {//验证用户名密码是否符合要求
//                Toast.makeText(MainActivity.this,""+root+admin+user+name,Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        Toast.makeText(getApplicationContext(),"hhhhhh"+root+admin+user,Toast.LENGTH_SHORT);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
