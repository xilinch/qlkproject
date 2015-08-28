package com.xiaocoder.test.dialogs;

import android.os.Bundle;
import android.view.View;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.view.CircleProgressBar;
import com.xiaocoder.buffer.QlkActivity;
import com.xiaocoder.test.R;

public class CircleProgressBarActivity extends QlkActivity {

    int progress = 0;
    CircleProgressBar progress1;
    CircleProgressBar progress2;
    CircleProgressBar progressWithArrow;
    CircleProgressBar progressWithoutBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_circle_progress);
        super.onCreate(savedInstanceState);
        progress1 = (CircleProgressBar) findViewById(R.id.progress1);
        progress2 = (CircleProgressBar) findViewById(R.id.progress2);
        progressWithArrow = (CircleProgressBar) findViewById(R.id.progressWithArrow);
        progressWithoutBg = (CircleProgressBar) findViewById(R.id.progressWithoutBg);


//        progress1.setColorSchemeResources(android.R.color.holo_blue_bright);
        progress2.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        progressWithArrow.setColorSchemeResources(android.R.color.holo_orange_light);
        progressWithoutBg.setColorSchemeResources(android.R.color.holo_red_light);

        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            XCApplication.getBase_handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (finalI * 10 >= 90) {
                        progress1.setVisibility(View.VISIBLE);
                        progress2.setVisibility(View.INVISIBLE);
                    } else {
                        progress2.setProgress(finalI * 10);
                    }
                }
            }, 1000 * (i + 1));
        }

    }


    @Override
    public void initWidgets() {

    }

    @Override
    public void listeners() {

    }

    @Override
    public void onNetRefresh() {

    }
}