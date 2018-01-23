package com.example.footer.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.footer.R;
import com.example.footer.application.GlobalApplication;
import com.example.footer.model.Discuss;

import java.util.List;

/**
 * Created by 乃军 on 2017/11/16.
 */

public class DiscussAdapter extends BaseAdapter {

    public List<Discuss> mDataList;
    private ViewHolder viewHolder;

    public void setData(List<Discuss> dataList) {

        mDataList = dataList;
    }
    @Override
    public int getCount() {
        return null!=mDataList?mDataList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return null!=mDataList?mDataList.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(null==convertView){
            viewHolder=new ViewHolder();
            convertView=View.inflate(GlobalApplication.instance, R.layout.discuss_item,null);
            viewHolder.img_head=(ImageView)convertView.findViewById(R.id.img_head);
            viewHolder.txt_name=(TextView)convertView.findViewById(R.id.txt_name);
            viewHolder.txt_time=(TextView)convertView.findViewById(R.id.txt_time);
            viewHolder.txt_desc=(TextView)convertView.findViewById(R.id.txt_desc);


            convertView.setTag(viewHolder);
        }else
        {
            viewHolder=(ViewHolder)convertView.getTag();
        }

        Discuss discuss=mDataList.get(position);
        viewHolder.img_head.setBackgroundResource(discuss.getImg_head());
        viewHolder.txt_name.setText(discuss.getTxt_name());
        viewHolder.txt_time.setText(discuss.getTxt_time());
        viewHolder.txt_desc.setText(discuss.getTxt_desc());

        return convertView;
    }

    class ViewHolder {
        ImageView img_head;
        TextView txt_name;
        TextView txt_time;
        TextView txt_desc;

    }
}
