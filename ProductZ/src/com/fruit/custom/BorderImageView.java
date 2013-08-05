package com.fruit.custom;

import com.fruit.fruitonline.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint({ "DrawAllocation" })
public class BorderImageView extends ImageView {
	
	int borderColor = Color.GRAY;
	
	float borderWidth = 2;
	
	Paint paint;
	
	public BorderImageView(Context context) {
		super(context);
		paint = new Paint();
	}

	public BorderImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.BorderImageView);
		borderColor = typedArray.getColor(R.styleable.BorderImageView_borderColor, 0XFFFFFFFF);
		borderWidth = typedArray.getDimension(R.styleable.BorderImageView_borderWidth, 2f);
		typedArray.recycle();
	}
	
	public BorderImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Rect rec = canvas.getClipBounds();
		rec.bottom--;
		rec.right--;
		paint.setColor(borderColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(borderWidth);
		canvas.drawRect(rec, paint);
	}
}
