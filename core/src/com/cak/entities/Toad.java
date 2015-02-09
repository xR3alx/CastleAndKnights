package com.cak.entities;

import java.util.Random;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.cak.assets.Assets;
import com.cak.bodydata.EnemyWeaponBodyData;
import com.cak.bodydata.EntityBodyData;
import com.cak.bodydata.EntityPlayerSensorBodyData;
import com.cak.ingame.MapManager;
import com.cak.screens.IngameScreen;
import com.cak.worldobjects.ContactFilters;

public class Toad extends Entity {

	private EntityBodyData entityBodyData;
	private EnemyWeaponBodyData enemyWeaponBodyData;
	private EntityPlayerSensorBodyData entityPlayerSensorBodyData;
	private boolean move, left, right;
	private PointLight light;
	
	private int attackingStamina, followTimer;

	public Toad(int id, World world, RayHandler rayHandler, Vector2 position) {
		super(id);
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(position.x, position.y);
		bodyDef.type = BodyType.DynamicBody;
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 0.0f;
		fixtureDef.filter.categoryBits = ContactFilters. CAT_ENTITIES;
		fixtureDef.filter.groupIndex = ContactFilters.GROUP_ENTITIES;
		fixtureDef.filter.maskBits = ContactFilters.MASK_ENTITIES;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(20 / IngameScreen.PIXELS_TO_METERS, 30 / IngameScreen.PIXELS_TO_METERS);
		fixtureDef.shape = shape;
		
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		
		entityBodyData = new EntityBodyData(id, Assets.get("/ingame/gfx/animations/player_notex.pack", TextureAtlas.class).getRegions(), 1 / 10f, PlayMode.LOOP, true, 12 * MapManager.unitScale, 20 * MapManager.unitScale);
		body.setUserData(entityBodyData);
		
		
		
		
		BodyDef bodyDefWeapon = new BodyDef();
		bodyDefWeapon.position.set(body.getPosition().x + (65 / IngameScreen.PIXELS_TO_METERS), body.getPosition().y + (15 / IngameScreen.PIXELS_TO_METERS));
		bodyDefWeapon.type = BodyType.DynamicBody;
		
		FixtureDef fixtureDefWeapon = new FixtureDef();
		fixtureDefWeapon.density = 1.0f;
		fixtureDefWeapon.friction = 0.0f;
		fixtureDefWeapon.restitution = 0.0f;
		fixtureDefWeapon.isSensor = true;
		PolygonShape shapeWeapon = new PolygonShape();
		shapeWeapon.setAsBox(50 / IngameScreen.PIXELS_TO_METERS, 50 / IngameScreen.PIXELS_TO_METERS);
		fixtureDefWeapon.shape = shapeWeapon;
		
		weaponBody = world.createBody(bodyDefWeapon);
		weaponBody.createFixture(fixtureDefWeapon);
		
		weaponBody.setGravityScale(0);

		enemyWeaponBodyData = new EnemyWeaponBodyData(Assets.get("/ingame/gfx/animations/weapons/sword_death_blade.pack", TextureAtlas.class).getRegions(), 1 / 10f, PlayMode.NORMAL, true, 64f * MapManager.unitScale, 64f * MapManager.unitScale);
		enemyWeaponBodyData.stop();
		weaponBody.setUserData(enemyWeaponBodyData);
	
		
		
		
		BodyDef bodyDefPlayerSensor = new BodyDef();
		bodyDefPlayerSensor.position.set(body.getPosition().x + (65 / IngameScreen.PIXELS_TO_METERS), body.getPosition().y + (15 / IngameScreen.PIXELS_TO_METERS));
		bodyDefPlayerSensor.type = BodyType.DynamicBody;
		
		FixtureDef fixtureDefPlayerSensor = new FixtureDef();
		fixtureDefPlayerSensor.density = 1.0f;
		fixtureDefPlayerSensor.friction = 0.0f;
		fixtureDefPlayerSensor.restitution = 0.0f;
		fixtureDefPlayerSensor.isSensor = true;
		CircleShape shapePlayerSensor = new CircleShape();
		shapePlayerSensor.setRadius(10f);
		fixtureDefPlayerSensor.shape = shapePlayerSensor;
		
		playerSensorBody = world.createBody(bodyDefPlayerSensor);
		playerSensorBody.createFixture(fixtureDefPlayerSensor);
		
		entityPlayerSensorBodyData = new EntityPlayerSensorBodyData();
		playerSensorBody.setUserData(entityPlayerSensorBodyData);
		

		shape.dispose();
		shapeWeapon.dispose();
		shapePlayerSensor.dispose();
		
		
		
		light = new PointLight(rayHandler, 20, new Color(1f, 0.7f, 0.3f, 0.7f), 15, 0, 0);
		light.setSoftnessLength(1f);
		light.setXray(true);
		light.attachToBody(body);
		
		health = 10;
		movespeed = 4;
		enemyWeaponBodyData.damage = 5f;
		enemyWeaponBodyData.canAttack = true;
		
		turnRandom();
	}
	
	@Override
	public void update() {
		super.update();
		checkInput();
		body.setTransform(body.getPosition(), 0);
		playerSensorBody.setTransform(body.getPosition(), 0);
		
		if(!entityBodyData.turn){
			if(entityPlayerSensorBodyData.follow){
				if(followTimer != 0){
					Entity followEntity = IngameScreen.getEntityManager().getEntity(entityPlayerSensorBodyData.playerId);
					
					if(followEntity.body.getPosition().x+2.5f < body.getPosition().x){
						if(!left){
							right = true;
							turn();
						}
					}else if(followEntity.body.getPosition().x-2.5f > body.getPosition().x){
						if(!right){
							left = true;
							turn();
						}
					}else{
						left = false;
						right = false;
					}
				}else{
					followTimer--;
				}
			}
		}else{
			entityPlayerSensorBodyData.follow = false;
			followTimer = 25;
			turn();
		}
		
		
		if(enemyWeaponBodyData.hitPlayer){
			if(attackingStamina >= 100){
				enemyWeaponBodyData.start();
				attackingStamina = 0;

				if(!enemyWeaponBodyData.damaged){
					Entity attackedEntity = IngameScreen.getEntityManager().getEntity(enemyWeaponBodyData.attackingId);
					attackedEntity.health-=enemyWeaponBodyData.damage;
					enemyWeaponBodyData.damaged = true;
				}
				
				if(enemyWeaponBodyData.isFinished(Gdx.graphics.getDeltaTime(), true)){
					enemyWeaponBodyData.damaged = false;
					enemyWeaponBodyData.stop();
				}
			}else{
				if(attackingStamina <= 100){
					attackingStamina+=51;
				}
			}
		}else{
			enemyWeaponBodyData.canAttack = true;
			enemyWeaponBodyData.reset();
			enemyWeaponBodyData.stop();
		}
		
		
		if(left){
			weaponBody.setTransform(new Vector2(body.getPosition().x - (65 / IngameScreen.PIXELS_TO_METERS), body.getPosition().y + (15 / IngameScreen.PIXELS_TO_METERS)), weaponBody.getAngle());
			enemyWeaponBodyData.setFlip(true, false);
		}else if(right){
			weaponBody.setTransform(new Vector2(body.getPosition().x + (65 / IngameScreen.PIXELS_TO_METERS), body.getPosition().y + (15 / IngameScreen.PIXELS_TO_METERS)), weaponBody.getAngle());
			enemyWeaponBodyData.setFlip(false, false);
		}
		
		
		if(body.getLinearVelocity().y < 0){
			if(!animationFalling){
				animationJump = false;
				animationRun = false;
				animationFalling = true;
//				playerBodyData.changeAnimation(Assets.get("game/gfx/animations/neonjumper_falling.pack", TextureAtlas.class).getRegions(), 1 / 10f);
			}
		}
	}

	public void checkInput(){
		if(left){
			body.setLinearVelocity(-movespeed, body.getLinearVelocity().y);
			move = true;
		}
		if(right){
			body.setLinearVelocity(+movespeed, body.getLinearVelocity().y);
			move = true;
		}
		
		if(move && !left && !right){
			body.setLinearVelocity(0, 0);
			move = false;
		}
	}
	
	
	
	public void turnRandom(){
		int randomMove = new Random().nextInt(2);
		if(randomMove == 0){
			right = true;
		}else{
			left = true;
		}
	}
	
	public void turn(){
		if(right){
			this.left = true;
			right = false;
		}else{
			right = true;
			this.left = false;
		}
		
		entityBodyData.turn = false;
	}
}
