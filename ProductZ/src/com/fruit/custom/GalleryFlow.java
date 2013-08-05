package com.fruit.custom;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

@SuppressWarnings("deprecation")
public class GalleryFlow extends Gallery {

    private Camera mCamera = new Camera();
    private int mMaxRotationAngle = 45;
    private int mMaxZoom = -60;
    private int mCoveflowCenter;
    
    private int lastPosition;
    
    Handler handler = new Handler();
    
	public GalleryFlow(Context context) {
            super(context);
            this.setStaticTransformationsEnabled(true);
    }

    public GalleryFlow(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.setStaticTransformationsEnabled(true);
    }

    public GalleryFlow(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            this.setStaticTransformationsEnabled(true);
    }

    public int getMaxRotationAngle() {
            return mMaxRotationAngle;
    }

    public void setMaxRotationAngle(int maxRotationAngle) {
            mMaxRotationAngle = maxRotationAngle;
    }

    public int getMaxZoom() {
            return mMaxZoom;
    }

    public void setMaxZoom(int maxZoom) {
            mMaxZoom = maxZoom;
    }

    private int getCenterOfCoverflow() {
            return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
                            + getPaddingLeft();
    }

    private static int getCenterOfView(View view) {
            return view.getLeft() + view.getWidth() / 2;
    }
    
    protected boolean getChildStaticTransformation(View child, Transformation t) {

            final int childCenter = getCenterOfView(child);
            int rotationAngle = 0;
            final int childWidth = child.getWidth();

            t.clear();
            t.setTransformationType(Transformation.TYPE_MATRIX);
            
            if (childCenter == mCoveflowCenter) {
            	transformImageBitmap((ImageView) child, t, 0);
            } else {
                rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / (childWidth)) * mMaxRotationAngle);
                if (Math.abs(rotationAngle) > mMaxRotationAngle) {
                	rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle : mMaxRotationAngle;
                }
                transformImageBitmap((ImageView) child, t, rotationAngle);
            }
            return true;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            mCoveflowCenter = getCenterOfCoverflow();
            super.onSizeChanged(w, h, oldw, oldh);
    }

    private void transformImageBitmap(ImageView child, Transformation t,
                    int rotationAngle) {
            mCamera.save();
            final Matrix imageMatrix = t.getMatrix();
            final int imageHeight = child.getLayoutParams().height;
            final int imageWidth = child.getLayoutParams().width;

            // 在Z轴上正向移动camera的视角，实际效果为放大图片。
            // 如果在Y轴上移动，则图片上下移动；X轴上对应图片左右移动。
            mCamera.translate(0.0f, 0.0f, 50.0f);

            // 在Y轴上旋转，对应图片竖向向里翻转。
            // 如果在X轴上旋转，则对应图片横向向里翻转。
            mCamera.getMatrix(imageMatrix);
            imageMatrix.preTranslate(-(imageWidth/2), -(imageHeight / 2));
            imageMatrix.postTranslate((imageWidth/2), (imageHeight / 2));
            mCamera.restore();
    }
    
    /*
	 * 生成view 的顺序 选中的左边 顺序生成 选中的右边倒序生成 解决 重叠顺序的问题
	 */
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		// TODO Auto-generated method stub
		int mFirstPosition = getFirstVisiblePosition();
		int mSelectedPosition = getSelectedItemPosition();
		int selectedIndex = mSelectedPosition - mFirstPosition;
		if (i == 0) {
			lastPosition = 0;
		}
		int ret = 0;
		if (selectedIndex < 0) {
			return i;
		}
		if (i == childCount - 1) {
			ret = selectedIndex;
		} else if (i >= selectedIndex) {
			lastPosition++;
			ret = childCount - lastPosition;
		} else {
			ret = i;
		}
		return ret;

	}
}
