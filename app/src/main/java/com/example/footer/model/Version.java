package com.example.footer.model;

import java.io.Serializable;

/**
 * Created by 乃军 on 2017/4/20.
 */
public class Version implements Serializable {
   public String version;
   public String description;
   public String url;

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}
