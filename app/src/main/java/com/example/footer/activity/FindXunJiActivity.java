package com.example.footer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.inter.ResponseStringDataListener;
import com.example.footer.R;
import com.example.footer.adapter.NineFindFootAdapter;
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
import com.hsg.sdk.common.util.ConnectionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

import static com.example.footer.R.id.pListView;
import static com.example.footer.activity.DetailFootActivity.BENEFIT;

/**
 * Created by 乃军 on 2017/11/16.
 */

public class FindXunJiActivity extends BaseActivity implements View.OnClickListener,ResponseStringDataListener{
    private static final int TAG=0x001,BENEFIT=0x002,COLLECT=0x003;
    private TextView tvRight;
    private RelativeLayout sousuobar;
    private PullToRefreshListView pullToRefreshListView;
    private ListView refreshableListView;
    private NineFindFootAdapter mAdapter;

    private EditText etHomeInput;
    private SharedPreferences userLogin;
    private String tel;
    private String tag;
    private TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLogin = PreferenceHelper.getUserLogin(this);
        tel = userLogin.getString("userPhone","");
        setContentView(R.layout.findxunji_view);
    }

    @Override
    protected void uIViewInit() throws ParseException {

        tag =getIntent().getStringExtra("Tag");

        ImageView imageView=(ImageView)findViewById(R.id.left_image);
        imageView.setBackgroundResource(R.drawable.titlebar_left);
        imageView.setOnClickListener(this);

        sousuobar =(RelativeLayout)findViewById(R.id.sousuobar);
        sousuobar.setVisibility(View.VISIBLE);

        etHomeInput =(EditText)findViewById(R.id.etHomeInput);
        display =(TextView)findViewById(R.id.display);
        if(TextUtils.isEmpty(tag)){
            display.setVisibility(View.GONE);
        }else{
            etHomeInput.setText(tag);
        }


        tvRight = (TextView) findViewById(R.id.titleBar_Right_tv);
        tvRight.setText("搜索");
        tvRight.setOnClickListener(this);

        pullToRefreshListView = (PullToRefreshListView) findViewById(pListView);
        refreshableListView = pullToRefreshListView.getRefreshableView();
        pullToRefreshListView.setOnRefreshListener(new MyOnRefreshListener());

        if(!TextUtils.isEmpty(tag)){
            requestFindSpoorByTag(tag);
        }
    }



    @Override
    protected void uIViewDataApply() {

    }

    public void initData(final List<NineGridTestModel> mList) {
        LogUtil.e("集合："+mList.toString());
        mAdapter = new NineFindFootAdapter(FindXunJiActivity.this);
        mAdapter.setList(mList);

        refreshableListView.setAdapter(mAdapter);



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

            }
        });

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
    protected void onStart() {
        super.onStart();
        if(!TextUtils.isEmpty(tag)){
            requestFindSpoorByTag(tag);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.left_image:
                finish();
                break;
            case R.id.titleBar_Right_tv:
                tag=etHomeInput.getText().toString().trim();
                LogUtil.e("tag0----------:"+tag);
                if(!TextUtils.isEmpty(tag)){
                    requestFindSpoorByTag(tag);
                }else{
                    AddLengthFilter.CustomTimeToast(GlobalApplication.instance,"请输入关键字进行搜索");
                    //Toast.makeText(GlobalApplication.instance,"请输入关键字进行搜索",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    public void requestSpoorBeneFit (String Benefit,String spoorId,String mobile){

            if (ConnectionUtil.isConnected(this)) {

                HttpRequestUtil.HttpRequestByGet(Config.SPOORBENEFIT_URL+"?benefit="+Benefit+"&spoorId="+spoorId+"&mobile="+mobile, this,BENEFIT);

            } else {
                Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
            }


    }

    public void requestFindSpoorByTag(String mobile){
        if (ConnectionUtil.isConnected(this)) {
            if(TextUtils.isEmpty(tel)){
                HttpRequestUtil.HttpRequestByGet(Config.FINDSPOORBYTAG_URL+"?tag="+mobile, this,TAG);
            }else{
                HttpRequestUtil.HttpRequestByGet(Config.FINDSPOORBYTAG_URL+"?tag="+mobile+"&loginId="+tel, this,TAG);
            }


        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }
    }

    public void  requestSpoorCollect(String collect,String spoorId,String mobile){
        if(TextUtils.isEmpty(tel)){
            Intent intent=new Intent(this, UserLoginActivity.class);
            startActivity(intent);
        }else{
            if (ConnectionUtil.isConnected(this)) {

                HttpRequestUtil.HttpRequestByGet(Config.SPOORCOLLECT_URL+"?mobile="+mobile+"&collect="+collect+"&spoorId="+spoorId, this,COLLECT);

            } else {
                Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
            }
        }

    }



    @Override
    public void onDataDelivered(int taskId, String data) throws JSONException {
        switch (taskId){
            case TAG:
                LogUtil.e(data);
                NineGridTestModels models=ParseJson.parseJson(data, NineGridTestModels.class);

                List<NineGridTestModel> list=models.getData();
                if(list.size()>0){
                    display.setVisibility(View.GONE);
                    pullToRefreshListView.setVisibility(View.VISIBLE);
                    initData(list);
                }else{
                    pullToRefreshListView.setVisibility(View.GONE);
                    display.setVisibility(View.VISIBLE);
                }

                break;
            case COLLECT:
                LogUtil.e(data.toString());
                JSONObject object=new JSONObject(data);
                String msg=object.getString("message");
                AddLengthFilter.CustomTimeToast(GlobalApplication.instance,msg);
                //Toast.makeText(GlobalApplication.instance,msg,Toast.LENGTH_SHORT).show();
                break;
            case BENEFIT:
                JSONObject jsonObject=new JSONObject(data);
                String message=jsonObject.getString("message");
                AddLengthFilter.CustomTimeToast(GlobalApplication.instance,message);
                //Toast.makeText(GlobalApplication.instance,message,Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onErrorHappened(int taskId, String errorCode, String errorMessage) {

    }
}
