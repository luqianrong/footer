package com.example.footer.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.footer.R;


/**
 * 退出框
 * Created by dell on 2015/11/23.
 */

public class Exit_Dialog_hb extends Dialog implements OnClickListener {
    TextView cancel, ok;
    Context context;
    private ExitDialogListener listener;

    public interface ExitDialogListener {
        public void onClick(View view);
    }

    public Exit_Dialog_hb(Context context, int theme, ExitDialogListener listener) {
        super(context, theme);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.listener = listener;
    }

    public Exit_Dialog_hb(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public Exit_Dialog_hb(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.exit_dialog_layout);
        initViews();
    }

    private void initViews() {
        cancel = (TextView) findViewById(R.id.tvActionCancel);
        ok = (TextView) findViewById(R.id.tvActionConfirm);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        listener.onClick(v);
    }
}