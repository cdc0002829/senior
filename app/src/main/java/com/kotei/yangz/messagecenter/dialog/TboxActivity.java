package com.kotei.yangz.messagecenter.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.kotei.yangz.messagecenter.R;

public class TboxActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_box_activity);
        Button know_t_box_bt = findViewById(R.id.know_t_box_bt);
        know_t_box_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
