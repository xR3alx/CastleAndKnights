package com.cak.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.cak.assets.Assets;
import com.cak.files.UserData;
import com.cak.main.Main;
import com.cak.utils.Utils;

public class HeroScreen {

	private Table upperRightTable, bottomTable;
	
	private Label healthLabel, strengthLabel, agilityLabel;
	private TextButton castleTextbutton, missionsTextButton, optionsTextButton,
						shopTextButton,
						upgradeHealthTextButton, upgradeStrengthTextButton, upgradeAgilityTextButton;
	
	private Task timerTask;
	
	public HeroScreen() {
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
				}else if(event.getListenerActor().equals(shopTextButton)){
					MenuScreen.changeScreen("shop");
				}else if(event.getListenerActor().equals(missionsTextButton)){
					MenuScreen.changeScreen("missions");
				}else if(event.getListenerActor().equals(optionsTextButton)){
					// TODO Switch to/show options
				}else if(event.getListenerActor().equals(upgradeHealthTextButton)){
					int price = Utils.calculateUpgradeCost(UserData.getInt("playerhealthupgradecost"), UserData.getInt("playerhealth") / 10);
					
					if(UserData.getInt("currentmoney") >= price){
						UserData.setProperty("playerhealth", (UserData.getInt("playerhealth")+10) + "");
						UserData.setProperty("currentmoney", (UserData.getInt("currentmoney")-price) + "");
						healthLabel.setText("Health " + UserData.getInt("playerhealth"));
						upgradeHealthTextButton.setText("Upgrade for $" + Utils.calculateUpgradeCost(UserData.getInt("playerhealthupgradecost"), UserData.getInt("playerhealth") / 10));
						
						MenuScreen.updateUI();
					}else{
						Main.notificationsManager.addNotification("Error!", "You don't have enough money.");
					}
				}else if(event.getListenerActor().equals(upgradeStrengthTextButton)){
					int price = Utils.calculateUpgradeCost(UserData.getInt("playerstrengthupgradecost"), UserData.getInt("playerstrength"));
					
					if(UserData.getInt("currentmoney") >= price){
						UserData.setProperty("playerstrength", (UserData.getInt("playerstrength")+2) + "");
						UserData.setProperty("currentmoney", (UserData.getInt("currentmoney")-price) + "");
						strengthLabel.setText("Strength " + UserData.getInt("playerstrength"));
						upgradeStrengthTextButton.setText("Upgrade for $" + Utils.calculateUpgradeCost(UserData.getInt("playerstrengthupgradecost"), UserData.getInt("playerstrength")));
						
						MenuScreen.updateUI();
					}else{
						Main.notificationsManager.addNotification("Error!", "You don't have enough money.");
					}
				}else if(event.getListenerActor().equals(upgradeAgilityTextButton)){
					int price = Utils.calculateUpgradeCost(UserData.getInt("playeragilityupgradecost"), UserData.getInt("playeragility"));
					
					if(UserData.getInt("currentmoney") >= price){
						UserData.setProperty("playeragility", (UserData.getInt("playeragility")+1) + "");
						UserData.setProperty("currentmoney", (UserData.getInt("currentmoney")-price) + "");
						agilityLabel.setText("Agility " + UserData.getInt("playeragility"));
						upgradeAgilityTextButton.setText("Upgrade for $" + Utils.calculateUpgradeCost(UserData.getInt("playeragilityupgradecost"), UserData.getInt("playeragility")));
						
						MenuScreen.updateUI();
					}else{
						Main.notificationsManager.addNotification("Error!", "You don't have enough money.");
					}				
				}
			}
		};
		
		castleTextbutton.addListener(clickListener);
		missionsTextButton.addListener(clickListener);
		optionsTextButton.addListener(clickListener);
		shopTextButton.addListener(clickListener);
		upgradeHealthTextButton.addListener(clickListener);
		upgradeStrengthTextButton.addListener(clickListener);
		upgradeAgilityTextButton.addListener(clickListener);
	}
	
	private void createUI(){
		
		upperRightTable = new Table();
		upperRightTable.setBackground(Assets.getSkin().getDrawable("table_background_light"));
		
		castleTextbutton = new TextButton("Castle", Assets.getSkin(), "fixedsys_80");
		missionsTextButton = new TextButton("Missions", Assets.getSkin(), "fixedsys_80");
		optionsTextButton = new TextButton("Options", Assets.getSkin(), "fixedsys_32");
		
		upperRightTable.add(castleTextbutton).bottom().padRight(20).padLeft(10);
		upperRightTable.add(missionsTextButton).bottom().padRight(20);
		upperRightTable.add(optionsTextButton).top();

		
		
		
		
		
		
		Table leftBottomTable = new Table();
		leftBottomTable.setBackground(Assets.getSkin().getDrawable("table_background_light"));
		
		shopTextButton = new TextButton("Store", Assets.getSkin(), "courier_48");
		healthLabel = new Label("Health " + UserData.getInt("playerhealth"), Assets.getSkin(), "courier_48");
		upgradeHealthTextButton = new TextButton("Upgrade for $" + Utils.calculateUpgradeCost(UserData.getInt("playerhealthupgradecost"), UserData.getInt("playerhealth") / 10), Assets.getSkin(), "fixedsys_32");
		strengthLabel = new Label("Strength " + UserData.getInt("playerstrength"), Assets.getSkin(), "courier_48");
		upgradeStrengthTextButton = new TextButton("Upgrade for $" + Utils.calculateUpgradeCost(UserData.getInt("playerstrengthupgradecost"), UserData.getInt("playerstrength")), Assets.getSkin(), "fixedsys_32");
		agilityLabel = new Label("Agility " + UserData.getInt("playeragility"), Assets.getSkin(), "courier_48");
		upgradeAgilityTextButton = new TextButton("Upgrade for $" + Utils.calculateUpgradeCost(UserData.getInt("playeragilityupgradecost"), UserData.getInt("playeragility")), Assets.getSkin(), "fixedsys_32");
		
		leftBottomTable.add(shopTextButton).fill().pad(10).padBottom(40).row();
		leftBottomTable.add(healthLabel).fill().pad(10).padBottom(10).row();
		leftBottomTable.add(upgradeHealthTextButton).fill().pad(10).padBottom(25).row();
		leftBottomTable.add(strengthLabel).fill().pad(10).padBottom(10).row();
		leftBottomTable.add(upgradeStrengthTextButton).fill().pad(10).padBottom(25).row();
		leftBottomTable.add(agilityLabel).fill().pad(10).padBottom(10).row();
		leftBottomTable.add(upgradeAgilityTextButton).fill().pad(10);
		
		
		
		
		
		Table rightBottomTable = new Table();
		
		bottomTable = new Table();
		bottomTable.add(leftBottomTable).expand().left().top();
		bottomTable.add(rightBottomTable).pad(200).expand();
	}
	
	public Table getUpperRightTable(){
		return upperRightTable;
	}
	
	public Table getBottomTable(){
		return bottomTable;
	}
}
