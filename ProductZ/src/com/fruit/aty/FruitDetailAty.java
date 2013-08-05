package com.fruit.aty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fruit.adapter.FruitImgAdapter;
import com.fruit.app.SysApplication;
import com.fruit.bean.FruitDetail;
import com.fruit.bean.FruitImg;
import com.fruit.custom.GalleryFlow;
import com.fruit.fruitonline.R;
import com.fruit.json.JsonUrlParams;
import com.fruit.json.JsonUtil;
import com.fruit.util.CommUtil;
import com.fruit.util.CommViewUtil;
import com.fruit.util.FruitUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FruitDetailAty extends Activity {
	
	private Handler handler = new Handler();
	
	private Context _context;
	
	private Dialog _loadingDialog;
	
	private EditText _searchView;
	
	private GalleryFlow _galleryFlow;
	
	private TextView _nameView;
	
	private TextView _detailView;
	
	private TextView _priceView;
	
	private TextView _cartnumView;
	
	private ImageView _cartImgView;
	
	private Button _add2cartBtn;
	
	private RelativeLayout _webviewLayout;
	
	private String _fruitid;
	
	private FruitDetail _fruitDetail;
	
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	
	protected void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		CommViewUtil.initWindows(this);
		
		setContentView(R.layout.detail);
		
		_context = getBaseContext();
		
		initParams();
		
		initView();
		
	}
	
	public void initParams() {
		Bundle bundle = getIntent().getBundleExtra(CommUtil.DETAIL_BUNDLE);
		_fruitid = bundle.getString("fruitid");
	}
	
	public void initView() {
		_searchView = (EditText) findViewById(R.id.detail_search);
		_searchView.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				return false;
			}
		});
		_searchView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(_context, FruitSearchAty.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		
		_nameView = (TextView) findViewById(R.id.detail_name);
		_detailView = (TextView) findViewById(R.id.detail_remark);
		_priceView = (TextView) findViewById(R.id.detail_price);
		
		_cartnumView = (TextView) findViewById(R.id.detail_cartnum);
		
		_cartImgView = (ImageView) findViewById(R.id.detail_cart);
		
		_cartImgView.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(_context, CartListAty.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		
		_galleryFlow = (GalleryFlow) findViewById(R.id.detail_gallery);
		
		_add2cartBtn = (Button) findViewById(R.id.detail_add2cart);
		
		_add2cartBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FruitUtil.addFruit2Cart(_fruitDetail, 1, true);
				cartChangeView();
				CommViewUtil.handlerToast(SysApplication._context.getResources().getString(R.string.addsuccess));
			}
		});
		
		_webviewLayout = (RelativeLayout) findViewById(R.id.detail_webview_layout);
		
		executorService.execute(new FruitDetailRunnable());
	}
	
	class FruitDetailRunnable implements Runnable {
		public void run() {
			HashMap<String, String> jsonParams = new HashMap<String, String>();
			jsonParams.put("id", _fruitid);
			_fruitDetail =  JsonUtil.getFruitDetailJsonData(JsonUrlParams.fruitDetailUrlPrefix, jsonParams, JsonUrlParams.JSON_GET);
			if (_fruitDetail != null) {
				handler.post(new Runnable() {
					public void run() {
//						String nameTextStr = _fruitDetail.getName() + "<font color=\"#ff0000\"><small>" + _fruitDetail.getRemark() + "</small></font>";
						_nameView.setText(_fruitDetail.getName());
						_detailView.setText(_fruitDetail.getRemark());
						_priceView.setText(getResources().getString(R.string.detail_str5) + "￥ " + _fruitDetail.getPrice() + "/" + _fruitDetail.getSpdw());
						ArrayList<FruitImg> fruitImgList = _fruitDetail.getImglist();
						if (fruitImgList != null && fruitImgList.size() > 0) {
							_galleryFlow.setAdapter(new FruitImgAdapter(_context, fruitImgList));
						}
						WebView webView = new WebView(_context);
						_webviewLayout.addView(webView);
						webView.setBackgroundColor(0);
						CommViewUtil.webviewSetting(webView);
						webView.getSettings().setBuiltInZoomControls(false);
						webView.loadUrl(CommUtil.getStrVal(_fruitDetail.getSpxx()));
					}
				});
			}
		}
	}
	
	public void cartChangeView() {
		int cartNum = FruitUtil.getCartShared().getInt("cartnum", 0);
		if (cartNum > 0) {
			_cartnumView.setVisibility(View.VISIBLE);
			_cartnumView.setText(String.valueOf(cartNum));
		} else {
			_cartnumView.setVisibility(View.GONE);
		}
	}
	
	public synchronized void showLoadingDialog(String contentStr) {
		if ("".equals(CommUtil.getStrVal(contentStr))) {
			contentStr = getResources().getString(R.string.loadingdialog_str1);
		}
		if (_loadingDialog == null) {
			_loadingDialog = CommViewUtil.getLoadingDialog(FruitDetailAty.this, contentStr);
			_loadingDialog.show();
		} else if (!_loadingDialog.isShowing()) {
			_loadingDialog.show();
		}
	}
	
	public synchronized boolean dismissLoadingDialog() {
		boolean flag = false;
		if (_loadingDialog != null) {
			flag = true;
			_loadingDialog.dismiss();
			_loadingDialog = null;
		}
		return flag;
	}
	
	protected void onResume() {
		super.onResume();
		cartChangeView();
	}

}
