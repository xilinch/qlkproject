package com.xiaocoder.buffer;

import android.content.Context;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.exception.XLCrashHandler;
import com.xiaocoder.android.fw.general.imageloader.XCImageLoaderHelper;
import com.xiaocoder.android.fw.general.io.XCLog;
import com.xiaocoder.android.fw.general.io.XCSP;
import com.xiaocoder.android.fw.general.util.UtilScreen;
import com.xiaocoder.android.fw.general.util.UtilSystem;

/**
 * Created by xiaocoder on 2015/7/14.
 */
public class QlkApplication extends XCApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化一些路径的问题

        // 图片缓存路径
        base_imageloader = XCImageLoaderHelper
                .getInitedImageLoader
                        (XCImageLoaderHelper.getImageLoaderConfiguration
                                (getApplicationContext(), base_io.createDirInAndroid(QlkConfig.CACHE_DIRECTORY)));
        // sp保存路径
        base_sp = new XCSP(getApplicationContext(), QlkConfig.SP_SETTING, Context.MODE_APPEND);// Context.MODE_MULTI_PROCESS

        // 图片视频等保存的路径
        base_io.createDirInAndroid(QlkConfig.CHAT_MOIVE_FILE);
        base_io.createDirInAndroid(QlkConfig.CHAT_VIDEO_FILE);
        base_io.createDirInAndroid(QlkConfig.CHAT_PHOTO_FILE);
        base_io.createDirInAndroid(QlkConfig.CRASH_FILE);


        // log , 可以打印日志 与 toast
        base_log = new XCLog(getApplicationContext(),
                QlkConfig.IS_DTOAST, QlkConfig.IS_OUTPUT, QlkConfig.IS_PRINTLOG,
                QlkConfig.APP_ROOT, QlkConfig.LOG_FILE, QlkConfig.TEMP_PRINT_FILE, QlkConfig.ENCODING_UTF8);

        printi(UtilSystem.getDeviceId(getApplicationContext()) + "--deviceId , " + UtilSystem.getVersionCode(getApplicationContext())
                + "--versionCode , " + UtilSystem.getVersionName(getApplicationContext()) + "--versionName , "
                + UtilScreen.getScreenHeightPx(getApplicationContext())
                + "--screenHeightPx , " + UtilScreen.getScreenWidthPx(getApplicationContext()) + "--screenWidthPx , " + UtilScreen.getDensity(getApplicationContext()) + "--density , " + UtilScreen.getScreenHeightDP(getApplicationContext()) + "--screenHeightDP , " + UtilScreen.getScreenWidthPx(getApplicationContext()) + "--screenWidthDP");

        // 异常日志捕获的存储路径
        XLCrashHandler crashHandler = XLCrashHandler.getInstance();
        crashHandler.init(getApplicationContext(), QlkConfig.CRASH_FILE, QlkConfig.IS_SHOW_EXCEPTION_ACTIVITY);

    }
}