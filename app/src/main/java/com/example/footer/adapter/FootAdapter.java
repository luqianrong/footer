package com.example.footer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.footer.R;
import com.example.footer.application.GlobalApplication;
import com.example.footer.model.NineGridTestModel;
import com.example.footer.utils.AdapterCallback;
import com.example.footer.utils.AnimationTools;
import com.example.footer.utils.ConvertDemo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.util.List;

/**
 * Created by 乃军 on 2017/12/18.
 */

public class FootAdapter extends RecyclerView.Adapter<FootAdapter.ViewHoler> {

    private List<NineGridTestModel> mList;


    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bbs_nine_grid,parent,false);
        ViewHoler viewHoler = new ViewHoler(view);
        return viewHoler;
    }

    public FootAdapter(List<NineGridTestModel> mList){
        this.mList=mList;
    }

    private AdapterCallback mAdapterCallback = null;

    public void setAdapterCallback(AdapterCallback mAdapterCallback){
        this.mAdapterCallback=mAdapterCallback;
    }

    @Override
    public void onBindViewHolder(final ViewHoler holder, int position) {
        final NineGridTestModel model=mList.get(position);
        if(position==0){
            holder.v_bg1.setVisibility(View.GONE);
            holder.v_bg2.setVisibility(View.GONE);
        }
        ImageLoader.getInstance().displayImage(mList.get(position).getUserIcon(),holder.userIcon);
        holder.nickName.setText(mList.get(position).getNickName());
        try {
            String createtime=ConvertDemo.showTime(ConvertDemo.timestamp2Date(mList.get(position).getCreateTime()),"yyyy-MM-dd HH:mm:ss");
            holder.createTime.setText(createtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.spoorTagOne.setText(mList.get(position).getSpoorTagOne());
        holder.spoorTagTwo.setText(mList.get(position).getSpoorTagTwo());
        holder.spoorTagThree.setText(mList.get(position).getSpoorTagThree());
        holder.spoorContent.setText(mList.get(position).getSpoorContent());


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

        /*holder.fd_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GlobalApplication.instance, DetailFootActivity.class);
                intent.putExtra(DetailFootActivity.SPOORID,model.getSpoorId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                GlobalApplication.instance.startActivity(intent);
            }
        });*/

        holder.img_benefitCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                AnimationTools.scale(holder.img_benefitCount);
                notifyDataSetChanged();
                mAdapterCallback.onItemBenefitClick(view,benefit,spoorId);
            }
        });

        holder.img_shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String collect=model.getIscollect();
                String spoorId=model.getSpoorId();

                int num=Integer.valueOf(model.getBenefitCount()).intValue();
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
                AnimationTools.scale(holder.img_benefitCount);
                notifyDataSetChanged();
                mAdapterCallback.onItemSpoorCollectClick(view,collect,spoorId);

            }
        });

        holder.userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GlobalApplication.instance,"hh",Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHoler extends RecyclerView.ViewHolder{
        LinearLayout fd_item;
        LinearLayout line;
        View v_bg1;
        View v_bg2;
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

        public ViewHoler(View itemView) {
            super(itemView);
            fd_item=(LinearLayout)itemView.findViewById(R.id.fd_item);

            v_bg1 =(View)itemView.findViewById(R.id.v_bg1);
            //v_bg2=(View)itemView.findViewById(R.id.v_bg2);

            userIcon=(ImageView)itemView.findViewById(R.id.userIcon);//头像
            nickName=(TextView)itemView.findViewById(R.id.nickName);//昵称
            createTime=(TextView)itemView.findViewById(R.id.createTime);//时间
            spoorContent=(TextView)itemView.findViewById(R.id.spoorContent);//内容
            spoorTagOne=(TextView) itemView.findViewById(R.id.spoorTagOne);
            spoorTagTwo=(TextView) itemView.findViewById(R.id.spoorTagTwo);
            spoorTagThree=(TextView) itemView.findViewById(R.id.spoorTagThree);
            benefitCount=(TextView)itemView.findViewById(R.id.benefitCount);//收益个数
            comments=(TextView)itemView.findViewById(R.id.comments);//评论个数
            img_benefitCount=(ImageView)itemView.findViewById(R.id.img_benefitCount);//收益
            img_comments=(ImageView)itemView.findViewById(R.id.img_comments);
            img_shoucang=(ImageView)itemView.findViewById(R.id.img_shoucang);

        }
    }
}
