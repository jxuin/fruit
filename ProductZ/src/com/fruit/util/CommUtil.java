package com.fruit.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import com.fruit.log.SysLog;

/**
 * 通用工具类
 * 
 * @author xuj 2013.5.7
 * 
 */
public class CommUtil {

	public static final String VERSION = "1.0.0";

	public static final String TAG = "Product";

	public static final String LIST_BUNDLE = "list_bundle";

	public static final String DETAIL_BUNDLE = "detail_bundle";

	public static final String SEARCHRESULT_BUNDLE = "searchresult_bundle";

	public static boolean APP_EXITFLAG = false;

	public static String getStrVal(Object o) {
		String result = (o != null) ? o.toString() : "";
		return result;
	}

	public static int getIntVal(Object o, int defaultVal) {
		String resultStr = (o != null) ? o.toString() : "";
		int result = isNum(resultStr) ? Integer.parseInt(resultStr) : defaultVal;
		return result;
	}

	public static Float getFloatVal(Object o, Float defaultVal) {
		String resultStr = (o != null) ? o.toString() : "";
		Float result = isDecimal(resultStr) ? Float.parseFloat(resultStr) : defaultVal;
		return result;
	}

	public static boolean isNum(String str) {
		if (str == null || "".equals(str.trim())) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]+");
		return pattern.matcher(str).matches();
	}

	public static boolean isDecimal(String str) {
		if (str == null || "".equals(str))
			return false;
		Pattern pattern = Pattern.compile("[0-9]*(\\.?)[0-9]*");
		return pattern.matcher(str).matches();
	}

	// 取得系统内存
	public static void getTotalMemory() {
		String str1 = "/proc/meminfo";
		String str2 = "";
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			while ((str2 = localBufferedReader.readLine()) != null) {
				SysLog.v(str2);
			}
			localBufferedReader.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// http://blog.csdn.net/shimiso/article/details/7743639
	public static void xml2Json() {

	}

	public static void json2Xml() {

	}

}
