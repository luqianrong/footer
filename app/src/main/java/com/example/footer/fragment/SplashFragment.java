package com.example.footer.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.inter.ResponseStringDataListener;
import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.example.footer.R;
import com.example.footer.activity.ReleaseFootActivity;
import com.example.footer.adapter.FootAdapter;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.framework.BaseFragment;
import com.example.footer.model.NineGridTestModel;
import com.example.footer.model.NineGridTestModels;
import com.example.footer.network.HttpRequestUtil;
import com.example.footer.utils.AdapterCallback;
import com.example.footer.utils.LogUtil;
import com.example.footer.utils.ParseJson;
import com.example.footer.utils.PreferenceHelper;
import com.example.footer.views.HeadLinearView;
import com.hsg.sdk.common.util.ConnectionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by 乃军 on 2017/12/18.
 */

public class SplashFragment extends BaseFragment implements View.OnClickListener,ResponseStringDataListener, PullToRefreshListener {
    private static final int MAIN=0x001,BENEFIT=0x002,COLLECT=0x003;
    private PullToRefreshRecyclerView pullToRefreshRecyclerView;
    private HeadLinearView headlinearview;
    private FootAdapter footAdapter;

    SharedPreferences userLogin;
    private String tel;
    private String spoorId;
    @Override
    protected View getFragmentContentView() {
        return View.inflate(GlobalApplication.instance, R.layout.splash, null);
    }

    @Override
    protected void initFragmentView(View view) {
        userLogin = PreferenceHelper.getUserLogin(getActivity());
        tel =userLogin.getString("userPhone","");
        spoorId =userLogin.getString("spoorId","");

        TextView tvMiddle = (TextView) view.findViewById(R.id.title);
        tvMiddle.setText("足迹");

        ImageView imageView=(ImageView)view.findViewById(R.id.left_image);
        imageView.setBackgroundResource(R.drawable.titlebar_logo);

        TextView textView=(TextView)view.findViewById(R.id.titleBar_Right_tv);
        textView.setText("发布足迹");
        textView.setOnClickListener(this);

        pullToRefreshRecyclerView = (PullToRefreshRecyclerView) view.findViewById(R.id.pullToRefreshRV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        pullToRefreshRecyclerView.setLayoutManager(layoutManager);
        //是否开启下拉刷新功能
        pullToRefreshRecyclerView.setPullRefreshEnabled(true);
        //是否开启上拉加载功能
        pullToRefreshRecyclerView.setLoadingMoreEnabled(true);
        //设置是否显示上次刷新的时间
        pullToRefreshRecyclerView.displayLastRefreshTime(true);
        //设置刷新回调
        pullToRefreshRecyclerView.setPullToRefreshListener(this);
        //头部缓存数据的显示
        addHeadView();

        requestFindSpoorByUser(tel);
    }

    public void addHeadView(){
        //直线布局
        headlinearview = new HeadLinearView(getActivity());
        pullToRefreshRecyclerView.addHeaderView(headlinearview);
        //pullToRefreshRecyclerView.setAdapter(null);
    }

    @Override
    protected void initFragmentData() {

    }

    @Override
    protected void fragmentCreate() {

    }

    @Override
    protected void fragmentDestroy() {

    }

    public void requestFindSpoorByUser(String mobile){
        if (ConnectionUtil.isConnected(getActivity())) {

            HttpRequestUtil.HttpRequestByGet(Config.FINDSPOORBYUSEr_URL+"?mobile="+mobile, this,MAIN);

        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
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


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.titleBar_Right_tv:
                Intent intent=new Intent(getActivity(), ReleaseFootActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDataDelivered(int taskId, String data) throws JSONException {
        switch (taskId){
            case MAIN:
                NineGridTestModels models= ParseJson.parseJson(data, NineGridTestModels.class);
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
        }
    }

    @Override
    public void onErrorHappened(int taskId, String errorCode, String errorMessage) {

    }

    public void initData(final List<NineGridTestModel> mList) {
        footAdapter = new FootAdapter(mList);
        footAdapter.setAdapterCallback(new AdapterCallback() {
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

        pullToRefreshRecyclerView.setAdapter(footAdapter);

    }

    //RecyclerView舒心
    @Override
    public void onRefresh() {

    }

    //加载更多
    @Override
    public void onLoadMore() {

    }
}
