package com.itheima.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Uitls {
	/**
	 * md5加密工具方法
	 * 
	 * @param password 要加密的密码
	 * @return 密文
	 */
	public static String encoder(String password){
		try {
			// 信息摘要
			MessageDigest digest = MessageDigest.getInstance("md5");
			// 转换成byte数组
			byte[] result = digest.digest(password.getBytes());
			StringBuffer buffer = new StringBuffer();
			// 遍历数组，做与运算
			for (byte b : result) {
				// 与8个二进制做与运算
				int number = b & 0xFF;
				// 转换成16进制
				String numberStr = Integer.toHexString(number);
				if (numberStr.length() == 1) {
					buffer.append("0");
				}
				buffer.append(numberStr);

			}
			// 标准的md5加密后的结果

			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
}
