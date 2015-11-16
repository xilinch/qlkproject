package com.xiaocoder.test.dialogs;

import android.os.Bundle;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.function.thread.XCExecutor;
import com.xiaocoder.android.fw.general.util.UtilView;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;
import com.xiaocoder.views.view.sx.SXProgressView;

/**
 * Created by xiaocoder on 2015/9/14.
 */
public class ProgressViewActivity extends MActivity {

    SXProgressView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_view_progress);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initWidgets() {
        view = getViewById(R.id.sx_id_progress_view);
        showProgressView();
    }

    @Override
    public void listeners() {

    }

    private void showProgressView() {
        UtilView.setVisible(true, view);
        XCExecutor.getCache().execute(new Runnable() {
            @Override
            public void run() {

                while (view.getProgress() < 100) {
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    XCApp.getBaseHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            view.setProgress(view.getProgress() + 2);
                        }
                    });
                }
            }
        });
    }

}
