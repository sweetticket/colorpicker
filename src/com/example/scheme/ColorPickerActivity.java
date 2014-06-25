package com.example.scheme;

import com.example.scheme.R;

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

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments representing each object in a collection. We use a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter} derivative,
	 * which will destroy and re-create fragments as needed, saving and
	 * restoring their state in the process. This is important to conserve
	 * memory and is a best practice when allowing navigation between objects in
	 * a potentially large collection.
	 */
	private HuePagerAdapter mHuePagerAdapter;
	private SaturationPagerAdapter mSaturationPagerAdapter;
	private ValuePagerAdapter mValuePagerAdapter;
	private Activity mColorPickerActivity;

	/**
	 * The {@link android.support.v4.view.ViewPager} that will display the
	 * object collection.
	 */
	ViewPager mViewPager;
	private int mColor;
	private ColorModel mColorModel;
	private CharSequence mToastText;
	private int mBrowseBy;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mColorPickerActivity = this;
		setContentView(R.layout.fragment_color_picker_activity2);

		Intent intent = getIntent();

		mColor = intent.getIntExtra("color", 0);
		mColorModel = new ColorModel(mColor);

		mBrowseBy = intent.getIntExtra("browse_by", BY_HUE);

		// Create an adapter that when requested, will return a fragment
		// representing an object in
		// the collection.
		//
		// ViewPager and its adapters use support library fragments, so we must
		// use
		// getSupportFragmentManager.

		// Set up action bar.
		final ActionBar actionBar = getActionBar();
		// Specify that the Home button should show an "Up" caret, indicating
		// that touching the
		// button will take the user one step up in the application's hierarchy.
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Set up the ViewPager, attaching the adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);

		switch (mBrowseBy) {
		case BY_HUE:
			mHuePagerAdapter = new HuePagerAdapter(getSupportFragmentManager());
			mHuePagerAdapter.setColor(mColor);
			mViewPager.setAdapter(mHuePagerAdapter);
			mViewPager.setCurrentItem(Math.round(mColorModel.getHue()));
			mToastText = "Swipe to browse by HUE";
			break;
		case BY_SATURATION:
			mSaturationPagerAdapter = new SaturationPagerAdapter(
					getSupportFragmentManager());
			mSaturationPagerAdapter.setColor(mColor);
			mViewPager.setAdapter(mSaturationPagerAdapter);
			mViewPager
					.setCurrentItem(Math.round(mColorModel.getSaturation() * 100));
			mToastText = "Swipe to browse by SATURATION";
			break;
		case BY_VALUE:
			mValuePagerAdapter = new ValuePagerAdapter(
					getSupportFragmentManager());
			mValuePagerAdapter.setColor(mColor);
			mViewPager.setAdapter(mValuePagerAdapter);
			mViewPager.setCurrentItem(Math.round(mColorModel.getValue() * 100));
			mToastText = "Swipe to browse by VALUE";
			break;
		}
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, mToastText, duration);
		toast.show();
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
			return true;
		case R.id.action_by_hue:
			Intent hueIntent = new Intent(mColorPickerActivity,
					ColorPickerActivity.class);
			hueIntent.putExtra("color", mColor);
			hueIntent.putExtra("browse_by", BY_HUE);
			startActivity(hueIntent);
			return true;
		case R.id.action_by_saturation:
			Intent saturationIntent = new Intent(mColorPickerActivity,
					ColorPickerActivity.class);
			saturationIntent.putExtra("color", mColor);
			saturationIntent.putExtra("browse_by", BY_SATURATION);
			startActivity(saturationIntent);
			return true;
		case R.id.action_by_value:
			Intent valueIntent = new Intent(mColorPickerActivity,
					ColorPickerActivity.class);
			valueIntent.putExtra("color", mColor);
			valueIntent.putExtra("browse_by", BY_VALUE);
			startActivity(valueIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a
	 * fragment representing an object in the collection.
	 */
	public static class HuePagerAdapter extends FragmentStatePagerAdapter {

		private ColorModel mColorModel;

		public HuePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public void setColor(int color) {
			mColorModel = new ColorModel(color);
		}

		@Override
		public Fragment getItem(int hue) {
			Fragment fragment = new ColorObjectFragment();
			Bundle args = new Bundle();
			float[] hsv_temp = new float[] { hue + 1.0f,
					mColorModel.getSaturation(), mColorModel.getValue() };
			ColorModel nextColorModel = new ColorModel(hsv_temp);
			Log.d("nextcolor", "nextcolor hue: " + nextColorModel.getHue());
			args.putInt(ColorObjectFragment.ARG_COLOR_INT,
					nextColorModel.getColor());
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 360;
		}

		@Override
		public CharSequence getPageTitle(int hue) {
			float[] hsv_temp = new float[] { hue + 1.0f,
					mColorModel.getSaturation(), mColorModel.getValue() };
			ColorModel nextColorModel = new ColorModel(hsv_temp);
			Log.d("nextcolor", "nextcolor hue: " + nextColorModel.getHue());
			return nextColorModel.getHexCode();
		}
	}

	public static class SaturationPagerAdapter extends
			FragmentStatePagerAdapter {

		private ColorModel mColorModel;

		public SaturationPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public void setColor(int color) {
			mColorModel = new ColorModel(color);

		}

		@Override
		public Fragment getItem(int saturation) {
			Fragment fragment = new ColorObjectFragment();
			Bundle args = new Bundle();
			float[] hsv_temp = new float[] { mColorModel.getHue(),
					(saturation + 1) * 0.01f, mColorModel.getValue() };
			ColorModel nextColorModel = new ColorModel(hsv_temp);
			args.putInt(ColorObjectFragment.ARG_COLOR_INT,
					nextColorModel.getColor());
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 100;
		}

		@Override
		public CharSequence getPageTitle(int saturation) {
			float[] hsv_temp = new float[] { mColorModel.getHue(),
					(saturation + 1) * 0.01f, mColorModel.getValue() };
			ColorModel nextColorModel = new ColorModel(hsv_temp);

			return nextColorModel.getHexCode();
		}
	}

	public static class ValuePagerAdapter extends FragmentStatePagerAdapter {

		private ColorModel mColorModel;

		public ValuePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public void setColor(int color) {
			mColorModel = new ColorModel(color);

		}

		@Override
		public Fragment getItem(int value) {
			Fragment fragment = new ColorObjectFragment();
			Bundle args = new Bundle();
			float[] hsv_temp = new float[] { mColorModel.getHue(),
					mColorModel.getSaturation(), (value + 1) * 0.01f };
			ColorModel nextColorModel = new ColorModel(hsv_temp);
			args.putInt(ColorObjectFragment.ARG_COLOR_INT,
					nextColorModel.getColor());
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 100;
		}

		@Override
		public CharSequence getPageTitle(int value) {
			float[] hsv_temp = new float[] { mColorModel.getHue(),
					mColorModel.getSaturation(), (value + 1) * 0.01f };
			ColorModel nextColorModel = new ColorModel(hsv_temp);

			return nextColorModel.getHexCode();
		}
	}

	/**
	 * A fragment representing a color.
	 */
	public static class ColorObjectFragment extends Fragment {

		public static final String ARG_OBJECT = "object";
		public static final String ARG_COLOR_INT = "color_int";

		private int mColor;
		private ColorModel mColorModel;
		private TextView mTextView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_colorpicker_object, container, false);
			Bundle args = getArguments();
			mColor = args.getInt(ARG_COLOR_INT);
			mColorModel = new ColorModel(mColor);
			int[] rgb = mColorModel.getRGB();
			float[] cmyk = mColorModel.getCMYK();
			float[] hsv = mColorModel.getHSV();

			mTextView = (TextView) rootView.findViewById(android.R.id.text1);
			mTextView.setBackgroundColor(mColor);
			int textColor = mColorModel.getValue() > .5f ? Color.BLACK
					: Color.WHITE;
			mTextView.setTextColor(textColor);
			mTextView.setText("RGB: \n" + rgb[0] + ", " + rgb[1] + ", "
					+ rgb[2] + "\n" + "CMYK: \n" + cmyk[0] + ", " + cmyk[1]
					+ ", " + cmyk[2] + ", " + cmyk[3] + "\n" + "HSV: \n"
					+ hsv[0] + ", " + hsv[1] + ", " + hsv[2]);

			return rootView;
		}
	}
}
