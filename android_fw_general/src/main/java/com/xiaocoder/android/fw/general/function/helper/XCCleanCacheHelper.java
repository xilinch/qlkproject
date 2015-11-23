package com.xiaocoder.android.fw.general.function.helper;

import android.app.Dialog;

import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.function.thread.XCExecutor;

import java.io.File;

/**
 * 删除缓存 ，是在子线程运行的（文件或文件夹）
 */
public class XCCleanCacheHelper {

    public interface RemoveDirListener {

        /**
         * 正要删除的文件
         * <p/>
         * 这个方法是在子线程中运行的
         */
        void removing(File file);

        /**
         * 子线程的删除文件代码执行完成，handler到主线程
         * <p/>
         * 这个方法是在主线程中运行的
         */
        void removeFinish();
    }

    RemoveDirListener mRemoveDirListener;

    public void setRemoveDirListener(RemoveDirListener removeDirListener) {
        this.mRemoveDirListener = removeDirListener;
    }

    public boolean isGoOnDeleting;

    Dialog mDeletingDialog;

    public XCCleanCacheHelper(Dialog deletingDialog) {

        mDeletingDialog = deletingDialog;
        // 如果是实际中, 可能存在正在删除, 而页面此时退出的情况
        isGoOnDeleting = true;

    }

    private void removeDir(File dir) {
        if (dir != null && dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                // 如果目录是系统级文件夹，java没有访问权限，那么会返回null数组
                for (File file : files) {
                    if (isGoOnDeleting) {
                        if (file.isDirectory()) {
                            removeDir(file);
                        } else {
                            if (mRemoveDirListener != null) {
                                mRemoveDirListener.removing(file);
                            }
                            file.delete();
                        }
                    }
                }
            }
            // dir.delete();
        }
    }

    /**
     * 子线程中删除
     */
    public void removeFileAsyn(final File file) {
        XCExecutor.getCache().execute(new Runnable() {
            @Override
            public void run() {
                // 如果文件不存在
                if (!file.exists()) {
                    return;
                }
                // 文件存在，则开始转圈
                XCApp.getBaseHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (mDeletingDialog != null) {
                            mDeletingDialog.show();
                        }
                    }
                });

                if (file.isDirectory()) {
                    removeDir(file);
                } else {
                    if (mRemoveDirListener != null) {
                        mRemoveDirListener.removing(file);
                    }
                    file.delete();
                }
                XCApp.getBaseHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mRemoveDirListener != null) {
                            mRemoveDirListener.removeFinish();
                        }

                        if (mDeletingDialog != null) {
                            mDeletingDialog.cancel();
                        }
                    }
                }, 2000);
            }
        });
    }

    public void quit() {
        isGoOnDeleting = false;
        mDeletingDialog = null;
    }
}
