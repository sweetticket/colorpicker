package com.example.scheme;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class PaletteDialogFragment extends DialogFragment {
	private ArrayList mSelectedPalettes;
	private Activity mActivity;
	private SharedPreferences mPalettePrefs;
	private CharSequence[] mPaletteNames;
	
	public interface PaletteDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);

		public void onDialogNegativeClick(DialogFragment dialog);
	}

	PaletteDialogListener mListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
		try {
			mListener = (PaletteDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement PaletteDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Context context = getActivity();
		mPalettePrefs = context.getSharedPreferences(getString(R.string.palette_file_key), Context.MODE_PRIVATE);
		mPalettePrefs.getAll();
		//TODO
		mSelectedPalettes = new ArrayList();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setTitle(R.string.add_to_palettes).setMultiChoiceItems(mPaletteNames, null, new DialogInterface.OnMultiChoiceClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked){
				if (isChecked) {
					mSelectedPalettes.add(which);
				}else if (mSelectedPalettes.contains(which)){
					mSelectedPalettes.remove(Integer.valueOf(which));
				}
			}
		})
		.setNeutralButton(R.string.new_palette, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// create a new palette, input palette name, add selected color
				// new fragment? intent?
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				mListener
				.onDialogNegativeClick(PaletteDialogFragment.this);
			}
		})
		.setPositiveButton(R.string.add, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				mListener
				.onDialogPositiveClick(PaletteDialogFragment.this);
				// add color to selected palettes
			}
		});
		
		return builder.create();
	}
}
