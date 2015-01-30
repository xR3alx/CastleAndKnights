package com.cak.customactors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class SpriteActor extends Actor {

	private Sprite sprite;
	private int relativeX, relativeY;
	private boolean matchScreenSizeX, matchScreenSizeY;
	
	public SpriteActor(Texture texture, int relativeX, int relativeY, boolean matchScreenSizeX, boolean matchScreenSizeY) {
		if(texture != null){
			sprite = new Sprite(texture);
			setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
		}
		this.matchScreenSizeX = matchScreenSizeX;
		this.matchScreenSizeY = matchScreenSizeY;
		this.relativeX = relativeX;
		this.relativeY = relativeY;
	}

	public SpriteActor(AtlasRegion findRegion, int relativeX2, int relativeY2,
			boolean matchScreenSizeX2, boolean matchScreenSizeY2) {
		if(findRegion != null){
			sprite = new Sprite(findRegion);
			setBounds(getX(), getY(), findRegion.packedWidth, findRegion.packedHeight);
		}
		this.matchScreenSizeX = matchScreenSizeX2;
		this.matchScreenSizeY = matchScreenSizeY2;
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
			batch.draw(sprite, getX() + relativeX, getY() + relativeY, matchScreenSizeX ? Gdx.graphics.getWidth() : sprite.getWidth() * sprite.getScaleX(), matchScreenSizeY ? Gdx.graphics.getHeight() : sprite.getHeight() * sprite.getScaleY());
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
}
