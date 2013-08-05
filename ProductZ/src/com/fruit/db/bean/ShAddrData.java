package com.fruit.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="shaddrdata")
public class ShAddrData {
	
	public static final String ID = "id";  
	public static final String ADDRID = "addrid";  
	public static final String NAME = "name";
	public static final String PADDRID = "paddrid";
	
	@DatabaseField(generatedId=true,columnName=ID)
	private int id;
    @DatabaseField(columnName=ADDRID)
	private String addrid;
    @DatabaseField(columnName=NAME)
	private String name;
    @DatabaseField(columnName=PADDRID)
	private String paddrid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAddrid() {
		return addrid;
	}
	public void setAddrid(String addrid) {
		this.addrid = addrid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPaddrid() {
		return paddrid;
	}
	public void setPaddrid(String paddrid) {
		this.paddrid = paddrid;
	}

}
