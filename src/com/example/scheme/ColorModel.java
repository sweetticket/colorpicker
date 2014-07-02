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
	private int[] mSplitComp;
	private int[] mAnalog;
	private int[] mMonochrome;

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
		// Log.d("color", "hsv: " + mHue + "," + mSaturation + "," + mValue);

		mHexCode = String.format("#%02x%02x%02x", mRed, mGreen, mBlue);

		mCMYK = calcCMYK();

		mComplement = calcComplement();
		/*
		 * Log.d("color", "compl original r,g,b : " + mRed + "," + mGreen + ","
		 * + mBlue); Log.d("color", "compl r,g,b : " + Color.red(mComplement) +
		 * "," + Color.green(mComplement) + "," + Color.blue(mComplement));
		 */

		mTriad = calcTriad();

		mSplitComp = calcSplitComp();

		mAnalog = calcAnalog();

		mMonochrome = calcMonochrome();
	}

	public ColorModel(int r, int g, int b) {
		this(Color.rgb(r, g, b));
	}

	public ColorModel(float[] hsv) {
		this(Color.HSVToColor(hsv));
	}

	public ColorModel(float c, float m, float y, float k) {
		this(Math.round(255.0f * (1.0f - c) * (1.0f - k)), Math.round(255.0f
				* (1.0f - m) * (1.0f - k)), Math.round(255.0f * (1.0f - y)
				* (1.0f - k)));

	}

	public ColorModel(String hexcode) {
		this(Integer.valueOf(hexcode.substring(1, 3), 16), Integer.valueOf(
				hexcode.substring(3, 5), 16), Integer.valueOf(
				hexcode.substring(5, 7), 16));
	}

	public int getColor() {
		return mColorInt;
	}

	public float getHue() {
		return mHue;
	}

	public float getSaturation() {
		return mSaturation;
	}

	public float getValue() {
		return mValue;
	}

	public String getHexCode() {
		return mHexCode;
	}

	public int[] getRGB() {
		return mRGB;
	}

	public float[] getHSV() {
		return mHSV;
	}

	public float[] getCMYK() {
		return mCMYK;
	}

	public int[] getTriad() {
		return mTriad;
	}

	public int getComplement() {
		return mComplement;
	}

	public int[] getAnalog() {
		return mAnalog;
	}

	public int[] getSplitComp() {
		return mSplitComp;
	}

	public int[] getMonochrome() {
		return mMonochrome;
	}

	private float[] calcCMYK() {
		float red_temp = mRed / 255.0f;
		float green_temp = mGreen / 255.0f;
		float blue_temp = mBlue / 255.0f;
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

	private int calcComplement() {
		return Color.rgb(255 - mRed, 255 - mGreen, 255 - mBlue);
		// or add 180 to HSV hue value
	}

	private int[] calcTriad() {
		float[] hsv_temp_1 = new float[] { (mHue + 120) % 360, mSaturation,
				mValue };
		float[] hsv_temp_2 = new float[] { (mHue - 120) % 360, mSaturation,
				mValue };
		return new int[] { Color.HSVToColor(hsv_temp_1),
				Color.HSVToColor(hsv_temp_2) };
	}

	private int[] calcSplitComp() {
		float[] hsv_temp_1 = new float[] { (mHue + 150) % 360, mSaturation,
				mValue };
		float[] hsv_temp_2 = new float[] { (mHue - 150) % 360, mSaturation,
				mValue };
		return new int[] { Color.HSVToColor(hsv_temp_1),
				Color.HSVToColor(hsv_temp_2) };
	}

	private int[] calcAnalog() {
		float[] hsv_temp_1 = new float[] { (mHue + 30) % 360, mSaturation,
				mValue };
		float[] hsv_temp_2 = new float[] { (mHue - 30) % 360, mSaturation,
				mValue };
		return new int[] { Color.HSVToColor(hsv_temp_1),
				Color.HSVToColor(hsv_temp_2) };
	}

	private int[] calcMonochrome() {
		float[] hsv_temp_1 = new float[] { mHue, mSaturation, .1f };
		float[] hsv_temp_3 = new float[] { mHue, mSaturation, .3f };
		float[] hsv_temp_5 = new float[] { mHue, mSaturation, .5f };
		float[] hsv_temp_7 = new float[] { mHue, mSaturation, .7f };
		float[] hsv_temp_9 = new float[] { mHue, mSaturation, .9f };

		return new int[] { Color.HSVToColor(hsv_temp_1),
				Color.HSVToColor(hsv_temp_3), Color.HSVToColor(hsv_temp_5),
				Color.HSVToColor(hsv_temp_7), Color.HSVToColor(hsv_temp_9) };
	}

}
