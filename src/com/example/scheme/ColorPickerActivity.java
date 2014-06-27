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
	
	public static int COLOR_COUNT;
	
	public static HashMap<Integer, String> mPosToHexMap;
	public static HashMap<Integer, Integer> mColorToPosMap;
	public static HashMap<Integer, Integer> mPosToColorMap;
	private HSVPagerAdapter mHSVPagerAdapter;
	private Activity mColorPickerActivity;
	ViewPager mViewPager;
	private int mBaseColor;
	private static ColorModel mBaseColorModel;
	private static Integer mCurrentColor;
	private CharSequence mToastText;
	private static int mBrowseBy;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mColorPickerActivity = this;
		setContentView(R.layout.fragment_color_picker_activity2);
		mPosToHexMap = new HashMap<Integer, String>();
		mColorToPosMap = new HashMap<Integer, Integer>();
		mPosToColorMap = new HashMap<Integer, Integer>();
		// get data from intent
		Intent intent = getIntent();
		mBaseColor = intent.getIntExtra("color", 0);
		mBaseColorModel = new ColorModel(mBaseColor);
		mBrowseBy = intent.getIntExtra("browse_by", 0);

		final ActionBar actionBar = getActionBar();
		// Specify that the Home button should show an "Up" caret, indicating
		// that touching the
		// button will take the user one step up in the application's hierarchy.
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Set up the ViewPager, attaching the adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mHSVPagerAdapter = new HSVPagerAdapter(getSupportFragmentManager());
		mHSVPagerAdapter.setColor(mBaseColor);
		Log.d("awer", "received color: "+mBaseColor);
		mViewPager.setAdapter(mHSVPagerAdapter);
		
		// set mToastText, number of fragments
		switch (mBrowseBy) {
		case BY_HUE:
			COLOR_COUNT = 360;
			mHSVPagerAdapter.notifyDataSetChanged();
			mToastText = "Swipe to browse by HUE";
			break;
		case BY_SATURATION:
			COLOR_COUNT = 101;
			mHSVPagerAdapter.notifyDataSetChanged();
			mToastText = "Swipe to browse by SATURATION";
			break;
		case BY_VALUE:
			COLOR_COUNT = 101;
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
				Log.d("awer", "pos map size: "+mColorToPosMap.size());
				mViewPager.setCurrentItem(mColorToPosMap.get(mBaseColor));
				Log.d("awer", "pos: "+mColorToPosMap.get(mBaseColor));

				
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, mToastText, duration);
		toast.show();
		
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			@Override
			public void onPageSelected(int position){
				mCurrentColor = mPosToColorMap.get(position);
				Log.d("awer", "current color: "+mCurrentColor);
			}
		});
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
			hueIntent.putExtra("color", mCurrentColor);
			Log.d("awer", "sent color: "+mCurrentColor);
			hueIntent.putExtra("browse_by", BY_HUE);
			startActivity(hueIntent);
			return true;
		case R.id.action_by_saturation:
			Intent saturationIntent = new Intent(mColorPickerActivity,
					ColorPickerActivity.class);
			saturationIntent.putExtra("color", mCurrentColor);
			Log.d("awer", "sent color: "+mCurrentColor);
			saturationIntent.putExtra("browse_by", BY_SATURATION);
			startActivity(saturationIntent);
			return true;
		case R.id.action_by_value:
			Intent valueIntent = new Intent(mColorPickerActivity,
					ColorPickerActivity.class);
			valueIntent.putExtra("color", mCurrentColor);
			Log.d("awer", "sent color: "+mCurrentColor);
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
	
	private void moveTaskBack(){
		if (mBrowseBy != 0){
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
	
	}else{
		super.onBackPressed();
	}
	}
	
	/**
	 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a
	 * fragment representing an object in the collection.
	 */
	public static class HSVPagerAdapter extends FragmentPagerAdapter {

		private ColorModel mAdapterColorModel;

		public HSVPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public void setColor(int base_color) {
			mAdapterColorModel = new ColorModel(base_color);
		}
		
		public ColorModel getCurrentColor(){
			return mAdapterColorModel;
		}
		
		
		public void generateMap(){
			//HashMap<String, ColorObjectFragment> map = new HashMap<String, ColorObjectFragment>();
			for (int pos=0; pos <= COLOR_COUNT; pos++){
				getItem(pos);
			}
		}
		
		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new ColorObjectFragment();
			Bundle args = new Bundle();
			setNextColor(position);
			args.putInt(ColorObjectFragment.ARG_COLOR_INT,
					mAdapterColorModel.getColor());
			fragment.setArguments(args);
			mPosToHexMap.put(position, mAdapterColorModel.getHexCode());
			mColorToPosMap.put(mAdapterColorModel.getColor(), position);
			mPosToColorMap.put(position, mAdapterColorModel.getColor());
			return fragment;
		}
		
		private void setNextColor(int position){
			float[] hsv_temp;
			switch(mBrowseBy){
			case BY_VALUE:
				hsv_temp = new float[] { mBaseColorModel.getHue(),
						mBaseColorModel.getSaturation(), position*0.01f };
				break;
			case BY_SATURATION:
				hsv_temp = new float[] { mBaseColorModel.getHue(),
						position*0.01f, mBaseColorModel.getValue() };
				break;
			default:
				hsv_temp = new float[] { position,
						mBaseColorModel.getSaturation(), mBaseColorModel.getValue() };
				break;
			}
			mAdapterColorModel = new ColorModel(hsv_temp);
		}

		@Override
		public int getCount() {
			return COLOR_COUNT;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position == 0){
				return mBaseColorModel.getHexCode();
			}
			if (mPosToHexMap.get(position) == null){
				getItem(position);
			}
			return mPosToHexMap.get(position);
		}
	}


	/**
	 * A fragment representing a color.
	 */
	public static class ColorObjectFragment extends Fragment {

		public static final String ARG_OBJECT = "object";
		public static final String ARG_COLOR_INT = "color_int";

		private int mFragmentColor;
		private ColorModel mFragmentColorModel;
		private TextView mTextView;
		private int mPosition;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_colorpicker_object, container, false);
			Bundle args = getArguments();
			mFragmentColor = args.getInt(ARG_COLOR_INT);
			mFragmentColorModel = new ColorModel(mFragmentColor);
			int[] rgb = mFragmentColorModel.getRGB();
			float[] cmyk = mFragmentColorModel.getCMYK();
			float[] hsv = mFragmentColorModel.getHSV();

			mTextView = (TextView) rootView.findViewById(android.R.id.text1);
			mTextView.setBackgroundColor(mFragmentColor);
			int textColor = mFragmentColorModel.getValue() > .6f && mFragmentColorModel.getSaturation() < .5f? Color.BLACK
					: Color.WHITE;
			mTextView.setTextColor(textColor);
			mTextView.setText(mFragmentColorModel.getHexCode()+"\n RGB: \n" + rgb[0] + ", " + rgb[1] + ", "
					+ rgb[2] + "\n" + "CMYK: \n" + round(cmyk[0],2) + ", " + round(cmyk[1],2)
					+ ", " + round(cmyk[2],2) + ", " + round(cmyk[3],2) + "\n" + "HSV: \n"
					+ round(hsv[0],0) + ", " + round(hsv[1],2) + ", " + round(hsv[2],2));

			return rootView;
		}
		
		public String getHexCode(){
			return mFragmentColorModel.getHexCode();
		}
		
		private static float round(float value, int places) {
		    if (places < 0) throw new IllegalArgumentException();

		    BigDecimal bd = new BigDecimal(value);
		    bd = bd.setScale(places, RoundingMode.HALF_UP);
		    return bd.floatValue();
		}
	}
}
