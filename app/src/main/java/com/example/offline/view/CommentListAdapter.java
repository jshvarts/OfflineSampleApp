package com.example.offline.view;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.offline.R;
import com.example.offline.model.Comment;

import java.util.List;

import timber.log.Timber;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private final List<Comment> comments;

    public CommentListAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView commentText = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_row, parent, false);
        return new ViewHolder(commentText);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Comment comment = comments.get(position);
        if (comment.isSyncPending()) {
            holder.commentText.setTextColor(Color.LTGRAY);
        } else {
            holder.commentText.setTextColor(Color.BLACK);
        }
        holder.commentText.setText(comment.getCommentText());
    }

    @Override
    public int getItemCount() {
        return comments == null ? 0 : comments.size();
    }

    public void updateCommentList(List<Comment> newComments) {
        Timber.d("Got new comments " + newComments.size());
        this.comments.clear();
        this.comments.addAll(newComments);
        notifyDataSetChanged();
    }

    /**
     * View holder for shopping list items of this adapter
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView commentText;

        public ViewHolder(final TextView commentText) {
            super(commentText);
            this.commentText = commentText;
        }
    }
}
