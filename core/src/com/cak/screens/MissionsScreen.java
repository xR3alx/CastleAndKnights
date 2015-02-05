package com.cak.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.cak.assets.Assets;
import com.cak.files.MissionsLoader.Mission;
import com.cak.files.UserData;
import com.cak.main.Main;

public class MissionsScreen{
	
	private Table upperRightTable, bottomTable;
	
	private Label missionsLabel, missionNameLabel, missionDifficultyLabel, missionMoneyRewardLabel, missionExpRewardLabel, missionDescriptionLabel;
	private TextButton castleTextbutton, heroTextButton, optionsTextButton,
						playTextButton;
	private List<String> list;
	
	private Task timerTask;
	
	public MissionsScreen() {
		timerTask = new Task() {
			@Override
			public void run() {
				updateTimer();
			}
		};
		Timer.schedule(timerTask, 0, 1);
		
		createUI();
		clickListener();
	}
	
	private void updateTimer(){
	}
	
	private void clickListener(){
		ClickListener clickListener = new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(event.getListenerActor().equals(castleTextbutton)){
					MenuScreen.changeScreen("castle");
				}else if(event.getListenerActor().equals(heroTextButton)){
					MenuScreen.changeScreen("hero");
				}else if(event.getListenerActor().equals(optionsTextButton)){
					// TODO Switch to/show options
				}else if(event.getListenerActor().equals(playTextButton)){
					Main.changeScreen(new IngameScreen(Main.missionLoader.getMissions().get(list.getSelected())));
				}else if(event.getListenerActor().equals(list)){
					missionNameLabel.setText(list.getSelected());
					missionDescriptionLabel.setText(Main.missionLoader.getMissions().get(list.getSelected()).description);
					missionDifficultyLabel.setText("Difficult: " + Main.missionLoader.getMissions().get(list.getSelected()).difficulty);
					missionMoneyRewardLabel.setText("Reward: $" + Main.missionLoader.getMissions().get(list.getSelected()).moneyReward);
					missionExpRewardLabel.setText("Reward EXP" + Main.missionLoader.getMissions().get(list.getSelected()).expReward);
				}
			}
		};
		
		castleTextbutton.addListener(clickListener);
		heroTextButton.addListener(clickListener);
		optionsTextButton.addListener(clickListener);
		playTextButton.addListener(clickListener);
		list.addListener(clickListener);
	}
	
	private void createUI(){

		upperRightTable = new Table();
		upperRightTable.setBackground(Assets.getSkin().getDrawable("table_background_light"));
		
		castleTextbutton = new TextButton("Castle", Assets.getSkin(), "fixedsys_80");
		heroTextButton = new TextButton("Hero", Assets.getSkin(), "fixedsys_80");
		optionsTextButton = new TextButton("Options", Assets.getSkin(), "fixedsys_32");
		
		upperRightTable.add(heroTextButton).bottom().padRight(20).padLeft(10);
		upperRightTable.add(castleTextbutton).bottom().padRight(20);
		upperRightTable.add(optionsTextButton).top();

		
		
		
		
		
		
		
		Table leftBottomTable = new Table();
		leftBottomTable.setBackground(Assets.getSkin().getDrawable("table_background_light"));
		
		missionsLabel = new Label("Missions", Assets.getSkin(), "fixedsys_48");
		list = new List<String>(Assets.getSkin(), "fixedsys_32");
		list.setItems(createListMissions());
		
		leftBottomTable.add(missionsLabel).fill().pad(10).padBottom(15).row();
		leftBottomTable.add(list).fill().pad(10);
		
		
		
		
		
		
		Table rightBottomTable = new Table();
		Table rightBottomInfoTable = new Table();
		rightBottomTable.setBackground(Assets.getSkin().getDrawable("table_background_light"));
		
		missionNameLabel = new Label("No Mission selected", Assets.getSkin(), "fixedsys_64");
		missionDescriptionLabel = new Label("No Description", Assets.getSkin(), "fixedsys_32");
		missionDescriptionLabel.setWrap(true);
		missionDifficultyLabel = new Label("Difficult: " + "-", Assets.getSkin(), "fixedsys_32");
		missionMoneyRewardLabel = new Label("Reward $ " + "-", Assets.getSkin(), "fixedsys_32");
		missionExpRewardLabel = new Label("Reward EP " + "-", Assets.getSkin(), "fixedsys_32");
		
		if(list.getSelected() != null){
			missionNameLabel.setText(list.getSelected());
			missionDescriptionLabel.setText(Main.missionLoader.getMissions().get(list.getSelected()).description);
			missionDifficultyLabel.setText("Difficult: " + Main.missionLoader.getMissions().get(list.getSelected()).difficulty);
			missionMoneyRewardLabel.setText("Reward $ " + Main.missionLoader.getMissions().get(list.getSelected()).moneyReward);
			missionExpRewardLabel.setText("Reward EP " + Main.missionLoader.getMissions().get(list.getSelected()).expReward);
		}
		
		playTextButton = new TextButton("Play", Assets.getSkin(), "courier_32");
		
		rightBottomTable.add(missionNameLabel).fill().pad(10).row();
		rightBottomTable.add(missionDescriptionLabel).fill().pad(10).padBottom(5).row();
		rightBottomTable.add(missionDifficultyLabel).fill().pad(10).padBottom(5).row();
		rightBottomInfoTable.add(missionExpRewardLabel).fill().left().expandX().padRight(30);
		rightBottomInfoTable.add(missionMoneyRewardLabel).fill().right().expandX();
		rightBottomTable.add(rightBottomInfoTable).fill().pad(10).row();
		rightBottomTable.add(playTextButton).fill().pad(10);
		

		
		
		
		bottomTable = new Table();
		bottomTable.add(leftBottomTable).expand().left().top();
		bottomTable.add(rightBottomTable).pad(200).padBottom(400).padTop(50).padLeft(50).expand();
	}
	
	public Table getUpperRightTable() {
		return upperRightTable;
	}
	
	public Table getBottomTable() {
		return bottomTable;
	}
	
	
	
	
	private Array<String> createListMissions(){
		Array<String> listItems = new Array<String>();
		for(Mission mission : Main.missionLoader.getMissions().values()){
			if(UserData.getInt("currentlevel") > mission.minLevel-1 && UserData.getInt("currentlevel") < mission.maxLevel+1){
				if(!mission.retryable){
					if(!UserData.getBoolean(mission.name+"_finished")){
						listItems.add(mission.name);
					}
				}else{
					listItems.add(mission.name);
				}
			}
		}
		return listItems;
	}
}
