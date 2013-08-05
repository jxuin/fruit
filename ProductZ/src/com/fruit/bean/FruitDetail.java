package com.fruit.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class FruitDetail implements Serializable {
	
	private static final long serialVersionUID = 2050892413982144454L;

	private String id = "";
	
	private String name = "";
	
	private String remark = "";
	
	private String price = "";
	
	private String spdw = "";
	
	private String kc = "";
	
	private String psfw = "";
	
	private String cd = "";
	
	private String pp = "";
	
	private String img = "";
	
	private String spxx = "";
	
//	private String spjs = "";
//	
//	private String ccfs = "";
//	
//	private String spjj = "";
//	
//	private String tjcp = "";
	
	private String num = "1";
	
	ArrayList<FruitImg> imglist = new ArrayList<FruitImg>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSpdw() {
		return spdw;
	}

	public void setSpdw(String spdw) {
		this.spdw = spdw;
	}

	public String getKc() {
		return kc;
	}

	public void setKc(String kc) {
		this.kc = kc;
	}

	public String getPsfw() {
		return psfw;
	}

	public void setPsfw(String psfw) {
		this.psfw = psfw;
	}

	public String getCd() {
		return cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}

	public String getPp() {
		return pp;
	}

	public void setPp(String pp) {
		this.pp = pp;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getSpxx() {
		return spxx;
	}

	public void setSpxx(String spxx) {
		this.spxx = spxx;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public ArrayList<FruitImg> getImglist() {
		return imglist;
	}

	public void setImglist(ArrayList<FruitImg> imglist) {
		this.imglist = imglist;
	}


}
