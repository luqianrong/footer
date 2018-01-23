package com.example.footer.model;

import java.io.Serializable;

/**
 * Created by 乃军 on 2017/12/8.
 */

public class Picture implements Serializable {
    public String pictureId;
    public String createTime;
    public String spoorId;
    public String pictureUrl;

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSpoorId() {
        return spoorId;
    }

    public void setSpoorId(String spoorId) {
        this.spoorId = spoorId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
