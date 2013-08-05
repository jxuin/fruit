package com.fruit.aty;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fruit.adapter.FruitListAdapter;
import com.fruit.adapter.FruitListAdapter.CartChangeListener;
import com.fruit.bean.FruitDetail;
import com.fruit.bean.FruitList;
import com.fruit.fruitonline.R;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FruitSearchResultAty extends Activity {
	
	private Handler handler = new Handler();
	
	private Context _context;
	
	private int _screenHeight;
	
	private Dialog _loadingDialog;
	
	private EditText _searchView;
	
	private ListView _listView;
	
	private TextView _cartnumView;
	
	private ImageView _cartImgView;
	
	private FruitList _fruitList;
	
	private FruitListAdapter _fruitListAdapter;
	
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	
	protected void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		CommViewUtil.initWindows(this);
		
		setContentView(R.layout.list);
		
		_context = getBaseContext();
		
		initParams();
		
		initView();
	}
	
	public void initParams() {
		int[] screenParams = CommViewUtil.initScreenParams(FruitSearchResultAty.this);
		_screenHeight = CommUtil.getIntVal(screenParams[1], 1000);
		Bundle bundle = getIntent().getBundleExtra(CommUtil.SEARCHRESULT_BUNDLE);
		_fruitList = (FruitList) bundle.getSerializable("fruitlist");
	}
	
	public void initView() {
		
		_listView = (ListView) findViewById(R.id.list);
		int height = _screenHeight - CommViewUtil.dip2px(_context, 60);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommViewUtil.MatchParent, height);
		_listView.setLayoutParams(params);
		
		_searchView = (EditText) findViewById(R.id.list_search);
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
		
		_cartnumView = (TextView) findViewById(R.id.list_cartnum);
		
		_cartImgView = (ImageView) findViewById(R.id.list_cart);
		
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
			if (_fruitList != null && _fruitList.getList().size() > 0) {
				handler.post(new Runnable() {
					public void run() {
						_fruitListAdapter = new FruitListAdapter(_context, _fruitList.getList(), new CartChangeLis());
						_listView.setAdapter(_fruitListAdapter);
						_listView.setOnItemClickListener(new OnItemClickListener() {
							public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
								FruitDetail fruitDetail = _fruitList.getList().get(position);
								Intent intent = new Intent(_context, FruitDetailAty.class);
								Bundle bundle = new Bundle();
								bundle.putString("fruitid", fruitDetail.getId());
								intent.putExtra(CommUtil.DETAIL_BUNDLE, bundle);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}
						});
					}
				});
			} else {
				CommViewUtil.handlerToast(getResources().getString(R.string.dataerror2));
			}
		}
	}
	
	class CartChangeLis implements CartChangeListener {
		public void cartChange() {
			cartChangeView();
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
			_loadingDialog = CommViewUtil.getLoadingDialog(FruitSearchResultAty.this, contentStr);
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
