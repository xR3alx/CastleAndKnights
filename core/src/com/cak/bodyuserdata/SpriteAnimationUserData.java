package com.cak.bodyuserdata;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class SpriteAnimationUserData extends DrawUserData{

	private float animationTime;
	private boolean repeat, hasFinished, flipX, flipY,
					run;
	private PlayMode playMode;
	private Animation animation;
	
	public SpriteAnimationUserData(Array<AtlasRegion> textures, float frameDuration, PlayMode playMode, boolean repeat, float width, float height){
		this.width = width;
		this.height = height;
		this.repeat = repeat;
		this.playMode = playMode;
		animation = new Animation(frameDuration, textures);
		animation.setPlayMode(playMode);
	}
	
	@Override
	public void render(SpriteBatch spriteBatch, float delta, float x, float y, float angle){
		if(!hasFinished){
			if(run){
				animationTime+=delta;
			}
			
			Sprite sprite = new Sprite(animation.getKeyFrame(animationTime));
			sprite.setSize(width * 0.9f, height * 0.9f);
			sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
			sprite.setRotation(angle * MathUtils.radiansToDegrees);
			sprite.setPosition(x - sprite.getWidth() / 2, y - sprite.getHeight() / 2);
			sprite.setFlip(flipX, flipY);
			sprite.draw(spriteBatch);
			
			if(animation.isAnimationFinished(animationTime)){
				if(!repeat){
					hasFinished = true;
				}
				animationTime = 0;
			}
		}
	}
	
	public void changeAnimation(Array<AtlasRegion> textures, float frameDuration){
		animation = new Animation(frameDuration, textures);
		animation.setPlayMode(playMode);
	}
	
	public void setFlip(boolean x, boolean y){
		flipX = x;
		flipY = y;
	}
	
	public void reset(){
		animationTime = 0;
	}
	
	public boolean isFinished(float delta, boolean resetAnimationTimeOnComplete) {
		if(animation.isAnimationFinished(animationTime+delta)){
			if(resetAnimationTimeOnComplete){
				animationTime = 0;
			}
			return true;
		}else{
			return false;
		}
	}
	
	public void start(){
		run = true;
	}
	
	public void stop(){
		run = false;
	}
}
