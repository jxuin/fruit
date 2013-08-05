package com.fruit.app;

import android.app.Application;
import android.content.Context;

import com.fruit.db.DataHelper;

public class SysApplication extends Application {
	
	private static SysApplication singleton;

	// 初始化下载线程池
	public static Context _context;
	
	public static SysApplication getInstance() {
		return singleton;
	}
	
	@Override
	public final void onCreate() {

		_context = getApplicationContext();

		DataHelper.getDataHelper(_context);

	}

	// 回收
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

}
