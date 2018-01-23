package com.example.footer.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefUtils {

	public static final String PREF_NAME = "config";
	public static final String VERSION_NAME = "version";


	public static boolean getBoolean(Context ctx, String key,
			boolean defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);
	}

	public static void setBoolean(Context ctx, String key, boolean value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}

	public static void setVersion(Context context,String key,int version){
		SharedPreferences sp=context.getSharedPreferences(VERSION_NAME,Context.MODE_PRIVATE);
		sp.edit().putInt(key, version).commit();
	}
	public static int  getVersion(Context context,String key,int version){
		SharedPreferences sp=context.getSharedPreferences(VERSION_NAME,Context.MODE_PRIVATE);
		return sp.getInt(key,version);
	}
}
