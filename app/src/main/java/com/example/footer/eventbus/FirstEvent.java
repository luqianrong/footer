package com.example.footer.eventbus;

import java.util.ArrayList;

/**
 * Created by 乃军 on 2017/12/23.
 */

public class FirstEvent {
    private String mMsg;
    ArrayList<String> list;
    public FirstEvent(String msg) {
        // TODO Auto-generated constructor stub
        mMsg = msg;
    }

    public FirstEvent(ArrayList<String> list){
        this.list=list;
    }
    public String getMsg(){
        return mMsg;
    }

    public ArrayList<String> getList() {
        return list;
    }
}
