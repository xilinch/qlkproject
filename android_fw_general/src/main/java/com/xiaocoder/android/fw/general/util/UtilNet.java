package com.xiaocoder.android.fw.general.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xiaocoder.android.fw.general.tool.XC;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class UtilNet {

    /**
     * 网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 是否是wifi
     */
    public static boolean isNetWorkWifi(Context context) {
        if (isNetworkAvailable(context)) {
            ConnectivityManager conManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * ip地址
     */
    public static String getIpAddr() {
        String ip = "000.000.000.000";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        ip = inetAddress.getHostAddress().toString();
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return ip;
    }

    /**
     * 获取网络类型
     *
     * @return String 返回网络类型
     */
    public static String getAccessNetworkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        int type = cm.getActiveNetworkInfo().getType();
        if (type == ConnectivityManager.TYPE_WIFI) {
            return "wifi";
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            return "phone";
        }
        return "";
    }

    /**
     * 获得当前网络类型
     *
     * @return TYPE_MOBILE_CMNET:1 TYPE_MOBILE_CMWAP:2 TYPE_WIFI:3 TYPE_NO:0(未知类型)
     */
    public static int getNetWorkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获得当前网络信息
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isAvailable()) {
            int currentNetWork = ni.getType();
            if (currentNetWork == ConnectivityManager.TYPE_MOBILE) {
                if (ni.getExtraInfo() != null && ni.getExtraInfo().equals("cmwap")) {
                    XC.i("", "当前网络为:cmwap网络");
                    return TYPE_MOBILE_CMWAP;
                } else if (ni.getExtraInfo() != null && ni.getExtraInfo().equals("uniwap")) {
                    XC.i("", "当前网络为:uniwap网络");
                    return TYPE_MOBILE_CMWAP;
                } else if (ni.getExtraInfo() != null && ni.getExtraInfo().equals("3gwap")) {
                    XC.i("", "当前网络为:3gwap网络");
                    return TYPE_MOBILE_CMWAP;
                } else if (ni.getExtraInfo() != null && ni.getExtraInfo().contains("ctwap")) {
                    XC.i("", "当前网络为:" + ni.getExtraInfo() + "网络");
                    return TYPE_MOBILE_CTWAP;
                } else {
                    XC.i("", "当前网络为:net网络");
                    return TYPE_MOBILE_CMNET;
                }

            } else if (currentNetWork == ConnectivityManager.TYPE_WIFI) {
                XC.i("", "当前网络为:WIFI网络");
                return TYPE_WIFI;
            }
        }
        XC.i("", "当前网络为:不是我们考虑的网络");
        return TYPE_NO;
    }

    public static int TYPE_NO = 0;
    public static int TYPE_MOBILE_CMNET = 1;
    public static int TYPE_MOBILE_CMWAP = 2;
    public static int TYPE_WIFI = 3;
    public static int TYPE_MOBILE_CTWAP = 4; // 移动梦网代理
}
