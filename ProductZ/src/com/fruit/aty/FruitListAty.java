package com.fruit.aty;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fruit.adapter.FruitListAdapter;
import com.fruit.adapter.FruitListAdapter.CartChangeListener;
import com.fruit.bean.FruitList;
import com.fruit.fragment.FruitListFragment;
import com.fruit.fragment.HeaderFragment;
import com.fruit.fruitonline.R;
import com.fruit.json.JsonUrlParams;
import com.fruit.json.JsonUtil;
import com.fruit.util.CommUtil;
import com.fruit.util.CommViewUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.LinearLayout;

public class FruitListAty extends FragmentActivity {
	
	private Handler handler = new Handler();
	
	private Context _context;
	
	private int _screenHeight;
	
	private Dialog _loadingDialog;
	
	private FruitList _fruitList;
	
	private FruitListAdapter _fruitListAdapter;
	
	private String _pfruitid;
	
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	
	protected void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		CommViewUtil.initWindows(this);
		
		setContentView(R.layout.list);
		
		_context = getBaseContext();
		
		showLoadingDialog("");
		
		initParams();
		
		initView();
	}
	
	public void initParams() {
		int[] screenParams = CommViewUtil.initScreenParams(FruitListAty.this);
		_screenHeight = CommUtil.getIntVal(screenParams[1], 1000);
		Bundle bundle = getIntent().getBundleExtra(CommUtil.LIST_BUNDLE);
		_pfruitid = bundle.getString("fruitid");
	}
	
	public void initView() {
		executorService.execute(new FruitListRunnable());
	}
	
	class FruitListRunnable implements Runnable {
		public void run() {
			HashMap<String, String> jsonParams = new HashMap<String, String>();
			jsonParams.put("id", _pfruitid);
			_fruitList = JsonUtil.getFruitListJsonData(JsonUrlParams.fruitlistUrlPrefix, jsonParams, JsonUrlParams.JSON_GET);
			if (_fruitList != null && _fruitList.getList().size() > 0) {
				handler.post(new Runnable() {
					public void run() {
						_fruitListAdapter = new FruitListAdapter(_context, _fruitList.getList(), new CartChangeLis());
						FruitListFragment flf = (FruitListFragment) getSupportFragmentManager().findFragmentById(R.id.list_fragment);
						int height = _screenHeight - CommViewUtil.dip2px(_context, 60);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommViewUtil.MatchParent, height);
						flf.getView().setLayoutParams(params);
						flf.setListAdapter(_fruitListAdapter);
						flf.setFruitList(_fruitList);
						dismissLoadingDialog();
					}
				});
			} else {
				dismissLoadingDialog();
				CommViewUtil.netFalseToast();
			}
		}
	}
	
	class CartChangeLis implements CartChangeListener {
		public void cartChange() {
			HeaderFragment hf = (HeaderFragment) getSupportFragmentManager().findFragmentById(R.id.header_fragment);
			hf.cartChangeView();
		}
	}
	
	public synchronized void showLoadingDialog(String contentStr) {
		if ("".equals(CommUtil.getStrVal(contentStr))) {
			contentStr = getResources().getString(R.string.loadingdialog_str1);
		}
		if (_loadingDialog == null) {
			_loadingDialog = CommViewUtil.getLoadingDialog(FruitListAty.this, contentStr);
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

}
