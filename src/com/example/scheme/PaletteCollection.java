package com.example.scheme;

import java.util.ArrayList;

public class PaletteCollection {
	private ArrayList<String> collection;
	public PaletteCollection() {
		collection = new ArrayList<String>();
	}
	
	public PaletteCollection(String name){
		collection = new ArrayList<String>();
		collection.add(name);
	}
	
	public void add(String name){
		collection.add(name);
	}
	
	public ArrayList<String> getCollection(){
		return collection;
	}

}
