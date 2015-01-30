package com.cak.worldobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PolygonObject {

	private Body body;
	
	public PolygonObject(World world, Vector2 position, BodyType bodyType, float width, float height, float density, float friction, float restitution, boolean sensor, boolean useFilter, short filterCat, short filterGroup, short filterMask){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodyType;
		bodyDef.position.set(position);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.6f;
		fixtureDef.restitution = 0.4f;
		if(useFilter){
			if(filterGroup != ContactFilters.NONE){
				fixtureDef.filter.groupIndex = filterGroup;
			}
			if(filterCat != ContactFilters.NONE){
				fixtureDef.filter.categoryBits = filterCat;
			}
			if(filterMask != ContactFilters.NONE){
				fixtureDef.filter.maskBits = filterMask;
			}
		}
		fixtureDef.isSensor = sensor;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 2);
		fixtureDef.shape = shape;
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
	}
	
	public Body getBody(){
		return body;
	}
	
}
