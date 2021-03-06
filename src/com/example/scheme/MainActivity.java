package com.example.scheme;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity {
	public static final int SELECT_PICTURE = 1;
	public static final int REQUEST_IMAGE_CAPTURE = 2;
	private String imagePath;
	private ImageView mToGallery;
	private ImageView mToCamera;
	private ImageView mToPicker;
	private ImageView mToPalettes;


	// private View mMainView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mToGallery = (ImageView) findViewById(R.id.to_gallery);
		mToCamera = (ImageView) findViewById(R.id.to_camera);
		mToPicker = (ImageView) findViewById(R.id.to_picker);
		mToPalettes = (ImageView) findViewById(R.id.to_palettes);
		initButtons();
		
	}

	/** Set listeners, etc. */
	private void initButtons() {
		mToGallery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent galleryIntent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(galleryIntent, SELECT_PICTURE);
			}
		});

		mToCamera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent cameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				if (cameraIntent.resolveActivity(getPackageManager()) != null) {
					startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
				}
			}
		});

		mToPicker.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				startColorPicker(Color.HSVToColor(new float[]{20.0f, 0.7f, 0.7f}));
			}
		});
		
		mToPalettes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				startMyPalettes();
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK
				&& data != null) {
			Uri pickedImage = data.getData();
			String[] filePath = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(pickedImage, filePath,
					null, null, null);
			cursor.moveToFirst();
			imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
			cursor.close();
			Intent pinpointIntent = new Intent(this, PinpointActivity.class);
			pinpointIntent.putExtra("path", imagePath);
			startActivity(pinpointIntent.setData(pickedImage));
		}

		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Uri pickedImage = data.getData();
			imagePath = getRealPathFromURI(pickedImage);
			Intent pinpointIntent = new Intent(this, PinpointActivity.class);
			pinpointIntent.putExtra("path", imagePath);
			startActivity(pinpointIntent.setData(pickedImage));
		}
	}

	public String getRealPathFromURI(Uri contentUri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(contentUri, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			return contentUri.getPath();
		}
	}
	
	private void startColorPicker(int color){
		Intent colorPickerIntent = new Intent(this, ColorPickerActivity.class);
		colorPickerIntent.putExtra("color", color);
		startActivity(colorPickerIntent);
		
	}
	
	private void startMyPalettes(){
		Intent paletteIntent = new Intent(this, MyPalettesActivity.class);
		startActivity(paletteIntent);
		
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
