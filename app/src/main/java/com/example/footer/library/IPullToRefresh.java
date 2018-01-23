package com.example.footer.library;

import android.view.View;
import android.view.animation.Interpolator;


public interface IPullToRefresh<T extends View> {

	public boolean demo();

	public PullToRefreshBase.Mode getCurrentMode();

	public boolean getFilterTouchEvents();

	public ILoadingLayout getLoadingLayoutProxy();

	public ILoadingLayout getLoadingLayoutProxy(boolean includeStart,
                                                boolean includeEnd);

	public PullToRefreshBase.Mode getMode();

	public T getRefreshableView();

	public boolean getShowViewWhileRefreshing();

	public PullToRefreshBase.State getState();

	public boolean isPullToRefreshEnabled();

	public boolean isPullToRefreshOverScrollEnabled();

	public boolean isRefreshing();

	public boolean isScrollingWhileRefreshingEnabled();

	public void onRefreshComplete();

	public void setFilterTouchEvents(boolean filterEvents);

	public void setMode(PullToRefreshBase.Mode mode);

	public void setOnPullEventListener(PullToRefreshBase.OnPullEventListener<T> listener);

	public void setOnRefreshListener(PullToRefreshBase.OnRefreshListener<T> listener);

	public void setOnRefreshListener(PullToRefreshBase.OnRefreshListener2<T> listener);

	public void setPullToRefreshOverScrollEnabled(boolean enabled);

	public void setRefreshing();

	public void setRefreshing(boolean doScroll);

	public void setScrollAnimationInterpolator(Interpolator interpolator);

	public void setScrollingWhileRefreshingEnabled(
            boolean scrollingWhileRefreshingEnabled);

	public void setShowViewWhileRefreshing(boolean showView);

}