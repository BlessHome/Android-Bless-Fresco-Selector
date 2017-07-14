package com.bless.fresco.selector.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * <pre>
 *
 * 作者:      ASLai(gdcpljh@126.com).
 * 日期:      17-7-14
 * 版本:      V1.0
 * 描述:      description
 *
 * </pre>
 */

class ViewHolder  extends RecyclerView.ViewHolder {

    private View itemView;

    public ViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public View getItemView() {
        return itemView;
    }

    public <T extends View> T getView(int viewId) {
        return (T) itemView.findViewById(viewId);
    }
}

