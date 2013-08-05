package com.fruit.db;

import com.fruit.app.SysApplication;
import com.fruit.db.bean.ShAddrData;
import com.j256.ormlite.dao.Dao;

public class ShAddrDataDao {

	private static DataHelper _dataHelper = null;

	private static Dao<ShAddrData, Integer> _dao = null;

	public static Dao<ShAddrData, Integer> getDao() {
		try {
			if (_dao == null) {
				_dataHelper = DataHelper.getDataHelper(SysApplication._context);
				_dao = _dataHelper.getDao(ShAddrData.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _dao;
	}

	public static void close() {
		_dao = null;
	}
}
