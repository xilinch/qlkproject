package com.xiaocoder.android.fw.general.util;import android.text.TextUtils;import com.loopj.android.http.RequestParams;import com.xiaocoder.android.fw.general.application.XCApplication;import com.xiaocoder.android.fw.general.base.XCConfig;import java.security.MessageDigest;import java.util.Arrays;/* * 16位-->32位 , 未加别的算法 */public class UtilMd5 {	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };	public static String byteArrayToHexString(byte[] b) {		StringBuffer resultSb = new StringBuffer();		for (int i = 0; i < b.length; i++) {			resultSb.append(byteToHexString(b[i]));		}		return resultSb.toString();	}	private static String byteToHexString(byte b) {		int n = b;		if (n < 0)			n = 256 + n;		int d1 = n / 16;		int d2 = n % 16;		return hexDigits[d1] + hexDigits[d2];	}	public static String MD5Encode(String origin) {		String resultString = null;		try {			resultString = new String(origin);			MessageDigest md = MessageDigest.getInstance("MD5");			resultString = byteArrayToHexString(md.digest(resultString.getBytes()));		} catch (Exception ex) {			ex.printStackTrace();		}		return resultString;	}	/*	 * 排序和md5参数 	 */	public static String sortAddMD5Params(String ts, String p_key, RequestParams params) {		String sig = "";		params.remove("p_key");		String[] str;		str = params.toString().split("&");		Arrays.sort(str, String.CASE_INSENSITIVE_ORDER);        XCApplication.printi(XCConfig.TAG_HTTP, params.toString() + "----->params");		for (int i = 0; i < str.length; i++) {			sig += str[i] + "&";		}		XCApplication.printi(XCConfig.TAG_HTTP, sig + "------>sig");		sig = sig + "ts=" + ts + "&p_key=" + p_key;		XCApplication.printi(XCConfig.TAG_HTTP, sig + "------>sig plus");		sig = UtilMd5.MD5Encode(sig);		XCApplication.printi(XCConfig.TAG_HTTP, sig + "------>sig secret");		return sig;	}	/* * 排序和md5参数 */	public static String sortAddMD5Params(String ts, RequestParams params) {		String sig = "";		String token = "";		String[] str;		str = params.toString().split("&");		Arrays.sort(str, String.CASE_INSENSITIVE_ORDER);		XCApplication.printi(XCConfig.TAG_HTTP, params.toString() + "----->params");		int length = str.length;		for (int i = 0; i < length; i++) {			String[] param_keyValue = str[i].split("=");			if(param_keyValue != null && param_keyValue.length == 2 && param_keyValue[0].equals("token")){					token = param_keyValue[1];			}else if(param_keyValue != null && param_keyValue[0].equals("userId")){					//不需要userId			}else{				 sig += str[i] + "&";			}		}		if(!TextUtils.isEmpty(sig)){			sig = sig.substring(0,sig.lastIndexOf("&"));		}		XCApplication.printi(XCConfig.TAG_HTTP, sig + "------>sig");		sig =  "token=" + token + "&ts=" + ts + "&" +sig ;		XCApplication.printi(XCConfig.TAG_HTTP, sig + "------>sig plus");		sig = UtilMd5.MD5Encode(sig);		XCApplication.printi(XCConfig.TAG_HTTP, sig +  "------>sig secret");		return sig;	}}