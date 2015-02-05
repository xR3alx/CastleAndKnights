package com.cak.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.cak.bodydata.WeaponBodyData;

public class Entity {

	public WeaponBodyData weaponBodyData;
	public boolean jumped, canJump;
	public boolean animationFalling, animationJump, animationRun, animationAttack;
	public Body body, weaponBody, playerSensorBody;
	public int health, movespeed;
	public int id;
	
	public Entity(int id) {
		this.id = id;
	}
	
	public void update(){
		
	}
}
