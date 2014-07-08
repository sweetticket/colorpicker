package com.example.scheme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FirstPaletteActivity extends FragmentActivity implements
		NewPaletteDialogFragment.NewPaletteDialogListener {
	private ObjectPreference mObjectPref;
	private ComplexPreferences mComplexPrefs;
	private Button mNewPaletteButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mObjectPref = (ObjectPreference) getApplication();
		mComplexPrefs = mObjectPref.getComplexPreference();
		setContentView(R.layout.activity_first_palette);
		mNewPaletteButton = (Button) findViewById(R.id.create_first_palette);
		mNewPaletteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showNewPaletteDialog();
			}

		});

	}

	public void showNewPaletteDialog() {
		DialogFragment dialog = new NewPaletteDialogFragment();
		dialog.show(getSupportFragmentManager(), "NewPaletteDialogFragment");
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
			}
		} else {
			android.util.Log.e("pref null", "Preference is null");
		}
		Intent myPalettesIntent = new Intent(this, MyPalettesActivity.class);
		myPalettesIntent.putExtra("toast_text", "Created new palette \'" + name + "\'");
		startActivity(myPalettesIntent);
		finish();
	}

	@Override
	public void onNewPaletteDialogNegativeClick(DialogFragment dialog) {
		dialog.dismiss();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first_palette, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
