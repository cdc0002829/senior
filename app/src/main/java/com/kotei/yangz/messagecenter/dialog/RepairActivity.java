package com.kotei.yangz.messagecenter.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.kotei.yangz.messagecenter.R;


public class RepairActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_activity);
        Button know_repair_bt = findViewById(R.id.know_repair_bt);
        know_repair_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.know_repair_bt:
                finish();
                break;
            default:
                break;
        }
    }
}
