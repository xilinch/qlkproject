package com.xiaocoder.android.fw.general.js_xl_encryption.des;

/**
 * DES加密解密类
 * @author 徐金山
 * @version 1.0
 */
public class DesEncryptAndDecrypt {
	/**
	 * 加密处理
	 * @param requestStr 明文请求报文（明文字符串）
	 * @return String 密文请求报文（密文字符串）
	 */
	public static String encodeRequestStr(String requestStr) {
		String encryptedStr = "";
		
		try {
			encryptedStr = Base64DesEncrypt.getInstance().encode(requestStr);
		} catch (Exception e) {
			e.printStackTrace();
			encryptedStr = "";
		}
		
		return encryptedStr;
	}
	
	/**
	 * 解密处理
	 * @param responseStr 密文应答报文（密文字符串）
	 * @return String 明文应答报文（明文字符串）
	 */
	public static String decodeResponseStr(String responseStr) {
		String decipheredStr = "";
		
		try {
			decipheredStr = Base64DesEncrypt.getInstance().decode(responseStr);
		} catch (Exception e) {
			e.printStackTrace();
			decipheredStr = "";
		}
		
		return decipheredStr;
	}
}
