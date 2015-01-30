package com.cak.files;

import java.io.IOException;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class UserData {

	private static FileHandle file;
	private static Properties properties;
	
	public UserData(){
		
		file = Gdx.files.local("userdata.yml");
		
		if(!file.exists()){
			try {
				file.file().createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
//		RACipher.decryptFile(file);
		
		properties = new Properties();
		try {
			properties.load(file.read());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getString(String property){
		return properties.getProperty(property);
	}
	
	public static Boolean getBoolean(String property){
		return Boolean.parseBoolean(properties.getProperty(property));
	}
	
	public static Float getFloat(String property){
		if(properties.containsKey(property)){
			return Float.parseFloat(properties.getProperty(property));
		}else{
			return 0f;
		}
	}
	
	public static Integer getInt(String property){
		if(properties.containsKey(property)){
			return Integer.parseInt(properties.getProperty(property));
		}else{
			return 0;
		}
	}
	
	public static void setProperty(String key, String value){
		properties.setProperty(key, value);
	}
	
	public static void save(){
		try {
			properties.store(file.write(false), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		RACipher.encryptFile(file);
	}
}
