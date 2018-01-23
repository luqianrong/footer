package com.example.footer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by 乃军 on 2018/1/5.
 */

public class MyGridView extends GridView {
    public MyGridView(Context paramContext)

    {

        super(paramContext);

    }



    public MyGridView(Context paramContext, AttributeSet paramAttributeSet)

    {

        super(paramContext, paramAttributeSet);

    }



    public MyGridView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

    }



    @Override public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec( Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
