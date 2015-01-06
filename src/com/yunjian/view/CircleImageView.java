package com.yunjian.view;


import com.yunjian.activity.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;


public class CircleImageView extends View {

	// 鎺т欢鐨勯珮搴�?拰�?�藉�?
	private int mWidth;
	private int mHeight;
	// 鎺т欢鐨勫浘鐗囪祫婧�
	private Bitmap srcBitmap;

	public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.CircleImageView);
		int resourceId = typedArray.getResourceId(
				R.styleable.CircleImageView_src, R.drawable.logo);
		srcBitmap = BitmapFactory.decodeResource(getResources(), resourceId);
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleImageView(Context context) {
		this(context, null, 0);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mWidth = MeasureSpec.getSize(widthMeasureSpec);
		mHeight = MeasureSpec.getSize(heightMeasureSpec);
		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
			mWidth = Math.min(mWidth, srcBitmap.getWidth() + getPaddingLeft()
					+ getPaddingRight());
		}
		if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
			mHeight = Math.min(mHeight, srcBitmap.getHeight() + getPaddingTop()
					+ getPaddingBottom());
		}
		int min = Math.min(mWidth, mHeight);
		mWidth = min;
		mHeight = min;
		setMeasuredDimension(mWidth, mHeight);
	}
	
	public void setBitmap(Bitmap srcBitmap){
		this.srcBitmap = srcBitmap;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);

		srcBitmap = getCircleBitmap();
		canvas.drawBitmap(srcBitmap, 0, 0, null);
	}

	private Bitmap getCircleBitmap() {
		Bitmap bitmap = cutBitmap();
		Paint paint = new Paint();
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, 0, 0, paint);
		bitmap = null;
		return output;
	}

	private Bitmap cutBitmap() {
		int bitmapWidth = srcBitmap.getWidth();
		int bitmapHeidht = srcBitmap.getHeight();
		Bitmap bitmap;
		if (bitmapHeidht < bitmapWidth) {
			bitmap = Bitmap.createBitmap(srcBitmap,
					(bitmapWidth - bitmapHeidht) / 2, 0, bitmapHeidht,
					bitmapHeidht);
		} else if (bitmapWidth < bitmapHeidht) {
			bitmap = Bitmap.createBitmap(srcBitmap, 0,
					(bitmapHeidht - bitmapWidth) / 2, bitmapWidth, bitmapWidth);
		} else {
			bitmap = srcBitmap;
		}
		bitmap = Bitmap.createScaledBitmap(bitmap, mWidth, mHeight, false);
		return bitmap;
	}

}