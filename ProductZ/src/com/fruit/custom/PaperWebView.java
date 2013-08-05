package com.fruit.custom;

import android.content.Context;
import android.view.MotionEvent;
import android.webkit.WebView;

public class PaperWebView extends WebView {

	private int pagewidth;
	private int screenWidth;
	private float lastTouchX, lastTouchY;
	private boolean hasMoved = false;
	private float downXValue;
	private long downTime;
	private OnWebviewActionListener _listener;

	public PaperWebView(Context context) {
		super(context);
	}

	public int getPagewidth() {
		return pagewidth;
	}

	public void setPagewidth(int pagewidth) {
		this.pagewidth = pagewidth;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public float getMRightFadingEdgeStrength() {
		return this.getRightFadingEdgeStrength();
	}

	@Override
	public void goBack() {
		super.goBack();
	}

	@Override
	public boolean onTouchEvent(MotionEvent evt) {

		boolean consumed = super.onTouchEvent(evt);
		if (isClickable()) {
			switch (evt.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				lastTouchX = evt.getX();
				lastTouchY = evt.getY();
				downXValue = evt.getX();
				downTime = evt.getEventTime();
				hasMoved = false;
				break;
			case MotionEvent.ACTION_MOVE:
				hasMoved = moved(evt);
				break;
			case MotionEvent.ACTION_UP:
				float currentX = evt.getX();
				long currentTime = evt.getEventTime();
				float difference = Math.abs(downXValue - currentX);
				long time = currentTime - downTime;
				if ((downXValue < currentX) && (difference > screenWidth / 2) && this.getScrollX() <= 1 && time < 1000) {
					preWebview();
				}
				if ((downXValue > currentX) && (difference > screenWidth / 2) && (time < 1000)) {// 向下翻页
					nextWebview();
				}
				break;

			case MotionEvent.ACTION_POINTER_UP:
				break;
			// 设置多点触摸模式
			case MotionEvent.ACTION_POINTER_DOWN:
				break;
			// 若为DRAG模式，则点击移动图片
			}
		}
		return consumed || isClickable();
	}

	@Override
	public synchronized void loadUrl(String url) {
		super.loadUrl(url);
	}

	private boolean moved(MotionEvent evt) {
		return hasMoved || Math.abs(evt.getX() - lastTouchX) > 10.0 || Math.abs(evt.getY() - lastTouchY) > 10.0;
	}
	
	public interface OnWebviewActionListener {
		public void preWebview();
		public void nextWebview();
	}
	
	public void setOnWebviewActionListener(OnWebviewActionListener listener) {
		_listener = listener;
	}
	
	private void preWebview() {
		if (_listener != null) {
			_listener.preWebview();
		}
	}
	
	private void nextWebview() {
		if (_listener != null) {
			_listener.nextWebview();
		}
	}
}
