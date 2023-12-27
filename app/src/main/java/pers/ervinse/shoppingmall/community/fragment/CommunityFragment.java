package pers.ervinse.shoppingmall.community.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import pers.ervinse.shoppingmall.BaseFragment;
import pers.ervinse.shoppingmall.R;
import pers.ervinse.shoppingmall.domain.Goods;
import pers.ervinse.shoppingmall.domain.GoodsOPT;
import pers.ervinse.shoppingmall.domain.User;
import pers.ervinse.shoppingmall.utils.OkhttpUtils;
import pers.ervinse.shoppingmall.utils.PropertiesUtils;

public class CommunityFragment extends BaseFragment {

    private EditText editTextName, editTextDescription, editTextPrice, editTextLocation,editTextType;
    private Button btnUploadImage, btnPublish,btCancle;
    private ImageView ivProductImage;
    private TextView tvImageNumber;
    private int selectedImageNumber = -1;
    // 请求代码用于识别选择图片的返回结果
    private static final int PICK_IMAGE_REQUEST = 1;

    private Handler handler = new Handler(); // Add this line

    @Override
    public View initView() {
            // 使用 inflater 对象进行布局的加载
            View view = View.inflate(mContext, R.layout.fragment_community, null);

            editTextName = view.findViewById(R.id.etProductName);
            editTextDescription = view.findViewById(R.id.etProductDescription);
            editTextPrice = view.findViewById(R.id.etProductPrice);
            editTextLocation = view.findViewById(R.id.etProductLocation);
            btnUploadImage = view.findViewById(R.id.btnUploadImage);
            ivProductImage = view.findViewById(R.id.ivProductImage);
            tvImageNumber = view.findViewById(R.id.tvImageNumber);
            btnPublish = view.findViewById(R.id.btnPublish);
            btCancle= view.findViewById(R.id.btnCancel);
            editTextType = view.findViewById(R.id.etProductType);

            //上传图片
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理上传图片的逻辑，打开文件选择器
                openFileChooser();
            }
        });

        //取消操作
        btCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearData();
            }
        });
             btnPublish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = User.getInstance();
                    // 获取要删除评论的信息
                    String userName = user.getName();
                    if (user != null && !TextUtils.isEmpty(user.getName())) {
                        // 获取用户输入的物品信息
                        String name = editTextName.getText().toString();
                        String description = editTextDescription.getText().toString();
                        String priceStr = editTextPrice.getText().toString();
                        String location = editTextLocation.getText().toString();
                        String imgnumber = tvImageNumber.getText().toString();
                        String type = editTextType.getText().toString();

                        // 构建 Goods 对象
                        GoodsOPT newGoods = new GoodsOPT(name, description, location, imgnumber, Double.parseDouble(priceStr), type);

                        // 调用后端接口添加商品
                        addGoodsToDatabase(newGoods);

                        clearData();
                    } else {
                        Toast.makeText(mContext, "未登录不能发布", Toast.LENGTH_SHORT).show();
                        clearData();
                    }
                }
            });
            return view;
    }

    @Override
    public void refreshData() {
        // 实现刷新数据的逻辑


    }

    @Override
    public void saveData() {
        // 实现保存数据到数据库的逻辑


    }
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            // 获取选择的图片的 Uri
            Uri selectedImageUri = data.getData();

            // 获取图片文件名
            String imageName = getFileName(selectedImageUri);

            // 显示图片
            ivProductImage.setImageURI(selectedImageUri);

            // 在这里可以根据需要进行其他操作，例如保存图片信息等
            // 这里简单示范一个获取随机图片编号的逻辑
            selectedImageNumber = getRandomImageNumber();
            updateImageNumber(imageName);
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private int getRandomImageNumber() {
        return (int) (Math.random() * 1000);
    }

    private void updateImageNumber(String imageName) {
        if (selectedImageNumber != -1) {
            // 去掉文件名的后缀部分
            String fileNameWithoutExtension = removeFileExtension(imageName);

            // 显示图片编号和图片文件名（去掉后缀）
            tvImageNumber.setText( fileNameWithoutExtension);
        } else {
            tvImageNumber.setText("");
        }
    }

    // 去掉文件名的后缀部分
    private String removeFileExtension(String fileName) {
        if (fileName.contains(".")) {
            return fileName.substring(0, fileName.lastIndexOf('.'));
        }
        return fileName;
    }

    //取消
    private void clearData() {
        editTextName.setText("");
        editTextDescription.setText("");
        editTextPrice.setText("");
        editTextLocation.setText("");
        editTextType.setText("");
        ivProductImage.setImageDrawable(null);
        tvImageNumber.setText("");
        selectedImageNumber = -1;
    }


    private void addGoodsToDatabase(GoodsOPT goods) {
        // 将商品信息通过 addGoods 接口添加到数据库
        // 注意：这里需要使用异步任务或其他异步机制进行网络请求
        // 以下是一个简单的示例，实际中你可能需要使用 AsyncTask 或其他方式来异步执行网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 构建 JSON 字符串或其他合适的数据格式
                String jsonData = convertGoodsToJson(goods);

                try {
                    // 发送网络请求，将商品信息添加到数据库
                    String url = PropertiesUtils.getUrl(mContext);
                    String response = OkhttpUtils.doPost(url + "/goods/addGoods", jsonData);

                    // 处理服务器返回的结果，根据实际情况进行相应的操作
                    if ("success".equals(response)) {
                        // 发布成功，可以在这里进行一些界面上的操作，例如清空输入框
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                clearData();
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
                                Toast.makeText(mContext, "发布成功，到首页查看", Toast.LENGTH_SHORT).show();
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

    // 将 Goods 对象转换为 JSON 字符串，这里可以使用 Gson 等库
    private String convertGoodsToJson(GoodsOPT goods) {
        Gson gson = new Gson();
        return gson.toJson(goods);
    }

}
