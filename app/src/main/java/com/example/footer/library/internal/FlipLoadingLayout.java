package com.example.footer.library.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
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


@SuppressLint("ViewConstructor")
public class FlipLoadingLayout extends LoadingLayout {

	static final int FLIP_ANIMATION_DURATION = 150;

	private static final long ROTATION_ANIMATION_DURATION = 2600;
	private TextView tvTime;

	private Animation mRotateAnimation, mResetRotateAnimation;

	private PullToRefreshBase.Mode mode;

	private View view;
	
	private boolean mRotateDrawableWhilePulling;
	private boolean isDiaplayRefreshTime = true;


//	private RoundProgressBar progressBar;

	private Animation rotateAnim;
	private ImageView ivZhu;
	private AnimationDrawable animaiton;
	private Matrix mHeaderImageMatrix;
	private int mRotationPivotX;
	private int mRotationPivotY;

	public FlipLoadingLayout(Context context, final PullToRefreshBase.Mode mode,
			final PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);
		
		mRotateDrawableWhilePulling = attrs.getBoolean(
				R.styleable.PullToRefresh_ptrRotateDrawableWhilePulling, true);

		mHeaderImage.setScaleType(ScaleType.MATRIX);
		mHeaderImageMatrix = new Matrix();
		mHeaderImage.setImageMatrix(mHeaderImageMatrix);

		final int rotateAngle = mode == PullToRefreshBase.Mode.PULL_FROM_START ? -180 : 180;



		mResetRotateAnimation = new RotateAnimation(rotateAngle, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mResetRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		mResetRotateAnimation.setDuration(FLIP_ANIMATION_DURATION);
		mResetRotateAnimation.setFillAfter(true);

		mRotateAnimation = new RotateAnimation(0, 1440,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		//
		this.mode = mode;
		if (mode == PullToRefreshBase.Mode.PULL_FROM_END) {
			view = View.inflate(getContext(),
					R.layout.pull_to_refresh_header_vertical_end, this);
		} else if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {
			view = View.inflate(getContext(),
					R.layout.pull_to_refresh_header_vertical_start, this);
//			progressBar = (RoundProgressBar) view
//					.findViewById(R.id.roundProgressBar);


			ivZhu = (ImageView) view
					.findViewById(R.id.ivZhu);
			tvTime = (TextView) view.findViewById(R.id.pull_to_refresh_sub_text);

			rotateAnim = AnimationUtils.loadAnimation(getContext(),
					R.anim.rotate_anim);
			LinearInterpolator lin = new LinearInterpolator();
			rotateAnim.setInterpolator(lin);
		}
	}

	@Override
	protected void onLoadingDrawableSet(Drawable imageDrawable) {

		if (null != imageDrawable) {
			mRotationPivotX = Math
					.round(imageDrawable.getIntrinsicWidth() / 2f);
			mRotationPivotY = Math
					.round(imageDrawable.getIntrinsicHeight() / 2f);
		}
		if (null != imageDrawable) {
			final int dHeight = imageDrawable.getIntrinsicHeight();
			final int dWidth = imageDrawable.getIntrinsicWidth();

			ViewGroup.LayoutParams lp = mHeaderImage.getLayoutParams();
			lp.width = lp.height = Math.max(dHeight, dWidth);
			mHeaderImage.requestLayout();

			mHeaderImage.setScaleType(ScaleType.MATRIX);
			Matrix matrix = new Matrix();
			matrix.postTranslate((lp.width - dWidth) / 2f,
					(lp.height - dHeight) / 2f);
			matrix.postRotate(getDrawableRotationAngle(), lp.width / 2f,
					lp.height / 2f);
			mHeaderImage.setImageMatrix(matrix);
			
		
		}
	}



	@Override
	protected void onPullImpl(float scaleOfLayout) {
		// NO-OP
		float angle;
		if (mRotateDrawableWhilePulling) {
			angle = scaleOfLayout * 90f;
		} else {
			angle = Math.max(0f, Math.min(180f, scaleOfLayout * 360f - 180f));
		}
		if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {
//			int imageNum = (int) (scaleOfLayout * 100) % 30;
//			String imageName = "";
//			if (imageNum >= 1 && imageNum <= 30) {
//
//				if (imageNum >= 1 && imageNum <= 9) {
//					imageName = "zhu000" + imageNum;
//
//				} else {
//					imageName = "zhu00" + imageNum;
//				}
//				try {
//					int resourceId = getResource(imageName);
//					ivZhu.setBackgroundResource(resourceId);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
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
		if(mode== PullToRefreshBase.Mode.PULL_FROM_END){
			mHeaderImageMatrix.setRotate(angle, mRotationPivotX, mRotationPivotY);
			mHeaderImage.setImageMatrix(mHeaderImageMatrix);
		}

	}

	@Override
	protected void pullToRefreshImpl() {
		// Only start reset Animation, we've previously show the rotate anim



	}

	@Override
	protected void refreshingImpl() {

		try {

			if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {
//			progressBar.startAnimation(rotateAnim);
//				ivZhu.setBackgroundResource(R.drawable.pull_dow_animation);
//				animaiton = (AnimationDrawable) ivZhu.getBackground();
//				animaiton.start();
				long beforeRefreshTime = System.currentTimeMillis();
				String lastRefreshTime = timeFormat.format(beforeRefreshTime);
				tvTime.setText("刷新时间:" + lastRefreshTime);
				PreferenceHelper.write(GlobalApplication.instance, "time", "lastRefreshTime", beforeRefreshTime);
				isDiaplayRefreshTime = true;
				mHeaderProgress.setVisibility(View.VISIBLE);
			}
//			else if(mode== PullToRefreshBase.Mode.PULL_FROM_END){
//				mHeaderImage.setBackgroundResource(R.drawable.refreshing);
//				mHeaderImage.startAnimation(mRotateAnimation);
//			}
		} catch (Exception e) {
			if(Config.DEBUG){
				e.printStackTrace();
			}
		}
//		mHeaderImage.clearAnimation();
//		mHeaderImage.setVisibility(View.INVISIBLE);
//		mHeaderProgress.setVisibility(View.VISIBLE);

	}

	private void resetImageRotation() {
		if (null != mHeaderImageMatrix) {
            mHeaderImageMatrix.reset();
            mHeaderImage.setImageMatrix(mHeaderImageMatrix);
		}
	}

	@Override
	protected void releaseToRefreshImpl() {
//		if (progressBar != null && mode == Mode.PULL_FROM_START) {
//			progressBar.clearAnimation();
//		}
		// NO-OP
		if (mode == PullToRefreshBase.Mode.PULL_FROM_END) {
//			ivZhu.clearAnimation();
//			mHeaderImage.startAnimation(mRotateAnimation);
		}
	}

	@Override
	protected void resetImpl() {


//		if (progressBar != null && mode == Mode.PULL_FROM_START) {
//			progressBar.clearAnimation();
//		}
		mHeaderProgress.setVisibility(View.INVISIBLE);
		try {
			if (null != animaiton && mode == PullToRefreshBase.Mode.PULL_FROM_START) {
				animaiton.stop();
			}else if(mode== PullToRefreshBase.Mode.PULL_FROM_END){
				mHeaderImage.clearAnimation();
				mHeaderImage.setVisibility(View.VISIBLE);
			}
			resetImageRotation();
		} catch (Exception e) {
			if(Config.DEBUG){
				e.printStackTrace();
			}

		}
	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.drawable.refreshing;
	}

	private float getDrawableRotationAngle() {
		float angle = 0f;
		switch (mMode) {
		case PULL_FROM_END:
			if (mScrollDirection == PullToRefreshBase.Orientation.HORIZONTAL) {
				angle = 90f;
			} else {
				angle = 180f;
			}
			break;

		case PULL_FROM_START:
			if (mScrollDirection == PullToRefreshBase.Orientation.HORIZONTAL) {
				angle = 270f;
			}
			break;

		default:
			break;
		}

		return angle;
	}

}
