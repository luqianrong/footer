package com.example.footer.library.internal;


import com.example.footer.utils.LogUtil;

public class Utils {

	static final String LOG_TAG = "PullToRefresh";

	public static void warnDeprecation(String depreacted, String replacement) {
		LogUtil.w("You're using the deprecated " + depreacted + " attr, please switch over to " + replacement);
	}

}
