package com.fruit.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;

import com.fruit.app.SysApplication;
import com.fruit.bean.FruitDetail;
import com.fruit.json.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FruitUtil {
	
	public static void addFruit2Cart(FruitDetail fruitDetail, int num, boolean addFlag) {
		fruitDetail.setNum(String.valueOf(num));
		SharedPreferences share = SysApplication._context.getSharedPreferences("fruitcart", Activity.MODE_PRIVATE);
		String jsonStr = share.getString("jsonstr", "");
		Gson gson = new Gson();
		List<FruitDetail> cartList = new ArrayList<FruitDetail>();
		if (!"".equals(CommUtil.getStrVal(jsonStr))) {
			cartList = gson.fromJson(jsonStr,new TypeToken<List<FruitDetail>>(){}.getType());
		}
		boolean exitFlag = false;
		if (cartList.size() > 0) {
			for (int i = 0; i < cartList.size(); i ++) {
				FruitDetail tmpDetail = cartList.get(i);
				if (CommUtil.getStrVal(tmpDetail.getId()).equals(CommUtil.getStrVal(fruitDetail.getId()))) {
					if (num <= 0) {
						cartList.remove(i);
					} else {
						if (addFlag) {
							tmpDetail.setNum(String.valueOf(CommUtil.getIntVal(tmpDetail.getNum(), 1) + num));
						} else {
							tmpDetail.setNum(String.valueOf(num));
						}
						cartList.set(i, tmpDetail);
					}
					exitFlag = true;
					break;
				}
			}
		}
		if (!exitFlag) {
			cartList.add(fruitDetail);
		}
		setCartInfo2Shared(cartList);
	}
	
	public static SharedPreferences getCartShared() {
		SharedPreferences share = SysApplication._context.getSharedPreferences("fruitcart", Activity.MODE_PRIVATE);
		return share;
	}
	
	public static void setCartInfo2Shared(List<FruitDetail> cartList) {
		SharedPreferences share = getCartShared();
		int cartnum = 0;
		float totalmoney = 0f;
		String jsonStr = "";
		Gson gson = new Gson();
		if (cartList == null) {
			cartList = new ArrayList<FruitDetail>();
			jsonStr = share.getString("jsonstr", "");
			cartList = gson.fromJson(jsonStr,new TypeToken<List<FruitDetail>>(){}.getType());
		} else {
			jsonStr = JsonUtil.getJsonStr(cartList);
		}
		for (FruitDetail fruitDetail : cartList)  {
			int tmpNum = CommUtil.getIntVal(fruitDetail.getNum(), 0);
			cartnum = cartnum + tmpNum;
			totalmoney = totalmoney + CommUtil.getFloatVal(fruitDetail.getPrice(), 0f) * tmpNum;
		}
		SharedPreferences.Editor editor = share.edit();
		editor.putString("jsonstr", jsonStr);
		editor.putInt("cartnum", cartnum);
		editor.putFloat("totalmoney", totalmoney);
		editor.commit();
	}
	
	public static void clearCartJsonStr() {
		SharedPreferences share = SysApplication._context.getSharedPreferences("fruitcart", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = share.edit();
		editor.putString("jsonstr", "");
		editor.putInt("cartnum", 0);
		editor.putFloat("totalmoney", 0f);
		editor.commit();
	}

}
