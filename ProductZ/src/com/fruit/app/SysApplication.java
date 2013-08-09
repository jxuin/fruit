package com.fruit.app;

import android.app.Application;
import android.content.Context;

import com.fruit.db.DataHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

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

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				_context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		ImageLoader.getInstance().init(config);

	}

	// 回收
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

}
