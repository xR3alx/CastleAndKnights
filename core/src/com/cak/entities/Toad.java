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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.cak.assets.Assets;
import com.cak.bodydata.EnemyWeaponBodyData;
import com.cak.bodydata.EntityBodyData;
import com.cak.ingame.MapManager;
import com.cak.screens.IngameScreen;
import com.cak.worldobjects.ContactFilters;

public class Toad extends Entity {

	private EntityBodyData entityBodyData;
	private EnemyWeaponBodyData enemyWeaponBodyData;
	private boolean move, left, right, attack;
	private PointLight light;
	
	private int attackingStamina;

	public Toad(World world, RayHandler rayHandler, Vector2 position) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(position.x, position.y);
		bodyDef.type = BodyType.DynamicBody;
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 0.0f;
		fixtureDef.filter.categoryBits = ContactFilters.CAT_ENTITIES;
		fixtureDef.filter.groupIndex = ContactFilters.GROUP_ENTITIES;
		fixtureDef.filter.maskBits = ContactFilters.MASK_ENTITIES;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(20 / IngameScreen.PIXELS_TO_METERS, 30 / IngameScreen.PIXELS_TO_METERS);
		fixtureDef.shape = shape;
		
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		
		entityBodyData = new EntityBodyData(Assets.get("ingame/gfx/animations/player_notex.pack", TextureAtlas.class).getRegions(), 1 / 10f, PlayMode.LOOP, true, 12 * MapManager.unitScale, 20 * MapManager.unitScale);
		body.setUserData(entityBodyData);
		
		BodyDef bodyDefWeapon = new BodyDef();
		bodyDefWeapon.position.set(body.getPosition().x + (65 / IngameScreen.PIXELS_TO_METERS), body.getPosition().y + (15 / IngameScreen.PIXELS_TO_METERS));
		bodyDefWeapon.type = BodyType.DynamicBody;
		
		FixtureDef fixtureDefWeapon = new FixtureDef();
		fixtureDefWeapon.density = 1.0f;
		fixtureDefWeapon.friction = 0.0f;
		fixtureDefWeapon.restitution = 0.0f;
		fixtureDefWeapon.filter.categoryBits = ContactFilters.CAT_ENTITIES;
		fixtureDefWeapon.filter.groupIndex = ContactFilters.GROUP_ENTITIES;
		fixtureDefWeapon.filter.maskBits = ContactFilters.MASK_ENTITIES;
		fixtureDefWeapon.isSensor = true;
		PolygonShape shapeWeapon = new PolygonShape();
		shapeWeapon.setAsBox(50 / IngameScreen.PIXELS_TO_METERS, 50 / IngameScreen.PIXELS_TO_METERS);
		fixtureDefWeapon.shape = shapeWeapon;
		
		weaponBody = world.createBody(bodyDefWeapon);
		weaponBody.createFixture(fixtureDefWeapon);
		
		weaponBody.setGravityScale(0);

		enemyWeaponBodyData = new EnemyWeaponBodyData(Assets.get("ingame/gfx/animations/weapons/sword_death_blade.pack", TextureAtlas.class).getRegions(), 1 / 10f, PlayMode.NORMAL, true, 64f * MapManager.unitScale, 64f * MapManager.unitScale);
		enemyWeaponBodyData.stop();
		weaponBody.setUserData(enemyWeaponBodyData);
	
		shape.dispose();
		shapeWeapon.dispose();
		
		
		light = new PointLight(rayHandler, 20, new Color(1f, 0.7f, 0.3f, 0.7f), 15, 0, 0);
		light.setSoftnessLength(1f);
		light.setXray(true);
		light.attachToBody(body);
		
		health = 10;
		movespeed = 4;
		
		int randomMove = new Random().nextInt(2);
		if(randomMove == 0){
			right = true;
		}else{
			left = true;
		}
	}
	
	@Override
	public void update() {
		super.update();
		if(entityBodyData.turn){
			turn();
		}
		checkInput();
		body.setTransform(body.getPosition(), 0);
		
		if(enemyWeaponBodyData.attacking && attackingStamina >= 100){
			if(attackingStamina > 100){
				enemyWeaponBodyData.start();
				attackingStamina = 0;
			}
			
			if(enemyWeaponBodyData.isFinished(Gdx.graphics.getDeltaTime(), true)){
				enemyWeaponBodyData.stop();
				enemyWeaponBodyData.attacking = false;
			}
		}else{
			if(attackingStamina <= 100){
				attackingStamina+=51;
			}
		}
		if(right){
			weaponBody.setTransform(new Vector2(body.getPosition().x + (65 / IngameScreen.PIXELS_TO_METERS), body.getPosition().y + (15 / IngameScreen.PIXELS_TO_METERS)), weaponBody.getAngle());
			enemyWeaponBodyData.setFlip(false, false);
		}else if(left){
			weaponBody.setTransform(new Vector2(body.getPosition().x - (65 / IngameScreen.PIXELS_TO_METERS), body.getPosition().y + (15 / IngameScreen.PIXELS_TO_METERS)), weaponBody.getAngle());
			enemyWeaponBodyData.setFlip(true, false);
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
			this.left = true;
		}
		if(right){
			body.setLinearVelocity(+movespeed, body.getLinearVelocity().y);
			move = true;
			this.left = false;
		}
		
		if(move && !left && !right){
			body.setLinearVelocity(0, 0);
			move = false;
		}
	}
	
	
	
	public void turn(){
		if(right){
			left = true;
			right = false;
		}else{
			right = true;
			left = false;
		}
		
		entityBodyData.turn = false;
	}
}
