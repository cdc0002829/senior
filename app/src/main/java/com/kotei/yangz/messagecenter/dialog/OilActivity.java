package com.kotei.yangz.messagecenter.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.kotei.yangz.messagecenter.R;


public class OilActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_dialog);
        Button go_oil_bt = findViewById(R.id.go_oil_bt);
        Button cancel_bt = findViewById(R.id.cancel_bt);
        go_oil_bt.setOnClickListener(this);
        cancel_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go_oil_bt:
                OilActivity.this.finish();
                break;
            case R.id.cancel_bt:
                OilActivity.this.finish();
                break;
            default:
                break;
        }
    }
}
