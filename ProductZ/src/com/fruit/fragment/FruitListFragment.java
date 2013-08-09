package com.fruit.fragment;

import com.fruit.aty.FruitDetailAty;
import com.fruit.bean.FruitDetail;
import com.fruit.bean.FruitList;
import com.fruit.fruitonline.R;
import com.fruit.util.CommUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FruitListFragment extends ListFragment {
	
	private FruitList _fruitList;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.listfragment, container, false);
		
		return view;
	}
	
	public void setFruitList(FruitList fruitList) {
		
		_fruitList = fruitList;
	}
	
	public void onListItemClick(ListView parent, View v, int position, long id) {
		
		FruitDetail fruitDetail = _fruitList.getList().get(position);
		Intent intent = new Intent(getActivity(), FruitDetailAty.class);
		Bundle bundle = new Bundle();
		bundle.putString("fruitid", fruitDetail.getId());
		intent.putExtra(CommUtil.DETAIL_BUNDLE, bundle);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}
