package com.kotei.yangz.messagecenter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "message_provider.db";
    public static final String MESSAGE_TABLE_NAME = "";
    public static final int DB_VERSION = 1;
    String CREATE_TABLE_NOTE = "CREATE TABLE IF NOT EXISTS " + "message" + "("
            + "id" + " INTEGER PRIMARY KEY   AUTOINCREMENT, "
            + "drawable" + " TEXT Default 'Unknown', "
            +"content" + " TEXT Default 'Unknown', "
            + "time" + " TEXT Default 'Unknown')";
    private String CREATE_MESSAGE_TABLE = "create table if not exists message (id integer primary key autoincrement,drawable text,content text,time text)";

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
