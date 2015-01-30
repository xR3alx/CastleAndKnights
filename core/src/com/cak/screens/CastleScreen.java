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

public class CastleScreen {

	private Table upperRightTable, bottomTable;
	
	private Label weaponsmithLabel, armorsmithLabel, alchemistLabel;
	private TextButton heroTextbutton, missionsTextButton, optionsTextButton,
						upgradeWeaponsmithTexButton, upgradeArmorsmithTexButton, upgradeAlchemistTexButton;
	
	private Task timerTask;
	
	public CastleScreen() {
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
				if(event.getListenerActor().equals(heroTextbutton)){
					MenuScreen.changeScreen("hero");
				}else if(event.getListenerActor().equals(missionsTextButton)){
					MenuScreen.changeScreen("missions");
				}else if(event.getListenerActor().equals(optionsTextButton)){
					// TODO Switch to/show options
				}else if(event.getListenerActor().equals(upgradeWeaponsmithTexButton)){
					int price = Utils.calculateUpgradeCost(UserData.getInt("weaponsmithupgradecost"), UserData.getInt("currentweaponsmithlevel"));
					
					if(UserData.getInt("currentmoney") >= price){
						UserData.setProperty("currentweaponsmithlevel", (UserData.getInt("currentweaponsmithlevel")+1) + "");
						UserData.setProperty("currentmoney", (UserData.getInt("currentmoney")-price) + "");
						weaponsmithLabel.setText("Weaponsmith Lv." + UserData.getInt("currentweaponsmithlevel"));
						upgradeWeaponsmithTexButton.setText("Upgrade for $" + Utils.calculateUpgradeCost(UserData.getInt("weaponsmithupgradecost"), UserData.getInt("currentweaponsmithlevel")));
						
						MenuScreen.updateUI();
					}else{
						Main.notificationsManager.addNotification("Error!", "You don't have enough money.");
					}
				}else if(event.getListenerActor().equals(upgradeArmorsmithTexButton)){
					int price = Utils.calculateUpgradeCost(UserData.getInt("armorsmithupgradecost"), UserData.getInt("currentarmorsmithlevel"));
					
					if(UserData.getInt("currentmoney") >= price){
						UserData.setProperty("currentarmorsmithlevel", (UserData.getInt("currentarmorsmithlevel")+1) + "");
						UserData.setProperty("currentmoney", (UserData.getInt("currentmoney")-price) + "");
						armorsmithLabel.setText("Armorsmith Lv." + UserData.getInt("currentarmorsmithlevel"));
						upgradeArmorsmithTexButton.setText("Upgrade for $" + Utils.calculateUpgradeCost(UserData.getInt("armorsmithupgradecost"), UserData.getInt("currentarmorsmithlevel")));
						
						MenuScreen.updateUI();
					}else{
						Main.notificationsManager.addNotification("Error!", "You don't have enough money.");
					}
				}else if(event.getListenerActor().equals(upgradeAlchemistTexButton)){
					int price = Utils.calculateUpgradeCost(UserData.getInt("alchemistupgradecost"), UserData.getInt("currentalchemistlevel"));
					
					if(UserData.getInt("currentmoney") >= price){
						UserData.setProperty("currentalchemistlevel", (UserData.getInt("currentalchemistlevel")+1) + "");
						UserData.setProperty("currentmoney", (UserData.getInt("currentmoney")-price) + "");
						alchemistLabel.setText("Alchemist Lv." + UserData.getInt("currentalchemistlevel"));
						upgradeAlchemistTexButton.setText("Upgrade for $" + Utils.calculateUpgradeCost(UserData.getInt("alchemistupgradecost"), UserData.getInt("currentalchemistlevel")));
						
						MenuScreen.updateUI();
					}else{
						Main.notificationsManager.addNotification("Error!", "You don't have enough money.");
					}
				}
			}
		};
		
		heroTextbutton.addListener(clickListener);
		missionsTextButton.addListener(clickListener);
		optionsTextButton.addListener(clickListener);
		upgradeWeaponsmithTexButton.addListener(clickListener);
		upgradeArmorsmithTexButton.addListener(clickListener);
		upgradeAlchemistTexButton.addListener(clickListener);
	}
	
	private void createUI(){
		
		upperRightTable = new Table();
		upperRightTable.setBackground(Assets.getSkin().getDrawable("table_background_light"));
		
		heroTextbutton = new TextButton("Hero", Assets.getSkin(), "fixedsys_80");
		missionsTextButton = new TextButton("Missions", Assets.getSkin(), "fixedsys_80");
		optionsTextButton = new TextButton("Options", Assets.getSkin(), "fixedsys_32");
		
		upperRightTable.add(heroTextbutton).bottom().padRight(20).padLeft(10);
		upperRightTable.add(missionsTextButton).bottom().padRight(20);
		upperRightTable.add(optionsTextButton).top();

		
		
		
		
		
		
		Table leftBottomTable = new Table();
		leftBottomTable.setBackground(Assets.getSkin().getDrawable("table_background_light"));
		
		weaponsmithLabel = new Label("Weaponsmith Lv." + UserData.getInt("currentweaponsmithlevel"), Assets.getSkin(), "courier_48");
		upgradeWeaponsmithTexButton = new TextButton("Upgrade for $" + Utils.calculateUpgradeCost(UserData.getInt("weaponsmithupgradecost"), UserData.getInt("currentweaponsmithlevel")), Assets.getSkin(), "fixedsys_32");
		armorsmithLabel = new Label("Armorsmith Lv." + UserData.getInt("currentarmorsmithlevel"), Assets.getSkin(), "courier_48");
		upgradeArmorsmithTexButton = new TextButton("Upgrade for $" + Utils.calculateUpgradeCost(UserData.getInt("armorsmithupgradecost"), UserData.getInt("currentarmorsmithlevel")), Assets.getSkin(), "fixedsys_32");
		alchemistLabel = new Label("Alchemist Lv." + UserData.getInt("currentalchemistlevel"), Assets.getSkin(), "courier_48");
		upgradeAlchemistTexButton = new TextButton("Upgrade for $" + Utils.calculateUpgradeCost(UserData.getInt("alchemistupgradecost"), UserData.getInt("currentalchemistlevel")), Assets.getSkin(), "fixedsys_32");
		
		leftBottomTable.add(weaponsmithLabel).fill().pad(10).padBottom(15).row();
		leftBottomTable.add(upgradeWeaponsmithTexButton).fill().padBottom(25).row();
		leftBottomTable.add(armorsmithLabel).fill().pad(10).padBottom(15).row();
		leftBottomTable.add(upgradeArmorsmithTexButton).fill().padBottom(25).row();
		leftBottomTable.add(alchemistLabel).fill().pad(10).padBottom(15).row();
		leftBottomTable.add(upgradeAlchemistTexButton).fill().pad(10);
		
		
		
		
		
		Table rightBottomTable = new Table();
		
		bottomTable = new Table();
		bottomTable.add(leftBottomTable).expand().left().top();
		bottomTable.add(rightBottomTable).pad(200).expand();
	}
	
	public Table getUpperRightTable() {
		return upperRightTable;
	}
	
	public Table getBottomTable() {
		return bottomTable;
	}
	
}
