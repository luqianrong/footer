package com.example.footer.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.footer.R;
import com.example.footer.fragment.DiscoveryFrament;
import com.example.footer.fragment.MeFragment;
import com.example.footer.fragment.RailingFragment;
import com.example.footer.fragment.ZhuYeFragment;
import com.example.footer.framework.BaseActivity;

import java.text.ParseException;

/**
 * Created by 乃军 on 2017/11/29.
 */

public class SplashActivity extends BaseActivity {

    private final String mIndicator[] = {"足迹", "寻迹", "发现", "我的"};

    private final Class mFragmentsClass[] = {ZhuYeFragment.class,
            RailingFragment.class,
            DiscoveryFrament.class,
            MeFragment.class};

    private final int mTabImage[] = {R.drawable.foot_selector,
            R.drawable.xj_selector,
            R.drawable.find_selector,
            R.drawable.tou_selector};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
    }

    @Override
    protected void uIViewInit() throws ParseException {
        final FragmentTabHost tabHost = (FragmentTabHost) findViewById(R.id.tab_host);
        if (tabHost == null) {
            return;
        }
        tabHost.setup(this, getSupportFragmentManager(), R.id.real_tab_content);
        for (int i = 0; i < mIndicator.length; i++) {
            tabHost.addTab(tabHost.newTabSpec(mIndicator[i]).setIndicator(getTabView(i)),
                    mFragmentsClass[i], null);
        }
        tabHost.getTabWidget().setDividerDrawable(android.R.color.transparent);//去掉组件间的分割线
    }

    private View getTabView(int index) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.tab_item, null);
        ImageView image = (ImageView) view.findViewById(R.id.tab_image);
        TextView text = (TextView) view.findViewById(R.id.tab_title);
        image.setImageResource(mTabImage[index]);
        text.setText(mIndicator[index]);


        return view;
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
}
