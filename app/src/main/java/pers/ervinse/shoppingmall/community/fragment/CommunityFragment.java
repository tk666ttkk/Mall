package pers.ervinse.shoppingmall.community.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Random;

import pers.ervinse.shoppingmall.BaseFragment;
import pers.ervinse.shoppingmall.R;

public class CommunityFragment extends BaseFragment {

    private EditText editTextName, editTextDescription, editTextPrice, editTextLocation;
    private Button buttonPublish;


    private TextView tvImageNumber;
    private int selectedImageNumber = -1; // 初始值设为-1，表示未选择图片


    @Override
    public View initView() {
            // 使用 inflater 对象进行布局的加载
            View view = View.inflate(mContext, R.layout.fragment_community, null);

            editTextName = view.findViewById(R.id.etProductName);
            editTextDescription = view.findViewById(R.id.etProductDescription);
            editTextPrice = view.findViewById(R.id.etProductPrice);
            editTextLocation = view.findViewById(R.id.etProductLocation);
            buttonPublish = view.findViewById(R.id.btnPublish);
            tvImageNumber = view.findViewById(R.id.tvImageNumber);


            buttonPublish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 获取用户输入的物品信息
                    String name = editTextName.getText().toString();
                    String description = editTextDescription.getText().toString();
                    String priceStr = editTextPrice.getText().toString();
                    String location = editTextLocation.getText().toString();

                    // TODO: 将物品信息插入数据库
                    saveData();

                    // 发布成功后，你可能需要清空输入框或进行其他操作
                    refreshData();
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


}
