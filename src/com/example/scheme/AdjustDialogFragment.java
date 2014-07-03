package com.example.scheme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class AdjustDialogFragment extends DialogFragment {

	private SeekBar mHueSeekBar;
	private SeekBar mSatSeekBar;
	private SeekBar mValSeekBar;
	private TextView mHueSeekText;
	private TextView mSatSeekText;
	private TextView mValSeekText;
	private float mHue;
	private float mSat;
	private float mVal;

	public interface AdjustDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);

		public void onDialogNegativeClick(DialogFragment dialog);
	}

	AdjustDialogListener mListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (AdjustDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement AdjustDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View diaRootView = inflater.inflate(R.layout.fragment_adjust_dialog,
				null);
		builder.setView(diaRootView)
				.setMessage(R.string.adjust_hsv)
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mListener
										.onDialogNegativeClick(AdjustDialogFragment.this);

							}
						})
				.setPositiveButton(R.string.adjust,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mListener
										.onDialogPositiveClick(AdjustDialogFragment.this);

							}
						});

		mHueSeekBar = (SeekBar) diaRootView.findViewById(R.id.seek_hue);
		mHueSeekText = (TextView) diaRootView.findViewById(R.id.seek_hue_text);
		mHueSeekText.setText("Hue    " + mHueSeekBar.getProgress() + "/"
				+ mHueSeekBar.getMax());
		mHueSeekBar.setProgress(0);
		Rect hueBounds = mHueSeekBar.getProgressDrawable().getBounds();
		mHueSeekBar.setProgressDrawable(getResources().getDrawable(
				R.drawable.seekbar_bg1));
		mHueSeekBar.getProgressDrawable().setBounds(hueBounds);
		mHueSeekBar.setProgress((int) mHue);

		mSatSeekBar = (SeekBar) diaRootView.findViewById(R.id.seek_sat);
		mSatSeekText = (TextView) diaRootView.findViewById(R.id.seek_sat_text);
		mSatSeekText.setText("Saturation    " + mSatSeekBar.getProgress() + "/"
				+ mSatSeekBar.getMax());
		mSatSeekBar.setProgress(0);
		Rect satBounds = mSatSeekBar.getProgressDrawable().getBounds();
		mSatSeekBar.setProgressDrawable(getResources().getDrawable(
				R.drawable.seekbar_bg1));
		mSatSeekBar.getProgressDrawable().setBounds(satBounds);
		mSatSeekBar.setProgress((int) (mSat * 100));

		mValSeekBar = (SeekBar) diaRootView.findViewById(R.id.seek_val);
		mValSeekText = (TextView) diaRootView.findViewById(R.id.seek_val_text);
		mValSeekText.setText("Value    " + mValSeekBar.getProgress() + "/"
				+ mValSeekBar.getMax());
		mValSeekBar.setProgress(0);
		Rect valBounds = mValSeekBar.getProgressDrawable().getBounds();
		mValSeekBar.setProgressDrawable(getResources().getDrawable(
				R.drawable.seekbar_bg1));
		mValSeekBar.getProgressDrawable().setBounds(valBounds);
		mValSeekBar.setProgress((int) (mVal * 100));

		int firstcolor = Color.HSVToColor(new float[] { mHue, mSat, mVal });
		setProgressColor(mHueSeekBar, firstcolor);
		setProgressColor(mSatSeekBar, firstcolor);
		setProgressColor(mValSeekBar, firstcolor);

		mHueSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progress = 0;

			@Override
			public void onProgressChanged(SeekBar seekBar, int progressValue,
					boolean fromUser) {
				if (!fromUser) {
					return;
				}
				progress = progressValue;
				mHueSeekText.setText("Hue    " + progress + "/"
						+ mHueSeekBar.getMax());
				mHue = progress;
				int newColor = Color
						.HSVToColor(new float[] { mHue, mSat, mVal });
				setProgressColor(mHueSeekBar, newColor);
				setProgressColor(mSatSeekBar, newColor);
				setProgressColor(mValSeekBar, newColor);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});

		mSatSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progress = 0;

			@Override
			public void onProgressChanged(SeekBar seekBar, int progressValue,
					boolean fromUser) {
				if (!fromUser) {
					return;
				}
				progress = progressValue;
				mSatSeekText.setText("Saturation    " + progress + "/"
						+ mSatSeekBar.getMax());
				mSat = progress / 100.0f;
				int newColor = Color
						.HSVToColor(new float[] { mHue, mSat, mVal });
				setProgressColor(mHueSeekBar, newColor);
				setProgressColor(mSatSeekBar, newColor);
				setProgressColor(mValSeekBar, newColor);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		mValSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progress = 0;

			@Override
			public void onProgressChanged(SeekBar seekBar, int progressValue,
					boolean fromUser) {
				if (!fromUser) {
					return;
				}
				progress = progressValue;
				mValSeekText.setText("Value    " + progress + "/"
						+ mValSeekBar.getMax());
				mVal = progress / 100.0f;
				int newColor = Color
						.HSVToColor(new float[] { mHue, mSat, mVal });
				setProgressColor(mHueSeekBar, newColor);
				setProgressColor(mSatSeekBar, newColor);
				setProgressColor(mValSeekBar, newColor);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		return builder.create();
	}

	public void setHue(float hue) {
		mHue = hue;
	}

	public void setSat(float sat) {
		mSat = sat;
	}

	public void setVal(float val) {
		mVal = val;
	}

	public float[] getHSV() {
		return new float[] { mHue, mSat, mVal };
	}

	public void setProgressColor(SeekBar seekbar, int newColor) {
		Log.d("setProgressColor", "setting progress color");
		LayerDrawable ld = (LayerDrawable) seekbar.getProgressDrawable();
		ClipDrawable d1 = (ClipDrawable) ld
				.findDrawableByLayerId(R.id.progressshape);
		d1.setColorFilter(newColor, PorterDuff.Mode.SRC_IN);
	}

	public void setAllColors() {
		mHueSeekBar.setProgressDrawable(getResources().getDrawable(
				R.drawable.seekbar_bg1));
		mSatSeekBar.setProgressDrawable(getResources().getDrawable(
				R.drawable.seekbar_bg1));
		mValSeekBar.setProgressDrawable(getResources().getDrawable(
				R.drawable.seekbar_bg1));

		int firstcolor = Color.HSVToColor(new float[] { mHue, mSat, mVal });
		setProgressColor(mHueSeekBar, firstcolor);
		setProgressColor(mSatSeekBar, firstcolor);
		setProgressColor(mValSeekBar, firstcolor);
	}
}
