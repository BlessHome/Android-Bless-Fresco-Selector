package com.bless.fresco.selector;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bless.fresco.selector.common.ImageSelectCallback;
import com.bless.fresco.selector.common.ImageSelectConstant;
import com.bless.fresco.selector.crop.Crop;
import com.bless.fresco.selector.utils.FileUtils;
import com.bless.fresco.selector.utils.StatusBarCompat;

import java.io.File;
import java.util.ArrayList;


/**
 * <pre>
 *
 * 作者:      ASLai(gdcpljh@126.com).
 * 日期:      17-7-14
 * 版本:      V1.0
 * 描述:      图像选择
 *
 * </pre>
 */
public class ImageSelectActivity extends FragmentActivity implements View.OnClickListener, ImageSelectCallback {

    /**
     * The constant INTENT_RESULT.
     */
    public static final String INTENT_RESULT = "result";
    private static final int IMAGE_CROP_CODE = 1;
    private static final int STORAGE_REQUEST_CODE = 1;

    private ImageSelectConfig config;

    private RelativeLayout _ImageSelectTitlebarRelativelayout;
    private TextView _ImageSelectTitleTextview;
    private Button _ImageSelectConfirmButton;
    private ImageView _ImageSelectBackImageview;
    private String cropImagePath;

    private ImageSelectFragment fragment;

    private ArrayList<String> result = new ArrayList<>();

    /**
     * Start activity.
     *
     * @param activity    the activity
     * @param config      the config
     * @param RequestCode the request code
     */
    public static void startActivity(Activity activity, ImageSelectConfig config, int RequestCode) {
        Intent intent = new Intent(activity, ImageSelectActivity.class);
        ImageSelectConstant.config = config;
        activity.startActivityForResult(intent, RequestCode);
    }

    /**
     * Start activity.
     *
     * @param fragment    the fragment
     * @param config      the config
     * @param RequestCode the request code
     */
    public static void startActivity(Fragment fragment, ImageSelectConfig config, int RequestCode) {
        Intent intent = new Intent(fragment.getActivity(), ImageSelectActivity.class);
        ImageSelectConstant.config = config;
        fragment.startActivityForResult(intent, RequestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_select_activity);
        config = ImageSelectConstant.config;

        // Android 6.0 checkSelfPermission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_REQUEST_CODE);
        } else {
            fragment = ImageSelectFragment.instance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.image_select_imageList_fragment, fragment, null)
                    .commit();
        }

        initView();
        if (!FileUtils.isSdCardAvailable()) {
            Toast.makeText(this, getString(R.string.image_select_text_sd_disable), Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        _ImageSelectTitlebarRelativelayout = (RelativeLayout) findViewById(R.id.image_select_titleBar_relativeLayout);
        _ImageSelectTitleTextview = (TextView) findViewById(R.id.image_select_title_textView);

        _ImageSelectConfirmButton = (Button) findViewById(R.id.image_select_confirm_button);
        _ImageSelectConfirmButton.setOnClickListener(this);

        _ImageSelectBackImageview = (ImageView) findViewById(R.id.image_select_back_imageView);
        _ImageSelectBackImageview.setOnClickListener(this);

        if (config != null) {
            if (config.getBackImageResourceId() != -1) {
                _ImageSelectBackImageview.setImageResource(config.getBackImageResourceId());
            }

            if (config.getStatusBarColor() != -1) {
                StatusBarCompat.compat(this, config.getStatusBarColor());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                        && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                }
            }
            _ImageSelectTitlebarRelativelayout.setBackgroundColor(config.getTitleBackgroundColor());
            _ImageSelectTitleTextview.setTextColor(config.getTitleTextColor());
            _ImageSelectTitleTextview.setText(config.getTitleText());
            _ImageSelectConfirmButton.setBackgroundColor(config.getConfirmBackgroundColor());
            _ImageSelectConfirmButton.setTextColor(config.getConfirmTextColor());

            if (config.isMultiSelect()) {
                if (!config.isRememberSelected()) {
                    ImageSelectConstant.imageList.clear();
                }
                _ImageSelectConfirmButton.setText(String.format(getString(R.string.image_select_text_confirm_format), config.getConfirmText(), ImageSelectConstant.imageList.size(), config.getMaxNum()));
            } else {
                ImageSelectConstant.imageList.clear();
                _ImageSelectConfirmButton.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.image_select_confirm_button) {
            if (ImageSelectConstant.imageList != null && !ImageSelectConstant.imageList.isEmpty()) {
                exit();
            } else {
                Toast.makeText(this, getString(R.string.image_select_text_minNum), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.image_select_back_imageView) {
            onBackPressed();
        }
    }

    @Override
    public void onSingleImageSelected(String path) {
        if (config.isNeedCrop()) {
            crop(path);
        } else {
            ImageSelectConstant.imageList.add(path);
            exit();
        }
    }

    @Override
    public void onImageSelected(String path) {
        _ImageSelectConfirmButton.setText(String.format(getString(R.string.image_select_text_confirm_format), config.getConfirmText(), ImageSelectConstant.imageList.size(), config.getMaxNum()));
    }

    @Override
    public void onImageUnselected(String path) {
        _ImageSelectConfirmButton.setText(String.format(getString(R.string.image_select_text_confirm_format), config.getConfirmText(), ImageSelectConstant.imageList.size(), config.getMaxNum()));
    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            if (config.isNeedCrop()) {
                crop(imageFile.getAbsolutePath());
            } else {
                ImageSelectConstant.imageList.add(imageFile.getAbsolutePath());
                config.setMultiSelect(false); // 多选点击拍照，强制更改为单选
                exit();
            }
        }
    }

    @Override
    public void onPreviewChanged(int select, int sum, boolean visible) {
        if (visible) {
            _ImageSelectTitleTextview.setText(select + "/" + sum);
        } else {
            _ImageSelectTitleTextview.setText(config.getTitleText());
        }
    }

    private void crop(String imagePath) {
        File file = new File(config.getFolderPath(), "crop-" + System.currentTimeMillis() + ".jpg");

        cropImagePath = file.getAbsolutePath();

        Uri source = getImageContentUri(new File(imagePath));
        Uri destination = Uri.fromFile(file);

        if (config.isUseSystemCrop()) {
            Crop.of(source, destination)
                    .withAspect(config.getAspectX(), config.getAspectY())
                    .withOutputSize(config.getOutputX(), config.getOutputY())
                    .startSystemCrop(this, IMAGE_CROP_CODE);
        } else {
            Crop.of(source, destination)
                    .withAspect(config.getAspectX(), config.getAspectY())
                    .withOutputSize(config.getOutputX(), config.getOutputY())
                    .start(this, IMAGE_CROP_CODE);
        }
    }

    /**
     * Gets image content uri.
     *
     * @param imageFile the image file
     * @return the image content uri
     */
    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_CROP_CODE && resultCode == RESULT_OK) {
            ImageSelectConstant.imageList.add(cropImagePath);
            config.setMultiSelect(false); // 多选点击拍照，强制更改为单选
            exit();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Exit.
     */
    public void exit() {
        Intent intent = new Intent();
        result.clear();
        result.addAll(ImageSelectConstant.imageList);
        intent.putStringArrayListExtra(INTENT_RESULT, result);
        setResult(RESULT_OK, intent);

        if (!config.isMultiSelect()) {
            ImageSelectConstant.imageList.clear();
        }

        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_REQUEST_CODE:
                if (grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.image_select_imageList_fragment, ImageSelectFragment.instance(), null)
                            .commitAllowingStateLoss();
                } else {
                    Toast.makeText(this, getString(R.string.image_select_text_permission_storage_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (fragment == null || !fragment.hidePreview()) {
            ImageSelectConstant.imageList.clear();
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageSelectConstant.config = null;
    }
}
