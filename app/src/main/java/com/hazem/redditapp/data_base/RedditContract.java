package com.hazem.redditapp.data_base;

import android.net.Uri;
import android.provider.BaseColumns;


public class RedditContract {

    static final String AUTHORITY = "com.hazem.redditapp";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    public static final class REDDIT_USER_TABLE implements BaseColumns {

        public static final String COLUMN_ID = "id";
        public static final String ICON_IMG = "iconImg";
        public static final String COLUMN_NAME = "name";
        static final String PATH_FREDDIT_DATA = "reddit_data";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FREDDIT_DATA).build();
        static final String TABLE_NAME = "reddit_user";


    }

    public static final class USER_COMMENTS_TABLE implements BaseColumns {

        public static final String TABLE_NAME = "user_comments";
        public static final String COLUMN_ID = "id";
        public static final String SUBREDDIT = "subreddit";
        public static final String BODY = "body";
        public static final String LINK_TITLE = "link_title";
        public static final String LINK_PERMALINK = "link_permalink";
        static final String PATH_COMMENTS_DATA = "comments_data";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMENTS_DATA).build();


    }

    public static final class USER_POSTS_TABLE implements BaseColumns {

        public static final String TABLE_NAME = "user_posts";
        public static final String COLUMN_ID = "id";
        public static final String SUBREDDIT = "subreddit";
        public static final String AUTHER_NAME = "auther_name";
        public static final String POST_TITLE = "post_title";
        public static final String VOTE_COUNT = "vote_count";
        public static final String COMMENTS_COUNT = "comment_count";
        public static final String LINK_PERMALINK = "link_permalink";
        static final String PATH_POSTS_DATA = "posts_data";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_POSTS_DATA).build();

    }

    public static final class USER_SAVED_TABLE implements BaseColumns {

        public static final String TABLE_NAME = "user_saved";

        public static final String POST_TITLE = "post_title";
        public static final String SUBREDDIT_NAME = "subreddit_name";
        public static final String USER_COMMENT = "user_comment";
        public static final String IS_POST = "is_post";
        public static final String LINK_PERMALINK = "link_permalink";



        static final String PATH_SAVED_DATA = "saved_data";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SAVED_DATA).build();
    }

}
