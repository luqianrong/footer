package com.example.footer.framework;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.footer.R;
import com.example.footer.application.ApplicationController;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.library.PullToRefreshBase;
import com.example.footer.utils.AppMainfestInfo;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.ParseException;




/**
 * base activity
 */

public abstract class BaseActivity extends FragmentActivity {
    protected Handler mHandler = new Handler();
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int color = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Boolean transparentStatus = null;
            try {
                transparentStatus = AppMainfestInfo.getActivtyBoolInfo(this, "TRANS_STATUS");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (null != transparentStatus && transparentStatus) {
                color = getResources().getColor(R.color.transparent);
            } else {
                color = getResources().getColor(R.color.colorblack);
            }
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setTintColor(color);
        }
        activityCreate();

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            parentView.setFitsSystemWindows(true);
        }
        try {
            uIViewInit();
        } catch (ParseException e) {
            if(Config.DEBUG){
                e.printStackTrace();
            }
        }
        uIViewDataApply();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        onWindowHasFocus(hasFocus);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    /**
     * activity view 初始化
     */
    protected abstract void uIViewInit() throws ParseException;

    /**
     * activity view 初始化完成调用
     */
    protected abstract void uIViewDataApply();

    /**
     * activity 创建
     */
    protected abstract void activityCreate();

    /**
     * activity 销毁
     */
    protected abstract void activityDestroy();

    /**
     * window 是否获取焦点
     *
     */

    protected abstract void onWindowHasFocus(boolean hasFocus);

    protected void onPullDownRefresh(){}

    protected void onPullUpRefresh(){}


    public class MyOnRefreshListener implements PullToRefreshBase.OnRefreshListener2 {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onPullDownRefresh();
                }
            }, 1000);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onPullUpRefresh();
                }
            }, 1000);
        }
    }




}
