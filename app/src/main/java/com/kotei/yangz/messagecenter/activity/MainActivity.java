package com.kotei.yangz.messagecenter.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kotei.yangz.messagecenter.R;
import com.kotei.yangz.messagecenter.entity.MessageEntity;
import com.kotei.yangz.messagecenter.service.LocalService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jacsdk.jacbasicsdk.JACTSPBasicSDK;
import com.jacsdk.jacbasicsdk.callback.OnMsgReceiveListener;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerview;
    private List<MessageEntity> messageEntities = new ArrayList<>();
    ;
    private Context mContext = MainActivity.this;
    private MessageAdapter adapter;
    private Button clean_all_bt;
    private RelativeLayout no_data_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        startService(new Intent(this, LocalService.class));
    }

    LeakHandler leakHandler = new LeakHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            messageEntities = (CopyOnWriteArrayList<MessageEntity>) msg.obj;
            adapter.setList(messageEntities);
        }
    };

    private static class LeakHandler extends Handler {
        private WeakReference<Activity> mService;

        public LeakHandler(Activity service) {
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri messageUri = Uri.parse("content://com.kotei.yangz.messagecenter.provider");
                Cursor cursor = getContentResolver().query(messageUri, new String[]{"drawable", "content", "time"}, null, null, null);
                CopyOnWriteArrayList<MessageEntity> messageEntities = new CopyOnWriteArrayList<>();
                while (cursor.moveToNext()) {
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setDrawable(cursor.getString(0));
                    messageEntity.setContent(cursor.getString(1));
                    messageEntity.setTime(cursor.getString(2));
                    messageEntities.add(messageEntity);
                }
                cursor.close();
                Message message = Message.obtain();
                message.obj = messageEntities;
                leakHandler.sendMessage(message);
            }
        }).start();
    }

    private void initView() {
        recyclerview = findViewById(R.id.recyclerview);
        clean_all_bt = findViewById(R.id.clean_all_bt);
        no_data_rl = findViewById(R.id.no_data_rl);
        clean_all_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageEntities != null && messageEntities.size() > 0) {
                    Uri messageUri = Uri.parse("content://com.kotei.yangz.messagecenter.provider");
                    getContentResolver().delete(messageUri, null, null);
                    messageEntities.clear();
                    adapter.notifyDataSetChanged();
                    no_data_rl.setVisibility(View.VISIBLE);
                    recyclerview.setVisibility(View.GONE);
                    clean_all_bt.setVisibility(View.GONE);
                }
            }
        });
        //创建LinearLayoutManager 对象 这里使用 LinearLayoutManager 是线性布局的意思
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        //设置RecyclerView 布局
        recyclerview.setLayoutManager(layoutmanager);
        //设置Adapter
        adapter = new MessageAdapter(new CollectionDataListCallBack() {
            @Override
            public void onDataSizeChanged(int count) {
                if (count <= 0) {
                    no_data_rl.setVisibility(View.VISIBLE);
                    recyclerview.setVisibility(View.GONE);
                    clean_all_bt.setVisibility(View.GONE);
                } else {
                    no_data_rl.setVisibility(View.GONE);
                    recyclerview.setVisibility(View.VISIBLE);
                    clean_all_bt.setVisibility(View.VISIBLE);
                }
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                switch (v.getId()) {
                    case R.id.total_rl:
                        Toast.makeText(mContext, "跳转:" + position, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.delete_ib:
                        Uri messageUri = Uri.parse("content://com.kotei.yangz.messagecenter.provider");
                        getContentResolver().delete(messageUri, "content = ?", new String[]{messageEntities.get(position).getContent()});
                        messageEntities.remove(position);
                        adapter.notifyDataSetChanged();
                        if (messageEntities != null && messageEntities.size() == 0) {
                            no_data_rl.setVisibility(View.VISIBLE);
                            recyclerview.setVisibility(View.GONE);
                            clean_all_bt.setVisibility(View.GONE);
                        } else {
                            no_data_rl.setVisibility(View.GONE);
                            recyclerview.setVisibility(View.VISIBLE);
                            clean_all_bt.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        recyclerview.setAdapter(adapter);
    }


    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public interface CollectionDataListCallBack {
        void onDataSizeChanged(int count);
    }

    public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

        private List<MessageEntity> messageEntityList;
        private OnItemClickListener mOnItemClickListener;
        private CollectionDataListCallBack mCallBack;

        public MessageAdapter(
                CollectionDataListCallBack callBack) {
            mCallBack = callBack;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public void setList(List<MessageEntity> list) {
            this.messageEntityList = list;
            if (mCallBack != null) {
                mCallBack.onDataSizeChanged(
                        (messageEntityList == null) ? 0 : messageEntityList.size()
                );
            }
            notifyDataSetChanged();
        }

        class MessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ImageView icon_iv;
            private TextView content_tv;
            private TextView time_tv;
            private Button delete_ib;
            private RelativeLayout total_rl;

            public MessageHolder(View itemView) {
                super(itemView);
                total_rl = itemView.findViewById(R.id.total_rl);
                icon_iv = itemView.findViewById(R.id.icon_iv);
                content_tv = itemView.findViewById(R.id.content_tv);
                time_tv = itemView.findViewById(R.id.time_tv);
                delete_ib = itemView.findViewById(R.id.delete_ib);
                total_rl.setOnClickListener(this);
                delete_ib.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(getAdapterPosition(), view);
                }
            }
        }

        @NonNull
        @Override
        public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
            MessageHolder holder = new MessageHolder(view);
            return holder;
        }


        @Override
        public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
            MessageEntity messageEntity = messageEntityList.get(position);
            byte[] byteArray = Base64.decode(messageEntity.getDrawable(), Base64.DEFAULT);
            Glide.with(MainActivity.this).load(byteArray).into(holder.icon_iv);
            holder.content_tv.setText(messageEntity.getContent());
            holder.time_tv.setText(messageEntity.getTime());
        }

        @Override
        public int getItemCount() {
            return messageEntityList == null ? 0 : messageEntityList.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
