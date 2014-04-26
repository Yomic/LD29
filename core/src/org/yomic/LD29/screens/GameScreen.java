package org.yomic.LD29.screens;

import java.util.ArrayList;

import org.yomic.LD29.LD29;
import org.yomic.LD29.objects.Player;
import org.yomic.LD29.objects.TiledObject;
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
	Texture wallTexture;
	
	//Tiled object setup
	TiledMap map;
	OrthogonalTiledMapRenderer renderer;
	TiledMapTileLayer collisionLayer;
	TiledMapTileLayer objectLayer;
	ArrayList<TiledObject> tiledObjects;
	
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
				
				//TODO add objects like pearls, enemies, spikes, etc
				
			}
		}
	}
	
	@Override
	public void show() {
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		player = new Player(new Sprite(new Texture(Gdx.files.internal("fish.png"))));
		player.setPosition(4*32, 298*32);
		
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(player);
		//TODO add Player to MP
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		wallTexture = new Texture(Gdx.files.internal("rock_wall_solid.png"));
		
		tiledObjects = new ArrayList<TiledObject>();
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
		
		player.update(delta, tiledObjects);
		
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
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
