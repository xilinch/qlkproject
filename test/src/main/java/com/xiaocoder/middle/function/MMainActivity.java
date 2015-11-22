package com.xiaocoder.middle.function;

import android.os.Bundle;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaocoder.android.fw.general.exception.XCCrashHandler;
import com.xiaocoder.android.fw.general.exception.XCExceptionModelDb;
import com.xiaocoder.android.fw.general.function.helper.XCActivityHelper;
import com.xiaocoder.android.fw.general.io.XCLog;
import com.xiaocoder.middle.MActivity;

/**
 * Created by xiaocoder on 2015/7/15.
 */
public abstract class MMainActivity extends MActivity {

    /**
     * 双击两次返回键退出应用
     */
    long back_quit_time;
    public static final int CLICK_QUICK_GAP = 1000;

    @Override
    public void onBackPressed() {
        long this_quit_time = System.currentTimeMillis();
        if (this_quit_time - back_quit_time <= CLICK_QUICK_GAP) {
            XCActivityHelper.appExit();
        } else {
            back_quit_time = this_quit_time;
            XCLog.shortToast("快速再按一次退出");
        }
    }

    /**
     * 首页的activity不可滑动销毁
     */
    @Override
    protected void initSlideDestroyActivity() {

    }

    /**
     * 友盟统计的一些初始化
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.updateOnlineConfig(getApplicationContext());
        AnalyticsConfig.enableEncrypt(true);
        uploadException();
    }

    /**
     * 进入首页时或闪屏页，把未上传的异常信息上传到服务器
     */
    protected void uploadException() {

        XCExceptionModelDb exceptionModelDb = XCCrashHandler.getInstance().getExceptionModelDb();

        if (exceptionModelDb != null) {
            // TODO 每次启动时，将上一次的异常信息上传到服务器，每上传成功一条，更新model中的uploadSuccess为“1”
            XCLog.itemp(exceptionModelDb.queryCount());
            XCLog.itemp(exceptionModelDb.queryUploadFail(XCExceptionModelDb.SORT_DESC));
            //XCLog.itemp(exceptionModelDb.queryUploadFail(XCExceptionModelDb.SORT_ASC));
            //XCLog.itemp(exceptionModelDb.queryUploadSuccess(XCExceptionModelDb.SORT_DESC));
            //XCLog.itemp(exceptionModelDb.queryAllByDESC());
        }

    }
}
