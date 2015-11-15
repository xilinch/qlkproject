package com.xiaocoder.test.line_point;

import android.os.Bundle;
import android.widget.ImageView;

import com.xiaocoder.android.fw.general.io.XCLog;
import com.xiaocoder.android.fw.general.tool.XC;
import com.xiaocoder.views.fragment.XCTitleCommonFragment;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;

public class LinePointActivityGC extends MActivity {
    int i = 10;
    ImageView test_imageview;
    XCTitleCommonFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_point);
        test_imageview = getViewById(R.id.test_imageview);
        addFragment(R.id.test, fragment = new XCTitleCommonFragment());
    }

    @Override
    public void initWidgets() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                XCLog.i(test_imageview);
                XCLog.i(i);

                System.gc();
                System.gc();

                XCLog.i(fragment.toString());
                XCLog.i(fragment.getActivity());

                XC.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        XCLog.i("handler --" + test_imageview); // 不为空
                        XCLog.i("handler --" + i);// 不为空

                        XCLog.i("handler --" + fragment.toString()); // 不为空
                        XCLog.i("handler --" + fragment.getActivity()); // 空
                    }
                }, 10000);

            }
        }).start();

    }

    @Override
    public void listeners() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XCLog.i(this+"--onDestroy()");
    }
}
