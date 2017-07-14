# Android-Bless-Fresco-Selector

## 概述

### 基于 `Android-Bless-FrescoPlus` 的 Android 图片选择器。

充分自由定制，极大程度简化使用。支持功能：

- 多选
- 单选
- 预览
- 裁剪
- 拍照
- 自定义图片加载方式
- 自定义色调
- 沉浸式状态栏

图片加载需要自定义一个ImageLoader（可直接使用 FrescoPlus 默认的加载方式）, 也可通过Glide、Picasso等方式加载。

## 使用

### 配置权限

```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.CAMERA"/>
```

### 使用方式

#### 1、在Application初始化FrescoPlus
```java
@Override
public void onCreate() {
    super.onCreate();
	...
    FrescoInitializer.getInstance().init(this);
    ...
}
```

#### 2、在代码上使用
```java

// 自定义图片加载器
private ImageLoader imageLoader = new ImageLoader() {
    @Override
    public void displayImage(Context context, String path, FrescoImageView imageView) {
    	// 使用 FrescoPlus 默认的加载方式
        imageView.showImage(path);
    }
};

// 配置
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
                

// 进入图片选择器
ImageSelectActivity.startActivity(this, config, REQUEST_CODE);

```

```java
// 重写onActivityResult方法，获取选择的图片
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    // 图片选择结果回调
    if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
        List<String> pathList = data.getStringArrayListExtra(ImageSelectActivity.INTENT_RESULT);
        for (String path : pathList) {
            resultTextView.append(path + "\n");
        }
    }
}
```