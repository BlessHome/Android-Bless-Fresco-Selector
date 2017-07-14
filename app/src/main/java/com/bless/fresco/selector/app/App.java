package com.bless.fresco.selector.app;

import android.app.Application;

import com.bless.fresco.FrescoInitializer;

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

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FrescoInitializer.getInstance().init(this);
    }
}
