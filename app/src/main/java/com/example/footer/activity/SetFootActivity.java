package com.example.footer.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
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
import com.example.footer.utils.NameLengthFilter;
import com.example.footer.utils.PreferenceHelper;
import com.example.footer.views.EaseSwitchButton;
import com.hsg.sdk.common.util.ConnectionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 乃军 on 2017/11/14.
 */

public class SetFootActivity extends BaseActivity implements View.OnClickListener,ResponseStringDataListener{
    private static final int SETTING=0x001,PERSON=0x002;

    private EaseSwitchButton mEaseSwitchButton2;
    String type ="0";
    private EditText et_1;
    private EditText et_2;
    private EditText et_3;
    private TextView txt_true;
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setfoot_view);
        getPerson();
    }

    @Override
    protected void uIViewInit() throws ParseException {
        TextView tvMiddle = (TextView) findViewById(R.id.title);
        tvMiddle.setText("足迹设置");

        ImageView imageView=(ImageView)findViewById(R.id.left_image);
        imageView.setBackgroundResource(R.drawable.titlebar_left);
        imageView.setOnClickListener(this);

        mEaseSwitchButton2 = (EaseSwitchButton) findViewById(R.id.two);
        mEaseSwitchButton2.closeSwitch();
        mEaseSwitchButton2.setOnClickListener(this);

        et_1 =(EditText)findViewById(R.id.et_1);
        et_2 =(EditText)findViewById(R.id.et_2);
        et_3 =(EditText)findViewById(R.id.et_3);

        txt_true =(TextView)findViewById(R.id.txt_true);
        txt_true.setOnClickListener(this);
        NameLengthFilter nameLengthFilter=new NameLengthFilter(10);
        et_1.setFilters(new InputFilter[]{nameLengthFilter});
        et_2.setFilters(new InputFilter[]{nameLengthFilter});
        et_3.setFilters(new InputFilter[]{nameLengthFilter});

        /*et_2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                String tag1=et_1.getText().toString().trim();
                if(TextUtils.isEmpty(tag1)){
                    showToast("请先设置第一足迹");
                    return true;
                }
                return false;
            }
        });
        et_3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                String tag1=et_1.getText().toString().trim();
                String tag2=et_2.getText().toString().trim();

                if(TextUtils.isEmpty(tag1)){
                    showToast("请先设置第一足迹");
                    return true;
                }else if(TextUtils.isEmpty(tag2)){
                    showToast("请先设置第二足迹");
                    return true;
                }
                return false;
            }
        });*/
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
             case R.id.two:
                 if (mEaseSwitchButton2.isSwitchOpen()) {
                     mEaseSwitchButton2.closeSwitch();
                     type = "1";//关闭

                     et_2.setFocusableInTouchMode(true);
                     et_2.setFocusable(true);
                     et_2.requestFocus();

                     et_3.setFocusableInTouchMode(true);
                     et_3.setFocusable(true);
                     et_3.requestFocus();

                     //editText可编辑状态
                     et_1.setFocusableInTouchMode(true);
                     et_1.setFocusable(true);
                     et_1.requestFocus();

                     et_2.setOnTouchListener(new View.OnTouchListener() {

                         @Override
                         public boolean onTouch(View view, MotionEvent motionEvent) {
                             String tag1=et_1.getText().toString().trim();
                             if(TextUtils.isEmpty(tag1)){
                                 //CustomTimeToast("请先设置第一足迹");
                                 return true;
                             }
                             return false;
                         }
                     });
                     et_3.setOnTouchListener(new View.OnTouchListener() {
                         @Override
                         public boolean onTouch(View view, MotionEvent motionEvent) {
                             String tag1=et_1.getText().toString().trim();
                             String tag2=et_2.getText().toString().trim();

                             if(TextUtils.isEmpty(tag1)){
                                 //CustomTimeToast("请先设置第一足迹");
                                 return true;
                             }else if(TextUtils.isEmpty(tag2)){
                                 //CustomTimeToast("请先设置第二足迹");
                                 return true;
                             }
                             return false;
                         }
                     });




                 } else {
                     mEaseSwitchButton2.openSwitch();
                     type = "0";//开
                     //EditText不可编辑
                     et_1.setFocusable(false);
                     et_1.setFocusableInTouchMode(false);

                     et_2.setFocusable(false);
                     et_2.setFocusableInTouchMode(false);

                     et_3.setFocusable(false);
                     et_3.setFocusableInTouchMode(false);

                 }
                 break;
             case R.id.txt_true:
                 String tag1=et_1.getText().toString().trim();
                 String tag2=et_2.getText().toString().trim();
                 String tag3=et_3.getText().toString().trim();
                 if(!TextUtils.isEmpty(tag2)){

                     if(TextUtils.isEmpty(tag1)){
                         CustomTimeToast("请先设置第一足迹");
                     }else{
                         setTag(type,tag1,tag2,tag3);

                     }

                 }else if(!TextUtils.isEmpty(tag3)){

                     if(TextUtils.isEmpty(tag1)){
                         CustomTimeToast("请先设置第一足迹");
                     }else if(TextUtils.isEmpty(tag2)){
                         CustomTimeToast("请先设置第二足迹");
                     }else{
                         setTag(type,tag1,tag2,tag3);

                     }
                 }else{
                     setTag(type,tag1,tag2,tag3);
                 }



                 break;

         }
    }

    /*
    * 获取个人信息
    * */
    public void getPerson(){
        if (ConnectionUtil.isConnected(getApplicationContext())) {
            String userPhone = PreferenceHelper.getUserLogin(GlobalApplication.instance).getString("userPhone", "");
            HttpRequestUtil.HttpRequestByGet(Config.FIND_USER+"?mobile="+userPhone,this,PERSON);
        } else {
            Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    public void setTag(String type,String tag1,String tag2,String tag3){

        if (ConnectionUtil.isConnected(getApplicationContext())) {
            String userPhone = PreferenceHelper.getUserLogin(GlobalApplication.instance).getString("userPhone", "");
            if("0".equals(type)){
                //0为开启 以系统默认的方式显示
                HttpRequestUtil.HttpRequestByGet(Config.SET_TAGS+"?setting="+type+"&mobile="+userPhone+"&tagOne="+tag1+
                        "&tagTwo="+tag2+"&tagThree="+tag3,this,SETTING);

            }else if("1".equals(type)){
                //1为关闭 自定义形式显示
                HttpRequestUtil.HttpRequestByGet(Config.SET_TAGS+"?setting="+type+"&mobile="+userPhone+"&tagOne="+tag1+
                        "&tagTwo="+tag2+"&tagThree="+tag3,this,SETTING);
            }

        } else {
            Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDataDelivered(int taskId, String data) throws JSONException {
        switch (taskId){
            case SETTING:
                LogUtil.e(data.toString());
                JSONObject object2=new JSONObject(data);
                String code=object2.getString("code");
                if("0".equals(code)){
                    finish();
                }
                break;
            case PERSON:
                LogUtil.e(data.toString());
                JSONObject jsonObject=new JSONObject(data);
                String isSetting=jsonObject.getJSONObject("data").getString("isSetting");
                String tagone=jsonObject.getJSONObject("data").getString("userTagOne");
                String tagtwo=jsonObject.getJSONObject("data").getString("userTagTwo");
                String tagthree=jsonObject.getJSONObject("data").getString("userTagThree");
                LogUtil.e("---"+isSetting+"  "+tagone+"  "+tagtwo+"  "+tagthree);
                initView(isSetting,tagone,tagtwo,tagthree);
                break;
        }
    }

    @Override
    public void onErrorHappened(int taskId, String errorCode, String errorMessage) {

    }

    public void initView(String isSetting,String tagone,String tagtwo,String tagthree){

        if("0".equals(isSetting)){
            LogUtil.e("---kai---"+isSetting);
            mEaseSwitchButton2.openSwitch();
            type="0";
            //不可编辑
            et_1.setFocusable(false);
            et_1.setFocusableInTouchMode(false);

            et_2.setFocusable(false);
            et_2.setFocusableInTouchMode(false);

            et_3.setFocusable(false);
            et_3.setFocusableInTouchMode(false);

            et_1.setText(tagone);
            et_2.setText(tagtwo);
            et_3.setText(tagthree);

        }else if("1".equals(isSetting)){
            LogUtil.e("---kai1---"+isSetting);
            mEaseSwitchButton2.closeSwitch();
            type="1";
            //editText可编辑状态
            et_2.setFocusableInTouchMode(true);
            et_2.setFocusable(true);
            et_2.requestFocus();

            et_3.setFocusableInTouchMode(true);
            et_3.setFocusable(true);
            et_3.requestFocus();

            et_1.setFocusableInTouchMode(true);
            et_1.setFocusable(true);
            et_1.requestFocus();

            et_1.setText(tagone);
            et_2.setText(tagtwo);
            et_3.setText(tagthree);

            et_2.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    String tag1=et_1.getText().toString().trim();
                    if(TextUtils.isEmpty(tag1)){
                        //CustomTimeToast("请先设置第一足迹");
                        return true;
                    }
                    return false;
                }
            });
            et_3.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    String tag1=et_1.getText().toString().trim();
                    String tag2=et_2.getText().toString().trim();

                    if(TextUtils.isEmpty(tag1)){
                        //CustomTimeToast("请先设置第一足迹");
                        return true;
                    }else if(TextUtils.isEmpty(tag2)){
                        //CustomTimeToast("请先设置第二足迹");
                        return true;
                    }
                    return false;
                }
            });

        }
    }

    private void CustomTimeToast(String msg) {
        final Toast toast = Toast.makeText(this,msg,
                Toast.LENGTH_LONG);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 500);// 1000表示点击按钮之后，Toast延迟1000ms后显示
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, 1000);// 1000表示Toast显示时间为1秒
    }


}
