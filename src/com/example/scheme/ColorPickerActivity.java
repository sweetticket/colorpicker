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
	
	public static HashMap<Integer, String> mHexCodeMap = new HashMap<Integer, String>();
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments representing each object in a collection. We use a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter} derivative,
	 * which will destroy and re-create fragments as needed, saving and
	 * restoring their state in the process. This is important to conserve
	 * memory and is a best practice when allowing navigation between objects in
	 * a potentially large collection.
	 */
	private HSVPagerAdapter mHSVPagerAdapter;
	private Activity mColorPickerActivity;

	/**
	 * The {@link android.support.v4.view.ViewPager} that will display the
	 * object collection.
	 */
	ViewPager mViewPager;
	private int mColor;
	private ColorModel mColorModel;
	private CharSequence mToastText;
	private static int mBrowseBy;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mColorPickerActivity = this;
		setContentView(R.layout.fragment_color_picker_activity2);
		Intent intent = getIntent();

		mColor = intent.getIntExtra("color", 0);
		mColorModel = new ColorModel(mColor);
		
		mBrowseBy = intent.getIntExtra("browse_by", 0);

		// Set up action bar.
		final ActionBar actionBar = getActionBar();
		// Specify that the Home button should show an "Up" caret, indicating
		// that touching the
		// button will take the user one step up in the application's hierarchy.
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Set up the ViewPager, attaching the adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mHSVPagerAdapter = new HSVPagerAdapter(getSupportFragmentManager());
		mHSVPagerAdapter.setColor(mColor);
		Log.d("awer", "received color: "+mColorModel.getHexCode());
		mViewPager.setAdapter(mHSVPagerAdapter);
		switch (mBrowseBy) {
		case BY_HUE:
			mViewPager.setCurrentItem(Math.round(mColorModel.getHue()));
			mToastText = "Swipe to browse by HUE";
			break;
		case BY_SATURATION:
			mViewPager.setCurrentItem(Math.round(mColorModel.getSaturation() * 100.0f));
			mToastText = "Swipe to browse by SATURATION";
			break;
		case BY_VALUE:
			mViewPager.setCurrentItem(Math.round(mColorModel.getValue() * 100.0f));
			mToastText = "Swipe to browse by VALUE";
			break;
		default:
			mViewPager.setCurrentItem(Math.round(mColorModel.getHue()));
			mToastText = "Swipe to browse by HUE";
			break;
		}
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, mToastText, duration);
		toast.show();
		
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			@Override
			public void onPageSelected(int position){
				mColorModel = mHSVPagerAdapter.getCurrentColor();
				mColor = mColorModel.getColor();
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
			hueIntent.putExtra("color", mColor);
			Log.d("awer", "sent color: "+mColorModel.getHexCode());
			hueIntent.putExtra("browse_by", BY_HUE);
			startActivity(hueIntent);
			return true;
		case R.id.action_by_saturation:
			Intent saturationIntent = new Intent(mColorPickerActivity,
					ColorPickerActivity.class);
			saturationIntent.putExtra("color", mColor);
			Log.d("awer", "sent color: "+mColorModel.getHexCode());
			saturationIntent.putExtra("browse_by", BY_SATURATION);
			startActivity(saturationIntent);
			return true;
		case R.id.action_by_value:
			Intent valueIntent = new Intent(mColorPickerActivity,
					ColorPickerActivity.class);
			valueIntent.putExtra("color", mColor);
			Log.d("awer", "sent color: "+mColorModel.getHexCode());
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
	public static class HSVPagerAdapter extends FragmentStatePagerAdapter {

		private ColorModel mAdapterColorModel;

		public HSVPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public void setColor(int color) {
			mAdapterColorModel = new ColorModel(color);
		}
		
		public ColorModel getCurrentColor(){
			return mAdapterColorModel;
		}
		
		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new ColorObjectFragment();
			Bundle args = new Bundle();
			setNextColor(position);
			args.putInt(ColorObjectFragment.ARG_COLOR_INT,
					mAdapterColorModel.getColor());
			fragment.setArguments(args);
			mHexCodeMap.put(position, mAdapterColorModel.getHexCode());
			if(mHexCodeMap.containsKey(position+2)){
				mHexCodeMap.remove(position+2);
			}
			if(mHexCodeMap.containsKey(position-2)){
				mHexCodeMap.remove(position-2);
			}
			return fragment;
		}
		
		private void setNextColor(int position){
			float[] hsv_temp;
			switch(mBrowseBy){
			case BY_VALUE:
				hsv_temp = new float[] { mAdapterColorModel.getHue(),
						mAdapterColorModel.getSaturation(), position * 0.01f };
				break;
			case BY_SATURATION:
				hsv_temp = new float[] { mAdapterColorModel.getHue(),
						position%101 * 0.01f, mAdapterColorModel.getValue() };
				break;
			default:
				hsv_temp = new float[] { position%360,
						mAdapterColorModel.getSaturation(), mAdapterColorModel.getValue() };
				break;
			}
			mAdapterColorModel = new ColorModel(hsv_temp);
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position == 0){
				return mAdapterColorModel.getHexCode();
			}
			if (mHexCodeMap.get(position) == null){
				getItem(position);
			}
			return mHexCodeMap.get(position);
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
