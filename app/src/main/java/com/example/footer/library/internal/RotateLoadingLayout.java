package com.example.footer.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.example.footer.R;
import com.example.footer.application.GlobalApplication;
import com.example.footer.config.Config;
import com.example.footer.library.PullToRefreshBase;
import com.example.footer.utils.PreferenceHelper;


public class RotateLoadingLayout extends LoadingLayout {

    static final int ROTATION_ANIMATION_DURATION = 1200;

    private final Animation mRotateAnimation;
    private final Matrix mHeaderImageMatrix;

    private ImageView ivZhu;

    private float mRotationPivotX, mRotationPivotY;

    private final boolean mRotateDrawableWhilePulling;

    private View view;

//	private RoundProgressBar progressBar;

    private Animation rotateAnim;

    private PullToRefreshBase.Mode mode;
    private AnimationDrawable animaiton;
    private TextView tvTime;
    private boolean isDiaplayRefreshTime = true;

    public RotateLoadingLayout(Context context, PullToRefreshBase.Mode mode,
                               PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);




        mRotateDrawableWhilePulling = attrs.getBoolean(
                R.styleable.PullToRefresh_ptrRotateDrawableWhilePulling, true);

        mHeaderImage.setScaleType(ScaleType.MATRIX);
        mHeaderImageMatrix = new Matrix();
        mHeaderImage.setImageMatrix(mHeaderImageMatrix);

        mRotateAnimation = new RotateAnimation(0, 720,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setRepeatMode(Animation.RESTART);
        //
        this.mode = mode;
        if (mode == PullToRefreshBase.Mode.PULL_FROM_END) {
            view = View.inflate(getContext(),
                    R.layout.pull_to_refresh_header_vertical_end, this);
        } else if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {
            view = View.inflate(getContext(),
                    R.layout.pull_to_refresh_header_vertical_start, this);
            ivZhu = (ImageView) view
                    .findViewById(R.id.ivZhu);
            tvTime = (TextView) view.findViewById(R.id.pull_to_refresh_sub_text);

            rotateAnim = AnimationUtils.loadAnimation(getContext(),
                    R.anim.rotate_anim);
            LinearInterpolator lin = new LinearInterpolator();
            rotateAnim.setInterpolator(lin);
        }

    }

    public void onLoadingDrawableSet(Drawable imageDrawable) {
        if (null != imageDrawable) {
            mRotationPivotX = Math
                    .round(imageDrawable.getIntrinsicWidth() / 2f);
            mRotationPivotY = Math
                    .round(imageDrawable.getIntrinsicHeight() / 2f);
        }
    }

    protected void onPullImpl(float scaleOfLayout) {

        float angle;
        if (mRotateDrawableWhilePulling) {
            angle = scaleOfLayout * 90f;
        } else {
            angle = Math.max(0f, Math.min(180f, scaleOfLayout * 360f - 180f));
        }
        if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {
//            int imageNum = (int) (scaleOfLayout * 100) % 30;
//            String imageName = "";
//            if (imageNum >= 1 && imageNum <= 30) {
//
//                if (imageNum >= 1 && imageNum <= 9) {
//                    imageName = "zhu000" + imageNum;
//
//                } else {
//                    imageName = "zhu00" + imageNum;
//                }
//                try {
//                    int resourceId = getResource(imageName);
//                    ivZhu.setBackgroundResource(resourceId);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
            if (isDiaplayRefreshTime) {
                long lastRefreshTime = PreferenceHelper.readLong(GlobalApplication.instance, "time", "lastRefreshTime", 0);
                boolean isToday = isSameDayOfMillis(System.currentTimeMillis(), lastRefreshTime);
                if (isToday) {
                    tvTime.setText("上次刷新:" + timeFormat.format(lastRefreshTime));
                } else {
                    tvTime.setText("上次刷新:" + dayFormat.format(lastRefreshTime));
                }

                isDiaplayRefreshTime = false;
            }
        }


//			progressBar.setMax(100);
//			if (Math.round(scaleOfLayout * 100) < 100
//					&& Math.round(scaleOfLayout * 100) > 0) {
//				if ((Math.round(scaleOfLayout * 100) - 5) > 0) {
//					progressBar
//							.setProgress((Math.round(scaleOfLayout * 100) - 5));
//				}
//			}

            mHeaderImageMatrix.setRotate(angle, mRotationPivotX, mRotationPivotY);
            mHeaderImage.setImageMatrix(mHeaderImageMatrix);
    }



    @Override
    protected void refreshingImpl() {
//        mHeaderImage.startAnimation(mRotateAnimation);
        try {
            if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {
//			progressBar.startAnimation(rotateAnim);
//                ivZhu.setBackgroundResource(R.drawable.pull_dow_animation);
//                animaiton = (AnimationDrawable) ivZhu.getBackground();
//                animaiton.start();
                long beforeRefreshTime = System.currentTimeMillis();
                String lastRefreshTime = timeFormat.format(beforeRefreshTime);
                tvTime.setText("刷新时间:" + lastRefreshTime);
                PreferenceHelper.write(GlobalApplication.instance, "time", "lastRefreshTime", beforeRefreshTime);
                isDiaplayRefreshTime = true;
                mHeaderProgress.setVisibility(View.VISIBLE);
            }else if (mode==PullToRefreshBase.Mode.PULL_FROM_END){
                mHeaderProgress.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            if(Config.DEBUG){
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void resetImpl() {
        mHeaderProgress.setVisibility(View.INVISIBLE);
        try {
            mHeaderImage.clearAnimation();
            if (null != animaiton && mode == PullToRefreshBase.Mode.PULL_FROM_START) {
//			progressBar.clearAnimation();
//                animaiton.stop();
            }
            resetImageRotation();
        } catch (Exception e) {
            if(Config.DEBUG){
                e.printStackTrace();
            }

        }
    }

    private void resetImageRotation() {
        if (null != mHeaderImageMatrix) {
//            mHeaderImageMatrix.reset();
//            mHeaderImage.setImageMatrix(mHeaderImageMatrix);
        }
    }

    @Override
    protected void pullToRefreshImpl() {
        // NO-OP
    }

    @Override
    protected void releaseToRefreshImpl() {
        // NO-OP
        if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {
//			ivZhu.clearAnimation();

        }
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.default_ptr_rotate;
    }

}
