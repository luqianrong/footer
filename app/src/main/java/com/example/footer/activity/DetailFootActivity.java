package com.example.footer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.inter.ResponseStringDataListener;
import com.example.footer.R;
import com.example.footer.adapter.PhotoWallAdapter;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.framework.BaseActivity;
import com.example.footer.model.NineGridTestModel;
import com.example.footer.model.Picture;
import com.example.footer.network.HttpRequestUtil;
import com.example.footer.utils.AddLengthFilter;
import com.example.footer.utils.AnimationTools;
import com.example.footer.utils.ConvertDemo;
import com.example.footer.utils.LogUtil;
import com.example.footer.utils.ParseJson;
import com.example.footer.utils.PreferenceHelper;
import com.example.footer.view.PicGridView;
import com.hsg.sdk.common.util.ConnectionUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by 乃军 on 2017/11/13.
 */

public class DetailFootActivity extends BaseActivity implements View.OnClickListener,ResponseStringDataListener{
    public static final String SPOORID="spoorId";
    public static final int SPOORDETAIL=0x001,ADDCOMMENT=0x002,BENEFIT=0x003,COLLECT=0x004;;
    private TextView tx_report;
    private RelativeLayout rl_comment;
    private TextView comment_send;
    private ImageView img_head;
    private TextView nickName;
    private TextView createTime;
    private TextView spoorTagOne;
    private TextView spoorTagTwo;
    private TextView spoorTagThree;
    private TextView spoorContent;
    private TextView benefitCount;
    private PicGridView  photo_wall;
    private PhotoWallAdapter photoWallAdapter;
    private LinearLayout line_comment;
    private EditText comment_content;
    private String spoorid;
    private SharedPreferences userLogin;
    private String tel;
    private String nicheng;
    private int flag;
    private String rely;
    private String content;
    private TextView txt_shouyi;
    private TextView txt_sc;
    private ImageView img_shoucang;
    private ImageView img_benefitCount;
    private String benefit;
    private String spoorId;
    private String collect;
    private ImageView big_img;
    private LinearLayout lr_tag;
    private View loading;
    private LinearLayout lr_benefitCount;
    private LinearLayout lr_shoucang;
    private RelativeLayout rl_all;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);
        userLogin = PreferenceHelper.getUserLogin(this);
        tel = userLogin.getString("userPhone","");
        nicheng =userLogin.getString("nickName","");
        spoorid =getIntent().getStringExtra(DetailFootActivity.SPOORID);

        loading =(View)findViewById(R.id.loading);
        LogUtil.e("获取spoorid值："+spoorid);
            if(!TextUtils.isEmpty(spoorid)){
                requestFindSpoorDetail(spoorid,tel);
            }

    }

    @Override
    protected void uIViewInit() throws ParseException {
        TextView tvMiddle = (TextView) findViewById(R.id.title);
        tvMiddle.setText("详情");

        rl_all =(RelativeLayout)findViewById(R.id.rl_all);


        ImageView imageView=(ImageView)findViewById(R.id.left_image);
        imageView.setBackgroundResource(R.drawable.titlebar_left);
        imageView.setOnClickListener(this);

        lr_benefitCount =(LinearLayout)findViewById(R.id.lr_benefitCount);
        lr_shoucang =(LinearLayout)findViewById(R.id.lr_shoucang);

        img_head =(ImageView)findViewById(R.id.img_head);
        nickName =(TextView)findViewById(R.id.nickName);
        createTime =(TextView)findViewById(R.id.createTime);
        lr_tag =(LinearLayout)findViewById(R.id.lr_tag);
        spoorTagOne =(TextView)findViewById(R.id.spoorTagOne);
        spoorTagTwo =(TextView)findViewById(R.id.spoorTagTwo);
        spoorTagThree =(TextView)findViewById(R.id.spoorTagThree);
        spoorContent =(TextView)findViewById(R.id.spoorContent);
        benefitCount =(TextView)findViewById(R.id.benefitCount);
        photo_wall =(PicGridView)findViewById(R.id.photo_wall);//微信图片全部显示


        img_benefitCount =(ImageView)findViewById(R.id.img_benefitCount);
        img_benefitCount.setOnClickListener(this);
        img_shoucang =(ImageView)findViewById(R.id.img_shoucang);
        img_shoucang.setOnClickListener(this);

        tx_report =(TextView)findViewById(R.id.tx_report);
        tx_report.setOnClickListener(this);

        line_comment =(LinearLayout)findViewById(R.id.line_comment);

        rl_comment =(RelativeLayout)findViewById(R.id.rl_comment);
        rl_comment.setOnClickListener(this);

        comment_content =(EditText)findViewById(R.id.comment_content);

        comment_send=(TextView)findViewById(R.id.comment_send);
        comment_send.setOnClickListener(this);


    }

    public void requestFindSpoorDetail(String spoorid,String tel){

            if (ConnectionUtil.isConnected(this)) {
                loading.setVisibility(View.VISIBLE);

                if(TextUtils.isEmpty(tel)){
                    HttpRequestUtil.HttpRequestByGet(Config.FINDSPOORDETAIL_URL+"?spoorId="+spoorid, this,SPOORDETAIL);
                }else{
                    HttpRequestUtil.HttpRequestByGet(Config.FINDSPOORDETAIL_URL+"?spoorId="+spoorid+"&mobile="+tel, this,SPOORDETAIL);
                }

            } else {
                Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
            }

    }

    @Override
    protected void uIViewDataApply() {

    }

    @Override
    protected void activityCreate() {

    }

    @Override
    protected void activityDestroy() {

    }

    @Override
    protected void onWindowHasFocus(boolean hasFocus) {

    }

    public static void startActivity(String spoorId){
        Intent intent=new Intent(GlobalApplication.instance, DetailFootActivity.class);
        intent.putExtra(DetailFootActivity.SPOORID,spoorId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        GlobalApplication.instance.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.left_image:

                finish();
                break;

            case R.id.tx_report:
                if(TextUtils.isEmpty(tel)){
                    //如果用户没有登陆
                    Intent intent=new Intent(DetailFootActivity.this,UserLoginActivity.class);
                    startActivity(intent);
                }else{
                    rl_comment.setVisibility(View.VISIBLE);
                    SpannableString s = new SpannableString("评论");
                    comment_content.setHint(s);
                    flag =0;
                    rely ="0";
                }


                break;
            case R.id.comment_send:
                rl_comment.setVisibility(View.VISIBLE);
                content =comment_content.getText().toString().trim();
                if(TextUtils.isEmpty(content)){
                    AddLengthFilter.CustomTimeToast(GlobalApplication.instance,"内容不能为空");
                    //Toast.makeText(GlobalApplication.instance,"内容不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    //flag ,rely;
                    if(flag ==0){//发表文章评论
                        requestAddComment(spoorid,tel,rely, content);
                    }else if(flag ==1){//回复他人评论
                        requestAddComment(spoorid,tel, rely, content);
                    }
                }

            break;

        }
    }

    /*
    * 受益请求
    * */
    public void requestSpoorBeneFit (String Benefit,String spoorId,String mobile){

            if (ConnectionUtil.isConnected(this)) {

                HttpRequestUtil.HttpRequestByGet(Config.SPOORBENEFIT_URL+"?benefit="+Benefit+"&spoorId="+spoorId+"&mobile="+mobile, this,BENEFIT);

            } else {
                Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
            }


    }
    /*
    * 收藏请求
    * */
    public void  requestSpoorCollect(String collect,String spoorId,String mobile){

            if (ConnectionUtil.isConnected(this)) {

                HttpRequestUtil.HttpRequestByGet(Config.SPOORCOLLECT_URL+"?mobile="+mobile+"&collect="+collect+"&spoorId="+spoorId, this,COLLECT);

            } else {
                Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
            }
    }
    /*
    * 发表评论
    * */
    public void requestAddComment (String spoorid,String mobile,String reply,String  content){
        if(TextUtils.isEmpty(tel)){
            Intent intent=new Intent(this,UserLoginActivity.class);
            startActivity(intent);
        }else{
            if (ConnectionUtil.isConnected(this)) {
                loading.setVisibility(View.VISIBLE);
                HashMap<String, String> params = new HashMap<>();
                params.put("spoorId",spoorid);
                params.put("mobile",mobile);
                params.put("reply",reply);
                params.put("content",content);
                LogUtil.e("spoorId:"+spoorid+"  mobile:"+mobile+"  reply:"+reply+"  content:"+content);

                HttpRequestUtil.HttpRequestByPost(Config.ADDCOMMENT_URL,params, this,ADDCOMMENT);

            } else {
                AddLengthFilter.CustomTimeToast(GlobalApplication.instance,"网络未连接");
                //Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onDataDelivered(int taskId, String data) throws JSONException {
        switch (taskId){
            case SPOORDETAIL:
                LogUtil.e(data.toString());
                rl_all.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);

                JSONObject jsonObject=new JSONObject(data);
                JSONObject o= (JSONObject) jsonObject.get("data");
                final NineGridTestModel model=ParseJson.parseJson(o.toString(),NineGridTestModel.class);
                LogUtil.e(model.toString());
                ImageLoader.getInstance().displayImage(Config.BASE_URL+model.getUserIcon(),img_head);
                if(TextUtils.isEmpty(model.getNickName())){
                    nickName.setText(model.getUserPhone());
                }else{
                    nickName.setText(model.getNickName());
                }

                try {
                    String time=ConvertDemo.showTime(ConvertDemo.timestamp2Date(model.getCreateTime()),"yyyy-MM-dd HH:mm:ss");
                    createTime.setText(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String tag1=model.getSpoorTagOne();
                String tag2=model.getSpoorTagTwo();
                String tag3=model.getSpoorTagThree();

                if(TextUtils.isEmpty(model.getSpoorContent())){
                    spoorContent.setVisibility(View.GONE);
                }else{
                    spoorContent.setVisibility(View.VISIBLE);
                }

                if(TextUtils.isEmpty(tag1)){
                    spoorTagOne.setVisibility(View.GONE);
                }else{
                    spoorTagTwo.setVisibility(View.VISIBLE);
                }
                if(TextUtils.isEmpty(tag2)){
                    spoorTagTwo.setVisibility(View.GONE);
                }else{
                    spoorTagTwo.setVisibility(View.VISIBLE);
                }
                if(TextUtils.isEmpty(tag3)){
                    spoorTagThree.setVisibility(View.GONE);
                }else{
                    spoorTagThree.setVisibility(View.VISIBLE);
                }
                spoorTagOne.setText(" "+model.getSpoorTagOne()+" ");
                spoorTagTwo.setText(" "+model.getSpoorTagTwo()+" ");
                spoorTagThree.setText(" "+model.getSpoorTagThree()+" ");
                spoorContent.setText(model.getSpoorContent());
                benefitCount.setText("已受益: "+model.getBenefitCount());

                //收益
                if("1".equals(model.getIsBenefit())){
                    img_benefitCount.setImageResource(R.drawable.home_benefit);
                }else if("0".equals(model.getIsBenefit())){
                    img_benefitCount.setImageResource(R.drawable.benefit_gray);
                }

                lr_benefitCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(TextUtils.isEmpty(tel)){
                            Intent intent=new Intent(GlobalApplication.instance,UserLoginActivity.class);
                            startActivity(intent);
                        }else{
                            //这里的代码不要删除，还有用
                            benefit =model.getIsBenefit();
                            spoorId =model.getSpoorId();
                            int num=Integer.valueOf(model.getBenefitCount()).intValue();
                            //0 为没有评论 1为已经评论
                            if("0".equals(benefit)){
                                model.setBenefitCount(num+1+"");
                                benefit ="1";
                                //反向存储
                                model.setIsBenefit("1");
                                img_benefitCount.setImageResource(R.drawable.home_benefit);
                            }else if("1".equals(benefit)){
                                model.setBenefitCount(num-1+"");
                                benefit ="0";
                                //反向存储
                                model.setIsBenefit("0");
                                img_benefitCount.setImageResource(R.drawable.benefit_gray);
                            }
                            requestSpoorBeneFit(benefit, spoorId,tel);
                        }

                    }
                });

                //收藏
                if("1".equals(model.getIscollect())){
                    img_shoucang.setImageResource(R.drawable.home_collect);
                }else if("0".equals(model.getIscollect())){
                    img_shoucang.setImageResource(R.drawable.collect_gray);
                }

                lr_shoucang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(TextUtils.isEmpty(tel)){
                            Intent  intent=new Intent(DetailFootActivity.this,UserLoginActivity.class);
                            startActivity(intent);
                        }else{
                            collect =model.getIscollect();
                            spoorId=model.getSpoorId();

                            //0 为没有评论 1为已经评论
                            if("0".equals(collect)){
                                collect ="1";
                                //反向存储
                                model.setIscollect("1");
                                img_shoucang.setImageResource(R.drawable.home_collect);
                            }else if("1".equals(collect)){
                                collect ="0";
                                //反向存储
                                model.setIscollect("0");
                                img_shoucang.setImageResource(R.drawable.collect_gray);
                            }
                            requestSpoorCollect(collect,spoorId,tel);
                        }

                    }
                });



                if(photoWallAdapter==null){
                    photoWallAdapter=new PhotoWallAdapter();
                    photo_wall.setAdapter(photoWallAdapter);
                }
                photoWallAdapter.setData(model.getSpoorPictures());
                photoWallAdapter.notifyDataSetChanged();

                line_comment.removeAllViews();
                if(model.getSpoorComment().size()!=0){

                    for(int i=0;i<model.getSpoorComment().size();i++){
                        View v = LayoutInflater.from(this).inflate(R.layout.comment_item,null);
                        View v_grey =v.findViewById(R.id.v_grey);
                        View first_view=v.findViewById(R.id.first_view);
                        ImageView userIcon =(ImageView)v.findViewById(R.id.userIcon);
                        TextView username=(TextView)v.findViewById(R.id.nickName);
                        TextView replay =(TextView)v.findViewById(R.id.replay);
                        TextView reNickName =(TextView)v.findViewById(R.id.reNickName);
                        TextView userComment =(TextView)v.findViewById(R.id.userComment);
                        TextView createTime=(TextView)v.findViewById(R.id.createTime);

                        if(i==0){
                              v_grey.setVisibility(View.GONE);
                            first_view.setVisibility(View.GONE);
                          }
                          ImageLoader.getInstance().displayImage(Config.BASE_URL+model.getSpoorComment().get(i).getUserIcon(),userIcon);
                          if(TextUtils.isEmpty(model.getSpoorComment().get(i).getNickName())){
                              if(TextUtils.isEmpty(nicheng)){
                                  username.setText(model.getSpoorComment().get(i).getUserPhone());
                              }else{
                                  username.setText(nicheng);
                              }
                          }else{
                              username.setText(model.getSpoorComment().get(i).getNickName());
                          }

                          if(!TextUtils.isEmpty(model.getSpoorComment().get(i).getReNickName())){
                              replay.setVisibility(View.VISIBLE);
                              reNickName.setVisibility(View.VISIBLE);
                              reNickName.setText(model.getSpoorComment().get(i).getReNickName());

                          }else{
                              if(!TextUtils.isEmpty(model.getSpoorComment().get(i).getReUserPhone())){
                                  replay.setVisibility(View.VISIBLE);
                                  reNickName.setVisibility(View.VISIBLE);
                                  reNickName.setText(model.getSpoorComment().get(i).getReUserPhone());

                              }else{
                                  replay.setVisibility(View.GONE);
                                  reNickName.setVisibility(View.GONE);
                              }

                          }
                        try {
                            String createtime=ConvertDemo.showTime(ConvertDemo.timestamp2Date(model.getSpoorComment().get(i).getCreateTime()),"yyyy-MM-dd HH:mm:ss");
                            createTime.setText(createtime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(!TextUtils.isEmpty(model.getSpoorComment().get(i).getUserComment())){
                            userComment.setText(model.getSpoorComment().get(i).getUserComment());
                        }
                        line_comment.addView(v);
                        final int finalI1 = i;
                        final int finalI = i;
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(TextUtils.isEmpty(tel)){
                                    Intent intent=new Intent(DetailFootActivity.this,UserLoginActivity.class);
                                    startActivity(intent);
                                }else{
                                    rl_comment.setVisibility(View.VISIBLE);
                                    if(!TextUtils.isEmpty(model.getSpoorComment().get(finalI).getNickName())){
                                        SpannableString s = new SpannableString(model.getSpoorComment().get(finalI).getNickName());
                                        comment_content.setHint("回复"+s);
                                    }else if(TextUtils.isEmpty(model.getSpoorComment().get(finalI).getNickName())){
                                        SpannableString s = new SpannableString(model.getSpoorComment().get(finalI).getUserPhone());
                                        comment_content.setHint("回复"+s);
                                    }

                                    flag =1;
                                    rely =model.getSpoorComment().get(finalI1).getId();
                                }

                            }
                        });
                    }
                }
                List<Picture> list= model.getSpoorPictures();
                final List<String> pics=new ArrayList<>();
                for(Picture picture:list){
                    pics.add(Config.BASE_URL+picture.getPictureUrl());
                }

                photo_wall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(DetailFootActivity.this, ImagePagerActivity.class);
                        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (Serializable) pics);
                        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, i);
                        startActivity(intent);
                    }
                });
                break;
            case ADDCOMMENT:
                loading.setVisibility(View.GONE);
                comment_content.setText("");
                //收起虚拟键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(comment_content.getWindowToken(), 0) ;

                LogUtil.e(data.toString());
                JSONObject object=new JSONObject(data);
                String code=object.getString("code");
                String message=object.getString("message");
                AddLengthFilter.CustomTimeToast(GlobalApplication.instance,message);

                if("0".equals(code)){
                    rl_comment.setVisibility(View.GONE);
                    requestFindSpoorDetail(spoorid,tel);
                }

                break;
            case BENEFIT:
                JSONObject Object=new JSONObject(data);
                String statu=Object.getString("code");
                String msg=Object.getString("message");
                if("0".equals(statu)){
                    AddLengthFilter.CustomTimeToast(GlobalApplication.instance,msg);
                    //Toast.makeText(GlobalApplication.instance,msg,Toast.LENGTH_SHORT).show();
                    requestFindSpoorDetail(spoorid,tel);
                }

                break;
            case COLLECT:
                JSONObject Obj=new JSONObject(data);
                String sta=Obj.getString("code");
                String msge=Obj.getString("message");
                if("0".equals(sta)){
                    AddLengthFilter.CustomTimeToast(GlobalApplication.instance,msge);
                    //Toast.makeText(GlobalApplication.instance,msge,Toast.LENGTH_SHORT).show();
                    requestFindSpoorDetail(spoorid,tel);
                }
                break;
        }
    }

    @Override
    public void onErrorHappened(int taskId, String errorCode, String errorMessage) {

    }
}
