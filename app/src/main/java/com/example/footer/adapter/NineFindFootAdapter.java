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
import com.example.footer.activity.MyFootActivity;
import com.example.footer.activity.UserLoginActivity;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.model.NineGridTestModel;
import com.example.footer.utils.AdapterCallback;
import com.example.footer.utils.AnimationTools;
import com.example.footer.utils.ConvertDemo;
import com.example.footer.utils.PreferenceHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

import static android.transition.Fade.IN;
import static com.example.footer.R.id.fd_item;
import static com.example.footer.R.id.start;

/**
 * Created by 乃军 on 2017/11/20.
 */

public class NineFindFootAdapter extends BaseAdapter {
    private Context mContext;
    private List<NineGridTestModel> mList;
    protected LayoutInflater inflater;
    private ViewHolder holder;
    SharedPreferences userLogin;
    private String tel;


    public NineFindFootAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        userLogin = PreferenceHelper.getUserLogin(GlobalApplication.instance);
        tel =userLogin.getString("userPhone","");
    }

    private AdapterCallback mAdapterCallback = null;

    public void setAdapterCallback(AdapterCallback mAdapterCallback){
        this.mAdapterCallback=mAdapterCallback;
    }

    public void setList(List<NineGridTestModel> list) {
        mList = list;
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

    //item_bbs_nine_grid
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NineGridTestModel model=mList.get(position);


        if (convertView == null || convertView.getTag() == null) {
            holder = new ViewHolder();
            convertView = View.inflate(GlobalApplication.instance,R.layout.item_bbs_nine_grid,null);
            holder.fd_item=(LinearLayout)convertView.findViewById(fd_item);
            holder.line=(LinearLayout) convertView.findViewById(R.id.line);
            holder.lr_shoucang=(LinearLayout)convertView.findViewById(R.id.lr_shoucang);
            holder.lr_benefitCount=(LinearLayout)convertView.findViewById(R.id.lr_benefitCount);
            holder.userIcon=(ImageView)convertView.findViewById(R.id.userIcon);//头像
            holder.nickName=(TextView)convertView.findViewById(R.id.nickName);//昵称
            holder.createTime=(TextView)convertView.findViewById(R.id.createTime);//时间
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

        ImageLoader.getInstance().displayImage(Config.BASE_URL+mList.get(position).getUserIcon(),holder.userIcon);
        if(TextUtils.isEmpty(mList.get(position).getNickName())){
            holder.nickName.setText(mList.get(position).getUserPhone());
        }else{
            holder.nickName.setText(mList.get(position).getNickName());
        }

        try {
            String createtime= ConvertDemo.showTime(ConvertDemo.timestamp2Date(mList.get(position).getCreateTime()),"yyyy-MM-dd HH:mm:ss");
            holder.createTime.setText(createtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(TextUtils.isEmpty(model.getSpoorContent())){
            holder.spoorContent.setVisibility(View.GONE);
        }else{
            holder.spoorContent.setVisibility(View.VISIBLE);
        }

        if(TextUtils.isEmpty(model.getSpoorTagOne())){
            holder.spoorTagOne.setVisibility(View.GONE);
        }else{
            holder.spoorTagOne.setVisibility(View.VISIBLE);
        }
        if(TextUtils.isEmpty(model.getSpoorTagTwo())){
            holder.spoorTagTwo.setVisibility(View.GONE);
        }else{
            holder.spoorTagTwo.setVisibility(View.VISIBLE);
        }
        if(TextUtils.isEmpty(model.getSpoorTagThree())){
            holder.spoorTagThree.setVisibility(View.GONE);
        }else{
            holder.spoorTagThree.setVisibility(View.VISIBLE);
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
        holder.fd_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailFootActivity.startActivity(model.getSpoorId());
            }
        });

        if(mList.get(position).getSpoorPictures().size()<3){
            for(int i=0;i<mList.get(position).getSpoorPictures().size();i++){
                createView(i,position);
            }
        }else if(mList.get(position).getSpoorPictures().size()>=3){
            for(int i=0;i<3;i++){
                createView(i,position);
            }
        }



        holder.userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(tel)){
                    Intent intent=new Intent(GlobalApplication.instance, UserLoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    GlobalApplication.instance.startActivity(intent);
                }else{
                    MyFootActivity.startActivity(model.getUserPhone(),model.getNickName(),model.getUserIcon());
                }

            }
        });
        //收益
        if("1".equals(model.getIsBenefit())){
            holder.img_benefitCount.setImageResource(R.drawable.home_benefit);
        }else if("0".equals(model.getIsBenefit())){
            holder.img_benefitCount.setImageResource(R.drawable.benefit_gray);
        }

        //收藏
        if("1".equals(model.getIscollect())){
            holder.img_shoucang.setImageResource(R.drawable.home_collect);
        }else if("0".equals(model.getIscollect())){
            holder.img_shoucang.setImageResource(R.drawable.collect_gray);
        }

        holder.benefitCount.setText(mList.get(position).getBenefitCount());
        holder.comments.setText(mList.get(position).getComments());

        holder.lr_benefitCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(tel)){
                    Intent intent=new Intent(GlobalApplication.instance,UserLoginActivity.class);
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
        LinearLayout lr_shoucang;
        LinearLayout lr_benefitCount;
        LinearLayout fd_item;
        LinearLayout line;
        ImageView userIcon;
        TextView nickName;
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
