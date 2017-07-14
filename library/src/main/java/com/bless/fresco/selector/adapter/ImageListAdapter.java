package com.bless.fresco.selector.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
 * 日期:      17-7-14
 * 版本:      V1.0
 * 描述:      图像列表适配器
 *
 * </pre>
 */

public class ImageListAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context context;
    private List<Image> list;

    private boolean showCamera;
    private boolean multiSelect;

    private ImageSelectConfig config;
    private OnItemClickListener listener;

    public ImageListAdapter(Context context, List<Image> list, ImageSelectConfig config) {
        this.list = list;
        this.context = context;
        this.config = config;
        this.showCamera = config.isNeedCamera();
        this.multiSelect = config.isMultiSelect();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int itemLayoutId;
        if (viewType == 1) {
            itemLayoutId = R.layout.image_select_item_take_photo;
        } else {
            itemLayoutId = R.layout.image_select_item_image;
        }

        View itemView = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        final Image item = getItem(position);

        if (position == 0 && showCamera) {
            ImageView imageSelectTakePhotoImageView = viewHolder.getView(R.id.image_select_takePhoto_imageView);
            imageSelectTakePhotoImageView.setImageResource(R.drawable.image_select_ic_take_photo);
            imageSelectTakePhotoImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onImageClick(position, item);
                }
            });
            return;
        }

        if (multiSelect) {
            final ImageView imageSelectCheckImageView = viewHolder.getView(R.id.image_select_check_imageView);
            imageSelectCheckImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int ret = listener.onCheckedClick(position, item);
                        if (ret == 1) { // 局部刷新

                            if (ImageSelectConstant.imageList.contains(item.path)) {
                                imageSelectCheckImageView.setImageResource(R.drawable.image_select_ic_checked);
                            } else {
                                imageSelectCheckImageView.setImageResource(R.drawable.image_select_ic_uncheck);
                            }
                        }
                    }
                }
            });
        }

        viewHolder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onImageClick(position, item);
            }
        });

        final FrescoImageView imageSelectImageFrescoImageView = viewHolder.getView(R.id.image_select_image_frescoImageView);
        config.getImageLoader().displayImage(context, item.path, imageSelectImageFrescoImageView);

        ImageView photoChecked = viewHolder.getView(R.id.image_select_check_imageView);
        if (multiSelect) {
            photoChecked.setVisibility(View.VISIBLE);
            if (ImageSelectConstant.imageList.contains(item.path)) {
                photoChecked.setImageResource(R.drawable.image_select_ic_checked);
            } else {
                photoChecked.setImageResource(R.drawable.image_select_ic_uncheck);
            }
        } else {
            photoChecked.setVisibility(View.GONE);
        }
    }

    public Image getItem(int position) {
        return (list != null && list.size() > 0) ? list.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && showCamera) {
            return 1;
        }
        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
