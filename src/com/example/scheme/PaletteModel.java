package com.example.scheme;

import java.util.ArrayList;

public class PaletteModel {

	private static final long serialVersionUID = 1L;
	private String mName;
	private ArrayList<Integer> mColorArr;
	
	public PaletteModel(String name) {
		mName = name;
		mColorArr = new ArrayList<Integer>();
	}
	
	public PaletteModel(String name, int Color){
		this(name);
		mColorArr = new ArrayList<Integer>();
		mColorArr.add(Color);
	}
	
	public String getName(){
		return mName;
	}
	
	public void setName(String name){
		mName = name;
	}
	
	public void add(int Color){
		mColorArr.add(Color);
	}
	
	public ArrayList<Integer> getColors(){
		return mColorArr;
	}
	
	public int size(){
		return mColorArr.size();
	}

}
