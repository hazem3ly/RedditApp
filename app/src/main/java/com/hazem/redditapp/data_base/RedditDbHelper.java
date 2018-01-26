package com.hazem.redditapp.data_base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class RedditDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reddit_database.db";
    private static final int VERSION = 3;

    RedditDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_USER_TABLE = "CREATE TABLE " + RedditContract.REDDIT_USER_TABLE.TABLE_NAME + " (" +
                RedditContract.REDDIT_USER_TABLE.COLUMN_ID + " TEXT PRIMARY KEY, " +
                RedditContract.REDDIT_USER_TABLE.ICON_IMG + " TEXT NOT NULL, " +
                RedditContract.REDDIT_USER_TABLE.COLUMN_NAME + " TEXT NOT NULL);";

        final String CREATE_USER_COMMENTS_TABLE = "CREATE TABLE " + RedditContract.USER_COMMENTS_TABLE.TABLE_NAME + " (" +
                RedditContract.USER_COMMENTS_TABLE.COLUMN_ID + " TEXT , " +
                RedditContract.USER_COMMENTS_TABLE.SUBREDDIT + " TEXT , " +
                RedditContract.USER_COMMENTS_TABLE.BODY + " TEXT , " +
                RedditContract.USER_COMMENTS_TABLE.LINK_TITLE + " TEXT , " +
                RedditContract.USER_COMMENTS_TABLE.LINK_PERMALINK + " TEXT);";

        final String CREATE_USER_POSTS_TABLE = "CREATE TABLE " + RedditContract.USER_POSTS_TABLE.TABLE_NAME + " (" +
                RedditContract.USER_POSTS_TABLE.COLUMN_ID + " TEXT , " +
                RedditContract.USER_POSTS_TABLE.SUBREDDIT + " TEXT , " +
                RedditContract.USER_POSTS_TABLE.AUTHER_NAME + " TEXT, " +
                RedditContract.USER_POSTS_TABLE.POST_TITLE + " TEXT, " +
                RedditContract.USER_POSTS_TABLE.VOTE_COUNT + " TEXT, " +
                RedditContract.USER_POSTS_TABLE.COMMENTS_COUNT + " TEXT, " +
                RedditContract.USER_POSTS_TABLE.LINK_PERMALINK + " TEXT);";

        final String CREATE_USER_SAVED_TABLE = "CREATE TABLE " + RedditContract.USER_SAVED_TABLE.TABLE_NAME + " (" +
                RedditContract.USER_SAVED_TABLE.POST_TITLE + " TEXT , " +
                RedditContract.USER_SAVED_TABLE.SUBREDDIT_NAME + " TEXT , " +
                RedditContract.USER_SAVED_TABLE.USER_COMMENT + " TEXT, " +
                RedditContract.USER_SAVED_TABLE.IS_POST + " TEXT, " +
                RedditContract.USER_SAVED_TABLE.LINK_PERMALINK + " TEXT);";


        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_USER_COMMENTS_TABLE);
        db.execSQL(CREATE_USER_POSTS_TABLE);
        db.execSQL(CREATE_USER_SAVED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RedditContract.REDDIT_USER_TABLE.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RedditContract.USER_COMMENTS_TABLE.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RedditContract.USER_POSTS_TABLE.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RedditContract.USER_SAVED_TABLE.TABLE_NAME);
        onCreate(db);
    }
}
