package com.example.footer.library.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.footer.R;
import com.example.footer.library.ILoadingLayout;
import com.example.footer.library.PullToRefreshBase;
import com.example.footer.views.CircularProgress;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@SuppressLint("ViewConstructor")
public abstract class LoadingLayout extends FrameLayout implements
		ILoadingLayout {

	static final String LOG_TAG = "PullToRefresh-LoadingLayout";

	static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
	protected SharedPreferences wlibaoConfig;

	private FrameLayout mInnerLayout;

	protected ImageView mHeaderImage;
	protected final CircularProgress mHeaderProgress;

	private boolean mUseIntrinsicAnimation;

	private final TextView mHeaderText;
	private final TextView mSubHeaderText;



	protected final PullToRefreshBase.Mode mMode;
	protected final PullToRefreshBase.Orientation mScrollDirection;

	private CharSequence mPullLabel;
	private CharSequence mRefreshingLabel;
	private CharSequence mReleaseLabel;
	protected SimpleDateFormat dayFormat,timeFormat;

	public LoadingLayout(Context context, final PullToRefreshBase.Mode mode,
			final PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
		super(context);

		timeFormat = new SimpleDateFormat("HH:mm");
		dayFormat = new SimpleDateFormat("MM月dd日");
		mMode = mode;
		mScrollDirection = scrollDirection;
//		wlibaoConfig = SharedPreferenceUtil.getWlibaoConfig(context);

		switch (scrollDirection) {
		case HORIZONTAL:
			LayoutInflater.from(context).inflate(
					R.layout.pull_to_refresh_header_horizontal, this);
			break;
		case VERTICAL:
		default:
			if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {
				LayoutInflater.from(context).inflate(
						R.layout.pull_to_refresh_header_vertical_start, this);
			} else if (mode == PullToRefreshBase.Mode.PULL_FROM_END) {
				LayoutInflater.from(context).inflate(
						R.layout.pull_to_refresh_header_vertical_end, this);
			}
			break;
		}

		mInnerLayout = (FrameLayout) findViewById(R.id.fl_inner);
		mHeaderText = (TextView) mInnerLayout
				.findViewById(R.id.pull_to_refresh_text);
		mHeaderProgress = (CircularProgress) mInnerLayout
				.findViewById(R.id.pull_to_refresh_progress);
		mSubHeaderText = (TextView) mInnerLayout
				.findViewById(R.id.pull_to_refresh_sub_text);
		mHeaderImage = (ImageView) mInnerLayout
				.findViewById(R.id.pull_to_refresh_image);

		LayoutParams lp = (LayoutParams) mInnerLayout
				.getLayoutParams();

		switch (mode) {
		case PULL_FROM_END:

			if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderBackground)) {
			Drawable background = attrs
					.getDrawable(R.styleable.PullToRefresh_ptrHeaderBackground);
			if (null != background) {
				ViewCompat.setBackground(this, background);
			}
		}

			lp.gravity = scrollDirection == PullToRefreshBase.Orientation.VERTICAL ? Gravity.TOP
					: Gravity.LEFT;

			// Load in labels
			mPullLabel = context
					.getString(R.string.pull_to_refresh_from_bottom_pull_label);
			mRefreshingLabel = context
					.getString(R.string.pull_to_refresh_from_bottom_refreshing_label);
			mReleaseLabel = context
					.getString(R.string.pull_to_refresh_from_bottom_release_label);
			break;

		case PULL_FROM_START:
		default:
			lp.gravity = scrollDirection == PullToRefreshBase.Orientation.VERTICAL ? Gravity.BOTTOM
					: Gravity.RIGHT;

			// Load in labels
			mPullLabel = context.getString(R.string.pull_to_refresh_pull_label);
			mRefreshingLabel = context
					.getString(R.string.pull_to_refresh_refreshing_label);
			mReleaseLabel = context
					.getString(R.string.pull_to_refresh_release_label);
			break;
		}

//		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderBackground)) {
//			Drawable background = attrs
//					.getDrawable(R.styleable.PullToRefresh_ptrHeaderBackground);
//			if (null != background) {
//				ViewCompat.setBackground(this, background);
//			}
//		}

		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance)) {
			TypedValue styleID = new TypedValue();
			attrs.getValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance,
					styleID);
			setTextAppearance(styleID.data);
		}
		if (attrs
				.hasValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance)) {
			TypedValue styleID = new TypedValue();
			attrs.getValue(
					R.styleable.PullToRefresh_ptrSubHeaderTextAppearance,
					styleID);
			setSubTextAppearance(styleID.data);
		}

		// Text Color attrs need to be set after TextAppearance attrs
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextColor)) {
			ColorStateList colors = attrs
					.getColorStateList(R.styleable.PullToRefresh_ptrHeaderTextColor);
			if (null != colors) {
				setTextColor(colors);
			}
		}
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderSubTextColor)) {
			ColorStateList colors = attrs
					.getColorStateList(R.styleable.PullToRefresh_ptrHeaderSubTextColor);
			if (null != colors) {
				setSubTextColor(colors);
			}
		}

		// Try and get defined drawable from Attrs
		Drawable imageDrawable = null;
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawable)) {
			imageDrawable = attrs
					.getDrawable(R.styleable.PullToRefresh_ptrDrawable);
		}

		// Check Specific Drawable from Attrs, these overrite the generic
		// drawable attr above
		switch (mode) {
		case PULL_FROM_START:
		default:
			if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableStart)) {
				imageDrawable = attrs
						.getDrawable(R.styleable.PullToRefresh_ptrDrawableStart);
			} else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableTop)) {
				Utils.warnDeprecation("ptrDrawableTop", "ptrDrawableStart");
				imageDrawable = attrs
						.getDrawable(R.styleable.PullToRefresh_ptrDrawableTop);
			}
			break;

		case PULL_FROM_END:
			if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableEnd)) {
				imageDrawable = attrs
						.getDrawable(R.styleable.PullToRefresh_ptrDrawableEnd);
			} else if (attrs
					.hasValue(R.styleable.PullToRefresh_ptrDrawableBottom)) {
				Utils.warnDeprecation("ptrDrawableBottom", "ptrDrawableEnd");
				imageDrawable = attrs
						.getDrawable(R.styleable.PullToRefresh_ptrDrawableBottom);
			}
			break;
		}

		// If we don't have a user defined drawable, load the default
		if (null == imageDrawable) {
			imageDrawable = context.getResources().getDrawable(
					getDefaultDrawableResId());
		}

		// Set Drawable, and save width/height
		// 隐藏图片
		if (mode == PullToRefreshBase.Mode.PULL_FROM_END) {
			setLoadingDrawable(imageDrawable);
		}

		reset();
	}

	public final void setHeight(int height) {
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
		lp.height = height;
		requestLayout();
	}

	public final void setWidth(int width) {
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
		lp.width = width;
		requestLayout();
	}

	public final int getContentSize() {
		switch (mScrollDirection) {
		case HORIZONTAL:
			return mInnerLayout.getWidth();
		case VERTICAL:
		default:
			return mInnerLayout.getHeight();
		}
	}

	public final void hideAllViews() {
		if (View.VISIBLE == mHeaderText.getVisibility()) {
			mHeaderText.setVisibility(View.INVISIBLE);
		}
		if (View.VISIBLE == mHeaderProgress.getVisibility()) {
			mHeaderProgress.setVisibility(View.INVISIBLE);
		}
		if (View.VISIBLE == mHeaderImage.getVisibility()) {
			 mHeaderImage.setVisibility(View.INVISIBLE);
		}
		if (View.VISIBLE == mSubHeaderText.getVisibility()) {
			mSubHeaderText.setVisibility(View.GONE);
		}
	}

	public final void onPull(float scaleOfLayout) {
		if (!mUseIntrinsicAnimation) {
			onPullImpl(scaleOfLayout);
		}
	}

	public final void pullToRefresh() {
		if (null != mHeaderText) {
			mHeaderText.setText(mPullLabel);
		}
		if (null != mSubHeaderText) {
			mSubHeaderText.setVisibility(View.VISIBLE);
		}

		// Now call the callback
		pullToRefreshImpl();
	}

	public final void refreshing() {
		if (null != mHeaderText) {
			mHeaderText.setText(mRefreshingLabel);
		}

		if (mUseIntrinsicAnimation) {
//			((AnimationDrawable) mHeaderImage.getDrawable()).start();
		} else {
			// Now call the callback
			refreshingImpl();
		}

		if (null != mSubHeaderText) {
			mSubHeaderText.setVisibility(View.VISIBLE);
		}
	}

	public final void releaseToRefresh() {
		if (null != mHeaderText) {
			mHeaderText.setText(mReleaseLabel);
		}
		if (null != mSubHeaderText) {
			mSubHeaderText.setVisibility(View.VISIBLE);
		}

		// Now call the callback
		releaseToRefreshImpl();
	}

	public final void reset() {
		if (null != mHeaderText) {
			mHeaderText.setText(mPullLabel);
		}
//		 mHeaderImage.setVisibility(View.VISIBLE);

		if (mUseIntrinsicAnimation) {
			((AnimationDrawable) mHeaderImage.getDrawable()).stop();
		} else {
			// Now call the callback
			resetImpl();
		}

		if (null != mSubHeaderText) {
			if (TextUtils.isEmpty(mSubHeaderText.getText())) {
				mSubHeaderText.setVisibility(View.VISIBLE);
			} else {
				 mSubHeaderText.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void setLastUpdatedLabel(CharSequence label) {
		setSubHeaderText(label);
	}

	public final void setLoadingDrawable(Drawable imageDrawable) {
		// Set Drawable
//		mHeaderImage.setImageDrawable(imageDrawable);
		mUseIntrinsicAnimation = (imageDrawable instanceof AnimationDrawable);

		// Now call the callback
		onLoadingDrawableSet(imageDrawable);
	}

	public void setPullLabel(CharSequence pullLabel) {
		mPullLabel = pullLabel;
	}

	public void setRefreshingLabel(CharSequence refreshingLabel) {
		mRefreshingLabel = refreshingLabel;
	}

	public void setReleaseLabel(CharSequence releaseLabel) {
		mReleaseLabel = releaseLabel;
	}

	@Override
	public void setTextTypeface(Typeface tf) {
		mHeaderText.setTypeface(tf);
	}

	public final void showInvisibleViews() {
		if (View.INVISIBLE == mHeaderText.getVisibility()) {
			mHeaderText.setVisibility(View.VISIBLE);
		}
		if (View.INVISIBLE == mHeaderProgress.getVisibility()) {
			mHeaderProgress.setVisibility(View.VISIBLE);
		}
		if (View.INVISIBLE == mHeaderImage.getVisibility()) {
			 mHeaderImage.setVisibility(View.VISIBLE);
		}
		if (View.INVISIBLE == mSubHeaderText.getVisibility()) {
			mSubHeaderText.setVisibility(View.GONE);
		}
	}

	/**
	 * Callbacks for derivative Layouts
	 */

	protected abstract int getDefaultDrawableResId();

	protected abstract void onLoadingDrawableSet(Drawable imageDrawable);

	protected abstract void onPullImpl(float scaleOfLayout);

	protected abstract void pullToRefreshImpl();

	protected abstract void refreshingImpl();

	protected abstract void releaseToRefreshImpl();

	protected abstract void resetImpl();

	private void setSubHeaderText(CharSequence label) {
		if (null != mSubHeaderText) {
			if (TextUtils.isEmpty(label)) {
				mSubHeaderText.setVisibility(View.GONE);
			} else {
				mSubHeaderText.setText(label);

				// Only set it to Visible if we're GONE, otherwise VISIBLE will
				// be set soon
				if (View.GONE == mSubHeaderText.getVisibility()) {
					mSubHeaderText.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	private void setSubTextAppearance(int value) {
		if (null != mSubHeaderText) {
			mSubHeaderText.setTextAppearance(getContext(), value);
		}
	}

	private void setSubTextColor(ColorStateList color) {
		if (null != mSubHeaderText) {
			mSubHeaderText.setTextColor(color);
		}
	}

	private void setTextAppearance(int value) {
		if (null != mHeaderText) {
			mHeaderText.setTextAppearance(getContext(), value);
		}
		if (null != mSubHeaderText) {
			mSubHeaderText.setTextAppearance(getContext(), value);
		}
	}

	private void setTextColor(ColorStateList color) {
		if (null != mHeaderText) {
			mHeaderText.setTextColor(color);
		}
		if (null != mSubHeaderText) {
			mSubHeaderText.setTextColor(color);
		}
	}


	protected int getResource(String imageName) {
//		int resId = getResources().getIdentifier(imageName, "drawable", .getPackageName());
		return R.drawable.default_ptr_rotate;
	}

	public static final int SECONDS_IN_DAY = 60 * 60 * 24;
	public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

	public static boolean isSameDayOfMillis(final long ms1, final long ms2) {
		final long interval = ms1 - ms2;
		return interval < MILLIS_IN_DAY
				&& interval > -1L * MILLIS_IN_DAY
				&& toDay(ms1) == toDay(ms2);
	}

	private static long toDay(long millis) {
		return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
	}
}