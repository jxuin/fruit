package com.fruit.fragment;

import com.fruit.aty.CartListAty;
import com.fruit.aty.FruitSearchAty;
import com.fruit.fruitonline.R;
import com.fruit.util.FruitUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

public class HeaderFragment extends Fragment {
	
	private TextView _cartnumView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.headerfragment, container, false);
		
		EditText searchView = (EditText) view.findViewById(R.id.header_search);
		
		ImageView cartView = (ImageView) view.findViewById(R.id.header_cart);
		
		_cartnumView = (TextView) view.findViewById(R.id.header_cartnum);
		
		searchView.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				return false;
			}
		});
		
		searchView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), FruitSearchAty.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		
		cartView.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), CartListAty.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		
		return view;
	}
	
	public void cartChangeView() {
		int cartNum = FruitUtil.getCartShared().getInt("cartnum", 0);
		if (cartNum > 0) {
			_cartnumView.setVisibility(View.VISIBLE);
			_cartnumView.setText(String.valueOf(cartNum));
		} else {
			_cartnumView.setVisibility(View.GONE);
		}
	}
	
	public void onResume() {
		super.onResume();
		cartChangeView();
	}

}
