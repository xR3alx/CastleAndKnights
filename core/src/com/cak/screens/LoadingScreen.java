package com.cak.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.cak.assets.Assets;
import com.cak.customactors.SpriteActor;
import com.cak.main.Main;

public class LoadingScreen implements Screen {

	private Screen nextScreen;
	
	private Stage stage;
	private Table mainTable;
	
	private Label loadingLabel;
	private SpriteActor backgroundLabel;
	
	private OrthographicCamera camera;
	private SpriteBatch spriteBatch;
	
	private boolean logoSetup, isLoaded, loadAssets;
	
	@Override
	public void show() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Main.VIRTUAL_RESOLUTION_WIDTH, Main.VIRTUAL_RESOLUTION_HEIGHT);
		
		StretchViewport stretchViewport = new StretchViewport(Main.VIRTUAL_RESOLUTION_WIDTH, Main.VIRTUAL_RESOLUTION_HEIGHT, camera);
		stage = new Stage(stretchViewport);
		if(Main.DEBUG){
			stage.setDebugAll(true);
		}
		
		
		
		mainTable = new Table();
		mainTable.setWidth(camera.viewportWidth);
		mainTable.setHeight(camera.viewportHeight);
		
		loadingLabel = new Label("Loading", Assets.getSkin(), "fixedsys_24");
		if(Assets.isFileLoaded("/menu/gfx/imgs/background_loadingScreen.png")){
			backgroundLabel = new SpriteActor(Assets.get("/menu/gfx/imgs/background_loadingScreen.png", Texture.class), 0, 0, false, false, MenuScreen.getCamera());
//			backgroundLabel.setScale(2);
			backgroundLabel.centerActor(camera);
		}
		
		mainTable.add(loadingLabel).expand().bottom().right();
		
		if(backgroundLabel != null){
			stage.addActor(backgroundLabel);
			logoSetup = true;
		}
		stage.addActor(mainTable);
		
		isLoaded = true;
	}

	@Override
	public void render(float delta) {
		stage.act();
		stage.draw();
		
		if(!logoSetup){
			if(Assets.isFileLoaded("/menu/gfx/imgs/background_loadingScreen.png")){
				backgroundLabel = new SpriteActor(Assets.get("/menu/gfx/imgs/background_loadingScreen.png", Texture.class), 0, 0, false, false, MenuScreen.getCamera());
//				backgroundLabel.setScale(2);
				backgroundLabel.centerActor(camera);
				
				stage.clear();
				stage.addActor(backgroundLabel);
				stage.addActor(mainTable);
				
				logoSetup = true;
			}
		}
		if(isLoaded){
			if(loadAssets){
				if(nextScreen instanceof IngameScreen){
					if(Assets.isIngameAssetsLoaded("/ingame/maps/" + ((IngameScreen) nextScreen).getCurrentMission().mappath+ ".tmx")){
						if(Assets.isLoaded()){
							if(Main.itemsMissionsLoaded){
								loadAssets = false;
								((Game) Gdx.app.getApplicationListener()).setScreen(nextScreen);
							}
						}
					}else{
						Assets.loadIngameAssets("/ingame/maps/" + ((IngameScreen) nextScreen).getCurrentMission().mappath + ".tmx");
					}
				}else if(nextScreen instanceof MenuScreen){
					if(Assets.isMenuAssetsLoaded()){
						if(Assets.isLoaded()){
							if(Main.itemsMissionsLoaded){
								loadAssets = false;
								((Game) Gdx.app.getApplicationListener()).setScreen(nextScreen);
							}
						}
					}else{
						Assets.loadMenuAssets();
					}
				}
			}
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
	
	public void changeNextScreen(Screen screen){
		((Game) Gdx.app.getApplicationListener()).setScreen(this);
		nextScreen = screen;
		loadAssets = true;
	}
}
