package com.example.scheme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ColorPickerActivity extends ActionBarActivity {

	private int mColor;
	private ColorModel mColorModel;
	private TextView mHexCodeBlock;
	private TextView mInfoBlock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_picker);
		Intent intent = getIntent();
		mColor = intent.getIntExtra("color", 0);
		mColorModel = new ColorModel(mColor);
		
		mHexCodeBlock = (TextView) findViewById(R.id.current_hexcode);
		mHexCodeBlock.setTextSize(50f);
		mInfoBlock = (TextView) findViewById(R.id.current_info);
		mInfoBlock.setTextSize(30f);
		
		displayInfo();
	}
	
	public void displayInfo(){
		mColorModel = new ColorModel(mColor);
		int[] rgb = mColorModel.getRGB();
		float[] cmyk = mColorModel.getCMYK();
		float[] hsv = mColorModel.getHSV();

		mHexCodeBlock = (TextView) findViewById(R.id.current_hexcode);
		mHexCodeBlock.setBackgroundColor(mColor);
		int textColor = mColorModel.getHue() > .5f ? Color.BLACK : Color.WHITE;
		mHexCodeBlock.setTextColor(textColor);
		mHexCodeBlock.setTextSize(50f);
		mHexCodeBlock.setText(mColorModel.getHexCode() + "\n");

		mInfoBlock = (TextView) findViewById(R.id.current_info);
		mInfoBlock.setBackgroundColor(mColor);
		mInfoBlock.setTextSize(30f);
		mInfoBlock.setTextColor(textColor);
		mInfoBlock.setText("RGB: \n" + rgb[0] + ", " + rgb[1] + ", " + rgb[2]
				+ "\n" + "CMYK: \n" + cmyk[0] + ", " + cmyk[1] + ", " + cmyk[2]
				+ ", " + cmyk[3] + "\n" + "HSV: \n" + hsv[0] + ", " + hsv[1]
				+ ", " + hsv[2]);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.color_picker, menu);
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
