package http;

import android.content.DialogInterface;
import android.view.KeyEvent;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.base.function.XCBaseMainActivity;
import com.xiaocoder.android.fw.general.dialog.XCBaseDialog;
import com.xiaocoder.android.fw.general.dialog.XCSystemHDialog;
import com.xiaocoder.android.fw.general.http.XCHttpAsyn;
import com.xiaocoder.android.fw.general.http.XCIHttpResult;
import com.xiaocoder.android.fw.general.jsonxml.XCJsonBean;
import com.xiaocoder.android.fw.general.util.UtilString;
import com.xiaocoder.android.fw.general.util.UtilSystem;
import com.xiaocoder.test.buffer.QlkBean;

/**
 * @author xiaocoder
 * @date 2014-12-30 下午5:04:48
 */
public class QlkHttpResponseHandler<T extends XCJsonBean> extends XCHttpResponseHandler<T> {


    public QlkHttpResponseHandler(XCIHttpResult result_http, int content_type, boolean show_background_when_net_fail,
                                  Class<T> result_bean_class) {
        super(result_http, content_type, show_background_when_net_fail, result_bean_class);
    }

    public QlkHttpResponseHandler(XCIHttpResult result_http, Class<T> result_bean_class) {
        super(result_http, result_bean_class);
    }


    // 需要根据公司业务重写
    public void yourCompanyLogic() {

        XCApplication.printi("yourCompanyResultRule()");

        if (!UtilString.isBlank(result_bean.obtString(QlkBean.MSG, ""))) {
            if (mContext instanceof XCBaseActivity) {
                if (((XCBaseActivity) mContext).isActivityDestroied()) {
                    result_boolean = false;
                    XCApplication.printe("activity被销毁了，result_boolean = false");
                    return;
                }
            }
            result_boolean = true;
        } else {
            result_boolean = false;
            XCApplication.shortToast(result_bean.obtString(QlkBean.MSG, ""));
        }

    }

    @Override
    public void yourCompanySecret(RequestParams params, AsyncHttpClient client, boolean needSecret) {

        XCApplication.printi("yourCompanySecret()");

        client.addHeader("_v", UtilSystem.getVersionCode(mContext) + "");// 版本号，必填
        client.addHeader("_m", UtilSystem.getMacAddress(mContext));// 设备的mac地址，选填
        client.addHeader("_c", "2222");// JSONP的回调函数名 ,可选
        client.addHeader("_p", "1"); // 平台，必填

//        String token = Qlk
//        params.put("token", token);
//        if (params != null && params.has("token")) {
//            String ts = System.currentTimeMillis() + "";
//            String sig = UtilMd5.sortAddMD5Params(ts, params);
//            //有token的接口需要添加sig
//            params.put("sig", sig);
//            params.put("ts", ts);
//        }
        XCApplication.printi(XCConfig.TAG_HTTP, "plus public params-->" + params.toString());
    }


    @Override
    public void closeHttpDialog() {
        if (httpDialog != null && httpDialog.isShowing()) {
            httpDialog.dismiss();
            httpDialog.setOnKeyListener(null);
            httpDialog.cancel();
            XCApplication.printi("closeHttpDialog()");
        }
    }

    @Override
    public void showHttpDialog() {
        if (httpDialog == null) {
            httpDialog = new XCSystemHDialog(mContext, XCBaseDialog.TRAN_STYLE);
            httpDialog.setCanceledOnTouchOutside(false);
            httpDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        closeHttpDialog();
                        XCHttpAsyn.resetNetingStatus();
                        if (!(mContext instanceof XCBaseMainActivity)) {
                            ((XCBaseActivity) mContext).myFinish();
                        }
                    }
                    return false;
                }
            });
            httpDialog.show();
            XCApplication.printi("showHttpDialog()");
        }
    }
}