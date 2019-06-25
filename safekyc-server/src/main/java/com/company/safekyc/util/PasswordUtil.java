package com.company.safekyc.util;

import java.util.UUID;


public class PasswordUtil {

	static int encoder = 347;
	
	public static void main(String[] args) {
		System.out.println(decodeString("14375949872614401094"));
	}

	public static String encodeString(String str) {
		String ret = "";
		for (int i = 0; i < str.length(); i++) {
			String tmp = str.substring(i, i + 1);
			int num = encoder * (int) (Math.random() * 26);
			num += (tmp.charAt(0));
			tmp = "0000" + num;
			tmp = tmp.substring(tmp.length() - 4);
			ret += tmp;
		}
		return ret;
	}

	public static String decodeString(String str) {
		String resStr, unify, tempc1, tempc2, tempc3, tempc4;
		int len;
		int num;
		resStr = "";
		len = str.length();
		len = len / 4;
		for (int i = 0; i <= len - 1; i++) {
			tempc1 = str.substring(4 * i + 0, 4 * i + 1);
			tempc2 = str.substring(4 * i + 1, 4 * i + 2);
			tempc3 = str.substring(4 * i + 2, 4 * i + 3);
			tempc4 = str.substring(4 * i + 3, 4 * i + 4);
			unify = tempc1 + tempc2 + tempc3 + tempc4;
			num = Integer.parseInt(unify) % encoder;
			resStr = resStr + (char) num;
		}
		return resStr;
	}
	
	public static String generateRandomPassword(){
		return UUID.randomUUID().toString().substring(0, 8);		
	}
}
