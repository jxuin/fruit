package com.fruit.util;

import java.security.MessageDigest;

public class MD5 {
	
	public static String getMd5(byte[] bytes) {
		String md5Str = null;
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(bytes);
			md5Str = toHexString(algorithm.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5Str;
	}

	private static String toHexString(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			hexString.append(Integer.toHexString(0xFF & b));
		}
		return hexString.toString();
	}
}
