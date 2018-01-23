package com.example.footer.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.inter.ResponseStringDataListener;
import com.example.footer.R;
import com.example.footer.activity.FindXunJiActivity;
import com.example.footer.adapter.RailingAdapter;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.constant.ListType;
import com.example.footer.framework.BaseFragment;
import com.example.footer.library.PullToRefreshListView;
import com.example.footer.model.HotFoots;
import com.example.footer.model.ListEntity;
import com.example.footer.model.NewFoots;
import com.example.footer.model.Tag;
import com.example.footer.network.HttpRequestUtil;
import com.example.footer.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hsg.sdk.common.util.ConnectionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 乃军 on 2018/1/3.
 */

public class RailingFragment extends BaseFragment implements View.OnClickListener,ResponseStringDataListener{
    public static final int ALL=0x001;
    private List<ListEntity> list;
    //装四个类的集合
    private RailingAdapter listAdapter;
    private PullToRefreshListView pullToRefreshListView;
    private ListView refreshableListView;

    @Override
    protected View getFragmentContentView() {
        return View.inflate(GlobalApplication.instance, R.layout.railing,null);
    }

    @Override
    protected void initFragmentView(View view) {
        TextView tvMiddle = (TextView) view.findViewById(R.id.title);
        tvMiddle.setText("寻迹");

        ImageView rightimageView=(ImageView)view.findViewById(R.id.right_image);
        rightimageView.setBackgroundResource(R.drawable.titlebar_search_gray);
        rightimageView.setOnClickListener(this);

        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pListView);
        refreshableListView = pullToRefreshListView.getRefreshableView();
        pullToRefreshListView.setOnRefreshListener(new MyOnRefreshListener());

        requestfindAll();
    }

    public void requestfindAll(){
        if (ConnectionUtil.isConnected(getActivity())) {

            HttpRequestUtil.HttpRequestByGet(Config.ALLTAG_URL, this,ALL);

        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.right_image:
                Intent intent=new Intent(getActivity(),FindXunJiActivity.class);
                intent.putExtra("Tag","");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDataDelivered(int taskId, String data) throws JSONException {
        switch (taskId){
            case ALL:
                LogUtil.e(data);
                JSONObject object=new JSONObject(data);
                JSONObject obj=object.getJSONObject("data");
                String newTag=obj.getString("newTag");
                String hotTag=obj.getString("hotTag");


                Gson gson=new Gson();
                //最新tag
                List<Tag> new_list=gson.fromJson(newTag,new TypeToken<List<Tag>>(){}.getType());
                //热门tag
                List<Tag> hot_list=gson.fromJson(hotTag,new TypeToken<List<Tag>>(){}.getType());

                LogUtil.e(hot_list.toString());
                initView(new_list,hot_list);
                break;
        }
    }

    @Override
    public void onErrorHappened(int taskId, String errorCode, String errorMessage) {

    }

    public void initView(final List<Tag> new_list, final List<Tag> hot_list){
        //最新足迹
        NewFoots newFoots = new NewFoots(ListType.NEW_FOOT,new_list);
        //热门
        HotFoots hotFoots=new HotFoots(ListType.HOT_FOOT,hot_list);

        list = new ArrayList<>();
        list.add(newFoots);
        list.add(hotFoots);

        if (null == listAdapter) {
            listAdapter=new RailingAdapter();

            refreshableListView.setAdapter(listAdapter);
        }
        listAdapter.setData(list);
        listAdapter.notifyDataSetChanged();
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
        requestfindAll();
    }
}
