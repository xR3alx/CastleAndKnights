package com.cak.customactors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class AnimationActor extends Actor {

	private Animation animation;
	private float animationTime, scaleX, scaleY;
	private int relativeX, relativeY;
	
	public AnimationActor(float frameDuration, float scaleXY , int relativeX, int relativeY, PlayMode playMode, Array<? extends TextureRegion> textures) {
		if(textures != null){
			animation = new Animation(frameDuration, textures, playMode);
			this.relativeX = relativeX;
			this.relativeY = relativeY;
			scaleX = scaleXY;
			scaleY = scaleXY;
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		animationTime+=delta;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		if(animation != null){
			TextureRegion region = animation.getKeyFrame(animationTime);
			batch.draw(region, getX() + relativeX, getY() + relativeY, region.getRegionWidth() * scaleX, region.getRegionHeight() * scaleY);
		}
	}
	
	public void setScale(float scaleXY){
		scaleX = scaleXY;
		scaleY = scaleXY;
	}
	
	public void setScale(float scaleX, float scaleY){
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
}
