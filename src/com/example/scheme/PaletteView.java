package com.example.scheme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class PaletteView extends ImageView {

	public static final int RADIUS = 60;
	public static final int BIT_HEIGHT = 130;
	public static final int BIT_WIDTH = 130;

	private Paint mPaint;
	/*private int mScreenWidth;
	private int mScreenHeight;*/
	

	
	
	public PaletteView(Context context) {
		super(context);
		init();
	}
	
	public PaletteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public PaletteView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public void init(){
		mPaint = new Paint();

	}
	
	public void setPaintColor(int color){
		mPaint.setColor(color);
	}
	
	public int getPaintColor(){
		return mPaint.getColor();
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		drawColor();
	}
	
	private void drawColor(){
		Bitmap canvasBit = Bitmap.createBitmap(BIT_WIDTH, BIT_HEIGHT,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(canvasBit);
		canvas.drawCircle(BIT_WIDTH/2, BIT_HEIGHT/2,
				RADIUS, mPaint);
		setImageDrawable(new BitmapDrawable(getResources(),
				canvasBit));
		this.invalidate();
	}

}
