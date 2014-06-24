package com.example.scheme;

import android.app.ActionBar;
import android.content.Intent;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ColorPickerActivity2 extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments representing each object in a collection. We use a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter} derivative,
	 * which will destroy and re-create fragments as needed, saving and
	 * restoring their state in the process. This is important to conserve
	 * memory and is a best practice when allowing navigation between objects in
	 * a potentially large collection.
	 */
	HuePagerAdapter mHueCollectionPagerAdapter;

	/**
	 * The {@link android.support.v4.view.ViewPager} that will display the
	 * object collection.
	 */
	ViewPager mViewPager;
	private int mColor;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_color_picker_activity2);
		
		Intent intent = getIntent();
		mColor = intent.getIntExtra("color", 0);

		// Create an adapter that when requested, will return a fragment
		// representing an object in
		// the collection.
		//
		// ViewPager and its adapters use support library fragments, so we must
		// use
		// getSupportFragmentManager.
		mHueCollectionPagerAdapter = new HuePagerAdapter(
				getSupportFragmentManager());
		//mDemoCollectionPagerAdapter.setColor(mColor);
		// Set up action bar.
		final ActionBar actionBar = getActionBar();

		// Specify that the Home button should show an "Up" caret, indicating
		// that touching the
		// button will take the user one step up in the application's hierarchy.
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Set up the ViewPager, attaching the adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mHueCollectionPagerAdapter);
		mViewPager.setCurrentItem(mColor);
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
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a
	 * fragment representing an object in the collection.
	 */
	public static class HuePagerAdapter extends
			FragmentStatePagerAdapter {

		// private int mColor;
		// private ColorModel mColorModel;

		public HuePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		/*
		 * public void setColor(int color){ mColor = color; mColorModel = new
		 * ColorModel(color);
		 * 
		 * }
		 */

		@Override
		public Fragment getItem(int color) {
			Fragment fragment = new DemoObjectFragment();
			Bundle args = new Bundle();
			ColorModel currentColorModel = new ColorModel(color);
			float[] hsv_temp = new float[] { currentColorModel.getHue() + 10.0f,
					currentColorModel.getSaturation(),
					currentColorModel.getValue() };
			ColorModel nextColorModel = new ColorModel(hsv_temp);
			Log.d("nextcolor", "nextcolor hue: "+nextColorModel.getHue());
			args.putInt(DemoObjectFragment.ARG_COLOR_INT,
					nextColorModel.getColor()); // Our object is just an integer
												// :-P\
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// For this contrived example, we have a 100-object collection.
			return 36;
		}

		@Override
		public CharSequence getPageTitle(int color) {
			ColorModel currentColorModel = new ColorModel(color);
			float[] hsv_temp = new float[] { currentColorModel.getHue() + 10.0f,
					currentColorModel.getSaturation(),
					currentColorModel.getValue() };
			ColorModel nextColorModel = new ColorModel(hsv_temp);
			return nextColorModel.getHexCode();
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DemoObjectFragment extends Fragment {

		public static final String ARG_OBJECT = "object";
		public static final String ARG_COLOR_INT = "color_int";

		private int mColor;
		private ColorModel mColorModel;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_colorpicker_object, container, false);
			Bundle args = getArguments();
			mColor = args.getInt(ARG_COLOR_INT);
			mColorModel = new ColorModel(mColor);
			((TextView) rootView.findViewById(android.R.id.text1))
					.setBackgroundColor(mColor);

			return rootView;
		}
	}
}