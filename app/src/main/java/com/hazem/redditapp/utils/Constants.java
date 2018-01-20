package com.hazem.redditapp.utils;

/**
 * Created by Hazem Ali.
 * On 1/12/2018.
 */

public class Constants {

    private static final String SCOPE = "identity edit flair modposts mysubreddits " +
            "read report save submit subscribe vote";
    public static final String CLIENT_ID = "-uirF03FNWtvDw";
    public static final String REDIRECT_URI = "http://127.0.0.1/my_redirect";
    public static final String STATE = "MY_RANDOM_STRING_1";
    public static final String ACCESS_TOKEN_URL = "/api/v1/access_token";
    public static final String LOGIN_URL = "https://www.reddit.com/api/v1/authorize.compact?" +
            "client_id=" + CLIENT_ID + "&" +
            "response_type=code&" +
            "state=" + STATE + "&" +
            "redirect_uri=" + REDIRECT_URI + "&" +
            "duration=permanent&" +
            "scope=" + SCOPE;
    public static final String BASE_URL = "https://www.reddit.com/";
    public static final String BASE_URL_OAUTH = "https://oauth.reddit.com";

    public static final String HOME_SUBRIDDIT = "";
    public static final String ALL_SUBRIDDIT = "r/all/";
    public static final String POPULAR_SUBRIDDIT = "r/popular/";

    public static final String NEW_POSTS = "new/.json";
    public static final String HOT_POSTS = "hot/.json";
    public static final String RISING_POSTS = "rising/.json";
    public static final String TOP_POSTS = "top/.json";
    public static final String CONTROVERSIAL_POSTS = "controversial/.json";

    public static final int PAGES_COUNT = 4;
    public static final String FRAGMENT_POSITION = "position";


    public static final String POST_URL = "post_detail_url";
    public static final String POST_ID = "post_detail_id";
    public static final String POST_SUBREDDIT_NAME = "post_detail_subreddit";
}
