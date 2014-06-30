package com.example.scheme;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SchemingView extends ImageView {
	
	private int mScreenW;
	private int mScreenH;
	
	public SchemingView(Context context) {
		super(context);
		init();
	}

	public SchemingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SchemingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init(){
		
	}
	
	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld)
    {   
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        mScreenW = xNew;
        mScreenH = yNew;
    }
	
}
