package com.example.scheme;

import com.example.scheme.AdjustDialogFragment.AdjustDialogListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DeleteDialogFragment extends DialogFragment {
	
	private String mPaletteSelection;
	
	public interface DeleteDialogListener {
		public void onDeleteDialogPositiveClick(DialogFragment dialog);

		public void onDeleteDialogNegativeClick(DialogFragment dialog);
	}

	DeleteDialogListener mListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (DeleteDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement DeleteDialogListener");
		}
	}

	 @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(R.string.confirm_delete + mPaletteSelection)
	               .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // FIRE ZE MISSILES!
	                   }
	               })
	               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // User cancelled the dialog
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	 
	 public void setPaletteSelection(String name){
		 mPaletteSelection = name;
	 }
	 
	 public String getPaletteSelection(){
		 return mPaletteSelection;
	 }

}
