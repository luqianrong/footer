package com.example.footer.model;

import java.io.Serializable;

/**
 * Created by android on 5/24/16.
 */
public class SelectItem implements Serializable{

    public String name;

    public SelectItem(String name){
        this.name=name;
    }

    public SelectItem() {

    }
}
