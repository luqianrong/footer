package com.example.footer.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.inter.ResponseStringDataListener;
import com.example.footer.R;
import com.example.footer.activity.FaBuActivity;
import com.example.footer.activity.MyFootActivity;
import com.example.footer.activity.UnReadActivity;
import com.example.footer.activity.UserLoginActivity;
import com.example.footer.adapter.NineGridTestAdapter;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.eventbus.FirstEvent;
import com.example.footer.framework.BaseFragment;
import com.example.footer.library.PullToRefreshListView;
import com.example.footer.model.NineGridTestModel;
import com.example.footer.model.NineGridTestModels;
import com.example.footer.network.HttpRequestUtil;
import com.example.footer.utils.AdapterCallback;
import com.example.footer.utils.GetPathFromUri4kitkat;
import com.example.footer.utils.LogUtil;
import com.example.footer.utils.ParseJson;
import com.example.footer.utils.PreferenceHelper;
import com.example.footer.utils.UploadUtil;
import com.example.footer.views.HeadLinearView;
import com.hsg.sdk.common.util.ConnectionUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

import static android.app.Activity.RESULT_OK;
/**
 * Created by 乃军 on 2017/11/7.
 */

public class ZhuYeFragment extends BaseFragment implements View.OnClickListener,ResponseStringDataListener,UploadUtil.OnUploadProcessListener{
    private static final int MAIN=0x001,BENEFIT=0x002,COLLECT=0x003;
    private static final int EMPTY= 1,BIND=4;
    private PullToRefreshListView pullToRefreshListView;
    private ListView refreshableListView;
    private HeadLinearView headlinearview;
    private NineGridTestAdapter mAdapter;

    SharedPreferences userLogin;

    private String tel;
    private String spoorId;
    private String  userIcon;
    private ImageView home_icon;

    private String nickname;
    private TextView nick_name;
    private ImageView img_unread;
    private String msg;
    private RelativeLayout lr_unread;
    private TextView txt_unread;
    private LinearLayout lr_fenge;
    private ImageView home_banner;



    @Override
    protected View getFragmentContentView() {
        return View.inflate(GlobalApplication.instance,R.layout.zhuye, null);
    }

    @Override
    protected void initFragmentView(View view) {

        userLogin = PreferenceHelper.getUserLogin(getActivity());
        tel =userLogin.getString("userPhone","");
        LogUtil.e("用户名："+tel);
        spoorId =userLogin.getString("spoorId","");
        userIcon=userLogin.getString("userIcon","");
        nickname =userLogin.getString("nickName","");



        TextView tvMiddle = (TextView) view.findViewById(R.id.title);
        tvMiddle.setText("足迹");


        TextView textView=(TextView)view.findViewById(R.id.titleBar_Right_tv);
        textView.setText("发布足迹");
        textView.setOnClickListener(this);

        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pListView);
        refreshableListView = pullToRefreshListView.getRefreshableView();
        pullToRefreshListView.setOnRefreshListener(new MyOnRefreshListener());
        //头部缓存数据的显示
        addHeadView();

        if(!TextUtils.isEmpty(tel)){
            bindJushId(tel);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        userLogin = PreferenceHelper.getUserLogin(getActivity());
        tel =userLogin.getString("userPhone","");
        nickname =userLogin.getString("nickName","");
        userIcon =userLogin.getString("userIcon","");
        ImageLoader.getInstance().displayImage(Config.BASE_URL+userIcon, home_icon);
        if(TextUtils.isEmpty(nickname)){
            if(TextUtils.isEmpty(tel)){
                nick_name.setText("用户昵称");
            }else{
                nick_name.setText(tel);
            }

        }else{
            nick_name.setText(nickname);
        }


        if(!TextUtils.isEmpty(tel)){
            bindJushId(tel);
        }

        requestFindSpoorByUser(tel);
    }

    public void addHeadView(){
        //直线布局
        headlinearview = new HeadLinearView(getActivity());
        lr_unread=(RelativeLayout)headlinearview.findViewById(R.id.lr_unread);
        lr_unread.setVisibility(View.GONE);

        home_banner =(ImageView)headlinearview.findViewById(R.id.home_banner);//背景

        lr_fenge =(LinearLayout)headlinearview.findViewById(R.id.lr_fenge);
        lr_fenge.setVisibility(View.GONE);

        txt_unread =(TextView)headlinearview.findViewById(R.id.txt_unread);
        home_icon =(ImageView) headlinearview.findViewById(R.id.home_icon);
        ImageLoader.getInstance().displayImage(Config.BASE_URL+userIcon, home_icon);
        home_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(tel)){
                    Intent intent=new Intent(getActivity(),UserLoginActivity.class);
                    startActivity(intent);
                }else{
                    MyFootActivity.startActivity(tel,nickname,userIcon);
                }

            }
        });


        nick_name =(TextView)headlinearview.findViewById(R.id.nickName);
        if(TextUtils.isEmpty(nickname)){
            if(TextUtils.isEmpty(tel)){
                nick_name.setText("用户昵称");
            }else{
                nick_name.setText(tel);
            }

        }else{
            nick_name.setText(nickname);
        }

        refreshableListView.addHeaderView(headlinearview);
        refreshableListView.setAdapter(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==EMPTY){
            lr_unread.setVisibility(View.GONE);
            lr_fenge.setVisibility(View.GONE);
        }
    }


    @Override
    protected void initFragmentData() {

    }
    @Subscribe
    public void onEventMainThread(FirstEvent event) {

        msg = "  您有"+event.getMsg()+"条未读消息  ";

        if(!TextUtils.isEmpty(event.getMsg())&&(!"0".equals(event.getMsg()))){
            lr_unread.setVisibility(View.VISIBLE);
            txt_unread.setText(msg);
            lr_fenge.setVisibility(View.VISIBLE);
            txt_unread.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(), UnReadActivity.class);
                    startActivityForResult(intent,EMPTY);
                }
            });
        }else{
            lr_unread.setVisibility(View.GONE);
            lr_fenge.setVisibility(View.GONE);
        }

    }



    @Override
    protected void fragmentCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void fragmentDestroy() {
        EventBus.getDefault().unregister(this);
    }

    public void requestFindSpoorByUser(String mobile){
        if(TextUtils.isEmpty(mobile)){
            if (ConnectionUtil.isConnected(getActivity())) {

                HttpRequestUtil.HttpRequestByGet(Config.FINDSPOORBYUSEr_URL, this,MAIN);

            } else {
                Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
            }
        }else{
            if (ConnectionUtil.isConnected(getActivity())) {

                HttpRequestUtil.HttpRequestByGet(Config.FINDSPOORBYUSEr_URL+"?mobile="+mobile, this,MAIN);

            } else {
                Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
            }
        }

    }

    public void requestSpoorBeneFit (String Benefit,String spoorId,String mobile){

            if (ConnectionUtil.isConnected(getActivity())) {

                HttpRequestUtil.HttpRequestByGet(Config.SPOORBENEFIT_URL+"?benefit="+Benefit+"&spoorId="+spoorId+"&mobile="+mobile, this,BENEFIT);

            } else {
                Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
            }


    }

    public void  requestSpoorCollect(String collect,String spoorId,String mobile){

            if (ConnectionUtil.isConnected(getActivity())) {

                HttpRequestUtil.HttpRequestByGet(Config.SPOORCOLLECT_URL+"?mobile="+mobile+"&collect="+collect+"&spoorId="+spoorId, this,COLLECT);

            } else {
                Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
            }


    }

    public void bindJushId(String mobile){
        if (ConnectionUtil.isConnected(getActivity())){
            String registrationId = JPushInterface.getRegistrationID(getContext());
            LogUtil.e("登陆jpushId："+registrationId+"mobile="+mobile);
            if(!TextUtils.isEmpty(registrationId)){
                HttpRequestUtil.HttpRequestByGet(Config.BANDJUSHID+"?mobile="+mobile+"&registrationId="+registrationId,this,BIND);
            }

        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.titleBar_Right_tv:
                 if(TextUtils.isEmpty(tel)){
                     Intent intent=new Intent(getActivity(),UserLoginActivity.class);
                     startActivity(intent);
                 }else{
                     Intent intent=new Intent(getActivity(), FaBuActivity.class);
                     startActivity(intent);
                 }
                 break;
         }
    }



    @Override
    protected void onPullUpRefresh() {
        super.onPullUpRefresh();
        pullToRefreshListView.onRefreshComplete();
    }

    @Override
    protected void onPullDownRefresh() {
        super.onPullDownRefresh();
        pullToRefreshListView.onRefreshComplete();
        //拉下的时候刷新一下
        requestFindSpoorByUser(tel);
    }

    public void initData(final List<NineGridTestModel> mList) {
        mAdapter = new NineGridTestAdapter(getActivity());
        mAdapter.setList(mList);
        mAdapter.setAdapterCallback(new AdapterCallback() {
            @Override
            public void onItemBenefitClick(View view, String status, String spoorId) {
                requestSpoorBeneFit(status,spoorId,tel);
            }

            @Override
            public void onItemSpoorCollectClick(View view, String status, String spoorId) {
                requestSpoorCollect(status,spoorId,tel);
            }

            @Override
            public void onItemDeleteClick(View view, String spoorId) {

            }
        });

        refreshableListView.setAdapter(mAdapter);
        /*refreshableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),DetailFootActivity.class);
                startActivity(intent);
            }
        });*/
    }

    @Override
    public void onDataDelivered(int taskId, String data) throws JSONException {
        switch (taskId){
            case MAIN:
                LogUtil.e("首页数据："+data.toString());
                NineGridTestModels models= ParseJson.parseJson(data, NineGridTestModels.class);
                String banner=models.getBanner();
                ImageLoader.getInstance().displayImage(Config.BASE_URL+banner,home_banner);
                List<NineGridTestModel> list=models.getData();
                initData(list);
                break;
            case BENEFIT:
                JSONObject jsonObject=new JSONObject(data);
                String message=jsonObject.getString("message");
                Toast.makeText(GlobalApplication.instance,message,Toast.LENGTH_SHORT).show();
                break;
            case COLLECT:
                LogUtil.e(data.toString());
                JSONObject object=new JSONObject(data);
                String msg=object.getString("message");
                Toast.makeText(GlobalApplication.instance,msg,Toast.LENGTH_SHORT).show();
                break;
            case BIND:
                LogUtil.e(data.toString());
                JSONObject obj=new JSONObject(data);
                String msgg=obj.getString("message");
                LogUtil.e(msgg);
                //Toast.makeText(GlobalApplication.instance,msgg,Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onErrorHappened(int taskId, String errorCode, String errorMessage) {

    }


    @Override
    public void onUploadDone(int responseCode, String message) {

    }

    @Override
    public void onUploadProcess(int uploadSize) {

    }

    @Override
    public void initUpload(int fileSize) {

    }
}
