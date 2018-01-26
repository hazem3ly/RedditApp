package com.hazem.redditapp.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.RemoteViews;

import com.hazem.redditapp.R;
import com.hazem.redditapp.data_base.RedditContract;


public class SavedRemoteViewsService extends android.widget.RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewFactory(this.getApplicationContext());
    }

    private class RemoteViewFactory implements RemoteViewsFactory {

        Context mContext;
        Cursor dataCursor;

        RemoteViewFactory(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (dataCursor != null) dataCursor.close();
            dataCursor = mContext.getContentResolver().query(
                    RedditContract.USER_SAVED_TABLE.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onDestroy() {
            if (dataCursor != null)
                dataCursor.close();
        }

        @Override
        public int getCount() {
            if (dataCursor == null) return 0;
            return dataCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (dataCursor == null || dataCursor.getCount() == 0) return null;
            dataCursor.moveToPosition(position);

            int postTitleIndx = dataCursor.getColumnIndex(RedditContract.USER_SAVED_TABLE.POST_TITLE);
            int subredditIndx = dataCursor.getColumnIndex(RedditContract.USER_SAVED_TABLE.SUBREDDIT_NAME);
            int isPostIndx = dataCursor.getColumnIndex(RedditContract.USER_SAVED_TABLE.IS_POST);
            int user_commentIndx = dataCursor.getColumnIndex(RedditContract.USER_SAVED_TABLE.USER_COMMENT);

            String postTitle = dataCursor.getString(postTitleIndx);
            String subreddit = dataCursor.getString(subredditIndx);
            String isPost = dataCursor.getString(isPostIndx);
            String user_comment = dataCursor.getString(user_commentIndx);


            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.user_saved_item_widget);


            if (isPost != null && isPost.equals("0")) {
                views.setViewVisibility(R.id.user_comment, View.VISIBLE);
                views.setTextViewText(R.id.user_comment, String.valueOf(user_comment));
            }

            views.setTextViewText(R.id.post_title, String.valueOf(postTitle));
            views.setTextViewText(R.id.subreddit_name, String.valueOf(subreddit));


            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
