package com.fruit.adapter;

import java.io.File;
import java.util.List;

import com.fruit.app.SysApplication;
import com.fruit.bean.FruitDetail;
import com.fruit.custom.BorderImageView;
import com.fruit.fruitonline.R;
import com.fruit.util.CommViewUtil;
import com.fruit.util.FruitUtil;
import com.fruit.util.file.FileUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class FruitListAdapter extends BaseAdapter {

	private List<FruitDetail> _list;
	
	private Handler _handler = new Handler();
	
	private CartChangeListener _cartChangeListener;

	private Context _context;
	
	public FruitListAdapter(Context context, List<FruitDetail> list, CartChangeListener listener) {
		_context = context;
		_list = list;
		_cartChangeListener = listener;
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
			viewHolder.priceView = tmpHolder.priceView;
			viewHolder.imgView = tmpHolder.imgView;
			viewHolder.btn = tmpHolder.btn;
		} else {
			convertView = LayoutInflater.from(_context).inflate(R.layout.list_fruitlist_item, null);
			TextView titleView = (TextView) convertView.findViewById(R.id.list_fruitlist_title);
			TextView priceView = (TextView) convertView.findViewById(R.id.list_fruitlist_price);
			BorderImageView imgView = (BorderImageView) convertView.findViewById(R.id.list_fruitlist_img);
			Button btn = (Button) convertView.findViewById(R.id.list_fruitlist_add2cart);
			viewHolder.titleView = titleView;
			viewHolder.priceView = priceView;
			viewHolder.imgView = imgView;
			viewHolder.btn = btn;
			convertView.setTag(viewHolder);
		}
		final FruitDetail fruitDetail = _list.get(position);
		final String imgUrlStr = fruitDetail.getImg();
		String title = fruitDetail.getName();
		viewHolder.titleView.setText(title);
		String price = fruitDetail.getPrice();
		viewHolder.priceView.setText("￥ " + price + "/" + fruitDetail.getSpdw());
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
		
		viewHolder.btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FruitUtil.addFruit2Cart(fruitDetail, 1, true);
				CommViewUtil.handlerToast(SysApplication._context.getResources().getString(R.string.addsuccess));
				if (_cartChangeListener != null) {
					_cartChangeListener.cartChange();
				}
			}
		});

		return convertView;
	}
	
	public interface CartChangeListener{
		public void cartChange();
	}
	
	class ViewHolder {
		TextView titleView;
		TextView priceView;
		BorderImageView imgView;
		Button btn;
	}
}
