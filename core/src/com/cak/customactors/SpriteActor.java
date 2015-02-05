package com.cak.customactors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SpriteActor extends Actor {

	private Sprite sprite;
	private int relativeX, relativeY;
	private float screenSizeWidth, screenSizeHeight;
	private boolean matchScreenSizeX, matchScreenSizeY;
	
	public SpriteActor(Texture texture, int relativeX, int relativeY, boolean matchScreenSizeX, boolean matchScreenSizeY, OrthographicCamera camera) {
		if(texture != null){
			sprite = new Sprite(texture);
			setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
		}
		
		if(matchScreenSizeX){
			this.matchScreenSizeX = matchScreenSizeX;
			screenSizeWidth = camera.viewportWidth;
		}
		if(matchScreenSizeY){
			this.matchScreenSizeY = matchScreenSizeY;
			screenSizeHeight = camera.viewportHeight;
		}
		
		this.relativeX = relativeX;
		this.relativeY = relativeY;
	}

	public SpriteActor(AtlasRegion findRegion, int relativeX2, int relativeY2, boolean matchScreenSizeX2, boolean matchScreenSizeY2, OrthographicCamera camera) {
		if(findRegion != null){
			sprite = new Sprite(findRegion);
			setBounds(getX(), getY(), findRegion.packedWidth, findRegion.packedHeight);
		}
		
		if(matchScreenSizeX2){
			this.matchScreenSizeX = matchScreenSizeX2;
			screenSizeWidth = camera.viewportWidth;
		}
		if(matchScreenSizeY2){
			this.matchScreenSizeY = matchScreenSizeY2;
			screenSizeHeight = camera.viewportHeight;
		}
		
		this.relativeX = relativeX2;
		this.relativeY = relativeY2;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		if(sprite != null){
			sprite.setColor(Color.BLACK);
			batch.draw(sprite, getX() + relativeX, getY() + relativeY, matchScreenSizeX ? screenSizeWidth : sprite.getWidth() * sprite.getScaleX(), matchScreenSizeY ? screenSizeHeight : sprite.getHeight() * sprite.getScaleY());
		}
	}
	
	public void setScale(float scaleXY){
		sprite.setScale(scaleXY);
		setBounds(getX(), getY(), sprite.getWidth() * scaleXY, sprite.getHeight() * scaleXY);
	}
	
	public void setScale(float scaleX, float scaleY){
		sprite.setScale(scaleX, scaleY);
		setBounds(getX(), getY(), sprite.getWidth() * scaleX, sprite.getHeight() * scaleY);
	}

	public void changeTexture(Texture newTexture){
		sprite = new Sprite(newTexture);
		setBounds(getX(), getY(), newTexture.getWidth(), newTexture.getHeight());
	}
	
	public void changeTexture(AtlasRegion newTexture) {
		sprite = new Sprite(newTexture);
		setBounds(getX(), getY(), newTexture.packedWidth, newTexture.packedHeight);
	}
	
	
	public void centerActor(OrthographicCamera camera){
		sprite.setPosition(camera.viewportWidth / 2 - sprite.getWidth() / 2, camera.viewportHeight / 2 - sprite.getHeight() / 2);
		setBounds(camera.viewportWidth / 2 - sprite.getWidth() / 2, camera.viewportHeight / 2 - sprite.getHeight() / 2, sprite.getWidth(), sprite.getHeight());
	}
}
