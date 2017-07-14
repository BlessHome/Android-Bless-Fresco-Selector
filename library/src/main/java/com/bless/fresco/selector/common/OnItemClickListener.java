package com.bless.fresco.selector.common;

import com.bless.fresco.selector.bean.Image;

/**
 * <pre>
 *
 * 作者:      ASLai(gdcpljh@126.com).
 * 日期:      17-7-13
 * 版本:      V1.0
 * 描述:      点击 Item 监听器
 *
 * </pre>
 */

public interface OnItemClickListener {

    int onCheckedClick(int position, Image image);

    void onImageClick(int position, Image image);
}
