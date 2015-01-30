package com.cak.files;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.cak.assets.Assets;

public class ItemLoader {

	private HashMap<String, Item> weapons, armors, potions;
	
	public ItemLoader() {
		
		weapons = new HashMap<String, Item>();
		armors = new HashMap<String, Item>();
		potions = new HashMap<String, Item>();
		
		Properties props = new Properties();
		try {
			for(FileHandle file : Gdx.files.internal(Assets.loadConfiguration.replace("res", "items")).list()){
				props.load(file.read());
				
				if(props.containsKey("name") && props.containsKey("type") && props.containsKey("cost")){
					String name = props.getProperty("name");
					String type = props.getProperty("type");
					int cost = Integer.parseInt(props.getProperty("cost"));
					int castleBuildingLevelNeed = Integer.parseInt(props.getProperty("buildinglevel"));
					Texture texture = null;
					if(!props.getProperty("texture").equals("")){
						texture = Assets.get(props.getProperty("texture").replace("$", "/"), Texture.class);
					}
					
					if(type.equalsIgnoreCase("weapon")){
						WeaponItem item = new WeaponItem();
						item.name = name;
						item.type = type;
						item.cost = cost;
						item.castleBuildingLevelNeed = castleBuildingLevelNeed;
						item.texture = texture;
						
						item.damage = Float.parseFloat(props.getProperty("damage"));
						item.healthIncrease = Integer.parseInt(props.getProperty("healthincrease"));
						item.strengthIncrease = Integer.parseInt(props.getProperty("strengthincrease"));
						item.agilityIncrease = Integer.parseInt(props.getProperty("agilityincrease"));
						
						weapons.put(name, item);
					}else if(type.equalsIgnoreCase("armor")){
						ArmorItem item = new ArmorItem();
						item.name = name;
						item.type = type;
						item.cost = cost;
						item.castleBuildingLevelNeed = castleBuildingLevelNeed;
						item.texture = texture;
						
						item.defence = Float.parseFloat(props.getProperty("defence"));
						item.healthIncrease = Integer.parseInt(props.getProperty("healthincrease"));
						item.strengthIncrease = Integer.parseInt(props.getProperty("strengthincrease"));
						item.agilityIncrease = Integer.parseInt(props.getProperty("agilityincrease"));
						
						armors.put(name, item);
					}else if(type.equalsIgnoreCase("potion")){
						PotionItem item = new PotionItem();
						item.name = name;
						item.type = type;
						item.cost = cost;
						item.castleBuildingLevelNeed = castleBuildingLevelNeed;
						item.texture = texture;
						
						item.heal = Float.parseFloat(props.getProperty("heal"));
						item.strengthIncrease = Float.parseFloat(props.getProperty("strengthincrease"));
						item.agilityIncrease = Float.parseFloat(props.getProperty("agilityincrease"));
						item.duration = Integer.parseInt(props.getProperty("duration"));
						
						potions.put(name, item);
					}
				}else{
					Gdx.app.log("ERROR", "Couldn't load file: " + file.path());
					Gdx.app.exit();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, Item> getWeapons() {
		return weapons;
	}
	
	public HashMap<String, Item> getArmors() {
		return armors;
	}
	
	public HashMap<String, Item> getPotions() {
		return potions;
	}
	
	
	
	
	public class Item {
		public String name, type;
		public int cost, castleBuildingLevelNeed;
		public Texture texture;
	}
	
	public class WeaponItem extends Item{
		public float damage;
		public int healthIncrease, strengthIncrease, agilityIncrease;
	}
	
	public class ArmorItem extends Item{
		public float defence;
		public int healthIncrease, strengthIncrease, agilityIncrease;
	}
	
	public class PotionItem extends Item{
		public float heal, strengthIncrease, agilityIncrease;
		public int duration;
	}
}
