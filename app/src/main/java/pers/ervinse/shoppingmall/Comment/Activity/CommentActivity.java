package pers.ervinse.shoppingmall.Comment.Activity;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pers.ervinse.shoppingmall.Comment.adapter.CommentAdapter;
import pers.ervinse.shoppingmall.R;
import pers.ervinse.shoppingmall.domain.Comment;
import pers.ervinse.shoppingmall.utils.OkhttpUtils;
import pers.ervinse.shoppingmall.utils.PropertiesUtils;

public class CommentActivity extends AppCompatActivity {

    private  String productId;

    private Context mContext;
    private Handler handler = new Handler();
    private CommentAdapter CommentAdapter;
    private  List<Comment> commentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        mContext = this;
        // 获取传递过来的商品ID
         productId = getIntent().getStringExtra("productId");

        // 初始化RecyclerView和Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerViewComment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 这里假设您有一个 CommentAdapter 类，可以根据您的实际情况进行修改
        CommentAdapter = new CommentAdapter(getCommentData(productId));
        recyclerView.setAdapter(CommentAdapter);
    }

    // 这里模拟从后端获取评论数据的方法，根据实际情况修改
    private List<Comment> getCommentData(String productId) {
        // 在这里调用后端接口，根据商品ID获取评论数据
        // 返回一个 Comment 对象的列表，Comment 类根据您的实际情况定义
        // 这里只是一个示例，您需要替换成真实的后端接口调用
        commentList = new ArrayList<>();
        refreshCommentData(productId);
        return commentList;
    }

    // 将 Comment 对象转换为 JSON 字符串，这里可以使用 Gson 等库w
    private String convertCommentToJson(Comment comment) {
        Gson gson = new Gson();
        return gson.toJson(comment);
    }
    /**
     * 刷新商品评论数据
     */
    public void refreshCommentData(String productId) {
        Log.i(TAG, "联网刷新评论数据");
        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "进入获取商品评论线程");

                Gson gson = new Gson();
                String responseJson = null;

                try {
                    // 发送获取商品评论请求
                    String url = PropertiesUtils.getUrl(mContext);
                    responseJson = OkhttpUtils.doGet(url + "/comments/getByid?productId=" + productId);
                    Log.i(TAG, "获取商品评论响应json:" + responseJson);

                    // 将获取到的评论数据解析成 List<Comment>
                    commentList = gson.fromJson(responseJson, new TypeToken<List<Comment>>() {
                    }.getType());
                    Log.i(TAG, "获取商品评论响应解析对象:" + commentList);

                    // 切回主线程更新视图
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // 更新评论数据
                            CommentAdapter.setCommentList(commentList);
                            // 刷新评论视图
                            CommentAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(mContext, "获取评论数据失败,服务器错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }.start();
    }

}
