package com.bless.fresco.selector.bean;

import java.util.List;

/**
 * <pre>
 *
 * 作者:      ASLai(gdcpljh@126.com).
 * 日期:      17-7-13
 * 版本:      V1.0
 * 描述:      文件夹
 *
 * </pre>
 */

public class Folder {

    public String name;
    public String path;
    public Image cover;
    public List<Image> images;

    public boolean isAll = false;

    public Folder(){

    }

    public Folder(boolean isAll){
        this.isAll = isAll;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Folder other = (Folder) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
