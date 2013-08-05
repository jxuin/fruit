package com.fruit.adapter;

import java.util.List;

import com.fruit.db.bean.ShAddrData;
import com.fruit.fruitonline.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShAddrAdapter extends BaseAdapter {

	private List<ShAddrData> _list;
	
	private Context _context;
	
	public ShAddrAdapter(Context context, List<ShAddrData> list) {
		_context = context;
		_list = list;
	}
	
	public void setNewList(List<ShAddrData> list) {
		_list = list;
	}

	public int getCount() {
		return _list.size();
	}

	public Object getItem(int position) {
		return _list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		
		
		if (convertView == null) {
			convertView = LayoutInflater.from(_context).inflate(R.layout.shaddr_item, null);
		}
		TextView nameView = (TextView) convertView.findViewById(R.id.shaddr_item_name);
		
		nameView.setText(_list.get(position).getName());
		
		return convertView;
	}
}
