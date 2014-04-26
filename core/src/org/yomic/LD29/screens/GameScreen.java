package org.yomic.LD29.screens;

import java.util.ArrayList;

import org.yomic.LD29.LD29;
import org.yomic.LD29.objects.Actor;
import org.yomic.LD29.objects.Actor.ActorType;
import org.yomic.LD29.objects.Pearl;
import org.yomic.LD29.objects.PearlDoor;
import org.yomic.LD29.objects.Player;
import org.yomic.LD29.objects.SavePoint;
import org.yomic.LD29.objects.TiledObject;
import org.yomic.LD29.objects.Urchin;
import org.yomic.LD29.objects.Wall;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameScreen implements Screen, InputProcessor {
	
	Game game;
	enum gameStates {Init, Playing}
	SpriteBatch spriteBatch;
	ShapeRenderer shapeRenderer;
	OrthographicCamera camera;
	InputMultiplexer inputMultiplexer;
	Player player;
	Texture wallTexture, pearlTexture, pearlDoorTexture, 
	spikeBtexture, spikeTtexture, spikeLtexture, spikeRtexture, 
	urchinTexture, saveTexture, bubbleTexture;
	
	//Tiled object setup
	TiledMap map;
	OrthogonalTiledMapRenderer renderer;
	TiledMapTileLayer collisionLayer;
	TiledMapTileLayer objectLayer;
	ArrayList<TiledObject> tiledObjects;
	ArrayList<Actor> objects;
	
	public GameScreen(Game game) {
		this.game = game;
		camera = new OrthographicCamera();
	}
	
	private void loadMap() {
		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load("map.tmx");
		
		renderer = new OrthogonalTiledMapRenderer(map);
		objectLayer = (TiledMapTileLayer) map.getLayers().get("objects");
		collisionLayer = (TiledMapTileLayer) map.getLayers().get("walls");
		
		for (int x = 0; x < collisionLayer.getWidth(); x++) {
			for (int y = 0; y < collisionLayer.getHeight(); y++) {
				
				if (collisionLayer.getCell(x, y).getTile() != null && collisionLayer.getCell(x, y).getTile().getProperties().containsKey("blocked")) {
					tiledObjects.add(new Wall(new Sprite(wallTexture), true, x, y));
				}
				
			}
		}
		
		for (int x = 0; x < collisionLayer.getWidth(); x++) {
			for (int y = 0; y < collisionLayer.getHeight(); y++) {
				
				//System.out.println("x: " + x + " y: " + y);
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("pearl")) {
					objects.add(new Pearl(new Sprite(pearlTexture), x, y));
				}
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("pearldoor")) {
					objects.add(new PearlDoor(new Sprite(pearlDoorTexture), true, x, y));
				}
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("savepoint")) {
					objects.add(new SavePoint(new Sprite(saveTexture), x, y));
				}
				
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("spikesT")) {
					//TODO add new Spikes instead of Actor
					objects.add(new Actor(new Sprite(spikeTtexture), x, y));
				}
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("spikesB")) {					
					objects.add(new Actor(new Sprite(spikeBtexture), x, y));
				}
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("spikesL")) {					
					objects.add(new Actor(new Sprite(spikeLtexture), x, y));
				}
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("spikesR")) {					
					objects.add(new Actor(new Sprite(spikeRtexture), x, y));
				}
				
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("urchinUD")) {
					//TODO add new Urchin instead of Actor
					objects.add(new Urchin(new Sprite(urchinTexture), x, y, true));
				}
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("urchinLR")) {
					//TODO add new Urchin instead of Actor
					objects.add(new Urchin(new Sprite(urchinTexture), x, y, false));
				}
				
				
			}
		}
	}
	
	@Override
	public void show() {
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		player = new Player(new Sprite(new Texture(Gdx.files.internal("fish.png"))), 4*32, 298*32);
		//player.setPosition(4*32, 298*32);
		
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(player);
		//TODO add Player to MP
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		wallTexture = new Texture(Gdx.files.internal("rock_wall_solid.png"));
		pearlTexture = new Texture(Gdx.files.internal("pearl.png"));
		pearlDoorTexture = new Texture(Gdx.files.internal("pearl_door.png"));
		spikeBtexture = new Texture(Gdx.files.internal("spike_bottom.png"));
		spikeTtexture = new Texture(Gdx.files.internal("spike_top.png"));
		spikeLtexture = new Texture(Gdx.files.internal("spike_left.png"));
		spikeRtexture = new Texture(Gdx.files.internal("spike_right.png"));
		urchinTexture = new Texture(Gdx.files.internal("urchin.png"));
		saveTexture = new Texture(Gdx.files.internal("savepoint.png"));
		bubbleTexture = new Texture(Gdx.files.internal("bubble.png"));
		
		tiledObjects = new ArrayList<TiledObject>();
		objects = new ArrayList<Actor>();
		loadMap();
	}

	@Override
	public void resize(int width, int height) {		
		this.camera = new OrthographicCamera(LD29.CAMERA_WIDTH, LD29.CAMERA_HEIGHT);
		this.camera.position.set(LD29.CAMERA_WIDTH / 2f, LD29.CAMERA_HEIGHT / 2f, 0);
		this.camera.update();
	}	
	
	private int[] background = new int[] {0,1};
	
	@Override
	public void render(float delta) {
		camera.position.set((int)(player.getX() + player.getWidth()/2), (int)(player.getY() + player.getHeight() / 2), 0);
		camera.update();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (delta > 1/60f) {
			delta = 1/60f;
		}
		
		renderer.setView(camera);
		renderer.render(background);
		renderer.getSpriteBatch().begin();
		renderer.getSpriteBatch().end();
		
		player.update(delta, tiledObjects, objects);
		
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		
		for (Actor a : objects) {
			if (a.isAlive() && a != null) {
				if (a.thisType == ActorType.Pearl) a.update(delta, player);
				if (a.thisType == ActorType.Urchin) a.update(delta, tiledObjects, player);
				if (a.thisType == ActorType.PearlDoor) a.update(player);
				a.draw(spriteBatch);
			}			
		}
		
		player.draw(spriteBatch);
		
		spriteBatch.end();
		
		//SHAPE DEBUG
		shapeRenderer.setColor(Color.RED);
		player.drawRect(shapeRenderer);
		for (TiledObject o : tiledObjects) {
			o.drawRect(shapeRenderer);
		}
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
