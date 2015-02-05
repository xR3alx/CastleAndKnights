package com.cak.screens;

import box2dLight.RayHandler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.cak.assets.Assets;
import com.cak.bodydata.EnemyWeaponBodyData;
import com.cak.bodydata.EntityBodyData;
import com.cak.bodydata.EntityPlayerSensorBodyData;
import com.cak.bodydata.EntityTurnBodyData;
import com.cak.bodydata.FinishBodyData;
import com.cak.bodydata.GroundBodyData;
import com.cak.bodydata.InstantDeathBodyData;
import com.cak.bodydata.PlayerBodyData;
import com.cak.bodyuserdata.DrawUserData;
import com.cak.entities.EntityManager;
import com.cak.files.MissionsLoader.Mission;
import com.cak.files.UserData;
import com.cak.ingame.GUIManager;
import com.cak.ingame.MapManager;
import com.cak.main.Main;

public class IngameScreen implements Screen {
	
	private SpriteBatch spriteBatch;
	
	
	private World world;
	private RayHandler rayHandler;
	private Box2DDebugRenderer debugRenderer;
	
	
	private static EntityManager entityManager;
	private MapManager mapManager;
	private GUIManager guiManager;
	
	
	
	private Mission mission;
	private Array<Body> tempBodies;
	private boolean isLoaded, finishReached;
	private int monsterKillMoneyReward, monsterKillExpReward;
	
	public static final float PIXELS_TO_METERS = 32, METERS_TO_PIXELS = 0.32f;
	
	public final Vector2 gravity = new Vector2(0, -9.81f);
	private final float timeStep = 1/30f;
	private final int velocityIterations = 3, positionIterations = 3;
	public boolean worldSleep = true;
	
	public IngameScreen(Mission mission) {
		this.mission = mission;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void show() {
		spriteBatch = new SpriteBatch();
			
		world = new World(gravity, worldSleep);
		contactListener();
		tempBodies = new Array<Body>();
		rayHandler = new RayHandler(world);
		rayHandler.setGammaCorrection(true);
		debugRenderer = new Box2DDebugRenderer();
			
		guiManager = new GUIManager();
		entityManager = new EntityManager(world, rayHandler);
		mapManager = new MapManager("/ingame/maps/" + mission.mappath + ".tmx");
		mapManager.setupMap(rayHandler, world, entityManager);
			
			
		isLoaded = true;
	}

	@Override
	public void render(float delta) {
		if(isLoaded){
			update();
			mapManager.render(entityManager.getPlayer().body.getPosition());
			world.getBodies(tempBodies);
			spriteBatch.setProjectionMatrix(mapManager.getCamera().combined);
			spriteBatch.begin();
				for (Body body : tempBodies) {
					if(body.getUserData() instanceof DrawUserData){
						if(body.getPosition().x > (mapManager.getCamera().position.x - mapManager.getCamera().viewportWidth - 300) && body.getPosition().y > (mapManager.getCamera().position.y - mapManager.getCamera().viewportHeight - 300)
								&& body.getPosition().x < (mapManager.getCamera().position.x + mapManager.getCamera().viewportWidth + 300) && body.getPosition().y < (mapManager.getCamera().position.y + mapManager.getCamera().viewportHeight + 300)){
							((DrawUserData) body.getUserData()).render(spriteBatch, delta, body.getPosition().x, body.getPosition().y, body.getAngle());
						}
					}
				}
				mapManager.renderParticles(spriteBatch, delta);
			spriteBatch.end();
			rayHandler.render();
			
			debugRenderer.render(world, mapManager.getCamera().combined);
			guiManager.render(entityManager.getPlayer());
			
			
			
			
			if(finishReached){
				UserData.setProperty("currentmoney", UserData.getInt("currentmoney") + mission.moneyReward + monsterKillMoneyReward + "");
				UserData.setProperty("levelprogression_current", UserData.getInt("levelprogression_current") + mission.expReward + monsterKillExpReward + "");
				Main.notificationsManager.addNotification("Mission finished!", "You finished the mission " + mission.name + "\n and got a reward of $" + (mission.moneyReward + monsterKillMoneyReward) + " and EXP " + (mission.expReward + monsterKillExpReward));
				
				Main.changeScreen(new MenuScreen());
			}else if(entityManager.getPlayer().health <= 0){
				Main.notificationsManager.addNotification("Mission failed!", "You failed the mission. Be careful next time!");
				
				Main.changeScreen(new MenuScreen());
			}else if(Gdx.input.isKeyJustPressed(Keys.F12)){
				Main.changeScreen(new MenuScreen());
			}
		}
	}
	
	private void update(){
		world.step(timeStep, velocityIterations, positionIterations);
		rayHandler.setCombinedMatrix(mapManager.getCamera());
		rayHandler.update();
		entityManager.update(guiManager.isMoveLeftPressed(), guiManager.isMoveRightPressed(), guiManager.isJumpPressed(), guiManager.isAttackPressed());
	}
	
	@Override
	public void resize(int width, int height) {
		if(isLoaded){
			guiManager.resize();
		}
	}

	
	private void contactListener(){
		ContactListener contactListener = new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				Body bodyA = contact.getFixtureA().getBody();
				Body bodyB = contact.getFixtureB().getBody();
				
				if((bodyA.getUserData() instanceof PlayerBodyData || bodyA.getUserData() instanceof EntityBodyData) && bodyB.getUserData() instanceof GroundBodyData){   // Ground
					contact.setRestitution(0);
					if(bodyA.getPosition().y > (bodyB.getPosition().y + ((GroundBodyData) bodyB.getUserData()).height)){
						entityManager.getPlayer().setCanJump(true);
					}
				}else if((bodyB.getUserData() instanceof PlayerBodyData || bodyB.getUserData() instanceof EntityBodyData) && bodyA.getUserData() instanceof GroundBodyData){
					contact.setRestitution(0);
					if(bodyB.getPosition().y > (bodyA.getPosition().y + ((GroundBodyData) bodyA.getUserData()).height)){
						entityManager.getPlayer().setCanJump(true);
					}
				}else if(bodyA.getUserData() instanceof PlayerBodyData && bodyB.getUserData() instanceof FinishBodyData){   // Finish
					finishReached = true;
				}else if(bodyB.getUserData() instanceof PlayerBodyData && bodyA.getUserData() instanceof FinishBodyData){
					finishReached = true;
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
					entityManager.getPlayer().health = 0;
				}else if(bodyB.getUserData() instanceof PlayerBodyData && bodyA.getUserData() instanceof InstantDeathBodyData){
					entityManager.getPlayer().health = 0;
				}else if(bodyA.getUserData() instanceof EntityBodyData && bodyB.getUserData() instanceof EntityTurnBodyData){   // EntityTurn
					((EntityBodyData) bodyA.getUserData()).turn = true;
				}else if(bodyB.getUserData() instanceof EntityBodyData && bodyA.getUserData() instanceof EntityTurnBodyData){
					((EntityBodyData) bodyB.getUserData()).turn = true;
				}else if(bodyA.getUserData() instanceof PlayerBodyData && bodyB.getUserData() instanceof EnemyWeaponBodyData){   // EnemyWeapon
					if(((EnemyWeaponBodyData) bodyB.getUserData()).canAttack){
						((EnemyWeaponBodyData) bodyB.getUserData()).attackingId = ((PlayerBodyData) bodyA.getUserData()).entityId;
						((EnemyWeaponBodyData) bodyB.getUserData()).attacking = true;
						((EnemyWeaponBodyData) bodyB.getUserData()).canAttack = false;

						entityManager.getPlayer().health -= ((EnemyWeaponBodyData) bodyB.getUserData()).damage;
					}
				}else if(bodyB.getUserData() instanceof PlayerBodyData && bodyA.getUserData() instanceof EnemyWeaponBodyData){
					if(((EnemyWeaponBodyData) bodyA.getUserData()).canAttack){
						((EnemyWeaponBodyData) bodyA.getUserData()).attackingId = ((PlayerBodyData) bodyB.getUserData()).entityId;
						((EnemyWeaponBodyData) bodyA.getUserData()).attacking = true;
						((EnemyWeaponBodyData) bodyA.getUserData()).canAttack = false;

						entityManager.getPlayer().health -= ((EnemyWeaponBodyData) bodyA.getUserData()).damage;
					}
				}
			}
			
			@Override
			public void endContact(Contact contact) {
				Body bodyA = contact.getFixtureA().getBody();
				Body bodyB = contact.getFixtureB().getBody();		
				
				
				if(bodyA.getUserData() instanceof PlayerBodyData && bodyB.getUserData() instanceof GroundBodyData){   // Ground
					if(bodyA.getPosition().y > (bodyB.getPosition().y + ((GroundBodyData) bodyB.getUserData()).height)){
						entityManager.getPlayer().setCanJump(false);
					}
				}else if(bodyB.getUserData() instanceof PlayerBodyData && bodyA.getUserData() instanceof GroundBodyData){
					if(bodyB.getPosition().y > (bodyA.getPosition().y + ((GroundBodyData) bodyA.getUserData()).height)){
						entityManager.getPlayer().setCanJump(false);
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
			
		};
		world.setContactListener(contactListener);
	}
	
	
	
	
	
	@Override
	public void pause() {
		if(isLoaded){
			
		}
	}

	@Override
	public void resume() {
		if(isLoaded){
			
		}
	}

	@Override
	public void hide() {
		if(isLoaded){
			
		}
	}

	@Override
	public void dispose() {
		if(isLoaded){
			guiManager.dispose();
			spriteBatch.dispose();
			world.dispose();
			rayHandler.dispose();
			mapManager.dispose();

			isLoaded = false;
		}
	}

	
	public static EntityManager getEntityManager(){
		return entityManager;
	}
	
	public Mission getCurrentMission(){
		return mission;
	}
}
