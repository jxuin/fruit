package com.fruit.adapter;

import java.util.List;

import com.fruit.bean.FruitDetail;
import com.fruit.fruitonline.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FruitListGridAdapter extends BaseAdapter {
	
	private ImageLoader _imageLoader;
	
	private DisplayImageOptions _options;

	private List<FruitDetail> _list;
	
	public FruitListGridAdapter(Context _context, List<FruitDetail> list) {
		_list = list;
		 _imageLoader = ImageLoader.getInstance();
		_options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_stub).showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_error).cacheInMemory(true).cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
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
		
		final ViewHolder viewHolder = new ViewHolder();
		
		if (convertView != null) {
			ViewHolder tmpHolder = (ViewHolder) convertView.getTag();
			viewHolder.titleView = tmpHolder.titleView;
			viewHolder.imgView = tmpHolder.imgView;
		} else {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.index_fruitlist_item, null);
			TextView titleView = (TextView) convertView.findViewById(R.id.index_fruitlist_title);
			ImageView imgView = (ImageView) convertView.findViewById(R.id.index_fruitlist_img);
			viewHolder.titleView = titleView;
			viewHolder.imgView = imgView;
			convertView.setTag(viewHolder);
		}
		FruitDetail fruitDetail = _list.get(position);
		final String imgUrlStr = fruitDetail.getImg();
		String title = fruitDetail.getName();
		viewHolder.titleView.setText(title);
		_imageLoader.displayImage(imgUrlStr, viewHolder.imgView, _options);

		return convertView;
	}
	
	class ViewHolder {
		TextView titleView;
		ImageView imgView;
	}
}
