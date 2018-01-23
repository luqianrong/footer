package com.example.footer.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
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
import com.example.footer.utils.LogUtil;
import com.hsg.sdk.common.util.ConnectionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by 乃军 on 2017/11/15.
 */

public class FindPasswordActivity extends BaseActivity implements View.OnClickListener,ResponseStringDataListener{
    private static final int TEL_CODE=0x001;
    private static final int FORGET=0x002;
    private static final int SENDING = -9;
    private static final int RESEND = -8;
    //成功
    private static final int SUCCESS = 0;
    //加载
    private static final int LOADING = 1;
    private EditText et_tel;
    private EditText et_code;
    private EditText et_pass;
    private TextView tx_code;
    private String phone;
    private TextView tx_reset;
    private String code;
    private String password;
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
        setContentView(R.layout.find_password);
    }

    @Override
    protected void uIViewInit() throws ParseException {
        TextView tvMiddle = (TextView) findViewById(R.id.title);
        tvMiddle.setText("找回密码");

        ImageView imageView=(ImageView)findViewById(R.id.left_image);
        imageView.setBackgroundResource(R.drawable.titlebar_left);
        imageView.setOnClickListener(this);

        loadingView = findViewById(R.id.loading);

        et_tel =(EditText)findViewById(R.id.et_tel);//电话号码


        tx_code =(TextView)findViewById(R.id.tx_code);
        tx_code.setOnClickListener(this);


        et_code =(EditText)findViewById(R.id.et_code);//动态码
        et_pass =(EditText)findViewById(R.id.et_pass);//密码

        tx_reset =(TextView)findViewById(R.id.tx_reset);//重置密码
        tx_reset.setOnClickListener(this);
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
            case R.id.tx_reset:
                resert();//注册
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
                HttpRequestUtil.HttpRequestByGet(Config.DUANXIN_YANZHENGMA+"?mobile="+ phone, this,TEL_CODE);

            } else {
                Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
            }
        }else {
            AddLengthFilter.CustomTimeToast(GlobalApplication.instance,"请填写完整11位数手机号");
            //Toast.makeText(this,"请填写完整11位数手机号",Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * 重置密码
    * */
    public void resert(){
        code =et_code.getText().toString().trim();
        password =et_pass.getText().toString().trim();

        if(TextUtils.isEmpty(phone)){
            AddLengthFilter.CustomTimeToast(GlobalApplication.instance,"请输入手机号码");
            //Toast.makeText(GlobalApplication.instance,"请输入手机号码",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(code)){
            AddLengthFilter.CustomTimeToast(GlobalApplication.instance,"请输入动态码");
            //Toast.makeText(GlobalApplication.instance,"请输入动态码",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            AddLengthFilter.CustomTimeToast(GlobalApplication.instance,"请输入密码");
            //Toast.makeText(GlobalApplication.instance,"请输入密码",Toast.LENGTH_SHORT).show();
        }

        reset_net(phone, code, password);
    }

    /*
   * 忘记密码的网络请求
   *
   * */
    public void reset_net(String mobile,String code,String password){
        if (ConnectionUtil.isConnected(this)) {
            setLoadingViewStatus(LOADING);
            HttpRequestUtil.HttpRequestByGet(Config.FORGET_PASS_URL+"?mobile="+mobile+"&code="+code+"&nPsw="+password, this,FORGET);

        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDataDelivered(int taskId, String data) throws JSONException {
        switch (taskId){
            case TEL_CODE:
                LogUtil.e(data.toString());
                JSONObject object=new JSONObject(data);
                String statu_code=object.getString("code");
                String  message=object.getString("message");
                if("0".equals(statu_code)){
                    AddLengthFilter.CustomTimeToast(GlobalApplication.instance,message);
                    //Toast.makeText(ApplicationController.getInstance(),message,Toast.LENGTH_SHORT).show();
                }
                break;
            case FORGET:
                setLoadingViewStatus(SUCCESS);
                LogUtil.e(data.toString());
                JSONObject object1=new JSONObject(data);
                String statu_code1=object1.getString("code");
                String msg=object1.getString("message");
                if("0".equals(statu_code1)){
                    AddLengthFilter.CustomTimeToast(GlobalApplication.instance,msg);
                    //Toast.makeText(ApplicationController.getInstance(),msg,Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    @Override
    public void onErrorHappened(int taskId, String errorCode, String errorMessage) {

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
