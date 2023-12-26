package pers.ervinse.shoppingmall.Comment.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pers.ervinse.shoppingmall.Comment.adapter.CommentAdapter;
import pers.ervinse.shoppingmall.R;
import pers.ervinse.shoppingmall.domain.Comment;

public class CommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // 获取传递过来的商品ID
        String productId = getIntent().getStringExtra("productId");

        // 初始化RecyclerView和Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerViewComment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 这里假设您有一个 CommentAdapter 类，可以根据您的实际情况进行修改
        CommentAdapter commentAdapter = new CommentAdapter(getCommentData(productId));
        recyclerView.setAdapter(commentAdapter);
    }

    // 这里模拟从后端获取评论数据的方法，根据实际情况修改
    private List<Comment> getCommentData(String productId) {
        // 在这里调用后端接口，根据商品ID获取评论数据
        // 返回一个 Comment 对象的列表，Comment 类根据您的实际情况定义
        // 这里只是一个示例，您需要替换成真实的后端接口调用
        List<Comment> commentList = new ArrayList<>();
        commentList.add(new Comment(5,2,4,"Good product!"));
        commentList.add(new Comment(6,2,4,"Good product2!"));
        return commentList;
    }
}
