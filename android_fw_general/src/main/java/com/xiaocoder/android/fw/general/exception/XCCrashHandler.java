package com.xiaocoder.android.fw.general.exception;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.function.helper.XCActivityHelper;
import com.xiaocoder.android.fw.general.io.XCIO;
import com.xiaocoder.android.fw.general.io.XCIOAndroid;
import com.xiaocoder.android.fw.general.io.XCLog;
import com.xiaocoder.android.fw.general.util.UtilDate;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 使用时记得在清单文件中注册 XLShowExceptionsActivity
 */
public class XCCrashHandler implements UncaughtExceptionHandler {

    private static XCCrashHandler INSTANCE = new XCCrashHandler();

    private XCApp application;

    /**
     * 异常信息
     */
    private Map<String, String> infos = new HashMap<String, String>();
    /**
     * 是否显示异常界面
     */
    private boolean mIsShowExceptionActivity;
    /**
     * 存储SD卡的哪个目录
     */
    private String mCrashDir;
    /**
     * 异常上传接口
     */
    XCIException2Server uploadServer;
    /**
     * 异常db
     */
    XCExceptionModelDb exceptionModelDb;
    /**
     * 存入数据库的时间
     */
    String tempTime;

    public void setUploadServer(XCIException2Server uploadServer) {
        this.uploadServer = uploadServer;
    }

    private XCCrashHandler() {
    }

    public static XCCrashHandler getInstance() {
        return INSTANCE;
    }

    public XCExceptionModelDb getExceptionModelDb() {
        return exceptionModelDb;
    }

    public XCCrashHandler init(boolean isInit, Context context, String crash_dir, boolean isShowExceptionActivity) {

        if (isInit) {

            if (context instanceof XCApp) {
                application = (XCApp) context;
            } else {
                XCLog.e("XLCrashHandler的init方法中传入的application不是xcapp");
                throw new RuntimeException("XLCrashHandler的init方法中传入的application不是xcapp");
            }

            mIsShowExceptionActivity = isShowExceptionActivity;
            mCrashDir = crash_dir;

            exceptionModelDb = new XCExceptionModelDb(context, "XCExceptionDb.db", 1, "exception_1");

            Thread.setDefaultUncaughtExceptionHandler(this);
        }
        return INSTANCE;
    }

    /**
     * 当 UncaughtException 发生时会转入该函数来处理
     * <p/>
     * try catch的异常是不会回调这个方法的
     */
    @Override
    public synchronized void uncaughtException(Thread thread, Throwable ex) {

        // 收集设备参数信息
        collectDeviceInfo(application);

        // 设备参数信息 异常信息 写到crash目录的日志文件中
        String info = saveCrashInfo2File(ex);

        // 打印到控制台、写到app目录的log中
        toLogcat(info);

        // 是否打开showExcpetionAcivity
        toShowExceptionActivity(info);

        // 存入数据库，此时还未存userid，子类中根据需要是否存储
        XCExceptionModel model = sava2DB(info);

        //上传到服务器
        if (uploadServer != null) {
            uploadServer.uploadException2Server(info, ex, thread, model, exceptionModelDb);
        }

        endException();

    }

    private XCExceptionModel sava2DB(String info) {

        XCExceptionModel model = new XCExceptionModel(
                info,
                tempTime,
                XCExceptionModel.UPLOAD_NO,
                "",
                tempTime + XCConfig.UNDERLINE + UUID.randomUUID()
        );

        if (exceptionModelDb != null) {
            exceptionModelDb.insert(model);
        }

        return model;

    }

    /**
     * 在控制台显示 ，同时写入到log中
     */
    public void toLogcat(String hint) {

        XCLog.e(hint);

    }

    public static int QUIT_FREEZE_TIME = 2500;

    public void endException() {

        showToast(application, "很抱歉，程序遭遇异常，即将退出！");

        try {
            Thread.sleep(QUIT_FREEZE_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (application != null) {
            XCActivityHelper.appExit();
        }

    }

    /**
     * 收集设备参数信息
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);

            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            XCLog.e("an error occured when collect package info--", e);
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                XCLog.i(field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                e.printStackTrace();
                XCLog.e("an error occured when collect crash info--", e);
            }
        }
    }

    /**
     * 保存错误信息到crash目录的文件中 
     */
    public String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String result = writer.toString();
        sb.append(result);

        try {
            tempTime = System.currentTimeMillis() + "";
            String time = UtilDate.format(new Date(), UtilDate.FORMAT_LONG);
            String fileName = "crash-" + time + "-" + tempTime + ".log";

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                FileOutputStream fos = new FileOutputStream(XCIOAndroid.createFileInSDCard(mCrashDir, fileName));
                fos.write(("crash=" + fileName + XCIO.LINE_SEPARATOR + sb.toString()).getBytes());
                fos.close();

            }

            return "crash=" + fileName + XCIO.LINE_SEPARATOR + sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            XCLog.e("an error occured while writing file--", e);
        }

        return sb.toString();
    }

    public static String EXCEPTION_INFO = "exception_info";

    /**
     * 打开一个activity展示异常信息
     */
    public void toShowExceptionActivity(String info) {
        if (mIsShowExceptionActivity) {
            Intent intent = new Intent(application, XLShowExceptionsActivity.class);
            intent.putExtra(EXCEPTION_INFO, info);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            application.startActivity(intent);
        }
    }

    /**
     * 显示提示信息，需要在线程中显示Toast
     */
    private void showToast(final Context context, final String msg) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }).start();
    }

}
