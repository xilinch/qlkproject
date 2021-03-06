package com.xiaocoder.test.http;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xiaocoder.android.fw.general.function.thread.XCExecutor;
import com.xiaocoder.android.fw.general.http.XCHttper;
import com.xiaocoder.views.dialog.XCQueryDialog;
import com.xiaocoder.android.fw.general.function.runnable.XCDownloadRunnable;
import com.xiaocoder.android.fw.general.io.XCIOAndroid;
import com.xiaocoder.android.fw.general.util.UtilString;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.middle.MConfig;
import com.xiaocoder.middle.parse.MBean;
import com.xiaocoder.middle.parse.MResponseHandlerBean;
import com.xiaocoder.test.R;

import org.apache.http.Header;

import java.io.File;
import java.util.HashMap;

public class HttpDownLoadActivity extends MActivity {

    Button button;
    XCQueryDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_http_download);
        super.onCreate(savedInstanceState);
    }

    public static String url = "http://yyf.7lk.com/api/goods/category-goods-list?userId=399&token=c2a623a6f3c7d6e1a126f1655c13b3f0&_m=&catId=515&_v=1.0.0&page=1&num=20&ts=1438155912203&_c=&_p=android&sig=96702f0846e8cb5d2701f5e39f28ba95";

    public void request() {
//        XCApp.getAsyn(true,true, this, "http://" + MainActivity.TEST_HOST + ":8080/qlktest/test.mp3", new RequestParams(), new QlkHttpResponseHandler(HttpDownLoadActivity.this) {
        XCHttper.getAsyn(true, url, new HashMap<String, Object>()
                , new MResponseHandlerBean<MBean>(this, this, MBean.class) {

            @Override
            public void success(int code, Header[] headers, byte[] arg2) {
                super.success(code, headers, arg2);
                // 这里拿到的result_bean是一个XCJsonBean对象
                if (result_boolean) {

                    dialog = new XCQueryDialog(HttpDownLoadActivity.this, "下载提示", "该文件大小为" + UtilString.getFileSizeByUnit(arg2.length), new String[]{"下载", "取消"}, false);

                    dialog.setOnDecideListener(new XCQueryDialog.OnDecideListener() {
                        @Override
                        public void confirm() {
                            downLoad();
                            dialog.dismiss();
                        }

                        @Override
                        public void cancle() {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }

        });
    }

    private void downLoad() {

        XCDownloadRunnable downloadHelper = new XCDownloadRunnable("http://www.baidu.com"
                , XCIOAndroid.createFileInAndroid(getApplicationContext(), MConfig.APP_ROOT, "downfile"));

        downloadHelper.setDownloadListener(new XCDownloadRunnable.DownloadListener() {
            @Override
            public void downloadFinished(long totalSize, File file) {

            }

            @Override
            public void downloadProgress(int len, long totalProgress, long totalSize, File file) {

            }

            @Override
            public void downloadStart(long totalSize, File file) {

            }

            @Override
            public void netFail(File file) {

            }
        });

        XCExecutor.getCache().execute(downloadHelper);
    }

    @Override
    public void initWidgets() {
        button = getViewById(R.id.test_http);
        request();
    }

    @Override
    public void listeners() {
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                request();
            }
        });
    }

}
