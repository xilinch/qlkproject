package com.xiaocoder.android.fw.general.tool;

import android.os.Handler;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.function.helper.XCExecutorHelper;
import com.xiaocoder.android.fw.general.http.IHttp.XCIResponseHandler;
import com.xiaocoder.android.fw.general.http.XCHttpSend;
import com.xiaocoder.android.fw.general.imageloader.XCIImageLoader;

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

    public static XCIImageLoader getImageLoader() {
        return imageLoader;
    }

    public static void setImageLoader(XCIImageLoader imageLoader) {
        XC.imageLoader = imageLoader;
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
