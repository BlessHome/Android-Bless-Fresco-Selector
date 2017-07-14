package com.bless.fresco.selector.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bless.fresco.selector.ImageSelectConfig;
import com.bless.fresco.selector.R;
import com.bless.fresco.selector.bean.Image;
import com.bless.fresco.selector.common.ImageSelectConstant;
import com.bless.fresco.selector.common.OnItemClickListener;
import com.bless.fresco.widget.FrescoImageView;

import java.util.List;

/**
 * <pre>
 *
 * 作者:      ASLai(gdcpljh@126.com).
 * 日期:      17-7-13
 * 版本:      V1.0
 * 描述:      预览适配器
 *
 * </pre>
 */

public class PreviewAdapter extends PagerAdapter {

    private Activity activity;
    private List<Image> images;
    private ImageSelectConfig config;
    private OnItemClickListener listener;

    public PreviewAdapter(Activity activity, List<Image> images, ImageSelectConfig config) {
        this.activity = activity;
        this.images = images;
        this.config = config;
    }

    @Override
    public int getCount() {
        if (config.isNeedCamera())
            return images.size() - 1;
        else
            return images.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        View root = View.inflate(activity, R.layout.image_select_item_pager, null);
        final FrescoImageView imageSelectImageFrescoImageView = (FrescoImageView) root.findViewById(R.id.image_select_image_frescoImageView);
        final ImageView imageSelectCheckImageView = (ImageView) root.findViewById(R.id.image_select_check_imageView);

        if (config.isMultiSelect()) {

            imageSelectCheckImageView.setVisibility(View.VISIBLE);
            final Image image = images.get(config.isNeedCamera() ? position + 1 : position);
            if (ImageSelectConstant.imageList.contains(image.path)) {
                imageSelectCheckImageView.setImageResource(R.drawable.image_select_ic_checked);
            } else {
                imageSelectCheckImageView.setImageResource(R.drawable.image_select_ic_uncheck);
            }

            imageSelectCheckImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int ret = listener.onCheckedClick(position, image);
                        if (ret == 1) { // 局部刷新
                            if (ImageSelectConstant.imageList.contains(image.path)) {
                                imageSelectCheckImageView.setImageResource(R.drawable.image_select_ic_checked);
                            } else {
                                imageSelectCheckImageView.setImageResource(R.drawable.image_select_ic_uncheck);
                            }
                        }
                    }
                }
            });

            imageSelectImageFrescoImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onImageClick(position, images.get(position));
                    }
                }
            });
        } else {
            imageSelectCheckImageView.setVisibility(View.GONE);
        }

        container.addView(root, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        displayImage(imageSelectImageFrescoImageView, images.get(config.isNeedCamera() ? position + 1 : position).path);

        return root;
    }

    private void displayImage(FrescoImageView photoView, String path) {
        config.getImageLoader().displayImage(activity, path, photoView);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
