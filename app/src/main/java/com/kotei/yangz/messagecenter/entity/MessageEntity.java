package com.kotei.yangz.messagecenter.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageEntity implements Parcelable {
    private String drawable;
    private String content;
    private String time;

    public MessageEntity(String drawable, String content, String time) {
        this.drawable = drawable;
        this.content = content;
        this.time = time;
    }

    private MessageEntity(Parcel in) {
        drawable = in.readString();
        content = in.readString();
        time = in.readString();
    }

    public static final Creator<MessageEntity> CREATOR = new Creator<MessageEntity>() {
        @Override
        public MessageEntity createFromParcel(Parcel in) {
            return new MessageEntity(in);
        }

        @Override
        public MessageEntity[] newArray(int size) {
            return new MessageEntity[size];
        }
    };

    public MessageEntity() {
    }

    public String getDrawable() {

        return drawable;
    }

    public void setDrawable(String drawable) {
        this.drawable = drawable;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(drawable);
        out.writeString(content);
        out.writeString(time);
    }
}
