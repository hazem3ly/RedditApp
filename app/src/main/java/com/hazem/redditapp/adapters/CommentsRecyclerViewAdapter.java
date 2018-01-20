package com.hazem.redditapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hazem.redditapp.R;
import com.hazem.redditapp.model.post.Child;

import java.util.List;

/**
 * Created by Hazem Ali.
 * On 1/13/2018.
 */

public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentsRecyclerViewAdapter.ViewHolder> {

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
                    Toast.makeText(context, "up", Toast.LENGTH_SHORT).show();
                }
            });
            down_vote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "down", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
