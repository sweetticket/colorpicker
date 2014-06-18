package com.example.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ColorView extends View {

	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;

	public ColorView(Context context) {
		super(context);
		init();
	}

	public ColorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ColorView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public void init() {
		
	}

	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
		super.onSizeChanged(xNew, yNew, xOld, yOld);
		SCREEN_WIDTH = getWidth();
		SCREEN_HEIGHT = getHeight();
		
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
	}
}