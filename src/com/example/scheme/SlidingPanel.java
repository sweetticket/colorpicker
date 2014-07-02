package com.example.scheme;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class SlidingPanel extends LinearLayout {
	
	public static final int FROM_TOP = 23592435;
	public static final int FROM_BOTTOM = 2351354;
	public static final int FROM_RIGHT = 8775473;
	public static final int FROM_LEFT = 2999138;


	private int mSpeed = 300;
	private boolean mIsOpen = false;
	private int mGravity;
	private int mBackgroundColor;

	public SlidingPanel(Context context) {
		super(context);
	}

	public SlidingPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public SlidingPanel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}
	
	public boolean getIsOpen(){
		return mIsOpen;
	}
	
	public void setSpeed(int speed){
		mSpeed = speed;
	}
	
	@Override
	public void setBackgroundColor(int color){
		mBackgroundColor = color;
		super.setBackgroundColor(color);
	}
	
	public int getBackgroundColor(){
		return mBackgroundColor;
	}
	
	public void setGravity(String panelGravity){
		if (panelGravity.equals("top")){
			mGravity = FROM_TOP;
		}else if (panelGravity.equals("bottom")){
			mGravity = FROM_BOTTOM;
		}else if (panelGravity.equals("right")){
			mGravity = FROM_RIGHT;
		}else{
			mGravity = FROM_LEFT;
		}
	}
	
	public void init(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.SlidingPanel, 0, 0);
		mSpeed = a.getInt(R.styleable.SlidingPanel_speed, 300);
		String panelGravity = a.getString(R.styleable.SlidingPanel_panelGravity);
		if (panelGravity.equals("top")){
			mGravity = FROM_TOP;
		}else if (panelGravity.equals("bottom")){
			mGravity = FROM_BOTTOM;
		}else if (panelGravity.equals("right")){
			mGravity = FROM_RIGHT;
		}else{
			mGravity = FROM_LEFT;
		}
		a.recycle();
	}
	
	public void toggle() {
		TranslateAnimation anim = null;
		mIsOpen = !mIsOpen;
		bringToFront();
		
		if (mGravity == FROM_BOTTOM){
			if (mIsOpen) {
				setVisibility(View.VISIBLE);
				anim = new TranslateAnimation(0.0f, 0.0f, getHeight(), 0.0f);
			} else {
				
				anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, getHeight());
				anim.setAnimationListener(collapseListener);
			}
		}else if (mGravity == FROM_TOP){
			if (mIsOpen) {
				setVisibility(View.VISIBLE);
				anim = new TranslateAnimation(0.0f, 0.0f, -getHeight(), 0.0f);
			} else {
				anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, -getHeight());
				anim.setAnimationListener(collapseListener);
			}
		}else if (mGravity == FROM_LEFT){
			if (mIsOpen) {
				setVisibility(View.VISIBLE);
				anim = new TranslateAnimation(-getWidth(), 0.0f, 0.0f, 0.0f);
			} else {
				anim = new TranslateAnimation(0.0f, -getWidth(), 0.0f, 0.0f);
				anim.setAnimationListener(collapseListener);
			}
		}else{
			if (mIsOpen) {
				setVisibility(View.VISIBLE);
				anim = new TranslateAnimation(getWidth(), 0.0f, 0.0f, 0.0f);
			} else {
				anim = new TranslateAnimation(0.0f, getWidth(), 0.0f, 0.0f);
				anim.setAnimationListener(collapseListener);
			}
		}
		
		anim.setDuration(mSpeed);
		anim.setInterpolator(new AccelerateInterpolator(1.0f));
		startAnimation(anim);
	}

	Animation.AnimationListener collapseListener = new Animation.AnimationListener() {
		public void onAnimationEnd(Animation animation) {
			setVisibility(View.GONE);
		}

		public void onAnimationRepeat(Animation animation) {
			// not needed
		}

		public void onAnimationStart(Animation animation) {
			// not needed
		}
	};
}
