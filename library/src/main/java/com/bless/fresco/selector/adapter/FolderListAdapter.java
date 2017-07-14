package com.bless.fresco.selector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bless.fresco.selector.ImageSelectConfig;
import com.bless.fresco.selector.R;
import com.bless.fresco.selector.bean.Folder;
import com.bless.fresco.selector.common.OnFolderChangeListener;
import com.bless.fresco.widget.FrescoImageView;

import java.util.List;

/**
 * <pre>
 *
 * 作者:      ASLai(gdcpljh@126.com).
 * 日期:      17-7-14
 * 版本:      V1.0
 * 描述:      文件夹列表适配器
 *
 * </pre>
 */
public class FolderListAdapter extends BaseAdapter {

    private Context context;
    private List<Folder> list;
    private ImageSelectConfig config;

    private int selected = 0;
    private OnFolderChangeListener listener;

    /**
     * Instantiates a new Folder list adapter.
     *
     * @param context the context
     * @param list    the list
     * @param config  the config
     */
    public FolderListAdapter(Context context, List<Folder> list, ImageSelectConfig config) {
        this.context = context;
        this.list = list;
        this.config = config;
    }

    /**
     * On create view holder view holder.
     *
     * @param parent   the parent
     * @param viewType the view type
     * @return the view holder
     */
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int itemLayoutId = R.layout.image_select_item_folder;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * On bind view holder.
     *
     * @param holder   the holder
     * @param position the position
     */
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Folder folder = getItem(position);

        TextView imageSelectFolderNameTextView = holder.getView(R.id.image_select_folder_name_textView);
        TextView imageSelectImageNumTextView = holder.getView(R.id.image_select_image_num_textView);
        FrescoImageView imageSelectFolderFrescoImageView = holder.getView(R.id.image_select_folder_frescoImageView);
        ImageView imageSelectIndicatorImageView = holder.getView(R.id.image_select_indicator_imageView);

        if (position == 0) {

            imageSelectFolderNameTextView.setText("所有图片");
            imageSelectImageNumTextView.setText("共" + getTotalImageSize() + "张");

            if (getCount() > 0) {
                config.getImageLoader().displayImage(context, folder.cover.path, imageSelectFolderFrescoImageView);
            }
        } else {
            imageSelectFolderNameTextView.setText(folder.name);
            imageSelectImageNumTextView.setText("共" + folder.images.size() + "张");
            if (getCount() > 0) {
                config.getImageLoader().displayImage(context, folder.cover.path, imageSelectFolderFrescoImageView);
            }
        }

        if (selected == position) {
            imageSelectIndicatorImageView.setVisibility(View.VISIBLE);
        } else {
            imageSelectIndicatorImageView.setVisibility(View.GONE);
        }

        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectIndex(position);
            }
        });
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Folder getItem(int position) {
        return getCount() > 0 ? list.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.image_select_item_folder, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(convertView);
        onBindViewHolder(viewHolder, position);
        return viewHolder.getItemView();
    }

    /**
     * Sets data.
     *
     * @param folders the folders
     */
    public void setData(List<Folder> folders) {
        list.clear();
        if (folders != null && folders.size() > 0) {
            list.addAll(folders);
        }
        notifyDataSetChanged();
    }

    private int getTotalImageSize() {
        int result = 0;
        if (list != null && list.size() > 0) {
            for (Folder folder : list) {
                result += folder.images.size();
            }
        }
        return result;
    }

    /**
     * Gets select index.
     *
     * @return the select index
     */
    public int getSelectIndex() {
        return selected;
    }

    /**
     * Sets select index.
     *
     * @param position the position
     */
    public void setSelectIndex(int position) {
        if (selected == position)
            return;
        if (listener != null)
            listener.onChange(position, getItem(position));
        selected = position;
        notifyDataSetChanged();
    }

    /**
     * Sets on floder change listener.
     *
     * @param listener the listener
     */
    public void setOnFloderChangeListener(OnFolderChangeListener listener) {
        this.listener = listener;
    }
}
