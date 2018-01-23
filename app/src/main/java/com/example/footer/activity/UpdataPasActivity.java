package com.example.footer.activity;

import android.os.Bundle;
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
import com.example.footer.utils.PreferenceHelper;
import com.hsg.sdk.common.util.ConnectionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by 乃军 on 2017/11/15.
 */

public class UpdataPasActivity extends BaseActivity implements View.OnClickListener,ResponseStringDataListener{
    private static final int REQUEST_MODIFIPWD_TASK =0x00 ;
    private EditText etInputPwdNew;
    private EditText etInputPwdOld;
    private EditText etInputRePwdNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password);
    }

    @Override
    protected void uIViewInit() throws ParseException {
        TextView tvMiddle = (TextView) findViewById(R.id.title);
        tvMiddle.setText("更改密码");

        ImageView imageView=(ImageView)findViewById(R.id.left_image);
        imageView.setBackgroundResource(R.drawable.titlebar_left);
        imageView.setOnClickListener(this);

        etInputPwdOld = (EditText) findViewById(R.id.etInputPwdOld);
        etInputPwdNew = (EditText) findViewById(R.id.etInputPwdNew);
        etInputRePwdNew = (EditText) findViewById(R.id.etInputRePwdNew);
        TextView tvActionCommit = (TextView) findViewById(R.id.tvActionCommit);
        tvActionCommit.setOnClickListener(this);

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
            case R.id.tvActionCommit:
                String pwdNew = etInputPwdNew.getText().toString().trim();
                String pwdOld = etInputPwdOld.getText().toString().trim();
                String rePwdNew = etInputRePwdNew.getText().toString().trim();

                if (fiterCondition(pwdNew, pwdOld, rePwdNew)) {
                    requestPwdModify(pwdNew,pwdOld);
                }

                break;
        }
    }

    private boolean fiterCondition(String pwdNew, String pwdOld, String rePwdNew) {
        boolean flag = false;
        if (TextUtils.isEmpty(pwdOld)&&!(pwdOld.length()<=16&&pwdOld.length()>=6)) {
            Toast.makeText(getApplicationContext(), "请输入6-16位旧密码!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pwdNew)&&!(pwdNew.length()<=16&&pwdNew.length()>=6)) {
            Toast.makeText(getApplicationContext(), "请输入6-16位新密码!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(rePwdNew)) {
            Toast.makeText(getApplicationContext(), "请确认新密码!", Toast.LENGTH_SHORT).show();
        } else if (!pwdNew.equals(rePwdNew)) {
            Toast.makeText(getApplicationContext(), "两次输入密码不一致!", Toast.LENGTH_SHORT).show();
        } else {
            flag = true;
        }
        return flag;

    }

    private void requestPwdModify(String pwdNew, String pwdOld) {
        if (ConnectionUtil.isConnected(getApplicationContext())) {
            String userPhone = PreferenceHelper.getUserLogin(GlobalApplication.instance).getString("userPhone", "");
            HttpRequestUtil.HttpRequestByGet(Config.PASSWORD_MODIFY_URL+"?mobile="+userPhone+"&oPsw="+pwdOld+"&nPsw="+pwdNew,this,REQUEST_MODIFIPWD_TASK);
        } else {
            Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDataDelivered(int taskId, String data) throws JSONException {
        switch (taskId){
            case REQUEST_MODIFIPWD_TASK :
                JSONObject object=new JSONObject(data);
                String statu_code=object.getString("code");
                if("0".equals(statu_code)){
                    Toast.makeText(ApplicationController.getInstance(),"修改成功",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    public void onErrorHappened(int taskId, String errorCode, String errorMessage) {

    }
}
