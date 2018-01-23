package com.example.footer.views;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import com.example.footer.R;


/**
 * Created by 乃军 on 2017/11/16.
 */

public class HeadView extends LinearLayout {
    private final Context mContext;

    public HeadView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = View.inflate(mContext, R.layout.head_view, this);

    }


}
