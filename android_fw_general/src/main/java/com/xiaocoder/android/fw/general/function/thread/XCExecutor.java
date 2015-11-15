package com.xiaocoder.android.fw.general.function.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 该类是单例的 ， 可以获取自动变动大小的线程池 与 single大小的线程池
 *
 * @author xiaocoder
 *         2014-10-17 下午1:52:54
 */
public class XCExecutor {

    private static ExecutorService threadpool_single;
    private static ExecutorService threadpool_cache;
    private static ExecutorService threadpool_fix;
    private static ScheduledExecutorService scheduled_threadpool_fix;

    private static XCExecutor executor = new XCExecutor();

    private XCExecutor() {
    }

    public static void initXCExecutor(int fixNum, int scheduledFixNum) {

        if (threadpool_single == null) {
            synchronized (XCExecutor.class) {
                if (threadpool_single == null) {
                    threadpool_single = Executors.newSingleThreadExecutor();
                }
            }
        }

        if (threadpool_cache == null) {
            synchronized (XCExecutor.class) {
                if (threadpool_cache == null) {
                    threadpool_cache = Executors.newCachedThreadPool();
                }
            }
        }

        if (fixNum > 0) {
            if (threadpool_fix == null) {
                synchronized (XCExecutor.class) {
                    if (threadpool_fix == null) {
                        threadpool_fix = Executors.newFixedThreadPool(fixNum);
                    }
                }
            }
        }

        if (scheduledFixNum > 0) {
            if (scheduled_threadpool_fix == null) {
                synchronized (XCExecutor.class) {
                    if (scheduled_threadpool_fix == null) {
                        scheduled_threadpool_fix = Executors.newScheduledThreadPool(scheduledFixNum);
                    }
                }
            }
        }
    }

    public static ExecutorService getSingle() {
        return threadpool_single;
    }

    public static ExecutorService getCache() {
        return threadpool_cache;
    }

    public static ExecutorService getFix() {
        return threadpool_fix;
    }

    public static ScheduledExecutorService getScheduledFix() {

        return scheduled_threadpool_fix;
    }

}
