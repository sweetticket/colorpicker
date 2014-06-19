package com.example.scheme;

import com.example.scheme.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
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
	public static final int SELECT_PICTURE = 1;
	private String imagePath;
	private ImageView mToGallery;
	private ImageView mToCamera;
	private ImageView mToPicker;

	// private View mMainView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mToGallery = (ImageView) findViewById(R.id.to_gallery);
		mToCamera = (ImageView) findViewById(R.id.to_camera);
		mToPicker = (ImageView) findViewById(R.id.to_picker);
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
				
			}
		});

		mToPicker.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				
			}
		});

	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            cursor.close();
            Intent pinpointIntent = new Intent(this, PinpointActivity.class);
			pinpointIntent.putExtra("path", imagePath);
			startActivity(pinpointIntent.setData(pickedImage));
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
