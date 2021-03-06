package com.xiaocoder.middle.parse;

import android.app.Activity;

import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.http.IHttp.XCIHttpNotify;
import com.xiaocoder.android.fw.general.http.IHttp.XCIHttpResult;
import com.xiaocoder.android.fw.general.io.XCLog;
import com.xiaocoder.android.fw.general.json.XCJsonParse;

/**
 * 通用的jsonbean解析
 */
public class MResponseHandlerBean<T extends MBean> extends MResponseHandler<T> {

    public MResponseHandlerBean(XCIHttpResult result_http, XCIHttpNotify notify, Activity activity, int content_type, boolean show_background_when_net_fail, Class<T> result_bean_class) {
        super(result_http, notify, activity, content_type, show_background_when_net_fail, result_bean_class);
    }

    public MResponseHandlerBean(XCIHttpResult result_http, XCIHttpNotify notify, Activity activity, Class<T> result_bean_class) {
        super(result_http, notify, activity, result_bean_class);
    }

    public MResponseHandlerBean(XCIHttpResult result_http,Activity activity, Class<T> result_bean_class) {
        super(result_http,activity, result_bean_class);
    }

    public MResponseHandlerBean(Activity activity, Class<T> result_bean_class) {
        super(activity, result_bean_class);
    }

    @Override
    public T parseWay(String responseStr , byte[] response_bytes) {

        XCLog.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "-----parseWay()");

        // 打印bean到控制台， 然后复制，格式化，即自动生成字段常量，该方法受log的isoutput开关控制
        XCJsonParse.json2Bean(responseStr);

        return XCJsonParse.getJsonParseData(responseStr, result_bean_class);
    }


}
