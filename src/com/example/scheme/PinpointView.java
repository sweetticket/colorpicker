package com.example.scheme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class PinpointView extends ImageView {

	public static final int RECT_SIDE_LENGTH = 170;
	public static final int LEFT = 80135;
	public static final int RIGHT = 10350;
	
	private boolean mZooming;
	private int mXPos;
	private int mYPos;
	private int mRectLeft;
	private int mRectRight;
	private Paint mPaint;
	private Bitmap mBitmap;
	private int mColor;
	private int mScreenWidth;
	private int mScreenHeight;
	private Canvas mCanvas;
	private int mDirection;
	

	public PinpointView(Context context) {
		super(context);
		init();
	}

	public PinpointView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PinpointView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mZooming = false;
		mPaint = new Paint();
		mDirection = 0;
		Log.d("bit", "bitmap = " + mBitmap);
	}

	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
		super.onSizeChanged(xNew, yNew, xOld, yOld);
		mScreenWidth = xNew;
		mScreenHeight = yNew;
		mRectLeft = 0;
		mRectRight = RECT_SIDE_LENGTH;
	}

	public void setRectPos() {
		if (mDirection == LEFT) {
			mRectLeft--;
			mRectRight--;
			if (mRectLeft < 0) {
				mRectLeft = 0;
				mRectRight = RECT_SIDE_LENGTH;
			}
		} else if (mDirection == RIGHT) {
			mRectLeft++;
			mRectRight++;
			if (mRectRight > mScreenWidth) {
				mRectLeft = mScreenWidth - RECT_SIDE_LENGTH;
				mRectRight = mScreenWidth;
			}
		}
	}

	public void setZooming(boolean b) {
		mZooming = b;
	}

	public void setZoomPos(float x, float y) {
		mXPos = Math.round(x);
		mYPos = Math.round(y);

	}
	
	public int getXPos(){
		return mXPos;
	}
	
	public int getYPos(){
		return mYPos;
	}
	
	public int getColor(){
		return mColor;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mBitmap = ((BitmapDrawable) this.getDrawable()).getBitmap();
		mCanvas = canvas;

		if (mZooming) {
			mColor = mBitmap.getPixel(mXPos, mYPos);
			mPaint.setColor(mColor);
			drawZoom();
		}

	}

	private void drawZoom() {
		mCanvas.drawRect(mRectLeft, 0, mRectRight, RECT_SIDE_LENGTH, mPaint);
		this.invalidate();
	}

	public boolean isInRectangle() {
		if (mYPos <= RECT_SIDE_LENGTH) {
			if (mXPos >= mRectLeft && mXPos <= mRectRight){
				if (mRectLeft <= mScreenWidth/2){
					mDirection = RIGHT;
				} else {
					mDirection = LEFT;
				}
				return true;
			}
		}
		return false;
	}
}
