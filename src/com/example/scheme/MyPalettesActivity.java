/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.scheme;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

public class MyPalettesActivity extends FragmentActivity implements
		DeleteDialogFragment.DeleteDialogListener,
		NewPaletteDialogFragment.NewPaletteDialogListener,
		EditDialogFragment.EditDialogListener,
		RenameDialogFragment.RenameDialogListener{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private ObjectPreference mObjectPref;
	private static ComplexPreferences mComplexPrefs;
	private static ArrayList<String> mPaletteNames;
	private ArrayAdapter mDrawerAdapter;
	private int mCurrentPos;
	private static boolean mEditMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mEditMode = false;
		setContentView(R.layout.activity_my_palettes);

		if (getIntent().getStringExtra("toast_text") != null) {
			Toast toast = Toast.makeText(getApplicationContext(), getIntent()
					.getStringExtra("toast_text"), Toast.LENGTH_SHORT);
			toast.show();
		}

		mObjectPref = (ObjectPreference) getApplication();
		mComplexPrefs = mObjectPref.getComplexPreference();
		if (mComplexPrefs.getObject("palette_collection",
				PaletteCollection.class) == null
				|| mComplexPrefs
						.getObject("palette_collection",
								PaletteCollection.class).getCollection().size() == 0) {
			Intent firstPaletteIntent = new Intent(this,
					FirstPaletteActivity.class);
			startActivity(firstPaletteIntent);
			finish();
		} else {
			mPaletteNames = mComplexPrefs.getObject("palette_collection",
					PaletteCollection.class).getCollection();

			mTitle = mDrawerTitle = getTitle();
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			mDrawerList = (ListView) findViewById(R.id.left_drawer);

			// set a custom shadow that overlays the main content when the
			// drawer
			// opens
			mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
					GravityCompat.START);
			// set up the drawer's list view with items and click listener
			mDrawerAdapter = new ArrayAdapter<String>(this,
					R.layout.drawer_list_item, mPaletteNames);
			mDrawerList.setAdapter(mDrawerAdapter);
			mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

			// enable ActionBar app icon to behave as action to toggle nav
			// drawer
			getActionBar().setDisplayHomeAsUpEnabled(true);
			// getActionBar().setHomeButtonEnabled(true);

			// ActionBarDrawerToggle ties together the the proper interactions
			// between the sliding drawer and the action bar app icon
			mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
			mDrawerLayout, /* DrawerLayout object */
			R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
			R.string.drawer_open, /* "open drawer" description for accessibility */
			R.string.drawer_close /* "close drawer" description for accessibility */
			) {
				public void onDrawerClosed(View view) {
					getActionBar().setTitle(mTitle);
					invalidateOptionsMenu(); // creates call to
												// onPrepareOptionsMenu()
				}

				public void onDrawerOpened(View drawerView) {
					getActionBar().setTitle(mDrawerTitle);
					invalidateOptionsMenu(); // creates call to
												// onPrepareOptionsMenu()
				}
			};
			mDrawerLayout.setDrawerListener(mDrawerToggle);

			if (savedInstanceState == null) {
				selectItem(0);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.my_palettes, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_create_palette:
			showNewPaletteDialog();
			return true;
		case R.id.action_rename_palette:
			showRenameDialog();
			return true;
		case R.id.action_edit_palette:
			editPalette();
			return true;
		case R.id.action_delete_palette:
			showDeleteDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void editPalette() {
		mEditMode = !mEditMode;
		mDrawerLayout.closeDrawer(mDrawerList);
		if (mEditMode) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Touch to delete a color", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	public void showDeleteDialog() {
		DialogFragment dialog = new DeleteDialogFragment();

		((DeleteDialogFragment) dialog).setPaletteSelection(mTitle.toString());

		dialog.show(getSupportFragmentManager(), "DeleteDialogFragment");
	}
	
	
	public void showRenameDialog(){
		DialogFragment dialog = new RenameDialogFragment();
		dialog.show(getSupportFragmentManager(), "RenameDialogFragment");
	}
	
	public void showNewPaletteDialog() {
		DialogFragment dialog = new NewPaletteDialogFragment();
		dialog.show(getSupportFragmentManager(), "NewPaletteDialogFragment");
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		mCurrentPos = position;
		Fragment fragment = new PaletteFragment();
		Bundle args = new Bundle();
		args.putInt(PaletteFragment.ARG_PALETTE_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mPaletteNames.get(position));
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Fragment that appears in the "content_frame", shows a planet
	 */
	public static class PaletteFragment extends Fragment {
		public static final String ARG_PALETTE_NUMBER = "palette_number";
		private LinearLayout mPaletteContainer;

		public PaletteFragment() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.fragment_palette,
					container, false);
			int i = getArguments().getInt(ARG_PALETTE_NUMBER);
			String palette_name = mPaletteNames.get(i);

			mPaletteContainer = (LinearLayout) rootView
					.findViewById(R.id.palette_container);
			PaletteModel current_palette = mComplexPrefs.getObject(
					palette_name, PaletteModel.class);
			if (current_palette != null){
			for (int pos = 0; pos < current_palette.getColors().size(); pos++) {
				if (rootView.findViewById(pos / 3) == null) {
					LinearLayout lL = new LinearLayout(getActivity());
					lL.setPadding(15, 15, 15, 15);
					lL.setId(pos / 3);
					mPaletteContainer.addView(lL);
				}
				int color = current_palette.getColors().get(pos);
				PaletteView currentPaletteView = new PaletteView(getActivity());
				((LinearLayout) rootView.findViewById(pos / 3))
						.addView(currentPaletteView);
				currentPaletteView.setPaintColor(color);
				currentPaletteView.setPadding(15, 15, 15, 15);
				currentPaletteView.setId(pos * -1);
				currentPaletteView.getLayoutParams().height = PaletteView.BIT_HEIGHT;
				currentPaletteView.getLayoutParams().width = PaletteView.BIT_WIDTH;
				currentPaletteView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!mEditMode) {
							Intent colorPickerIntent = new Intent(
									getActivity(), ColorPickerActivity.class);
							colorPickerIntent.putExtra("color",
									((PaletteView) v).getPaintColor());
							startActivity(colorPickerIntent);
						} else {
							((MyPalettesActivity) getActivity())
									.showEditDialog(
											((PaletteView) v).getPaintColor(),
											v.getId() * -1);
						}
					}

				});
			}
			}
			getActivity().setTitle(palette_name);
			return rootView;
		}
	}

	public void showEditDialog(int color, int pos) {
		DialogFragment dialog = new EditDialogFragment();

		((EditDialogFragment) dialog).setColorSelection(color);
		((EditDialogFragment) dialog).setColorPos(pos);
		dialog.show(getSupportFragmentManager(), "EditDialogFragment");
	}

	@Override
	public void onDeleteDialogPositiveClick(DialogFragment dialog) {

		if (mComplexPrefs != null) {
			mComplexPrefs.removeObject(mTitle.toString());
			PaletteCollection temp_name_arr = mComplexPrefs.getObject(
					"palette_collection", PaletteCollection.class);
			temp_name_arr.getCollection().remove(mTitle);
			mComplexPrefs.putObject("palette_collection", temp_name_arr);
			if (temp_name_arr.getCollection().size() == 0) {
				Intent firstPaletteIntent = new Intent(this,
						FirstPaletteActivity.class);
				startActivity(firstPaletteIntent);
				finish();
			}
			dialog.dismiss();
			String toast_text = "Deleted palette \'" + mTitle + "\'";
			refresh(toast_text);
		} else {
			android.util.Log.e("pref null", "Preference is null");
		}

	}

	public void refresh(String toastText) {
		Intent refreshIntent = new Intent(this, MyPalettesActivity.class);
		refreshIntent.putExtra("toast_text", toastText);
		startActivity(refreshIntent);
		finish();
	}

	@Override
	public void onDeleteDialogNegativeClick(DialogFragment dialog) {
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
				PaletteCollection temp_name_arr = mComplexPrefs.getObject(
						"palette_collection", PaletteCollection.class);
				temp_name_arr.add(name);
				mComplexPrefs.putObject("palette_collection", temp_name_arr);
				mDrawerAdapter.notifyDataSetChanged();
				dialog.dismiss();
				String toast_text = "Created new palette \'" + name + "\'";
				refresh(toast_text);
			}
		} else {
			android.util.Log.e("pref null", "Preference is null");
			dialog.dismiss();
			Toast toast = Toast.makeText(getApplicationContext(),
					"Error. Try Again.", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	@Override
	public void onNewPaletteDialogNegativeClick(DialogFragment dialog) {
		dialog.dismiss();
	}

	@Override
	public void onEditDialogPositiveClick(DialogFragment dialog, int colorpos) {
		PaletteModel temp_palette_model = mComplexPrefs.getObject(
				mTitle.toString(), PaletteModel.class);
		temp_palette_model.getColors().remove(colorpos);
		mComplexPrefs.removeObject(mTitle.toString());
		mComplexPrefs.putObject(mTitle.toString(), temp_palette_model);
		int deleted_color = ((EditDialogFragment) dialog).getColorSelection();
		refresh("Deleted " + ((new ColorModel(deleted_color)).getHexCode())
				+ " from /'" + mTitle.toString() + "\'");
	}

	@Override
	public void onEditDialogNegativeClick(DialogFragment dialog) {
		dialog.dismiss();
	}

	@Override
	public void onRenameDialogPositiveClick(DialogFragment dialog, String name) {
		PaletteModel temp_palette_model = mComplexPrefs.getObject(mTitle.toString(), PaletteModel.class);
		temp_palette_model.setName(name);
		mComplexPrefs.removeObject(mTitle.toString());
		mComplexPrefs.putObject(name, temp_palette_model);
		PaletteCollection temp_collection = mComplexPrefs.getObject("palette_collection", PaletteCollection.class);
		temp_collection.getCollection().remove(mTitle.toString());
		temp_collection.getCollection().add(name);
		mComplexPrefs.removeObject("palette_collection");
		mComplexPrefs.putObject("palette_collection", temp_collection);
		dialog.dismiss();
		refresh("Renamed palette to \'"+name+"\'");
	}

	@Override
	public void onRenameDialogNegativeClick(DialogFragment dialog) {
		dialog.dismiss();	
	}
}