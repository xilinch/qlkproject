package com.xiaocoder.test.baidumap;

import android.os.Bundle;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.middle.function.MBaiduMapActivity;
import com.xiaocoder.test.R;


public class MapActivity extends MBaiduMapActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_map);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initWidgets() {


        setOnReceiverAddressListener(new OnReceiverAddressListener() {
            @Override
            public void onReceiverAddressListener(String addr) {
                XCApp.shortToast(addr);
            }
        });

    }

    @Override
    public void listeners() {

    }


}
