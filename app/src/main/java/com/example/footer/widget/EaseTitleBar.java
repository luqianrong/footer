package com.example.footer.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.footer.R;


/**
 * 标题栏
 *
 */
public class EaseTitleBar extends RelativeLayout{

    protected RelativeLayout leftLayout;
    protected ImageView leftImage;
    protected TextView lefttext;
    protected RelativeLayout rightLayout;
    protected ImageView rightImage;
    protected ImageView fangdajing;
    protected TextView titleView;
    protected RelativeLayout titleLayout;
    protected RelativeLayout sousuobar;
    private TextView tvRight;
    private View rootView;

    public EaseTitleBar(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public EaseTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EaseTitleBar(Context context) {
        super(context);
        init(context, null);
    }
    
    private void init(Context context, AttributeSet attrs){
        rootView = LayoutInflater.from(context).inflate(R.layout.widget_title_bar_layout, this);
        leftLayout = (RelativeLayout) findViewById(R.id.left_layout);
        leftImage = (ImageView) findViewById(R.id.left_image);
        lefttext=(TextView)findViewById(R.id.left_tex);
        rightLayout = (RelativeLayout) findViewById(R.id.right_layout);
        rightImage = (ImageView) findViewById(R.id.right_image);
        fangdajing=(ImageView)findViewById(R.id.fangdajing);
        titleView = (TextView) findViewById(R.id.title);
        titleLayout = (RelativeLayout) findViewById(R.id.root);
        tvRight = (TextView) findViewById(R.id.titleBar_Right_tv);

        sousuobar=(RelativeLayout)findViewById(R.id.sousuobar);

        parseStyle(context, attrs);
    }


    private void parseStyle(Context context, AttributeSet attrs){
        if(attrs != null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
            String title = ta.getString(R.styleable.TitleBar_titleBarTitle);
            String rightText = ta.getString(R.styleable.TitleBar_titleBarRightText);

            if (null!=title) {
                titleView.setText(title);
            }
            if (null!=rightText) {
                tvRight.setText(rightText);
            }
            
            Drawable leftDrawable = ta.getDrawable(R.styleable.TitleBar_titleBarLeftImage);
            if (null != leftDrawable) {
                leftImage.setImageDrawable(leftDrawable);
            }
            Drawable rightDrawable = ta.getDrawable(R.styleable.TitleBar_titleBarRightImage);
            if (null != rightDrawable) {
                rightImage.setImageDrawable(rightDrawable);
            }
        
            Drawable background = ta.getDrawable(R.styleable.TitleBar_titleBarBackground);
            if(null != background) {
                titleLayout.setBackgroundDrawable(background);
            }
            
            ta.recycle();
        }
    }
    
    public void setLeftImageResource(int resId) {
        leftImage.setImageResource(resId);
    }

    public void setLeftText(String text){
        lefttext.setText(text);
    }

    public void setLeftTextImageResource(Drawable nav_up){
        lefttext.setCompoundDrawables(null, null, nav_up, null);
    }
    
    public void setRightImageResource(int resId) {
        rightImage.setImageResource(resId);
    }

    public void setFangdajingImageResource(int resId){fangdajing.setImageResource(resId);}
    
    public void setLeftLayoutClickListener(OnClickListener listener){
        leftLayout.setOnClickListener(listener);
    }
    
    public void setRightLayoutClickListener(OnClickListener listener){
        rightLayout.setOnClickListener(listener);
    }

    public void setSousuobarClickListener(OnClickListener listener){
        sousuobar.setOnClickListener(listener);
    }
    
    public void setLeftLayoutVisibility(int visibility){
        leftLayout.setVisibility(visibility);
    }

    public void setLeftTextVisibility(int visibility){
        lefttext.setVisibility(visibility);
    }

    public void setSousuobarVisibility(int  visibility){
        sousuobar.setVisibility(visibility);
    }
    public void setRightLayoutVisibility(int visibility){
        rightLayout.setVisibility(visibility);
    }
    
    public void setFangdajingVisibility(int visibility){
        fangdajing.setVisibility(visibility);
    }

    public void setTitle(String title){
        titleView.setText(title);
    }
    
    public void setBackgroundColor(int color){
        titleLayout.setBackgroundColor(color);
    }
    
    public RelativeLayout getLeftLayout(){
        return leftLayout;
    }
    
    public RelativeLayout getRightLayout(){
        return rightLayout;
    }
    public TextView getTvRight(){
        return tvRight;
    }
}
