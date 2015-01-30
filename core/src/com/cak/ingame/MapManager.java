package com.cak.ingame;

import java.util.ArrayList;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.cak.assets.Assets;
import com.cak.bodydata.EntityTurnBodyData;
import com.cak.bodydata.FinishBodyData;
import com.cak.bodydata.GroundBodyData;
import com.cak.bodydata.InstantDeathBodyData;
import com.cak.entities.EntityManager;
import com.cak.main.Main;
import com.cak.screens.IngameScreen;
import com.cak.worldobjects.CircleObject;
import com.cak.worldobjects.ContactFilters;
import com.cak.worldobjects.PolygonObject;

public class MapManager {

	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer mapRenderer;
	private OrthographicCamera camera;
	
	private ArrayList<ParticleEffect> particleEffects;
	
	private int[] layers = {0, 1, 2, 3, 4, 5, 6};
	public static final float unitScale = 0.1f;
	
	public MapManager(String mappath) {
		tiledMap = Assets.get(mappath, TiledMap.class);
		mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 * unitScale);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Main.VIRTUAL_RESOLUTION_WIDTH, Main.VIRTUAL_RESOLUTION_HEIGHT);
		camera.zoom = 0.035f;
//		camera.zoom = 0.2f;
//		camera.zoom = 0.8f;

		particleEffects = new ArrayList<ParticleEffect>();
	}
	
	public void render(Vector2 playerPos){
		camera.position.set(playerPos, 0);
		camera.update();
		
		mapRenderer.setView(camera);
		mapRenderer.render(layers);
	}
	
	public void renderParticles(SpriteBatch spriteBatch, float delta){
		for (ParticleEffect particleEffect : particleEffects) {
			if((particleEffect.getEmitters().get(0).getX()) > (camera.position.x - camera.viewportWidth / 2)
					&& (particleEffect.getEmitters().get(0).getX()) < (camera.position.x + camera.viewportWidth / 2)
					&& (particleEffect.getEmitters().get(0).getY()) > (camera.position.y - camera.viewportHeight / 2)
					&& (particleEffect.getEmitters().get(0).getY()) < (camera.position.y + camera.viewportHeight / 2)){
				particleEffect.draw(spriteBatch, delta);
			}
		}
	}
	
	public void setupMap(RayHandler rayHandler, World world, EntityManager entityManager){
		
		String[] rgba = (String[]) ((String) tiledMap.getProperties().get("ambientcolor")).split(",");
		rayHandler.setAmbientLight(Float.parseFloat(rgba[0]), Float.parseFloat(rgba[1]), Float.parseFloat(rgba[2]), Float.parseFloat(rgba[3]));
		
		// player spawn
		for(int x = 0; x < ((TiledMapTileLayer) tiledMap.getLayers().get("system")).getWidth(); x++){
			for(int y = 0; y < ((TiledMapTileLayer) tiledMap.getLayers().get("system")).getWidth(); y++){
				if(((TiledMapTileLayer) tiledMap.getLayers().get("system")).getCell(x, y) != null){
					if(((TiledMapTileLayer) tiledMap.getLayers().get("system")).getCell(x, y).getTile().getProperties().containsKey("p1")){
						entityManager.spawnPlayer(new Vector2(x * IngameScreen.PIXELS_TO_METERS * mapRenderer.getUnitScale(), y * IngameScreen.PIXELS_TO_METERS * mapRenderer.getUnitScale()));
					}else if(((TiledMapTileLayer) tiledMap.getLayers().get("system")).getCell(x, y).getTile().getProperties().containsKey("finish")){
						PolygonObject poly = new PolygonObject(world, new Vector2((x + 0.6f) * IngameScreen.PIXELS_TO_METERS * mapRenderer.getUnitScale(), (y + 1) * IngameScreen.PIXELS_TO_METERS * mapRenderer.getUnitScale()), BodyType.StaticBody, ((TiledMapTileLayer) tiledMap.getLayers().get(1)).getTileWidth() / 2 * mapRenderer.getUnitScale(), ((TiledMapTileLayer) tiledMap.getLayers().get(1)).getTileHeight() * mapRenderer.getUnitScale(), 1.0f, 0.0f, 0.0f, true, true, ContactFilters.CAT_MAP, ContactFilters.GROUP_WORLD, ContactFilters.MASK_MAP);
						poly.getBody().setUserData(new FinishBodyData());
					}
				}
			}
		}
		
		// collision | complete solid
		for(MapObject object : tiledMap.getLayers().get("collisions").getObjects()) {
			if(object instanceof RectangleMapObject) {
				Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
				
				PolygonObject poly = new PolygonObject(world, new Vector2((rectangle.x + rectangle.width / 2) * mapRenderer.getUnitScale(), (rectangle.y+ rectangle.height / 2) * mapRenderer.getUnitScale()), BodyType.StaticBody, rectangle.width * mapRenderer.getUnitScale(), rectangle.height * mapRenderer.getUnitScale(), 1.0f, 0.0f, 0.0f, false, true, ContactFilters.CAT_MAP, ContactFilters.GROUP_WORLD, ContactFilters.MASK_MAP);
				poly.getBody().setUserData(new GroundBodyData());
			}else if(object instanceof EllipseMapObject) {
				Ellipse ellipse = ((EllipseMapObject) object).getEllipse();
				
				CircleObject circle = new CircleObject(world, new Vector2((ellipse.x + ellipse.width / 2) * mapRenderer.getUnitScale(), (ellipse.y + ellipse.height / 2) * mapRenderer.getUnitScale()), BodyType.StaticBody, ellipse.width / 2 * mapRenderer.getUnitScale(), 1.0f, 0.0f, 0.0f, false, true, ContactFilters.CAT_MAP, ContactFilters.GROUP_WORLD, ContactFilters.MASK_MAP);
				circle.getBody().setUserData(new FinishBodyData());
			}
		}
		
		// instant death
		for(MapObject object : tiledMap.getLayers().get("instant death").getObjects()) {
			if(object instanceof RectangleMapObject) {
				Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
				
				PolygonObject poly = new PolygonObject(world, new Vector2((rectangle.x + rectangle.width / 2) * mapRenderer.getUnitScale(), (rectangle.y+ rectangle.height / 2) * mapRenderer.getUnitScale()), BodyType.StaticBody, rectangle.width * mapRenderer.getUnitScale(), rectangle.height * mapRenderer.getUnitScale(), 1.0f, 0.0f, 0.0f, true, true, ContactFilters.NONE, ContactFilters.GROUP_LIGHT, ContactFilters.NONE);
				poly.getBody().setUserData(new InstantDeathBodyData());
			}else if(object instanceof EllipseMapObject) {
				Ellipse ellipse = ((EllipseMapObject) object).getEllipse();
				
				CircleObject circle = new CircleObject(world, new Vector2((ellipse.x + ellipse.width / 2) * mapRenderer.getUnitScale(), (ellipse.y + ellipse.height / 2) * mapRenderer.getUnitScale()), BodyType.StaticBody, ellipse.width / 2 * mapRenderer.getUnitScale(), 1.0f, 0.0f, 0.0f, true, true, ContactFilters.NONE, ContactFilters.GROUP_LIGHT, ContactFilters.NONE);
				circle.getBody().setUserData(new InstantDeathBodyData());
			}
		}
		
		// entity direction turns
		for(MapObject object : tiledMap.getLayers().get("entity direction turns").getObjects()) {
			if(object instanceof RectangleMapObject) {
				Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
				
				PolygonObject poly = new PolygonObject(world, new Vector2((rectangle.x + rectangle.width / 2) * mapRenderer.getUnitScale(), (rectangle.y+ rectangle.height / 2) * mapRenderer.getUnitScale()), BodyType.StaticBody, rectangle.width * mapRenderer.getUnitScale(), rectangle.height * mapRenderer.getUnitScale(), 1.0f, 0.0f, 0.0f, true, true, ContactFilters.NONE, ContactFilters.GROUP_LIGHT, ContactFilters.NONE);
				poly.getBody().setUserData(new EntityTurnBodyData());
			}
		}
		
		
		// entity spawn
		for(MapObject object : tiledMap.getLayers().get("entity spawn").getObjects()) {
			if(object instanceof RectangleMapObject) {
				Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
				
				entityManager.spawnEntity((String) object.getProperties().get("name"), new Vector2(rectangle.x * mapRenderer.getUnitScale(), rectangle.y * mapRenderer.getUnitScale()));
			}
		}
		
		// particle effects
		for(MapObject object : tiledMap.getLayers().get("particle effects").getObjects()) {
			if(object instanceof RectangleMapObject) {
				Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
				
				if(object.getProperties().containsKey("name")){
					ParticleEffect particleEffect = new ParticleEffect(Assets.get("ingame/particleeffects/" + object.getProperties().get("name") + ".p", ParticleEffect.class));
					
					float posX = (rectangle.getX() + (rectangle.getWidth() / 2) - (particleEffect.getEmitters().first().getSpawnWidth().getHighMax() / 2)) * mapRenderer.getUnitScale();
					float posY = rectangle.getY() * mapRenderer.getUnitScale();
					float scaleFactor = mapRenderer.getUnitScale() * 0.5f;
					
//					boolean continous = Boolean.parseBoolean((String) object.getProperties().get("continous"));
//					particleEffect.getEmitters().first().setContinuous(continous);
					
					particleEffect.setPosition(posX, posY);
					particleEffect.scaleEffect(scaleFactor);
					particleEffect.start();
					
					particleEffects.add(particleEffect);
				}
			}
		}
		
		// lights
		for(MapObject object : tiledMap.getLayers().get("lights").getObjects()) {
			if(object instanceof EllipseMapObject) {
				Ellipse ellipse = ((EllipseMapObject) object).getEllipse();
				int rays = Integer.parseInt((String) object.getProperties().get("rays"));
				String[] rgbaL = (String[]) ((String) object.getProperties().get("rgba")).split(",");
				Color ambientColorL = new Color(Float.parseFloat(rgbaL[0]), Float.parseFloat(rgbaL[1]), Float.parseFloat(rgbaL[2]), Float.parseFloat(rgbaL[3]));
				String type = (String) object.getProperties().get("type");
				boolean xray = Boolean.parseBoolean((String) object.getProperties().get("xray"));
						
				if(type.equals("pointlight")){
					PointLight light = new PointLight(rayHandler, rays, ambientColorL, ellipse.width * mapRenderer.getUnitScale() * 5f, (ellipse.x + ellipse.width / 2) * mapRenderer.getUnitScale(), (ellipse.y + ellipse.height / 2) * mapRenderer.getUnitScale());
					light.setXray(xray);
					light.setContactFilter(ContactFilters.NONE, ContactFilters.GROUP_LIGHT, ContactFilters.NONE);
				}else if(type.equals("conelight")){
					int degreeesDirection = Integer.parseInt((String) object.getProperties().get("degreesDirection"));
					int degreesCone = Integer.parseInt((String) object.getProperties().get("degreesCone"));
					
					ConeLight light = new ConeLight(rayHandler, rays, ambientColorL, ellipse.width * mapRenderer.getUnitScale() * 5f, (ellipse.x + ellipse.width / 2) * mapRenderer.getUnitScale(), (ellipse.y + ellipse.height / 2) * mapRenderer.getUnitScale(), degreeesDirection, degreesCone);
					light.setXray(xray);
					light.setContactFilter(ContactFilters.NONE, ContactFilters.GROUP_LIGHT, ContactFilters.NONE);
				}
			}
		}
	}

	
	public OrthographicCamera getCamera() {
		return camera;
	}
	
	public void dispose() {
		tiledMap.dispose();
		mapRenderer.dispose();
	}
	
}
