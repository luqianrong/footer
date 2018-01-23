package com.example.footer.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 乃军 on 2017/12/4.
 */

public class NineGridTestModels implements Serializable{
    public String message;
    public String homeIcon;
    public String banner;
    public String code;
    public List<NineGridTestModel> data;


    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public String getHomeIcon() {
        return homeIcon;
    }

    public String getBanner() {
        return banner;
    }

    public List<NineGridTestModel> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "NineGridTestModels{" +
                "message='" + message + '\'' +
                ", code='" + code + '\'' +
                ", data=" + data +
                '}';
    }
}
