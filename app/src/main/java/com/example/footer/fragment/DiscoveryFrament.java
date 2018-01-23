package com.example.footer.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.footer.R;
import com.example.footer.activity.HelpActivity;
import com.example.footer.application.GlobalApplication;
import com.example.footer.framework.BaseFragment;

/**
 * Created by 乃军 on 2017/11/7.
 */

public class DiscoveryFrament  extends BaseFragment implements View.OnClickListener{


    private RelativeLayout help;

    @Override
    protected View getFragmentContentView() {
        return View.inflate(GlobalApplication.instance, R.layout.discover,null);
    }

    @Override
    protected void initFragmentView(View view) {
        TextView tvMiddle = (TextView) view.findViewById(R.id.title);
        tvMiddle.setText("发现");


        /* help =(RelativeLayout)view.findViewById(R.id.help);
        help.setOnClickListener(this);*/
    }

    @Override
    protected void initFragmentData() {

    }

    @Override
    protected void fragmentCreate() {

    }

    @Override
    protected void fragmentDestroy() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.help:
                /*Intent intent=new Intent(getActivity(),HelpActivity.class);
                startActivity(intent);*/
                break;
        }
    }
}
