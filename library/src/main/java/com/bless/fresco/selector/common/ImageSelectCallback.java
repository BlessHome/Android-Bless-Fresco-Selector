package com.bless.fresco.selector.common;

import java.io.File;
import java.io.Serializable;

/**
 * <pre>
 *
 * 作者:      ASLai(gdcpljh@126.com).
 * 日期:      17-7-13
 * 版本:      V1.0
 * 描述:      图像选择回调
 *
 * </pre>
 */
public interface ImageSelectCallback extends Serializable {

    /**
     * On single image selected.
     *
     * @param path the path
     */
    void onSingleImageSelected(String path);

    /**
     * On image selected.
     *
     * @param path the path
     */
    void onImageSelected(String path);

    /**
     * On image unselected.
     *
     * @param path the path
     */
    void onImageUnselected(String path);

    /**
     * On camera shot.
     *
     * @param imageFile the image file
     */
    void onCameraShot(File imageFile);

    /**
     * On preview changed.
     *
     * @param select  the select
     * @param sum     the sum
     * @param visible the visible
     */
    void onPreviewChanged(int select, int sum, boolean visible);
}
