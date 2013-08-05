package com.fruit.adapter;

import java.util.List;

import com.fruit.bean.FruitDetail;
import com.fruit.fruitonline.R;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter {
final Handler _handler = new Handler();
	
	private Context _context;
	
	private List<FruitDetail> _list;
	
	public SearchAdapter(Context context, List<FruitDetail> list) {
		_context = context;
		_list = list;
	}
	
	public void setNewList(List<FruitDetail> list) {
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

	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder viewHolder = new ViewHolder();
		
		if (convertView != null) {
			ViewHolder tmpHolder = (ViewHolder) convertView.getTag();
			viewHolder.titleView = tmpHolder.titleView;
		} else {
			convertView = LayoutInflater.from(_context).inflate(R.layout.search_fruitlist_item, null);
			TextView titleView = (TextView) convertView.findViewById(R.id.search_fruitlist_title);
			viewHolder.titleView = titleView;
			convertView.setTag(viewHolder);
		}
		
		FruitDetail fruitDetail = _list.get(position);
		
		viewHolder.titleView.setText(fruitDetail.getName());
		
		return convertView;
	}
	
	class ViewHolder {
		TextView titleView;
	}
}
