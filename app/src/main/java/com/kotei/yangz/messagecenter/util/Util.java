package com.kotei.yangz.messagecenter.util;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.kotei.yangz.messagecenter.R;

import java.lang.ref.WeakReference;

public class Util {

    public static void showToast(Context context) {
        //加载Toast布局
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.toast, null);
        //Toast的初始化
        Toast toastStart = new Toast(context);
        //获取屏幕高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        //Toast的Y坐标是屏幕高度的1/2，不会出现不适配的问题
        toastStart.setGravity(Gravity.TOP, 0, height / 2);
        toastStart.setDuration(Toast.LENGTH_LONG);
        toastStart.setView(toastRoot);
        toastStart.show();
    }

    private static WindowManager mWindowManager = null;
    private static View mView = null;
    public static Boolean isShown = false;

    public static void showTopWindow(final Context context) {
        isShown = true;
        final Context mContext = context.getApplicationContext();
        // 获取WindowManager
        mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        mView = LayoutInflater.from(context).inflate(R.layout.app_push_layout, null);
        RelativeLayout clickBt = mView.findViewById(R.id.click_total_rl);
        clickBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidePopupWindow();
                showUpdataDialog(context);
            }
        });
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 类型
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        // 设置flag
        int flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP;
        mWindowManager.addView(mView, params);
        new MyHandler(context).postDelayed(new Runnable() {//延时1秒之后再向上移动
            @Override
            public void run() {
                hidePopupWindow();
            }
        }, 3000);
    }

    public static class MyHandler extends Handler {
        private final WeakReference<Context> localServiceWeakReference;

        public MyHandler(Context localServiceWeakReference) {
            this.localServiceWeakReference = new WeakReference<>(localServiceWeakReference);
        }
    }

    public static void hidePopupWindow() {
        if (isShown && null != mView) {
            mWindowManager.removeView(mView);
            isShown = false;
        }
    }

    public static void showOilDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.contact_del_dialog).setCancelable(false);
        final AlertDialog dialog = builder.create();
        View inflate = LayoutInflater.from(context).inflate(R.layout.activity_oil_dialog, null);
        dialog.setView(inflate);
        Button go_oil_bt = inflate.findViewById(R.id.go_oil_bt);
        Button cancel_bt = inflate.findViewById(R.id.cancel_bt);
        cancel_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        go_oil_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER); //可设置dialog的位置
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialogWindow.setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        //获得窗体的属性
        dialog.show();//显示对话框
    }

    public static void showUpdataDialog(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.contact_del_dialog).setCancelable(false);
        final AlertDialog dialog = builder.create();
        View inflate = LayoutInflater.from(context).inflate(R.layout.activity_updata_dialog, null);
        dialog.setView(inflate);
        Button go_oil_bt = inflate.findViewById(R.id.go_updata_bt);
        Button cancel_bt = inflate.findViewById(R.id.cancel_bt);
        cancel_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        go_oil_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                PackageManager packageManager =context.getPackageManager();
                Intent intent =packageManager.getLaunchIntentForPackage("com.kotei.fota");
                if(intent==null){
                    Toast.makeText(context, "未安装", Toast.LENGTH_LONG).show();
                }else{
                    Intent intent1 = new Intent();
                    ComponentName componentName = new ComponentName("com.kotei.fota", "com.kotei.fota.view.FotaActivity");
                    intent1.setComponent(componentName);
                    Bundle bundle=new Bundle();
                    bundle.putInt("type",1);
                    intent1.putExtras(bundle);
                    context.startActivity(intent1);
                }
            }
        });
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER); //可设置dialog的位置
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialogWindow.setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        //获得窗体的属性
        dialog.show();//显示对话框
    }

    public static void showBatteryDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.contact_del_dialog).setCancelable(false);
        final AlertDialog dialog = builder.create();
        View inflate = LayoutInflater.from(context).inflate(R.layout.activity_dialog, null);
        dialog.setView(inflate);
        Button know_bt = inflate.findViewById(R.id.know_bt);
        know_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER); //可设置dialog的位置
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialogWindow.setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        //获得窗体的属性
        dialog.show();//显示对话框
    }

    public static void showDataDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.contact_del_dialog).setCancelable(false);
        final AlertDialog dialog = builder.create();
        View inflate = LayoutInflater.from(context).inflate(R.layout.data_activity_layout, null);
        dialog.setView(inflate);
        Button go_dateshop_bt = inflate.findViewById(R.id.go_dateshop_bt);
        Button cancel_data_bt = inflate.findViewById(R.id.cancel_data_bt);
        go_dateshop_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        cancel_data_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER); //可设置dialog的位置
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialogWindow.setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        //获得窗体的属性
        dialog.show();//显示对话框
    }

    public static void showRepairDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.contact_del_dialog).setCancelable(false);
        final AlertDialog dialog = builder.create();
        View inflate = LayoutInflater.from(context).inflate(R.layout.repair_activity, null);
        dialog.setView(inflate);
        Button know_repair_bt = inflate.findViewById(R.id.know_repair_bt);
        know_repair_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER); //可设置dialog的位置
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialogWindow.setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        //获得窗体的属性
        dialog.show();//显示对话框
    }

    public static void showTboxDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.contact_del_dialog).setCancelable(false);
        final AlertDialog dialog = builder.create();
        View inflate = LayoutInflater.from(context).inflate(R.layout.t_box_activity, null);
        dialog.setView(inflate);
        Button know_repair_bt = inflate.findViewById(R.id.know_t_box_bt);
        know_repair_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER); //可设置dialog的位置
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialogWindow.setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        //获得窗体的属性
        dialog.show();//显示对话框
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);  //+0.5是为了向上取整
    }
}
