package com.kotei.yangz.messagecenter.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kotei.yangz.messagecenter.R;


public class DataActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_activity_layout);
        Button go_data_bt = findViewById(R.id.go_dateshop_bt);
        Button cancel_data_bt = findViewById(R.id.cancel_data_bt);
        go_data_bt.setOnClickListener(this);
        cancel_data_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go_dateshop_bt:
                Toast.makeText(this,"goshop",Toast.LENGTH_LONG).show();
//                finish();
                break;
            case R.id.cancel_data_bt:
                finish();
                break;
            default:
                break;
        }
    }
}
