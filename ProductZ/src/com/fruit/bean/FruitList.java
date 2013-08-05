package com.fruit.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class FruitList implements Serializable {
	
	private static final long serialVersionUID = 1401177970076879925L;
	
	ArrayList<FruitDetail> list = new ArrayList<FruitDetail>();

	public ArrayList<FruitDetail> getList() {
		return list;
	}

	public void setList(ArrayList<FruitDetail> list) {
		this.list = list;
	}
	
}
