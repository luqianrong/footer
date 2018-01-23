package com.example.footer.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.footer.R;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.model.Picture;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by 乃军 on 2017/12/21.
 */

public class PicAdapter extends BaseAdapter {
    public List<Picture> mDataList;
    private ViewHolder viewHolder;

    public void setData(List<Picture> dataList) {

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(null==view){
            viewHolder=new ViewHolder();
            view=View.inflate(GlobalApplication.instance, R.layout.item,null);
            viewHolder.pic=(ImageView)view.findViewById(R.id.img);


            view.setTag(viewHolder);
        }else
        {
            viewHolder=(ViewHolder)view.getTag();
        }

        Picture picture=mDataList.get(i);
        //ImageLoader.getInstance().displayImage(Config.BASE_URL+picture.getPictureUrl(),viewHolder.pic);

        if(!TextUtils.isEmpty(picture.getPictureUrl())){

            ImageLoader.getInstance().displayImage(Config.BASE_URL+picture.getPictureUrl(),viewHolder.pic);
        }else{
           viewHolder.pic.setImageResource(R.drawable.home_head);
        }



        return view;
    }

    class ViewHolder {
        ImageView pic;

    }
}
