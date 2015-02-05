package com.cak.bodydata;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.cak.bodyuserdata.SpriteAnimationUserData;

public class PlayerBodyData extends SpriteAnimationUserData{

	public int entityId;
	
	public PlayerBodyData(int entityId, Array<AtlasRegion> textures, float frameDuration,
			PlayMode playMode, boolean repeat, float width, float height) {
		super(textures, frameDuration, playMode, repeat, width, height);
		this.entityId = entityId;
	}

}
