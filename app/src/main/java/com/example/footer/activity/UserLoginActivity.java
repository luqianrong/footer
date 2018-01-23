package com.example.footer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.inter.ResponseStringDataListener;
import com.example.footer.R;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.framework.BaseActivity;
import com.example.footer.network.HttpRequestUtil;
import com.example.footer.utils.LogUtil;
import com.example.footer.utils.PreferenceHelper;
import com.hsg.sdk.common.util.ConnectionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import cn.jpush.android.api.JPushInterface;

import static android.R.attr.description;
import static android.R.attr.version;

/**
 * Created by 乃军 on 2017/11/15.
 */

public class UserLoginActivity extends BaseActivity implements View.OnClickListener, ResponseStringDataListener {
    private static final int LOGIN=0x001,BIND=0x002;
    private TextView titleBar_Right_tv;
    private TextView txt_wj;
    private EditText etuserName;
    private EditText etpassword;
    private TextView tx_login;
    private String userName;
    private String password;
    private SharedPreferences userLogin;
    private View loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        userLogin = PreferenceHelper.getUserLogin(this);
    }

    @Override
    protected void uIViewInit() throws ParseException {
        TextView tvMiddle = (TextView) findViewById(R.id.title);
        tvMiddle.setText("登录");

        ImageView imageView=(ImageView)findViewById(R.id.left_image);
        imageView.setBackgroundResource(R.drawable.titlebar_left);
        imageView.setOnClickListener(this);

        titleBar_Right_tv =(TextView)findViewById(R.id.titleBar_Right_tv);
        titleBar_Right_tv.setText("注册");
        titleBar_Right_tv.setOnClickListener(this);

        etuserName =(EditText)findViewById(R.id.etuserName);//用户名

        etpassword =(EditText)findViewById(R.id.etpassword);//密码

        txt_wj =(TextView)findViewById(R.id.txt_wj);//忘记密码
        txt_wj.setOnClickListener(this);

        tx_login =(TextView)findViewById(R.id.tx_login);//登陆
        tx_login.setOnClickListener(this);

        loading = findViewById(R.id.loading);

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
            case R.id.titleBar_Right_tv://注册
                Intent intent=new Intent(UserLoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_wj://忘记密码
                Intent intent1=new Intent(UserLoginActivity.this,FindPasswordActivity.class);
                startActivity(intent1);
                break;
            case R.id.tx_login://登陆
                login();
                break;
        }
    }

    public void login(){
        userName =etuserName.getText().toString().trim();//用户名
        password =etpassword.getText().toString().trim();//密码
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(getApplicationContext(), "请输入您的手机号!", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "请输入您的密码!", Toast.LENGTH_SHORT).show();

        } else if (!(userName.length() >= 4 && userName.length() <= 20)) {
            Toast.makeText(getApplicationContext(), "请输入4-20位的账号!", Toast.LENGTH_SHORT).show();

        } else if (!(password.length() >= 6 && password.length() <= 16)) {
            Toast.makeText(getApplicationContext(), "请输入6-16位的密码!", Toast.LENGTH_SHORT).show();

        } else {
           if(ConnectionUtil.isConnected(this)){
                loading.setVisibility(View.VISIBLE);
                HttpRequestUtil.HttpRequestByGet(Config.USER_LOGIN_URL+"?mobile="+userName+"&psw="+password, this,LOGIN);
           }
        }
    }




    @Override
    public void onDataDelivered(int taskId, String data) throws JSONException {
        switch (taskId){
            case LOGIN:
                loading.setVisibility(View.GONE);
                LogUtil.e(data.toString());
                JSONObject object1=new JSONObject(data);
                String message=object1.getString("message");
                Toast.makeText(GlobalApplication.instance,message,Toast.LENGTH_SHORT).show();

                String userId=object1.getJSONObject("data").getString("userId");
                String userPhone=object1.getJSONObject("data").getString("userPhone");
                String userIcon=object1.getJSONObject("data").getString("userIcon");
                String nickName=object1.getJSONObject("data").getString("nickName");

                String userAge=object1.getJSONObject("data").getString("userAge");
                String userWx=object1.getJSONObject("data").getString("userWx");
                String userQq=object1.getJSONObject("data").getString("userQq");
                String userMail=object1.getJSONObject("data").getString("userMail");

                String stars=object1.getJSONObject("data").getString("userPoints");
                String status_code=object1.getString("code");
                if("0".equals(status_code)){
                    //这里做用户信息的存储
                    SharedPreferences.Editor edit = userLogin.edit();
                    edit.putBoolean("login", true);
                    edit.putString("userId",userId);
                    edit.putString("userPhone",userPhone);
                    edit.putString("userIcon",userIcon);
                    edit.putString("nickName",nickName);
                    edit.putString("status_code",status_code);
                    edit.putString("userAge",userAge);
                    edit.putString("userWx",userWx);
                    edit.putString("userQq",userQq);
                    edit.putString("userMail",userMail);
                    edit.putString("userPoints",stars);
                    edit.commit();

                    Intent intent=new Intent(UserLoginActivity.this,MainActivity.class);
                    startActivity(intent);

                }
                break;
            case BIND:
                LogUtil.e(data.toString());
                JSONObject object=new JSONObject(data);
                String msg=object.getString("message");
                Toast.makeText(GlobalApplication.instance,msg,Toast.LENGTH_SHORT).show();
                Intent intentt=new Intent(this,MainActivity.class);
                startActivity(intentt);
                finish();
                break;

        }
    }

    @Override
    public void onErrorHappened(int taskId, String errorCode, String errorMessage) {
        loading.setVisibility(View.GONE);
        LogUtil.e(errorMessage);
    }

    /*
    * 当输入完毕后想隐藏软键盘时我们通过直接点击EditText的之外的其他空白处就可以隐藏软键盘
    * */
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (UserLoginActivity.this.getCurrentFocus() != null) {
                if (UserLoginActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(UserLoginActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
