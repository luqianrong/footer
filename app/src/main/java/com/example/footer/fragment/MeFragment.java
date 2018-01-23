package com.example.footer.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.inter.ResponseStringDataListener;
import com.example.footer.R;
import com.example.footer.activity.AboutFootActivity;
import com.example.footer.activity.MainActivity;
import com.example.footer.activity.MyFootActivity;
import com.example.footer.activity.MyShouCangActivity;
import com.example.footer.activity.PersonalDataActivity;
import com.example.footer.activity.SetFootActivity;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.framework.BaseFragment;
import com.example.footer.network.HttpRequestUtil;
import com.example.footer.utils.LogUtil;
import com.example.footer.utils.PreferenceHelper;
import com.example.footer.views.Exit_Dialog_hb;
import com.hsg.sdk.common.util.ConnectionUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 乃军 on 2017/11/7.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener,ResponseStringDataListener{
    private static final int UNBIND=0x002;

    private RelativeLayout rl_about;
    private RelativeLayout my_sc;
    private LinearLayout llGeren;
    private RelativeLayout rl_set;
    private TextView etInputPhoneNumber;
    private Exit_Dialog_hb dialog;
    private RelativeLayout rl_myfoot;
    private SharedPreferences userLogin;
    private String tel;
    private String nickName;
    private String userIcon;
    private ImageView tou;
    private TextView name;
    private String userPoints;
    private TextView fot_num;


    @Override
    protected View getFragmentContentView() {

        return View.inflate(GlobalApplication.instance,R.layout.wo_view,null);
    }

    @Override
    protected void initFragmentView(View view) {

        TextView tvMiddle = (TextView) view.findViewById(R.id.title);
        tvMiddle.setText("我的");

        tou =(ImageView)view.findViewById(R.id.tou);
        name =(TextView)view.findViewById(R.id.name);
        fot_num =(TextView)view.findViewById(R.id.fot_num);


        llGeren =(LinearLayout)view.findViewById(R.id.llGeren);
        llGeren.setOnClickListener(this);


        rl_about =(RelativeLayout)view.findViewById(R.id.rl_about);
        rl_about.setOnClickListener(this);

        rl_myfoot =(RelativeLayout)view.findViewById(R.id.rl_myfoot);
        rl_myfoot.setOnClickListener(this);

        my_sc =(RelativeLayout)view.findViewById(R.id.my_sc);
        my_sc.setOnClickListener(this);

        rl_set =(RelativeLayout)view.findViewById(R.id.rl_set);
        rl_set.setOnClickListener(this);

        etInputPhoneNumber =(TextView)view.findViewById(R.id.etInputPhoneNumber);
        etInputPhoneNumber.setOnClickListener(this);
    }

    @Override
    protected void initFragmentData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        userLogin = PreferenceHelper.getUserLogin(getActivity());
        tel =userLogin.getString("userPhone","");
        nickName =userLogin.getString("nickName","");
        userIcon =userLogin.getString("userIcon","");
        userPoints =userLogin.getString("userPoints","");

        ImageLoader.getInstance().displayImage(Config.BASE_URL+userIcon,tou);
        if(TextUtils.isEmpty(nickName)){
            name.setText(tel);
        }else{
            name.setText(nickName);
        }
        if(TextUtils.isEmpty(userPoints)){
            fot_num.setText("足迹值: 0");
        }else{
            fot_num.setText("足迹值: "+userPoints);
        }
    }

    @Override
    protected void fragmentCreate() {

    }



    @Override
    protected void fragmentDestroy() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_about:
                Intent intent=new Intent(getActivity(),AboutFootActivity.class);
                startActivity(intent);
                break;
            case R.id.my_sc:
                Intent intent1=new Intent(getActivity(), MyShouCangActivity.class);
                startActivity(intent1);
                break;
            case R.id.llGeren:
                Intent intent2=new Intent(getActivity(),PersonalDataActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_set:
                Intent intent3=new Intent(getActivity(),SetFootActivity.class);
                startActivity(intent3);
                break;
            case R.id.etInputPhoneNumber:
                dialog = new Exit_Dialog_hb(getActivity(), R.style.MyDialogStyle, new Exit_Dialog_hb.ExitDialogListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.tvActionConfirm:


                                SharedPreferences.Editor editor = userLogin.edit();
                                editor.clear();
                                editor.commit();
                                dialog.dismiss();

                                unbindJushId(tel);

//
                                break;
                            case R.id.tvActionCancel:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                dialog.show();
                break;
            case R.id.rl_myfoot:
                 MyFootActivity.startActivity(tel,nickName,userIcon);
                break;
        }
    }
    //解绑
    public void unbindJushId(String mobile){
        if (ConnectionUtil.isConnected(getActivity())){
            String registrationId = JPushInterface.getRegistrationID(getContext());
            LogUtil.e("退出jpushId："+registrationId);
            if(!TextUtils.isEmpty(registrationId)){
                HttpRequestUtil.HttpRequestByGet(Config.UNBINDJUSHID+"?loginId="+mobile+"&registrationId="+registrationId,this,UNBIND);

            }
        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDataDelivered(int taskId, String data) throws JSONException {
        switch (taskId){
            case UNBIND:
                LogUtil.e(data.toString());
                JSONObject object=new JSONObject(data);
                String msg=object.getString("message");
                String code=object.getString("code");


                getActivity().finish();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onErrorHappened(int taskId, String errorCode, String errorMessage) {

    }
}
