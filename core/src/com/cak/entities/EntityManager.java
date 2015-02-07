package com.cak.entities;

import java.util.ArrayList;

import box2dLight.RayHandler;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class EntityManager {

	private World world;
	private RayHandler rayHandler;
	private ArrayList<Entity> entities;
	
	private boolean playerSpawned;
	
	public EntityManager(World world, RayHandler rayHandler){
		entities = new ArrayList<Entity>();
		this.world = world;
		this.rayHandler = rayHandler;
	}
	
	public void update(boolean left, boolean right, boolean jump, boolean attack){
		if(playerSpawned){
			((Player) entities.get(0)).checkInput(left, right, jump, attack);
			((Player) entities.get(0)).update();
		}
		
		for(Entity entity : entities){
			entity.update();
		}
	}
	
	
	
	public void spawnEntity(String type, Vector2 position){
		if(type.equalsIgnoreCase("player")){
			entities.add(new Player(entities.size(), world, rayHandler, position));
			playerSpawned = true;
		}else if(type.equalsIgnoreCase("toad")){
			entities.add(new Toad(entities.size(), world, rayHandler, position));
		}
	}
	
	public int getEntityCount(){
		return entities.size();
	}
	
	public Entity getEntity(int entityNumber){
		return entities.get(entityNumber);
	}
	
	
	
	public Player getPlayer(){
		return ((Player) entities.get(0));
	}
	
	public boolean isPlayerSpawned(){
		return playerSpawned;
	}
	
}
