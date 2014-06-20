package com.example.scheme;

import android.graphics.Color;

public class ColorModel {
	
	private int mColorInt;
	
	private int mRed;
	private int mBlue;
	private int mGreen;
	private int[] mRGB;
	
	private int mCyan;
	private int mMagenta;
	private int mYellow;
	private int mKey;
	private int[] mCMYK;
	
	private float mHue;
	private float mSaturation;
	private float mValue;
	private float[] mHSV;
	
	private String mHexCode;
	
	private int mComplement;
	private int[] mTriad;
	private int[] mSplitComplements;
	private int[] mAnalog;
	
	public ColorModel(int color) {
		mColorInt = color;
		mRed = Color.red(mColorInt);
		mBlue = Color.blue(mColorInt);
		mGreen = Color.green(mColorInt);
		mRGB = new int[] {mRed, mBlue, mGreen};
		
		Color.colorToHSV(mColorInt, mHSV);
		mHue = mHSV[0];
		mSaturation = mHSV[1];
		mValue = mHSV[2];
		
		
		// calculate and initialize fields
	}

	private void calcComplement(){
		//TODO
	}
}
