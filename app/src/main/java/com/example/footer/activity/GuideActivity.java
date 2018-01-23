package com.example.footer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.footer.R;
import com.example.footer.adapter.GuideAdapter;
import com.example.footer.utils.DensityUtils;
import com.example.footer.utils.LogUtil;
import com.example.footer.utils.PrefUtils;
import com.example.footer.utils.PreferenceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2015/12/3.
 */
public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager vpGuide;
    private Button btStart;
    private LinearLayout ll_Point;
    private View viewRedPoint;
    private List<ImageView> list = new ArrayList<>();
    private int mPointWidth;// 圆点间的距离
    private int[] guide = new int[]{R.drawable.two,R.drawable.three,R.drawable.four,R.drawable.five};
    SharedPreferences userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = getWindow();
        window.setFlags(flag, flag);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        initView();
        initData();

    }

    protected void initView(){
        vpGuide = (ViewPager) findViewById(R.id.vpGuide);
        btStart = (Button) findViewById(R.id.bt_start);
        btStart.setOnClickListener(this);
        //ll_Point = (LinearLayout) findViewById(R.id.ll_point);
        //viewRedPoint = findViewById(R.id.view_red_point);
    }

    protected void initData() {
        for (int i = 0; i < guide.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(guide[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            //加图片数组
            list.add(iv);
        }
        /*for (int i = 0; i < guide.length; i++) {
            View point = new View(this);
            point.setBackgroundResource(R.drawable.guide_point_shape);
            int x= DensityUtils.dip2px(this,5);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(x,x);
            if (i > 0) {
                params.leftMargin = 10;
            }
            point.setLayoutParams(params);// 设置圆点的大小
            ll_Point.addView(point);// 将圆点添加给线性布局
        }
        // 获取视图树, 对layout结束事件进行监听
        ll_Point.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    // 当layout执行结束后回调此方法
                    @Override
                    public void onGlobalLayout() {
                        System.out.println("layout 结束");
                        ll_Point.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        mPointWidth = ll_Point.getChildAt(1).getLeft() - ll_Point.getChildAt(0).getLeft();
                        System.out.println("圆点距离:" + mPointWidth);
                    }
                });*/

        vpGuide.setAdapter(new GuideAdapter(list, this));
        vpGuide.setOnPageChangeListener(this);
        btStart.setOnClickListener(this);

    }
    // 滑动事件
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // System.out.println("当前位置:" + position + ";百分比:" + positionOffset
        // + ";移动距离:" + positionOffsetPixels);
       /* int len = (int) (mPointWidth * positionOffset) + position
                * mPointWidth;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewRedPoint
                .getLayoutParams();// 获取当前点的布局参数
        params.leftMargin = len;// 设置左边距

        viewRedPoint.setLayoutParams(params);// 重新给小点设置布局参数*/
    }

    // 某个页面被选中
    @Override
    public void onPageSelected(int position) {
        if (position == guide.length - 1) {// 最后一个页面
            btStart.setVisibility(View.VISIBLE);// 显示开始体验的按钮
        } else {
            btStart.setVisibility(View.INVISIBLE);
        }
    }

    // 滑动状态发生变化
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_start:
                // 更新sp, 表示已经展示了新手引导
                PrefUtils.setBoolean(GuideActivity.this, "is_user_guide_showed", true);
//        Intent intent =new Intent();
//        intent.setClass(GuideActivity.this, SplashActivity.class);
//        // 跳转主页面
//        startActivity(intent);

                Intent in=null;
                userLogin = PreferenceHelper.getUserLogin(this);
                LogUtil.e("登陆："+userLogin.getBoolean("login", false));
               /* if (userLogin.getBoolean("login", false)) {
                    //in = new Intent(GuideActivity.this, MainActivity.class);
                    in=new Intent(GuideActivity.this,MainActivity.class);
                    startActivity(in);
                }else{
                    in=new Intent(GuideActivity.this,UserLoginActivity.class);
                    startActivity(in);
                }*/
                in=new Intent(GuideActivity.this,MainActivity.class);
                startActivity(in);
                finish();
                break;
        }

    }
}
