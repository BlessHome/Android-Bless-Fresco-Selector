package com.bless.fresco.selector.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bless.fresco.selector.ImageSelectActivity;
import com.bless.fresco.selector.ImageSelectConfig;
import com.bless.fresco.selector.common.ImageLoader;
import com.bless.fresco.selector.crop.Crop;
import com.bless.fresco.widget.FrescoImageView;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;
    private TextView tvResult;
    private FrescoImageView draweeView;
    private ImageLoader loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, FrescoImageView imageView) {
            imageView.showImage(path);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        tvResult = (TextView) findViewById(R.id.tvResult);
        draweeView = (FrescoImageView) findViewById(R.id.my_image_view);
        draweeView.showImage("/storage/sdcard1/Android/data/com.bless.fresco.selector.app/cache/1500001339547.jpg");
    }

    public void Multiselect(View view) {
        tvResult.setText("");
        ImageSelectConfig config = new ImageSelectConfig.Builder(this, loader)
                .multiSelect()
                .confirmBackgroundColor(Color.GREEN)
                // 是否记住上次选中记录
                .rememberSelected(true)
                // 最大选择图片数量
                .maxNum(2)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5")).build();

        ImageSelectActivity.startActivity(this, config, REQUEST_CODE);
    }

    public void Single(View view) {
        tvResult.setText("");
        ImageSelectConfig config = new ImageSelectConfig.Builder(this, loader)
                // 单选
                .singleSelect()
                // 多选
                .multiSelect()
                // 确定按钮文字
                .confirmText("确定")
                // 确定按钮背景色
                .confirmBackgroundColor(Color.GREEN)
                // 确定按钮文字颜色
                .confirmTextColor(Color.WHITE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3369FF"))
                // 返回图标ResId
                .backImageResourceId(R.drawable.image_select_ic_back)
                // 标题文字
                .titleText("图片")
                // 标题文字颜色
                .titleTextColor(Color.WHITE)
                // 标题背景颜色
                .titleBackgroundColor(Color.parseColor("#3369FF"))
                // 是否需要裁剪
                .needCrop(true)
                // 是否使用系统的裁剪功能
                .useSystemCrop(false)
                // 裁剪尺寸
                .cropSize(1, 1, 200, 200)
                // 第一个是否显示相机
                .needCamera(true)
                // 最大选择图片数量
                .maxNum(9)
                .build();

        ImageSelectActivity.startActivity(this, config, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectActivity.INTENT_RESULT);

            // 测试Fresco。可不理会
             draweeView.setImageURI(Uri.parse("file://"+pathList.get(0)));
            for (String path : pathList) {
                tvResult.append(path + "\n");
            }
        }

        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    public void onPick(View view) {
        Crop.pickImage(this);
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "crop-" + TimeUnit.MILLISECONDS + ".jpg"));
        Crop.of(source, destination).withAspect(4, 3).withOutputSize(1024, 768).start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Toast.makeText(this, Crop.getOutput(result).toString(), Toast.LENGTH_SHORT).show();
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
