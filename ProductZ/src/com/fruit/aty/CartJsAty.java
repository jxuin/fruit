package com.fruit.aty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.fruit.adapter.ShAddrAdapter;
import com.fruit.bean.FruitDetail;
import com.fruit.db.ShAddrDataDao;
import com.fruit.db.bean.ShAddrData;
import com.fruit.fruitonline.R;
import com.fruit.json.JsonUrlParams;
import com.fruit.json.JsonUtil;
import com.fruit.log.SysLog;
import com.fruit.util.CommUtil;
import com.fruit.util.CommViewUtil;
import com.fruit.util.FruitUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class CartJsAty extends Activity {

	Handler handler = new Handler();

	private Context _context;
	
	private EditText _shrView;
	
	private TextView _dzView;
	
	private EditText _dhhmView;
	
	private EditText _shsjView;
	
	private List<List<ShAddrData>> _shaddrlistList;
	private List<Spinner> _spinnerList;
	private List<ShAddrAdapter> _adapterList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		CommViewUtil.initWindows(this);
		
		setContentView(R.layout.cart_js);
		
		_context = getBaseContext();
		
		initParams();
		
		initView();
	}
	
	public void initParams() {
		_shaddrlistList = new ArrayList<List<ShAddrData>>();
		_spinnerList = new ArrayList<Spinner>();
		_adapterList = new ArrayList<ShAddrAdapter>();
	}
	
	public void initView() {
		final SharedPreferences share = FruitUtil.getCartShared();
		
		_shrView = (EditText) findViewById(R.id.cart_js_shr);
		_shrView.setText(share.getString("shr", ""));
		
		_dzView = (TextView) findViewById(R.id.cart_js_dz);
		_dzView.setText(share.getString("dz", ""));
		
		_dhhmView = (EditText) findViewById(R.id.cart_js_dhhm);
		_dhhmView.setText(share.getString("dhhm", ""));
		
		_shsjView = (EditText) findViewById(R.id.cart_js_shsj);
		
		Button confirmBtn = (Button) findViewById(R.id.cart_js_confirm);
		
		_dzView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showShAddrDialog();
			}
		});
		
		confirmBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String shrStr = CommUtil.getStrVal(_shrView.getText().toString());
				String dzStr = CommUtil.getStrVal(_dzView.getText().toString());
				String dhhmStr = CommUtil.getStrVal(_dhhmView.getText().toString());
				String shsjStr = CommUtil.getStrVal(_shsjView.getText().toString());
				if ("".equals(shrStr)) {
					CommViewUtil.handlerToast(getResources().getString(R.string.cartjshint1));
				} else if ("".equals(dzStr)) {
					CommViewUtil.handlerToast(getResources().getString(R.string.cartjshint2));
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
							finish();
						}
					}).start();
					SharedPreferences.Editor editor = share.edit();
					editor.putString("shr", shrStr);
					editor.putString("dz", dzStr);
					editor.putString("dhhm", dhhmStr);
					editor.commit();
					FruitUtil.clearCartJsonStr();
				}
			}
		});
	}
	
	public List<ShAddrData> getShAddrList(String paddrid) {
		List<ShAddrData> list = new ArrayList<ShAddrData>();
		try {
			Dao<ShAddrData, Integer> dao = ShAddrDataDao.getDao();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(ShAddrData.PADDRID, paddrid);
			list = dao.queryForFieldValues(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void spinnerChange(int num, String paddrid) {
		List<ShAddrData> list = _shaddrlistList.get(num);
		ShAddrAdapter adapter = _adapterList.get(num);
		list.clear();
		list.addAll(getShAddrList(paddrid));
		if (list != null && list.size() > 0) {
			adapter.setNewList(list);
			adapter.notifyDataSetChanged();
			num ++;
			if (num < _spinnerList.size()) {
				spinnerChange(num, list.get(0).getAddrid());
			}
		}
	}
	
	
	public void showShAddrDialog() {
		LayoutInflater inflater = LayoutInflater.from(_context);
		View v = inflater.inflate(R.layout.shaddr_dialog, null);
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.shaddr_layout);
		
		Button confirmBtn = (Button) v.findViewById(R.id.shaddr_confirm);
		Button cancelBtn = (Button) v.findViewById(R.id.shaddr_cancel);
		
		Spinner spinner1 = (Spinner) v.findViewById(R.id.shaddr_spinner1);
		Spinner spinner2 = (Spinner) v.findViewById(R.id.shaddr_spinner2);
		Spinner spinner3 = (Spinner) v.findViewById(R.id.shaddr_spinner3);
		Spinner spinner4 = (Spinner) v.findViewById(R.id.shaddr_spinner4);
		
		_spinnerList.clear();
		_spinnerList.add(spinner1);
		_spinnerList.add(spinner2);
		_spinnerList.add(spinner3);
		_spinnerList.add(spinner4);
		
		List<ShAddrData> list1 = new ArrayList<ShAddrData>();
		List<ShAddrData> list2 = new ArrayList<ShAddrData>();
		List<ShAddrData> list3 = new ArrayList<ShAddrData>();
		List<ShAddrData> list4 = new ArrayList<ShAddrData>();
		
		_shaddrlistList.clear();
		_shaddrlistList.add(list1);
		_shaddrlistList.add(list2);
		_shaddrlistList.add(list3);
		_shaddrlistList.add(list4);
		
		ShAddrAdapter adapter1 = new ShAddrAdapter(_context, list1);
		ShAddrAdapter adapter2 = new ShAddrAdapter(_context, list2);
		ShAddrAdapter adapter3 = new ShAddrAdapter(_context, list3);
		ShAddrAdapter adapter4 = new ShAddrAdapter(_context, list4);
		
		_adapterList.clear();
		_adapterList.add(adapter1);
		_adapterList.add(adapter2);
		_adapterList.add(adapter3);
		_adapterList.add(adapter4);
		
		spinner1.setAdapter(adapter1);
		spinner2.setAdapter(adapter2);
		spinner3.setAdapter(adapter3);
		spinner4.setAdapter(adapter4);
		
		spinnerChange(0, "0");
		
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				spinnerChange(1, _shaddrlistList.get(0).get(position).getAddrid());
			}
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				spinnerChange(2, _shaddrlistList.get(1).get(position).getAddrid());
			}
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		
		spinner3.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				spinnerChange(3, _shaddrlistList.get(2).get(position).getAddrid());
			}
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		
		final Dialog dialog = new Dialog(CartJsAty.this, R.style.sys_dialog);
		
		confirmBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				StringBuilder addrNameSb = new StringBuilder();
				for (int i = 0; i < _spinnerList.size(); i ++) {
					addrNameSb.append(_shaddrlistList.get(i).get(_spinnerList.get(i).getSelectedItemPosition()).getName()).append(" ");
				}
				_dzView.setText(addrNameSb.toString());
				dialog.dismiss();
			}
		});
		
		cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		dialog.setCanceledOnTouchOutside(true);
		int dialogWidth = CommViewUtil.initScreenParams(CartJsAty.this)[0] - CommViewUtil.dip2px(_context, 40);
		dialog.setContentView(layout, new LinearLayout.LayoutParams(dialogWidth, CommViewUtil.MatchParent));// 设置布局
		
		dialog.show();
	}

}
