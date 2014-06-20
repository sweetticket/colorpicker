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
	
	private boolean mZooming;
	private int mZoomX;
	private int mZoomY;
	private Paint mPaint;
	private Matrix mMatrix;
	private Bitmap mBitmap;
	private int mColor;


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
	
	private void init(){
		mZooming = false;
		mPaint = new Paint();
		Log.d("bit", "bitmap = "+mBitmap);
	}
	
	public void setZooming(boolean b){
		mZooming = b;
	}
	
	public void setZoomPos(float x, float y){
		mZoomX = Math.round(x);
		mZoomY = Math.round(y);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		mBitmap = ((BitmapDrawable)this.getDrawable()).getBitmap();
	    super.onDraw(canvas);
	    
	    if (mZooming) {
	        mColor = mBitmap.getPixel(mZoomX, mZoomY);
	        mPaint.setColor(mColor);
	        
	        Log.d("color", "color: "+mColor); // color doesn't change..
	        
	        //TODO: new bitmap at corner of screen, filled with mColor
	    }
	    
	    
	}
}
