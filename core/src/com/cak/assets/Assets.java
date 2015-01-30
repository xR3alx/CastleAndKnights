package com.cak.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {

	private static AssetManager assetManager;
	private static Skin skin;

	public static String loadConfiguration;
	
	private static boolean menuAssetsLoaded, ingameAssetsLoaded;
	private static Screen screenToSwitchAfterLoad;
	
	@SuppressWarnings("static-access")
	public Assets(String loadConfiguration){
		this.loadConfiguration = loadConfiguration;
		
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.setLoader(ParticleEffect.class, new ParticleEffectLoader(new InternalFileHandleResolver()));
		skin = new Skin(Gdx.files.internal(loadConfiguration + "/ui/skin.json"), new TextureAtlas(Gdx.files.internal(loadConfiguration + "/ui/skin.atlas")));
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/itemtextures").list()) {
			if(f.extension().equals("png")){
				assetManager.load(f.path(), Texture.class); 
			}
		}
	}
	
	public static boolean isLoaded(){
		return assetManager.update();
	}
	
	public static Skin getSkin(){
		return skin;
	}
	
	public static void dispose(){
		assetManager.dispose();
	}
	
	public static <T> T get(String file, Class<T> type){
		return assetManager.get(loadConfiguration + "/" + file, type);
	}
	
	public static AssetManager getAssetManager(){
		return assetManager;
	}
	
	private static void setScreenAfterLoad(Screen screen){
		screenToSwitchAfterLoad = screen;
	}
	
	public static Screen getScreenAfterLoad(){
		return screenToSwitchAfterLoad;
	}
	
	public static boolean isMenuAssetsLoaded(Screen screen){
		if(menuAssetsLoaded){
			setScreenAfterLoad(screen);
		}
		return menuAssetsLoaded;
	}
	
	public static boolean isIngameAssetsLoaded(String mappath){
		boolean maploaded = false;
		if(assetManager.isLoaded(loadConfiguration + mappath)){
			maploaded = true;
		}
		return ingameAssetsLoaded && maploaded ? true : false;
	}
	
	
	
	
	public static void loadIngameAssets(Screen screen, String mappath){
		setScreenAfterLoad(screen);
		unloadMenuAssets();
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/ingame/gfx/imgs/").list()) {
			if(f.extension().equals("png") || f.extension().equals("jpg")){
				assetManager.load(f.path(), Texture.class); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/ingame/gfx/packs/").list()) {
			if(f.extension().equals("pack")){
				assetManager.load(f.path(), TextureAtlas.class); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/ingame/gfx/animations/").list()) {
			if(f.extension().equals("pack")){
				assetManager.load(f.path(), TextureAtlas.class); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/ingame/gfx/animations/weapons/").list()) {
			if(f.extension().equals("pack")){
				assetManager.load(f.path(), TextureAtlas.class); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/ingame/gfx/animations/armor/").list()) {
			if(f.extension().equals("pack")){
				assetManager.load(f.path(), TextureAtlas.class); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/ingame/audio/music/").list()) {
			if(f.extension().equals("ogg")){
				assetManager.load(f.path(), Music.class); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/ingame/audio/sounds/").list()) {
			if(f.extension().equals("ogg")){
				assetManager.load(f.path(), Sound.class); 
			}
		}
		
		assetManager.load(loadConfiguration + mappath, TiledMap.class); 
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/ingame/particleeffects/").list()) {
			if(f.extension().equals("p")){
				assetManager.load(f.path(), ParticleEffect.class); 
			}
		}
		
		ingameAssetsLoaded = true;
	}
	
	public static void unloadGameAssets(){
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/ingame/gfx/imgs/").list()) {
			if(f.extension().equals("png") || f.extension().equals("jpg")){
				if(assetManager.isLoaded(f.path()))
					assetManager.unload(f.path()); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/ingame/gfx/animations/").list()) {
			if(f.extension().equals("pack")){
				if(assetManager.isLoaded(f.path()))
					assetManager.unload(f.path()); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/ingame/gfx/animations/weapons/").list()) {
			if(f.extension().equals("pack")){
				if(assetManager.isLoaded(f.path()))
					assetManager.unload(f.path()); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/ingame/gfx/animations/armor/").list()) {
			if(f.extension().equals("pack")){
				if(assetManager.isLoaded(f.path()))
					assetManager.unload(f.path()); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/ingame/gfx/packs/").list()) {
			if(f.extension().equals("pack")){
				if(assetManager.isLoaded(f.path()))
					assetManager.unload(f.path()); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/ingame/audio/music/").list()) {
			if(f.extension().equals("ogg")){
				if(assetManager.isLoaded(f.path()))
					assetManager.unload(f.path()); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/ingame/audio/sounds/").list()) {
			if(f.extension().equals("ogg")){
				if(assetManager.isLoaded(f.path()))
					assetManager.unload(f.path()); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/ingame/maps/").list()) {
			if(f.extension().equals("tmx")){
				if(assetManager.isLoaded(f.path()))
					assetManager.unload(f.path()); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/ingame/particleeffects/").list()) {
			if(f.extension().equals("p")){
				if(assetManager.isLoaded(f.path()))
					assetManager.unload(f.path()); 
			}
		}
		
		ingameAssetsLoaded = false;
	}
	
	
	
	
	public static void loadMenuAssets(Screen screen){
		setScreenAfterLoad(screen);
		unloadGameAssets();
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/menu/gfx/imgs/").list()) {
			if(f.extension().equals("png") || f.extension().equals("jpg")){
				assetManager.load(f.path(), Texture.class); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/menu/gfx/packs/").list()) {
			if(f.extension().equals("pack")){
				assetManager.load(f.path(), TextureAtlas.class); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/menu/gfx/animations/").list()) {
			if(f.extension().equals("pack")){
				assetManager.load(f.path(), TextureAtlas.class); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/menu/audio/music/").list()) {
			if(f.extension().equals("ogg")){
				assetManager.load(f.path(), Music.class); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/menu/audio/sounds/").list()) {
			if(f.extension().equals("ogg")){
				assetManager.load(f.path(), Sound.class); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/menu/particleeffects/").list()) {
			if(f.extension().equals("p")){
				assetManager.load(f.path(), ParticleEffect.class); 
			}
		}
		
		menuAssetsLoaded = true;
	}
	
	public static void unloadMenuAssets(){
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/menu/gfx/imgs/").list()) {
			if(f.extension().equals("png") || f.extension().equals("jpg")){
				if(assetManager.isLoaded(f.path()))
					assetManager.unload(f.path()); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/menu/gfx/animations/").list()) {
			if(f.extension().equals("pack")){
				if(assetManager.isLoaded(f.path()))
					assetManager.unload(f.path()); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/menu/gfx/packs/").list()) {
			if(f.extension().equals("pack")){
				if(assetManager.isLoaded(f.path()))
					assetManager.unload(f.path()); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/menu/audio/music/").list()) {
			if(f.extension().equals("ogg")){
				if(assetManager.isLoaded(f.path()))
					assetManager.unload(f.path()); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/menu/audio/sounds/").list()) {
			if(f.extension().equals("ogg")){
				if(assetManager.isLoaded(f.path()))
					assetManager.unload(f.path()); 
			}
		}
		
		for (FileHandle f : Gdx.files.internal(loadConfiguration + "/menu/particleeffects/").list()) {
			if(f.extension().equals("p")){
				if(assetManager.isLoaded(f.path()))
					assetManager.unload(f.path()); 
			}
		}
		
		menuAssetsLoaded = false;
	}
}
