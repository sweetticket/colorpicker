package com.example.scheme;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ColorPickerActivity extends FragmentActivity {

	public final static int BY_HUE = 9035;
	public final static int BY_SATURATION = 2039;
	public final static int BY_VALUE = 5893;
	public final static int NO_COLOR = 230958;
	public static int COLOR_COUNT;

	public HashMap<Integer, String> mPosToHexMap;
	public HashMap<BigDecimal, Integer> mColorToPosMap;
	public HashMap<Integer, BigDecimal> mPosToColorMap;
	private HSVPagerAdapter mHSVPagerAdapter;
	private ColorPickerActivity mColorPickerActivity;
	ViewPager mViewPager;
	private int mBaseColor;
	private ColorModel mBaseColorModel;
	private float mBaseHue;
	private float mBaseSat;
	private float mBaseVal;

	private float mCurrentHue;
	private float mCurrentSat;
	private float mCurrentVal;
	private Integer mCurrentColor;
	private CharSequence mToastText;
	private int mBrowseBy;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mColorPickerActivity = this;
		setContentView(R.layout.fragment_color_picker_activity2);
		mPosToHexMap = new HashMap<Integer, String>();
		mColorToPosMap = new HashMap<BigDecimal, Integer>();
		mPosToColorMap = new HashMap<Integer, BigDecimal>();
		// get data from intent
		Intent intent = getIntent();

		mBaseColor = intent.getIntExtra("color", NO_COLOR);
		mBaseColorModel = new ColorModel(mBaseColor);
		if (mBaseColor == NO_COLOR) {
			Log.d("wer", "no color base model");
			mBaseHue = intent.getFloatExtra("hue", 0.0f);
			mBaseSat = intent.getFloatExtra("sat", 0.0f);
			mBaseVal = intent.getFloatExtra("val", 0.0f);
		} else {
			Log.d("laiwe", "else base model");
			mBaseHue = round(mBaseColorModel.getHue(), 0);
			mBaseSat = round(mBaseColorModel.getSaturation(), 2);
			mBaseVal = round(mBaseColorModel.getValue(), 2);
		}
		mBaseColorModel = new ColorModel(new float[] { mBaseHue, mBaseSat,
				mBaseVal });
		mBaseColor = mBaseColorModel.getColor();

		mCurrentHue = mBaseHue;
		mCurrentVal = mBaseVal;
		mCurrentSat = mBaseSat;
		mCurrentColor = mBaseColor;
		mBrowseBy = intent.getIntExtra("browse_by", 0);

		final ActionBar actionBar = getActionBar();
		// Specify that the Home button should show an "Up" caret, indicating
		// that touching the
		// button will take the user one step up in the application's hierarchy.
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Set up the ViewPager, attaching the adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mHSVPagerAdapter = new HSVPagerAdapter(getSupportFragmentManager());
		mHSVPagerAdapter.setColor(mBaseHue, mBaseSat, mBaseVal);
		mHSVPagerAdapter.setActivity(mColorPickerActivity);
		Log.d("awer", "received color: " + mBaseColor);
		mViewPager.setAdapter(mHSVPagerAdapter);

		// set mToastText, number of fragments
		switch (mBrowseBy) {
		case BY_HUE:
			COLOR_COUNT = 360;
			mHSVPagerAdapter.notifyDataSetChanged();
			mToastText = "Swipe to browse by HUE";
			break;
		case BY_SATURATION:
			COLOR_COUNT = 100;
			mHSVPagerAdapter.notifyDataSetChanged();
			mToastText = "Swipe to browse by SATURATION";
			break;
		case BY_VALUE:
			COLOR_COUNT = 100;
			mHSVPagerAdapter.notifyDataSetChanged();
			mToastText = "Swipe to browse by VALUE";
			break;
		default:
			COLOR_COUNT = 360;
			mHSVPagerAdapter.notifyDataSetChanged();
			mToastText = "Swipe to browse by HUE";
			break;
		}

		// generate FragmentMap, set current item
		mHSVPagerAdapter.generateMap();
		Log.d("awer", "pos map size: " + mColorToPosMap.size());
		switch (mBrowseBy) {
		case BY_HUE:
			mViewPager.setCurrentItem(mColorToPosMap.get(new BigDecimal(mBaseHue)));
			break;
		case BY_SATURATION:
			mViewPager.setCurrentItem(mColorToPosMap.get(new BigDecimal(mBaseSat)));
		case BY_VALUE:
			mViewPager.setCurrentItem(mColorToPosMap.get(new BigDecimal(mBaseVal)));
			break;
		default:
			mViewPager.setCurrentItem(mColorToPosMap.get(new BigDecimal(mBaseHue)));
			break;
		}


		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, mToastText, duration);
		toast.show();

		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						switch (mBrowseBy) {
						case BY_HUE:
							mCurrentHue = mPosToColorMap.get(position)
									.floatValue();
							break;
						case BY_SATURATION:
							mCurrentSat = mPosToColorMap.get(position)
									.floatValue();
						case BY_VALUE:
							mCurrentVal = mPosToColorMap.get(position)
									.floatValue();
							break;
						default:
							mCurrentHue = mPosToColorMap.get(position)
									.floatValue();
							break;
						}
						Log.d("tqo", "current pos : "+mPosToColorMap.get(position));
						
						
					}
				});
	}
	
	// GETTERS
	public HashMap<Integer, BigDecimal> getPosToColorMap(){
		return mPosToColorMap;
	}
	
	public HashMap<BigDecimal, Integer> getColorToPosMap(){
		return mColorToPosMap;
	}
	
	public HashMap<Integer, String> getPosToHexMap(){
		return mPosToHexMap;
	}
	
	public float getBaseHue(){
		return mBaseHue;
	}
	
	public float getBaseSat(){
		return mBaseSat;
	}
	
	public float getBaseVal(){
		return mBaseVal;
	}
	
	public ColorModel getBaseColorModel(){
		return mBaseColorModel;
	}
	
	public float getCurrentHue(){
		return mCurrentHue;
	}
	
	public float getCurrentSat(){
		return mCurrentSat;
	}
	
	public float getCurrentVal(){
		return mCurrentVal;
	}
	
	public int getCurrentColor(){
		return mCurrentColor;
	}
	
	public int getBrowseBy(){
		return mBrowseBy;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.color_picker_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This is called when the Home (Up) button is pressed in the action
			// bar.
			// Create a simple intent that starts the hierarchical parent
			// activity and
			// use NavUtils in the Support Package to ensure proper handling of
			// Up.
			moveTaskBack();
			return true;
		case R.id.action_by_hue:
			Intent hueIntent = new Intent(mColorPickerActivity,
					ColorPickerActivity.class);
			hueIntent.putExtra("hue", mCurrentHue);
			hueIntent.putExtra("sat", mBaseSat);
			hueIntent.putExtra("val", mBaseVal);
			hueIntent.putExtra("browse_by", BY_HUE);
			startActivity(hueIntent);
			return true;
		case R.id.action_by_saturation:
			Intent saturationIntent = new Intent(mColorPickerActivity,
					ColorPickerActivity.class);
			saturationIntent.putExtra("hue", mBaseHue);
			saturationIntent.putExtra("sat", mCurrentSat);
			saturationIntent.putExtra("val", mBaseVal);
			saturationIntent.putExtra("browse_by", BY_SATURATION);
			startActivity(saturationIntent);
			return true;
		case R.id.action_by_value:
			Intent valueIntent = new Intent(mColorPickerActivity,
					ColorPickerActivity.class);
			valueIntent.putExtra("hue", mBaseHue);
			valueIntent.putExtra("sat", mBaseSat);
			valueIntent.putExtra("val", mCurrentVal);
			valueIntent.putExtra("browse_by", BY_VALUE);
			startActivity(valueIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		moveTaskBack();
	}

	private void moveTaskBack() {
		if (mBrowseBy != 0) {
			Intent upIntent = new Intent(this, MainActivity.class);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				// This activity is not part of the application's task, so
				// create a new task
				// with a synthesized back stack.
				TaskStackBuilder.create(this)
				// If there are ancestor activities, they should be added here.
						.addNextIntent(upIntent).startActivities();
				finish();
			} else {
				// This activity is part of the application's task, so simply
				// navigate up to the hierarchical parent activity.
				NavUtils.navigateUpTo(this, upIntent);
			}

		} else {
			super.onBackPressed();
		}
	}

	public static float round(float value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.floatValue();
	}

	/**
	 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a
	 * fragment representing an object in the collection.
	 */
	public static class HSVPagerAdapter extends FragmentPagerAdapter {

		private float mAdaptHue;
		private float mAdaptSat;
		private float mAdaptVal;
		private ColorPickerActivity mActivity;

		public HSVPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public void setColor(float h, float s, float v) {
			mAdaptHue = h;
			mAdaptSat = s;
			mAdaptVal = v;
		}
		
		public void setActivity(ColorPickerActivity ca){
			mActivity = ca;
		}
		
		public void generateMap() {
			// HashMap<String, ColorObjectFragment> map = new HashMap<String,
			// ColorObjectFragment>();
			for (int pos = 0; pos <= COLOR_COUNT; pos++) {
				getItem(pos);
			}
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new ColorObjectFragment();
			Bundle args = new Bundle();
			setNextColor(position);
			args.putFloat(ColorObjectFragment.ARG_HUE,
					mAdaptHue);
			args.putFloat(ColorObjectFragment.ARG_VAL,
					mAdaptVal);
			args.putFloat(ColorObjectFragment.ARG_SAT,
					mAdaptSat);
			fragment.setArguments(args);
			mActivity.getPosToHexMap().put(position, (new ColorModel(new float[]{mAdaptHue, mAdaptSat, mAdaptVal})).getHexCode());
			
			return fragment;
		}

		private void setNextColor(int position) {
			switch (mActivity.getBrowseBy()) {
			case BY_VALUE:
				if (mAdaptVal == round(position *0.01f, 2)){
					mAdaptVal = round((position+1) *0.01f, 2);
				}else{
					mAdaptVal = round(position *0.01f, 2);
				}
				mActivity.getColorToPosMap().put(new BigDecimal(mAdaptVal), position);
				mActivity.getPosToColorMap().put(position, new BigDecimal(mAdaptVal));
				break;
			case BY_SATURATION:
				if (mAdaptSat == round(position *0.01f, 2)){
					mAdaptSat = round((position+1) *0.01f, 2);
				}else{
					mAdaptSat = round(position *0.01f, 2);
				}
				mActivity.getColorToPosMap().put(new BigDecimal(mAdaptSat), position);
				mActivity.getPosToColorMap().put(position, new BigDecimal(mAdaptSat));
				break;
			case BY_HUE:
				mAdaptHue = position;
				mActivity.getColorToPosMap().put(new BigDecimal(mAdaptHue), position);
				mActivity.getPosToColorMap().put(position, new BigDecimal(mAdaptHue));
				break;
			default:
				mAdaptHue = position;
				mActivity.getColorToPosMap().put(new BigDecimal(mAdaptHue), position);
				mActivity.getPosToColorMap().put(position, new BigDecimal(mAdaptHue));
				break;
			}
		}

		@Override
		public int getCount() {
			return COLOR_COUNT;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position == 0) {
				return mActivity.getBaseColorModel().getHexCode();
			}
			if (mActivity.getPosToHexMap().get(position) == null) {
				getItem(position);
			}
			return mActivity.getPosToHexMap().get(position);
		}
	}

	/**
	 * A fragment representing a color.
	 */
	public static class ColorObjectFragment extends Fragment {

		public static final String ARG_OBJECT = "object";
		public static final String ARG_COLOR_INT = "color_int";
		public static final String ARG_HUE = "hue";
		public static final String ARG_SAT = "saturation";
		public static final String ARG_VAL = "value";
		private int mFragmentColor;
		private ColorModel mFragmentColorModel;
		private TextView mTextView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_colorpicker_object, container, false);
			Bundle args = getArguments();
			
			float hue =  args.getFloat(ARG_HUE);
			float sat =  args.getFloat(ARG_SAT);
			float val = args.getFloat(ARG_VAL);
			mFragmentColorModel = new ColorModel(new float[]{hue,sat,val});
			mFragmentColor = mFragmentColorModel.getColor();

			int[] rgb = mFragmentColorModel.getRGB();
			float[] cmyk = mFragmentColorModel.getCMYK();
			
			mTextView = (TextView) rootView.findViewById(android.R.id.text1);
			mTextView.setBackgroundColor(mFragmentColor);
			int textColor = mFragmentColorModel.getValue() > .6f
					&& mFragmentColorModel.getSaturation() < .5f ? Color.BLACK
					: Color.WHITE;
			mTextView.setTextColor(textColor);
			mTextView.setText(mFragmentColorModel.getHexCode() + "\n RGB: \n"
					+ rgb[0] + ", " + rgb[1] + ", " + rgb[2] + "\n"
					+ "CMYK: \n" + round(cmyk[0], 2) + ", " + round(cmyk[1], 2)
					+ ", " + round(cmyk[2], 2) + ", " + round(cmyk[3], 2)
					+ "\n" + "HSV: \n" + hue + ", "
					+ sat + ", " + val);

			return rootView;
		}

		public String getHexCode() {
			return mFragmentColorModel.getHexCode();
		}

		private static float round(float value, int places) {
			if (places < 0)
				throw new IllegalArgumentException();

			BigDecimal bd = new BigDecimal(value);
			bd = bd.setScale(places, RoundingMode.HALF_UP);
			return bd.floatValue();
		}
	}
}
