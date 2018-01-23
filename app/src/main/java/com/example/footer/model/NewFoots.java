package com.example.footer.model;

import com.example.footer.constant.ListType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 乃军 on 2018/1/3.
 */

public class NewFoots extends ListEntity {
    public List<Tag> list=new ArrayList<>();
    public NewFoots(ListType type,List<Tag> list){
        this.type=type;
        this.list=list;

    }

    public List<Tag> getList() {
        return list;
    }
}
