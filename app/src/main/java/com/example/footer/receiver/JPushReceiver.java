package com.example.footer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.footer.activity.WelcomeActivity;
import com.example.footer.application.GlobalApplication;
import com.example.footer.constant.Constant;
import com.example.footer.eventbus.FirstEvent;
import com.example.footer.utils.LogUtil;
import com.example.footer.utils.PreferenceHelper;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by �˾� on 2015/11/9.
 */


public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        LogUtil.e("jpushId:"+JPushInterface.getRegistrationID(GlobalApplication.instance));

        LogUtil.e("[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            LogUtil.e("接收Registration Id : " + regId);
            LogUtil.e("jpush receiver----------------->>"+regId);
            //send the Registration Id to your server...
            SharedPreferences jpushConfig = PreferenceHelper.getJpushConfig(GlobalApplication.instance);
            SharedPreferences.Editor edit = jpushConfig.edit();
            edit.putString("jpushId",regId);
            edit.apply();
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            String jpushId=bundle.getString(JPushInterface.EXTRA_MESSAGE);

            LogUtil.e("[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            LogUtil.e("jpushId:"+JPushInterface.getRegistrationID(GlobalApplication.instance));

            EventBus.getDefault().post(new FirstEvent(jpushId));
            //processCustomMessage(context, bundle);


        }else if(JPushInterface.EXTRA_TITLE.equals(intent.getAction())){
            LogUtil.e("title:"+bundle.getString(JPushInterface.EXTRA_TITLE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtil.d("[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            LogUtil.d("[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            Intent msgIntent=new Intent();
            msgIntent.setAction(Constant.MESSAGE_CHANGE_ACTION);
            context.sendBroadcast(msgIntent);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LogUtil.d("[MyReceiver] 用户点击打开了通知");

            //打开自定义的Activity
        	Intent i = new Intent(context, WelcomeActivity.class);
        	i.putExtras(bundle);
        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            LogUtil.d("[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            LogUtil.w("[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            LogUtil.d("[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            }
            else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
//        if (MainActivity.isForeground) {
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (null != extraJson && extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            context.sendBroadcast(msgIntent);
//        }
    }
}
