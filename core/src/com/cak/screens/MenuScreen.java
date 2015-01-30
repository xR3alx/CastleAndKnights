package com.cak.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.cak.assets.Assets;
import com.cak.files.UserData;
import com.cak.main.Main;
import com.cak.notifications.NotificationsManager;
import com.cak.utils.Utils;

public class MenuScreen implements Screen {

	private static Stage stage;
	private static Table mainTable, bottomTable, upperRightTable, upperTable;
	
	private static Label currentLevelLabel, currentMoneyLabel;
	private static ProgressBar levelProgressionBar;
	
	
	private static OrthographicCamera camera;
	private SpriteBatch spriteBatch;
	
	private boolean isLoaded;
	
	private static CastleScreen castleScreen;
	private static HeroScreen heroScreen;
	private static MissionsScreen missionScreen;
	private static ShopScreen shopScreen;
	
	@Override
	public void show() {
		if(Assets.isMenuAssetsLoaded(this)){
			camera = new OrthographicCamera();
			camera.setToOrtho(false, Main.VIRTUAL_RESOLUTION_WIDTH, Main.VIRTUAL_RESOLUTION_HEIGHT);
			
			StretchViewport stretchViewport = new StretchViewport(Main.VIRTUAL_RESOLUTION_WIDTH, Main.VIRTUAL_RESOLUTION_HEIGHT, camera);
			stage = new Stage(stretchViewport);
			if(Main.DEBUG){
				stage.setDebugAll(true);
			}
			
			castleScreen = new CastleScreen();
			heroScreen = new HeroScreen();
			missionScreen = new MissionsScreen();
			shopScreen = new ShopScreen();
			
			createUI();

			
			changeScreen("castle");
			
			
			Gdx.input.setInputProcessor(stage);
			isLoaded = true;
		}else{
			Assets.loadMenuAssets(this);
		}
	}

	@Override
	public void render(float delta) {
		if(isLoaded){
			update();
			
			stage.draw();
		}
	}
	
	private void update(){
		stage.act();
		updateUI();
		Main.notificationsManager.update(stage);
	}
	
	
	private void createUI(){
		mainTable = new Table(Assets.getSkin());
		
		Table leftUpperTable = new Table();
		leftUpperTable.setBackground(Assets.getSkin().getDrawable("table_background_dark"));
		
		currentLevelLabel = new Label("Lv." + UserData.getInt("currentlevel"), Assets.getSkin(), "courier_48");
		currentMoneyLabel = new Label("$" + UserData.getInt("currentmoney"), Assets.getSkin(), "fixedsys_32");
		levelProgressionBar = new ProgressBar(0, UserData.getInt("levelprogression_max"), 1, false, Assets.getSkin(), "green");
		levelProgressionBar.setValue(UserData.getInt("levelprogression_current"));
		
		Table levelProgressMoneyTable = new Table();
		levelProgressMoneyTable.add(currentMoneyLabel).fill().padBottom(10).row();
		levelProgressMoneyTable.add(levelProgressionBar).fill();
		
		leftUpperTable.add(currentLevelLabel).pad(10).padRight(10).padTop(10).padBottom(10);
		leftUpperTable.add(levelProgressMoneyTable).padTop(10).padBottom(10).pad(10);
		
		
		
		upperRightTable = new Table();
		
		
		
		upperTable = new Table();
		upperTable.add(leftUpperTable).left().top();
		upperTable.add(upperRightTable).left().top();
		
		bottomTable = new Table();
		
		mainTable.add(upperTable).left().top().row();
		mainTable.add(bottomTable).expand().left().top();
		
		stage.addActor(mainTable);
		
		
		mainTable.invalidateHierarchy();
		mainTable.setWidth(camera.viewportWidth);
		mainTable.setHeight(camera.viewportHeight);
	}
	
	
	
	
	
	public static void changeScreen(String type){
		if(type.equalsIgnoreCase("hero")){
			upperRightTable.clear();
			upperRightTable.add(heroScreen.getUpperRightTable());
			bottomTable.clear();
			bottomTable.add(heroScreen.getBottomTable());
			
		}else if(type.equalsIgnoreCase("castle")){
			upperRightTable.clear();
			upperRightTable.add(castleScreen.getUpperRightTable());
			bottomTable.clear();
			bottomTable.add(castleScreen.getBottomTable());
			
		}else if(type.equalsIgnoreCase("missions")){
			upperRightTable.clear();
			upperRightTable.add(missionScreen.getUpperRightTable());
			bottomTable.clear();
			bottomTable.add(missionScreen.getBottomTable());
			
		}else if(type.equalsIgnoreCase("shop")){
			upperRightTable.clear();
			upperRightTable.add(shopScreen.getUpperRightTable());
			bottomTable.clear();
			bottomTable.add(shopScreen.getBottomTable());
		}
	}
	
	

	
	
	@Override
	public void resize(int width, int height) {
		if(isLoaded){
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
			stage.dispose();
			spriteBatch.dispose();
		}
	}
	
	
	
	
	public static void updateUI(){
		currentLevelLabel.setText("Lv." + UserData.getInt("currentlevel"));
		currentMoneyLabel.setText("$" + UserData.getInt("currentmoney"));
		levelProgressionBar.setValue(UserData.getInt("levelprogression_current"));
		
		if(levelProgressionBar.getValue() >= levelProgressionBar.getMaxValue()){
			UserData.setProperty("currentlevel", UserData.getInt("currentlevel")+1 + "");
			int moneyreward =  Math.round((levelProgressionBar.getValue() / 4));
			UserData.setProperty("currentmoney", UserData.getInt("currentmoney") + moneyreward + "");
			UserData.setProperty("levelprogression_current", (int) (levelProgressionBar.getValue() - levelProgressionBar.getMaxValue()) + "");
			levelProgressionBar.setValue(UserData.getFloat("levelprogression_current"));
			Utils.calculateExpToLevel();
			levelProgressionBar.setRange(0, UserData.getInt("levelprogression_max"));
			
			Main.notificationsManager.addNotification("Level up!", "You reached the next Level!\nMoney reward: $" + moneyreward + "\nExp to next level: " + (int) levelProgressionBar.getMaxValue());
		}
	}
}
