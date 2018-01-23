package com.example.footer.model;

import java.io.Serializable;

/**
 * Created by 乃军 on 2017/12/22.
 */

public class Tag implements Serializable {
    public String id;
    public String createTime;
    public String context;
    public String time;
    public String isRecommend;

    public String getId() {
        return id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getContext() {
        return context;
    }

    public String getTime() {
        return time;
    }

    public String getIsRecommend() {
        return isRecommend;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id='" + id + '\'' +
                ", createTime='" + createTime + '\'' +
                ", context='" + context + '\'' +
                ", time='" + time + '\'' +
                ", isRecommend='" + isRecommend + '\'' +
                '}';
    }
}
