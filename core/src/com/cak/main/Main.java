package com.cak.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.cak.assets.Assets;
import com.cak.files.Configurations;
import com.cak.files.ItemLoader;
import com.cak.files.Localisation;
import com.cak.files.MissionsLoader;
import com.cak.files.UserData;
import com.cak.notifications.NotificationsManager;
import com.cak.screens.LoadingScreen;
import com.cak.screens.MenuScreen;

public class Main extends Game {

	private String desktopExtention;
	
	public static IActivityRequestHandler iActivityRequestHandler;
	public static GSPD gspd;
	public static ItemLoader itemLoader;
	public static MissionsLoader missionLoader;
	public static NotificationsManager notificationsManager;
	
	public static final int VIRTUAL_RESOLUTION_WIDTH = 1280, VIRTUAL_RESOLUTION_HEIGHT = 768;
	public static final boolean DEBUG = true;
	
	public static boolean itemsMissionsLoaded;;
	
	private static LoadingScreen loadingScreen;
	
	public Main(String desktopExtention, IActivityRequestHandler iActivityRequestHandler, GSPD gspd) {
		this.desktopExtention = desktopExtention;
		this.iActivityRequestHandler = iActivityRequestHandler;
		this.gspd = gspd;
	}
	
	@Override
	public void create() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);

		new Assets(desktopExtention);
		new Localisation(desktopExtention);
		new Configurations();
		new UserData();
		
		if(UserData.getInt("levelprogression_max") == 0){
			UserData.setProperty("levelprogression_max", 27 + "");
			UserData.setProperty("playerhealth", 50 + "");
			UserData.setProperty("playerstrength", 10 + "");
			UserData.setProperty("playeragility", 5+ "");
		}
		UserData.setProperty("currentmoney", 100 + "");
		UserData.setProperty("weaponsmithupgradecost", 23 + "");
		UserData.setProperty("armorsmithupgradecost", 17 + "");
		UserData.setProperty("alchemistupgradecost", 34 + "");
		UserData.setProperty("playerhealthupgradecost", 37 + "");
		UserData.setProperty("playerstrengthupgradecost", 26 + "");
		UserData.setProperty("playeragilityupgradecost", 31 + "");
		
		notificationsManager = new NotificationsManager();
		
		loadingScreen = new LoadingScreen();
		changeScreen(new MenuScreen());
	}
	
	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	
		if(Assets.isLoaded()){
			if(!itemsMissionsLoaded){
				itemLoader = new ItemLoader();
				missionLoader = new MissionsLoader();
				itemsMissionsLoaded = true;
			}
		}
	}

	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void pause() {
		super.pause();
		UserData.save();
		Configurations.save();
	}

	 @Override
	public void dispose() {
		super.dispose();
		Assets.dispose();
	}
	 
	 
	 
	 public static void changeScreen(Screen screen){
		 loadingScreen.changeNextScreen(screen);
	 }
}
