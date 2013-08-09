package com.fruit.aty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.fruit.adapter.CartListAdapter;
import com.fruit.bean.FruitDetail;
import com.fruit.fruitonline.R;
import com.fruit.json.JsonUrlParams;
import com.fruit.json.JsonUtil;
import com.fruit.log.SysLog;
import com.fruit.util.CommUtil;
import com.fruit.util.CommViewUtil;
import com.fruit.util.FruitUtil;
import com.fruit.adapter.CartListAdapter.CartChangeListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CartListAty extends Activity {

	Handler handler = new Handler();
	
	private Context _context;
	
	private EditText _searchView;
	
	private Button _delBtn;
	
	private Button _jsBtn;
	
	private TextView _totalMoneyView;
	
	private ListView _listView;
	
	private Dialog _delDialog;
	
	private CartListAdapter _cartListAdapter;
	
	private int _screenHeight;
	
	private ArrayList<FruitDetail> _fruitDetailList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		CommViewUtil.initWindows(this);
		
		setContentView(R.layout.cartlist);
		
		_context = getBaseContext();
		
		initParams();
		
		initView();
	}
	
	public void initParams() {
		
		_screenHeight = CommViewUtil.initScreenParams(CartListAty.this)[1];
		
		String cartJsonStr = FruitUtil.getCartShared().getString("jsonstr", "");
		_fruitDetailList = new ArrayList<FruitDetail>();
		Gson gson = new Gson();
		if (!"".equals(CommUtil.getStrVal(cartJsonStr))) {
			_fruitDetailList = gson.fromJson(cartJsonStr,new TypeToken<ArrayList<FruitDetail>>(){}.getType());
		}
	}
	
	public void initView() {
		
		_searchView = (EditText) findViewById(R.id.cartlist_search);
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
		
		_listView = (ListView) findViewById(R.id.cartlist);
		int height = _screenHeight - CommViewUtil.dip2px(_context, 60);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommViewUtil.MatchParent, height);
		_listView.setLayoutParams(params);
		
		View headerView = LayoutInflater.from(_context).inflate(R.layout.cartlist_header, null);
		_listView.addHeaderView(headerView);
		_cartListAdapter = new CartListAdapter(CartListAty.this, _fruitDetailList, new CartChangeLis());
		_listView.setAdapter(_cartListAdapter);
		
		final ArrayList<FruitDetail> tmpList = new ArrayList<FruitDetail>();
		tmpList.addAll(_fruitDetailList);
		
		_totalMoneyView = (TextView) headerView.findViewById(R.id.cartlist_header_totalmoney);
		_totalMoneyView.setText("共 ￥ " + FruitUtil.getCartShared().getFloat("totalmoney", 0f));
		
		_delBtn = (Button) headerView.findViewById(R.id.cartlist_header_delbtn);
		_delBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				if (tmpList.size() > 0) {
					_delDialog = CommViewUtil.setDelDialog(getResources().getString(R.string.del_confirm),CartListAty.this, new OnClickListener() {
						public void onClick(View view) {
							_fruitDetailList = new ArrayList<FruitDetail>();
							for(int i = 0; i < tmpList.size(); i++){
						         if (!_cartListAdapter.getCheckboxFlags()[i]) {
						        	 tmpList.get(i).getId();
						        	 _fruitDetailList.add(tmpList.get(i));
						         }
						     }
							FruitUtil.setCartInfo2Shared(_fruitDetailList);
							cartChangeView();
							_delDialog.dismiss();
						}
					});
					_delDialog.show();
				}
			}
		});
		
		_jsBtn = (Button) headerView.findViewById(R.id.cartlist_header_jsbtn);
		_jsBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (_fruitDetailList.size() > 0) {
					Intent intent = new Intent(_context, CartJsAty.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				} else {
					CommViewUtil.handlerToast(getResources().getString(R.string.dataerror3));
				}
			}
		});
		
		_listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position > 0) {
					FruitDetail fruitDetail = _fruitDetailList.get(position - 1);
					Intent intent = new Intent(_context, FruitDetailAty.class);
					Bundle bundle = new Bundle();
					bundle.putString("fruitid", fruitDetail.getId());
					intent.putExtra(CommUtil.DETAIL_BUNDLE, bundle);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}
		});
	}
	
	public void showCartJsDialog() {
		LayoutInflater inflater = LayoutInflater.from(_context);
		View v = inflater.inflate(R.layout.cart_js_dialog, null);
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.cart_js_layout);
		
		final SharedPreferences share = FruitUtil.getCartShared();
		
		final EditText shrView = (EditText) v.findViewById(R.id.cart_js_shr);
		shrView.setText(share.getString("shr", ""));
		
		final TextView dzView = (TextView) v.findViewById(R.id.cart_js_dz);
		dzView.setText(share.getString("dz", ""));
		
		final EditText dhhmView = (EditText) v.findViewById(R.id.cart_js_dhhm);
		dhhmView.setText(share.getString("dhhm", ""));
		
		final EditText shsjView = (EditText) v.findViewById(R.id.cart_js_shsj);
		
		Button confirmBtn = (Button) v.findViewById(R.id.cart_js_confirm);
		Button cancelBtn = (Button) v.findViewById(R.id.cart_js_cancel);
		
		final Dialog dialog = new Dialog(CartListAty.this, R.style.sys_dialog);
		
		dzView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
		
		confirmBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String shrStr = CommUtil.getStrVal(shrView.getText().toString());
				String dzStr = CommUtil.getStrVal(dzView.getText().toString());
				String dhhmStr = CommUtil.getStrVal(dhhmView.getText().toString());
				String shsjStr = CommUtil.getStrVal(shsjView.getText().toString());
				if ("".equals(shrStr)) {
					CommViewUtil.handlerToast(getResources().getString(R.string.cartjshint1));
				} else if ("".equals(dhhmStr)) {
					CommViewUtil.handlerToast(getResources().getString(R.string.cartjshint3));
				} else {
					final HashMap<String, String> params = new HashMap<String, String>();
					params.put("shr", shrStr);
					params.put("addr", dzStr);
					params.put("sjhm", dhhmStr);
					params.put("shsj", shsjStr);
					final Gson gson = new Gson();
					List<FruitDetail> cartList = new ArrayList<FruitDetail>();
					String jsonStr = FruitUtil.getCartShared().getString("jsonstr", "");
					if (!"".equals(jsonStr)) {
						cartList = gson.fromJson(jsonStr,new TypeToken<List<FruitDetail>>(){}.getType());
					}
					Hashtable<String, List<FruitDetail>> ht = new Hashtable<String, List<FruitDetail>>();
					ht.put("root", cartList);
					params.put("fruits", JsonUtil.getJsonStr(ht));
					new Thread(new Runnable() {
						public void run() {
							String resultStr = JsonUtil.getJsonData(JsonUrlParams.orderUrlPrefix, params, JsonUrlParams.JSON_POST);
							if (!"".equals(CommUtil.getStrVal(resultStr))) {
								Hashtable<String, String> resultHt = gson.fromJson(resultStr,new TypeToken<Hashtable<String, String>>(){}.getType());
								if ("0".equals(resultHt.get("status"))) {
									SysLog.v("sucess");
								}
							}
							CommViewUtil.handlerToast(getResources().getString(R.string.submit) + getResources().getString(R.string.success));
						}
					}).start();
					SharedPreferences.Editor editor = share.edit();
					editor.putString("shr", shrStr);
					editor.putString("dz", dzStr);
					editor.putString("dhhm", dhhmStr);
					editor.commit();
					FruitUtil.clearCartJsonStr();
					cartChangeView();
					dialog.dismiss();
				}
			}
		});
		
		cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		dialog.setCanceledOnTouchOutside(true);
		int dialogWidth = CommViewUtil.initScreenParams(CartListAty.this)[0] - CommViewUtil.dip2px(_context, 40);
		dialog.setContentView(layout, new LinearLayout.LayoutParams(dialogWidth, CommViewUtil.MatchParent));// 设置布局
		
		dialog.show();
	}
	
	class CartChangeLis implements CartChangeListener {
		public void cartChange() {
			cartChangeView();
		}
	}
	
	public void cartChangeView() {
		Gson gson = new Gson();
		List<FruitDetail> cartList = new ArrayList<FruitDetail>();
		String jsonStr = FruitUtil.getCartShared().getString("jsonstr", "");
		if (!"".equals(jsonStr)) {
			cartList = gson.fromJson(jsonStr,new TypeToken<List<FruitDetail>>(){}.getType());
		}
		_cartListAdapter.setNewList(cartList);
		_cartListAdapter.notifyDataSetChanged();
		_totalMoneyView.setText("共 ￥ " + FruitUtil.getCartShared().getFloat("totalmoney", 0f));
	}
	
	protected void onResume() {
		super.onResume();
		cartChangeView();
	}
}
