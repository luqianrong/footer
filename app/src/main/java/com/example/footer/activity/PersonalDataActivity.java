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
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.inter.ResponseStringDataListener;
import com.bigkoo.pickerview.TimePickerView;
import com.example.footer.R;
import com.example.footer.application.ApplicationController;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.framework.BaseActivity;
import com.example.footer.model.SelectItem;
import com.example.footer.network.HttpRequestUtil;
import com.example.footer.utils.AddLengthFilter;
import com.example.footer.utils.ConvertDemo;
import com.example.footer.utils.GetPathFromUri4kitkat;
import com.example.footer.utils.LogUtil;
import com.example.footer.utils.PreferenceHelper;
import com.example.footer.utils.UploadUtil;
import com.example.footer.view.SelectPicPopupWindow;
import com.hsg.sdk.common.util.ConnectionUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL;

import static android.os.Build.VERSION_CODES.M;


/**
 * Created by 乃军 on 2017/11/14.
 */

public class PersonalDataActivity extends BaseActivity implements View.OnClickListener,ResponseStringDataListener,UploadUtil.OnUploadProcessListener{
    private final int PIC_FROM＿LOCALPHOTO_LOGO = 0,PIC_FROM_CAMERA_LOGO = 1;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private static final int    NAME    = 2;
    private static final int PERSON=8;
    private static final int MODIFYUSER=9;
    private static final int PIC=10;
    private static final int NICK=11;
    private static final int GENDER=12;
    private static final int BIRTHDAY=13;
    private static final int EMPTY =14;
    private RelativeLayout reXing;
    private SelectPicPopupWindow menuWindow2;
    TextView etXing;

    private TextView txt_birthday;
    private RelativeLayout rl_birthday;
    private TimePickerView YearmonthDay;
    private ImageView ivTou;
    private PopupWindow pop;
    private View contentView;
    private WindowManager.LayoutParams lp;
    private TextView tvPaiZhao;
    private TextView tvShouJi;
    private TextView tvQuXiao;
    private File logopicFile;
    private Uri logoUri;
    private File mFolder;
    private String mImgName;
    private File zhenFolder;
    private String logofileDir;
    private RelativeLayout rl_pass;
    private TextView tx_name;

    private EditText etXing1;
    private TextView et_pwd;
    private String userPhone;
    private SharedPreferences userLogin;
    SharedPreferences.Editor edit;

    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    String re=(String) msg.obj;
                    ivTou.setImageBitmap(bm);
                    requsetUserPhoto(re);
                    break;
            }
        }
    }
            ;
    private Bitmap bm;
    private View loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLogin = PreferenceHelper.getUserLogin(this);
        setContentView(R.layout.person_data_view);

        getPerson();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void uIViewInit() throws ParseException {
        TextView tvMiddle = (TextView) findViewById(R.id.title);
        tvMiddle.setText("个人信息");

        ImageView imageView=(ImageView)findViewById(R.id.left_image);
        imageView.setBackgroundResource(R.drawable.titlebar_left);
        imageView.setOnClickListener(this);

        reXing = (RelativeLayout) findViewById(R.id.reXing);
        reXing.setOnClickListener(this);
        etXing = (TextView) findViewById(R.id.etXing);
        /*rl_xx =(RelativeLayout)findViewById(R.id.rl_xx);
        rl_xx.setOnClickListener(this);
        txt_xue =(TextView)findViewById(R.id.txt_xue);*/

        rl_birthday =(RelativeLayout)findViewById(R.id.rl_birthday);
        rl_birthday.setOnClickListener(this);
        txt_birthday =(TextView)findViewById(R.id.txt_birthday);



        //昵称
        tx_name =(TextView) findViewById(R.id.tx_name);
        tx_name.setOnClickListener(this);

        rl_pass =(RelativeLayout)findViewById(R.id.rl_pass);
        rl_pass.setOnClickListener(this);

        et_pwd =(TextView)findViewById(R.id.et_pwd);


        //头像
        ivTou =(ImageView)findViewById(R.id.ivTou);
        ivTou.setOnClickListener(this);
        contentView = this.getLayoutInflater().inflate(R.layout.mydataactivitypaizhao, null);
        tvPaiZhao = (TextView) contentView.findViewById(R.id.tv_paizhao);
        tvShouJi = (TextView) contentView.findViewById(R.id.tv_shouji);
        tvQuXiao = (TextView) contentView.findViewById(R.id.tv_quxiao);
        tvPaiZhao.setOnClickListener(this);
        tvShouJi.setOnClickListener(this);
        tvQuXiao.setOnClickListener(this);

        initPopupWindow();
        lp = PersonalDataActivity.this.getWindow().getAttributes();

        loading =(View)findViewById(R.id.loading);

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
                PersonalDataActivity.this.getWindow().setAttributes(lp);
                pop.dismiss();
            }
        });

    }

    @Override
    protected void uIViewDataApply() {
        //每天
        YearmonthDay = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        YearmonthDay.setTime(new Date());
        YearmonthDay.setCyclic(true);
        //时间选择后回调
        YearmonthDay.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(String date) {
                requestUserBirthday(date,userPhone);
            }
        });

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

    /*
    * 获取个人信息
    * */
    public void getPerson(){
        if (ConnectionUtil.isConnected(getApplicationContext())) {
            String userPhone = PreferenceHelper.getUserLogin(GlobalApplication.instance).getString("userPhone", "");
            HttpRequestUtil.HttpRequestByGet(Config.FIND_USER+"?mobile="+userPhone,this,PERSON);
        } else {
            Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }





    //上传文件
    private void toUploadFile(String mobile, String logofileDir) {
        LogUtil.e("电话："+mobile+"  照相："+logofileDir);
        Map<String,String> map=new HashMap<>();
        map.put("mobile",mobile);
        final String fileKey = "file";
        final UploadUtil uploadUtil = UploadUtil.getInstance();
        uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态

        loading.setVisibility(View.VISIBLE);

        uploadUtil.uploadFile(logofileDir, fileKey, Config.UPLOAD_USERICON, map);
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

    private void requsetUserPhoto(String result) {
        loading.setVisibility(View.GONE);
        LogUtil.e("result="+result);
        JSONObject jsonObject2 = null;
        try {
            jsonObject2 = new JSONObject(result);
            String message = jsonObject2.get("message").toString();
            String ret_code = jsonObject2.get("code").toString();
            String userIcon=jsonObject2.getJSONObject("data").getString("userIcon");

            if (ret_code.equals("0")) {
                edit = userLogin.edit();
                edit.putString("userIcon",userIcon);
                edit.commit();
                Thread.sleep(100);

                Toast.makeText(ApplicationController.getInstance(), "头像上传成功", Toast.LENGTH_LONG).show();
                getPerson();
            } else {
                AddLengthFilter.CustomTimeToast(GlobalApplication.instance,message);
                //Toast.makeText(ApplicationController.getInstance(), message, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            if (Config.DEBUG) {
                e.printStackTrace();
            }
        }
    }
    /*
    * 昵称的修改
    * */
    private void requestNick(String mobile,String nickname){
        if (ConnectionUtil.isConnected(getApplicationContext())) {

            HttpRequestUtil.HttpRequestByGet(Config.USERNICKNAME_URL+"?nickName="+nickname+"&mobile="+mobile,this,NICK);
            LogUtil.e("mobile:"+mobile+"  nickname:"+nickname);
        } else {
            Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    /*
    *性别的修改
    * */
    private void requestUserGender(String mobile,String gender){
        if (ConnectionUtil.isConnected(getApplicationContext())) {

            HttpRequestUtil.HttpRequestByGet(Config.USERGENDER_URL+"?gender="+gender+"&mobile="+mobile,this,GENDER);
        } else {
            Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    /*
   *性别的修改
   * */
    private void requestUserBirthday(String birthday,String mobile){
        if (ConnectionUtil.isConnected(getApplicationContext())) {

            HttpRequestUtil.HttpRequestByGet(Config.USERBIRTHDAY_URL+"?birthday="+birthday+"&mobile="+mobile,this,BIRTHDAY);
        } else {
            Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reXing:
                //实例化SelectPicPopupWindow
                SelectItem[] PLANETS2 = new SelectItem[]{new SelectItem("男"), new SelectItem("女")};
                menuWindow2 = new SelectPicPopupWindow(PersonalDataActivity.this, Arrays.asList(PLANETS2));
                //显示窗口
                menuWindow2.showAtLocation(PersonalDataActivity.this.findViewById(R.id.main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                menuWindow2.setYourListener(new SelectPicPopupWindow.YourListener() {
                    @Override
                    public void onSomeChange(SelectItem info, int i) {
                        requestUserGender(userPhone,info.name);

                    }
                });
                break;

            case R.id.rl_birthday:
                YearmonthDay.show();
                break;
            case R.id.ivTou:
                if (pop.isShowing()) {
                    pop.dismiss();
                } else {
                    pop.showAtLocation(PersonalDataActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    pop.update();
                }
                break;
            case R.id.tv_quxiao://正面取消
                lp.alpha = 1f;
                PersonalDataActivity.this.getWindow().setAttributes(lp);
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
            case R.id.left_image:
                finish();
                break;

            case R.id.rl_pass:
                Intent intent1=new Intent(this,UpdataPasActivity.class);
                startActivity(intent1);
                break;
            case R.id.tx_name://昵称
                Intent intent2=new Intent(this,WriteActivity.class);
                startActivityForResult(intent2,NAME);
                break;
        }
    }

    public void takePhone(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            //如果没有授权，则请求授权
            ActivityCompat.requestPermissions(this,
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

        mFolder = new File(PersonalDataActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "bCache");

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
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE2);

        }else {
            getImgFromLoCal();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK && requestCode == PIC_FROM_CAMERA_LOGO) {

            // 调用系统方法获取到的是被压缩过的图片，通过自定义路径轻松获取原始图片。

            /*bm = BitmapFactory.decodeFile(mFolder.getAbsolutePath()
                    + File.separator + mImgName);*/
            //获取到图片的地址
            logofileDir=zhenFolder.getAbsolutePath();

            bm =BitmapFactory.decodeFile(logofileDir);
            //ivTou.setImageBitmap(bm);
            //上传图片
            toUploadFile(userPhone,logofileDir);
            //ivTou.setScaleType(ImageView.ScaleType.FIT_XY);
            //ivTou.setVisibility(View.VISIBLE);
        }

        if (resultCode == RESULT_OK && requestCode == PIC_FROM＿LOCALPHOTO_LOGO) {
            try {
                if (data != null) {
                    // 获取本地相册图片。

                    Uri uri = data.getData();
                    //获取图片的本地地址
                    logofileDir= GetPathFromUri4kitkat.getPath(this,uri);

                    //logofileDir=getAbsoluteImagePath(uri);

                    ContentResolver cr = getContentResolver();

                   /* BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inSampleSize = 4;*/

                    bm = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    //ivTou.setImageBitmap(bm);
                    //上传图片
                    toUploadFile(userPhone,logofileDir);
                   /* img_z.setScaleType(ImageView.ScaleType.FIT_XY);
                    img_z.setVisibility(View.VISIBLE);*/
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if(requestCode==NAME){
            final String name =data.getStringExtra("name");
            LogUtil.e("昵称："+name);
            if(!TextUtils.isEmpty(name)){
                requestNick(userPhone, name);
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getImgFromZhen();
            } else
            {
                // Permission Denied
                Toast.makeText(PersonalDataActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }


        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE2)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getImgFromLoCal();
            } else
            {
                // Permission Denied
                Toast.makeText(PersonalDataActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onDataDelivered(int taskId, String data) throws JSONException {
        switch (taskId){
            case PERSON:
                LogUtil.e(data.toString());
                JSONObject object1=new JSONObject(data);
                userPhone =object1.getJSONObject("data").getString("userPhone");
                String userIcon=object1.getJSONObject("data").getString("userIcon");
                String nickName=object1.getJSONObject("data").getString("nickName");
                String  userGender=object1.getJSONObject("data").getString("userGender");
                String  userBirthday=object1.getJSONObject("data").getString("userBirthday");


                String status_code=object1.getString("code");
                if("0".equals(status_code)){

                    ImageLoader.getInstance().displayImage(Config.BASE_URL+userIcon,ivTou);//加载头像

                    tx_name.setText(nickName);
                    etXing.setText(userGender);
                    txt_birthday.setText(ConvertDemo.timestampDate(userBirthday));



                }
                break;
            case MODIFYUSER:
                LogUtil.e(data.toString());
                JSONObject object2=new JSONObject(data);
                String code=object2.getString("code");
                String message=object2.getString("message");
                if("0".equals(code)){
                    AddLengthFilter.CustomTimeToast(GlobalApplication.instance,message);
                    //Toast.makeText(GlobalApplication.instance,message,Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case PIC:
                LogUtil.e("图片："+data.toString());
                break;
            case NICK:
                LogUtil.e(data.toString());
                JSONObject object=new JSONObject(data);
                String codee=object.getString("code");
                String messagee=object.getString("message");
                String name=object.getJSONObject("data").getString("nickName");
                if("0".equals(codee)){
                    tx_name.setText(name);
                    edit = userLogin.edit();
                    edit.putString("nickName", name);
                    edit.commit();
                }

                break;
            case BIRTHDAY:
                LogUtil.e(data.toString());
                JSONObject obj=new JSONObject(data);
                String cd=obj.getString("code");
                String msg=obj.getString("message");
                String Birthday=obj.getJSONObject("data").getString("userBirthday");
                if("0".equals(cd)){
                    AddLengthFilter.CustomTimeToast(GlobalApplication.instance,msg);
                    //Toast.makeText(GlobalApplication.instance,msg,Toast.LENGTH_SHORT).show();
                    txt_birthday.setText(ConvertDemo.timestampDate(Birthday));
                }
                break;
            case GENDER:
                LogUtil.e(data.toString());
                JSONObject o=new JSONObject(data);
                String state=o.getString("code");
                String mes=o.getString("message");
                String gender=o.getJSONObject("data").getString("userGender");
                if("0".equals(state)){
                    AddLengthFilter.CustomTimeToast(GlobalApplication.instance,mes);
                    //Toast.makeText(GlobalApplication.instance,mes,Toast.LENGTH_SHORT).show();
                    etXing.setText(gender);
                }


                break;
        }
    }

    @Override
    public void onErrorHappened(int taskId, String errorCode, String errorMessage) {

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
