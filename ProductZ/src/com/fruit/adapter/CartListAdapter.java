package com.fruit.adapter;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener; 

import com.fruit.bean.FruitDetail;
import com.fruit.custom.BorderImageView;
import com.fruit.fruitonline.R;
import com.fruit.util.CommUtil;
import com.fruit.util.CommViewUtil;
import com.fruit.util.FruitUtil;
import com.fruit.util.file.FileUtil;

public class CartListAdapter extends BaseAdapter {

	private List<FruitDetail> _list;
	
	private Handler _handler = new Handler();
	
	private Activity _context;
	
	private boolean[] _checkboxFlags;
	
	private Dialog _delDialog;
	
	private CartChangeListener _cartChangeListener;
	
	public CartListAdapter(Activity context, List<FruitDetail> list, CartChangeListener cartChangeListener) {
		_context = context;
		_list = list;
		setCheckboxFlags();
		_cartChangeListener = cartChangeListener;
	}
	
	public void setNewList(List<FruitDetail> list) {
		_list = list;
		setCheckboxFlags();
	}
	
	public void setCheckboxFlags() {
		_checkboxFlags = new boolean[_list.size()];
		for (int i = 0; i < _checkboxFlags.length; i ++) {
			_checkboxFlags[i] = true;
		}
	}
	
	public void notifyData() {
		this.notifyDataSetChanged();
	}
	
	public boolean[] getCheckboxFlags() {
		return _checkboxFlags;
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
			viewHolder.numView = tmpHolder.numView;
			viewHolder.checkBox = tmpHolder.checkBox;
		} else {
			convertView = LayoutInflater.from(_context).inflate(R.layout.cartlist_item, null);
			TextView titleView = (TextView) convertView.findViewById(R.id.cartlist_item_title);
			TextView priceView = (TextView) convertView.findViewById(R.id.cartlist_item_price);
			BorderImageView imgView = (BorderImageView) convertView.findViewById(R.id.cartlist_item_img);
			TextView numView = (TextView) convertView.findViewById(R.id.cartlist_item_num);
			CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.cartlist_item_check);
			viewHolder.titleView = titleView;
			viewHolder.priceView = priceView;
			viewHolder.imgView = imgView;
			viewHolder.numView = numView;
			viewHolder.checkBox = checkBox;
			convertView.setTag(viewHolder);
		}
		final FruitDetail fruitDetail = _list.get(position);
		final String imgUrlStr = fruitDetail.getImg();
		String title = fruitDetail.getRemark();
		viewHolder.titleView.setText(title);
		String price = fruitDetail.getPrice();
		viewHolder.priceView.setText("￥ " + price + "/" + fruitDetail.getSpdw());
		final String imgNameMd5 = FileUtil.fileNameMd5(imgUrlStr);
		if ((new File(FileUtil.sdCardPathImgStr + imgNameMd5)).exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(FileUtil.sdCardPathImgStr + imgNameMd5);
			if (bitmap != null) {
				viewHolder.imgView.setImageBitmap(bitmap);
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
				}
			}).start();
		}
		
		viewHolder.checkBox.setChecked(true);
		viewHolder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				_checkboxFlags[position] = isChecked;
			}
		});
		
		viewHolder.numView.setText(fruitDetail.getNum());
		viewHolder.numView.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				getCartNumDialog(fruitDetail).show();
			}
		});
		
		return convertView;
	}
	
	public Dialog getCartNumDialog(final FruitDetail fruitDetail) {
		LayoutInflater inflater = LayoutInflater.from(_context);
		View v = inflater.inflate(R.layout.cartnum_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.cartnumdialog_layout);// 加载布局
		// main.xml中的ImageView
		final EditText numView = (EditText) v.findViewById(R.id.cartnum_num);
		numView.setText(fruitDetail.getNum());
		numView.setSelection(numView.getText().toString().length());
		
		final Dialog dialog = new Dialog(_context, R.style.sys_dialog);// 创建自定义样式dialog
		
		ImageButton minusBtn = (ImageButton) v.findViewById(R.id.cartnum_minus);
		ImageButton plusBtn = (ImageButton) v.findViewById(R.id.cartnum_plus);
		Button confirmBtn = (Button) v.findViewById(R.id.cartnum_confirm);
		Button cancelBtn = (Button) v.findViewById(R.id.cartnum_cancel);
		
		minusBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				numView.setText(String.valueOf(CommUtil.getIntVal(numView.getText(), 0) - 1));
				numView.setSelection(numView.getText().toString().length());
			}
		});
		
		plusBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				numView.setText(String.valueOf(CommUtil.getIntVal(numView.getText(), 0) + 1));
				numView.setSelection(numView.getText().toString().length());
			}
		});
		
		confirmBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (CommUtil.getIntVal(numView.getText(), 0) <= 0) {
					_delDialog = CommViewUtil.setDelDialog(_context.getResources().getString(R.string.del_confirm), _context, new OnClickListener() {
						public void onClick(View view) {
							FruitUtil.addFruit2Cart(fruitDetail, 0, false);
							if (_cartChangeListener != null) {
								_cartChangeListener.cartChange();
							}
							_delDialog.dismiss();
						}
					});
					_delDialog.show();
				} else {
					FruitUtil.addFruit2Cart(fruitDetail, CommUtil.getIntVal(numView.getText(), 0), false);
					if (_cartChangeListener != null) {
						_cartChangeListener.cartChange();
					}
				}
				dialog.dismiss();
			}
		});
		
		cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setCanceledOnTouchOutside(true);
		int dialogWidth = CommViewUtil.initScreenParams(_context)[0] - CommViewUtil.dip2px(_context, 40);
		dialog.setContentView(layout, new LinearLayout.LayoutParams(dialogWidth, CommViewUtil.MatchParent));// 设置布局
		
		return dialog;
	}
	
	public interface CartChangeListener{
		public void cartChange();
	}
	
	class ViewHolder {
		TextView titleView;
		TextView priceView;
		TextView numView;
		CheckBox checkBox;
		BorderImageView imgView;
	}
}
