package com.itheima.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Uitls {
	/**
	 * md5���ܹ��߷���
	 * 
	 * @param password Ҫ���ܵ�����
	 * @return ����
	 */
	public static String encoder(String password){
		try {
			// ��ϢժҪ
			MessageDigest digest = MessageDigest.getInstance("md5");
			// ת����byte����
			byte[] result = digest.digest(password.getBytes());
			StringBuffer buffer = new StringBuffer();
			// �������飬��������
			for (byte b : result) {
				// ��8����������������
				int number = b & 0xFF;
				// ת����16����
				String numberStr = Integer.toHexString(number);
				if (numberStr.length() == 1) {
					buffer.append("0");
				}
				buffer.append(numberStr);

			}
			// ��׼��md5���ܺ�Ľ��

			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
}
