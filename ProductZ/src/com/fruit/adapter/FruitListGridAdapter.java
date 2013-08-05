package com.fruit.adapter;

import java.io.File;
import java.util.List;

import com.fruit.bean.FruitDetail;
import com.fruit.fruitonline.R;
import com.fruit.util.file.FileUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FruitListGridAdapter extends BaseAdapter {

	private List<FruitDetail> _list;
	
	private Handler _handler = new Handler();
	
	public FruitListGridAdapter(Context _context, List<FruitDetail> list) {
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
		final String imgNameMd5 = FileUtil.fileNameMd5(imgUrlStr);
		if ((new File(FileUtil.sdCardPathImgStr + imgNameMd5)).exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(FileUtil.sdCardPathImgStr + imgNameMd5);
			if (bitmap != null) {
				viewHolder.imgView.setImageBitmap(bitmap);
			}
			if(bitmap != null && bitmap.isRecycled()){
				bitmap.recycle();
		    }
		} else {
			new Thread(new Runnable() {
				public void run() {
					FileUtil.downloadFile2SD(imgUrlStr, FileUtil.sdCardPathImgStr + imgNameMd5, FileUtil.WRITE_NOREPLACE);
					final Bitmap bitmap = BitmapFactory.decodeFile(FileUtil.sdCardPathImgStr + imgNameMd5);
					if (bitmap != null) {
						_handler.post(new Runnable() {
							public void run() {
								viewHolder.imgView.setImageBitmap(bitmap);
							}
						});
					}
					if(bitmap != null && bitmap.isRecycled()){
						bitmap.recycle();
				    }
				}
			}).start();
		}

		return convertView;
	}
	
	class ViewHolder {
		TextView titleView;
		ImageView imgView;
	}
}
