package com.example.footer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.inter.ResponseStringDataListener;
import com.example.footer.R;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.framework.BaseActivity;
import com.example.footer.model.Comment;
import com.example.footer.network.HttpRequestUtil;
import com.example.footer.utils.ConvertDemo;
import com.example.footer.utils.LogUtil;
import com.example.footer.utils.PreferenceHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hsg.sdk.common.util.ConnectionUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

import static android.icu.lang.UProperty.NAME;
import static android.media.CamcorderProfile.get;
import static cn.jpush.android.api.JPushInterface.a.v;

/**
 * Created by 乃军 on 2017/12/27.
 */

public class UnReadActivity extends BaseActivity implements View.OnClickListener,ResponseStringDataListener {
    public static final int LIST=0x001,REMOVE=3;
    private static final int EMPTY= 1;
    private SharedPreferences userLogin;
    private String tel;
    private String nicheng;
    private LinearLayout line_comment;
    private StringBuilder delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discuss_view);
    }

    @Override
    protected void uIViewInit() throws ParseException {
        userLogin = PreferenceHelper.getUserLogin(this);
        tel = userLogin.getString("userPhone","");
        nicheng =userLogin.getString("nickName","");

        TextView tvMiddle = (TextView)findViewById(R.id.title);
        tvMiddle.setText("评论");

        ImageView imageView=(ImageView)findViewById(R.id.left_image);
        imageView.setBackgroundResource(R.drawable.titlebar_left);
        imageView.setOnClickListener(this);

        line_comment =(LinearLayout)findViewById(R.id.line_comment);

        //ls_discuss =(ListView)findViewById(R.id.ls_discuss);
        if(TextUtils.isEmpty(tel)){
            Intent intent=new Intent(this,UserLoginActivity.class);
            startActivity(intent);
        }else{
            LogUtil.e("登陆id:"+tel);
            requestUnreadList(tel);
        }
    }

    public void requestUnreadList(String mobile){
        if (ConnectionUtil.isConnected(this)) {

            HttpRequestUtil.HttpRequestByGet(Config.FINDUNREAD_URL+"?mobile="+mobile, this,LIST);

        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }
    }

    public void requestRemoveReadMeassage(String mobile,String unreadIds){
        if (ConnectionUtil.isConnected(this)) {

            HttpRequestUtil.HttpRequestByGet(Config.REMOVESEND_URL+"?mobile="+mobile+"&unreadIds="+unreadIds, this,REMOVE);

        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }
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
                //移除所有未读信息是以未读消息列表中的unreadId+","拼接成字符串相互传递
                String deleteid=delete.toString();
                requestRemoveReadMeassage(tel,deleteid);
                break;
        }
    }

    @Override
    public void onDataDelivered(int taskId, String data) throws JSONException {
        switch (taskId){
            case LIST:
                LogUtil.e(data.toString());
                JSONObject jsonObject=new JSONObject(data);
                String shuju=jsonObject.getString("data");
                Gson gson=new Gson();
                List<Comment> list=gson.fromJson(shuju, new TypeToken<List<Comment>>(){}.getType());
                LogUtil.e(list.toString());
                initView(list);
                break;
            case REMOVE:
                LogUtil.e("删除成功"+data.toString());
                Intent intent=new Intent();
                setResult(EMPTY,intent);
                finish();
                break;
        }
    }

    @Override
    public void onErrorHappened(int taskId, String errorCode, String errorMessage) {

    }

    public void initView(final List<Comment> model){
        delete=new StringBuilder();
        line_comment.removeAllViews();
        if(model.size()!=0){

            for(int i=0;i<model.size();i++){
                String unreadid=model.get(i).getUnreadId();
                if(!TextUtils.isEmpty(unreadid)){
                    delete.append(unreadid+";");
                }
                View v = LayoutInflater.from(this).inflate(R.layout.comment_item,null);
                final int finalI = i;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DetailFootActivity.startActivity(model.get(finalI).getSpoorId());
                    }
                });

                View v_grey =v.findViewById(R.id.v_grey);
                ImageView userIcon =(ImageView)v.findViewById(R.id.userIcon);
                TextView username=(TextView)v.findViewById(R.id.nickName);
                TextView replay =(TextView)v.findViewById(R.id.replay);
                TextView reNickName =(TextView)v.findViewById(R.id.reNickName);
                TextView userComment =(TextView)v.findViewById(R.id.userComment);
                TextView createTime=(TextView)v.findViewById(R.id.createTime);


                ImageLoader.getInstance().displayImage(Config.BASE_URL+model.get(i).getUserIcon(),userIcon);
                if(TextUtils.isEmpty(model.get(i).getNickName())){
                    if(TextUtils.isEmpty(nicheng)){
                        username.setText(tel);
                    }else{
                        username.setText(nicheng);
                    }
                }else{
                    username.setText(model.get(i).getNickName());
                }

                if(!TextUtils.isEmpty(model.get(i).getReNickName())){
                    reNickName.setText(model.get(i).getReNickName());

                }else{
                    replay.setVisibility(View.GONE);
                    reNickName.setVisibility(View.GONE);
                }
                try {
                    String createtime= ConvertDemo.showTime(ConvertDemo.timestamp2Date(model.get(i).getCreateTime()),"yyyy-MM-dd HH:mm:ss");
                    createTime.setText(createtime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(!TextUtils.isEmpty(model.get(i).getUserComment())){
                    userComment.setText(model.get(i).getUserComment());
                }
                line_comment.addView(v);
            }

        }
    }
}
