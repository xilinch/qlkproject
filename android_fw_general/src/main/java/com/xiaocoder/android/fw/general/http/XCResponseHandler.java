package com.xiaocoder.android.fw.general.http;

import android.app.Activity;
import android.app.Dialog;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCBaseActivity;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.function.thread.XCExecutor;
import com.xiaocoder.android.fw.general.http.IHttp.XCIHttpNotify;
import com.xiaocoder.android.fw.general.http.IHttp.XCIHttpResult;
import com.xiaocoder.android.fw.general.http.IHttp.XCIResponseHandler;
import com.xiaocoder.android.fw.general.io.XCLog;

import org.apache.http.Header;

/**
 * @author xiaocoder
 * @date 2014-12-30 下午5:04:48
 * 该类是以asyn-http-android库的AsyncHttpResponseHandler为基础
 * <p/>
 * onFinish()不能被重写（不理会），这里设置为了final. 如果需要重写，则重写finish（）方法
 * onSuccess()不能被重写，这里设置为了final，重写success（）方法
 * onFailure()不能被重写，这里设置为了final，重写failure（）方法
 * <p/>
 * finish会在success或failure之后运行
 * <p/>
 * model数据解析的代码 -->在子线程中执行的
 * <p/>
 * 如果有特殊的解析需求，重写parse() 或 parseWay方法
 */
public abstract class XCResponseHandler<T> extends AsyncHttpResponseHandler implements XCIResponseHandler<T> {
    /**
     * 是json还是xml或图片等,默认是json
     */
    public int content_type;
    /**
     * 如果result_boolean为真就开始处理业务, 如果为false则不处理
     */
    public boolean result_boolean;
    /**
     * 结果集
     */
    public T result_bean;
    /**
     * 访问网络失败时,是否显示失败页面的背景
     */
    public boolean show_background_when_net_fail;

    public Activity activity;

    /**
     * 加载中的dialog
     */
    public Dialog httpDialog;
    /**
     * 返回的结果数据
     */
    public Class<T> result_bean_class;
    /**
     * 回调的接口
     */
    public XCIHttpResult result_http;

    public XCIHttpNotify notify;

    /**
     * 请求的完整参数
     */
    public XCHttpModel httpModel;

    @Override
    public Dialog getHttpDialog() {
        return httpDialog;
    }

    @Override
    public Activity obtainActivity() {
        return activity;
    }

    @Override
    public void setXCHttpModel(XCHttpModel model) {
        this.httpModel = model;
    }

    @Override
    public XCHttpModel getXCHttpModel() {
        return httpModel;
    }

    /**
     * @param result_http                   如果为null，不会异常，但是不会调用 有网无网的页面转换与重刷新 的功能
     * @param notify                        如果为null，不会异常，但是不会调用 请求的并行和串行的监听方法
     * @param activity                      如果为null，不会异常，但是不会showdialog，以及与context有关的功能都无法使用
     * @param content_type                  默认为JSON，目前也只有JSON的判断，xml 类型待以后加，photo可以直接用bytes
     * @param show_background_when_net_fail true 为展示背景 , false弹出吐司
     * @param result_bean_class             model的字节码文件
     */
    public XCResponseHandler(XCIHttpResult result_http,
                             XCIHttpNotify notify,
                             Activity activity,
                             int content_type,
                             boolean show_background_when_net_fail,
                             Class<T> result_bean_class
    ) {
        super();
        this.result_boolean = false;
        this.result_http = result_http;
        this.notify = notify;
        this.activity = activity;
        this.content_type = content_type;
        this.show_background_when_net_fail = show_background_when_net_fail;
        this.result_bean_class = result_bean_class;

    }

    public XCResponseHandler(XCIHttpResult result_http, XCIHttpNotify notify, Activity activity, Class<T> result_bean_class) {
        this(result_http, notify, activity, JSON, true, result_bean_class);
    }

    public XCResponseHandler(XCIHttpResult result_http, Activity activity, Class<T> result_bean_class) {
        this(result_http, null, activity, JSON, true, result_bean_class);
    }


    public XCResponseHandler(Activity activity, Class<T> result_bean_class) {
        this(null, null, activity, JSON, true, result_bean_class);
    }

    /**
     * 防止该方法被重写，主线程
     */
    @Override
    public final void onFinish() {
        super.onFinish();
        XCLog.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "----onFinish()-ignore-fake");
    }

    /**
     * 主线程
     */
    @Override
    public void finish() {
        XCHttper.resetNetingStatus();
        XCLog.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "----finish()");
        closeHttpDialog();
    }

    /**
     * 主线程，该方法不要去重写
     */
    @Override
    public final void onFailure(int code, Header[] headers, byte[] arg2, Throwable e) {

        if (isXCActivityDestroy(activity)) {
            return;
        }

        XCLog.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "-----onFailure()");
        XCLog.i(XCConfig.TAG_HTTP, "onFailure----->status code " + code + "----e.toString()" + e.toString());

        e.printStackTrace();

        if (XCLog.is_output && headers != null) {
            for (Header header : headers) {
                XCLog.i(XCConfig.TAG_HTTP, "headers----->" + header.toString());
            }
        }

        try {
            if (httpBack(code, headers, arg2, e)) {
                failure(code, headers, arg2, e);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            XCLog.e(activity, httpModel.getUrlString() + "---httpBack（） failure（） 异常了", e1);
        } finally {
            try {
                httpEnd(code, headers, arg2, e);
            } catch (Exception e1) {
                e1.printStackTrace();
                XCLog.e(activity, httpModel.getUrlString() + "failure---httpEnd（） 异常了", e1);
            }
        }
    }

    /**
     * 主线程中
     */
    @Override
    public void failure(int code, Header[] headers, byte[] bytes, Throwable e) {

        XCLog.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "-----failure()");

        if (result_http != null) {
            // 回调访问网络失败时的界面
            result_http.onNetFail(this, show_background_when_net_fail);
        } else {
            // 显示吐司
            XCLog.shortToast("网络有误");
        }
    }


    /**
     * 这里有数据解析的代码 -->在子线程中执行，该方法不要去重写
     */
    @Override
    public final void onSuccess(final int code, final Header[] headers, final byte[] bytes) {

        if (isXCActivityDestroy(activity)) {
            return;
        }

        XCLog.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "-----onSuccess()");
        XCLog.i(XCConfig.TAG_HTTP, "onSuccess----->status code " + code);

        if (XCLog.is_output && headers != null) {
            for (Header header : headers) {
                XCLog.i(XCConfig.TAG_HTTP, "headers----->" + header.toString());
            }
        }

        // 子线程
        XCExecutor.getFix().execute(new Runnable() {
            @Override
            public void run() {

                parse(bytes);

                XCApp.getBaseHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        // 增加activity是否销毁的判断
                        if (isXCActivityDestroy(activity)) {
                            return;
                        }

                        try {
                            if (httpBack(code, headers, bytes, null)) {
                                success(code, headers, bytes);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            XCLog.e(activity, httpModel.getUrlString() + "---httpBack（） success（） 异常了", e);
                        } finally {
                            try {
                                httpEnd(code, headers, bytes, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                                XCLog.e(activity, httpModel.getUrlString() + "success---httpEnd（） 异常了", e);
                            }
                        }
                    }
                });
            }
        });
    }

    private boolean httpBack(int code, Header[] headers, byte[] bytes, Throwable e) {
        XCLog.i(XCConfig.TAG_HTTP_HANDLER, this + "-----httpBack()--" + notify);

        if (notify != null) {
            return notify.httpBackNotify(this, result_boolean, code, headers, bytes, e);
        } else {
            // 继续调用后面的代码
            return true;
        }
    }

    private void httpEnd(int code, Header[] headers, byte[] bytes, Throwable e) {
        finish();

        XCLog.i(XCConfig.TAG_HTTP_HANDLER, this + "-----httpEnd()--" + notify);

        if (notify != null) {
            notify.httpEndNotify(this, result_boolean, code, headers, bytes, e);
        }
    }

    /**
     * 主线程
     */
    @Override
    public void success(int code, Header[] headers, byte[] arg2) {
        XCLog.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "-----success()");

        if (result_http != null) {
            result_http.onNetSuccess();
        }
    }

    /**
     * 主线程
     */
    @Override
    public boolean isXCActivityDestroy(Activity activity) {

        XCLog.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "-----isXCActivityDestroy()---" + activity);

        if (activity == null) {
            // 如果是与页面无关的网络请求，可以为null
            XCLog.e(this.toString() + "---传入的activity为null");
            return false;
        }

        if (activity instanceof XCBaseActivity) {
            if (((XCBaseActivity) activity).isActivityDestroied()) {
                XCLog.e(this.toString() + "---activity被销毁了");
                return true;
            }
        } else {
            // 如果不是xcbaseactivity，依旧可以使用，只是不会再判断该activity是否已经销毁
            XCLog.e(this.toString() + "---activity不是XCBaseActivity的实例");
        }
        return false;
    }

    /**
     * 在子线程运行的，所以不要有ui或toast等操作
     */
    @Override
    public void parse(byte[] response_bytes) {
        try {
            XCLog.i(XCConfig.TAG_HTTP_HANDLER, this.toString() + "-----parse()");
            if (content_type == JSON) {
                String response_str = new String(response_bytes, XCConfig.ENCODING_UTF8);
                // 把json串打印到控制台，该方法受log的isoutput开关控制
                XCLog.i(XCConfig.TAG_HTTP, response_str);
                // 有的时候json太长，控制台无法全部打印出来，就打印到本地的文件中，该方法受log的isoutput开关控制
                XCLog.tempPrint(response_str);

                result_bean = parseWay(response_str, response_bytes);

                if (result_bean == null) {
                    result_boolean = false;
                    XCLog.e(this.toString() + "---parse() , 解析数据失败");
                    return;
                }

                result_boolean = yourCompanyResultRule();
            }
        } catch (Exception e) {
            e.printStackTrace();
            result_boolean = false;
            XCLog.e("解析数据异常---" + this.toString() + "---" + e.toString());
        }
    }

    @Override
    public void onRetry(int retryNo) {
        super.onRetry(retryNo);
        XCLog.i(XCConfig.TAG_HTTP_HANDLER, retryNo + "----网络连接重试---" + httpModel);
    }

}

