package pers.ervinse.shoppingmall.Comment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pers.ervinse.shoppingmall.R;
import pers.ervinse.shoppingmall.domain.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> commentList;

    public CommentAdapter() {

    }
    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public void setCommentList(List<Comment> comments) {
        this.commentList = comments;
        notifyDataSetChanged();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        if (commentList == null || position >= commentList.size()) {
            // 添加空指针检查
            return;
        }
        Comment comment = commentList.get(position);

        holder.usernameTextView.setText(String.valueOf(comment.getUserName()));
        holder.commentTextView.setText(String.valueOf(comment.getCommentText()));
        holder.useridTextView.setText(String.valueOf(comment.getUserId()));
        // 格式化时间戳为可读的时间格式
        String formattedTime = formatTimestamp(comment.getTimestamp());
        holder.timeTextView.setText(formattedTime);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTextView;
        public TextView useridTextView;
        public TextView commentTextView;
        public TextView timeTextView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            useridTextView = itemView.findViewById(R.id.useridTextView);
        }
    }


    // 添加格式化时间戳的方法
    private String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return ""; // 处理空时间戳的情况
        }

        // 使用 SimpleDateFormat 进行格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(timestamp.getTime());
    }

}
