package com.xiaocoder.android.fw.general.io;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xiaocoder.android.fw.general.application.XCConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.util.Date;

/**
 * 1 可以控制频率的吐司
 * 2 输出log到控制台
 * 3 输出log到文件
 * 4 日志的清空，日志的大小控制
 *
 * 使用前初始化initLog方法
 */
public class XCLog {
    /**
     * 毫秒
     */
    public static final int TOAST_SHORT_TIME_GAP = 2000;
    public static final int TOAST_LONG_TIME_GAP = 3000;
    /**
     * 缓存文件达到70M就会清空
     */
    public static final long LOG_FILE_LIMIT_SIZE = 73400320;
    /**
     * 记录上一次土司的时间
     */
    public static long last_toast_time;
    /**
     * log文件 = dir_name + log_file_name
     */
    public static File file;

    /**
     * 以下值需要初始化
     */
    public static Context context;
    public static boolean is_dtoast;
    public static boolean is_printlog;
    public static boolean is_output;
    public static String dir_name;
    public static String log_file_name;
    public static String temp_file_name;
    public static String encoding;

    public static void initXCLog(Context context, boolean is_dtoast, boolean is_output, boolean is_printlog,
                                 String dir_name, String log_file_name, String temp_file_name, String encoding) {

        XCLog.context = context;
        XCLog.dir_name = dir_name;
        XCLog.log_file_name = log_file_name;
        XCLog.temp_file_name = temp_file_name;
        XCLog.encoding = encoding;
        XCLog.is_dtoast = is_dtoast;
        XCLog.is_output = is_output;
        XCLog.is_printlog = is_printlog;
    }

    /**
     * 防止点击频繁, 不断的弹出
     */
    public static void longToast(Object msg) {
        if (System.currentTimeMillis() - last_toast_time > TOAST_LONG_TIME_GAP) {
            Toast.makeText(context, msg + "", Toast.LENGTH_LONG).show();
            last_toast_time = System.currentTimeMillis();
        }
    }

    /**
     * 防止点击频繁, 不断的弹出
     */
    public static void longToast(boolean showImmediately, Object msg) {
        if (showImmediately) {
            Toast.makeText(context, msg + "", Toast.LENGTH_LONG).show();
            last_toast_time = System.currentTimeMillis();
        } else {
            longToast(msg);
        }
    }

    /**
     * 防止点击频繁, 不断的弹出
     */
    public static void shortToast(Object msg) {
        if (System.currentTimeMillis() - last_toast_time > TOAST_SHORT_TIME_GAP) {
            Toast.makeText(context, msg + "", Toast.LENGTH_SHORT).show();
            last_toast_time = System.currentTimeMillis();
        }
    }

    /**
     * 防止点击频繁, 不断的弹出
     */
    public static void shortToast(boolean showImmediately, Object msg) {
        if (showImmediately) {
            Toast.makeText(context, msg + "", Toast.LENGTH_SHORT).show();
            last_toast_time = System.currentTimeMillis();
        } else {
            shortToast(msg);
        }
    }


    /**
     * 调试的toast , 上线前开关关闭
     */
    public static void dShortToast(Object msg) {
        if (is_dtoast) {
            if (System.currentTimeMillis() - last_toast_time > TOAST_SHORT_TIME_GAP) {
                Toast.makeText(context, msg + "", Toast.LENGTH_SHORT).show();
                last_toast_time = System.currentTimeMillis();
            }
        }
    }

    /**
     * 调试的toast , 上线前开关关闭
     */
    public static void dLongToast(Object msg) {
        if (is_dtoast) {
            if (System.currentTimeMillis() - last_toast_time > TOAST_LONG_TIME_GAP) {
                Toast.makeText(context, msg + "", Toast.LENGTH_LONG).show();
                last_toast_time = System.currentTimeMillis();
            }
        }
    }

    /**
     * 以tag打印到控制台 和 文件
     * <p/>
     * 上线前is_output 与 is_printlog关闭
     */
    public static void i(Context context, Object msg) {
        if (is_output) {
            Log.i(context.getClass().getSimpleName(), msg + "");
        }
        if (is_printlog) {
            writeLog2File(context.getClass().getSimpleName() + "---" + msg, true);
        }
    }

    public static void i(String tag, Object msg) {
        if (is_output) {
            Log.i(tag, msg + "");
        }
        if (is_printlog) {
            writeLog2File(msg + "", true);
        }
    }

    public static void i(Object msg) {
        if (is_output) {
            Log.i(XCConfig.TAG_SYSTEM_OUT, msg + "");
        }
        if (is_printlog) {
            writeLog2File(msg + "", true);
        }
    }

    public static void itemp(Object msg) {
        i(XCConfig.TAG_TEMP, msg);
    }

    public static void itest(Object msg) {

        i(XCConfig.TAG_TEST, msg);
    }

    /**
     * 不管是否上线，都打印日志到本地，并输出到控制台
     * 注：e的日志颜色不同
     */
    public static void e(String hint) {
        Log.e(XCConfig.TAG_ALOG, hint);
        writeLog2File(hint, true);
    }

    public static void e(Context context, String hint) {
        Log.e(XCConfig.TAG_ALOG, context.getClass().getSimpleName() + "--" + hint);
        writeLog2File(context.getClass().getSimpleName() + "--" + hint, true);
    }

    public static void e(String hint, Exception e) {
        e.printStackTrace();
        Log.e(XCConfig.TAG_ALOG, hint + "--" + "Exception-->" + e.toString() + "--" + e.getMessage());
        writeLog2File("Exception-->" + hint + "-->" + e.toString() + "--" + e.getMessage(), true);
    }

    public static void e(Context context, String hint, Exception e) {
        e.printStackTrace();
        Log.e(XCConfig.TAG_ALOG, "Exception-->" + context.getClass().getSimpleName() + "--" + hint + "--" + e.toString() + "--" + e.getMessage());
        writeLog2File("Exception-->" + context.getClass().getSimpleName() + "--" + hint + "--" + e.toString() + "--" + e.getMessage(), true);
    }

    /**
     * 删除日志文件
     */
    public static synchronized void clearLog() {
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    /**
     * 只在有sd卡的时候，才会打印日志
     */
    public static synchronized void writeLog2File(String content, boolean is_append) {

        if (TextUtils.isEmpty(content) || !XCIOAndroid.isSDcardExist()) {
            return;
        }

        RandomAccessFile raf = null;

        try {
            if (file == null || !file.exists()) {

                // sd中，在app_root文件夹下创建log文件
                file = XCIOAndroid.createFileInSDCard(dir_name, log_file_name);

            }

            // 日志满了的处理
            logFull();

            // 已存在文件
            if (!is_append) {
                // 假如不允许追加写入，则删除 后 重新创建
                file.delete();
                file.createNewFile();
            }

            raf = new RandomAccessFile(file, "rw");
            long len = raf.length();
            raf.seek(len);
            raf.write((content + "-->" + DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.LONG).format(new Date()) + "  end  " + System.getProperty("line.separator"))
                    .getBytes(encoding));

        } catch (Exception e) {
            e.printStackTrace();
            // 这里不要调用e(),可能相互调用，异常
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 打印到XCConfig.TEMP_PRINT_FILE文件中，会覆盖之前的打印信息
     * <p/>
     * 场景：如果json很长，有时控制台未必会全部打印出来，则可以去app的目录下找到这个临时文件查看
     */
    public synchronized static void tempPrint(String str) {
        if (is_output) {
            FileOutputStream fos = null;
            try {
                // 在app_root目录下创建temp_print文件，如果没有sd卡，则写到内部存储中
                fos = new FileOutputStream(XCIOAndroid.createFileInAndroid(context, dir_name, temp_file_name));
                fos.write(str.getBytes());
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    } finally {
                        fos = null;
                    }
                }
            }
        }
    }

    public static void logFull() throws Exception {
        if (file != null && file.exists() && file.length() > LOG_FILE_LIMIT_SIZE) {
            file.delete();
            file.createNewFile();
        }
    }

}