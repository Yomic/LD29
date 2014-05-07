package org.yomic.LD29.objects;

import java.util.ArrayList;

import org.yomic.LD29.LD29;
import org.yomic.LD29.SoundFX;
import org.yomic.LD29.objects.Actor.ActorType;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor {
	
	public enum STATE {Idle, Moving}
	private STATE currentState;
	public enum FACING {LEFT, RIGHT};
	private FACING facing;
	Vector2 savedPosition;
	Vector2 direction;
	private boolean confined;
	private float lastTimeHurt, lastTimeSaved;
	boolean isAndroid;
	
	Sprite sprite;
	Vector2 acceleration = new Vector2();
	Vector2 velocity = new Vector2();
	Rectangle rect;
	
	int pearls, starfish, unlockedDoors;
	
	private float moveSpeed = 240;
	
	private TiledMapTileLayer tiledObjects;
	
	public Player (Sprite sprite, int x, int y, TiledMapTileLayer collisionLayer) {
		super(sprite);
		
		isAndroid = Gdx.app.getType() == ApplicationType.Android;
		
		this.pearls = 0;
		this.starfish = 0;
		savedPosition = new Vector2(x, y);
		this.confined = false;
		this.facing = FACING.RIGHT;
		this.lastTimeHurt = 0;
		this.lastTimeSaved = 0;
		this.tiledObjects = collisionLayer;
		direction = new Vector2(0,0);
		reset();
	}
	
	public void reset() {
		this.currentState = STATE.Idle;		
		rect = new Rectangle();
		this.setPosition(savedPosition.x, savedPosition.y);
		rect.width = getWidth();
		rect.height = getHeight();
	}
	
	public void update(float delta, ArrayList<Actor> actors) {
		
		float previousX = getX();
		float previousY = getY();
		
		lastTimeHurt += delta;
		lastTimeSaved += delta;
		
		System.out.println(direction.x * moveSpeed * delta + ", " + direction.y * moveSpeed * delta);
		if (!isAndroid) {
			setX((int)(getX() + (velocity.x * delta)));
		} else {
			setX((int)(getX() + (direction.x * moveSpeed * delta)));
		}
		
		this.rect.x = getX();
		checkCollisionX(actors, previousX);
		
		if (!isAndroid) {
			setY((int)(getY() + (velocity.y * delta)));
		} else {
			setY((int)(getY() + (direction.y * moveSpeed * delta)));
		}
		
		this.rect.y = getY();
		checkCollisionY(actors, previousY);
		
		for (Actor a : actors) {
			if (a.isHarmful() && this.rect.overlaps(a.rect)) {
				if (lastTimeHurt > 1) {
					SoundFX.sfx.playHurt();
					lastTimeHurt = 0;
				}				
				reset();
				break;
			}			
		}
		
	}
	
	public void confine() {
		this.confined = true;
		this.savedPosition.set(53*32, getY()); //Mid screen
	}
	
	public void givePearl() {
		this.pearls += 1;
	}

	public void giveStarfish() {
		this.starfish += 1;
	}
	
	private void checkCollisionX(ArrayList<Actor> actors, float previousX) {
		
		if (getX() < 32 || getX() + getWidth() > 99*32) {
			setX(previousX);
			this.rect.x = getX();	
		}
		
		for (Actor a : actors) {
			if (a.blocked && this.rect.overlaps(a.rect)) {
				if ((a.thisType == ActorType.PearlDoor && this.pearls > 0 || !a.alive) ||
						(a.thisType == ActorType.StarfishDoor && this.starfish > 0 || !a.alive)) {
					//go through
				} else {					
					setX(previousX);
					this.rect.x = getX();
					break;
				}				
			}
			
			if (a.thisType == ActorType.SavePoint && this.rect.overlaps(a.rect)) {
				savePoint();
			}
		}
		
		boolean collisionX = false;
		
		//top left
		collisionX = isCellBlocked(tiledObjects.getCell((int)(getX() / LD29.TILE_WIDTH),(int)((getY() + getHeight()) / LD29.TILE_HEIGHT)));
		
		//mid left
		if (!collisionX)
			collisionX = isCellBlocked(tiledObjects.getCell((int)(getX() / LD29.TILE_WIDTH),(int) ((getY() + getHeight() / 2) / LD29.TILE_HEIGHT)));
	    		
		//bot left
		if (!collisionX)
			collisionX = isCellBlocked(tiledObjects.getCell((int)(getX() / LD29.TILE_WIDTH),(int) (getY() / LD29.TILE_HEIGHT)));				
				
		//top right
		if (!collisionX)
			collisionX = isCellBlocked(tiledObjects.getCell((int)((getX() + getWidth()) / LD29.TILE_WIDTH),(int)((getY() + getHeight()) / LD29.TILE_HEIGHT)));			
			
		//mid right
		if (!collisionX)
			collisionX = isCellBlocked(tiledObjects.getCell((int)((getX() + getWidth()) / LD29.TILE_WIDTH),(int)((getY() + getHeight() / 2) / LD29.TILE_HEIGHT)));				
						
		//bot right
		if (!collisionX)
			collisionX = isCellBlocked(tiledObjects.getCell((int)((getX() + getWidth()) / LD29.TILE_WIDTH),(int)(getY() / LD29.TILE_HEIGHT)));
		
		if (collisionX) {
			setX(previousX);
			this.rect.x = getX();
		}
		
	}

	private void checkCollisionY(ArrayList<Actor> actors, float previousY) {
		
		if (!confined) {
			if (getY() < 32 || getY() + getHeight() > 199*32) {
				setY(previousY);
				this.rect.y = getY();
			}
		} else {
			if (getY() + getHeight() >= 36*32) {
				setY(36*32 - getHeight());
				this.rect.y = getY();
			}
		}
		
		for (Actor a : actors) {
			if (a.blocked && this.rect.overlaps(a.rect)) {
				if ((a.thisType == ActorType.PearlDoor && this.pearls > 0 || !a.alive) ||
						(a.thisType == ActorType.StarfishDoor && this.starfish > 0 || !a.alive)) {	
					//go through
				} else {
					setY(previousY);
					this.rect.y = getY();
					break;
				}				
			}
			
			if (a.thisType == ActorType.SavePoint && this.rect.overlaps(a.rect)) {
				savePoint();
			}
		}
		
		boolean collisionY = false;
		
		//bot left			
		collisionY = isCellBlocked(tiledObjects.getCell((int)(getX() / LD29.TILE_WIDTH),(int)(getY() / LD29.TILE_HEIGHT)));			
			
		//bot mid			
		if (!collisionY)
			collisionY = isCellBlocked(tiledObjects.getCell((int)((getX() + getWidth() / 2) / LD29.TILE_WIDTH),(int)(getY() / LD29.TILE_HEIGHT)));				
			
		//bot right
		if (!collisionY)
			collisionY = isCellBlocked(tiledObjects.getCell((int)((getX() + getWidth()) / LD29.TILE_WIDTH),(int)(getY() / LD29.TILE_HEIGHT)));				
			
		//top left
		if (!collisionY)
			collisionY = isCellBlocked(tiledObjects.getCell((int)(getX() / LD29.TILE_WIDTH),(int)((getY() + getHeight()) / LD29.TILE_HEIGHT)));
			
		//top mid
		if (!collisionY)
			collisionY = isCellBlocked(tiledObjects.getCell((int)((getX() + getWidth() / 2) / LD29.TILE_WIDTH),(int)((getY() + getHeight()) / LD29.TILE_HEIGHT)));
							
		//top right
		if (!collisionY)
			collisionY = isCellBlocked(tiledObjects.getCell((int)((getX() + getWidth()) / LD29.TILE_WIDTH),(int)((getY() + getHeight()) / LD29.TILE_HEIGHT)));				
		
		if (collisionY) {
			setY(previousY);
			this.rect.y = getY();
		}
		
	}
	
	private boolean isCellBlocked(Cell cell) {		
        return cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked");
	}
	
	private void savePoint() {
		if (lastTimeSaved > 2) {
			SoundFX.sfx.playSave();
			lastTimeSaved = 0;
		}		
		savedPosition.set(getX(), getY());
	}
	
	public void drawRect(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rect(this.rect.x, this.rect.y, this.rect.width, this.rect.height);
		shapeRenderer.end();
	}
	
	public STATE getState() {
		return this.currentState;
	}
	
	public void addDoorCounter() {
		this.unlockedDoors += 1;
	}
	
	public int getUnlockedDoors() {
		return this.unlockedDoors;
	}
	
	public void resetDoorCounter() {
		this.unlockedDoors = 0;
	}
	
	public void setSavedPosition(int x, int y) {
		this.savedPosition.set(x, y);
	}
	
	public FACING getFacing() {
		return this.facing;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		
		switch(keycode) {
		case Keys.UP:			
			velocity.y += moveSpeed;
			currentState = STATE.Moving;			
			break;
		case Keys.DOWN:
			velocity.y -= moveSpeed;
			currentState = STATE.Moving;
			break;
		case Keys.LEFT:
			velocity.x -= moveSpeed;
			currentState = STATE.Moving;
			if (facing == FACING.RIGHT) this.flip(true, false);
			facing = FACING.LEFT;
			break;
		case Keys.RIGHT:
			velocity.x += moveSpeed;
			currentState = STATE.Moving;
			if (facing == FACING.LEFT) this.flip(true, false);
			facing = FACING.RIGHT;
			break;
				
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
		case Keys.UP:
			velocity.y -= moveSpeed;
			break;
		case Keys.DOWN:
			velocity.y += moveSpeed;
			break;
		case Keys.LEFT:
			velocity.x += moveSpeed;
			break;
		case Keys.RIGHT:
			velocity.x -= moveSpeed;
			break;
		}
		if (velocity.x == 0 && velocity.y == 0) currentState = STATE.Idle; 
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenY = Gdx.graphics.getHeight() - screenY;
		
		int playerX = (int)Gdx.graphics.getWidth()/2;
		int playerY = (int)Gdx.graphics.getHeight()/2;
		
		float distance = (float) Math.sqrt(
										 Math.pow(screenX - (playerX), 2) + 
										 Math.pow(screenY - (playerY), 2)
										 );
				
		direction = new Vector2((screenX - (playerX)) / distance,
								(screenY - (playerY)) / distance);
		
		currentState = STATE.Moving;
		
		if (screenX > playerX) {
			if (facing == FACING.LEFT) this.flip(true, false);
			facing = FACING.RIGHT;
		} else if (screenX < playerX) {
			if (facing == FACING.RIGHT) this.flip(true, false);
			facing = FACING.LEFT;
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		currentState = STATE.Idle;
		direction.x = 0;
		direction.y = 0;
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		screenY = Gdx.graphics.getHeight() - screenY;
		
		int playerX = (int)Gdx.graphics.getWidth()/2;
		int playerY = (int)Gdx.graphics.getHeight()/2;
		
		float distance = (float) Math.sqrt(
										 Math.pow(screenX - (playerX), 2) + 
										 Math.pow(screenY - (playerY), 2)
										 );
				
		direction = new Vector2((screenX - (playerX)) / distance,
								(screenY - (playerY)) / distance);
		
		currentState = STATE.Moving;
		
		if (screenX > playerX) {
			if (facing == FACING.LEFT) this.flip(true, false);
			facing = FACING.RIGHT;
		} else if (screenX < playerX) {
			if (facing == FACING.RIGHT) this.flip(true, false);
			facing = FACING.LEFT;
		}
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public boolean isConfined() {
		return this.confined;
	}
	
	public float getSpeed() {
		return this.moveSpeed;
	}
	
}
