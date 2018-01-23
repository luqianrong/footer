package com.example.footer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.inter.ResponseStringDataListener;
import com.example.footer.R;
import com.example.footer.application.ApplicationController;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.framework.BaseActivity;
import com.example.footer.network.HttpRequestUtil;
import com.example.footer.utils.AddLengthFilter;
import com.example.footer.utils.Contants;
import com.example.footer.utils.LogUtil;
import com.example.footer.utils.PreferenceHelper;
import com.hsg.sdk.common.util.ConnectionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by 乃军 on 2017/11/15.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener,ResponseStringDataListener {

    private static final int TEL_CODE=0x001;
    private static final int REGISTER=0x002;
    private static final int LOGIN=0x003;
    private static final int SENDING = -9;
    private static final int RESEND = -8;
    //成功
    private static final int SUCCESS = 0;
    //加载
    private static final int LOADING = 1;
    private TextView titleBar_right_tv;
    private TextView tvNext;
    private EditText et_tel;
    private TextView tx_code;
    private String phone;
    private EditText et_code;
    private EditText et_pass;
    private EditText et_confirm;
    private SharedPreferences userLogin;
    private String code;
    private String password;
    private String confirm;
    //加载页面
    private View loadingView;
    private int i = 60;//倒计时

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SENDING) {
                tx_code.setText("重新发送（" + i + "）");
            } else if (msg.what == RESEND) {
                tx_code.setText("获取动态码");
                tx_code.setEnabled(true);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_view);
        userLogin = PreferenceHelper.getUserLogin(this);
    }

    @Override
    protected void uIViewInit() throws ParseException {
        TextView tvMiddle = (TextView) findViewById(R.id.title);
        tvMiddle.setText("注册");

        ImageView imageView=(ImageView)findViewById(R.id.left_image);
        imageView.setBackgroundResource(R.drawable.titlebar_left);
        imageView.setOnClickListener(this);


       /* titleBar_right_tv =(TextView)findViewById(R.id.titleBar_Right_tv);
        titleBar_right_tv.setText("登录");*/
       /* titleBar_right_tv.setOnClickListener(this);*/

        loadingView = findViewById(R.id.loading);

        et_tel =(EditText)findViewById(R.id.et_tel);//电话号码

        tx_code =(TextView)findViewById(R.id.tx_code);
        tx_code.setOnClickListener(this);

        et_code =(EditText)findViewById(R.id.et_code);//动态码

        et_pass =(EditText)findViewById(R.id.et_pass);//密码

        et_confirm =(EditText)findViewById(R.id.et_confirm);//重复密码

        tvNext =(TextView)findViewById(R.id.tvNext);//注册
        tvNext.setOnClickListener(this);

    }



    @Override
    protected void uIViewDataApply() {

    }

    @Override
    protected void activityCreate() {

    }

    @Override
    protected void activityDestroy() {

    }

    @Override
    protected void onWindowHasFocus(boolean hasFocus) {

    }

    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.left_image:
                 finish();
                 break;
             case R.id.tx_code://获取动态码
                 getSms();
                 break;
             case R.id.tvNext:
                 register();//注册
                 break;


         }
    }
    /*
    * 获取动态码
    * */
    private void getSms(){
        phone = et_tel.getText().toString().trim();

        //短信验证码发送
        if (!TextUtils.isEmpty(phone)&& phone.length()==11) {
            tx_code.setEnabled(false);
            tx_code.setText("重新发送（" + i + "）");
            new Thread() {
                @Override
                public void run() {
                    for (; i > 0; i--) {
                        handler.sendEmptyMessage(SENDING);
                        if (i <= 0)
                            break;
                        SystemClock.sleep(999);
                    }
                    handler.sendEmptyMessage(RESEND);
                }
            }.start();
            if (ConnectionUtil.isConnected(this)) {
                HttpRequestUtil.HttpRequestByGet(Config.REGISTER_YANZHENGMA+"?mobile="+phone, this,TEL_CODE);

            } else {
                Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
            }
        }else {
            AddLengthFilter.CustomTimeToast(GlobalApplication.instance,"请填写完整11位数手机号");
            //Toast.makeText(this,"请填写完整11位数手机号",Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * 注册的网络请求
    *
    * */
    public void register_net(String mobile,String code,String confirm){
        if (ConnectionUtil.isConnected(this)) {
            setLoadingViewStatus(LOADING);
            HttpRequestUtil.HttpRequestByGet(Config.REGISTER_URL+"?mobile="+mobile+"&code="+code+"&psw="+confirm, this,REGISTER);

        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }
    }

    private void register() {
        code =et_code.getText().toString().trim();
        password =et_pass.getText().toString().trim();
        confirm =et_confirm.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            AddLengthFilter.CustomTimeToast(GlobalApplication.instance,"请输入手机号码");
            //Toast.makeText(GlobalApplication.instance,"请输入手机号码",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(code)){
            AddLengthFilter.CustomTimeToast(GlobalApplication.instance,"请输入动态码");
            //Toast.makeText(GlobalApplication.instance,"请输入动态码",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)&&!(password.length()<=16&&password.length()>=6)) {
            AddLengthFilter.CustomTimeToast(GlobalApplication.instance,"请输入6-16位密码!");
            //Toast.makeText(getApplicationContext(), "请输入6-16位密码!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirm)&&!(confirm.length()<=16&&confirm.length()>=6)) {
            AddLengthFilter.CustomTimeToast(GlobalApplication.instance,"请重复输入6-16位新密码!");
            //Toast.makeText(getApplicationContext(), "请重复输入6-16位新密码!", Toast.LENGTH_SHORT).show();
        }else if(!confirm.equals(password)){
            AddLengthFilter.CustomTimeToast(GlobalApplication.instance,"两次输入密码不一致!");
            //Toast.makeText(getApplicationContext(), "两次输入密码不一致!", Toast.LENGTH_SHORT).show();
        }
        register_net(phone, code, confirm);
    }


    @Override
    public void onDataDelivered(int taskId, String data) throws JSONException {

        switch (taskId){
            case TEL_CODE:
                JSONObject object=new JSONObject(data);
                String statu_code=object.getString("code");
                String message=object.getString("message");
                if("0".equals(statu_code)){
                    AddLengthFilter.CustomTimeToast(GlobalApplication.instance,message);
                    //Toast.makeText(ApplicationController.getInstance(),message,Toast.LENGTH_SHORT).show();
                }else if("1".equals(statu_code)){
                    AddLengthFilter.CustomTimeToast(GlobalApplication.instance,message);
                    //Toast.makeText(ApplicationController.getInstance(),message,Toast.LENGTH_SHORT).show();
                    userLoginMethod();
                }
                break;
            case REGISTER:
                setLoadingViewStatus(SUCCESS);
                LogUtil.e(data.toString());
                JSONObject object1=new JSONObject(data);
                String status_code=object1.getString("code");
                String msg=object1.getString("message");
                AddLengthFilter.CustomTimeToast(GlobalApplication.instance,msg);
                //Toast.makeText(ApplicationController.getInstance(),msg,Toast.LENGTH_SHORT).show();
                if("0".equals(status_code)){
                    SharedPreferences.Editor edit = userLogin.edit();
                    edit.putBoolean("login", true);
                    edit.putString("userPhone", phone);
                    edit.commit();
                    userLoginMethod();
                }
                break;
            case R.id.titleBar_Right_tv:
                userLoginMethod();
                break;


        }

    }

    @Override
    public void onErrorHappened(int taskId, String errorCode, String errorMessage) {
         LogUtil.e("错误"+errorMessage);
    }

    public void userLoginMethod(){

        Intent intent=new Intent(RegisterActivity.this,UserLoginActivity.class);
        startActivity(intent);
    }

    private void setLoadingViewStatus(int status) {
        switch (status) {
            case SUCCESS:
                loadingView.setVisibility(View.GONE);
                break;
            case LOADING:
                loadingView.setVisibility(View.VISIBLE);
                break;
        }
    }

}
