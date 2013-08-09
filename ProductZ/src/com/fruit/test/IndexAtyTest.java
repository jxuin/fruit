package com.fruit.test;

import com.fruit.aty.IndexAty;

import android.test.ActivityInstrumentationTestCase2;

public class IndexAtyTest extends ActivityInstrumentationTestCase2<IndexAty> {
	
	@SuppressWarnings("deprecation")
	public IndexAtyTest() {
		super("com.fruit.aty", IndexAty.class);
	}

	public IndexAtyTest(Class<IndexAty> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}
	
	public void setup() throws Exception {
		super.setUp();
	}
	
	public void testAdd() {
		assertEquals(5, 5);
	}

}
