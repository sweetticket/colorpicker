package com.example.scheme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

public class AdjustDialogFragment extends DialogFragment {
	
	
	private SeekBar mHueSeekBar;
	private SeekBar mSatSeekBar;
	private SeekBar mValSeekBar;
	
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
		View diagRootView = inflater.inflate(R.layout.fragment_adjust_dialog, null);
		builder.setView(
				diagRootView)
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
		
		mHueSeekBar = (SeekBar) diagRootView.findViewById(R.id.seek_hue);
		mSatSeekBar = (SeekBar) diagRootView.findViewById(R.id.seek_sat);
		mValSeekBar = (SeekBar) diagRootView.findViewById(R.id.seek_val);
		return builder.create();
	}
	
}
