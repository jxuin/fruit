package com.fruit.aty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fruit.adapter.SearchAdapter;
import com.fruit.bean.FruitDetail;
import com.fruit.bean.FruitList;
import com.fruit.fruitonline.R;
import com.fruit.json.JsonUrlParams;
import com.fruit.json.JsonUtil;
import com.fruit.util.CommUtil;
import com.fruit.util.CommViewUtil;
import com.fruit.util.FruitUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

public class FruitSearchAty extends Activity {

	private Handler handler = new Handler();

	private Context _context;

	private EditText _searchView;
	
	private ImageView _delView;
	
	private ImageButton _searchBtn;
	
	private ListView _listView;
	
	private TextView _cartnumView;
	
	private ImageView _cartImgView;

	private int _screenWidth;
	
	private FruitList _searchResultList;
	
	private SearchAdapter _searchAdapter;

//	private Dialog _loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		CommViewUtil.initWindows(this);

		setContentView(R.layout.search);

		_context = getBaseContext();

		initParams();

		initView();
	}

	public void initParams() {
		int[] screenParams = CommViewUtil.initScreenParams(FruitSearchAty.this);
		_screenWidth = CommUtil.getIntVal(screenParams[0], 1000);
	}

	public void initView() {
		
		_searchView = (EditText) findViewById(R.id.search_searchtext);
		int searchViewWidth = _screenWidth - CommViewUtil.dip2px(_context, 125);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(searchViewWidth, CommViewUtil.dip2px(_context, 35));
		params.setMargins(CommViewUtil.dip2px(_context, 10), 0, 0, 0);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		_searchView.setLayoutParams(params);
		
		_searchView.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				return false;
			}
		});
		_searchView.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				final String keyWord = _searchView.getText().toString();
				if (!"".equals(CommUtil.getStrVal(keyWord))) {
					new Thread(new Runnable() {
						public void run() {
							HashMap<String, String> jsonParams = new HashMap<String, String>();
							jsonParams.put("key", keyWord);
							_searchResultList = JsonUtil.getFruitListJsonData(JsonUrlParams.searchUrlPrefix, jsonParams, JsonUrlParams.JSON_POST);
							if (_searchResultList != null) {
								handler.post(new Runnable() {
									public void run() {
										List<FruitDetail> tmpList = _searchResultList.getList();
										if (tmpList != null && tmpList.size() > 0) {
											if (tmpList.size() > 10) {
												tmpList = tmpList.subList(0, 10);
											}
											_searchAdapter.setNewList(tmpList);
											_searchAdapter.notifyDataSetChanged();
										}
									}
								});
							}
						}
					}).start();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});

		handler.postDelayed(new Runnable() {
			public void run() {
				InputMethodManager imm = (InputMethodManager) _context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 100);
		
		_delView = (ImageView) findViewById(R.id.search_del);
		
		_delView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				_searchView.setText("");
				_searchAdapter.setNewList(new ArrayList<FruitDetail>());
				_searchAdapter.notifyDataSetChanged();
			}
		});
		
		_searchBtn = (ImageButton) findViewById(R.id.search_btn);
		
		_searchBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (_searchResultList != null) {
					Intent intent = new Intent(_context, FruitSearchResultAty.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("fruitlist", _searchResultList);
					intent.putExtra(CommUtil.SEARCHRESULT_BUNDLE, bundle);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}
		});
		
		_listView = (ListView) findViewById(R.id.search_list);
		
		_cartnumView = (TextView) findViewById(R.id.search_cartnum);
		
		_cartImgView = (ImageView) findViewById(R.id.search_cart);
		
		_cartImgView.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(_context, CartListAty.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		
		_searchAdapter = new SearchAdapter(_context, new ArrayList<FruitDetail>());
		
		_listView.setAdapter(_searchAdapter);
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
	
	protected void onResume() {
		super.onResume();
		cartChangeView();
	}

}
