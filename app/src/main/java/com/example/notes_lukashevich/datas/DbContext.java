package com.example.notes_lukashevich.datas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbContext extends SQLiteOpenHelper {

    public static SQLiteDatabase sqLiteDatabase;

    public static final String DATABASE_NAME = "DBNotes";
    public static final int DATABASE_VERSION = 1;

    public DbContext(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Notes (" +
                "Id integer primary key autoincrement," +
                "Title text," +
                "Text text," +
                "Date text," +
                "Color text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}