package com.example.scheme;

import android.graphics.Color;
import android.util.Log;

public class ColorModel {

	private int mColorInt;

	private int mRed;
	private int mBlue;
	private int mGreen;
	private int[] mRGB;

	private float mCyan;
	private float mMagenta;
	private float mYellow;
	private float mKey;
	private float[] mCMYK;

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
		mRGB = new int[] { mRed, mBlue, mGreen };

		mHSV = new float[3];
		Color.colorToHSV(mColorInt, mHSV);
		mHue = mHSV[0];
		mSaturation = mHSV[1];
		mValue = mHSV[2];

		mHexCode = String.format("#%02x%02x%02x", mRed, mGreen, mBlue);

		mCMYK = calcCMYK();
		Log.d("color", "c,m,y,k : " + mCyan + "," + mMagenta + "," + mYellow
				+ "," + mKey);

		// calculate and initialize fields
	}

	private float[] calcCMYK() {
		float red_temp = mRed / 255;
		float green_temp = mGreen / 255;
		float blue_temp = mBlue / 255;
		mKey = 1.0f - Math.max(red_temp, Math.max(green_temp, blue_temp));
		if (mKey == 1.0f) {
			mCyan = 0.0f;
			mMagenta = 0.0f;
			mYellow = 0.0f;
		} else {
			mCyan = (1.0f - red_temp - mKey) / (1.0f - mKey);
			mMagenta = (1.0f - green_temp - mKey) / (1.0f - mKey);
			mYellow = (1.0f - blue_temp - mKey) / (1.0f - mKey);
		}
		return new float[] { mCyan, mMagenta, mYellow, mKey };
	}

	private void calcComplement() {
		 
	}
}
