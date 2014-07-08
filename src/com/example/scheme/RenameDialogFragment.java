package com.example.scheme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class RenameDialogFragment extends DialogFragment {
	
	private EditText mNameEditText;
	
	public RenameDialogFragment() {
		// TODO Auto-generated constructor stub
	}

	public interface RenameDialogListener {
		public void onRenameDialogPositiveClick(DialogFragment dialog, String name);

		public void onRenameDialogNegativeClick(DialogFragment dialog);
	}

	RenameDialogListener mListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (RenameDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement RenameDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View diaRootView = inflater.inflate(R.layout.fragment_new_palette_dialog,
				null);
		mNameEditText = (EditText) diaRootView.findViewById(R.id.palette_name);
		builder.setView(diaRootView)
				.setMessage(R.string.rename_palette)
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mListener
										.onRenameDialogNegativeClick(RenameDialogFragment.this);

							}
						})
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String name = mNameEditText.getText().toString();
								mListener
										.onRenameDialogPositiveClick(RenameDialogFragment.this, name);

							}
						});

		return builder.create();
	}
}
