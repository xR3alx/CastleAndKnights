package com.cak.bodyuserdata;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class SpriteUserData extends DrawUserData {

	private Sprite sprite;
	
	public SpriteUserData(Texture textures, float width, float height){
		this.width = width;
		this.height = height;
		sprite = new Sprite(textures);
	}
	
	public SpriteUserData(TextureRegion textures, float width, float height) {
		this.width = width;
		this.height = height;
		sprite = new Sprite(textures);
	}

	@Override
	public void render(SpriteBatch spriteBatch, float delta, float x, float y, float angle){
		sprite.setSize(width * 0.9f, height * 0.9f);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(x - sprite.getWidth() / 2, y - sprite.getHeight() / 2);
		sprite.setRotation(angle * MathUtils.radiansToDegrees);
		sprite.draw(spriteBatch);
	}
	
	public void changeTexture(Texture texture){
		sprite.setTexture(texture);
	}
	
}
