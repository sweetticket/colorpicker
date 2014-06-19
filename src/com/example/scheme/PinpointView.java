package com.example.scheme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.widget.ImageView;

public class PinpointView extends ImageView {
	
	private boolean mZooming;
	private float mZoomX;
	private float mZoomY;
	private Paint mPaint;
	private Matrix mMatrix;
	private Bitmap mMagnifier;


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
		Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
		mMagnifier = Bitmap.createBitmap(30, 30, conf); 
		BitmapShader shader = new BitmapShader(mMagnifier, TileMode.CLAMP, TileMode.CLAMP);
		mMatrix = new Matrix();
		mPaint = new Paint();
		mPaint.setShader(shader);
	}
	
	public void setZooming(boolean b){
		mZooming = b;
	}
	
	public void setZoomPos(float x, float y){
		mZoomX = x;
		mZoomY = y;
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {

	    super.onDraw(canvas);
	    
	    if (mZooming) {
	        mMatrix.reset();
	        mMatrix.postScale(2f, 2f, mZoomX, mZoomY);
	        mPaint.getShader().setLocalMatrix(mMatrix);

	        canvas.drawCircle(mZoomX, mZoomY, 100, mPaint);
	    }
	}
}
