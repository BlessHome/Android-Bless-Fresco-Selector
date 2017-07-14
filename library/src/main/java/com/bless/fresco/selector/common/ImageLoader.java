package com.bless.fresco.selector.common;

import android.content.Context;

import com.bless.fresco.widget.FrescoImageView;

import java.io.Serializable;

/**
 * <pre>
 *
 * 作者:      ASLai(gdcpljh@126.com).
 * 日期:      17-7-13
 * 版本:      V1.0
 * 描述:      用于加载图片的回调
 *
 * </pre>
 */

public interface ImageLoader extends Serializable {
    void displayImage(Context context, String path, FrescoImageView imageView);
}
