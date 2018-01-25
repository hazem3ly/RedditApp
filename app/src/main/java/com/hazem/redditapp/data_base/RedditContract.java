package com.hazem.redditapp.data_base;

import android.net.Uri;
import android.provider.BaseColumns;


public class RedditContract {

    public static final String AUTHORITY = "com.hazem.redditapp";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FREDDIT_DATA = "reddit_data";


    public static final class REDDIT_ENTRY implements BaseColumns {


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FREDDIT_DATA).build();


        public static final String TABLE_NAME = "reddit_user";

        public static final String COLUMN_ID = "id";
        public static final String ICON_IMG = "iconImg";
        public static final String COLUMN_NAME = "name";



    }

}
