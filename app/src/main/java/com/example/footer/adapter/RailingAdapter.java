package com.example.footer.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.footer.R;
import com.example.footer.activity.FindXunJiActivity;
import com.example.footer.application.GlobalApplication;
import com.example.footer.model.HotFoots;
import com.example.footer.model.ListEntity;
import com.example.footer.model.NewFoots;
import com.example.footer.model.Tag;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import static android.media.CamcorderProfile.get;
import static com.example.footer.R.id.flow_newTag;

/**
 * Created by 乃军 on 2018/1/3.
 */

public class RailingAdapter extends BaseAdapter {
    private static final int TYPE_COUNT = 2;//item类型的总数
    private static final int NEW_FOOT = 0;//新的足迹
    private static final int HOT_FOOT = 1;//热点足迹

    //新的
    View new_View = null;
    //热点
    View hot_View=null;

    //新的
    NewHolder  newHolder=null;
    //热点
    HotHolder   hotholder=null;

    private List<ListEntity> mDataList;

    //当前类型
    private int currentTabId;

    public void setData(List<ListEntity> dataList) {
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        return null != mDataList ? mDataList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null != mDataList ? mDataList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        currentTabId =getItemViewType(position);
        ListEntity item=mDataList.get(position);
        switch (currentTabId)
        {

            //项目分类
            case 0:
                if(null==new_View){
                    newHolder=new NewHolder();
                    new_View= LayoutInflater.from(GlobalApplication.instance).inflate(
                            R.layout.rail_item, null);
                    newHolder.title=(TextView) new_View.findViewById(R.id.title);
                    newHolder.flow_Tag=(TagFlowLayout)new_View.findViewById(R.id.flow_Tag);

                    new_View.setTag(newHolder);

                }else{
                    newHolder=(NewHolder)new_View.getTag();
                }

                final NewFoots new_list=(NewFoots)item;
                newHolder.title.setText("最新足迹");

                newHolder.flow_Tag.setAdapter(new TagAdapter(new_list.getList()) {
                    @Override
                    public View getView(FlowLayout parent, int position, Object o) {
                        Tag tag=(Tag)o;
                        TextView tv = (TextView)LayoutInflater.from(GlobalApplication.instance).inflate(
                                R.layout.tv,newHolder.flow_Tag, false);
                        tv.setText("   "+tag.getContext()+"   ");
                        return tv;

                    }
                });
                newHolder.flow_Tag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
                {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent)
                    {
                        Intent intent=new Intent(GlobalApplication.instance,FindXunJiActivity.class);
                        intent.putExtra("Tag",new_list.getList().get(position).getContext());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        GlobalApplication.instance.startActivity(intent);
                        return true;
                    }
                });

                convertView=new_View;
                break;
            case 1:
                if(null==hot_View){
                    hotholder=new HotHolder();
                    hot_View= LayoutInflater.from(GlobalApplication.instance).inflate(
                            R.layout.hot_rail_item, null);
                    hotholder.title=(TextView) hot_View.findViewById(R.id.title);
                    hotholder.flow_Tag=(TagFlowLayout)hot_View.findViewById(R.id.flow_Tag);

                    hot_View.setTag(hotholder);
                }
                else
                {
                    hotholder=(HotHolder)hot_View.getTag();
                }
                final HotFoots hotFoots=(HotFoots)item;
                hotholder.title.setText("最热足迹");

                hotholder.flow_Tag.setAdapter(new TagAdapter(hotFoots.getList()) {
                    @Override
                    public View getView(FlowLayout parent, int position, Object o) {
                        Tag tag=(Tag)o;
                        TextView tv = (TextView)LayoutInflater.from(GlobalApplication.instance).inflate(
                                R.layout.tv,hotholder.flow_Tag, false);
                        tv.setText("   "+tag.getContext()+"   ");
                        return tv;

                    }
                });
                hotholder.flow_Tag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
                {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent)
                    {
                        Intent intent=new Intent(GlobalApplication.instance,FindXunJiActivity.class);
                        intent.putExtra("Tag",hotFoots.getList().get(position).getContext());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        GlobalApplication.instance.startActivity(intent);
                        return true;
                    }
                });
                convertView=hot_View;

        }


        return convertView;
    }

    public int getItemViewType(int position) {
        // TODO Auto-generated method stub

        Object obj=mDataList.get(position);
        if(obj instanceof NewFoots){
            //新足迹
            return  NEW_FOOT;
        }else {
            //热点足迹
            return HOT_FOOT;
        }
    }

    //获取四种视图
    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    class NewHolder{
        TextView title;
        TagFlowLayout flow_Tag;
    }

    //热点
    class HotHolder{
        TextView title;
        TagFlowLayout flow_Tag;
    }
}
