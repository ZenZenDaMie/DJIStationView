package com.example.rsj.DJIStationView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * Created by rsj on 2018/1/7.
 */

public class HttpUtils {
    public static String url = "http://139.196.138.204/tp5/public/user/login";  //不可少，是你的请求地址
    public static MiMiLoginListener miLoginListener;                    //登录

    public  interface MiMiLoginListener{
        public  void onLoginResult(int ret,Object msg);
    }



    public void onLoginResult(int ret,Object msg){
        if(miLoginListener != null ){
            miLoginListener.onLoginResult(ret,msg);
        }
        miLoginListener = null;
    }
    public static void postLogin(String phone_num,String passWord,final MiMiLoginListener listener){
        miLoginListener = listener;
        url=url+phone_num+"&password="+passWord;
        OkHttpUtils
                .get()
                .url(url)
                .addParams("name", phone_num)
                .addParams("password",passWord)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
//                        Toast.makeText(LoginActivity.this,"连接错误，请查看网络连接", Toast.LENGTH_SHORT).show();
//                        ToastUtil.show("连接错误，请查看网络连接");
                    }

                    @Override
                    public void onResponse(String _response) {
                        listener.onLoginResult(1,_response);
                    }
                });
    }
}