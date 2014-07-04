package com.example.scheme;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ComplexPreferences {
	private static ComplexPreferences complexPreferences;
	private final Context context;
	private final SharedPreferences preferences;
	private final SharedPreferences.Editor editor;
	private static Gson GSON = new Gson();
	java.lang.reflect.Type typeOfObject = (new TypeToken<Object>() {
	}).getType();

	private ComplexPreferences(Context context, String namePreferences, int mode) {
		this.context = context;
		if (namePreferences == null || namePreferences.equals("")) {
			namePreferences = "abhan";
		}
		preferences = context.getSharedPreferences(namePreferences, mode);
		editor = preferences.edit();
	}

	public static ComplexPreferences getComplexPreferences(Context context,
			String namePreferences, int mode) {
		if (complexPreferences == null) {
			complexPreferences = new ComplexPreferences(context,
					namePreferences, mode);
		}
		return complexPreferences;
	}

	public void putObject(String key, Object object) {
		if (object == null) {
			throw new IllegalArgumentException("Object is null");
		}
		if (key.equals("") || key == null) {
			throw new IllegalArgumentException("Key is empty or null");
		}
		editor.putString(key, GSON.toJson(object));
		editor.commit();
		Log.d("fowe", "class of "+key+" :"+ object.getClass());
	}

	public void commit() {
		editor.commit();
	}

	public <T> T getObject(String key, Class<T> a) {
		String gson = preferences.getString(key, null);
		if (gson == null) {
			return null;
		} else {
			try {
				return GSON.fromJson(gson, a);
			} catch (Exception e) {
				throw new IllegalArgumentException("Object stored with key "
						+ key + " is instance of other class");
			}
		}
	}
}
