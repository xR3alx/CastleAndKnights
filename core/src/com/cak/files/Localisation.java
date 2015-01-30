package com.cak.files;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class Localisation {

	private static HashMap<String, Properties> locals;
	private static String curLocal;
	
	public Localisation(String loadConfiguration) {
		locals = new HashMap<String, Properties>();
		for(FileHandle fileHandle : Gdx.files.internal(loadConfiguration + "/localisation/").list()){
			Properties props = new Properties();
			try {
				props.load(fileHandle.read());
			} catch (IOException e) {
				e.printStackTrace();
			}

			locals.put(fileHandle.nameWithoutExtension(), props);
		}
		
		curLocal = "English";
	}

	public static Properties getLocal(String name){
		return locals.get(name);
	}
	
	public static Properties getCurLocal(){
		return locals.get(curLocal);
	}
	
	public static String getCurLocalString(){
		return curLocal;
	}
	
	public static void setLocal(String local){
		curLocal = local;
	}
	
	public static Array<String> getLocals(){
		Array<String> languages = new Array<String>();
		for( String s : locals.keySet()){
			languages.add(s);
		}
		return languages;
	}
	
}