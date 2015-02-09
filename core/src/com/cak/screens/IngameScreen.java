package com.cak.screens;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.cak.bodyuserdata.DrawUserData;
import com.cak.entities.EntityManager;
import com.cak.files.MissionsLoader.Mission;
import com.cak.files.UserData;
import com.cak.ingame.ContactManager;
import com.cak.ingame.GUIManager;
import com.cak.ingame.MapManager;
import com.cak.main.Main;

public class IngameScreen implements Screen {
	
	private static EntityManager entityManager;
	private static Mission mission;
	private static boolean finishReached;

	
	private SpriteBatch spriteBatch;
	
	private World world;
	private RayHandler rayHandler;
	private Box2DDebugRenderer debugRenderer;
	
	private MapManager mapManager;
	private GUIManager guiManager;
	
	private Array<Body> tempBodies;
	private boolean isLoaded;
	private int monsterKillMoneyReward, monsterKillExpReward;
	
	public static final float PIXELS_TO_METERS = 32, METERS_TO_PIXELS = 0.32f;
	
	public final Vector2 gravity = new Vector2(0, -9.81f);
	private final float timeStep = 1/30f;
	private final int velocityIterations = 3, positionIterations = 3;
	public boolean worldSleep = true;
	
	public IngameScreen(Mission mission) {
		IngameScreen.mission = mission;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void show() {
		spriteBatch = new SpriteBatch();
			
		world = new World(gravity, worldSleep);
		world.setContactListener(new ContactManager());
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
				Main.notificationsManager.addNotification("Mission failed!", "You died!\n Be careful next time!");
				
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

	
	
	public static void setFinishedReached(boolean bool){
		finishReached = bool;
	}
	
	public static boolean getFinishedReached(){
		return finishReached;
	}
	
	public static EntityManager getEntityManager(){
		return entityManager;
	}
	
	public static Mission getCurrentMission(){
		return mission;
	}
}
