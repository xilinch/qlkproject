package com.xiaocoder.middle;

import com.umeng.analytics.MobclickAgent;
import com.xiaocoder.android.fw.general.application.XCBaseActivity;

/**
 * Created by xiaocoder on 2015/7/15.
 */
public abstract class MActivity extends XCBaseActivity {

    @Override
    protected void onResume() {
        super.onResume();
        /*
         * 友盟统计
         */
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*
         * 友盟统计
         */
        MobclickAgent.onPause(this);
    }
}