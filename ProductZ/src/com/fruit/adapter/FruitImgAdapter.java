package com.fruit.adapter;

import java.io.File;
import java.util.List;

import com.fruit.bean.FruitImg;
import com.fruit.custom.GalleryFlow;
import com.fruit.util.CommViewUtil;
import com.fruit.util.file.FileUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class FruitImgAdapter extends BaseAdapter {
	
	final Handler _handler = new Handler();
	
	private Context _context;
	
	private List<FruitImg> _list;
	
	public FruitImgAdapter(Context context, List<FruitImg> list) {
		_context = context;
		_list = list;
	}
	
	public void setNewList(List<FruitImg> list) {
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

	@SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent) {
		final FruitImg fruitImg = _list.get(position);
		final ImageView imageView = new ImageView(_context);
		int width = CommViewUtil.dip2px(_context, 200);
		imageView.setLayoutParams(new GalleryFlow.LayoutParams(width, width));
//		imageView.setImageResource(R.drawable.l1_mainpage_paper_default);
		imageView.setScaleType(ScaleType.CENTER_INSIDE);
		final String imgUrlStr = fruitImg.getImg();
		final String imgNameMd5 = FileUtil.fileNameMd5(imgUrlStr);
		
		if ((new File(FileUtil.sdCardPathImgStr + imgNameMd5)).exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(FileUtil.sdCardPathImgStr + imgNameMd5);
			if (bitmap != null) {
				imageView.setImageBitmap(bitmap);
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
								imageView.setImageBitmap(bitmap);
							}
						});
					}
					if(bitmap != null && bitmap.isRecycled()){
						bitmap.recycle();
				    }
				}
			}).start();
		}
		return imageView;
	}

	public float getScale(boolean focused, int offset) {
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}
	
}
