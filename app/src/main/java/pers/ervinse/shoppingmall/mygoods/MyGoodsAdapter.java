package pers.ervinse.shoppingmall.mygoods;

import static android.content.ContentValues.TAG;
import okhttp3.Request;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import pers.ervinse.shoppingmall.GoodsInfoActivity;
import pers.ervinse.shoppingmall.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import pers.ervinse.shoppingmall.domain.Comment;
import pers.ervinse.shoppingmall.domain.Goods;
import pers.ervinse.shoppingmall.domain.User;
import pers.ervinse.shoppingmall.utils.OkhttpUtils;
import pers.ervinse.shoppingmall.utils.PropertiesUtils;

public class MyGoodsAdapter extends RecyclerView.Adapter<MyGoodsAdapter.ViewHolder> {

    private  List<Goods> goodsList;
    private  Context mContext;

    // 修改构造函数，同时接收商品数据和上下文
    public MyGoodsAdapter(List<Goods> goodsList, Context context) {
        this.goodsList = goodsList;
        this.mContext = context;
    }
    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_mygoods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Goods goods = goodsList.get(position);

        int id = mContext.getResources().getIdentifier(goodsList.get(position).getImage(), "drawable", mContext.getPackageName());
        holder.imageViewGoods.setImageResource(id);
        holder.textViewGoodsName.setText(goods.getName());
        holder.textViewGoodsDescription.setText(goods.getDescription());
        holder.textViewGoodsPrice.setText("$ " + goods.getPrice());

        holder.itemView.setOnClickListener(view -> {
            // Handle item click
        });
    }

        @Override
        public int getItemCount() {
            return goodsList != null ? goodsList.size() : 0;
        }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewGoods;
        TextView textViewGoodsName;
        TextView textViewGoodsDescription;
        TextView textViewGoodsPrice;
        private Button item_add_cart_btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewGoods = itemView.findViewById(R.id.item_image);
            textViewGoodsName = itemView.findViewById(R.id.item_name);
            textViewGoodsDescription = itemView.findViewById(R.id.item_description);
            textViewGoodsPrice = itemView.findViewById(R.id.item_price);
            item_add_cart_btn = itemView.findViewById(R.id.delete_button);

            /**
             * item点击事件监听
             */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Goods goodsByClick = goodsList.get(getLayoutPosition());
                    Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                    intent.putExtra("mygoods", goodsByClick);
                    mContext.startActivity(intent);
                }
            });

            // 删除按钮点击事件监听
            item_add_cart_btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    User user = User.getInstance();
                    String name11 = user.getName();

                    if(name11.isEmpty()){
                        Toast.makeText(mContext, "请登录再来查看", Toast.LENGTH_SHORT).show();
                    }
                    // 获取当前商品名
                    Goods goodsByClick = goodsList.get(getLayoutPosition());
                    String name = goodsByClick.getName();

                    // 发送删除商品请求
                    deleteGoodsByName(name);
                }
            });
        }

        // 将删除商品的逻辑提取成一个方法
        private void deleteGoodsByName(String name) {

            new Thread() {
                @Override
                public void run() {
                    Log.i(TAG, "进入删除商品线程");
                    try {
                        String url = PropertiesUtils.getUrl(mContext);
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(url + "/goods/deleteByName?Name=" + name)
                                .delete()
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseBody = response.body().string();

                        new Handler(Looper.getMainLooper()).post(() -> {
                            // 处理响应，根据需要进行操作
                            if ("true".equals(responseBody)) {
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        new Handler(Looper.getMainLooper()).post(() -> {
                            Toast.makeText(mContext, "获取数据失败,服务器错误", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            }.start();
        }
    }
}
