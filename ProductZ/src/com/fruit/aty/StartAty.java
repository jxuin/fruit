package com.fruit.aty;

import java.util.ArrayList;
import java.util.HashMap;

import com.fruit.bean.FruitShAddr;
import com.fruit.db.ShAddrDataDao;
import com.fruit.db.bean.ShAddrData;
import com.fruit.fruitonline.R;
import com.fruit.json.JsonUrlParams;
import com.fruit.json.JsonUtil;
import com.fruit.util.CommViewUtil;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class StartAty extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CommViewUtil.initWindows(this);
		setContentView(R.layout.start);
		initParams();
		
		Intent intent = new Intent(getBaseContext(), IndexAty.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	public void initParams() {
		new Thread(new Runnable() {
			public void run() {
				HashMap<String, String> jsonParams = new HashMap<String, String>();
				ArrayList<FruitShAddr> list = JsonUtil.getFruitShAddrListJsonData(JsonUrlParams.shaddrUrlPrefix, jsonParams, JsonUrlParams.JSON_GET);
				if (list != null && list.size() > 0) {
					try {
						Dao<ShAddrData, Integer> dao = ShAddrDataDao.getDao();
						dao.deleteBuilder().delete();
						for (int i = 0; i < list.size(); i ++) {
							FruitShAddr bean = list.get(i);
							ShAddrData data = new ShAddrData();
							data.setAddrid(bean.getAddrid());
							data.setName(bean.getName());
							data.setPaddrid(bean.getPaddrid());
							dao.createIfNotExists(data);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
