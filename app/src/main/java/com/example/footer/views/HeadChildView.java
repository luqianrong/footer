package com.example.footer.views;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.example.footer.R;
import com.example.footer.activity.MyFootActivity;
import com.example.footer.application.GlobalApplication;

/**
 * Created by 乃军 on 2017/12/20.
 */

public class HeadChildView extends LinearLayout{
    private final Context mContext;

    public HeadChildView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = View.inflate(mContext, R.layout.head_view, this);
    }

}
