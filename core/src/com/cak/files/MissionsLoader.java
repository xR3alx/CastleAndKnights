package com.cak.files;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.cak.assets.Assets;

public class MissionsLoader {

	private HashMap<String, Mission> missions;
	
	public MissionsLoader() {
		
		missions = new HashMap<String, Mission>();
		
		Properties props = new Properties();
		try {
			for(FileHandle file : Gdx.files.internal(Assets.loadConfiguration.replace("res", "missions")).list()){
				props.load(file.read());
				
				if(props.containsKey("name") && props.containsKey("description")){
					String name = props.getProperty("name");
					String description = props.getProperty("description");
					String mappath = props.getProperty("mappath");
					boolean retryable = Boolean.parseBoolean(props.getProperty("retryable"));
					int moneyReward = Integer.parseInt(props.getProperty("moneyreward"));
					int expReward = Integer.parseInt(props.getProperty("expreward"));
					int difficulty = Integer.parseInt(props.getProperty("difficulty"));
					int minLevel = Integer.parseInt(props.getProperty("minlevel"));
					int maxLevel = Integer.parseInt(props.getProperty("maxlevel"));
					
					Mission mission = new Mission();
					mission.name = name;
					mission.description = description;
					mission.mappath = mappath;
					mission.moneyReward = moneyReward;
					mission.difficulty = difficulty;
					mission.minLevel = minLevel;
					mission.maxLevel = maxLevel;
					mission.expReward = expReward;
					mission.retryable = retryable;
					
					missions.put(name, mission);
				}else{
					Gdx.app.log("ERROR", "Couldn't load file: " + file.path());
					Gdx.app.exit();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, Mission> getMissions() {
		return missions;
	}
	
	
	public class Mission {
		public String name, description, mappath;
		public int moneyReward, expReward, difficulty, minLevel, maxLevel;
		public boolean retryable;
	}
}
