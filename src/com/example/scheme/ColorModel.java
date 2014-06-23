package com.example.scheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
		
		mComplement = calcComplement();
		Log.d("color", "compl original r,g,b : "+ mRed+","+mGreen+","+mBlue);
		Log.d("color", "compl r,g,b : " + Color.red(mComplement) + "," + Color.green(mComplement) + "," + Color.blue(mComplement));

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

	private int calcComplement() {
		 return Color.rgb(255 - mRed, 255 - mGreen, 255 - mBlue);
	}
	
	public static List GenerateColors_Harmony(
			   int colorCount,
			   float offsetAngle1,
			   float offsetAngle2,
			   float rangeAngle0,
			   float rangeAngle1,
			   float rangeAngle2,
			   float saturation, float luminance)
			{
			   ArrayList colors = new ArrayList();

			   float referenceAngle = Random.nextFloat() * 360;

			   for (int i = 0; i < colorCount; i++)
			   {
			      float randomAngle = 
			         random.NextFloat() * (rangeAngle0 + rangeAngle1 + rangeAngle2);

			      if (randomAngle > rangeAngle0)
			      {
			         if (randomAngle < rangeAngle0 + rangeAngle1)
			         {
			            randomAngle += offsetAngle1;
			         }
			         else
			         {
			            randomAngle += offsetAngle2;
			         }
			      }

			      HSL hslColor = new HSL(
			         ((referenceAngle + randomAngle) / 360.0f) % 1.0f,
			         saturation, 
			         luminance);

			      colors.Add(hslColor.Color);
			   }

			   return colors;
			}
}
