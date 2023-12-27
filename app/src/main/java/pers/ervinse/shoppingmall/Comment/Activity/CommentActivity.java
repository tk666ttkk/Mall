package pers.ervinse.shoppingmall.Comment.Activity;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import pers.ervinse.shoppingmall.domain.GoodsOPT;
import pers.ervinse.shoppingmall.domain.User;
import pers.ervinse.shoppingmall.utils.OkhttpUtils;
import pers.ervinse.shoppingmall.utils.PropertiesUtils;

public class CommentActivity extends AppCompatActivity {

    private  String productId;

    private Context mContext;
    private Handler handler = new Handler();
    private CommentAdapter CommentAdapter;
    private  List<Comment> commentList;

    private EditText commentEditText;
    private Button publishButton;
    private Button deleteButton;


    @SuppressLint("MissingInflatedId")
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

        // 初始化评论输入框和按钮
        commentEditText = findViewById(R.id.commentEditText);
        publishButton = findViewById(R.id.publishButton);
        deleteButton = findViewById(R.id.deleteButton);


        // 设置按钮点击事件
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = commentEditText.getText().toString();
                User user = User.getInstance();

                if (user != null && !TextUtils.isEmpty(user.getName())) {
                    // 发布评论的逻辑
                    if (!TextUtils.isEmpty(commentText)) {
                        // 调用后端接口发布评论
                        int pid = Integer.parseInt(productId);
                        // 构建 Goods 对象
                        Comment comment = new Comment(user.getId(), pid, user.getName(), commentText);

                        // 调用后端接口添加商品
                        addCommentsToDatabase(comment);

                        commentEditText.setText("");

                        // 刷新评论数据
                        refreshCommentData(productId);

                    } else {
                        Toast.makeText(mContext, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "未登录不能进行评论", Toast.LENGTH_SHORT).show();
                }
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = User.getInstance();
                // 获取要删除评论的信息
                String userName = user.getName();
                if(user != null && !TextUtils.isEmpty(user.getName())){
                    int pid = Integer.parseInt(productId);
                    // 构建评论对象
                    Comment commentToDelete = new Comment();

                    commentToDelete.setUserName(userName);
                    commentToDelete.setProductId(pid);

                    // 设置其他评论信息...

                    // 发送删除评论的请求
                    deleteComment(commentToDelete);

                    // 刷新评论数据
                    refreshCommentData(productId);
                }
                else{
                    Toast.makeText(mContext, "未登录不能进行删除", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
    private void addCommentsToDatabase(Comment comment) {
        // 将商品信息通过 addGoods 接口添加到数据库
        // 注意：这里需要使用异步任务或其他异步机制进行网络请求
        // 以下是一个简单的示例，实际中你可能需要使用 AsyncTask 或其他方式来异步执行网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 构建 JSON 字符串或其他合适的数据格式
                String jsonData = convertGoodsToJson(comment);

                try {
                    // 发送网络请求，将商品信息添加到数据库
                    String url = PropertiesUtils.getUrl(mContext);
                    String response = OkhttpUtils.doPost(url + "/comments/addComment", jsonData);

                    // 处理服务器返回的结果，根据实际情况进行相应的操作
                    if ("success".equals(response)) {
                        // 发布成功，可以在这里进行一些界面上的操作，例如清空输入框
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // 处理发布失败的情况
                        // 这里可以添加一些提示用户的逻辑，例如 Toast 提示
                        // 注意：在这里不能直接操作 UI，需要通过 Handler 在主线程中执行
                        // 例如：
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    // 处理网络请求异常，例如 Toast 提示
                    // 注意：在这里不能直接操作 UI，需要通过 Handler 在主线程中执行
                    // 例如：
                    handler.post(new Runnable() {s
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "网络请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    // 将 Goods 对象转换为 JSON 字符串，这里可以使用 Gson 等库
    private String convertGoodsToJson(Comment comment) {
        Gson gson = new Gson();
        return gson.toJson(comment);
    }

    private void deleteComment(Comment commentToDelete) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 构建 JSON 字符串或其他合适的数据格式
                String jsonData = convertGoodsToJson(commentToDelete);
                String username = commentToDelete.getUserName();
                int pid = commentToDelete.getProductId();

                try {
                    // 发送网络请求，将商品信息添加到数据库
                    String url = PropertiesUtils.getUrl(mContext);

                    String response = OkhttpUtils.doGet(url + "/comments/deleteCommentByName?Username=" + username + "&productId=" + pid);

                    // 处理服务器返回的结果，根据实际情况进行相应的操作
                    if ("success".equals(response)) {
                        // 发布成功，可以在这里进行一些界面上的操作，例如清空输入框
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // 处理发布失败的情况
                        // 这里可以添加一些提示用户的逻辑，例如 Toast 提示
                        // 注意：在这里不能直接操作 UI，需要通过 Handler 在主线程中执行
                        // 例如：
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    // 处理网络请求异常，例如 Toast 提示
                    // 注意：在这里不能直接操作 UI，需要通过 Handler 在主线程中执行
                    // 例如：
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "网络请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}
