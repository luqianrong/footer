package com.example.footer.model;

import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by 乃军 on 2017/11/16.
 */

public class Discuss  implements Serializable {
    public int img_head;//头部
    public String txt_name;//评论人
    public String txt_time;//时间
    public String txt_desc;//描述

    public int getImg_head() {
        return img_head;
    }

    public void setImg_head(int img_head) {
        this.img_head = img_head;
    }

    public String getTxt_name() {
        return txt_name;
    }

    public void setTxt_name(String txt_name) {
        this.txt_name = txt_name;
    }

    public String getTxt_time() {
        return txt_time;
    }

    public void setTxt_time(String txt_time) {
        this.txt_time = txt_time;
    }

    public String getTxt_desc() {
        return txt_desc;
    }

    public void setTxt_desc(String txt_desc) {
        this.txt_desc = txt_desc;
    }

    public Discuss(int img_head, String txt_name, String txt_time, String txt_desc) {
        this.img_head = img_head;
        this.txt_name = txt_name;
        this.txt_time = txt_time;
        this.txt_desc = txt_desc;
    }

    @Override
    public String toString() {
        return "Discuss{" +
                "img_head=" + img_head +
                ", txt_name='" + txt_name + '\'' +
                ", txt_time='" + txt_time + '\'' +
                ", txt_desc='" + txt_desc + '\'' +
                '}';
    }
}
