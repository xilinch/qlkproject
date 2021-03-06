package com.xiaocoder.test.pop;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.io.XCLog;
import com.xiaocoder.android.fw.general.util.UtilScreen;
import com.xiaocoder.views.pop.XCHintPopupWindow;
import com.xiaocoder.views.pop.XCPhotoPopupWindow;
import com.xiaocoder.views.pop.XCReflectPopupWindow;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;

public class PopActivity extends MActivity {
    Button test_pop_button;
    Button test_pop_button2;
    Button test_pop_button3;

    XCHintPopupWindow hintPopupWindow;
    XCPhotoPopupWindow photoPopupWindow;
    XCReflectPopupWindow reflectPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pop);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initWidgets() {

        test_pop_button = getViewById(R.id.test_pop_button);
        test_pop_button2 = getViewById(R.id.test_pop_button2);
        test_pop_button3 = getViewById(R.id.test_pop_button3);

        reflectPopupWindow = new XCReflectPopupWindow(this, UtilScreen.getScreenWidthPx(this), UtilScreen.dip2px(this, 100));

        hintPopupWindow = new XCHintPopupWindow
                (this, (int) (UtilScreen.getScreenWidthPx(this) / 2.3), ViewGroup.LayoutParams.WRAP_CONTENT);

        photoPopupWindow = new XCPhotoPopupWindow
                (this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void listeners() {
        test_pop_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                reflectPopupWindow.showPopupWindow((View) (test_pop_button.getParent()), 0, -UtilScreen.dip2px(PopActivity.this, 100));
            }
        });

        test_pop_button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                photoPopupWindow.showAtLocation((View) test_pop_button2.getParent(), Gravity.BOTTOM, 0, 0);
            }
        });

        test_pop_button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hintPopupWindow.showAtLocation(test_pop_button3, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, UtilScreen.dip2px(PopActivity.this, 75));
            }
        });

        hintPopupWindow.setOnHintPopupItemClickListener(new XCHintPopupWindow.OnHintPopupItemClickListener() {
            @Override
            public void hint1(TextView textview) {
                XCLog.dShortToast("1");
                hintPopupWindow.dismiss();
            }

            @Override
            public void hint2(TextView textview) {
                XCLog.dShortToast("2");
                hintPopupWindow.dismiss();
            }

            @Override
            public void hint3(TextView textview) {
                XCLog.dShortToast("3");
                hintPopupWindow.dismiss();
            }
        });

        photoPopupWindow.setOnPhotoPopupItemClickListener(new XCPhotoPopupWindow.onPhotoPopupItemClickListener() {
            @Override
            public void onPhotoUpload() {
                XCLog.dShortToast("1");
                photoPopupWindow.dismiss();
            }

            @Override
            public void onLocalAlbum() {
                XCLog.dShortToast("2");
                photoPopupWindow.dismiss();
            }

            @Override
            public void onCancel() {
                XCLog.dShortToast("3");
                photoPopupWindow.dismiss();
            }

            @Override
            public void onNetPrescription() {
                XCLog.dShortToast("4");
                photoPopupWindow.dismiss();
            }
        });
    }

    boolean isFirst = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus && isFirst) {
            isFirst = false;
        }
    }

}
