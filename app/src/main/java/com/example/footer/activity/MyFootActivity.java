package com.example.footer.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.inter.ResponseStringDataListener;
import com.example.footer.R;
import com.example.footer.adapter.NineGridFootAdapter;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.framework.BaseActivity;
import com.example.footer.library.PullToRefreshListView;
import com.example.footer.model.NineGridTestModel;
import com.example.footer.model.NineGridTestModels;
import com.example.footer.network.HttpRequestUtil;
import com.example.footer.utils.AdapterCallback;
import com.example.footer.utils.AddLengthFilter;
import com.example.footer.utils.GetPathFromUri4kitkat;
import com.example.footer.utils.LogUtil;
import com.example.footer.utils.ParseJson;
import com.example.footer.utils.PreferenceHelper;
import com.example.footer.utils.UploadUtil;
import com.example.footer.views.HeadChildView;
import com.hsg.sdk.common.util.ConnectionUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 乃军 on 2017/11/16.
 */

public class MyFootActivity extends BaseActivity implements View.OnClickListener,ResponseStringDataListener,UploadUtil.OnUploadProcessListener{
    public static String MOBILE="userPhone";//列表用户的账号
    public static String NICKNAME="nickname";
    public static String USERICON="userIcon";
    private static final int LOGINID=0x001,BENEFIT=0x002,DELETE=0x003;
    private static final int EMPTY= 1;
    private final int PIC_FROM＿LOCALPHOTO_LOGO = 0,PIC_FROM_CAMERA_LOGO = 2;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;

    private ImageView img_pl;
    private PullToRefreshListView pullToRefreshListView;
    private ListView refreshableListView;
    private HeadChildView headview;
    private NineGridFootAdapter mAdapter;

    private String tel;
    private ImageView home_icon;
    private String mobile;
    private String nickname;
    private String usericon;
    private SharedPreferences userLogin;
    private TextView nick_name;
    private TextView tvMiddle;
    private ImageView img_release;
    private LinearLayout rl_myfabu;
    private boolean isLogin;
    private ImageView home_banner;
    private PopupWindow pop;
    private View contentView;
    private TextView tvPaiZhao;
    private TextView tvShouJi;
    private TextView tvQuXiao;
    private WindowManager.LayoutParams lp;
    private File mFolder;
    private String mImgName;
    private File zhenFolder;
    private String logofileDir;
    private Bitmap bm;
    private View loading;
    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    loading.setVisibility(View.GONE);
                    home_banner.setImageBitmap(bm);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfoot_view);
    }

    @Override
    protected void uIViewInit() throws ParseException {

        userLogin = PreferenceHelper.getUserLogin(this);
        isLogin =userLogin.getBoolean("login",false);
        tel = userLogin.getString("userPhone","");

        mobile =getIntent().getStringExtra(MyFootActivity.MOBILE);
        nickname =getIntent().getStringExtra(MyFootActivity.NICKNAME);
        usericon =getIntent().getStringExtra(MyFootActivity.USERICON);

        ImageView imageView=(ImageView)findViewById(R.id.left_img);
        imageView.setBackgroundResource(R.drawable.titlebar_left);
        imageView.setOnClickListener(this);

        loading =(View)findViewById(R.id.loading);

        img_pl =(ImageView)findViewById(R.id.img_pl);
        img_pl.setOnClickListener(this);


        tvMiddle = (TextView)findViewById(R.id.txt_title);
        if(mobile.equals(tel)){
           tvMiddle.setText("我的足迹");
        }else{
            if(TextUtils.isEmpty(nickname)){
                tvMiddle.setText(mobile+"的足迹");
            }else{
                tvMiddle.setText(nickname+"的足迹");
            }

            img_pl.setVisibility(View.GONE);
        }




        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pListView);
        refreshableListView = pullToRefreshListView.getRefreshableView();
        pullToRefreshListView.setOnRefreshListener(new MyOnRefreshListener());
        addHeadView();

        requestFindSpoorByLoginId(mobile,tel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //requestFindSpoorByLoginId(mobile,tel);
    }

    public void addHeadView(){
        //直线布局
        headview = new HeadChildView(this);
        home_banner =(ImageView)headview.findViewById(R.id.home_banner);//背景
        home_icon =(ImageView) headview.findViewById(R.id.home_icon);
        ImageLoader.getInstance().displayImage(Config.BASE_URL+usericon,home_icon);
        nick_name =(TextView)headview.findViewById(R.id.nickName);
        if(TextUtils.isEmpty(nickname)){
            nick_name.setText(mobile);
        }else{
            nick_name.setText(nickname);
        }

        contentView = this.getLayoutInflater().inflate(R.layout.mydataactivitypaizhao, null);
        tvPaiZhao = (TextView) contentView.findViewById(R.id.tv_paizhao);
        tvShouJi = (TextView) contentView.findViewById(R.id.tv_shouji);
        tvQuXiao = (TextView) contentView.findViewById(R.id.tv_quxiao);
        tvPaiZhao.setOnClickListener(this);
        tvShouJi.setOnClickListener(this);
        tvQuXiao.setOnClickListener(this);

        initPopupWindow();
        lp = MyFootActivity.this.getWindow().getAttributes();
        home_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLogin){
                    if (pop.isShowing()) {
                        pop.dismiss();
                    } else {
                        pop.showAtLocation(MyFootActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        pop.update();
                    }
                }
            }
        });

        rl_myfabu =(LinearLayout)headview.findViewById(R.id.rl_myfabu);//发布
        if(!tel.equals(mobile)){
            rl_myfabu.setVisibility(View.GONE);
        }else{
            rl_myfabu.setVisibility(View.VISIBLE);
        }
        img_release =(ImageView)headview.findViewById(R.id.img_release);
        img_release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(tel)){
                    Intent intent=new Intent(MyFootActivity.this,UserLoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(MyFootActivity.this,FaBuActivity.class);
                    startActivity(intent);
                }

            }
        });
        refreshableListView.addHeaderView(headview);
        refreshableListView.setAdapter(null);
    }

    private void initPopupWindow() {
        pop = new PopupWindow(contentView, ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setOutsideTouchable(true);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                lp.alpha = 1f;
                MyFootActivity.this.getWindow().setAttributes(lp);
                pop.dismiss();
            }
        });

    }

    @Override
    protected void uIViewDataApply() {

    }

    public static void startActivity(String tel,String nickname,String userIcon){
        Intent intent=new Intent(GlobalApplication.instance, MyFootActivity.class);
        intent.putExtra(MyFootActivity.MOBILE,tel);
        intent.putExtra(MyFootActivity.NICKNAME,nickname);
        intent.putExtra(MyFootActivity.USERICON,userIcon);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        GlobalApplication.instance.startActivity(intent);
    }

    public void requestFindSpoorByLoginId(String mobile,String loginId){
        if (ConnectionUtil.isConnected(this)){
            if(TextUtils.isEmpty(tel)){
                HttpRequestUtil.HttpRequestByGet(Config.FINDSPOORBYLOGINID_URL+"?mobile="+mobile,this,LOGINID);
            }else{
                HttpRequestUtil.HttpRequestByGet(Config.FINDSPOORBYLOGINID_URL+"?mobile="+mobile+"&loginId="+loginId,this,LOGINID);
            }


        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }
    }

    public void initData(final List<NineGridTestModel> mList) {
        mAdapter = new NineGridFootAdapter(this);
        mAdapter.setList(mList);
        mAdapter.setAdapterCallback(new AdapterCallback() {
            @Override
            public void onItemBenefitClick(View view, String benefit, String spoorId) {
                requestSpoorBeneFit(benefit,spoorId,tel);
            }

            @Override
            public void onItemSpoorCollectClick(View view, String collect, String spoorId) {

            }

            @Override
            public void onItemDeleteClick(View view, String spoorId) {
                requestDeleteSpoor(spoorId);
            }
        });
        refreshableListView.setAdapter(mAdapter);

    }

    public void requestSpoorBeneFit (String Benefit,String spoorId,String mobile){
        if (ConnectionUtil.isConnected(this)) {

            HttpRequestUtil.HttpRequestByGet(Config.SPOORBENEFIT_URL+"?benefit="+Benefit+"&spoorId="+spoorId+"&mobile="+mobile, this,BENEFIT);

        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }
    }

    public void requestDeleteSpoor(String spoorId){
        if (ConnectionUtil.isConnected(this)) {

            HttpRequestUtil.HttpRequestByGet(Config.DELETESPOOR_URL+"?spoorId="+spoorId,this,DELETE);

        } else {
            Toast.makeText(GlobalApplication.instance, R.string.network_error, Toast.LENGTH_LONG).show();
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.left_img:
                finish();
                break;
            case R.id.img_pl:
                if(TextUtils.isEmpty(tel)){
                    Intent intent=new Intent(this,UserLoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(this,DiscussActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_quxiao://正面取消
                lp.alpha = 1f;
                MyFootActivity.this.getWindow().setAttributes(lp);
                pop.dismiss();
                break;
            case R.id.tv_paizhao://正面拍摄
                takePhone();
                //getImgFromZhen();
                pop.dismiss();
                break;
            case R.id.tv_shouji://正面相册
                choosePhone();
                //getImgFromLoCal();
                pop.dismiss();
                break;
        }
    }

    public void takePhone(){
        if (ContextCompat.checkSelfPermission(MyFootActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            //如果没有授权，则请求授权
            ActivityCompat.requestPermissions(MyFootActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);

        }else {
            //有授权，直接开启摄像头
            getImgFromZhen();
        }

    }

    /*
    * 设置从相机获取正面图片
   */
    private void getImgFromZhen() {

        mFolder = new File(MyFootActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "bCache");

        // 判断手机中有没有这个文件夹，没有就新建。
        if (!mFolder.exists()) {
            mFolder.mkdirs();
        }
        // 自定义图片名字，这里是以毫秒数作为图片名。
        mImgName = System.currentTimeMillis() + ".jpg";
        zhenFolder=new File(mFolder,mImgName);
        Uri uri = Uri.fromFile(zhenFolder);


        // 调用系统拍照功能。
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, PIC_FROM_CAMERA_LOGO);
    }

    /*
    * 设置从本地相册获取正面图片
   */
    private void getImgFromLoCal() {

        // 调用本地图库。
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PIC_FROM＿LOCALPHOTO_LOGO);
    }

    public void choosePhone(){
        if (ContextCompat.checkSelfPermission(MyFootActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MyFootActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE2);

        }else {
            getImgFromLoCal();
        }


    }



    @Override
    public void onDataDelivered(int taskId, String data) throws JSONException {
        switch (taskId){
            case LOGINID:
                NineGridTestModels models= ParseJson.parseJson(data, NineGridTestModels.class);
                String homeIcon=models.getHomeIcon();
                ImageLoader.getInstance().displayImage(Config.BASE_URL+homeIcon,home_banner);

                List<NineGridTestModel> list=models.getData();
                LogUtil.e("-----"+list.toString());
                initData(list);
                break;
            case BENEFIT:
                JSONObject jsonObject1=new JSONObject(data);
                String message=jsonObject1.getString("message");
                AddLengthFilter.CustomTimeToast(GlobalApplication.instance,message);
               // Toast.makeText(GlobalApplication.instance,message,Toast.LENGTH_SHORT).show();
                break;
            case DELETE:
                LogUtil.e(data.toString());
                JSONObject object=new JSONObject(data);
                String msg=object.getString("message");
                AddLengthFilter.CustomTimeToast(GlobalApplication.instance,msg);
                //Toast.makeText(GlobalApplication.instance,msg,Toast.LENGTH_SHORT).show();
                requestFindSpoorByLoginId(mobile,tel);
                break;
        }
    }

    @Override
    public void onErrorHappened(int taskId, String errorCode, String errorMessage) {

    }

    protected void onPullUpRefresh() {
        super.onPullUpRefresh();
        pullToRefreshListView.onRefreshComplete();
    }

    @Override
    protected void onPullDownRefresh() {
        super.onPullDownRefresh();
        pullToRefreshListView.onRefreshComplete();
        //下拉的时候刷新一下
        requestFindSpoorByLoginId(mobile,tel);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if(resultCode == RESULT_OK && requestCode == PIC_FROM_CAMERA_LOGO) {

            // 调用系统方法获取到的是被压缩过的图片，通过自定义路径轻松获取原始图片。

            /*bm = BitmapFactory.decodeFile(mFolder.getAbsolutePath()
                    + File.separator + mImgName);*/
            //获取到图片的地址
            logofileDir=zhenFolder.getAbsolutePath();

            bm = BitmapFactory.decodeFile(logofileDir);
            //上传图片
            toUploadFile(tel,logofileDir);

        } else if (resultCode == RESULT_OK && requestCode == PIC_FROM＿LOCALPHOTO_LOGO) {
            try {
                if (data != null) {
                    // 获取本地相册图片。

                    Uri uri = data.getData();
                    //获取图片的本地地址
                    logofileDir= GetPathFromUri4kitkat.getPath(MyFootActivity.this,uri);
                    //logofileDir=getAbsoluteImagePath(uri);

                    ContentResolver cr = MyFootActivity.this.getContentResolver();
                    bm = BitmapFactory.decodeStream(cr.openInputStream(uri));

                    toUploadFile(tel,logofileDir);

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //上传文件
    private void toUploadFile(String mobile, String logofileDir) {
        Map<String,String> map=new HashMap<>();
        map.put("mobile",mobile);
        final String fileKey = "file";
        final UploadUtil uploadUtil = UploadUtil.getInstance();
        uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态

        loading.setVisibility(View.VISIBLE);

        uploadUtil.uploadFile(logofileDir, fileKey, Config.MODIFY_USER_ICON, map);
        uploadUtil.setYourListener(new UploadUtil.YourListener() {
            @Override
            public void onSomeChange(String re, int i) {
                if (i == 1) {
                    Message message=new Message();
                    message.obj=re;
                    message.what=1;
                    handler.sendMessage(message);

                }

            }
        });
    }

    @Override
    public void onUploadDone(int responseCode, String message) {

    }

    @Override
    public void onUploadProcess(int uploadSize) {

    }

    @Override
    public void initUpload(int fileSize) {

    }
}
