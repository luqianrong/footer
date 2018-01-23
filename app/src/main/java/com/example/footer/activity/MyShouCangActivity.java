package com.example.footer.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.inter.ResponseStringDataListener;
import com.example.footer.R;
import com.example.footer.adapter.NineGridScAdapter;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.framework.BaseActivity;
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

import static com.example.footer.activity.DetailFootActivity.COLLECT;

/**
 * Created by 乃军 on 2017/11/13.
 */

public class MyShouCangActivity extends BaseActivity implements View.OnClickListener,ResponseStringDataListener{
    private static final int SPOORCOLLECT=0x001,BENEFIT=0x002,DELETE=0x003;
    private NineGridScAdapter mAdapter;
    private ListView listView;
    SharedPreferences userLogin;
    private String tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoucang);
    }

    @Override
    protected void uIViewInit() throws ParseException {
        userLogin = PreferenceHelper.getUserLogin(this);
        tel =userLogin.getString("userPhone","");

        TextView tvMiddle = (TextView) findViewById(R.id.title);
        tvMiddle.setText("我的收藏");

        ImageView imageView=(ImageView)findViewById(R.id.left_image);
        imageView.setBackgroundResource(R.drawable.titlebar_left);
        imageView.setOnClickListener(this);
        listView =(ListView)findViewById(R.id.list_collection);
        requestFindSpoorByUser(tel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestFindSpoorByUser(tel);
    }

    @Override
    protected void uIViewDataApply() {
    }

    public void requestFindSpoorByUser(String mobile){
        if (ConnectionUtil.isConnected(this)) {

            HttpRequestUtil.HttpRequestByGet(Config.FINDSPOORCOLLECT_URL+"?mobile="+mobile, this,SPOORCOLLECT);

        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }
    }

    public void requestSpoorBeneFit (String Benefit,String spoorId,String mobile){
        if (ConnectionUtil.isConnected(this)) {

            HttpRequestUtil.HttpRequestByGet(Config.SPOORBENEFIT_URL+"?benefit="+Benefit+"&spoorId="+spoorId+"&mobile="+mobile, this,BENEFIT);

        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }
    }

    /*public void requestDeleteSpoor(String spoorId){
        if (ConnectionUtil.isConnected(this)) {

            HttpRequestUtil.HttpRequestByGet(Config.DELETESPOOR_URL+"?spoorId="+spoorId,this,DELETE);

        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }
    }*/

    /*
    * 取消收藏的足迹collect=0取消收藏
    * */
    public void requestcancelSpoor(String mobile,String spoorId){
        if (ConnectionUtil.isConnected(this)) {

            HttpRequestUtil.HttpRequestByGet(Config.SPOORCOLLECT_URL+"?mobile="+mobile+"&collect=0"+"&spoorId="+spoorId, this,DELETE);

        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }
    }
    public void initData(final List<NineGridTestModel> mList) {
        mAdapter = new NineGridScAdapter(this);
        mAdapter.setAdapterCallback(new AdapterCallback() {
            @Override
            public void onItemBenefitClick(View view, String benefit, String spoorId) {
                requestSpoorBeneFit(benefit,spoorId,tel);
            }

            @Override
            public void onItemSpoorCollectClick(View view, String collect, String spoorId) {

            }

            @Override
            public void onItemDeleteClick(View view, String spoorId) {
                //requestDeleteSpoor(spoorId);
                requestcancelSpoor(tel,spoorId);
            }
        });
        mAdapter.setList(mList);
        listView.setAdapter(mAdapter);
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
        }
    }

    @Override
    public void onDataDelivered(int taskId, String data) throws JSONException {
        switch (taskId){
            case SPOORCOLLECT:
                LogUtil.e(data.toString());
                NineGridTestModels models= ParseJson.parseJson(data, NineGridTestModels.class);
                List<NineGridTestModel> list=models.getData();
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
                JSONObject object=new JSONObject(data);
                String msg=object.getString("message");
                AddLengthFilter.CustomTimeToast(GlobalApplication.instance,msg);
                //Toast.makeText(GlobalApplication.instance,msg,Toast.LENGTH_SHORT).show();
                requestFindSpoorByUser(tel);
                break;
        }
    }

    @Override
    public void onErrorHappened(int taskId, String errorCode, String errorMessage) {

    }
}
