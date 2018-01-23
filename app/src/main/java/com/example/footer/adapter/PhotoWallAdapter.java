package com.example.footer.adapter;

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
 * Created by 乃军 on 2017/12/15.
 */

public class PhotoWallAdapter extends BaseAdapter {
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
            view=View.inflate(GlobalApplication.instance, R.layout.picture_item,null);
            viewHolder.pic=(ImageView)view.findViewById(R.id.img_pic);


            view.setTag(viewHolder);
        }else
        {
            viewHolder=(ViewHolder)view.getTag();
        }

        Picture picture=mDataList.get(i);

        ImageLoader.getInstance().displayImage(Config.BASE_URL+picture.getPictureUrl(),viewHolder.pic);
        /*Glide.with(GlobalApplication.instance).load(Config.BASE_URL+picture.getPictureUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                //Bitmap bitmap= BitmapCut.ImageCrop(resource,2,3,false);
                viewHolder.pic.setImageBitmap(resource);
            }
        });*/


        return view;
    }

    class ViewHolder {
        ImageView pic;

    }
}
