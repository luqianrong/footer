package com.example.footer.utils;

import android.view.View;

/**
 * Created by 乃军 on 2017/12/8.
 */

public interface AdapterCallback {

    void onItemBenefitClick(View view,String benefit,String spoorId);

    void onItemSpoorCollectClick(View view,String collect,String spoorId);

    void onItemDeleteClick(View view,String spoorId);
}
