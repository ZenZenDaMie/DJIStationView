package com.example.rsj.DJIStationView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Handler;

import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import java.io.IOException;

import okhttp3.Call;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    EditText username;
    EditText password;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String name1,pwd1;
    final OkHttpClient client = new OkHttpClient();
    public String geturl;
    private CheckBox rememberPass;
    HttpUtils OkhttpUtil=new HttpUtils();
//    public LoginActivity() {
//        mHandler = new Handler(){
//            @Override
//            public void handleMessage(Message msg){
//                if(msg.what==1){
//                    String qq = (String) msg.obj;
//                    Toast.makeText(getApplicationContext(),qq, Toast.LENGTH_SHORT).show();//提示用户登陆成功
//                    if(qq.equals("1")){
//    //                    Intent intent = new Intent();
//    //                    intent.setClass(LoginActivity.this, MainActivity.class);
//    //                    startActivity(intent);
//                    }
//                }
//
//            }
//        };
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test();
//        setContentView(R.layout.activity_login);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.complex_activity_main);
        login=(Button)findViewById(R.id.login_btn);
        username=(EditText)super.findViewById(R.id.login_input_username);//获取用户输入的用户名
        password=(EditText)super.findViewById(R.id.login_input_password);//获取用户密码
        rememberPass=(CheckBox)findViewById(R.id.remember_pwd);

        boolean isRemenber=pref.getBoolean("remember_password",false);
        if(isRemenber){
            //将账号和密码都设置到文本中
            String account=pref.getString("account","");
            String pwd=pref.getString("pwd","");
            username.setText(account);
            password.setText(pwd);
            rememberPass.setChecked(true);

        }

        login.setOnClickListener(new View.OnClickListener()//侦听登录点击事件
         {
             public void onClick(View v) {//验证用户名密码是否符合要求
                 name1 = username.getText().toString();
                 pwd1 = password.getText().toString();
                 //                                         Toast.makeText(getApplicationContext(),username.getText().toString()+"  "+password.getText().toString(), Toast.LENGTH_SHORT).show();//提示用户登陆成功
                 //                                         postRequest(name1,pwd1);
                 //                                         Toast.makeText(getApplicationContext(),name1+pwd1, Toast.LENGTH_SHORT).show();//提示用户登陆成功

                 String url = "http://139.196.138.204/tp5/public/user/login";
                 OkHttpUtils
                         .get()
                         .url(url)
                         .addParams("name", name1)
                         .addParams("password", pwd1)
                         .build()
                         .execute(new StringCallback() {
                             @Override
                             public void onError(Call call, Exception e) {
                                 Toast.makeText(getApplicationContext(), "连接错误，请查看网络连接", Toast.LENGTH_SHORT).show();
                                 //                        ToastUtil.show("连接错误，请查看网络连接");
                             }

                             @Override
                             public void onResponse(String _response) {
                                 Gson gson = new Gson();
                                 JsonBean log_respon = gson.fromJson(_response, JsonBean.class);
                                 //                                                         Log.v("Tag",b.getSuccess());
                                 if (log_respon.isSuccess()) {//用户登录成功
                                     editor = pref.edit();
                                     if (rememberPass.isChecked()) {
                                         editor.putBoolean("remember_password", true);
                                         editor.putString("account", name1);
                                         editor.putString("pwd", pwd1);
                                     } else {
                                         editor.clear();
                                     }
                                     editor.apply();


                                     Intent intent = new Intent(LoginActivity.this, LocationDemo.class);
                                     //用Bundle携带数据
                                     Bundle bundle = new Bundle();
                                     //传递name参数为tinyphp
                                     bundle.putBoolean("root", log_respon.isRoot());
                                     bundle.putBoolean("admin", log_respon.isAdmin());
                                     bundle.putBoolean("user", log_respon.isUser());
                                     bundle.putString("name", name1);
                                     intent.putExtras(bundle);

                                     startActivity(intent);

                                 } else {
                                     Toast.makeText(getApplicationContext(), "账号、密码错误，登录失败", Toast.LENGTH_SHORT).show();//提示用户登录失败
                                 }
                                 //                                                         Toast.makeText(getApplicationContext(),""+b.isSuccess(), Toast.LENGTH_SHORT).show();//提示用户登陆成功

                                 //                                                         Toast.makeText(getApplicationContext(),_response, Toast.LENGTH_SHORT).show();//提示用户登陆成功
                             }
                         });
             }
         }
        );
    }

    /**
     * post请求后台
     * @param name
     * @param pwd
     */
    private void postRequest(String name,String pwd)  {
        //建立请求表单，添加上传服务器的参数

        RequestBody formBody = new FormEncodingBuilder()
                .add("name",name)
                .add("password",pwd)
                .build();
        //发起请求
        geturl="http://139.196.138.204/tp5/public/user/login?name="+name+"&password="+pwd;
        final Request request = new Request.Builder()
                .url(geturl)
                .build();
        //新建一个线程，用于得到服务器响应的参数
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    //回调
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        //将服务器响应的参数response.body().string())发送到hanlder中，并更新ui
//                        mHandler.obtainMessage(1, response.body().string()).sendToTarget();
                        mHandler.obtainMessage(1, response.body().string()).sendToTarget();
                        //                        Log.e("OKHttp",response.body().string());
                    } else {
                        throw new IOException("Unexpected code:" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }



    private Handler mHandler;
    //动态申请权限的测试方法
    public void test() {
        // 要申请的权限 数组 可以同时申请多个权限
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION};

        if (Build.VERSION.SDK_INT >= 23) {
            //如果超过6.0才需要动态权限，否则不需要动态权限
            //如果同时申请多个权限，可以for循环遍历
            int check = ContextCompat.checkSelfPermission(this,permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check == PackageManager.PERMISSION_GRANTED) {
                //写入你需要权限才能使用的方法
            } else {
                //手动去请求用户打开权限(可以在数组中添加多个权限) 1 为请求码 一般设置为final静态变量
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        } else {
            //写入你需要权限才能使用的方法
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //回调，判断用户到底点击是还是否。
        //如果同时申请多个权限，可以for循环遍历
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //写入你需要权限才能使用的方法
        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this,"需要获得XXX权限",Toast.LENGTH_SHORT).show();
        }
    }
}