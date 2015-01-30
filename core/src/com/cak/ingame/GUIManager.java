package com.cak.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.cak.assets.Assets;
import com.cak.entities.Player;
import com.cak.files.UserData;
import com.cak.main.Main;

public class GUIManager {

	private Stage stage;
	private Table mainTable, upperTable, bottomTable;
	private Label timeLabel, healthLabel;
	private ProgressBar healthBar;
	private TextButton moveLeftTextButton, moveRightTextButton, jumpTextButton, attackTextButton;
	
	private OrthographicCamera camera;
	
	private Task task;
	private int timeSec, timeMin;
	
	public GUIManager() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Main.VIRTUAL_RESOLUTION_WIDTH, Main.VIRTUAL_RESOLUTION_HEIGHT);
		
		StretchViewport stretchViewport = new StretchViewport(Main.VIRTUAL_RESOLUTION_WIDTH, Main.VIRTUAL_RESOLUTION_HEIGHT, camera);
		stage = new Stage(stretchViewport);
		if(Main.DEBUG){
			stage.setDebugAll(true);
		}
		
		createUI();
		
		task = new Task() {
			@Override
			public void run() {
				updateTask();
			}
		};
		Timer.schedule(task, 0, 1);
		
		clickListener();
		Gdx.input.setInputProcessor(stage);
	}
	
	public void render(Player p){
		update(p);
		
		stage.draw();
	}
	
	private void update(Player p){
		stage.act();
		
		healthBar.setValue(p.health);
	}
	
	private void updateTask(){
		timeSec++;
		if(timeSec >= 60){
			timeMin++;
			timeSec = 0;
		}
		
		
		timeLabel.setText("%m%:%s%".replace("%m%", timeMin + "").replace("%s%", timeSec + ""));
	}
	
	
	
	
	
	private void clickListener(){
		ClickListener clickListener = new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				
			}
		};
		
		stage.addListener(clickListener);
	}
	
	private void createUI(){
		mainTable = new Table(Assets.getSkin());
		
		Table leftUpperTable = new Table();
		leftUpperTable.setBackground(Assets.getSkin().getDrawable("table_background_light"));
		
		
		Table playerTable = new Table();
		
		healthLabel = new Label("Health", Assets.getSkin(), "fixedsys_32");
		healthLabel.setAlignment(Align.center);
		healthBar = new ProgressBar(0, UserData.getInt("playerhealth"), 1, false, Assets.getSkin(), "red");
		healthBar.setValue(UserData.getInt("playerhealth"));
		
		playerTable.add(healthLabel).fill().pad(10).row();
		playerTable.add(healthBar).fill().pad(10).padTop(2).row();
		
		leftUpperTable.add(playerTable).left().top();
		
		
		
		
		
		
		
		Table rightUpperTable = new Table();
		
		timeLabel = new Label("%m%:%s%".replace("%m%", timeMin + "").replace("%s%", timeSec + ""), Assets.getSkin(), "courier_48");
		
		rightUpperTable.add(timeLabel).top().padLeft(350);

		
		
		
		
		
		
		
		Table leftBottomTable = new Table();
		
		Table rightBottomTable = new Table();
		
		
		
		upperTable = new Table();
		upperTable.add(leftUpperTable).left().top();
		upperTable.add(rightUpperTable).top();
		
		bottomTable = new Table();
		
		moveLeftTextButton = new TextButton("  ", Assets.getSkin(), "left");
		moveLeftTextButton.setScale(2);
		moveRightTextButton = new TextButton("  ", Assets.getSkin(), "right");
		moveRightTextButton.setScale(2);
		jumpTextButton = new TextButton("   ", Assets.getSkin(), "jump");
		jumpTextButton.setScale(2);
		attackTextButton = new TextButton("Attack", Assets.getSkin(), "fixedsys_64");
		
		Table input1Table = new Table();
		Table input2Table = new Table();
		
		input1Table.add(moveLeftTextButton).fill().pad(10).padRight(35);
		input1Table.add(moveRightTextButton).fill().pad(10).left();
		input2Table.add(jumpTextButton).fill().pad(10).padRight(35);
		input2Table.add(attackTextButton).fill().pad(10);
		
		bottomTable.add(input1Table).padRight(480).padBottom(10);
		bottomTable.add(input2Table);

		
		
		mainTable.add(upperTable).left().top().row();
		mainTable.add(bottomTable).expand().bottom();
		stage.addActor(mainTable);
	}
	
	public void resize(){
		mainTable.invalidateHierarchy();
		mainTable.setWidth(camera.viewportWidth);
		mainTable.setHeight(camera.viewportHeight);
	}
	
	public void dispose(){
		stage.dispose();
	}
	
	public boolean isMoveLeftPressed(){
		return moveLeftTextButton.isPressed();
	}
	
	public boolean isMoveRightPressed(){
		return moveRightTextButton.isPressed();
	}
	
	public boolean isJumpPressed(){
		return jumpTextButton.isPressed();
	}
	
	public boolean isAttackPressed(){
		return attackTextButton.isPressed();
	}
}
