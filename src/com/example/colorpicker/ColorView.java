package com.example.colorpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class ColorView extends View {
	
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	
	public ColorView(Context context){
		super(context);
		init();
	}
	
	public ColorView(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
	}
	
	public ColorView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public void init(){
		SCREEN_WIDTH = getWidth();
		SCREEN_HEIGHT = getHeight();
	}
}