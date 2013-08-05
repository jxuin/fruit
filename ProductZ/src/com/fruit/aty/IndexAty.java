package com.fruit.aty;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fruit.adapter.FruitListGridAdapter;
import com.fruit.bean.FruitDetail;
import com.fruit.bean.FruitList;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class IndexAty extends Activity {
	
	private Handler handler = new Handler();
	
	private Context _context;
	
	private int _screenHeight;
	
	private Dialog _loadingDialog;
	
	private EditText _searchView;
	
	private GridView _gridView;
	
	private TextView _cartnumView;
	
	private ImageView _cartImgView;
	
	private FruitListGridAdapter _fruitListGridAdapter;
	
	private FruitList _fruitList;
	
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		CommViewUtil.initWindows(this);
		
		setContentView(R.layout.index);
		
		_context = getBaseContext();
		
		showLoadingDialog("");
		
		initParams();
		
		initView();
	}
	
	final static int MESSAGE_1 = 1;
	final static int MESSAGE_2 = 2;
	
	Handler mHandler;
	
	public void initParams() {
		int[] screenParams = CommViewUtil.initScreenParams(IndexAty.this);
		_screenHeight = CommUtil.getIntVal(screenParams[1], 1000);
		
	}
	
	public void initView() {
		
		_gridView = (GridView) findViewById(R.id.index_grid);
		int height = _screenHeight - CommViewUtil.dip2px(_context, 60);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommViewUtil.MatchParent, height);
		_gridView.setLayoutParams(params);
		
		_searchView = (EditText) findViewById(R.id.index_search);
		
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
		
		_cartnumView = (TextView) findViewById(R.id.index_cartnum);
		
		_cartImgView = (ImageView) findViewById(R.id.index_cart);
		
		_cartImgView.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(_context, CartListAty.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		
		executorService.execute(new FruitListRunnable());
		
	}
	
	class FruitListRunnable implements Runnable {
		public void run() {
			HashMap<String, String> jsonParams = new HashMap<String, String>();
			_fruitList = JsonUtil.getFruitListJsonData(JsonUrlParams.fruitlistUrlPrefix, jsonParams, JsonUrlParams.JSON_GET);
			if (_fruitList != null && _fruitList.getList().size() > 0) {
				handler.post(new Runnable() {
					public void run() {
						_fruitListGridAdapter = new FruitListGridAdapter(_context, _fruitList.getList());
						_gridView.setAdapter(_fruitListGridAdapter);
						
						_gridView.setOnItemClickListener(new OnItemClickListener() {
							public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
								FruitDetail fruitDetail = _fruitList.getList().get(position);
								fruitDetail.getId();
								Intent intent = new Intent(_context, FruitListAty.class);
								Bundle bundle = new Bundle();
								bundle.putString("fruitid", fruitDetail.getId());
								intent.putExtra(CommUtil.LIST_BUNDLE, bundle);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}
						});
						dismissLoadingDialog();
					}
				});
			} else {
				dismissLoadingDialog();
				CommViewUtil.netFalseToast();
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
			_loadingDialog = CommViewUtil.getLoadingDialog(IndexAty.this, contentStr);
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
	
	private long exitTime = 0;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis() - exitTime) > 2000){  
	            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
	            exitTime = System.currentTimeMillis();   
	        } else {
	        	finish();
	            System.exit(0);
	        }
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	protected void onResume() {
		super.onResume();
		cartChangeView();
	}

}
