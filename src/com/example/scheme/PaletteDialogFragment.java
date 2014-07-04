package com.example.scheme;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

public class PaletteDialogFragment extends DialogFragment {
	private ArrayList<Integer> mSelectedPalettes;
	private Activity mActivity;
	private ObjectPreference mObjectPref;
	private ComplexPreferences mComplexPrefs;
	private ArrayList<String> mPaletteNames;
	private AlertDialog mAlertDialog;
	private ListView mListView;

	public interface PaletteDialogListener {
		public void onPaletteDialogPositiveClick(DialogFragment dialog, ArrayList<Integer> selectedPalettes);

		public void onPaletteDialogNeutralClick(DialogFragment dialog);

		public void onPaletteDialogNegativeClick(DialogFragment dialog);
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
		mSelectedPalettes = new ArrayList<Integer>();
		mObjectPref = (ObjectPreference) mActivity.getApplication();
		mComplexPrefs = mObjectPref.getComplexPreference();
		mPaletteNames = mComplexPrefs.getObject("palette_collection", PaletteCollection.class).getCollection();
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.add_to_palettes)
				.setAdapter(
						new ArrayAdapter<String>(getActivity(),
								R.layout.palette_choice_item, mPaletteNames),
						null)
				.setNeutralButton(R.string.new_palette,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mListener.onPaletteDialogNeutralClick(PaletteDialogFragment.this);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mListener
										.onPaletteDialogNegativeClick(PaletteDialogFragment.this);
							}
						})
				.setPositiveButton(R.string.add,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mListener
										.onPaletteDialogPositiveClick(PaletteDialogFragment.this, mSelectedPalettes);
								// add color to selected palettes
							}
						});

		mAlertDialog = builder.create();
		mListView = (ListView) mAlertDialog.getListView();
		mListView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.palette_choice_item, mPaletteNames));
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CheckedTextView checkedTextView = (CheckedTextView) view;
				mSelectedPalettes.add(position);
			}
			
		});
		mListView.setDivider(null);
		mListView.setDividerHeight(-1);
		
		return mAlertDialog;
	}
}
