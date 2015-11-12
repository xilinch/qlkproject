package com.xiaocoder.test.http;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.tool.XC;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.middle.parse.MResponseHandlerModel;
import com.xiaocoder.test.R;
import com.xiaocoder.test.bean.TestModel;

import org.apache.http.Header;

import java.util.HashMap;

public class HttpActivity extends MActivity {

    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_http);
        super.onCreate(savedInstanceState);
    }

    public static String url = "http://yyf.7lk.com/api/goods/category-goods-list?userId=399&token=c2a623a6f3c7d6e1a126f1655c13b3f0&_m=&catId=515&_v=1.0.0&page=1&num=20&ts=1438155912203&_c=&_p=android&sig=96702f0846e8cb5d2701f5e39f28ba95";

    public void request() {
        XC.getAsyn(true, url, new HashMap<String, Object>(),
                new MResponseHandlerModel<TestModel>(this,this, TestModel.class) {

                    @Override
                    public void success(int code, Header[] headers, byte[] arg2) {
                        super.success(code, headers, arg2);

                        XC.dShortToast("success");

                        if (result_boolean) {

                            XC.i(result_bean.toString());
                            XC.i(result_bean.getMsg());
                            XC.i(result_bean.getCode() + "");
                            XC.i(result_bean.getData().getResult().toString() + "");
                            XC.i(result_bean.getData().getTotalCount() + "");
                            XC.i(result_bean.getData().getTotalPages() + "");
                            XC.i(result_bean.getData().getResult().get(0).getCommission() + "");
                            XC.i(result_bean.getData().getResult().get(0).getImgUrl() + "");
                            XC.i(result_bean.getData().getResult().get(0).getMarketPrice() + "");
                            XC.i(result_bean.getData().getResult().get(0).getName() + "");
                            XC.i(result_bean.getData().getResult().get(1).getRebate() + "");
                            XC.i(result_bean.getData().getResult().get(2).getType() + "");
                            XC.i(result_bean.getData().getResult().get(2).getShare().getBaseUrl() + "");
                            XC.i(result_bean.getData().getResult().get(2).getShare().getContent() + "");
                            XC.i(result_bean.getData().getResult().get(2).getShare().getTitle() + "");
                            XC.i(result_bean.getData().getResult().get(2).getShare().getIcon() + "");
                            XC.i(result_bean.getData().getResult().get(1).getImgUrl());

                        }
                    }
                });
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
