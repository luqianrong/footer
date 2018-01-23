package com.example.footer.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.inter.ResponseStringDataListener;
import com.example.footer.R;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.fragment.MeFragment;
import com.example.footer.fragment.RailingFragment;
import com.example.footer.fragment.ZhuYeFragment;
import com.example.footer.framework.BaseActivity;
import com.example.footer.model.Version;
import com.example.footer.network.HttpRequestUtil;
import com.example.footer.utils.LogUtil;
import com.example.footer.utils.ParseJson;
import com.example.footer.utils.PreferenceHelper;
import com.google.gson.Gson;
import com.hsg.sdk.common.util.ConnectionUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import static android.provider.MediaStore.getVersion;
import static cn.jpush.android.api.JPushInterface.a.v;
import static com.example.footer.activity.WelcomeActivity.getVersionName;
import static com.example.footer.activity.WelcomeActivity.getVersionCode;

public class MainActivity extends BaseActivity implements View.OnClickListener,ResponseStringDataListener{
    private final int REQUEST_VERSION=0x001,UPDATEVERSION=0x002;
    private FragmentManager supportFragmentManager;
    private RelativeLayout  main;
    private RelativeLayout rlHome;
    private RelativeLayout rl_xj;
    private RelativeLayout rl_fd;
    private RelativeLayout rl_wo;
    ImageView ivHome;
    ImageView img_xj;
    ImageView fd_img;
    ImageView w_img;
    TextView tvHome;
    TextView tx_xj;
    TextView tx_fd;
    TextView tx_w;
    private int curTabId;

    private Fragment currentFragment;
    private FragmentManager fg;
    private FragmentTransaction ft;
    boolean isLogin;

    private SharedPreferences userLogin;
    private Handler handler = new Handler();
    ProgressDialog pBar;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportFragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_main);
        //url=https://www.pgyer.com/FJRe
        updateversion("1","第一版","https://www.pgyer.com/FJRe");
    }

    @Override
    protected void uIViewInit() throws ParseException {
        userLogin = PreferenceHelper.getUserLogin(this);
        isLogin = userLogin.getBoolean("login", false);

        main=(RelativeLayout)findViewById(R.id.main);

        //首页布局
        rlHome = (RelativeLayout) findViewById(R.id.rlHome);
        //首页图片
        ivHome = (ImageView) findViewById(R.id.ivHome);
        //首页文字
        tvHome = (TextView) findViewById(R.id.tvHome);

        //寻迹
        rl_xj = (RelativeLayout) findViewById(R.id.rl_xj);
        //寻迹
        img_xj = (ImageView) findViewById(R.id.img_xj);
        //寻迹
        tx_xj = (TextView) findViewById(R.id.tx_xj);

        /*//发现的布局
        rl_fd = (RelativeLayout) findViewById(R.id.rl_fd);
        //发现
        fd_img = (ImageView) findViewById(R.id.fd_img);
        //发现
        tx_fd = (TextView) findViewById(R.id.tx_fd);
*/
        //我的布局
        rl_wo = (RelativeLayout) findViewById(R.id.rl_wo);
        //我的pic
        w_img = (ImageView) findViewById(R.id.w_img);
        //我的文字
        tx_w = (TextView) findViewById(R.id.tx_w);


        rlHome.setOnClickListener(this);
        rl_xj.setOnClickListener(this);
        //rl_fd.setOnClickListener(this);
        rl_wo.setOnClickListener(this);

        ivHome.setEnabled(false);
        img_xj.setEnabled(true);
        //fd_img.setEnabled(true);
        w_img.setEnabled(true);

        tvHome.setEnabled(false);
        tx_xj.setEnabled(true);
        //tx_fd.setEnabled(true);
        tx_w.setEnabled(true);

        //在这里做版本判断的操作
        requsetVersion();

        switchTab(R.id.rlHome);
    }

    public void updateversion(String version,String description,String url){
        if(ConnectionUtil.isConnected(this)){

            HttpRequestUtil.HttpRequestByGet(Config.UPDATEVERSION+"?version="+version+"&description="+description+"&url="+url, this,UPDATEVERSION);
        }else
        {
            Toast.makeText(GlobalApplication.instance,R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    //获取当前网络的版本号
    public void requsetVersion(){
        if(ConnectionUtil.isConnected(this)){
            HttpRequestUtil.HttpRequestByGet(Config.VERSION_URL,this, REQUEST_VERSION);
        }else
        {
            Toast.makeText(GlobalApplication.instance,R.string.network_error, Toast.LENGTH_SHORT).show();
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


    @Override
    public void onClick(View view) {
        ft = supportFragmentManager.beginTransaction();
        switch (view.getId()) {
            //首页
            case R.id.rlHome:
                if (curTabId != R.id.rlHome) {
                    switchTab(R.id.rlHome);
                }

                break;
            //寻迹
            case R.id.rl_xj:
                   if (curTabId != R.id.rl_xj) {
                        switchTab(R.id.rl_xj);
                    }

                break;
            //发现
            /*case R.id.rl_fd:
                if (curTabId != R.id.rl_fd) {
                    switchTab(R.id.rl_fd);
                }
                break;*/
            //我的
            case R.id.rl_wo:
                boolean isLogin = userLogin.getBoolean("login", false);

                if (!isLogin) {
                    Intent it = new Intent(this, UserLoginActivity.class);
                    startActivity(it);

                }else{
                    if (curTabId != R.id.rl_wo) {
                        switchTab(R.id.rl_wo);
                    }
                }

                break;
        }
    }

    public void switchTab(int id) {

        switch (id) {
            case R.id.rlHome://主页

                currentFragment = switchFragment(supportFragmentManager, R.id.flContainer,currentFragment , ZhuYeFragment.class, null, false);
                //currentFragment = switchFragment(supportFragmentManager, R.id.flContainer,currentFragment , SplashFragment.class, null, false);

                curTabId = R.id.rlHome;

                ivHome.setEnabled(false);
                img_xj.setEnabled(true);
                //fd_img.setEnabled(true);
                w_img.setEnabled(true);

                tvHome.setEnabled(false);
                tx_xj.setEnabled(true);
                //tx_fd.setEnabled(true);
                tx_w.setEnabled(true);

                break;
            case R.id.rl_xj://寻迹

                currentFragment=switchFragment(supportFragmentManager,R.id.flContainer,currentFragment,RailingFragment.class,null,false);


                curTabId = R.id.rl_xj;
                img_xj.setEnabled(false);
                ivHome.setEnabled(true);
                //fd_img.setEnabled(true);
                w_img.setEnabled(true);

                tx_xj.setEnabled(false);
                tvHome.setEnabled(true);
                //tx_fd.setEnabled(true);
                tx_w.setEnabled(true);

                break;
            /*case R.id.rl_fd://发现
                currentFragment=switchFragment(supportFragmentManager,R.id.flContainer,currentFragment,DiscoveryFrament.class,null,false);
                curTabId = R.id.rl_fd;

                fd_img.setEnabled(false);
                w_img.setEnabled(true);
                ivHome.setEnabled(true);
                img_xj.setEnabled(true);

                tx_fd.setEnabled(false);
                tx_w.setEnabled(true);
                tvHome.setEnabled(true);
                tx_xj.setEnabled(true);

                break;*/
            case R.id.rl_wo://我的

                currentFragment=switchFragment(supportFragmentManager,R.id.flContainer,currentFragment,MeFragment.class,null,false);
                curTabId = R.id.rl_wo;


                w_img.setEnabled(false);
                ivHome.setEnabled(true);
                img_xj.setEnabled(true);
                //fd_img.setEnabled(true);

                tx_w.setEnabled(false);
                tvHome.setEnabled(true);
                tx_xj.setEnabled(true);
                //tx_fd.setEnabled(true);

                break;


        }

    }

    private Fragment switchFragment(FragmentManager fragmentManager, int container,
                                    Fragment currentFragment, Class<? extends Fragment> newFragment,
                                    Bundle args, boolean addToBackStack) {
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        final String tag = newFragment.getSimpleName();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            if (fragment != currentFragment) {
                if (currentFragment != null) {
                    transaction.hide(currentFragment);
                }
                transaction.show(fragment);
                if (addToBackStack) {
                    transaction.addToBackStack(null);
                }
                transaction.commitAllowingStateLoss();
                getSupportFragmentManager().executePendingTransactions();
            } else {
                fragment.getArguments().putAll(args);
            }
            return fragment;
        } else {
            try {
                fragment = newFragment.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // 为新的Fragment添加参数
        if (fragment != null) {
            if (args != null && !args.isEmpty()) {
                final Bundle bundle = fragment.getArguments();
                if (bundle != null) {
                    bundle.putAll(args);
                } else {
                    fragment.setArguments(args);
                }
            }
        }
        // 显示新的Fragment
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        transaction.add(container, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();
        return fragment;
    }


    @Override
    public void onDataDelivered(int taskId, String data) throws JSONException {
        switch(taskId){
            case UPDATEVERSION:
                LogUtil.e("修改版本更新的信息:"+data);
                break;
            case REQUEST_VERSION:
                JSONObject jsonObject=new JSONObject(data);
                String data1=jsonObject.getString("data");
                Version version= ParseJson.parseJson(data1, Version.class);

                String code_name=version.getVersion();//获取版本号
                String desc=version.getDescription();//描述
                String url=version.getUrl();//获取软件的下载地址

                repareVersion(code_name,desc,url);
                break;
        }
    }

    @Override
    public void onErrorHappened(int taskId, String errorCode, String errorMessage) {

    }

    public void repareVersion(String code_name,String desc,String url) {
        int code = getVersionCode(this);
        String version_code=String.valueOf(code);

        if (!(version_code.equals(code_name))) {
            //获取setting的xml文件，清空里面的所有引导所保留的状态
            SharedPreferences sharedPreferences = this.getSharedPreferences(
                    "Setting", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();

            //这里做安装新app的调用
            doNewVersionUpdate(code_name,desc,url); // 更新新版本
        } else {
            //Toast.makeText(GlobalApplication.instance,"当前是最新版本",Toast.LENGTH_SHORT).show();
        }
    }

    private void doNewVersionUpdate(String code_name, String desc,final String url) {

        View vv = LayoutInflater.from(GlobalApplication.instance).inflate(R.layout.version_dialog, null);
        final Dialog dialogt = new Dialog(this, R.style.dialogt);
        dialogt.setContentView(vv);
        dialogt.show();
        TextView version_num=(TextView)vv.findViewById(R.id.verson_id);//版本号
        version_num.setText(code_name);//版本号

        TextView txt_desc=(TextView) vv.findViewById(R.id.txt_desc);//描述
        txt_desc.setText(desc);

        Button true01=(Button) vv.findViewById(R.id.update_true);//立即更新
        true01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogt.dismiss();
                /**
                 * setAction方法设置指定那个浏览器启动：如
                 * 1. 系统默认浏览器 android.intent.action.VIEW
                 **/
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
                startActivity(intent);
            }
        });
/**
            }
        });
        /*true01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pBar = new ProgressDialog(MainActivity.this);
                pBar.setTitle("正在下载");
                pBar.setMessage("请稍候...");
                pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                //下载
                downFile(url);//url地址是将app给后台服务器由后台来生成下载url地址
            }
        });*/
        Button cancel=(Button) vv.findViewById(R.id.cancel);//稍后再说
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogt.dismiss();

            }
        });

    }


}
