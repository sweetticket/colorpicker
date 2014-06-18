package com.example.colorpicker;

import com.example.colorpicker.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Build;
import android.provider.MediaStore;

public class MainActivity extends ActionBarActivity {
	public static String PICTURE_PATH;
	public static final int SELECT_PICTURE = 1;
	private ImageView mToGallery;
	private ImageView mToCamera;
	private ImageView mToPicker;

	// private View mMainView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// MainView = (View) findViewById(R.id.container);
		mToGallery = (ImageView) findViewById(R.id.to_gallery);
		mToCamera = (ImageView) findViewById(R.id.to_camera);
		mToPicker = (ImageView) findViewById(R.id.to_picker);
		initButtons();
	}

	/** Set listeners, etc. */
	private void initButtons() {
		mToGallery.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				Intent galleryIntent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(galleryIntent, SELECT_PICTURE);
				return false;
			}
		});

		mToCamera.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}

		});

		mToPicker.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}

		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(resultCode, resultCode, data);

		if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK
				&& data != null) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			PICTURE_PATH = cursor.getString(columnIndex);
			cursor.close();
			Intent pinpointIntent = new Intent(this, PinpointActivity.class);
			pinpointIntent.putExtra("picture path", PICTURE_PATH);
			startActivity(pinpointIntent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
