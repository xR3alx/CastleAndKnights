package com.cak.ingame;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.cak.bodydata.EnemyWeaponBodyData;
import com.cak.bodydata.EntityBodyData;
import com.cak.bodydata.EntityPlayerSensorBodyData;
import com.cak.bodydata.EntityTurnBodyData;
import com.cak.bodydata.FinishBodyData;
import com.cak.bodydata.GroundBodyData;
import com.cak.bodydata.InstantDeathBodyData;
import com.cak.bodydata.PlayerBodyData;
import com.cak.screens.IngameScreen;

public class ContactManager implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();
		
		if((bodyA.getUserData() instanceof PlayerBodyData || bodyA.getUserData() instanceof EntityBodyData) && bodyB.getUserData() instanceof GroundBodyData){   // Ground
			contact.setRestitution(0);
			if(bodyA.getPosition().y > (bodyB.getPosition().y + ((GroundBodyData) bodyB.getUserData()).height)){
				IngameScreen.getEntityManager().getPlayer().setCanJump(true);
			}
		}else if((bodyB.getUserData() instanceof PlayerBodyData || bodyB.getUserData() instanceof EntityBodyData) && bodyA.getUserData() instanceof GroundBodyData){
			contact.setRestitution(0);
			if(bodyB.getPosition().y > (bodyA.getPosition().y + ((GroundBodyData) bodyA.getUserData()).height)){
				IngameScreen.getEntityManager().getPlayer().setCanJump(true);
			}
		}else if(bodyA.getUserData() instanceof PlayerBodyData && bodyB.getUserData() instanceof FinishBodyData){   // Finish
			IngameScreen.setFinishedReached(true);
		}else if(bodyB.getUserData() instanceof PlayerBodyData && bodyA.getUserData() instanceof FinishBodyData){
			IngameScreen.setFinishedReached(true);
		}else if(bodyA.getUserData() instanceof PlayerBodyData && bodyB.getUserData() instanceof EntityPlayerSensorBodyData){   // EntityPlayerSensor
			if(!((EntityPlayerSensorBodyData) bodyB.getUserData()).follow){
				((EntityPlayerSensorBodyData) bodyB.getUserData()).playerId = ((PlayerBodyData) bodyA.getUserData()).entityId;
				((EntityPlayerSensorBodyData) bodyB.getUserData()).follow = true;
			}
		}else if(bodyB.getUserData() instanceof PlayerBodyData && bodyA.getUserData() instanceof EntityPlayerSensorBodyData){
			if(!((EntityPlayerSensorBodyData) bodyA.getUserData()).follow){
				((EntityPlayerSensorBodyData) bodyA.getUserData()).playerId = ((PlayerBodyData) bodyB.getUserData()).entityId;
				((EntityPlayerSensorBodyData) bodyA.getUserData()).follow = true;
			}
		}else if(bodyA.getUserData() instanceof PlayerBodyData && bodyB.getUserData() instanceof InstantDeathBodyData){   // InstantDeath
			IngameScreen.getEntityManager().getPlayer().health = 0;
		}else if(bodyB.getUserData() instanceof PlayerBodyData && bodyA.getUserData() instanceof InstantDeathBodyData){
			IngameScreen.getEntityManager().getPlayer().health = 0;
		}else if(bodyA.getUserData() instanceof EntityBodyData && bodyB.getUserData() instanceof EntityTurnBodyData){   // EntityTurn
			((EntityBodyData) bodyA.getUserData()).turn = true;
		}else if(bodyB.getUserData() instanceof EntityBodyData && bodyA.getUserData() instanceof EntityTurnBodyData){
			((EntityBodyData) bodyB.getUserData()).turn = true;
		}else if(bodyA.getUserData() instanceof PlayerBodyData && bodyB.getUserData() instanceof EnemyWeaponBodyData){   // EnemyWeapon
			if(((EnemyWeaponBodyData) bodyB.getUserData()).canAttack){
				((EnemyWeaponBodyData) bodyB.getUserData()).hitPlayer = true;
				((EnemyWeaponBodyData) bodyB.getUserData()).attackingId = ((PlayerBodyData) bodyA.getUserData()).entityId;
				((EnemyWeaponBodyData) bodyB.getUserData()).canAttack = false;
			}
		}else if(bodyB.getUserData() instanceof PlayerBodyData && bodyA.getUserData() instanceof EnemyWeaponBodyData){
			if(((EnemyWeaponBodyData) bodyA.getUserData()).canAttack){
				((EnemyWeaponBodyData) bodyA.getUserData()).hitPlayer = true;
				((EnemyWeaponBodyData) bodyA.getUserData()).attackingId = ((PlayerBodyData) bodyB.getUserData()).entityId;
				((EnemyWeaponBodyData) bodyA.getUserData()).canAttack = false;
			}
		}
	}
	
	@Override
	public void endContact(Contact contact) {
		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();		
		
		
		if(bodyA.getUserData() instanceof PlayerBodyData && bodyB.getUserData() instanceof GroundBodyData){   // Ground
			if(bodyA.getPosition().y > (bodyB.getPosition().y + ((GroundBodyData) bodyB.getUserData()).height)){
				IngameScreen.getEntityManager().getPlayer().setCanJump(false);
			}
		}else if(bodyB.getUserData() instanceof PlayerBodyData && bodyA.getUserData() instanceof GroundBodyData){
			if(bodyB.getPosition().y > (bodyA.getPosition().y + ((GroundBodyData) bodyA.getUserData()).height)){
				IngameScreen.getEntityManager().getPlayer().setCanJump(false);
			}
		}else if(bodyA.getUserData() instanceof PlayerBodyData && bodyB.getUserData() instanceof EnemyWeaponBodyData){   // EnemyWeapon
			((EnemyWeaponBodyData) bodyB.getUserData()).hitPlayer = false;
		}else if(bodyB.getUserData() instanceof PlayerBodyData && bodyA.getUserData() instanceof EnemyWeaponBodyData){
			((EnemyWeaponBodyData) bodyA.getUserData()).hitPlayer = false;
		}else if(bodyA.getUserData() instanceof PlayerBodyData && bodyB.getUserData() instanceof EntityPlayerSensorBodyData){   // EntityPlayerSensor
			if(((EntityPlayerSensorBodyData) bodyB.getUserData()).follow){
				((EntityPlayerSensorBodyData) bodyB.getUserData()).follow = false;
			}
		}else if(bodyB.getUserData() instanceof PlayerBodyData && bodyA.getUserData() instanceof EntityPlayerSensorBodyData){
			if(!((EntityPlayerSensorBodyData) bodyA.getUserData()).follow){
				((EntityPlayerSensorBodyData) bodyB.getUserData()).follow = false;
			}
		}
	}
	
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}
	
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}
	
}
