package com.cak.bodydata;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.cak.bodyuserdata.SpriteAnimationUserData;

public class WeaponBodyData extends SpriteAnimationUserData{

	public int hitId;
	
	public WeaponBodyData(Array<AtlasRegion> textures, float frameDuration,
			PlayMode playMode, boolean repeat, float width, float height) {
		super(textures, frameDuration, playMode, repeat, width, height);
	}

}
