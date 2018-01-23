package com.example.footer.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.footer.R;
import com.example.footer.activity.DetailFootActivity;
import com.example.footer.activity.UserLoginActivity;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.model.NineGridTestModel;
import com.example.footer.utils.AdapterCallback;
import com.example.footer.utils.ConvertDemo;
import com.example.footer.utils.PreferenceHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 乃军 on 2018/1/3.
 */

public class OtherGridTestAdapter extends BaseAdapter {
    private Context mContext;
    private List<NineGridTestModel> mList;
    protected LayoutInflater inflater;
    private ViewHolder holder;
    private TextView txt_del;
    SharedPreferences userLogin;
    private String tel;


    private AdapterCallback mAdapterCallback = null;

    public OtherGridTestAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        userLogin = PreferenceHelper.getUserLogin(GlobalApplication.instance);
        tel =userLogin.getString("userPhone","");
    }

    public void setList(List<NineGridTestModel> list) {
        mList = list;
    }

    public void setAdapterCallback(AdapterCallback mAdapterCallback){
        this.mAdapterCallback=mAdapterCallback;
    }

    @Override
    public int getCount() {
        return getListSize(mList);
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //item_myfoot_view
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final NineGridTestModel model=mList.get(position);

        if (convertView == null || convertView.getTag() == null) {
            holder = new ViewHolder();
            convertView = View.inflate(GlobalApplication.instance, R.layout.item_otherfoot_view,null);

            holder.main_foot=(LinearLayout)convertView.findViewById(R.id.main_foot);
            holder.lr_tag=(LinearLayout)convertView.findViewById(R.id.lr_tag);
            holder.lr_benefitCount=(LinearLayout)convertView.findViewById(R.id.lr_benefitCount);
            holder.lr_shoucang=(LinearLayout) convertView.findViewById(R.id.lr_shoucang);
            holder.txt_del =(TextView)convertView.findViewById(R.id.txt_del);
            holder.line=(LinearLayout)convertView.findViewById(R.id.line);
            holder.day=(TextView)convertView.findViewById(R.id.tx_day);//日期
            holder.month=(TextView)convertView.findViewById(R.id.txt_month);//月份
            holder.spoorContent=(TextView)convertView.findViewById(R.id.spoorContent);//内容
            holder.spoorTagOne=(TextView) convertView.findViewById(R.id.spoorTagOne);
            holder.spoorTagTwo=(TextView) convertView.findViewById(R.id.spoorTagTwo);
            holder.spoorTagThree=(TextView) convertView.findViewById(R.id.spoorTagThree);
            holder.benefitCount=(TextView)convertView.findViewById(R.id.benefitCount);//收益个数
            holder.comments=(TextView)convertView.findViewById(R.id.comments);//评论个数
            holder.img_benefitCount=(ImageView)convertView.findViewById(R.id.img_benefitCount);//收益
            holder.img_comments=(ImageView)convertView.findViewById(R.id.img_comments);
            holder.img_shoucang=(ImageView)convertView.findViewById(R.id.img_shoucang);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String date_time= ConvertDemo.timestampDate(mList.get(position).getCreateTime());
        try {
            Date d1 = new SimpleDateFormat("yyyy年MM月dd日").parse(date_time);//定义起始日期
            SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
            SimpleDateFormat sdf2= new SimpleDateFormat("dd");

            String month = sdf1.format(d1);
            String day = sdf2.format(d1);
            holder.day.setText(day);
            holder.month.setText(month+"月");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String tag1=mList.get(position).getSpoorTagOne();
        String tag2=mList.get(position).getSpoorTagTwo();
        String tag3=mList.get(position).getSpoorTagThree();
        if(TextUtils.isEmpty(tag1)){
            holder.spoorTagOne.setVisibility(View.GONE);
        }else {
            holder.spoorTagOne.setVisibility(View.VISIBLE);
        }
        if(TextUtils.isEmpty(tag2)){
            holder.spoorTagTwo.setVisibility(View.GONE);
        }else{
            holder.spoorTagTwo.setVisibility(View.VISIBLE);
        }
        if(TextUtils.isEmpty(tag3)){
            holder.spoorTagThree.setVisibility(View.GONE);
        }else{
            holder.spoorTagThree.setVisibility(View.VISIBLE);
        }

        if(TextUtils.isEmpty(mList.get(position).getSpoorContent())){
            holder.spoorContent.setVisibility(View.GONE);
        }else{
            holder.spoorContent.setVisibility(View.VISIBLE);
        }


        holder.spoorTagOne.setText("  "+mList.get(position).getSpoorTagOne()+"  ");
        holder.spoorTagTwo.setText("  "+mList.get(position).getSpoorTagTwo()+"  ");
        holder.spoorTagThree.setText("  "+mList.get(position).getSpoorTagThree()+"  ");
        holder.spoorContent.setText(mList.get(position).getSpoorContent());
        //需要用微信朋友圈方式显示图片的时候可以把注释去掉
        /*holder.layout.setIsShowAll(mList.get(position).isShowAll);
        holder.layout.setUrlList(mList.get(position).urlList);*/


        holder.line.removeAllViews();
        holder.line.setWeightSum(3);

        if(mList.get(position).getSpoorPictures().size()<3){
            for(int i=0;i<mList.get(position).getSpoorPictures().size();i++){
                createView(i,position);
            }
        }else if(mList.get(position).getSpoorPictures().size()>=3){
            for(int i=0;i<3;i++){
                createView(i,position);
            }
        }

        for(int i = 0; i< holder.line.getChildCount(); i++){

            holder.line.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DetailFootActivity.startActivity(model.getSpoorId());
                }
            });
        }


        //收藏
        if("1".equals(model.getIscollect())){
            holder.img_shoucang.setImageResource(R.drawable.home_collect);
        }else if("0".equals(model.getIscollect())){
            holder.img_shoucang.setImageResource(R.drawable.collect_gray);
        }



        //收益
        if("1".equals(model.getIsBenefit())){
            holder.img_benefitCount.setImageResource(R.drawable.home_benefit);
        }else if("0".equals(model.getIsBenefit())){
            holder.img_benefitCount.setImageResource(R.drawable.benefit_gray);
        }

        holder.benefitCount.setText(mList.get(position).getBenefitCount());
        holder.comments.setText(mList.get(position).getComments());

        holder.lr_benefitCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(tel)){
                    Intent intent=new Intent(GlobalApplication.instance, UserLoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    GlobalApplication.instance.startActivity(intent);
                }else{
                    //这里的代码不要删除，还有用
                    String benefit=model.getIsBenefit();
                    String spoorId=model.getSpoorId();
                    int num=Integer.valueOf(model.getBenefitCount()).intValue();
                    //0 为没有评论 1为已经评论
                    if("0".equals(benefit)){
                        model.setBenefitCount(num+1+"");
                        benefit="1";
                        //反向存储
                        model.setIsBenefit("1");
                    }else if("1".equals(benefit)){
                        model.setBenefitCount(num-1+"");
                        benefit="0";
                        //反向存储
                        model.setIsBenefit("0");
                    }

                    notifyDataSetChanged();
                    mAdapterCallback.onItemBenefitClick(view,benefit,spoorId);
                }

            }
        });

        holder.lr_shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(tel)){
                    Intent intent=new Intent(GlobalApplication.instance,UserLoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    GlobalApplication.instance.startActivity(intent);
                }else{
                    String collect=model.getIscollect();
                    String spoorId=model.getSpoorId();

                    //0 为没有评论 1为已经评论
                    if("0".equals(collect)){
                        collect="1";
                        //反向存储
                        model.setIscollect("1");
                    }else if("1".equals(collect)){
                        collect="0";
                        //反向存储
                        model.setIscollect("0");
                    }

                    notifyDataSetChanged();
                    mAdapterCallback.onItemSpoorCollectClick(view,collect,spoorId);
                }
            }
        });


        holder.main_foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(tel)){
                    Intent intent=new Intent(GlobalApplication.instance,UserLoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    GlobalApplication.instance.startActivity(intent);
                }else{
                    DetailFootActivity.startActivity(model.getSpoorId());
                }

            }
        });

        return convertView;
    }

    public void createView(int i,int position){
        View view=View.inflate(GlobalApplication.instance,R.layout.item,null);
        ImageView img=(ImageView)view.findViewById(R.id.img);
        ImageLoader.getInstance().displayImage(Config.BASE_URL+mList.get(position).getSpoorPictures().get(i).getPictureUrl(),img);
        holder.line.addView(view);
    }

    private class ViewHolder {
        //这种是微信朋友圈显示的图片展示方式
       /* NineGridTestLayout layout;

        public ViewHolder(View view) {
            layout = (NineGridTestLayout) view.findViewById(R.id.layout_nine_grid);
        }*/
        LinearLayout main_foot;
        LinearLayout lr_benefitCount;
        LinearLayout lr_shoucang;

        LinearLayout lr_tag;
        LinearLayout line;
        TextView txt_del;
        TextView day;
        TextView month;
        TextView spoorContent;
        TextView benefitCount;
        TextView comments;
        TextView createTime;
        TextView spoorTagOne;
        TextView spoorTagTwo;
        TextView spoorTagThree;
        ImageView img_benefitCount;
        ImageView img_comments;
        ImageView img_shoucang;


    }

    private int getListSize(List<NineGridTestModel> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
}
