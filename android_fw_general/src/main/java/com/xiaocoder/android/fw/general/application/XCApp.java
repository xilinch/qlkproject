package com.xiaocoder.android.fw.general.application;

import android.app.Application;
import android.content.Context;

import com.xiaocoder.android.fw.general.util.UtilScreen;
import com.xiaocoder.android.fw.general.util.UtilSystem;

/**
 * 1 存储activity ， 回到首页activity， 弹出指定activity等
 * 2 线程池    handler  图片加载  log等
 */
public class XCApp extends Application {

    protected static Context base_applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        base_applicationContext = getApplicationContext();
    }

    public static Context getBase_applicationContext() {
        return base_applicationContext;
    }

    /**
     * 打印设备的基本信息
     */
    public String simpleDeviceInfo() {
        return (UtilSystem.getDeviceId(getApplicationContext()) + "--deviceId , "
                + UtilSystem.getVersionCode(getApplicationContext())
                + "--versionCode , " + UtilSystem.getVersionName(getApplicationContext()) + "--versionName , "
                + UtilScreen.getScreenHeightPx(getApplicationContext()) + "--screenHeightPx , "
                + UtilScreen.getScreenWidthPx(getApplicationContext()) + "--screenWidthPx , "
                + UtilScreen.getDensity(getApplicationContext()) + "--density , "
                + UtilScreen.getScreenHeightDP(getApplicationContext()) + "--screenHeightDP , "
                + UtilScreen.getScreenWidthPx(getApplicationContext()) + "--screenWidthDP");
    }


}
