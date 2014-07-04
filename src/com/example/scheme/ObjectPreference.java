package com.example.scheme;

import android.app.Application;
import android.util.Log;

public class ObjectPreference extends Application {
	private static final String TAG = "ObjectPreference";
	private ComplexPreferences complexPreferences = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		complexPreferences = ComplexPreferences.getComplexPreferences(getBaseContext(), "abhan", MODE_PRIVATE);
		Log.i(TAG, "Preference Created");
	}
	
	public ComplexPreferences getComplexPreference(){
		if (complexPreferences != null){
			return complexPreferences;
		}
		return null;
	}

}
