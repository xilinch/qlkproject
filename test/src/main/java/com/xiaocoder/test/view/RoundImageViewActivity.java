package com.xiaocoder.test.view;

import android.os.Bundle;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCBaseActivity;
import com.xiaocoder.android.fw.general.view.XCRoundedImageView;
import com.xiaocoder.test.R;

public class RoundImageViewActivity extends XCBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置布局
        setContentView(R.layout.activity_round_image_view);
        super.onCreate(savedInstanceState);
    }


    // 初始化控件
    @Override
    public void initWidgets() {

        XCRoundedImageView image3 = getViewById(R.id.image3);
        XCApp.displayImage("http://www.baidu.com/img/bdlogo.png", image3);

    }

    // 设置监听
    @Override
    public void listeners() {

    }


}
