package com.example.scheme;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ColorPickerActivity extends FragmentActivity implements
		AdjustDialogFragment.AdjustDialogListener,
		PaletteDialogFragment.PaletteDialogListener,
		NewPaletteDialogFragment.NewPaletteDialogListener {

	public final static int BY_HUE = 9035;
	public final static int BY_SATURATION = 2039;
	public final static int BY_VALUE = 5893;
	public final static int NO_COLOR = 230958;
	public static int COLOR_COUNT;

	public HashMap<Integer, String> mPosToHexMap;
	public HashMap<BigDecimal, Integer> mColorToPosMap;
	public HashMap<Integer, BigDecimal> mPosToColorMap;
	private HSVPagerAdapter mHSVPagerAdapter;
	private static ColorPickerActivity mColorPickerActivity;
	ViewPager mViewPager;
	private int mBaseColor;
	private ColorModel mBaseColorModel;
	private float mBaseHue;
	private float mBaseSat;
	private float mBaseVal;

	private float mCurrentHue;
	private float mCurrentSat;
	private float mCurrentVal;
	private CharSequence mToastText;
	private int mBrowseBy;
	private SlidingPanel mBottomPanel;
	private SlidingPanel mTopPanel;

	private Button mTriadButton;
	private Button mAnalogButton;
	private Button mMonoButton;
	private Button mCompButton;
	private Button mSplitCompButton;

	private SlidingPanel mPanel1;
	private SlidingPanel mPanel2;
	private SlidingPanel mPanel3;

	private SlidingPanel mPanel4;
	private SlidingPanel mPanel5;
	private SlidingPanel mPanel6;
	private SlidingPanel mPanel7;
	private SlidingPanel mPanel8;

	private SlidingPanel mPanel9;
	private SlidingPanel mPanel10;

	private SlidingPanel[] mAllPanels;

	private ObjectPreference mObjectPref;
	private ComplexPreferences mComplexPrefs;

	private boolean mBackToPaletteDialog = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mColorPickerActivity = this;
		setContentView(R.layout.fragment_color_picker_activity);

		mObjectPref = (ObjectPreference) this.getApplication();
		mComplexPrefs = mObjectPref.getComplexPreference();

		mBottomPanel = (SlidingPanel) findViewById(R.id.scheme_bottom_panel);
		mTopPanel = (SlidingPanel) findViewById(R.id.scheme_top_panel);
		mPosToHexMap = new HashMap<Integer, String>();
		mColorToPosMap = new HashMap<BigDecimal, Integer>();
		mPosToColorMap = new HashMap<Integer, BigDecimal>();
		// get data from intent
		Intent intent = getIntent();

		mBaseColor = intent.getIntExtra("color", NO_COLOR);
		mBaseColorModel = new ColorModel(mBaseColor);
		if (mBaseColor == NO_COLOR) {
			mBaseHue = intent.getFloatExtra("hue", 0.0f);
			mBaseSat = intent.getFloatExtra("sat", 0.0f);
			mBaseVal = intent.getFloatExtra("val", 0.0f);
		} else {
			mBaseHue = round(mBaseColorModel.getHue(), 0);
			mBaseSat = round(mBaseColorModel.getSaturation(), 2);
			mBaseVal = round(mBaseColorModel.getValue(), 2);
		}
		mBaseColorModel = new ColorModel(new float[] { mBaseHue, mBaseSat,
				mBaseVal });

		mCurrentHue = mBaseHue;
		mCurrentVal = mBaseVal;
		mCurrentSat = mBaseSat;
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
		mViewPager.setAdapter(mHSVPagerAdapter);

		// set mToastText, number of fragments
		switch (mBrowseBy) {
		case BY_HUE:
			setTitle("Browse Hues");
			COLOR_COUNT = 360;
			mHSVPagerAdapter.notifyDataSetChanged();
			mToastText = "Swipe to browse by HUE";
			break;
		case BY_SATURATION:
			setTitle("Browse Saturations");
			COLOR_COUNT = 101;
			mHSVPagerAdapter.notifyDataSetChanged();
			mToastText = "Swipe to browse by SATURATION";
			break;
		case BY_VALUE:
			setTitle("Browse Values");
			COLOR_COUNT = 101;
			mHSVPagerAdapter.notifyDataSetChanged();
			mToastText = "Swipe to browse by VALUE";
			break;
		default:
			setTitle("Browse Hues");
			COLOR_COUNT = 360;
			mHSVPagerAdapter.notifyDataSetChanged();
			mToastText = "Swipe to browse by HUE";
			break;
		}

		// generate FragmentMap, set current item
		mHSVPagerAdapter.generateMap();
		switch (mBrowseBy) {
		case BY_HUE:
			mViewPager.setCurrentItem(mColorToPosMap.get(new BigDecimal(
					mBaseHue)));
			break;
		case BY_SATURATION:
			mViewPager.setCurrentItem(mColorToPosMap.get(new BigDecimal(
					mBaseSat)));
			break;
		case BY_VALUE:
			mViewPager.setCurrentItem(mColorToPosMap.get(new BigDecimal(
					mBaseVal)));
			break;
		default:
			mViewPager.setCurrentItem(mColorToPosMap.get(new BigDecimal(
					mBaseHue)));
			break;
		}

		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, mToastText, duration);
		toast.show();

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
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
							break;
						case BY_VALUE:
							mCurrentVal = mPosToColorMap.get(position)
									.floatValue();
							break;
						default:
							mCurrentHue = mPosToColorMap.get(position)
									.floatValue();
							break;
						}
					}
				});

		initButtons();

	}

	public void showAdjustDialog() {
		DialogFragment dialog = new AdjustDialogFragment();

		((AdjustDialogFragment) dialog).setHue(mCurrentHue);
		((AdjustDialogFragment) dialog).setSat(mCurrentSat);
		((AdjustDialogFragment) dialog).setVal(mCurrentVal);

		dialog.show(getSupportFragmentManager(), "AdjustDialogFragment");
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		Intent sendColorIntent = new Intent(mColorPickerActivity,
				ColorPickerActivity.class);
		sendColorIntent.putExtra("color",
				Color.HSVToColor(((AdjustDialogFragment) dialog).getHSV()));
		startActivity(sendColorIntent);
		finish();

	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		dialog.dismiss();
	}

	public void toggleOptions() {
		mBottomPanel.toggle();
		mTopPanel.toggle();
	}

	public void initButtons() {
		mTriadButton = (Button) findViewById(R.id.triad);
		mTriadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorModel currentColor = new ColorModel(new float[] {
						mCurrentHue, mCurrentSat, mCurrentVal });
				int[] colorScheme = currentColor.getTriad();
				toggleScheme3(currentColor, colorScheme);
			}
		});

		mAnalogButton = (Button) findViewById(R.id.analog);
		mAnalogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorModel currentColor = new ColorModel(new float[] {
						mCurrentHue, mCurrentSat, mCurrentVal });
				int[] colorScheme = currentColor.getAnalog();
				toggleScheme3(currentColor, colorScheme);
			}

		});

		mSplitCompButton = (Button) findViewById(R.id.split_comp);
		mSplitCompButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorModel currentColor = new ColorModel(new float[] {
						mCurrentHue, mCurrentSat, mCurrentVal });
				int[] colorScheme = currentColor.getSplitComp();
				toggleScheme3(currentColor, colorScheme);
			}
		});

		mMonoButton = (Button) findViewById(R.id.mono);
		mMonoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorModel currentColor = new ColorModel(new float[] {
						mCurrentHue, mCurrentSat, mCurrentVal });
				int[] colorScheme = currentColor.getMonochrome();
				toggleScheme5(currentColor, colorScheme);
			}
		});

		mCompButton = (Button) findViewById(R.id.comp);
		mCompButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorModel currentColor = new ColorModel(new float[] {
						mCurrentHue, mCurrentSat, mCurrentVal });
				int comp = currentColor.getComplement();
				toggleScheme2(currentColor, comp);
			}
		});

	}

	public void toggleScheme3(ColorModel currentColor, int[] colorScheme) {
		mBottomPanel.toggle();
		mTopPanel.toggle();
		mPanel1 = (SlidingPanel) mColorPickerActivity
				.findViewById(R.id.fourth1);
		mPanel1.setBackgroundColor(colorScheme[0]);
		mPanel1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sendColorIntent = new Intent(mColorPickerActivity,
						ColorPickerActivity.class);
				sendColorIntent.putExtra("color", mPanel1.getBackgroundColor());
				startActivity(sendColorIntent);
				finish();

			}
		});
		mPanel3 = (SlidingPanel) mColorPickerActivity
				.findViewById(R.id.fourth2);
		mPanel3.setBackgroundColor(colorScheme[1]);
		mPanel3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sendColorIntent = new Intent(mColorPickerActivity,
						ColorPickerActivity.class);
				sendColorIntent.putExtra("color", mPanel3.getBackgroundColor());
				startActivity(sendColorIntent);
				finish();
			}
		});
		mPanel2 = (SlidingPanel) mColorPickerActivity
				.findViewById(R.id.fourth3);
		mPanel2.setBackgroundColor(currentColor.getColor());
		mPanel2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Handler toggleHandler = new Handler();
				toggleHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mPanel3.toggle();
					}
				}, 200);
				toggleHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mPanel2.toggle();
					}
				}, 400);
				toggleHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mPanel1.toggle();
					}
				}, 600);

			}
		});

		mAllPanels = new SlidingPanel[] { mPanel1, mPanel2, mPanel3 };

		Handler toggleHandler = new Handler();
		toggleHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mPanel1.toggle();
			}
		}, 200);
		toggleHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mPanel2.toggle();
			}
		}, 400);
		toggleHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mPanel3.toggle();
			}
		}, 600);

	}

	public void toggleScheme5(ColorModel currentColor, int[] colorScheme) {
		mBottomPanel.toggle();
		mTopPanel.toggle();

		mPanel4 = (SlidingPanel) mColorPickerActivity.findViewById(R.id.fifth1);
		mPanel4.setBackgroundColor(colorScheme[0]);
		mPanel4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sendColorIntent = new Intent(mColorPickerActivity,
						ColorPickerActivity.class);
				sendColorIntent.putExtra("color", mPanel4.getBackgroundColor());
				startActivity(sendColorIntent);
				finish();

			}
		});
		mPanel5 = (SlidingPanel) mColorPickerActivity.findViewById(R.id.fifth2);
		mPanel5.setBackgroundColor(colorScheme[1]);
		mPanel5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sendColorIntent = new Intent(mColorPickerActivity,
						ColorPickerActivity.class);
				sendColorIntent.putExtra("color", mPanel5.getBackgroundColor());
				startActivity(sendColorIntent);
				finish();
			}
		});

		mPanel6 = (SlidingPanel) mColorPickerActivity.findViewById(R.id.fifth3);
		mPanel6.setBackgroundColor(colorScheme[2]);
		mPanel6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sendColorIntent = new Intent(mColorPickerActivity,
						ColorPickerActivity.class);
				sendColorIntent.putExtra("color", mPanel6.getBackgroundColor());
				startActivity(sendColorIntent);
				finish();
			}
		});
		mPanel7 = (SlidingPanel) mColorPickerActivity.findViewById(R.id.fifth4);
		mPanel7.setBackgroundColor(colorScheme[3]);
		mPanel7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sendColorIntent = new Intent(mColorPickerActivity,
						ColorPickerActivity.class);
				sendColorIntent.putExtra("color", mPanel7.getBackgroundColor());
				startActivity(sendColorIntent);
				finish();
			}
		});
		mPanel8 = (SlidingPanel) mColorPickerActivity.findViewById(R.id.fifth5);
		mPanel8.setBackgroundColor(colorScheme[4]);
		mPanel8.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sendColorIntent = new Intent(mColorPickerActivity,
						ColorPickerActivity.class);
				sendColorIntent.putExtra("color", mPanel8.getBackgroundColor());
				startActivity(sendColorIntent);
				finish();
			}
		});

		mAllPanels = new SlidingPanel[] { mPanel4, mPanel5, mPanel6, mPanel7,
				mPanel8 };

		Handler toggleHandler = new Handler();
		toggleHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mPanel4.toggle();
			}
		}, 200);
		toggleHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mPanel5.toggle();
			}
		}, 400);
		toggleHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mPanel6.toggle();
			}
		}, 600);
		toggleHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mPanel7.toggle();
			}
		}, 800);
		toggleHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mPanel8.toggle();
			}
		}, 1000);

	}

	public void toggleScheme2(ColorModel currentColor, int comp) {
		mBottomPanel.toggle();
		mTopPanel.toggle();
		mPanel9 = (SlidingPanel) mColorPickerActivity.findViewById(R.id.half1);
		mPanel9.setBackgroundColor(comp);
		mPanel9.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sendColorIntent = new Intent(mColorPickerActivity,
						ColorPickerActivity.class);
				sendColorIntent.putExtra("color", mPanel9.getBackgroundColor());
				startActivity(sendColorIntent);
				finish();

			}
		});
		mPanel10 = (SlidingPanel) mColorPickerActivity.findViewById(R.id.half2);
		mPanel10.setBackgroundColor(currentColor.getColor());
		mPanel10.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Handler toggleHandler = new Handler();
				toggleHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mPanel10.toggle();
					}
				}, 200);
				toggleHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mPanel9.toggle();
					}
				}, 400);
			}
		});

		mAllPanels = new SlidingPanel[] { mPanel9, mPanel10 };

		Handler toggleHandler = new Handler();
		toggleHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mPanel9.toggle();
			}
		}, 500);
		toggleHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mPanel10.toggle();
			}
		}, 1000);

	}

	// GETTERS
	public HashMap<Integer, BigDecimal> getPosToColorMap() {
		return mPosToColorMap;
	}

	public HashMap<BigDecimal, Integer> getColorToPosMap() {
		return mColorToPosMap;
	}

	public HashMap<Integer, String> getPosToHexMap() {
		return mPosToHexMap;
	}

	public float getBaseHue() {
		return mBaseHue;
	}

	public float getBaseSat() {
		return mBaseSat;
	}

	public float getBaseVal() {
		return mBaseVal;
	}

	public ColorModel getBaseColorModel() {
		return mBaseColorModel;
	}

	public float getCurrentHue() {
		return mCurrentHue;
	}

	public float getCurrentSat() {
		return mCurrentSat;
	}

	public float getCurrentVal() {
		return mCurrentVal;
	}

	public int getBrowseBy() {
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
			finish();
			return true;
		case R.id.action_by_hue:
			Intent hueIntent = new Intent(mColorPickerActivity,
					ColorPickerActivity.class);
			hueIntent.putExtra("hue", mCurrentHue);
			hueIntent.putExtra("sat", mCurrentSat);
			hueIntent.putExtra("val", mCurrentVal);
			hueIntent.putExtra("browse_by", BY_HUE);
			startActivity(hueIntent);
			finish();
			return true;
		case R.id.action_by_saturation:
			Intent saturationIntent = new Intent(mColorPickerActivity,
					ColorPickerActivity.class);
			saturationIntent.putExtra("hue", mCurrentHue);
			saturationIntent.putExtra("sat", mCurrentSat);
			saturationIntent.putExtra("val", mCurrentVal);
			saturationIntent.putExtra("browse_by", BY_SATURATION);
			startActivity(saturationIntent);
			finish();
			return true;
		case R.id.action_by_value:
			Intent valueIntent = new Intent(mColorPickerActivity,
					ColorPickerActivity.class);
			valueIntent.putExtra("hue", mCurrentHue);
			valueIntent.putExtra("sat", mCurrentSat);
			valueIntent.putExtra("val", mCurrentVal);
			valueIntent.putExtra("browse_by", BY_VALUE);
			startActivity(valueIntent);
			finish();
			return true;
		case R.id.toggle_panel:
			mBottomPanel.toggle();
			mTopPanel.toggle();
			return true;
		case R.id.action_adjust:
			showAdjustDialog();
			return true;
		case R.id.action_add_to_palette:
			showPaletteDialog();
			return true;
		case R.id.action_create_palette:
			showNewPaletteDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (mAllPanels == null) {
			moveTaskBack();
		} else {
			boolean goBack = true;
			for (SlidingPanel sp : mAllPanels) {
				if (sp.getIsOpen()) {
					goBack = false;
				}
			}
			if (goBack) {
				moveTaskBack();
			} else {
				if (mAllPanels.length == 5) {
					Handler toggleHandler = new Handler();
					toggleHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							mPanel8.toggle();
						}
					}, 200);
					toggleHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							mPanel7.toggle();
						}
					}, 400);
					toggleHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							mPanel6.toggle();
						}
					}, 600);
					toggleHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							mPanel5.toggle();
						}
					}, 800);
					toggleHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							mPanel4.toggle();
						}
					}, 1000);
				} else if (mAllPanels.length == 3) {
					Handler toggleHandler = new Handler();
					toggleHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							mPanel3.toggle();
						}
					}, 200);
					toggleHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							mPanel2.toggle();
						}
					}, 400);
					toggleHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							mPanel1.toggle();
						}
					}, 600);
				} else if (mAllPanels.length == 2) {
					Handler toggleHandler = new Handler();
					toggleHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							mPanel10.toggle();
						}
					}, 200);
					toggleHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							mPanel9.toggle();
						}
					}, 400);
				}
			}
		}
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

		public void setActivity(ColorPickerActivity ca) {
			mActivity = ca;
		}

		public void generateMap() {
			for (int pos = 0; pos <= COLOR_COUNT; pos++) {
				getItem(pos);
			}
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new ColorObjectFragment();
			Bundle args = new Bundle();
			setNextColor(position);
			args.putFloat(ColorObjectFragment.ARG_HUE, mAdaptHue);
			args.putFloat(ColorObjectFragment.ARG_VAL, mAdaptVal);
			args.putFloat(ColorObjectFragment.ARG_SAT, mAdaptSat);
			fragment.setArguments(args);
			mActivity.getPosToHexMap().put(
					position,
					(new ColorModel(new float[] { mAdaptHue, mAdaptSat,
							mAdaptVal })).getHexCode());

			return fragment;
		}

		private void setNextColor(int position) {
			switch (mActivity.getBrowseBy()) {
			case BY_VALUE:
				if (mAdaptVal == round(position * 0.01f, 2)) {
					mAdaptVal = round((position + 1) * 0.01f, 2);
				} else {
					mAdaptVal = round(position * 0.01f, 2);
				}
				mActivity.getColorToPosMap().put(new BigDecimal(mAdaptVal),
						position);
				mActivity.getPosToColorMap().put(position,
						new BigDecimal(mAdaptVal));
				break;
			case BY_SATURATION:
				if (mAdaptSat == round(position * 0.01f, 2)) {
					mAdaptSat = round((position + 1) * 0.01f, 2);
				} else {
					mAdaptSat = round(position * 0.01f, 2);
				}
				mActivity.getColorToPosMap().put(new BigDecimal(mAdaptSat),
						position);
				mActivity.getPosToColorMap().put(position,
						new BigDecimal(mAdaptSat));
				break;
			case BY_HUE:
				mAdaptHue = position;
				mActivity.getColorToPosMap().put(new BigDecimal(mAdaptHue),
						position);
				mActivity.getPosToColorMap().put(position,
						new BigDecimal(mAdaptHue));
				break;
			default:
				mAdaptHue = position;
				mActivity.getColorToPosMap().put(new BigDecimal(mAdaptHue),
						position);
				mActivity.getPosToColorMap().put(position,
						new BigDecimal(mAdaptHue));
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
		private TextView mMainTextView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_colorpicker_object, container, false);
			Bundle args = getArguments();

			float hue = args.getFloat(ARG_HUE);
			float sat = args.getFloat(ARG_SAT);
			float val = args.getFloat(ARG_VAL);
			mFragmentColorModel = new ColorModel(new float[] { hue, sat, val });
			mFragmentColor = mFragmentColorModel.getColor();

			int[] rgb = mFragmentColorModel.getRGB();
			float[] cmyk = mFragmentColorModel.getCMYK();

			mMainTextView = (TextView) rootView
					.findViewById(android.R.id.text1);
			mMainTextView.setBackgroundColor(mFragmentColor);
			int textColor = (mFragmentColorModel.getValue() > .7f && mFragmentColorModel
					.getSaturation() < .5f)
					|| (mFragmentColorModel.getValue() > .9f && (mFragmentColorModel
							.getHue() >= 53.0f && mFragmentColorModel.getHue() <= 183.0f)) ? Color.BLACK
					: Color.WHITE;
			mMainTextView.setTextColor(textColor);
			Typeface tf = Typeface.createFromAsset(
					mColorPickerActivity.getAssets(), "Roboto-Light.ttf");
			mMainTextView.setTypeface(tf);
			mMainTextView.setText(Html.fromHtml("<h1><b><large>"
					+ mFragmentColorModel.getHexCode()
					+ "</large></b></h1><small>RGB: " + rgb[0] + ", " + rgb[1]
					+ ", " + rgb[2] + "<br/>CMYK: " + round(cmyk[0], 2) + ", "
					+ round(cmyk[1], 2) + ", " + round(cmyk[2], 2) + ", "
					+ round(cmyk[3], 2) + "<br/>" + "HSV: " + hue + ", " + sat
					+ ", " + val + "</small>"));

			mMainTextView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View view) {
					String copy = mMainTextView.getText().toString();
					ClipboardManager clipboard = (ClipboardManager) mColorPickerActivity
							.getSystemService(CLIPBOARD_SERVICE);
					ClipData clip = ClipData.newPlainText("color_info", copy);
					clipboard.setPrimaryClip(clip);
					Context context = mColorPickerActivity
							.getApplicationContext();
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, "Copied", duration);
					toast.show();
					return true;
				}
			});

			mMainTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					mColorPickerActivity.toggleOptions();
				}
			});

			return rootView;
		}

		public String getHexCode() {
			return mFragmentColorModel.getHexCode();
		}
	}

	public void showNewPaletteDialog() {
		DialogFragment dialog = new NewPaletteDialogFragment();
		dialog.show(getSupportFragmentManager(), "NewPaletteDialogFragment");
	}

	public void showPaletteDialog() {
		if (mComplexPrefs.getObject("palette_collection",
				PaletteCollection.class) == null) {
			DialogFragment dialog = new NewPaletteDialogFragment();
			dialog.show(getSupportFragmentManager(), "NewPaletteDialogFragment");
		} else {
			DialogFragment dialog = new PaletteDialogFragment();
			dialog.show(getSupportFragmentManager(), "PaletteDialogFragment");
			mBackToPaletteDialog = true;
		}
	}

	@Override
	public void onPaletteDialogPositiveClick(DialogFragment dialog,
			ArrayList<Integer> selectedPalettes) {
		for (Integer i : selectedPalettes) {
			String paletteKey = mComplexPrefs
					.getObject("palette_collection", PaletteCollection.class)
					.getCollection().get(i);
			mComplexPrefs.getObject(paletteKey, PaletteModel.class).add(
					Color.HSVToColor(new float[] { mCurrentHue, mCurrentSat,
							mCurrentVal }));
		}

		dialog.dismiss();
		Toast toast = Toast.makeText(getApplicationContext(),
				"Added to palettes", Toast.LENGTH_SHORT);
		toast.show();
	}

	@Override
	public void onPaletteDialogNeutralClick(DialogFragment dialog) {
		dialog.dismiss();
		showNewPaletteDialog();
	}

	@Override
	public void onPaletteDialogNegativeClick(DialogFragment dialog) {
		dialog.dismiss();

	}

	@Override
	public void onNewPaletteDialogPositiveClick(DialogFragment dialog,
			String name) {
		PaletteModel palette = new PaletteModel(name);
		if (mComplexPrefs != null) {
			mComplexPrefs.putObject(name, palette);
			if (mComplexPrefs.getObject("palette_collection",
					PaletteCollection.class) == null) {
				mComplexPrefs.putObject("palette_collection",
						new PaletteCollection(name));
			} else {
				PaletteCollection temp_name_arr = mComplexPrefs.getObject("palette_collection",
						PaletteCollection.class);
				temp_name_arr.add(name);
				mComplexPrefs.putObject("palette_collection",
						temp_name_arr);
				Log.d("la", "palette collection 0: "+ mComplexPrefs.getObject("palette_collection",
						PaletteCollection.class).getCollection().get(0));
				Log.d("la", "palette collection 1: "+ mComplexPrefs.getObject("palette_collection",
						PaletteCollection.class).getCollection().get(1));
				mComplexPrefs.putObject(name, new PaletteModel(name));
			}
		} else {
			android.util.Log.e("pref null", "Preference is null");
		}
		dialog.dismiss();
		showPaletteDialog();
		Toast toast = Toast.makeText(getApplicationContext(),
				"Created new palette \'" + name + "\'", Toast.LENGTH_SHORT);
		toast.show();
	}

	@Override
	public void onNewPaletteDialogNegativeClick(DialogFragment dialog) {
		dialog.dismiss();
		if (mBackToPaletteDialog) {
			showPaletteDialog();
			mBackToPaletteDialog = false;
		}
	}

}
