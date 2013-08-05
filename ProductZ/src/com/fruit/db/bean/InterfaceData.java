package com.fruit.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="interfacedata")
public class InterfaceData {
	
	public static final String ID = "id";  
    public static final String KEYID = "keyid";  
    public static final String KEYTYPE = "keytype";  
    public static final String STAMPKEY = "stampkey";//接口唯一标识
    public static final String MD5 = "md5";
    public static final String URLSTR = "urlstr";
    public static final String DATASTR = "datastr";
    
    @DatabaseField(generatedId=true,columnName=ID)
	private int id;
    @DatabaseField(columnName=KEYID)
	private String keyid;
    @DatabaseField(columnName=KEYTYPE)
	private String keytype;
    @DatabaseField(columnName=STAMPKEY)
	private String stampkey;
    @DatabaseField(columnName=MD5)
	private String md5;
    @DatabaseField(columnName=URLSTR)
	private String urlstr;
    @DatabaseField(columnName=DATASTR)
	private String datastr;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKeyid() {
		return keyid;
	}
	public void setKeyid(String keyid) {
		this.keyid = keyid;
	}
	public String getKeytype() {
		return keytype;
	}
	public void setKeytype(String keytype) {
		this.keytype = keytype;
	}
	public String getStampkey() {
		return stampkey;
	}
	public void setStampkey(String stampkey) {
		this.stampkey = stampkey;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getUrlstr() {
		return urlstr;
	}
	public void setUrlstr(String urlstr) {
		this.urlstr = urlstr;
	}
	public String getDatastr() {
		return datastr;
	}
	public void setDatastr(String datastr) {
		this.datastr = datastr;
	}
    

}
