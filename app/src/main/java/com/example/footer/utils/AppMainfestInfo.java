package com.example.footer.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

import com.example.footer.application.GlobalApplication;


/**
 * Created by android on 3/28/16.
 */
public class AppMainfestInfo {

    /**
     * activity 配置信息
     * @param activity
     * @param key
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static boolean getActivtyBoolInfo(Activity activity, String key) throws PackageManager.NameNotFoundException {

        ActivityInfo info = GlobalApplication.instance.getPackageManager()
                .getActivityInfo(activity.getComponentName(),
                        PackageManager.GET_META_DATA);
        if (null!=info.metaData) {
            return info.metaData.getBoolean(key);
        }
        return false;
    }
}
