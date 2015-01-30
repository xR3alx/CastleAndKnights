package com.cak.entities;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
import com.cak.bodydata.PlayerBodyData;
import com.cak.bodydata.WeaponBodyData;
import com.cak.files.UserData;
import com.cak.ingame.MapManager;
import com.cak.screens.IngameScreen;
import com.cak.worldobjects.ContactFilters;

public class Player extends Entity{
	
	private PlayerBodyData playerBodyData;
	private boolean attacking, move, left;
	private PointLight light;
	
	private int stamina, attackingStamina;

	public Player(World world, RayHandler rayHandler, Vector2 position) {
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
		shape.setAsBox(40 / IngameScreen.PIXELS_TO_METERS, 60 / IngameScreen.PIXELS_TO_METERS);
		fixtureDef.shape = shape;
		
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		
		playerBodyData = new PlayerBodyData(Assets.get("ingame/gfx/animations/player_notex.pack", TextureAtlas.class).getRegions(), 1 / 10f, PlayMode.LOOP, true, 30 * MapManager.unitScale, 42 * MapManager.unitScale);
		body.setUserData(playerBodyData);
		
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

		weaponBodyData = new WeaponBodyData(Assets.get("ingame/gfx/animations/weapons/sword_death_blade.pack", TextureAtlas.class).getRegions(), 1 / 10f, PlayMode.NORMAL, true, 64f * MapManager.unitScale, 64f * MapManager.unitScale);
		weaponBodyData.stop();
		weaponBody.setUserData(weaponBodyData);
	
		shape.dispose();
		shapeWeapon.dispose();
		
		
		light = new PointLight(rayHandler, 20, new Color(1f, 0.7f, 0.3f, 0.7f), 30, 0, 0);
		light.setSoftnessLength(1f);
		light.setXray(true);
		light.attachToBody(body);
		
		health = UserData.getInt("playerhealth");// TODO add Weapon and armor bonus
	}
	
	@Override
	public void update() {
		super.update();
		body.setTransform(body.getPosition(), 0);
		
		if(attacking){
			if(attackingStamina > 100){
				weaponBodyData.start();
				attackingStamina = 0;
			}
			
			if(weaponBodyData.isFinished(Gdx.graphics.getDeltaTime(), true)){
				weaponBodyData.stop();
				attacking = false;
			}
		}else{
			if(attackingStamina <= 100){
				attackingStamina+=51;
			}
		}
		if(!left){
			weaponBody.setTransform(new Vector2(body.getPosition().x + (65 / IngameScreen.PIXELS_TO_METERS), body.getPosition().y + (15 / IngameScreen.PIXELS_TO_METERS)), weaponBody.getAngle());
			weaponBodyData.setFlip(false, false);
		}else if(left){
			weaponBody.setTransform(new Vector2(body.getPosition().x - (65 / IngameScreen.PIXELS_TO_METERS), body.getPosition().y + (15 / IngameScreen.PIXELS_TO_METERS)), weaponBody.getAngle());
			weaponBodyData.setFlip(true, false);
		}
		
		if(canJump){
			if(stamina > 100){
				if(jumped){
					animationFalling = false;
					animationRun = false;
						
					canJump = false;
					jumped = false;
					stamina = 0;
					body.applyForceToCenter(0, 3250, true);
				}
			}else{
				stamina+=12;
			}
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

	public void checkInput(boolean left, boolean right, boolean jump, boolean attack){
		
		if(Gdx.app.getType() == ApplicationType.Desktop){
			if(Gdx.input.isKeyPressed(Keys.A)){
				left = true;
			}
			if(Gdx.input.isKeyPressed(Keys.D)){
				right = true;
			}
			if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
				jump = true;
			}
		}
		
		if(left){
			body.setLinearVelocity(-8, body.getLinearVelocity().y);
			move = true;
			this.left = true;
		}
		if(right){
			body.setLinearVelocity(+8, body.getLinearVelocity().y);
			move = true;
			this.left = false;
		}
		
		if(move && !left && !right){
			body.setLinearVelocity(0, 0);
			move = false;
		}
		
		
		if(jump && !jumped && canJump && stamina > 100){
			jumped = true;
		}
		
		if(attack && !attacking && attackingStamina > 100){
			attacking = true;
		}
		
	}
	
	
	
	
	public boolean isCanJump() {
		return canJump;
	}

	public void setCanJump(boolean jump) {
		canJump = jump;
	}
}
