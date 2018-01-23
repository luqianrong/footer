package com.example.footer.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.footer.R;
import com.example.footer.activity.MyFootActivity;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.utils.PreferenceHelper;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by 乃军 on 2015/11/30.
 */
public class HeadLinearView extends LinearLayout implements View.OnClickListener {


    private final Context mContext;

    public HeadLinearView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView() {

        View view = View.inflate(mContext, R.layout.linear_head_view, this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
