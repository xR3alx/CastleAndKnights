package com.cak.entities;

import java.util.ArrayList;

import box2dLight.RayHandler;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class EntityManager {

	private World world;
	private RayHandler rayHandler;
	private ArrayList<Entity> entities;
	private Player player;
	
	private boolean playerSpawned;
	private int entityCount;
	
	public EntityManager(World world, RayHandler rayHandler){
		entities = new ArrayList<Entity>();
		this.world = world;
		this.rayHandler = rayHandler;
	}
	
	public void update(boolean left, boolean right, boolean jump, boolean attack){
		if(playerSpawned){
			player.checkInput(left, right, jump, attack);
			player.update();
		}
		
		for(Entity entity : entities){
			entity.update();
		}
	}
	
	
	
	public void spawnEntity(String type, Vector2 position){
		if(type.equalsIgnoreCase("player")){
			player = new Player(entityCount, world, rayHandler, position);
			playerSpawned = true;
		}else if(type.equalsIgnoreCase("toad")){
			entities.add(new Toad(entityCount, world, rayHandler, position));
		}
		entityCount++;
	}
	
	public int getEntityCount(){
		return entityCount;
	}
	
	public Entity getEntity(int entityNumber){
		return entities.get(entityNumber);
	}
	
	
	
	public Player getPlayer(){
		return player;
	}
	
	public boolean isPlayerSpawned(){
		return playerSpawned;
	}
	
}
