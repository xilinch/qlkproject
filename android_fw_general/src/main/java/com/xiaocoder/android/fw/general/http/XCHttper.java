package com.xiaocoder.android.fw.general.http;

import com.xiaocoder.android.fw.general.http.IHttp.XCIResponseHandler;

import java.util.Map;

/**
 * Created by xiaocoder on 2015/11/12.
 */
public class XCHttper {

    protected static XCHttpSend httpSend = new XCHttpSend();

    public static XCHttpSend getHttpSend() {
        return httpSend;
    }

    public static void setHttpSend(XCHttpSend httpSend) {
        XCHttper.httpSend = httpSend;
    }

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
