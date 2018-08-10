package com.kotei.yangz.messagecenter.util;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kotei.yangz.messagecenter.R;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/2/17.
 * Toast自定义工具类
 */
public class ToastCommom {
    private static TextView text;
    private static Toast toast;
    private static ImageView image;
    private static RelativeLayout click_total_rl;

    /**
     * 显示Toast
     *
     * @param context
     * @param tvString
     * @param cntime
     */
    public static void ToastShow(final Context context, String tvString, int cntime) {

        if (toast == null) {
            toast = new Toast(context);
            toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            View layout = LayoutInflater.from(context).inflate(R.layout.app_push_layout, null);
            text = (TextView) layout.findViewById(R.id.content_tv);
            image = (ImageView) layout.findViewById(R.id.icon_iv);
            click_total_rl = (RelativeLayout) layout.findViewById(R.id.click_total_rl);
            click_total_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.showUpdataDialog(context);
                }
            });
            toast.setView(layout);
            showMyToast(toast, cntime);
        }
        text.setText(tvString);
        try {
            Object mTN;
            mTN = getField(toast, "mTN");
            if (mTN != null) {
                Object mParams = getField(mTN, "mParams");
                if (mParams != null
                        && mParams instanceof WindowManager.LayoutParams) {
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
                    //Toast可点击
                    params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

                    //设置viewgroup宽高
                    params.width = WindowManager.LayoutParams.MATCH_PARENT; //设置Toast宽度为屏幕宽度
                    params.height = WindowManager.LayoutParams.WRAP_CONTENT; //设置高度
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (toast != null) {
            toast.show();
        }

    }

    /**
     * 反射字段
     *
     * @param object    要反射的对象
     * @param fieldName 要反射的字段名称
     */
    private static Object getField(Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        if (field != null) {
            field.setAccessible(true);
            return field.get(object);
        }
        return null;
    }

    //自定义停留时间
    public static void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }

        }, 0, 3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }

        }, cnt);

    }

}
