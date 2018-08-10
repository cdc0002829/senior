package com.kotei.yangz.messagecenter.reiceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.kotei.yangz.messagecenter.service.LocalService;

public class SlptServiceInstalledReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"开机广播",Toast.LENGTH_LONG).show();
        Intent service = new Intent(context, LocalService.class);
        context.startService(service);
    }
}
