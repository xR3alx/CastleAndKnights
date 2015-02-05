package com.cak.screens;

import java.util.Collection;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.cak.assets.Assets;
import com.cak.customactors.SpriteActor;
import com.cak.files.ItemLoader.ArmorItem;
import com.cak.files.ItemLoader.Item;
import com.cak.files.ItemLoader.PotionItem;
import com.cak.files.ItemLoader.WeaponItem;
import com.cak.files.UserData;
import com.cak.main.Main;

public class ShopScreen {

	private Table upperRightTable, bottomTable, rightBottomInfoTable, rightBottomButtonsTable;
	
	private Label currentCategorieLabel, costInfoLabel, healthincreaseInfoLabel, strengthincreaseInfoLabel, agilityincreaseInfoLabel, defenceInfoLabel, damageInfoLabel, healInfoLabel, durationInfoLabel, castleRequiredLabel;
	private TextButton castleTextbutton, heroTextButton, optionsTextButton,
						weaponsTextButton, armorTextButton, potionsTextButton,
						buyTextButton, useTextButton;
	private List<String> list;
	private SpriteActor shopPreviewSpriteActor;
	
	private String currentCategorie, currentCategorieString;
	private int selectionCost, selectionBuildingLevel, selectionBuilding;
	
	private Task timerTask;
	
	public ShopScreen() {
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
				}else if(event.getListenerActor().equals(buyTextButton)){
					boolean canBuy = false;
					
					if(selectionBuilding == 0){
						if(UserData.getInt("currentweaponsmithlevel") >= selectionBuildingLevel){
							canBuy = true;
						}
					}else if(selectionBuilding == 1){
						if(UserData.getInt("currentarmorsmithlevel") >= selectionBuildingLevel){
							canBuy = true;
						}
					}else if(selectionBuilding == 2){
						if(UserData.getInt("currentalchemistlevel") >= selectionBuildingLevel){
							canBuy = true;
						}
					}
					
					if(canBuy){
						if(UserData.getInt("currentmoney") >= selectionCost){
							UserData.setProperty("currentmoney", (UserData.getInt("currentmoney")-selectionCost) + "");
							if(currentCategorie.equals("potions")){
								UserData.setProperty(list.getSelected(), (UserData.getInt(list.getSelected())+1) + "");
							}else{
								UserData.setProperty(list.getSelected() + "_bought", "true");
							}
							changeInfobox(false);
							rightBottomButtonsTable.clear();
							rightBottomButtonsTable.add(rightBottomInfoTable).pad(10).fill().row();
							if(UserData.getBoolean(list.getSelected() + "_bought")){
								if(currentCategorie.equalsIgnoreCase("weapons")){
									if(!UserData.getString("weapon_use").equals(list.getSelected())){
										rightBottomButtonsTable.add(useTextButton).pad(10).fill();
									}
								}else if(currentCategorie.equalsIgnoreCase("armor")){
									if(!UserData.getString("armor_use").equals(list.getSelected())){
										rightBottomButtonsTable.add(useTextButton).pad(10).fill();
									}
								}
								
								MenuScreen.updateUI();
							}else{
								rightBottomButtonsTable.add(buyTextButton).pad(10).fill();
							}
						}else{
							Main.notificationsManager.addNotification("Error!", "You don't have enough money.");
						}
					}else{
						Main.notificationsManager.addNotification("Error!", "You don't have the right building level.");
					}
				}else if(event.getListenerActor().equals(useTextButton)){
					if(currentCategorie.equalsIgnoreCase("weapons")){
						UserData.setProperty("weapon_use", list.getSelected());
					}else if(currentCategorie.equalsIgnoreCase("armor")){
						UserData.setProperty("armor_use", list.getSelected());
					}
					rightBottomButtonsTable.clear();
					rightBottomButtonsTable.add(rightBottomInfoTable).pad(10).fill().row();
					if(UserData.getBoolean(list.getSelected() + "_bought")){
						if(currentCategorie.equalsIgnoreCase("weapons")){
							if(!UserData.getString("weapon_use").equals(list.getSelected())){
								rightBottomButtonsTable.add(useTextButton).pad(10).fill();
							}
						}else if(currentCategorie.equalsIgnoreCase("armor")){
							if(!UserData.getString("armor_use").equals(list.getSelected())){
								rightBottomButtonsTable.add(useTextButton).pad(10).fill();
							}
						}
					}else{
						rightBottomButtonsTable.add(buyTextButton).pad(10).fill();
					}
				}else if(event.getListenerActor().equals(weaponsTextButton)){
					useTextButton.setVisible(UserData.getBoolean(list.getSelected() + "_bought"));
					currentCategorie = "weapons";
					changeInfobox(true);
					rightBottomButtonsTable.clear();
					rightBottomButtonsTable.add(rightBottomInfoTable).pad(10).fill().row();
					if(UserData.getBoolean(list.getSelected() + "_bought")){
						if(!UserData.getString("weapon_use").equals(list.getSelected())){
							rightBottomButtonsTable.add(useTextButton).pad(10).fill();
						}
					}else{
						rightBottomButtonsTable.add(buyTextButton).pad(10).fill();
					}
				}else if(event.getListenerActor().equals(armorTextButton)){
					useTextButton.setVisible(UserData.getBoolean(list.getSelected() + "_bought"));
					currentCategorie = "armor";
					changeInfobox(true);
					rightBottomButtonsTable.clear();
					rightBottomButtonsTable.add(rightBottomInfoTable).pad(10).fill().row();
					if(UserData.getBoolean(list.getSelected() + "_bought")){
						if(!UserData.getString("weapon_use").equals(list.getSelected())){
							rightBottomButtonsTable.add(useTextButton).pad(10).fill();
						}
					}else{
						rightBottomButtonsTable.add(buyTextButton).pad(10).fill();
					}
				}else if(event.getListenerActor().equals(potionsTextButton)){
					useTextButton.setVisible(false);
					currentCategorie = "potions";
					changeInfobox(true);
					rightBottomButtonsTable.clear();
					rightBottomButtonsTable.add(rightBottomInfoTable).pad(10).fill().row();
					if(UserData.getBoolean(list.getSelected() + "_bought")){
						if(!UserData.getString("weapon_use").equals(list.getSelected())){
							rightBottomButtonsTable.add(useTextButton).pad(10).fill();
						}
					}else{
						rightBottomButtonsTable.add(buyTextButton).pad(10).fill();
					}
				}else if(event.getListenerActor().equals(list)){
					changeInfobox(false);
					rightBottomButtonsTable.clear();
					rightBottomButtonsTable.add(rightBottomInfoTable).pad(10).fill().row();
					if(UserData.getBoolean(list.getSelected() + "_bought")){
						if(currentCategorie.equalsIgnoreCase("weapons")){
							if(!UserData.getString("weapon_use").equals(list.getSelected())){
								rightBottomButtonsTable.add(useTextButton).pad(10).fill();
							}
						}else if(currentCategorie.equalsIgnoreCase("armor")){
							if(!UserData.getString("armor_use").equals(list.getSelected())){
								rightBottomButtonsTable.add(useTextButton).pad(10).fill();
							}
						}
					}else{
						rightBottomButtonsTable.add(buyTextButton).pad(10).fill();
					}
				}
			}
		};
		
		castleTextbutton.addListener(clickListener);
		heroTextButton.addListener(clickListener);
		optionsTextButton.addListener(clickListener);
		buyTextButton.addListener(clickListener);
		useTextButton.addListener(clickListener);
		weaponsTextButton.addListener(clickListener);
		armorTextButton.addListener(clickListener);
		potionsTextButton.addListener(clickListener);
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
		
		weaponsTextButton = new TextButton("Weapons", Assets.getSkin(), "courier_48");
		armorTextButton = new TextButton("Armor", Assets.getSkin(), "courier_48");
		potionsTextButton = new TextButton("Potions", Assets.getSkin(), "courier_48");
		
		leftBottomTable.add(weaponsTextButton).fill().pad(10).padBottom(15).row();
		leftBottomTable.add(armorTextButton).fill().pad(10).padBottom(15).row();
		leftBottomTable.add(potionsTextButton).fill().pad(10).row();
		
		
		
		
		
		
		
		
		Table rightBottomTable = new Table();
		Table rightBottom2Table = new Table();
		rightBottomButtonsTable = new Table();
		rightBottomInfoTable = new Table();
		rightBottomTable.setBackground(Assets.getSkin().getDrawable("table_background_light"));
		
		currentCategorieLabel = new Label("", Assets.getSkin(), "fixedsys_64");
		currentCategorieLabel.setAlignment(Align.center);
		buyTextButton = new TextButton("Buy", Assets.getSkin(), "courier_32");
		useTextButton = new TextButton("Use", Assets.getSkin(), "courier_32");
		list = new List<String>(Assets.getSkin(), "fixedsys_32");
		
		currentCategorie = "weapons";
		changeInfobox(true);
		
		rightBottomButtonsTable.clear();
		rightBottomButtonsTable.add(rightBottomInfoTable).pad(10).fill().row();
		if(UserData.getBoolean(list.getSelected() + "_bought")){
			if(!UserData.getString("weapon_use").equals(list.getSelected())){
				rightBottomButtonsTable.add(useTextButton).pad(10).fill();
			}
		}else{
			rightBottomButtonsTable.add(buyTextButton).pad(10).fill();
		}
		
		rightBottom2Table.add(list).fill();
		rightBottom2Table.add(rightBottomButtonsTable).fill();
		
		rightBottomTable.add(currentCategorieLabel).fill().row();
		rightBottomTable.add(rightBottom2Table).fill();
		
		
		
		
		
		
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
	
	
	
	
	private void changeInfobox(boolean setList){
		rightBottomInfoTable.clear();
		int cost = 0;
		String castlebuilding = "";
		int castlebuildinglevel = 0;
		costInfoLabel = new Label("Cost: " + cost, Assets.getSkin(), "fixedsys_32");
		castleRequiredLabel = new Label("Required: " + castlebuilding + " Lv. " + castlebuildinglevel, Assets.getSkin(), "fixedsys_32");
		
		if(currentCategorie.equalsIgnoreCase("weapons")){
			if(setList){
				list.setItems(createListItems(Main.itemLoader.getWeapons().values()));
			}
			
			currentCategorieString = "Weapons";
			cost = Main.itemLoader.getWeapons().get(list.getSelected()).cost;
			selectionBuilding = 0;
			castlebuilding = "Weaponsmith";
			castlebuildinglevel = Main.itemLoader.getWeapons().get(list.getSelected()).castleBuildingLevelNeed;
			
			if(damageInfoLabel == null){
				damageInfoLabel = new Label("Damage: " + ((WeaponItem) Main.itemLoader.getWeapons().get(list.getSelected())).damage, Assets.getSkin(), "fixedsys_32");
			}else{
				damageInfoLabel.setText("Damage: " + ((WeaponItem) Main.itemLoader.getWeapons().get(list.getSelected())).damage);
			}
			if(healthincreaseInfoLabel == null){
				healthincreaseInfoLabel = new Label("Health increase: " + ((WeaponItem) Main.itemLoader.getWeapons().get(list.getSelected())).healthIncrease, Assets.getSkin(), "fixedsys_32");
			}else{
				healthincreaseInfoLabel.setText("Health increase: " + ((WeaponItem) Main.itemLoader.getWeapons().get(list.getSelected())).healthIncrease);
			}
			if(strengthincreaseInfoLabel == null){
				strengthincreaseInfoLabel = new Label("Strength increase: " + ((WeaponItem) Main.itemLoader.getWeapons().get(list.getSelected())).strengthIncrease, Assets.getSkin(), "fixedsys_32");
			}else{
				strengthincreaseInfoLabel.setText("Strength increase: " + ((WeaponItem) Main.itemLoader.getWeapons().get(list.getSelected())).strengthIncrease);
			}
			if(agilityincreaseInfoLabel == null){
				agilityincreaseInfoLabel = new Label("Agility increase: " + ((WeaponItem) Main.itemLoader.getWeapons().get(list.getSelected())).agilityIncrease, Assets.getSkin(), "fixedsys_32");
			}else{
				agilityincreaseInfoLabel.setText("Agility increase: " + ((WeaponItem) Main.itemLoader.getWeapons().get(list.getSelected())).agilityIncrease);
			}
			if(Main.itemLoader.getWeapons().get(list.getSelected()).texture != null){
				shopPreviewSpriteActor = new SpriteActor(Main.itemLoader.getWeapons().get(list.getSelected()).texture, 0, 0, false, false, MenuScreen.getCamera());
				shopPreviewSpriteActor.setScale(4);
			}else{
				shopPreviewSpriteActor = null;
			}
			
			Table infoTable = new Table();
			
			infoTable.add(costInfoLabel).fill().padBottom(5).row();
			infoTable.add(castleRequiredLabel).fill().padBottom(5).row();
			infoTable.add(damageInfoLabel).fill().padBottom(5).row();
			infoTable.add(healthincreaseInfoLabel).fill().padBottom(5).row();
			infoTable.add(strengthincreaseInfoLabel).fill().padBottom(5).row();
			infoTable.add(agilityincreaseInfoLabel).fill().padBottom(5).row();
			rightBottomInfoTable.add(infoTable).fill();
			if(shopPreviewSpriteActor != null){
				rightBottomInfoTable.add(shopPreviewSpriteActor).fill().padLeft(5);
			}
		}else if(currentCategorie.equalsIgnoreCase("armor")){
			if(setList){
				list.setItems(createListItems(Main.itemLoader.getArmors().values()));
			}
			
			currentCategorieString = "Armor";
			cost = Main.itemLoader.getArmors().get(list.getSelected()).cost;
			selectionBuilding = 1;
			castlebuilding = "Armorsmith";
			castlebuildinglevel = Main.itemLoader.getArmors().get(list.getSelected()).castleBuildingLevelNeed;
			
			if(defenceInfoLabel == null){
				defenceInfoLabel = new Label("Defence: " + ((ArmorItem) Main.itemLoader.getArmors().get(list.getSelected())).defence, Assets.getSkin(), "fixedsys_32");
			}else{
				defenceInfoLabel.setText("Defence: " + ((ArmorItem) Main.itemLoader.getArmors().get(list.getSelected())).defence);
			}
			if(healthincreaseInfoLabel == null){
				healthincreaseInfoLabel = new Label("Health increase: " + ((ArmorItem) Main.itemLoader.getArmors().get(list.getSelected())).healthIncrease, Assets.getSkin(), "fixedsys_32");
			}else{
				healthincreaseInfoLabel.setText("Health increase: " + ((ArmorItem) Main.itemLoader.getArmors().get(list.getSelected())).healthIncrease);
			}
			if(strengthincreaseInfoLabel == null){
				strengthincreaseInfoLabel = new Label("Strength increase: " + ((ArmorItem) Main.itemLoader.getArmors().get(list.getSelected())).strengthIncrease, Assets.getSkin(), "fixedsys_32");
			}else{
				strengthincreaseInfoLabel.setText("Strength increase: " + ((ArmorItem) Main.itemLoader.getArmors().get(list.getSelected())).strengthIncrease);
			}
			if(agilityincreaseInfoLabel == null){
				agilityincreaseInfoLabel = new Label("Agility increase: " + ((ArmorItem) Main.itemLoader.getArmors().get(list.getSelected())).agilityIncrease, Assets.getSkin(), "fixedsys_32");
			}else{
				agilityincreaseInfoLabel.setText("Agility increase: " + ((ArmorItem) Main.itemLoader.getArmors().get(list.getSelected())).agilityIncrease);
			}
			if(Main.itemLoader.getArmors().get(list.getSelected()).texture != null){
				shopPreviewSpriteActor = new SpriteActor(Main.itemLoader.getArmors().get(list.getSelected()).texture, 0, 0, false, false, MenuScreen.getCamera());
				shopPreviewSpriteActor.setScale(4);
			}else{
				shopPreviewSpriteActor = null;
			}
			
			Table infoTable = new Table();
			
			infoTable.add(costInfoLabel).fill().padBottom(5).row();
			infoTable.add(castleRequiredLabel).fill().padBottom(5).row();
			infoTable.add(defenceInfoLabel).fill().padBottom(5).row();
			infoTable.add(healthincreaseInfoLabel).fill().padBottom(5).row();
			infoTable.add(strengthincreaseInfoLabel).fill().padBottom(5).row();
			infoTable.add(agilityincreaseInfoLabel).fill().padBottom(5).row();
			rightBottomInfoTable.add(infoTable).fill();
			if(shopPreviewSpriteActor != null){
				rightBottomInfoTable.add(shopPreviewSpriteActor).fill().padLeft(5);
			}
		}else if(currentCategorie.equalsIgnoreCase("potions")){
			if(setList){
				list.setItems(createListItems(Main.itemLoader.getPotions().values()));
			}
			
			currentCategorieString = "Potions";
			cost = Main.itemLoader.getPotions().get(list.getSelected()).cost;
			selectionBuilding = 2;
			castlebuilding = "Alchemist";
			castlebuildinglevel = Main.itemLoader.getPotions().get(list.getSelected()).castleBuildingLevelNeed;
			
			if(healInfoLabel == null){
				healInfoLabel = new Label("Heal: " + ((PotionItem) Main.itemLoader.getPotions().get(list.getSelected())).heal, Assets.getSkin(), "fixedsys_32");
			}else{
				healInfoLabel.setText("Heal: " + ((PotionItem) Main.itemLoader.getPotions().get(list.getSelected())).heal);
			}
			if(strengthincreaseInfoLabel == null){
				strengthincreaseInfoLabel = new Label("Strength increase: " + ((PotionItem) Main.itemLoader.getPotions().get(list.getSelected())).strengthIncrease, Assets.getSkin(), "fixedsys_32");
			}else{
				strengthincreaseInfoLabel.setText("Strength increase: " + ((PotionItem) Main.itemLoader.getPotions().get(list.getSelected())).strengthIncrease);
			}
			if(agilityincreaseInfoLabel == null){
				agilityincreaseInfoLabel = new Label("Agility increase: " + ((PotionItem) Main.itemLoader.getPotions().get(list.getSelected())).agilityIncrease, Assets.getSkin(), "fixedsys_32");
			}else{
				agilityincreaseInfoLabel.setText("Agility increase: " + ((PotionItem) Main.itemLoader.getPotions().get(list.getSelected())).agilityIncrease);
			}
			if(durationInfoLabel == null){
				String timeS = "";
				if(((PotionItem) Main.itemLoader.getPotions().get(list.getSelected())).duration == 0){
					timeS = "Instant";
				}else{
					timeS = "" + ((PotionItem) Main.itemLoader.getPotions().get(list.getSelected())).duration;
				}
				durationInfoLabel = new Label("Duration: " + timeS, Assets.getSkin(), "fixedsys_32");
			}else{
				String timeS = "";
				if(((PotionItem) Main.itemLoader.getPotions().get(list.getSelected())).duration == 0){
					timeS = "Instant";
				}else{
					timeS = "" + ((PotionItem) Main.itemLoader.getPotions().get(list.getSelected())).duration;
				}
				durationInfoLabel.setText("Duration: " + timeS);
			}
			if(Main.itemLoader.getPotions().get(list.getSelected()).texture != null){
				shopPreviewSpriteActor = new SpriteActor(Main.itemLoader.getPotions().get(list.getSelected()).texture, 0, 0, false, false, MenuScreen.getCamera());
				shopPreviewSpriteActor.setScale(4);
			}else{
				shopPreviewSpriteActor = null;
			}
			
			Table infoTable = new Table();
			
			infoTable.add(costInfoLabel).fill().padBottom(5).row();
			infoTable.add(castleRequiredLabel).fill().padBottom(5).row();
			infoTable.add(durationInfoLabel).fill().padBottom(5).row();
			infoTable.add(healInfoLabel).fill().padBottom(5).row();
			infoTable.add(strengthincreaseInfoLabel).fill().padBottom(5).row();
			infoTable.add(agilityincreaseInfoLabel).fill().padBottom(5).row();
			rightBottomInfoTable.add(infoTable).fill();
			if(shopPreviewSpriteActor != null){
				rightBottomInfoTable.add(shopPreviewSpriteActor).fill().padLeft(5);
			}
		}
		
		selectionCost = cost;
		selectionBuildingLevel = castlebuildinglevel;
		
		String costS = "";
		if(UserData.getBoolean(list.getSelected() + "_bought")){
			costS = "bought";
		}else{
			costS = cost + "";
		}
		String requiredS = "";
		if(castlebuildinglevel != 0){
			requiredS = castlebuilding + " Lv. " + castlebuildinglevel;
		}else{
			requiredS = "No level required";
		}
		costInfoLabel.setText("Cost: " + costS);
		castleRequiredLabel.setText("Required: " + requiredS);
		currentCategorieLabel.setText(currentCategorieString);
	}
	
	private Array<String> createListItems(Collection<Item> items){
		Array<String> listItems = new Array<String>();
		for(Item item : items){
			listItems.add(item.name);
		}
		return listItems;
	}
}