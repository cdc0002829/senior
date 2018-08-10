package com.kotei.yangz.messagecenter.application;

import android.app.Application;

import com.kotei.yangz.messagecenter.db.DbOpenHelper;
import com.kotei.yangz.messagecenter.service.LocalService;

public class MyApplication extends Application {

    private DbOpenHelper dbOpenHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        dbOpenHelper = new DbOpenHelper(getApplicationContext());
        dbOpenHelper.getWritableDatabase();
    }
}
