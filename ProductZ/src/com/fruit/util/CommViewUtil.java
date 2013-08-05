package com.fruit.util;

import java.io.File;

import com.fruit.app.SysApplication;
import com.fruit.fruitonline.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class CommViewUtil {
	
	private final static Handler handler = new Handler();
	
	public static int MatchParent = ViewGroup.LayoutParams.MATCH_PARENT;
	
	public static int WrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;

	/**
	 * 初始化窗口
	 * 
	 * @param activity
	 */
	public static void initWindows(Activity activity) {

		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题懒
		activity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);// 隐藏软键盘
	}

	/**
	 * 预估得到view的宽高
	 * 
	 * @param child
	 */
	public static void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	public static void webviewSetting(WebView webview) {
		WebSettings.LayoutAlgorithm localLayoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS;// 设置　NARROW_COLUMNS　页面的展示效果最好
		webview.getSettings().setLayoutAlgorithm(localLayoutAlgorithm);
		webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webview.getSettings().setAppCacheEnabled(true);
		webview.getSettings().setAppCacheMaxSize(1000 * 1024 * 1024);
//		webview.getSettings().setAppCachePath(FileUtil.sdCardPathWebViewCacheStr);
		webview.getSettings().setLightTouchEnabled(true);
		webview.getSettings().setUseWideViewPort(true);// 推荐使用宽视口的WebView
		webview.getSettings().setPluginState(PluginState.ON);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.getSettings().setSupportZoom(true);
		webview.getSettings().setLoadWithOverviewMode(true);// 设置的WebView是否加载一个页面概览模式。 3.x 以上
		webview.setScrollBarStyle(0);
	}
	
	//apk安装
	public static Intent installAPK(File file) {
		Uri uri = Uri.fromFile(file);
		Intent installIntent = new Intent(Intent.ACTION_VIEW);
		installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		installIntent.setDataAndType(uri,"application/vnd.android.package-archive");
		return installIntent;
	}

	/** * 根据手机的分辨率从 dp 的单位 转成为 px(像素) */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/** * 根据手机的分辨率从 px(像素) 的单位 转成为 dp */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	public static void brightNessChange(Activity aty) {
		if (CommViewUtil.isAutoBrightness(aty.getContentResolver())) {
			CommViewUtil.stopAutoBrightness(aty);
		}
		int brightNessInt = CommViewUtil.getScreenBrightness(aty);
		if (brightNessInt < 80) {
			brightNessInt = brightNessInt + 10;
		} else {
			brightNessInt = 30;
		}
		CommViewUtil.setBrightness(aty, brightNessInt);
		CommViewUtil.saveBrightness(aty.getContentResolver(), brightNessInt);
	}
	
	
	/**
	 * 判断是否开启了自动亮度调节
	 */
	public static boolean isAutoBrightness(ContentResolver aContentResolver) {
	    boolean automicBrightness = false;
	    try {
	        automicBrightness = Settings.System.getInt(aContentResolver,
	                Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
	    } catch (SettingNotFoundException e) {
	        e.printStackTrace();
	    }
	    return automicBrightness;
	}
	/**
	 * 获取屏幕的亮度
	 */
	public static int getScreenBrightness(Activity activity) {
	    int nowBrightnessValue = 0;
	    ContentResolver resolver = activity.getContentResolver();
	    try {
	        nowBrightnessValue = android.provider.Settings.System.getInt(
	                resolver, Settings.System.SCREEN_BRIGHTNESS);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return nowBrightnessValue;
	}
	/**
	 * 设置亮度
	 */
	public static void setBrightness(Activity activity, int brightness) {
	    WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
	    lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
	    activity.getWindow().setAttributes(lp);
	 
	}
	/**
	 * 停止自动亮度调节
	 */
	public static void stopAutoBrightness(Activity activity) {
	    Settings.System.putInt(activity.getContentResolver(),
	            Settings.System.SCREEN_BRIGHTNESS_MODE,
	            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	}
	/**
	 * 保存亮度设置状态
	 */
	public static void saveBrightness(ContentResolver resolver, int brightness) {
	    Uri uri = android.provider.Settings.System
	            .getUriFor("screen_brightness");
	    android.provider.Settings.System.putInt(resolver, "screen_brightness",
	            brightness);
	    // resolver.registerContentObserver(uri, true, myContentObserver);
	    resolver.notifyChange(uri, null);
	}
	
	public static Dialog setDelDialog(String contextStr, Activity aty, OnClickListener listener) {
		
		LayoutInflater inflater = LayoutInflater.from(aty);
		View v = inflater.inflate(R.layout.del_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.deldialog_layout);// 加载布局
		
		final Dialog dialog = new Dialog(aty, R.style.sys_dialog);// 创建自定义样式dialog
		
		TextView contentView = (TextView) v.findViewById(R.id.deldialog_content);
		
		contentView.setText(contextStr);
		
		Button confirmBtn = (Button) v.findViewById(R.id.deldialog_confirm);
		Button cancelBtn = (Button) v.findViewById(R.id.deldialog_cancel);
		
		confirmBtn.setOnClickListener(listener);
		
		cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setCanceledOnTouchOutside(true);
		int dialogWidth = initScreenParams(aty)[0] - dip2px(aty, 40);
		dialog.setContentView(layout, new LinearLayout.LayoutParams(dialogWidth, CommViewUtil.MatchParent));// 设置布局
		
		return dialog;
	}
	
	//网络判断Toast
	public static void netFalseToast() {
		handlerToast(SysApplication._context.getResources().getString(R.string.neterror));
	}
	
	public static void handlerToast(final String str) {
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(SysApplication._context, str, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	public static int[] initScreenParams(Activity aty) {
		Display display = aty.getWindowManager().getDefaultDisplay();
		int[] screenParams = new int[2];
		if(Build.VERSION.SDK_INT >= 13){
			Point size = new Point();
			display.getSize(size); 
			screenParams[0] = CommUtil.getIntVal(size.x, 1000);
			screenParams[1] = CommUtil.getIntVal(size.y, 1000);
		} else {
			DisplayMetrics metric=new DisplayMetrics();
			display.getMetrics(metric);
			screenParams[0] = CommUtil.getIntVal(display.getWidth(), 1000);
			screenParams[1] = CommUtil.getIntVal(display.getHeight(), 1000);
		}
		return screenParams;
	}
	
	public static Dialog getLoadingDialog(Context context, String msg) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.loadingdialog_layout);// 加载布局
		// main.xml中的ImageView
		ImageView imgView = (ImageView) v.findViewById(R.id.loadingdialog_img);
		TextView textView = (TextView) v.findViewById(R.id.loadingdialog_tip);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		// 使用ImageView显示动画
		imgView.startAnimation(hyperspaceJumpAnimation);
		textView.setText(msg);// 设置加载信息 

		Dialog loadingDialog = new Dialog(context, R.style.sys_dialog);// 创建自定义样式dialog

//		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setCanceledOnTouchOutside(false);
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(CommViewUtil.MatchParent, CommViewUtil.MatchParent));// 设置布局
		
		return loadingDialog;
	}
	
	public static void clearWebViewCache(Context context) {
		context.deleteDatabase("webview.db");
		context.deleteDatabase("webviewCache.db");
		File file = new File(context.getCacheDir().getPath() + "/webviewCache/");
		if (file.isDirectory()) {
			for (File tmpFile : file.listFiles()) {
				tmpFile.delete();
			}
		}
		file =  new File(context.getCacheDir().getPath() + "/webviewCacheChromium/");
		if (file.isDirectory()) {
			for (File tmpFile : file.listFiles()) {
				tmpFile.delete();
			}
		}
	}
}
