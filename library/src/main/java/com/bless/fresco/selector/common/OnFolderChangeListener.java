package com.bless.fresco.selector.common;

import com.bless.fresco.selector.bean.Folder;

/**
 * <pre>
 *
 * 作者:      ASLai(gdcpljh@126.com).
 * 日期:      17-7-13
 * 版本:      V1.0
 * 描述:      文件夹改变监听器
 *
 * </pre>
 */

public interface OnFolderChangeListener {

    void onChange(int position, Folder folder);
}
