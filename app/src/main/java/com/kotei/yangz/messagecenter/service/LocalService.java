package com.kotei.yangz.messagecenter.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;


import com.kotei.yangz.messagecenter.IOnNewMessageArrivesLister;
import com.kotei.yangz.messagecenter.ProcessService;
import com.kotei.yangz.messagecenter.R;
import com.kotei.yangz.messagecenter.db.DbOpenHelper;
import com.kotei.yangz.messagecenter.entity.MessageEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jacsdk.jacbasicsdk.JACTSPBasicSDK;
import com.jacsdk.jacbasicsdk.callback.OnMsgReceiveListener;
import com.kotei.yangz.messagecenter.util.ToastCommom;
import com.kotei.yangz.messagecenter.util.Util;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class LocalService extends Service implements OnMsgReceiveListener {
    private final static int GRAY_SERVICE_ID = 1001;
    private CopyOnWriteArrayList<MessageEntity> messageEntities = new CopyOnWriteArrayList<MessageEntity>();
    private RemoteCallbackList<IOnNewMessageArrivesLister> mListenerList = new RemoteCallbackList<IOnNewMessageArrivesLister>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private Binder mBinder = new ProcessService.Stub() {
        @Override
        public void registerListener(IOnNewMessageArrivesLister listener) {
            mListenerList.register(listener);
        }

        @Override
        public void unregisterListener(IOnNewMessageArrivesLister listener) {
            mListenerList.unregister(listener);
        }
    };

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final JACTSPBasicSDK js = new JACTSPBasicSDK();
        js.setOnMsgReceiveListener(this);
        js.registerMsg(11);
        startForeground(GRAY_SERVICE_ID, new Notification());
        ToastCommom.ToastShow(this,"hgdhjhj",10000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void OnMsgReceive(String message, int i) {
        MessageEntity messageEntity = buildMessageEntity(message+Math.random());
        ContentValues contentValues = new ContentValues();
        contentValues.put("drawable", messageEntity.getDrawable());
        contentValues.put("content", messageEntity.getContent());
        contentValues.put("time", messageEntity.getTime());
        Uri messageUri = Uri.parse("content://com.kotei.yangz.messagecenter.provider");
        getContentResolver().insert(messageUri, contentValues);
        final int N = mListenerList.beginBroadcast();
        for (int j = 0; j < N; j++) {
            IOnNewMessageArrivesLister l = mListenerList.getBroadcastItem(j);
            if (l != null) {
                try {
                    l.onNewMessageArrives(messageEntity);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mListenerList.finishBroadcast();
    }

    public MessageEntity buildMessageEntity(String message) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String nowTime = formatter.format(curDate);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.message);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        return new MessageEntity(imageString, message, nowTime);
    }

}
