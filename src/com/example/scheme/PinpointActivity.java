package com.example.scheme;

import com.example.scheme.R;

import android.R.color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Build;

public class PinpointActivity extends ActionBarActivity {

	public final static int REQUIRED_SIZE = 80;
	public final static int FRAME_RATE = 10;
	
	private PinpointView mPinpointView;
	private Bitmap mBitmap;
	private Handler mRectHandler;
	private float mXPos;
	private float mYPos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pinpoint);
		mPinpointView = (PinpointView) findViewById(R.id.pinpoint_view);
		Intent intent = getIntent();
		String path = intent.getStringExtra("path");
		mBitmap = fixImage(path);
		mPinpointView.setImageBitmap(mBitmap);
		

		initTouch();

		Context context = getApplicationContext();
		CharSequence text = "Tap to pinpoint color";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	/** Sets up touch listeners */
	private void initTouch() {
		mRectHandler = new Handler();
		mPinpointView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				int action = MotionEventCompat.getActionMasked(event);
				float x = event.getX();
				float y = event.getY();
				mPinpointView.setZoomPos(x, y);
				switch (action) {
				case (MotionEvent.ACTION_DOWN): {
					mPinpointView.setZooming(true);
					return true;
				}
				case (MotionEvent.ACTION_MOVE):
					if (mPinpointView.isInRectangle(x, y)) {
						mRectHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								mPinpointView.setRectPos();
								mRectHandler.postDelayed(this, FRAME_RATE);
							}
						}, FRAME_RATE);
					}

					view.invalidate();
					return true;
				case (MotionEvent.ACTION_UP):
					view.invalidate();
					startColorPicker(mPinpointView.getColor());
					return true;
				default:
					return true;
				}

			}

		});
	}
	
	private Bitmap fixImage(String path) {
		try {
			if (path == null) {
				return null;
			}
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			// Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 6;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
			}
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bm = BitmapFactory.decodeFile(path, o2);
			Bitmap bitmap = bm;

			ExifInterface exif = new ExifInterface(path);
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, 1);
			Log.e("orientation", "" + orientation);
			Matrix m = new Matrix();

			if ((orientation == 3)) {
				m.postRotate(180);
				m.postScale((float) bm.getWidth(), (float) bm.getHeight());
				// if(m.preRotate(90)){
				Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			} else if (orientation == 6) {
				m.postRotate(90);
				Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			} else if (orientation == 8) {
				m.postRotate(270);
				Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			}
			return bitmap;
		} catch (Exception e) {
		}
		return null;

	}
	
	private void startColorPicker(int color){
		Log.d("here", "colorpicker start with: "+color);
		Intent colorPickerIntent = new Intent(this, ColorPickerActivity.class);
		colorPickerIntent.putExtra("color", color);
		startActivity(colorPickerIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pinpoint, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
	    // Handle presses on the action bar items
	    /*switch (item.getItemId()) {
	        case R.id.action_search:
	            openSearch();
	            return true;
	        case R.id.action_compose:
	            composeMessage();
	            return true;
	        default:*/
	            return super.onOptionsItemSelected(item);
	    //}
	}
	
	

}
