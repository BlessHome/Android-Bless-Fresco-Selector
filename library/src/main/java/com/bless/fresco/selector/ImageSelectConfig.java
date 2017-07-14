package com.bless.fresco.selector;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;

import com.bless.fresco.selector.common.ImageLoader;
import com.bless.fresco.selector.utils.FileUtils;

import java.io.Serializable;

/**
 * <pre>
 *
 * 作者:      ASLai(gdcpljh@126.com).
 * 日期:      17-7-13
 * 版本:      V1.0
 * 描述:      图像选择配置
 *
 * </pre>
 */
public class ImageSelectConfig {
    /**
     * 是否需要裁剪
     */
    private boolean needCrop;

    /**
     * 是否使用系统裁剪功能
     */
    private boolean useSystemCrop;

    /**
     * 是否多选
     */
    private boolean multiSelect = false;

    /**
     * 是否记住上次的选中记录(只对多选有效)
     */
    private boolean rememberSelected = true;

    /**
     * 最多选择图片数
     */
    private int maxNum = 9;

    /**
     * 第一个item是否显示相机
     */
    private boolean needCamera;

    private int statusBarColor = -1;

    /**
     * 返回图标资源
     */
    private int backImageResourceId = -1;

    /**
     * 标题
     */
    private String titleText;

    /**
     * 标题颜色
     */
    private int titleTextColor;

    /**
     * titlebar背景色
     */
    private int titleBackgroundColor;

    private String confirmText;

    /**
     * 确定按钮文字颜色
     */
    private int confirmTextColor;

    /**
     * 确定按钮背景色
     */
    private int confirmBackgroundColor;

    private String allImagesText;

    /**
     * 拍照存储路径
     */
    private String folderPath;

    /**
     * 自定义图片加载器
     */
    private ImageLoader imageLoader;

    /**
     * 裁剪输出大小
     */
    private int aspectX = 1;
    private int aspectY = 1;
    private int outputX = 500;
    private int outputY = 500;

    private ImageSelectConfig(Builder builder) {
        this.needCrop = builder.needCrop;
        this.useSystemCrop = builder.useSystemCrop;
        this.multiSelect = builder.multiSelect;
        this.rememberSelected = builder.rememberSelected;
        this.maxNum = builder.maxNum;
        this.needCamera = builder.needCamera;
        this.statusBarColor = builder.statusBarColor;
        this.backImageResourceId = builder.backImageResourceId;
        this.titleText = builder.titleText;
        this.titleBackgroundColor = builder.titleBackgroundColor;
        this.titleTextColor = builder.titleTextColor;
        this.confirmText = builder.confirmText;
        this.confirmBackgroundColor = builder.confirmBackgroundColor;
        this.confirmTextColor = builder.confirmTextColor;
        this.allImagesText = builder.allImagesText;
        this.folderPath = builder.folderPath;
        this.imageLoader = builder.imageLoader;
        this.aspectX = builder.aspectX;
        this.aspectY = builder.aspectY;
        this.outputX = builder.outputX;
        this.outputY = builder.outputY;
    }

    /**
     * 是否需要裁剪
     *
     * @return the boolean
     */
    public boolean isNeedCrop() {
        return needCrop;
    }

    /**
     * 是否使用系统裁剪功能
     *
     * @return boolean
     */
    public boolean isUseSystemCrop() {
        return useSystemCrop;
    }

    /**
     * 是否多选
     *
     * @return the boolean
     */
    public boolean isMultiSelect() {
        return multiSelect;
    }

    /**
     * 设置是否多选
     *
     * @param multiSelect the multi select
     */
    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    /**
     * 是否记住上次选中记录
     *
     * @return the boolean
     */
    public boolean isRememberSelected() {
        return rememberSelected;
    }

    /**
     * 最大数量
     *
     * @return the max num
     */
    public int getMaxNum() {
        return maxNum;
    }

    /**
     * 是否需要拍照
     *
     * @return the boolean
     */
    public boolean isNeedCamera() {
        return needCamera;
    }

    /**
     * 状态栏颜色
     *
     * @return the status bar color
     */
    public int getStatusBarColor() {
        return statusBarColor;
    }

    /**
     * 返回按钮的图片id
     *
     * @return the back image resource id
     */
    public int getBackImageResourceId() {
        return backImageResourceId;
    }

    /**
     * 标题的内容
     *
     * @return the title text
     */
    public String getTitleText() {
        return titleText;
    }

    /**
     * 标题文本的颜色
     *
     * @return the title text color
     */
    public int getTitleTextColor() {
        return titleTextColor;
    }

    /**
     * 标题背景颜色
     *
     * @return the title background color
     */
    public int getTitleBackgroundColor() {
        return titleBackgroundColor;
    }

    /**
     * 确认按钮的文本
     *
     * @return the confirm text
     */
    public String getConfirmText() {
        return confirmText;
    }

    /**
     * 确认按钮文本的颜色
     *
     * @return the confirm text color
     */
    public int getConfirmTextColor() {
        return confirmTextColor;
    }

    /**
     * 确认按钮的背景颜色
     *
     * @return the confirm background color
     */
    public int getConfirmBackgroundColor() {
        return confirmBackgroundColor;
    }

    /**
     * 所有图片的文字
     *
     * @return the all images text
     */
    public String getAllImagesText() {
        return allImagesText;
    }

    /**
     * 文件夹路径
     *
     * @return the folder path
     */
    public String getFolderPath() {
        return folderPath;
    }

    /**
     * 图像加载程器
     *
     * @return the image loader
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    /**
     * 裁剪横向比值
     *
     * @return the aspect x
     */
    public int getAspectX() {
        return aspectX;
    }

    /**
     * 裁剪纵向比值
     *
     * @return the aspect y
     */
    public int getAspectY() {
        return aspectY;
    }

    /**
     * 图片输出的横向大小
     *
     * @return the output x
     */
    public int getOutputX() {
        return outputX;
    }

    /**
     * 图片输出的纵向大小
     *
     * @return the output y
     */
    public int getOutputY() {
        return outputY;
    }

    /**
     * The type Builder.
     */
    public static class Builder implements Serializable {

        private int statusBarColor = -1;
        private boolean needCrop = false;
        private boolean useSystemCrop = false;
        private boolean multiSelect = true;
        private boolean rememberSelected = true;
        private int maxNum = 9;
        private boolean needCamera = true;
        private int backImageResourceId = -1;
        private String titleText;
        private int titleTextColor;
        private int titleBackgroundColor;
        private String confirmText;
        private int confirmTextColor;
        private int confirmBackgroundColor;
        private String allImagesText;
        private String folderPath;
        private ImageLoader imageLoader;

        private int aspectX = 1;
        private int aspectY = 1;
        private int outputX = 400;
        private int outputY = 400;

        /**
         * Instantiates a new Builder.
         *
         * @param context     the context
         * @param imageLoader the image loader
         */
        public Builder(Context context, ImageLoader imageLoader) {
            this.imageLoader = imageLoader;

            if (FileUtils.isSdCardAvailable())
                folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Camera";
            else
                folderPath = Environment.getRootDirectory().getAbsolutePath() + "/Camera";

            titleText = context.getResources().getString(R.string.image_select_text_image);
            titleBackgroundColor = Color.parseColor("#3F51B5");
            titleTextColor = Color.WHITE;

            confirmText = context.getResources().getString(R.string.image_select_text_confirm);
            confirmBackgroundColor = Color.TRANSPARENT;
            confirmTextColor = Color.WHITE;

            allImagesText = context.getResources().getString(R.string.image_select_text_all_images);
        }

        /**
         * 设置是否需要裁剪
         *
         * @param needCrop the need crop
         * @return the builder
         */
        public Builder needCrop(boolean needCrop) {
            this.needCrop = needCrop;
            return this;
        }

        /**
         * 设置是否使用系统本身的裁剪功能，否着使用该框架自带的裁剪功能
         *
         * @param useSystemCrop the use system crop
         * @return builder
         */
        public Builder useSystemCrop(boolean useSystemCrop) {
            this.useSystemCrop = useSystemCrop;
            return this;
        }

        /**
         * 设置多选模式
         *
         * @return the builder
         */
        public Builder multiSelect() {
            this.multiSelect = true;
            return this;
        }

        /**
         * 设置单选模式
         *
         * @return the builder
         */
        public Builder singleSelect() {
            this.multiSelect = false;
            return this;
        }

        /**
         * 设置记住上次选中记录
         *
         * @param rememberSelected the remember selected
         * @return the builder
         */
        public Builder rememberSelected(boolean rememberSelected) {
            this.rememberSelected = rememberSelected;
            return this;
        }

        /**
         * 设置最大数量
         *
         * @param maxNum the max num
         * @return the builder
         */
        public Builder maxNum(int maxNum) {
            this.maxNum = maxNum;
            return this;
        }

        /**
         * 设置是否需要拍照
         *
         * @param needCamera the need camera
         * @return the builder
         */
        public Builder needCamera(boolean needCamera) {
            this.needCamera = needCamera;
            return this;
        }

        /**
         * 设置状态栏颜色
         *
         * @param statusBarColor the status bar color
         * @return the builder
         */
        public Builder statusBarColor(int statusBarColor) {
            this.statusBarColor = statusBarColor;
            return this;
        }

        /**
         * 设置返回按钮的图片ID
         *
         * @param backImageResourceId the back image resource id
         * @return the builder
         */
        public Builder backImageResourceId(int backImageResourceId) {
            this.backImageResourceId = backImageResourceId;
            return this;
        }

        /**
         * 设置标题的文本
         *
         * @param titleText the title text
         * @return the builder
         */
        public Builder titleText(String titleText) {
            this.titleText = titleText;
            return this;
        }

        /**
         * 设置标题文本的颜色
         *
         * @param titleTextColor the title text color
         * @return the builder
         */
        public Builder titleTextColor(int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        /**
         * 设置标题背景的颜色
         *
         * @param titleBackgroundColor the title background color
         * @return the builder
         */
        public Builder titleBackgroundColor(int titleBackgroundColor) {
            this.titleBackgroundColor = titleBackgroundColor;
            return this;
        }

        /**
         * 设置确认按钮的文本
         *
         * @param confirmText the confirm text
         * @return the builder
         */
        public Builder confirmText(String confirmText) {
            this.confirmText = confirmText;
            return this;
        }

        /**
         * 设置确认按钮文本的颜色
         *
         * @param confirmTextColor the confirm text color
         * @return the builder
         */
        public Builder confirmTextColor(int confirmTextColor) {
            this.confirmTextColor = confirmTextColor;
            return this;
        }

        /**
         * 设置确认按钮背景的颜色
         *
         * @param confirmBackgroundColor the confirm background color
         * @return the builder
         */
        public Builder confirmBackgroundColor(int confirmBackgroundColor) {
            this.confirmBackgroundColor = confirmBackgroundColor;
            return this;
        }

        /**
         * 设置所有图片显示的文本
         *
         * @param allImagesText the all images text
         * @return the builder
         */
        public Builder allImagesText(String allImagesText) {
            this.allImagesText = allImagesText;
            return this;
        }

        /**
         * 设置输出的文件夹
         *
         * @param folderPath
         * @return
         */
        private Builder folderPath(String folderPath) {
            this.folderPath = folderPath;
            return this;
        }

        /**
         * 设置裁剪配置
         *
         * @param aspectX 横向比值
         * @param aspectY 纵向比值
         * @param outputX 横向输出的大小
         * @param outputY 纵向输出的大小
         * @return the builder
         */
        public Builder cropSize(int aspectX, int aspectY, int outputX, int outputY) {
            this.aspectX = aspectX;
            this.aspectY = aspectY;
            this.outputX = outputX;
            this.outputY = outputY;
            return this;
        }

        /**
         * 构建图像选择配置
         *
         * @return the image select config
         */
        public ImageSelectConfig build() {
            FileUtils.createDir(folderPath);
            return new ImageSelectConfig(this);
        }
    }
}
