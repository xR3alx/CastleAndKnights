package com.cak.notifications;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.cak.assets.Assets;
import com.cak.customactors.SpriteActor;
import com.cak.main.Main;

public class NotificationsManager {

	private ArrayList<Notification> notificationQeue;
	
	private Table mainTable, notificationTable;
	private Label titleLabel, textLabel;
	
	private boolean isNotificationRunning;
	
	public NotificationsManager() {
		notificationQeue = new ArrayList<Notification>();
		
		mainTable = new Table();
		mainTable.setBackground(Assets.getSkin().getDrawable("table_background_transparent_dark"));
		
		notificationTable = new Table();
		notificationTable.setBackground(Assets.getSkin().getDrawable("table_background_light"));
		
		titleLabel = new Label("", Assets.getSkin(), "courier_32");
		titleLabel.pack();
		textLabel = new Label("", Assets.getSkin(), "fixedsys_32");
		textLabel.setWrap(true);
		textLabel.pack();
		
		notificationTable.add(titleLabel).pad(100).padBottom(5).padTop(10).fill().row();
		notificationTable.add(textLabel).pad(10).maxWidth(Main.VIRTUAL_RESOLUTION_WIDTH / 2).fill();
		
		mainTable.add(notificationTable).expand();
		
		notificationTable.setVisible(false);
		mainTable.setVisible(false);
	}
	
	public void update(Stage stage){
		if(!stage.getActors().contains(mainTable, true)){
			stage.addActor(mainTable);
			
			mainTable.invalidateHierarchy();
			mainTable.setWidth(stage.getViewport().getCamera().viewportWidth);
			mainTable.setHeight(stage.getViewport().getCamera().viewportHeight);
		}
		
		if(notificationQeue.size() != 0){
			mainTable.addAction(Actions.show());
			mainTable.addAction(Actions.after(Actions.fadeIn(1f)));
			
			if(!isNotificationRunning){
				Notification notification = notificationQeue.get(0);
				titleLabel.setText(notification.title);
				textLabel.setText(notification.text);
				notificationTable.pack();
				
				notificationTable.addAction(Actions.moveTo(mainTable.getWidth() / 2 - notificationTable.getWidth() / 2, -notificationTable.getHeight()));
				notificationTable.addAction(Actions.show());
				notificationTable.addAction(Actions.moveTo(mainTable.getWidth() / 2 - notificationTable.getWidth() / 2, mainTable.getHeight() / 2 - notificationTable.getHeight() / 2, 1f));
				notificationTable.addAction(Actions.delay(5f, Actions.moveBy(0, mainTable.getHeight(), 1f)));
				notificationTable.addAction(Actions.delay(6f, Actions.hide()));
				
				notificationQeue.remove(0);
				isNotificationRunning = true;
			}else if(notificationTable.getActions().size == 0){
				isNotificationRunning = false;
			}
		}else{
			if(notificationTable.getActions().size == 0){
				isNotificationRunning = false;
			}
			
			if(!isNotificationRunning){
				mainTable.addAction(Actions.fadeOut(1f));
				mainTable.addAction(Actions.after(Actions.hide()));
			}
		}
	}
	
	public void addNotification(String title, String text){
		Notification notification = new Notification(title, text);
		notificationQeue.add(notification);
	}
	
	
	
	public class Notification {
		public String text, title;
		
		public Notification(String title, String text) {
			this.text = text;
			this.title = title;
		}
	}
}
