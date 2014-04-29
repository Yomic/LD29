package org.yomic.LD29.screens;

import java.util.ArrayList;
import java.util.Random;

import org.yomic.LD29.LD29;
import org.yomic.LD29.SoundFX;
import org.yomic.LD29.objects.Actor;
import org.yomic.LD29.objects.Actor.ActorType;
import org.yomic.LD29.objects.Boss;
import org.yomic.LD29.objects.Pearl;
import org.yomic.LD29.objects.PearlDoor;
import org.yomic.LD29.objects.Player;
import org.yomic.LD29.objects.Player.FACING;
import org.yomic.LD29.objects.Player.STATE;
import org.yomic.LD29.objects.SavePoint;
import org.yomic.LD29.objects.Spikes;
import org.yomic.LD29.objects.Spikes.SpikeDir;
import org.yomic.LD29.objects.Starfish;
import org.yomic.LD29.objects.StarfishDoor;
import org.yomic.LD29.objects.TiledObject;
import org.yomic.LD29.objects.Urchin;
import org.yomic.LD29.objects.Wall;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameScreen implements Screen {
	
	Game game;
	enum gameStates {Init, Playing}
	SpriteBatch spriteBatch;
	OrthographicCamera camera;
	InputMultiplexer inputMultiplexer;
	Player player;
	Animation playerAnimation;
	Boss boss;
	
	TextureRegion wallTexture, pearlTexture, pearlDoorTexture, starfishTexture, starfishDoorTexture, 
	spikeBtexture, spikeTtexture, spikeLtexture, spikeRtexture, 
	urchinTexture, saveTexture, bubbleTexture, seaweedTexture, rockTexture, bossTexture,
	blackScreenTexture, creditsTexture, outro1Texture, outro2Texture;
	TextureRegion[] fishRegion;
	Sprite blackScreen, intro1, intro2, outro1, outro2;
	TextureAtlas atlas;
	
	//Tiled object setup
	TiledMap map;
	OrthogonalTiledMapRenderer renderer;
	TiledMapTileLayer collisionLayer;
	TiledMapTileLayer objectLayer;
	ArrayList<TiledObject> tiledObjects;
	ArrayList<TiledObject> solidObjects;
	ArrayList<Actor> objects;
	
	//Boss stuff
	boolean showBoss = false;
	float bossTint = 0;
	float bossAlpha = 0;
	boolean keysSpawned[];	
	
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
					solidObjects.add(new Wall(new Sprite(wallTexture), true, x, y));
				}
				if (collisionLayer.getCell(x, y).getTile() != null && collisionLayer.getCell(x, y).getTile().getProperties().containsKey("fakewall")) {
					tiledObjects.add(new Wall(new Sprite(wallTexture), false, x, y));
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
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("starfish")) {
					objects.add(new Starfish(new Sprite(starfishTexture), x, y));
				}
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("starfishdoor")) {
					objects.add(new StarfishDoor(new Sprite(starfishDoorTexture), x, y));
				}
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("seaweed")) {
					objects.add(new Actor(new Sprite(seaweedTexture), x, y));
				}
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("rock")) {
					objects.add(new Actor(new Sprite(rockTexture), x, y));
				}
				
				
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("savepoint")) {
					objects.add(new SavePoint(new Sprite(saveTexture), x, y));
				}
				
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("spikesT")) {
					objects.add(new Spikes(new Sprite(spikeTtexture), x, y, SpikeDir.Top));
				}
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("spikesB")) {					
					objects.add(new Spikes(new Sprite(spikeBtexture), x, y, SpikeDir.Bottom));
				}
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("spikesL")) {					
					objects.add(new Spikes(new Sprite(spikeLtexture), x, y, SpikeDir.Left));
				}
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("spikesR")) {					
					objects.add(new Spikes(new Sprite(spikeRtexture), x, y, SpikeDir.Right));
				}
				
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("urchinUD")) {
					objects.add(new Urchin(new Sprite(urchinTexture), x, y, true, solidObjects));
				}
				if (objectLayer.getCell(x, y).getTile() != null && objectLayer.getCell(x, y).getTile().getProperties().containsKey("urchinLR")) {
					objects.add(new Urchin(new Sprite(urchinTexture), x, y, false, solidObjects));
				}
				
				
			}
		}
	}
	
	@Override
	public void show() {
		spriteBatch = new SpriteBatch();
		
		atlas = new TextureAtlas();
		atlas = new TextureAtlas(Gdx.files.internal("atlas/pack.pack"));
		wallTexture = atlas.findRegion("rock_wall_solid");
		pearlTexture = atlas.findRegion("pearl");
		pearlDoorTexture = atlas.findRegion("pearl_door");
		starfishTexture = atlas.findRegion("star");
		starfishDoorTexture = atlas.findRegion("star_door");
		spikeBtexture = atlas.findRegion("spike_bottom");
		spikeTtexture = atlas.findRegion("spike_top");
		spikeLtexture = atlas.findRegion("spike_left");
		spikeRtexture = atlas.findRegion("spike_right");
		urchinTexture = atlas.findRegion("urchin");
		saveTexture = atlas.findRegion("savepoint");		
		seaweedTexture = atlas.findRegion("seaweed");
		rockTexture = atlas.findRegion("rock");
		bossTexture = atlas.findRegion("boss");
		
		blackScreenTexture = atlas.findRegion("blackScreen");
		creditsTexture = atlas.findRegion("Credits");	
		outro1Texture = atlas.findRegion("Outro1");
		outro2Texture = atlas.findRegion("Outro2");
		blackScreen = new Sprite(blackScreenTexture);
		outro1 = new Sprite(outro1Texture);
		outro2 = new Sprite(outro2Texture);		
		
		fishRegion = new TextureRegion[4];
		
		fishRegion[0] = atlas.findRegion("fish1");
		fishRegion[1] = atlas.findRegion("fish2");
		fishRegion[2] = atlas.findRegion("fish3");
		fishRegion[3] = atlas.findRegion("fish2");
		
		playerAnimation = new Animation(0.25f, fishRegion);
		
		player = new Player(new Sprite(new Texture(Gdx.files.internal("fish1.png"))), 14*32, 192*32);
		
		boss = new Boss(new Sprite(bossTexture), 52, 25);
		
		keysSpawned = new boolean[6];
		for (int i = 0; i < keysSpawned.length; i++) {
			keysSpawned[i] = false;
		}		
		
		Gdx.input.setInputProcessor(player);
		
		tiledObjects = new ArrayList<TiledObject>();
		solidObjects = new ArrayList<TiledObject>();
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
	boolean haveConfinedPlayer = false;
	boolean bossDefeated = false;
	boolean gameWon = false;
	float deltaBoss = 0;
	float deltaAnimation = 0;
	
	@Override
	public void render(float delta) {
		if (player.getY() < 36*32) {
			//final boss area
			if (!haveConfinedPlayer) {
				player.confine();
				haveConfinedPlayer = true;
				player.resetDoorCounter();
			}
			camera.position.set((int)(53*32) + cameraOffsetX, (int)(30*32), 0);
		} else  {
			//normal
			camera.position.set((int)(player.getX() + player.getWidth()/2), (int)(player.getY() + player.getHeight() / 2), 0);
		}
		
		
		if (gameWon) {
			player.setPosition(16*32, 30*32);
			Gdx.input.setInputProcessor(null);
			camera.position.set((int)(16*32), (int)(30*32), 0);			
		}
		
		camera.update();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (delta > 1/60f) {
			delta = 1/60f;
		}
		
		renderer.setView(camera);
		renderer.render(background);
		
		if (player.isConfined() && !gameWon) {
			deltaBoss += delta;
		}
		
		if (!gameWon && player.isConfined()) bossEvents(deltaBoss, delta);
		
		if (gameWon) gameWonEvents(delta);
		
		deltaAnimation += delta;
		if (player.getState() == STATE.Moving) deltaAnimation += delta; //add delta again to make it look like it's going faster
		player.setRegion(playerAnimation.getKeyFrame(deltaAnimation, true));
		if (player.getFacing() == FACING.LEFT) player.flip(true, false);
		player.update(delta, tiledObjects, objects);
		
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		
		for (Actor a : objects) {
			if (a.isAlive() && a != null) {
				boolean closeToPlayer;
				
				if (!player.isConfined()) {
					closeToPlayer = Math.abs(player.getX() - a.getX()) < LD29.CAMERA_WIDTH/2 ||
							Math.abs(player.getY() - a.getY()) < LD29.CAMERA_HEIGHT/2;
				} else {
					closeToPlayer = Math.abs(player.getX() - a.getX()) < LD29.CAMERA_WIDTH*2 ||
							Math.abs(player.getY() - a.getY()) < LD29.CAMERA_HEIGHT*2;
				}					
				
				if (a.thisType == ActorType.Pearl && closeToPlayer) {
					a.update(delta, player);
					a.draw(spriteBatch);
				} else if (a.thisType == ActorType.Starfish && closeToPlayer) {
					a.update(delta, player);
					a.draw(spriteBatch);
				} else if (a.thisType == ActorType.Urchin && closeToPlayer) {
					
					a.update(delta, player);
					
					if (player.isConfined()) {
						if (a.getY() > 40*32) a.die();
					}
					
					a.draw(spriteBatch);
				} else if (a.thisType == ActorType.PearlDoor && closeToPlayer) {
					a.update(player);
					a.draw(spriteBatch);
				} else if (a.thisType == ActorType.StarfishDoor && closeToPlayer) {
					a.update(player);
					a.draw(spriteBatch);
				} else if (a.thisType == ActorType.Spikes && closeToPlayer) {
					a.draw(spriteBatch);
				} else {
					if (closeToPlayer) a.draw(spriteBatch);
				}
			}			
		}
		
		if (showBoss) {
			boss.setColor(bossTint, bossTint, bossTint, bossAlpha);
			boss.draw(spriteBatch);
		}
		
		player.draw(spriteBatch);
		
		if (deltaBossDead >= 10) {
			blackScreen.setPosition(player.getX() - Gdx.graphics.getWidth()/2, player.getY() - Gdx.graphics.getHeight()/2);
			blackScreen.setColor(1, 1, 1, blackScreenAlpha);
			blackScreen.scale(2);
			blackScreen.draw(spriteBatch);
		}
		
		if (gameWon) {
			outro1.setPosition(player.getX() - Gdx.graphics.getWidth()/2, player.getY() - Gdx.graphics.getHeight()/2);
			outro1.setColor(1, 1, 1, outroAlpha);
			outro1.draw(spriteBatch);
			
			outro2.setPosition(player.getX() - Gdx.graphics.getWidth()/2, player.getY() - Gdx.graphics.getHeight()/2);
			outro2.setColor(1, 1, 1, outro2Alpha);
			outro2.draw(spriteBatch);
		}
		
		spriteBatch.end();
	}
	
	boolean spawnWave = true;
	float deltaNewWave = 0;
	float deltaBossDead = 0;	
	float cameraOffsetX = 0;
	boolean cameraShakeRight = true;
	float blackScreenAlpha = 0;
	
	private void bossEvents(float deltaBoss, float delta) {
		/* Uncomment this and the confined check above to start at boss fight
		if (!haveConfinedPlayer) {			
			player.confine();
			player.setPosition(52*32, 25*32);
			haveConfinedPlayer = true;
			player.resetDoorCounter();
		}
		*/		
		
		player.setSavedPosition(53*32, 35*32);
		
		if (player.getUnlockedDoors() == 6) {
			bossDefeated = true;
		}
		
		if (deltaBoss > 3 && deltaBoss < 4.5f) {
			showBoss = true;
			bossAlpha += .02;
			if (bossAlpha > 1) bossAlpha = 1;
		}
		
		if (deltaBoss > 4.5f && deltaBoss < 6) {
			bossTint += .02;
			if (bossTint > 1) bossTint = 1;
		}
		
		if (deltaBoss > 2 && deltaBoss < 7) {
			if (cameraShakeRight) {
				cameraOffsetX += 1;
				if (cameraOffsetX >= 3) {
					//SoundFX.sfx.playShakeScreen();
					cameraShakeRight = false;
				}
			} else {
				cameraOffsetX -= 1;
				if (cameraOffsetX <= -3) {
					cameraShakeRight = true;
					SoundFX.sfx.playShakeScreen();
				}
			}
		}
		
		if (deltaBoss > 8 && spawnWave && bossDefeated == false) {
			cameraOffsetX = 0;
			Random random = new Random();
			int randInt = random.nextInt(16);
			for (int x = 44; x < 62; x++) {
				if (x != 45+randInt) objects.add(new Urchin(new Sprite(urchinTexture), x, 200-174, true, solidObjects));
			}
			spawnWave = false;
		}
		
		if (deltaNewWave > 2.5) {
			deltaNewWave = 0;
			spawnWave = true;
		}
		
		if (!spawnWave) {
			deltaNewWave += delta;
		}
		
		if (bossDefeated) {
			deltaBossDead += delta;
		}
		
		if (deltaBossDead > 2 && deltaBossDead < 4) {
			if (deltaBoss < 49) beatBossEarly = true;
			bossTint -= .01;
			if (bossTint < 0) bossTint = 0;
		}
		
		if (deltaBossDead > 4 && deltaBossDead < 6) {
			bossAlpha -= .01;
			if (bossAlpha < 0) bossAlpha = 0;
		}
		
		if (deltaBossDead > 1 && deltaBossDead < 8) {
			if (cameraShakeRight) {
				cameraOffsetX += 1;
				if (cameraOffsetX >= 3) {
					SoundFX.sfx.playShakeScreen();
					cameraShakeRight = false;
				}
			} else {
				cameraOffsetX -= 1;
				if (cameraOffsetX <= -3) {
					SoundFX.sfx.playShakeScreen();
					cameraShakeRight = true;
				}
			}
		}
		
		if (deltaBossDead > 6 && deltaBossDead < 8) {
			cameraOffsetX = 0;
		}
		
		if (deltaBossDead > 10 && deltaBossDead < 12) {
			blackScreenAlpha += .01;
			if (blackScreenAlpha > 1) blackScreenAlpha = 1;
		}
		
		if (deltaBossDead > 12) {
			gameWon = true;			
		}
		
		if (deltaBoss > 10 && !keysSpawned[0]) {			
			objects.add(new Pearl(new Sprite(pearlTexture), 46, 200-173));
			keysSpawned[0] = true;
		}
		
		if (deltaBoss > 20 && !keysSpawned[1]) {
			objects.add(new Starfish(new Sprite(starfishTexture), 59, 200-173));
			keysSpawned[1] = true;
		}
		
		if (deltaBoss > 30 && !keysSpawned[2]) {
			objects.add(new Pearl(new Sprite(pearlTexture), 46, 200-174));
			keysSpawned[2] = true;
		}
		
		if (deltaBoss > 40 && !keysSpawned[3]) {
			objects.add(new Starfish(new Sprite(starfishTexture), 59, 200-174));
			keysSpawned[3] = true;
		}
		
		if (deltaBoss > 50 && !keysSpawned[4]) {
			objects.add(new Pearl(new Sprite(pearlTexture), 46, 200-175));
			keysSpawned[4] = true;
		}
		
		if (deltaBoss > 60 && !keysSpawned[5]) {
			objects.add(new Starfish(new Sprite(starfishTexture), 59, 200-175));
			keysSpawned[5] = true;
		}
	}
	
	private float deltaWonEvents = 0;
	private float outroAlpha = 0;
	private float outro2Alpha = 0;
	private boolean beatBossEarly = false;
	private void gameWonEvents(float delta) {
		deltaWonEvents += delta;
		
		if (deltaWonEvents > 1 && deltaWonEvents < 3) {
			blackScreenAlpha -= .01;
			if (blackScreenAlpha < 0) blackScreenAlpha = 0;
		}
		
		if (deltaWonEvents > 5) {
			outroAlpha += .01;
			if (outroAlpha > 1) outroAlpha = 1;
		}
		
		if (deltaWonEvents > 10 && beatBossEarly) {
			outro2Alpha += .01;
			if (outro2Alpha > 1) outro2Alpha = 1;
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
		player.getTexture().dispose();
		boss.getTexture().dispose();
		wallTexture.getTexture().dispose();
		pearlTexture.getTexture().dispose();
		pearlDoorTexture.getTexture().dispose();
		starfishTexture.getTexture().dispose();
		starfishDoorTexture.getTexture().dispose();
		spikeBtexture.getTexture().dispose();
		spikeTtexture.getTexture().dispose();
		spikeLtexture.getTexture().dispose();
		spikeRtexture.getTexture().dispose();
		urchinTexture.getTexture().dispose();
		saveTexture.getTexture().dispose();
		seaweedTexture.getTexture().dispose();
		rockTexture.getTexture().dispose();
		bossTexture.getTexture().dispose();		
		blackScreenTexture.getTexture().dispose();
		creditsTexture.getTexture().dispose();
		outro1Texture.getTexture().dispose();
		blackScreen.getTexture().dispose();
		outro1.getTexture().dispose();
		
		map.dispose();
		renderer.dispose();		
		for (TiledObject o : tiledObjects) {
			o.getTexture().dispose();
		}
		for (Actor a : objects) {
			a.getTexture().dispose();
		}
	}
}
