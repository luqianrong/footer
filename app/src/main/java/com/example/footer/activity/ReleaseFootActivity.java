package com.example.footer.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.footer.R;
import com.example.footer.adapter.GridViewAddImgesAdpter;
import com.example.footer.application.ApplicationController;
import com.example.footer.config.Config;
import com.example.footer.framework.BaseActivity;
import com.example.footer.utils.LogUtil;
import com.example.footer.utils.NameLengthFilter;
import com.example.footer.utils.PreferenceHelper;
import com.example.footer.utils.UploadUtil;

import net.bither.util.NativeUtil;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 乃军 on 2017/11/17. 照相机是单选的
 */

public class ReleaseFootActivity extends BaseActivity implements View.OnClickListener,UploadUtil.OnUploadProcessListener{
    private GridView gw;
    private List<Map<String, Object>> datas;
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private Dialog dialog;
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;
    private final String IMAGE_DIR = Environment.getExternalStorageDirectory() + "/gridview/";
    /* 头像名称 */
    private final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private TextView tvRight;
    private EditText etContent;
    private EditText et_1;
    private EditText et_2;
    private EditText et_3;
    private Toast toast;
    private SharedPreferences userLogin;
    private Handler uiHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(ApplicationController.getInstance(),"发布成功",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release);
        userLogin = PreferenceHelper.getUserLogin(this);

    }

    @Override
    protected void uIViewInit() throws ParseException {
        TextView tvMiddle = (TextView)findViewById(R.id.title);
        tvMiddle.setText("发布足迹");

        ImageView imageView=(ImageView)findViewById(R.id.left_image);
        imageView.setBackgroundResource(R.drawable.titlebar_left);
        imageView.setOnClickListener(this);

        tvRight = (TextView) findViewById(R.id.titleBar_Right_tv);
        tvRight.setText("发送");
        tvRight.setOnClickListener(this);

        etContent =(EditText)findViewById(R.id.etContent);
        et_1 =(EditText)findViewById(R.id.et_1);
        et_2 =(EditText)findViewById(R.id.et_2);
        et_3 =(EditText)findViewById(R.id.et_3);

        /**
         * 长度监听过滤
         *
         * @version v1.0
         * EditText输入中文和英文长度监听，中文按两个字符，英文按一个字符，监听输入的长度，在你需要调用的地方添加：
         * AddLengthFilter.lengthFilter(this, nameEt, 32, "提示信息"));
         * @author
         */
        NameLengthFilter nameLengthFilter=new NameLengthFilter(10);
        et_1.setFilters(new InputFilter[]{nameLengthFilter});
        et_2.setFilters(new InputFilter[]{nameLengthFilter});
        et_3.setFilters(new InputFilter[]{nameLengthFilter});



        gw = (GridView) findViewById(R.id.gw);
        datas = new ArrayList<>();
        //horizontal_layout();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, this);
        gw.setAdapter(gridViewAddImgesAdpter);
        gw.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                showdialog();
            }
        });

        et_2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                String  tag1=et_1.getText().toString().trim();
                if(TextUtils.isEmpty(tag1)){
                    CustomTimeToast("请先设置第一足迹");
                    return true;
                }
                return false;
            }
        });
        et_3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                String  tag1=et_1.getText().toString().trim();
                String  tag2=et_2.getText().toString().trim();
                if(TextUtils.isEmpty(tag1)){
                    CustomTimeToast("请先设置第一足迹");
                    return true;
                }else if(TextUtils.isEmpty(tag2)){
                    CustomTimeToast("请先设置第二足迹");
                    return true;
                }
                return false;
            }
        });

        requestWritePermission();
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

    /**
     * 选择图片对话框
     */
    public void showdialog() {
        View localView = LayoutInflater.from(this).inflate(
                R.layout.dialog_add_picture, null);
        TextView tv_camera = (TextView) localView.findViewById(R.id.tv_camera);
        TextView tv_gallery = (TextView) localView.findViewById(R.id.tv_gallery);
        TextView tv_cancel = (TextView) localView.findViewById(R.id.tv_cancel);
        dialog = new Dialog(this, R.style.custom_dialog);
        dialog.setContentView(localView);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        // 设置全屏
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth(); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        tv_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        tv_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 拍照
                camera();
            }
        });

        tv_gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 从系统相册选取照片
                gallery();
            }
        });
    }

    /**
     * 拍照
     */
    public void camera() {
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {

            File dir = new File(IMAGE_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }
            tempFile = new File(dir,
                    System.currentTimeMillis() + "_" + PHOTO_FILE_NAME);
            //从文件中创建uri
            Uri uri = Uri.fromFile(tempFile);
            Intent intent = new Intent();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(intent.CATEGORY_DEFAULT);
            // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
            startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
        } else {
            Toast.makeText(this, "未找到存储卡，无法拍照！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断sdcard是否被挂载
     */
    public boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    /**
     * 从相册获取2
     */
    public void gallery() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PHOTO_REQUEST_GALLERY) {
                // 从相册返回的数据
                if (data != null) {
                    // 得到图片的全路径
                    Uri uri = data.getData();
                    String[] proj = {MediaStore.Images.Media.DATA};
                    //好像是android多媒体数据库的封装接口，具体的看Android文档
                    Cursor cursor = managedQuery(uri, proj, null, null, null);
                    //按我个人理解 这个是获得用户选择的图片的索引值
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    //将光标移至开头 ，这个很重要，不小心很容易引起越界
                    cursor.moveToFirst();
                    //最后根据索引值获取图片路径
                    String path = cursor.getString(column_index);


                    uploadImage(path);
                }

            } else if (requestCode == PHOTO_REQUEST_CAREMA) {
                if (resultCode != RESULT_CANCELED) {
                    // 从相机返回的数据
                    if (hasSdcard()) {
                        if (tempFile != null) {
                            uploadImage(tempFile.getPath());
                        } else {
                            Toast.makeText(this, "相机异常请稍后再试！", Toast.LENGTH_SHORT).show();
                        }

                        Log.i("images", "拿到照片path=" + tempFile.getPath());
                    } else {
                        Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0xAAAAAAAA) {
                photoPath(msg.obj.toString());
            }

        }
    };

    /**
     * 上传图片
     *
     * @param path
     */
    private void uploadImage(final String path) {
        new Thread() {
            @Override
            public void run() {
                if (new File(path).exists()) {
                    Log.d("images", "源文件存在" + path);
                } else {
                    Log.d("images", "源文件不存在" + path);
                }

                File dir = new File(IMAGE_DIR);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                final File file = new File(dir + "/temp_photo" + System.currentTimeMillis() + ".jpg");
                //Toast.makeText(MainActivity.this,file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
                Log.d("images", "path:"+path);

                NativeUtil.compressBitmap(path, file.getAbsolutePath(), 50);
                if (file.exists()) {
                    Log.d("images", "压缩后的文件存在" + file.getAbsolutePath());
                } else {
                    Log.d("images", "压缩后的不存在" + file.getAbsolutePath());
                }
                Message message = new Message();
                message.what = 0xAAAAAAAA;
                //获取图片的绝对路劲
                message.obj = file.getAbsolutePath();
                handler.sendMessage(message);

            }
        }.start();

    }

    public void photoPath(String path) {
        Map<String,Object> map=new HashMap<>();
        map.put("path",path);
        datas.add(map);
        for(int i=0;i<datas.size();i++){
            LogUtil.e("-图片地址-"+datas.get(i).toString());

        }
        gridViewAddImgesAdpter.notifyDataSetChanged();
    }

    //上传文件
    private void toUploadFile(String spoorContent,String spoorTagOne,String spoorTagTwo,String spoorTagThree,String mobile,List<String> logofileDir) {

        Map<String,String> map=new HashMap<>();
        map.put("spoorContent",spoorContent);
        map.put("spoorTagOne",spoorTagOne);
        map.put("spoorTagTwo",spoorTagTwo);
        map.put("spoorTagThree",spoorTagThree);
        map.put("mobile",mobile);
        final String fileKey = "file";
        final UploadUtil uploadUtil = UploadUtil.getInstance();
        uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态
        //uploadUtil.uploadFileList(logofileDir, fileKey, Config.INSERTSPOORR_URL, map);
        uploadUtil.UploadFiles(logofileDir, fileKey, Config.INSERTSPOORR_URL, map);
        uploadUtil.setYourListener(new UploadUtil.YourListener() {
            @Override
            public void onSomeChange(String re, int i) {
                LogUtil.e("i:"+i);
                if (i == 1) {
                    Message message=new Message();
                    message.what=1;
                    uiHandler.sendMessage(message);
                }

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void requestWritePermission(){
        if (ActivityCompat.checkSelfPermission(ReleaseFootActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ReleaseFootActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else if(ActivityCompat.checkSelfPermission(ReleaseFootActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ReleaseFootActivity.this, new String[]{Manifest.permission.CAMERA},1);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.left_image:
                finish();
                break;
            case R.id.titleBar_Right_tv:
                List<String> pics=new ArrayList<>();
                for(int i=0;i<datas.size();i++){
                    String path=datas.get(i).get("path").toString();
                    pics.add(path);
                }
                LogUtil.e("图片地址："+pics.toString());

                String mobile=userLogin.getString("userPhone", "");
                String content=etContent.getText().toString().trim();
                String e1=et_1.getText().toString().trim();
                String e2=et_2.getText().toString().trim();
                String e3=et_3.getText().toString().trim();


                if(TextUtils.isEmpty(content)&&pics.size()==0){

                    CustomTimeToast("内容或图片不能为空");

                }else if(TextUtils.isEmpty(e1)){

                    CustomTimeToast("请先设置第一足迹");

                }else if(!TextUtils.isEmpty(e3)){

                    if(TextUtils.isEmpty(e2)){

                        CustomTimeToast("请先设置第二足迹");

                    }else{
                        toUploadFile(content,e1,e2,e3,mobile,pics);
                    }
                }else {
                    toUploadFile(content,e1,e2,e3,mobile,pics);
                }
                break;
        }
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

   /* public void showToast(String msg) {

        if (toast == null) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            toast.cancel();//关闭吐司显示
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        }

        toast.show();//重新显示吐司
    }*/
   private void CustomTimeToast(String msg) {
                final Toast toast = Toast.makeText(this,msg,
                               Toast.LENGTH_LONG);
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
             @Override
            public void run() {
                                toast.show();
                           }
         }, 0, 500);// 1000表示点击按钮之后，Toast延迟1000ms后显示
               new Timer().schedule(new TimerTask() {
             @Override
             public void run() {
                                toast.cancel();
                                timer.cancel();
                            }
         }, 1000);// 1000表示Toast显示时间为1秒
     }

}
