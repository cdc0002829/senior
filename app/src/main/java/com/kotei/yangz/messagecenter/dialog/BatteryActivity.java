package com.kotei.yangz.messagecenter.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.kotei.yangz.messagecenter.R;


public class BatteryActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        Button know_bt = findViewById(R.id.know_bt);
        know_bt.setOnClickListener(this);
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                BatteryActivity.this.finish();
//            }
//        }, 3000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.know_bt:
                BatteryActivity.this.finish();
                break;
            default:
                break;
        }
    }
}
