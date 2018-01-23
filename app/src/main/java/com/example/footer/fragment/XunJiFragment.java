package com.example.footer.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.inter.ResponseStringDataListener;
import com.example.footer.R;
import com.example.footer.activity.FindXunJiActivity;

import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.framework.BaseFragment;
import com.example.footer.model.Tag;
import com.example.footer.network.HttpRequestUtil;
import com.example.footer.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hsg.sdk.common.util.ConnectionUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by 乃军 on 2017/11/7.
 */

public class XunJiFragment extends BaseFragment implements View.OnClickListener,ResponseStringDataListener {
    public static final int ALL=0x001;
    private TagFlowLayout flow_newTag;
    private TagFlowLayout flow_hotTag;


    @Override
    protected View getFragmentContentView() {
        return View.inflate(GlobalApplication.instance, R.layout.xunji_view,null);
    }

    @Override
    protected void initFragmentView(View view) {
        TextView tvMiddle = (TextView) view.findViewById(R.id.title);
        tvMiddle.setText("寻迹");

        ImageView rightimageView=(ImageView)view.findViewById(R.id.right_image);
        rightimageView.setBackgroundResource(R.drawable.titlebar_search_gray);
        rightimageView.setOnClickListener(this);

        flow_newTag =(TagFlowLayout)view.findViewById(R.id.flow_newTag);
        flow_hotTag =(TagFlowLayout)view.findViewById(R.id.flow_hotTag);
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
        LogUtil.e("最新tag:"+new_list.toString());
        /*TagAdapter tagAdapter1=new TagAdapter();
        grid_newTag.setAdapter(tagAdapter1);
        tagAdapter1.setData(new_list);
        tagAdapter1.notifyDataSetChanged();
        grid_newTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),FindXunJiActivity.class);
                intent.putExtra("Tag",new_list.get(i).getContext());
                startActivity(intent);
            }
        });*/
        final LayoutInflater mInflater = LayoutInflater.from(getActivity());
        flow_newTag.setAdapter(new TagAdapter(new_list) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                Tag tag=(Tag)o;
                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                        flow_newTag, false);
                tv.setText("   "+tag.getContext()+"   ");
                return tv;

            }
        });
        flow_newTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                Intent intent=new Intent(getActivity(),FindXunJiActivity.class);
                intent.putExtra("Tag",new_list.get(position).getContext());
                startActivity(intent);
                return true;
            }
        });



       /* TagAdapter tagAdapter=new TagAdapter();
        grid_hotTag.setAdapter(tagAdapter);
        tagAdapter.setData(hot_list);
        tagAdapter.notifyDataSetChanged();
        grid_hotTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),FindXunJiActivity.class);
                intent.putExtra("Tag",hot_list.get(i).getContext());
                startActivity(intent);
            }
        });*/
        flow_hotTag.setAdapter(new TagAdapter(hot_list) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                Tag tag=(Tag)o;
                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                        flow_newTag, false);
                tv.setText("   "+tag.getContext()+"   ");
                return tv;

            }
        });
        flow_hotTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                Intent intent=new Intent(getActivity(),FindXunJiActivity.class);
                intent.putExtra("Tag",hot_list.get(position).getContext());
                startActivity(intent);
                return true;
            }
        });




    }
}
