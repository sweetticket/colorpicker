package com.example.scheme;

import java.util.ArrayList;

public class PaletteModel extends ArrayList<Integer> {

	private static final long serialVersionUID = 1L;
	private String mName;
	
	public PaletteModel(String name) {
		super();
		mName = name;
	}
	
	public PaletteModel(String name, int Color){
		this(name);
		this.add(Color);
	}
	
	public String getName(){
		return mName;
	}
	
	public void setName(String name){
		mName = name;
	}

}
