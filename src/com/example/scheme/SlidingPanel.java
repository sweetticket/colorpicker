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

	private int mSpeed = 300;
	private boolean mIsOpen = false;
	private int mGravity;	

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

	public void init(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.SlidingPanel, 0, 0);
		mSpeed = a.getInt(R.styleable.SlidingPanel_speed, 300);
		mGravity = a.getString(R.styleable.SlidingPanel_panelGravity).equals("top") ? FROM_TOP : FROM_BOTTOM;
		a.recycle();
	}
	
	public void toggle() {
		TranslateAnimation anim = null;
		mIsOpen = !mIsOpen;
		if (mGravity == FROM_BOTTOM){
			if (mIsOpen) {
				setVisibility(View.VISIBLE);
				anim = new TranslateAnimation(0.0f, 0.0f, getHeight(), 0.0f);
			} else {
				anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, getHeight());
				anim.setAnimationListener(collapseListener);
			}
		}else{
			if (mIsOpen) {
				setVisibility(View.VISIBLE);
				anim = new TranslateAnimation(0.0f, 0.0f, -getHeight(), 0.0f);
			} else {
				anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, -getHeight());
				anim.setAnimationListener(collapseListener);
			}
		}
		
		anim.setDuration(mSpeed);
		anim.setInterpolator(new AccelerateInterpolator(1.0f));
		startAnimation(anim);
		Log.d("tap", "tap toggle");
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
