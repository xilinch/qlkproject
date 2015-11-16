package com.xiaocoder.android.fw.general.tool;

import android.widget.ImageView;

import com.xiaocoder.android.fw.general.http.IHttp.XCIResponseHandler;
import com.xiaocoder.android.fw.general.http.XCHttpSend;
import com.xiaocoder.android.fw.general.imageloader.XCIImageLoader;

import java.util.Map;

/**
 * Created by xiaocoder on 2015/11/12.
 */
public class XC {

    /**
     * http
     */
    protected static XCHttpSend httpSend = new XCHttpSend();

    /**
     * 图片加载 -- 缓存路径
     */
    protected static XCIImageLoader imageLoader;

    public static XCHttpSend getHttpSend() {
        return httpSend;
    }

    public static void setHttpSend(XCHttpSend httpSend) {
        XC.httpSend = httpSend;
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
