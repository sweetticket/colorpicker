package com.example.scheme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class EditDialogFragment extends DialogFragment {

	private int mColorSelection;
	private int mColorPos;

	public interface EditDialogListener {
		public void onEditDialogPositiveClick(DialogFragment dialog, int pos);

		public void onEditDialogNegativeClick(DialogFragment dialog);
	}

	EditDialogListener mListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (EditDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement EditDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Delete " + ((new ColorModel(mColorSelection)).getHexCode()) + " from palette?")
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mListener
										.onEditDialogNegativeClick(EditDialogFragment.this);
							}
						})
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mListener
										.onEditDialogPositiveClick(EditDialogFragment.this, mColorPos);
							}
						});
		// Create the AlertDialog object and return it
		return builder.create();
	}

	public void setColorSelection(int color) {
		mColorSelection = color;
	}

	public int getColorSelection() {
		return mColorSelection;
	}
	
	public void setColorPos(int pos){
		mColorPos = pos;
	}
	
	public int getColorPos(){
		return mColorPos;
	}

}
