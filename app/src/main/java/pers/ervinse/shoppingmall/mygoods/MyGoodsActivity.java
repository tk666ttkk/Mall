package pers.ervinse.shoppingmall.mygoods;

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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pers.ervinse.shoppingmall.Comment.adapter.CommentAdapter;
import pers.ervinse.shoppingmall.R;
import pers.ervinse.shoppingmall.domain.Comment;
import pers.ervinse.shoppingmall.domain.Goods;
import pers.ervinse.shoppingmall.domain.User;
import pers.ervinse.shoppingmall.mygoods.MyGoodsAdapter;
import pers.ervinse.shoppingmall.type.fragment.TypeFragment;
import pers.ervinse.shoppingmall.utils.OkhttpUtils;
import pers.ervinse.shoppingmall.utils.PropertiesUtils;

// MyGoodsActivity.java
public class MyGoodsActivity extends AppCompatActivity {

    private static final String TAG = TypeFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private MyGoodsAdapter myGoodsAdapter;
    private Context mContext;
    private Handler handler = new Handler();
    private  List<Goods> goodsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goods);

        // 获取上下文
        mContext = this;

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myGoodsAdapter = new MyGoodsAdapter(goodsList, this);
        recyclerView.setAdapter(myGoodsAdapter);

        // Assuming you have a method to refresh the data
        refreshData();
    }

    // Replace this method with your actual method to fetch goods data
    private List<Goods> getGoodsList() {
        // TODO: Implement your logic to get the list of goods from the database or elsewhere
        List<Goods> goodsList = new ArrayList<>();
        refreshData();
        return goodsList;
    }

    public void refreshData() {
        Log.i(TAG, "联网刷新数据");
        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "进入获取商品线程");

                User user = User.getInstance();
                String username = user.getName();

                Gson gson = new Gson();
                String responseJson = null;

                try {
                    //发送获取商品请求
                    String url = PropertiesUtils.getUrl(mContext);
                    responseJson = OkhttpUtils.doGet(url + "/goods/getByUserName?UserName=" + username);
                    Log.i(TAG, "获取商品响应json:" + responseJson);
                    goodsList = gson.fromJson(responseJson, new TypeToken<List<Goods>>() {
                    }.getType());
                    Log.i(TAG, "获取商品响应解析对象:" + goodsList);

                    //切回主线程更新视图
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //更新数据
                            myGoodsAdapter.setGoodsList(goodsList);
                            myGoodsAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(mContext, "获取数据失败,服务器错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }.start();
    }
}
