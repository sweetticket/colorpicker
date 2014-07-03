package com.example.scheme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
	
	public interface AdjustDialogListener{
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}
	
	AdjustDialogListener mListener;
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			mListener = (AdjustDialogListener) activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString() + " must implement AdjustDialogListener");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View diaRootView = inflater.inflate(R.layout.fragment_adjust_dialog, null);
		builder.setView(
				diaRootView)
				.setMessage(R.string.adjust_hsv)
				.setPositiveButton(R.string.adjust,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mListener.onDialogPositiveClick(AdjustDialogFragment.this);

							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mListener.onDialogNegativeClick(AdjustDialogFragment.this);

							}
						});
		
		int firstcolor = Color.HSVToColor(new float[] {mHue, mSat, mVal});
		
		mHueSeekBar = (SeekBar) diaRootView.findViewById(R.id.seek_hue);
		mHueSeekBar.setMax(359);
		mHueSeekText = (TextView) diaRootView.findViewById(R.id.seek_hue_text);
		mHueSeekBar.setProgress((int)mHue);
		mHueSeekText.setText("Hue    " + mHueSeekBar.getProgress() + "/" + mHueSeekBar.getMax());
		
		mSatSeekBar = (SeekBar) diaRootView.findViewById(R.id.seek_sat);
		mSatSeekBar.setMax(100);
		mSatSeekText = (TextView) diaRootView.findViewById(R.id.seek_sat_text);
		mSatSeekBar.setProgress((int)(mSat * 100));
		mSatSeekText.setText("Saturation    " + mSatSeekBar.getProgress() + "/" + mSatSeekBar.getMax());
		
		mValSeekBar = (SeekBar) diaRootView.findViewById(R.id.seek_val);
		mValSeekBar.setMax(100);
		mValSeekText = (TextView) diaRootView.findViewById(R.id.seek_val_text);
		mValSeekBar.setProgress((int)(mSat * 100));
		mValSeekText.setText("Value    " + mValSeekBar.getProgress() + "/" + mValSeekBar.getMax());
		
		
		mHueSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_bg1));
		mSatSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_bg1));
		mValSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_bg1));
		
		setProgressColor(mHueSeekBar, firstcolor);
		setProgressColor(mSatSeekBar, firstcolor);
		setProgressColor(mValSeekBar, firstcolor);
		
		mHueSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			int progress = 0;
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser){
				progress = progressValue;
				mHueSeekText.setText( "Hue    " + progress + "/" + mHueSeekBar.getMax());
				mHue = progress;
				int newColor = Color.HSVToColor(new float[]{mHue, mSat, mVal});
				setProgressColor(mHueSeekBar, newColor);
				setProgressColor(mSatSeekBar, newColor);
				setProgressColor(mValSeekBar, newColor);
			}
		
			@Override
			public void onStartTrackingTouch(SeekBar seekBar){
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
		});
		
		mSatSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			int progress = 0;
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser){
				progress = progressValue;
				mSatSeekText.setText( "Saturation    " + progress + "/" + mSatSeekBar.getMax());
				mSat = progress / 100.0f;
				int newColor = Color.HSVToColor(new float[]{mHue, mSat, mVal});
				setProgressColor(mHueSeekBar, newColor);
				setProgressColor(mSatSeekBar, newColor);
				setProgressColor(mValSeekBar, newColor);
			}
		
			@Override
			public void onStartTrackingTouch(SeekBar seekBar){
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
		
		mValSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			int progress = 0;
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser){
				progress = progressValue;
				mValSeekText.setText( "Value    " + progress + "/" + mValSeekBar.getMax());
				mVal = progress / 100.0f;
				int newColor = Color.HSVToColor(new float[]{mHue, mSat, mVal});
				setProgressColor(mHueSeekBar, newColor);
				setProgressColor(mSatSeekBar, newColor);
				setProgressColor(mValSeekBar, newColor);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar){
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
		
		return builder.create();
	}
	
	public void setHue(float hue){
		mHue = hue;
	}
	
	public void setSat(float sat){
		mSat = sat;
	}
	
	public void setVal(float val){
		mVal = val;
	}
	
	public float[] getHSV(){
		return new float[]{mHue, mSat, mVal};
	}
	
	public void setProgressColor(SeekBar seekbar, int newColor){ 
		  LayerDrawable ld = (LayerDrawable) seekbar.getProgressDrawable();
		  ClipDrawable d1 = (ClipDrawable) ld.findDrawableByLayerId(R.id.progressshape);
		  d1.setColorFilter(newColor, PorterDuff.Mode.SRC_IN);
		}
}
