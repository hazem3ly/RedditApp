package com.hazem.redditapp.data_base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class RedditDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reddit_database.db";
    private static final int VERSION = 1;

    RedditDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE " + RedditContract.REDDIT_ENTRY.TABLE_NAME + " (" +
                RedditContract.REDDIT_ENTRY.COLUMN_ID + " TEXT PRIMARY KEY, " +
                RedditContract.REDDIT_ENTRY.ICON_IMG + " TEXT NOT NULL, " +
                RedditContract.REDDIT_ENTRY.COLUMN_NAME + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RedditContract.REDDIT_ENTRY.TABLE_NAME);
        onCreate(db);
    }
}
