package com.hazem.redditapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hazem.redditapp.R;
import com.hazem.redditapp.RedditApi;
import com.hazem.redditapp.model.post.Child;

import java.util.List;

/**
 * Created by Hazem Ali.
 * On 1/13/2018.
 */

public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentsRecyclerViewAdapter.ViewHolder> implements RedditApi.CallbackListener {

    private List<Child> childList;
    private Context context;

    public CommentsRecyclerViewAdapter(Context context, List<Child> childList) {
        this.context = context;
        this.childList = childList;
    }

    @Override
    public CommentsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentsRecyclerViewAdapter.ViewHolder holder, int position) {
        Child child = this.childList.get(position);
        holder.BindData(child);
    }

    @Override
    public int getItemCount() {
        return childList.size();
    }

    @Override
    public void OnResult(boolean success) {
        if (context instanceof Activity) {
            Toast.makeText(context, success ? context.getString(R.string.voted)
                    :context.getString(R.string.failed), Toast.LENGTH_SHORT).show();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView comment_owner, comment_body, vote_count;
        LinearLayout reply_btn;
        ImageButton up_vote, down_vote;


        ViewHolder(View itemView) {
            super(itemView);

            comment_owner = itemView.findViewById(R.id.comment_owner);
            comment_body = itemView.findViewById(R.id.comment_body);
            vote_count = itemView.findViewById(R.id.vote_count);
            reply_btn = itemView.findViewById(R.id.reply_btn);
            up_vote = itemView.findViewById(R.id.up_vote);
            down_vote = itemView.findViewById(R.id.down_vote);

        }


        void BindData(final Child child) {

            if (!child.kind.equals("t1")) return;

            comment_owner.setText(String.valueOf(child.data.author));
            comment_body.setText(String.valueOf(child.data.body));

            String c = String.valueOf(child.data.score);
            int count = child.data.score;
            if (count > 1000) {
                c = String.valueOf(count / 1000) + "K";
            }
            vote_count.setText(c);
            up_vote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (child.data.likes == null)
                        RedditApi.votePost(RedditApi.VOTE_UP, child.data.name, CommentsRecyclerViewAdapter.this);
                    else if (!(Boolean) child.data.likes) {
                        RedditApi.votePost(RedditApi.VOTE_UP, child.data.name, CommentsRecyclerViewAdapter.this);
                    } else
                        RedditApi.votePost(RedditApi.UN_VOTE, child.data.name, CommentsRecyclerViewAdapter.this);
                }
            });
            down_vote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (child.data.likes == null)
                        RedditApi.votePost(RedditApi.VOTE_DOWN, child.data.name, CommentsRecyclerViewAdapter.this);
                    else if ((Boolean) child.data.likes) {
                        RedditApi.votePost(RedditApi.VOTE_DOWN, child.data.name, CommentsRecyclerViewAdapter.this);
                    } else
                        RedditApi.votePost(RedditApi.VOTE_DOWN, child.data.name, CommentsRecyclerViewAdapter.this);
                }
            });

            reply_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddCommentDialog(child);
                }
            });

        }

        private void showAddCommentDialog(final Child child) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle(context.getString(R.string.add_comment));
            View view = LayoutInflater.from(context).inflate(R.layout.add_comment_layout,null);
            final EditText add_comment_ed = view.findViewById(R.id.add_comment_et);
            alertDialog.setView(view);
            alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (add_comment_ed.getText().toString().isEmpty()) {
                        Toast.makeText(context, context.getString(R.string.empty_string_error),
                                Toast.LENGTH_SHORT).show();
                    }else {
                        addComment(dialog,child,add_comment_ed);
                    }
                }
            });

            alertDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog.create();
            alertDialog.show();

        }

        private void addComment(final DialogInterface dialog, Child child, final EditText add_comment_et) {
            if (child == null) return;
            RedditApi.commentToThing(child.data.name, add_comment_et.getText().toString(),
                    new RedditApi.CallbackListener() {
                        @Override
                        public void OnResult(boolean success) {

                            if (success) dialog.dismiss();

                            add_comment_et.getText().clear();
                            Toast.makeText(context,
                                    success ? context.getString(R.string.comment_success) :
                                            context.getString(R.string.error_comment), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }


}
