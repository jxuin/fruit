package com.fruit.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.fruit.db.bean.ShAddrData;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DataHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "fruitonline.db";
	private static final int DATABASE_VERSION = 1;
	private static DataHelper _dataHelper;

	public static synchronized DataHelper getDataHelper(Context context) {
		if (_dataHelper == null) {
			_dataHelper = new DataHelper(context);
		}
		return _dataHelper;
	}

	private DataHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
//			TableUtils.createTable(connectionSource, InterfaceData.class);
			TableUtils.createTable(connectionSource, ShAddrData.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int arg2, int arg3) {
		try {
//			TableUtils.dropTable(connectionSource, InterfaceData.class, true);
			TableUtils.dropTable(connectionSource, ShAddrData.class, true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		onCreate(db, connectionSource);
	}

	@Override
	public void close() {
		super.close();
	}
}
