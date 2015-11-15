package com.xiaocoder.android.fw.general.function.helper;

import android.app.Activity;

import com.xiaocoder.android.fw.general.io.XCLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Created by xiaocoder on 2015/11/4.
 * description: 管理activity
 */
public class XCActivityHelper {

    private static Stack<Activity> stack = new Stack<Activity>();

    public static Stack<Activity> getStack() {
        return stack;
    }

    private XCActivityHelper() {
    }

    /**
     * 添加Activity到栈中
     */
    public static void addActivityToStack(Activity activity) {
        stack.push(activity);
    }

    /**
     * 把Activity移出栈
     */
    public static void delActivityFromStack(Activity activity) {
        stack.remove(activity);
    }

    /**
     * 获取顶层Activity（activity不删除）
     */
    public static Activity getCurrentActivity() {

        return stack.lastElement();
    }

    /**
     * 判断某个acivity实例是否存在
     */
    public static boolean isActivityExist(Class<?> cls) {
        for (Activity activity : stack) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取某(几)个activity（activity不删除）
     */
    public static List<Activity> getActivity(Class<?> cls) {
        List<Activity> list = new ArrayList<Activity>();
        for (Activity activity : stack) {
            if (activity.getClass().equals(cls)) {
                list.add(activity);
            }
        }
        return list;
    }

    /**
     * 结束指定的一个Activity
     */
    public static void finishActivity(Activity activity) {
        if (activity != null) {
            stack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 通过class ， 结束指定类名的（几个或一个）Activity
     */
    public static void finishActivity(Class<?> cls) {

        for (Iterator<Activity> it = stack.iterator(); it.hasNext(); ) {
            Activity activity = it.next();
            if (activity.getClass().equals(cls)) {
                // finishActivity(activity);// 并发修改异常
                it.remove();
                activity.finish();
            }
        }
    }

    /**
     * 结束当前Activity
     */
    public static void finishCurrentActivity() {

        finishActivity(getCurrentActivity());
    }

    /**
     * 关闭所有的activity
     */
    public static void finishAllActivity() {
        for (Activity activity : stack) {
            // finishActivity(activity);//并发修改异常
            if (activity != null) {
                activity.finish();
            }
        }
        stack.clear();
    }

    /**
     * 去到指定的activity，该activity之上的activitys将销毁
     * <p/>
     * 如果该activity不存在，则一个也不删除
     */
    public static Activity toActivity(Class<? extends Activity> activity_class) {

        if (isActivityExist(activity_class)) {
            // activity存在
            Activity toActivity = null;

            while (true) {

                if (stack.isEmpty()) {
                    XCLog.e("---toActivity()的stack为null");
                    return null;
                }

                // 获取顶层Activity（activity暂不删除）
                toActivity = getCurrentActivity();

                if (toActivity == null) {
                    return null;
                }

                if (toActivity.getClass().getName().equals(activity_class.getName())) {
                    // 这个activity是返回的，不可以删
                    break;
                } else {
                    // 删除activity
                    finishActivity(toActivity);
                }

            }
            return toActivity;
        } else {
            // activity不存在
            return null;
        }
    }

    /**
     * 退出应用程序
     */
    public static void appExit() {
        finishAllActivity();
        System.exit(0);
    }

}
