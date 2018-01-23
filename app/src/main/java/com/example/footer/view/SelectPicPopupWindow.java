package com.example.footer.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.footer.R;
import com.example.footer.model.SelectItem;
import com.example.footer.utils.LogUtil;

import java.util.List;


/**
 * 弹出popwindow
 * Created by dell on 2015/11/23.
 */
public class SelectPicPopupWindow extends PopupWindow {
    private View mMenuView;
    //private static final String[] PLANETS = new String[]{"张三","李四","王五","门店","医生","门店和医生"};
    private YourListener mYourListener = null;
    private WheelView wva;
    private TextView tvOK;
    private SelectItem items;

    public SelectPicPopupWindow(Activity context, final List<? extends SelectItem> data) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.menu, null);

        wva = (WheelView) mMenuView.findViewById(R.id.main_wv);

        tvOK = (TextView) mMenuView.findViewById(R.id.main_show_dialog_btn);
        wva.setOffset(2);
        wva.setItems(data);
        wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, final SelectItem item) {
                LogUtil.e("selectedIndex=" + selectedIndex + "" + "item=" + item.name);
                items=item;
            }
        });
        tvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  //销毁弹出框
                  if (mYourListener != null) {
                         LogUtil.e("items="+items);
                      if (null==items){
                          mYourListener.onSomeChange(data.get(0), 9527);
                      }else {
                          mYourListener.onSomeChange(items, 9527);
                      }
                      }
                      dismiss();
            }
        });
//        wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
//            @Override
//            public void onSelected(int selectedIndex, final SelectItem item) {
//                LogUtil.e("selectedIndex=" + selectedIndex+"" + "item=" + item.name);
//                tvOK.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //销毁弹出框
//                        if (mYourListener != null) {
//                            mYourListener.onSomeChange(item, 9527);
//                        }
//                        dismiss();
////                        //以下写在按钮事件里的.
////                        Message msg = new Message();
////                        Bundle data = new Bundle();
////                        data.putString("keysl", item);
////                        msg.setData(data);
////                        msg.what = 1;
////                        mHandler.sendMessage(msg);
//                        //销毁弹出框
////                        dismiss();
//
//                    }
//                });
//            }
//        });
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
    public interface SelectCallBack {
        void SelectItemCallBack(SelectItem selectItem, int i);
    }

    public static void showSelectWindow(View parentView, Activity activity, List<? extends SelectItem> data, final SelectCallBack selectCallBack) {
        SelectPicPopupWindow menuWindow = new SelectPicPopupWindow(activity, data);
        //显示窗口
        menuWindow.showAtLocation(parentView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
        menuWindow.setYourListener(new SelectPicPopupWindow.YourListener() {
            @Override
            public void onSomeChange(SelectItem info, int i) {
                if (null != selectCallBack) {
                    selectCallBack.SelectItemCallBack(info, i);
                }
            }
        });

    }


    //回调接口(监听器)
    public interface YourListener {
        void onSomeChange(SelectItem info, int i);
    }

    //设置回调接口(监听器)的方法
    public void setYourListener(YourListener yourListener) {
        mYourListener = yourListener;
    }


}
