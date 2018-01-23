package com.example.footer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.inter.ResponseStringDataListener;
import com.example.footer.R;
import com.example.footer.adapter.NineGridTestAdapter;
import com.example.footer.adapter.OtherGridTestAdapter;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.framework.BaseActivity;
import com.example.footer.library.PullToRefreshListView;
import com.example.footer.model.NineGridTestModel;
import com.example.footer.model.NineGridTestModels;
import com.example.footer.network.HttpRequestUtil;
import com.example.footer.utils.AdapterCallback;
import com.example.footer.utils.AddLengthFilter;
import com.example.footer.utils.LogUtil;
import com.example.footer.utils.ParseJson;
import com.example.footer.utils.PreferenceHelper;
import com.example.footer.views.HeadChildView;
import com.hsg.sdk.common.util.ConnectionUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

import me.nereo.multi_image_selector.bean.Image;

import static com.example.footer.activity.DetailFootActivity.COLLECT;

/**
 * Created by 乃军 on 2018/1/3.
 */

public class OtherFootActivity extends BaseActivity implements View.OnClickListener,ResponseStringDataListener {
    public static String MOBILE="userPhone";//列表用户的账号
    public static String NICKNAME="nickname";
    public static String USERICON="userIcon";
    private static final int LOGINID=0x001,BENEFIT=0x002,DELETE=0x003;
    private static final int EMPTY= 1;

    private ImageView img_pl;
    private PullToRefreshListView pullToRefreshListView;
    private ListView refreshableListView;
    private HeadChildView headview;
    private OtherGridTestAdapter mAdapter;

    private String tel;
    private ImageView home_icon;
    private String mobile;
    private String nickname;
    private String usericon;
    private SharedPreferences userLogin;
    private TextView nick_name;
    private TextView tvMiddle;
    private ImageView img_release;
    private LinearLayout rl_myfabu;
    private View bg_color;
    private ImageView home_banner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfoot_view);
    }

    @Override
    protected void uIViewInit() throws ParseException {

        userLogin = PreferenceHelper.getUserLogin(this);
        tel = userLogin.getString("userPhone","");

        mobile =getIntent().getStringExtra(MyFootActivity.MOBILE);
        nickname =getIntent().getStringExtra(MyFootActivity.NICKNAME);
        usericon =getIntent().getStringExtra(MyFootActivity.USERICON);

        ImageView imageView=(ImageView)findViewById(R.id.left_img);
        imageView.setBackgroundResource(R.drawable.titlebar_left);
        imageView.setOnClickListener(this);

        img_pl =(ImageView)findViewById(R.id.img_pl);
        img_pl.setOnClickListener(this);


        tvMiddle = (TextView)findViewById(R.id.txt_title);

        if(TextUtils.isEmpty(nickname)){
            tvMiddle.setText(mobile+"的足迹");
        }else{
            tvMiddle.setText(nickname+"的足迹");
        }

        img_pl.setVisibility(View.GONE);





        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pListView);
        refreshableListView = pullToRefreshListView.getRefreshableView();
        pullToRefreshListView.setOnRefreshListener(new MyOnRefreshListener());
        addHeadView();

        requestFindSpoorByLoginId(mobile,tel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestFindSpoorByLoginId(mobile,tel);
    }

    public void addHeadView(){
        //直线布局
        headview = new HeadChildView(this);
        home_banner =(ImageView)headview.findViewById(R.id.home_banner);
        home_icon =(ImageView) headview.findViewById(R.id.home_icon);
        ImageLoader.getInstance().displayImage(Config.BASE_URL+usericon,home_icon);
        nick_name =(TextView)headview.findViewById(R.id.nickName);
        if(TextUtils.isEmpty(nickname)){
            nick_name.setText(mobile);
        }else{
            nick_name.setText(nickname);
        }

        rl_myfabu =(LinearLayout)headview.findViewById(R.id.rl_myfabu);//发布
        bg_color =(View)headview.findViewById(R.id.bg_color);

        rl_myfabu.setVisibility(View.GONE);
        bg_color.setVisibility(View.GONE);

        img_release =(ImageView)headview.findViewById(R.id.img_release);
        img_release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(tel)){
                    Intent intent=new Intent(OtherFootActivity.this,UserLoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(OtherFootActivity.this,FaBuActivity.class);
                    startActivity(intent);
                }

            }
        });
        refreshableListView.addHeaderView(headview);
        refreshableListView.setAdapter(null);
    }

    @Override
    protected void uIViewDataApply() {

    }

    public static void startActivity(String tel,String nickname,String userIcon){
        Intent intent=new Intent(GlobalApplication.instance, OtherFootActivity.class);
        intent.putExtra(OtherFootActivity.MOBILE,tel);
        intent.putExtra(OtherFootActivity.NICKNAME,nickname);
        intent.putExtra(OtherFootActivity.USERICON,userIcon);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        GlobalApplication.instance.startActivity(intent);
    }

    public void requestFindSpoorByLoginId(String mobile,String loginId){
        if (ConnectionUtil.isConnected(this)){
            if(TextUtils.isEmpty(tel)){
                HttpRequestUtil.HttpRequestByGet(Config.FINDSPOORBYLOGINID_URL+"?mobile="+mobile,this,LOGINID);
            }else{
                HttpRequestUtil.HttpRequestByGet(Config.FINDSPOORBYLOGINID_URL+"?mobile="+mobile+"&loginId="+loginId,this,LOGINID);
            }


        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }
    }

    public void initData(final List<NineGridTestModel> mList) {
        mAdapter = new OtherGridTestAdapter(this);
        mAdapter.setList(mList);
        mAdapter.setAdapterCallback(new AdapterCallback() {
            @Override
            public void onItemBenefitClick(View view, String benefit, String spoorId) {
                requestSpoorBeneFit(benefit,spoorId,tel);
            }

            @Override
            public void onItemSpoorCollectClick(View view, String collect, String spoorId) {
                requestSpoorCollect(collect,spoorId,tel);
            }

            @Override
            public void onItemDeleteClick(View view, String spoorId) {
                requestDeleteSpoor(spoorId);
            }
        });
        refreshableListView.setAdapter(mAdapter);

    }

    public void requestSpoorBeneFit (String Benefit,String spoorId,String mobile){
        if (ConnectionUtil.isConnected(this)) {

            HttpRequestUtil.HttpRequestByGet(Config.SPOORBENEFIT_URL+"?benefit="+Benefit+"&spoorId="+spoorId+"&mobile="+mobile, this,BENEFIT);

        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }
    }

    public void  requestSpoorCollect(String collect,String spoorId,String mobile){

        if (ConnectionUtil.isConnected(this)) {

            HttpRequestUtil.HttpRequestByGet(Config.SPOORCOLLECT_URL+"?mobile="+mobile+"&collect="+collect+"&spoorId="+spoorId, this,COLLECT);

        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }


    }

    public void requestDeleteSpoor(String spoorId){
        if (ConnectionUtil.isConnected(this)) {

            HttpRequestUtil.HttpRequestByGet(Config.DELETESPOOR_URL+"?spoorId="+spoorId,this,DELETE);

        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }
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
            case R.id.left_img:
                finish();
                break;
            case R.id.img_pl:
                if(TextUtils.isEmpty(tel)){
                    Intent intent=new Intent(this,UserLoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(this,DiscussActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }



    @Override
    public void onDataDelivered(int taskId, String data) throws JSONException {
        switch (taskId){
            case LOGINID:
                /*JSONObject object=new JSONObject(data);
                String homeIcon=object.getString("homeIcon");
                ImageLoader.getInstance().displayImage(Config.BASE_URL+homeIcon,home_banner);*/

                NineGridTestModels models= ParseJson.parseJson(data, NineGridTestModels.class);
                String homeIcon=models.getHomeIcon();
                ImageLoader.getInstance().displayImage(Config.BASE_URL+homeIcon,home_banner);

                List<NineGridTestModel> list=models.getData();
                LogUtil.e("-----"+list.toString());
                initData(list);
                break;
            case BENEFIT:
                JSONObject jsonObject=new JSONObject(data);
                String message=jsonObject.getString("message");
                AddLengthFilter.CustomTimeToast(GlobalApplication.instance,message);
                //Toast.makeText(GlobalApplication.instance,message,Toast.LENGTH_SHORT).show();
                break;
            case DELETE:
                LogUtil.e(data.toString());
                JSONObject jsonObject1=new JSONObject(data);
                String msg=jsonObject1.getString("message");
                AddLengthFilter.CustomTimeToast(GlobalApplication.instance,msg);
                //Toast.makeText(GlobalApplication.instance,msg,Toast.LENGTH_SHORT).show();
                requestFindSpoorByLoginId(mobile,tel);
                break;
        }
    }

    @Override
    public void onErrorHappened(int taskId, String errorCode, String errorMessage) {

    }

    protected void onPullUpRefresh() {
        super.onPullUpRefresh();
        pullToRefreshListView.onRefreshComplete();
    }

    @Override
    protected void onPullDownRefresh() {
        super.onPullDownRefresh();
        pullToRefreshListView.onRefreshComplete();
        //下拉的时候刷新一下
        requestFindSpoorByLoginId(mobile,tel);
    }
}
