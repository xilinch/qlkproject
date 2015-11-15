package com.xiaocoder.android.fw.general.tool;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.function.helper.XCExecutorHelper;
import com.xiaocoder.android.fw.general.http.IHttp.XCIResponseHandler;
import com.xiaocoder.android.fw.general.http.XCHttpSend;
import com.xiaocoder.android.fw.general.imageloader.XCIImageLoader;
import com.xiaocoder.android.fw.general.io.XCLog;
import com.xiaocoder.android.fw.general.io.XCSP;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by xiaocoder on 2015/11/12.
 * description: 开发工具集
 */
public class XC {
    /**
     * 可变线程池
     */
    protected static ExecutorService cacheThreadPool = XCExecutorHelper.getInstance().getCache();
    /**
     * 主线程中的handler
     */
    protected static Handler handler = new Handler();
    /**
     * http
     */
    protected static XCHttpSend httpSend = new XCHttpSend();

    /**
     * 以下的涉及到路径和文件名等配置的，在子类中初始化
     */

    /**
     * 固定线程池（http中用到了这个解析model）-- 数量
     */
    protected static ExecutorService fixThreadPool;
    /**
     * 调度线程池 -- 数量
     */
    protected static ScheduledExecutorService scheduledThreadPool;

    protected static XCSP sp;
    /**
     * 图片加载 -- 缓存路径
     */
    protected static XCIImageLoader imageLoader;

    public static ExecutorService getCacheThreadPool() {
        return cacheThreadPool;
    }

    public static void setCacheThreadPool(ExecutorService cacheThreadPool) {
        XC.cacheThreadPool = cacheThreadPool;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static void setHandler(Handler handler) {
        XC.handler = handler;
    }

    public static XCHttpSend getHttpSend() {
        return httpSend;
    }

    public static void setHttpSend(XCHttpSend httpSend) {
        XC.httpSend = httpSend;
    }

    public static ExecutorService getFixThreadPool() {
        return fixThreadPool;
    }

    public static void setFixThreadPool(ExecutorService fixThreadPool) {
        XC.fixThreadPool = fixThreadPool;
    }

    public static ScheduledExecutorService getScheduledThreadPool() {
        return scheduledThreadPool;
    }

    public static void setScheduledThreadPool(ScheduledExecutorService scheduledThreadPool) {
        XC.scheduledThreadPool = scheduledThreadPool;
    }


    public static XCSP getSp() {
        return sp;
    }

    public static void setSp(XCSP sp) {
        XC.sp = sp;
    }

    public static XCIImageLoader getImageLoader() {
        return imageLoader;
    }

    public static void setImageLoader(XCIImageLoader imageLoader) {
        XC.imageLoader = imageLoader;
    }

    public static void spPut(String key, boolean value) {
        sp.putBoolean(key, value);
    }

    public static void spPut(String key, int value) {
        sp.putInt(key, value);
    }

    public static void spPut(String key, long value) {
        sp.putLong(key, value);
    }

    public static void spPut(String key, float value) {
        sp.putFloat(key, value);
    }

    public static void spPut(String key, String value) {
        sp.putString(key, value);
    }

    public static String spGet(String key, String default_value) {
        return sp.getString(key, default_value);
    }

    public static int spGet(String key, int default_value) {
        return sp.getInt(key, default_value);
    }

    public static long spGet(String key, long default_value) {
        return sp.getLong(key, default_value);
    }

    public static boolean spGet(String key, boolean default_value) {
        return sp.getBoolean(key, default_value);
    }

    public static float spGet(String key, float default_value) {
        return sp.getFloat(key, default_value);
    }

    public static Map<String, ?> spGetAll() {
        return sp.getAll();
    }

    /**
     * 图片加载
     */
    public static void displayImage(String uri, ImageView imageView, Object... options) {
        imageLoader.display(uri, imageView, options);
    }

    public static void displayImage(String uri, ImageView imageView) {
        imageLoader.display(uri, imageView);
    }

    /**
     * http请求
     */
    public static void resetNetingStatus() {
        httpSend.resetNetingStatus();
    }

    public static void postAsyn(boolean needSecret, boolean isFrequentlyClick, boolean isShowDialog,
                                String urlString, Map<String, Object> map, XCIResponseHandler res) {
        httpSend.postAsyn(needSecret, isFrequentlyClick, isShowDialog, urlString, map, res);
    }

    public static void postAsyn(boolean isShowDialog, String urlString, Map<String, Object> map,
                                XCIResponseHandler res) {
        httpSend.postAsyn(true, true, isShowDialog, urlString, map, res);
    }

    public static void getAsyn(boolean needSecret, boolean isFrequentlyClick, boolean isShowDialog,
                               String urlString, Map<String, Object> map, XCIResponseHandler res) {
        httpSend.getAsyn(needSecret, isFrequentlyClick, isShowDialog, urlString, map, res);
    }

    public static void getAsyn(boolean isShowDialog, String urlString, Map<String, Object> map,
                               XCIResponseHandler res) {
        httpSend.getAsyn(true, true, isShowDialog, urlString, map, res);
    }

    public static void sendHttpRequest(XCIResponseHandler responseHandler) {
        httpSend.sendAsyn(responseHandler);
    }

}
