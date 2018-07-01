package com.example.rsj.DJIStationView;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 */
public class LocationDemo extends Activity implements SensorEventListener {
    //参数相关
    private Button login;
    public Boolean root;
    public Boolean admin;
    public Boolean user;
    public String name;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    public List<OverlayOptions> options;
    MapView mMapView;
    BaiduMap mBaiduMap;

    // UI相关
    OnCheckedChangeListener radioButtonListener;
    Button requestLocButton;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private float direction;
    public BitmapDescriptor bdA;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
        bdA = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_markx);
        setContentView(R.layout.activity_location);
        requestLocButton = (Button) findViewById(R.id.button1);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = LocationMode.NORMAL;

        Bundle bundle = this.getIntent().getExtras();

        root = bundle.getBoolean("root");
        admin = bundle.getBoolean("admin");
        user = bundle.getBoolean("user");
        name=bundle.getString("name");

//        Toast.makeText(LocationDemo.this,name,Toast.LENGTH_LONG).show();

        request();

        requestLocButton.setText("普通");
        OnClickListener btnClickListener = new OnClickListener() {
            public void onClick(View v) {
                switch (mCurrentMode) {
                    case NORMAL:
                        requestLocButton.setText("跟随");
                        mCurrentMode = LocationMode.FOLLOWING;
                        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                        MapStatus.Builder builder = new MapStatus.Builder();
                        builder.overlook(0);
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                        break;
                    case COMPASS:
                        requestLocButton.setText("普通");
                        mCurrentMode = LocationMode.NORMAL;
                        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                        MapStatus.Builder builder1 = new MapStatus.Builder();
                        builder1.overlook(0);
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));
                        break;
                    case FOLLOWING:
                        requestLocButton.setText("罗盘");
                        mCurrentMode = LocationMode.COMPASS;
                        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                        break;
                    default:
                        break;
                }
            }
        };
        requestLocButton.setOnClickListener(btnClickListener);

//        RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
//        radioButtonListener = new OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == R.id.defaulticon) {
//                    // 传入null则，恢复默认图标
//                    mCurrentMarker = null;
//                    mBaiduMap
//                            .setMyLocationConfigeration(new MyLocationConfiguration(
//                                    mCurrentMode, true, null));
//                }
//                if (checkedId == R.id.customicon) {
//                    // 修改为自定义marker
//                    mCurrentMarker = BitmapDescriptorFactory
//                            .fromResource(R.mipmap.icon_geo);
//                    mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
//                            mCurrentMode, true, mCurrentMarker,
//                            accuracyCircleFillColor, accuracyCircleStrokeColor));
//                }
//            }
//        };
//        group.setOnCheckedChangeListener(radioButtonListener);

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView1);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        //mLocationClient为第二步初始化过的LocationClient对象
        //调用LocationClient的start()方法，便可发起定位请求
        //创建OverlayOptions的集合

        options = new ArrayList<OverlayOptions>();
//        //设置坐标点
//
//        LatLng point1 = new LatLng(31.32352, 121.405438);
//        LatLng point2 = new LatLng(31.323833, 121.404225);
//
//        //创建OverlayOptions属性
//
//        OverlayOptions option1 =  new MarkerOptions()
//                .position(point1)
//                .icon(bdA);
//        OverlayOptions option2 =  new MarkerOptions()
//                .position(point2)
//                .icon(bdA);
//        //将OverlayOptions添加到list
//        options.add(option1);
//        options.add(option2);
//        //在地图上批量添加
//        mBaiduMap.addOverlays(options);
        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
//                Toast.makeText(getApplicationContext(),""+marker.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LocationDemo.this, TestActivity.class);
//                Toast.makeText(getApplicationContext(),""+marker.getPosition().longitude,Toast.LENGTH_LONG).show();
                //用Bundle携带数据
                Bundle bundle = new Bundle();
                //传递name参数为tinyphp
                bundle.putBoolean("root", root);
                bundle.putBoolean("admin", admin);
                bundle.putBoolean("user", user);
                bundle.putString("name", name);
                bundle.putString("stationid",""+marker.getTitle());
                bundle.putString("longitude",""+marker.getPosition().longitude);
                bundle.putString("latitude",""+marker.getPosition().latitude);
                intent.putExtras(bundle);

                startActivity(intent);

                return true;
            }
        });

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
//            Toast.makeText(LocationDemo.this,"lat："+mCurrentLat+"lon"+mCurrentLon,Toast.LENGTH_LONG).show();

            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    protected void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    private void request(){
        String url = "http://139.196.138.204/tp5/public/station/returngps";
        OkHttpUtils
                .get()
                .url(url)
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
                        Gson gson = new Gson();
                        List<LocationBean> lists=gson.fromJson(_response,new   TypeToken<List<LocationBean>>(){}.getType());
//                        System.out.println("反序列之后");
                        for(LocationBean pp:lists){

//                            Toast.makeText(getApplicationContext(), pp.getStationid(), Toast.LENGTH_SHORT).show();
                            //设置坐标点

                            LatLng point1 = new LatLng(pp.getLon(), pp.getLat());

                            //创建OverlayOptions属性

                            OverlayOptions option1 =  new MarkerOptions()
                                    .position(point1)
                                    .icon(bdA)
                                    .title(pp.getStationid());

                            //将OverlayOptions添加到list
                            options.add(option1);
                            //在地图上批量添加
                            mBaiduMap.addOverlays(options);
                        }
                        //                        Iterator it=list.iterator();
//                        while(it.hasNext())
//                        {
//                            LocationBean lb=(LocationBean) it.next();
//                            Toast.makeText(getApplicationContext(), lb.getStationid(), Toast.LENGTH_SHORT).show();
//
//
//                        }

//                        Toast.makeText(getApplicationContext(), log_respon.getStationid(), Toast.LENGTH_SHORT).show();
                        //                        if(log_respon.getStationid()!="0")
//                        {
//                        }
                    }
                });
    }
//    public static <T> List<T> parseModelList(String jsonStr, com.google.gson.reflect.TypeToken<List<T>> LocationBean) {
//        Gson gson = new Gson();
//        List<T> objList = null;
//        if (gson != null) {
//            objList = gson.fromJson(jsonStr, LocationBean.getType());
//        }
//        return objList;
//    }
}