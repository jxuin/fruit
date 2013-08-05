package com.fruit.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.fruit.bean.FruitDetail;
import com.fruit.bean.FruitList;
import com.fruit.bean.FruitShAddr;
import com.fruit.util.CommUtil;
import com.fruit.util.net.NetUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings("rawtypes")
public class JsonUtil {
	
	public static String getJsonData(String urlPrefix, HashMap params, int mothod) {
		
		String jsonStr = "";
		if (mothod == JsonUrlParams.JSON_GET) {
			jsonStr = NetUtil.getHtmlHttpGet(urlPrefix, params);
		} else {
			jsonStr = NetUtil.getHtmlHttpPost(urlPrefix, params);
		}
		return jsonStr;
	}
	
	public static String getStrFromParams(String urlPrefix, HashMap<String, String> params) {
		
		String urlStr = urlPrefix;
		
		StringBuilder urlSb = new StringBuilder(urlStr);
		
		if (params != null) {
			Iterator iterator = params.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String value = params.get(key);
				urlSb.append(key).append("=").append(value).append("&");
			}
			urlStr = urlSb.toString();
			if (urlStr.lastIndexOf("&") == (urlStr.length() - 1)) {
				urlStr = urlStr.substring(0, urlStr.length() - 1);
			}
		}
		return urlStr;
	}
	
	public static HashMap<String, String> copyParams(HashMap<String, String> params) {
		HashMap<String, String> tmpMap = new HashMap<String, String>();
		Iterator iterator = params.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = params.get(key);
			tmpMap.put(key, value);
		}
		return tmpMap;
	}
	
	public static FruitList getFruitListJsonData(String urlPrefix, HashMap<String, String> params, int mothod) {
		FruitList obj = null;
		String gsonStr = getJsonData(urlPrefix, params, mothod);
		Gson gson = new Gson();
		if (!"".equals(CommUtil.getStrVal(gsonStr))) {
			obj = gson.fromJson(gsonStr,new TypeToken<FruitList>(){}.getType());
		}
		return obj;
	}
	
	public static FruitDetail getFruitDetailJsonData(String urlPrefix, HashMap<String, String> params, int mothod) {
		FruitDetail obj = null;
		String gsonStr = getJsonData(urlPrefix, params, mothod);
		Gson gson = new Gson();
		if (!"".equals(CommUtil.getStrVal(gsonStr))) {
			obj = gson.fromJson(gsonStr,new TypeToken<FruitDetail>(){}.getType());
		}
		return obj;
	}
	
	public static ArrayList<FruitShAddr> getFruitShAddrListJsonData(String urlPrefix, HashMap<String, String> params, int mothod) {
		ArrayList<FruitShAddr> obj = null;
		String gsonStr = getJsonData(urlPrefix, params, mothod);
		Gson gson = new Gson();
		if (!"".equals(CommUtil.getStrVal(gsonStr))) {
			obj = gson.fromJson(gsonStr,new TypeToken<ArrayList<FruitShAddr>>(){}.getType());
		}
		return obj;
	}
	
	public static String getJsonStr(Object o){
		
		String jsonStr = "[]";
		
		if(o != null){
			
			Gson gson = new Gson();
			
			jsonStr = gson.toJson(o);
			
			jsonStr = jsonStr.replace("\\u003d", "=").replace("\\u0026", "&").replace("\\u0027", "'");
			
		}
		return jsonStr;
	}
}
