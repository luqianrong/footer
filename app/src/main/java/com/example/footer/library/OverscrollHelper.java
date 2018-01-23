package com.example.footer.library;

import android.annotation.TargetApi;
import android.view.View;

import com.example.footer.utils.LogUtil;


@TargetApi(9)
public final class OverscrollHelper {

	static final String LOG_TAG = "OverscrollHelper";
	static final float DEFAULT_OVERSCROLL_SCALE = 1f;

	public static void overScrollBy(final PullToRefreshBase<?> view, final int deltaX, final int scrollX,
			final int deltaY, final int scrollY, final boolean isTouchEvent) {
		overScrollBy(view, deltaX, scrollX, deltaY, scrollY, 0, isTouchEvent);
	}

	public static void overScrollBy(final PullToRefreshBase<?> view, final int deltaX, final int scrollX,
			final int deltaY, final int scrollY, final int scrollRange, final boolean isTouchEvent) {
		overScrollBy(view, deltaX, scrollX, deltaY, scrollY, scrollRange, 0, DEFAULT_OVERSCROLL_SCALE, isTouchEvent);
	}

	public static void overScrollBy(final PullToRefreshBase<?> view, final int deltaX, final int scrollX,
			final int deltaY, final int scrollY, final int scrollRange, final int fuzzyThreshold,
			final float scaleFactor, final boolean isTouchEvent) {

		final int deltaValue, currentScrollValue, scrollValue;
		switch (view.getPullToRefreshScrollDirection()) {
			case HORIZONTAL:
				deltaValue = deltaX;
				scrollValue = scrollX;
				currentScrollValue = view.getScrollX();
				break;
			case VERTICAL:
			default:
				deltaValue = deltaY;
				scrollValue = scrollY;
				currentScrollValue = view.getScrollY();
				break;
		}

		// Check that OverScroll is enabled and that we're not currently
		// refreshing.
		if (view.isPullToRefreshOverScrollEnabled() && !view.isRefreshing()) {
			final PullToRefreshBase.Mode mode = view.getMode();

			// Check that Pull-to-Refresh is enabled, and the event isn't from
			// touch
			if (mode.permitsPullToRefresh() && !isTouchEvent && deltaValue != 0) {
				final int newScrollValue = (deltaValue + scrollValue);

				if (PullToRefreshBase.DEBUG) {
					LogUtil.d("OverScroll. DeltaX: " + deltaX + ", ScrollX: " + scrollX + ", DeltaY: " + deltaY
							+ ", ScrollY: " + scrollY + ", NewY: " + newScrollValue + ", ScrollRange: " + scrollRange
							+ ", CurrentScroll: " + currentScrollValue);
				}

				if (newScrollValue < (0 - fuzzyThreshold)) {
					// Check the mode supports the overscroll direction, and
					// then move scroll
					if (mode.showHeaderLoadingLayout()) {
						// If we're currently at zero, we're about to start
						// overscrolling, so change the state
						if (currentScrollValue == 0) {
							view.setState(PullToRefreshBase.State.OVERSCROLLING);
						}

						view.setHeaderScroll((int) (scaleFactor * (currentScrollValue + newScrollValue)));
					}
				} else if (newScrollValue > (scrollRange + fuzzyThreshold)) {
					// Check the mode supports the overscroll direction, and
					// then move scroll
					if (mode.showFooterLoadingLayout()) {
						// If we're currently at zero, we're about to start
						// overscrolling, so change the state
						if (currentScrollValue == 0) {
							view.setState(PullToRefreshBase.State.OVERSCROLLING);
						}

						view.setHeaderScroll((int) (scaleFactor * (currentScrollValue + newScrollValue - scrollRange)));
					}
				} else if (Math.abs(newScrollValue) <= fuzzyThreshold
						|| Math.abs(newScrollValue - scrollRange) <= fuzzyThreshold) {
					// Means we've stopped overscrolling, so scroll back to 0
					view.setState(PullToRefreshBase.State.RESET);
				}
			} else if (isTouchEvent && PullToRefreshBase.State.OVERSCROLLING == view.getState()) {
				// This condition means that we were overscrolling from a fling,
				// but the user has touched the View and is now overscrolling
				// from touch instead. We need to just reset.
				view.setState(PullToRefreshBase.State.RESET);
			}
		}
	}

	static boolean isAndroidOverScrollEnabled(View view) {
		return view.getOverScrollMode() != View.OVER_SCROLL_NEVER;
	}
}
