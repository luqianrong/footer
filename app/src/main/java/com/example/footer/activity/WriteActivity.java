package com.example.footer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.footer.R;
import com.example.footer.application.GlobalApplication;
import com.example.footer.framework.BaseActivity;

import java.text.ParseException;

import static android.os.Build.VERSION_CODES.N;

/**
 * Created by 乃军 on 2017/11/20.
 */

public class WriteActivity extends BaseActivity implements View.OnClickListener{
    private static final int  NAME    = 2;
    private static final int  EMPTY   =14;

    private TextView txt_save;

    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_view);
    }

    @Override
    protected void uIViewInit() throws ParseException {

        TextView tvMiddle = (TextView)findViewById(R.id.title);
        tvMiddle.setText("昵称");

        ImageView imageView=(ImageView)findViewById(R.id.left_image);
        imageView.setBackgroundResource(R.drawable.titlebar_left);
        imageView.setOnClickListener(this);

        txt =(TextView)findViewById(R.id.txt);//填写的内容

        txt_save =(TextView)findViewById(R.id.txt_save);
        txt_save.setOnClickListener(this);
    }

    @Override
    protected void uIViewDataApply() {

    }

    @Override
    protected void activityCreate() {

    }

    @Override
    protected void activityDestroy() {

    }

    @Override
    protected void onWindowHasFocus(boolean hasFocus) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.left_image:
                Intent intent=new Intent();
                intent.putExtra("name","");
                setResult(NAME, intent);
                finish();
                break;
            case R.id.txt_save:
                String info=txt.getText().toString().trim();
                if(!info.isEmpty()){

                    Intent intent0 = new Intent();
                    intent0.putExtra("name", info);
                    setResult(NAME, intent0);
                    finish();

                }
                break;
        }
    }
}
