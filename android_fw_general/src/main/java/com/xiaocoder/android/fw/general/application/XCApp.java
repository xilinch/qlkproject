package com.xiaocoder.android.fw.general.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.xiaocoder.android.fw.general.util.UtilScreen;
import com.xiaocoder.android.fw.general.util.UtilSystem;

public class XCApp extends Application {

    protected static Context baseApplicationContext;
    protected static Handler baseHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplicationContext = getApplicationContext();
        baseHandler = new Handler();
    }

    public static Context getBaseApplicationContext() {
        return baseApplicationContext;
    }

    public static Handler getBaseHandler() {
        return baseHandler;
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
