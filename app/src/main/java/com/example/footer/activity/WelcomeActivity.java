package com.example.footer.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.footer.R;
import com.example.footer.config.Config;
import com.example.footer.utils.PrefUtils;
import com.example.footer.utils.PreferenceHelper;
import com.hsg.sdk.common.util.ConnectionUtil;

import java.util.Set;


/**
 * Created by dell on 2015/12/3.
 */
public class WelcomeActivity extends Activity {
    private final int REQUEST_VERSION=0x001;
    SharedPreferences userLogin;
    private Handler mHandler = new Handler();
    private Handler handler = new Handler();
    ProgressDialog pBar;


    //private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);


        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

        //requsetVersion();这个方法主要是用于做版本检测和升级，因为是在主页做，所以在欢迎页已取消

        getWindow().setFlags(flag, flag);

//        setPushAliasAndTag();


    }


    @Override
    protected void onStart() {
        super.onStart();
        //用的时候在加回来
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                jumpNextPage();
                //startActivity(new Intent(WelcomeActivity.this, SplashActivity.class));
                //finish();
            }
        }, 3000);

    }

    /**
     * 跳转下一个页面
     */
    private void jumpNextPage() {
        // 判断之前有没有显示过新手引导
        boolean userGuide = PrefUtils.getBoolean(this, "is_user_guide_showed", false);
        int code = getVersionCode(this);
        if (!userGuide) {
            // 跳转到新手引导页
            startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
        } else {
            Intent in = null;
            userLogin = PreferenceHelper.getUserLogin(this);
            in = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(in);
        }
        PrefUtils.setVersion(this, "welcome_version", code);
        finish();
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            if (Config.DEBUG) {
                e.printStackTrace();
            }
        }

        return pi;
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


}
