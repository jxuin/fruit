package com.fruit.log;

import com.fruit.util.CommUtil;

import android.util.Log;

public class SysLog {

	public static void i(String msg) {

		Log.i(CommUtil.TAG, msg);
	}

	public static void e(String msg) {

		Log.e(CommUtil.TAG, msg);
	}

	public static void v(String msg) {

		Log.v(CommUtil.TAG, msg);
	}

}
