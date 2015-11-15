package com.xiaocoder.middle;

import android.content.Context;

import com.xiaocoder.android.fw.general.application.XCBaseActivity;
import com.xiaocoder.android.fw.general.io.XCSP;

/**
 * Created by xiaocoder on 2015/7/13.
 * <p/>
 * 用户的信息，登录状态，用户刷新，注销 等统一在这里存取
 */
public class MUser {

    public static String USER_NAME = "userName";
    public static String USER_ID = "userId";
    public static String USER_TOKEN = "userToken";
    public static String IS_LOGIN = "isLogin";
    public static String USER_HEAD = "userHeader";
    public static String USER_PHONE_NUM = "userPhoneNum";
    // TODO 添加存储的字段

    public static String getUserId() {

        return XCSP.spGet(USER_ID, "");

    }

    public static String getUserToken() {

        return XCSP.spGet(USER_TOKEN, "");

    }

    public static String getUserName() {

        return XCSP.spGet(USER_NAME, "");

    }

    public static String getUserHead() {

        return XCSP.spGet(USER_HEAD, "");
    }

    public static String getUserPhoneNum() {

        return XCSP.spGet(USER_PHONE_NUM, "");
    }

    /**
     * 获取登录状态
     */
    public static boolean isLogin() {

        return XCSP.spGet(IS_LOGIN, false);

    }

    public static void putUserId(String userId) {

        XCSP.spPut(USER_ID, userId);

    }

    public static void putUserToken(String userToken) {

        XCSP.spPut(USER_TOKEN, userToken);

    }

    public static void putUserName(String userName) {

        XCSP.spPut(USER_NAME, userName);

    }

    public static void putUserHeader(String userHeader) {

        XCSP.spPut(USER_HEAD, userHeader);

    }

    public static void putUserPhoneNum(String userPhoneNum) {

        XCSP.spPut(USER_PHONE_NUM, userPhoneNum);

    }

    /**
     * 保存登录状态
     */
    public static void putLogin(boolean isLogin) {

        XCSP.spPut(IS_LOGIN, isLogin);

    }


    /**
     * 注销
     */
    public static void loginOut(Class<? extends XCBaseActivity> classes, Context context) {

    }

    /**
     * 刷新用户信息
     */
    public static void refreshUserInfo(Context context) {

    }

}
