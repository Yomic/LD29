package org.yomic.LD29.objects;

import java.util.ArrayList;

import org.yomic.LD29.LD29;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;

public class Actor extends TiledObject implements ActorInterface {
	
	boolean harmful;
	public enum ActorType {Urchin, Pearl, PearlDoor, SavePoint, Spikes, Starfish, StarfishDoor, Boss}
	public ActorType thisType;
	private final String blockedKey = "blocked";

	public Actor(Sprite sprite, int x, int y) {
		super(sprite, false, x, y);
		this.alive = true;
		setX(x*32);
		setY(y*32);
		this.rect = new Rectangle(getX(), getY(), getWidth(), getHeight());
	}
	
	public boolean isHarmful() {
		return this.harmful;
	}

	@Override
	public void update(float delta, Player player) {
		System.out.println("Override update for object:  " + this.thisType.toString());
	}

	@Override
	public void update(float delta) {
		System.out.println("Override update for object:  " + this.thisType.toString());
	}

	public void update(float delta, ArrayList<TiledObject> tiles, Player player) {
		System.out.println("Override update for object:  " + this.thisType.toString());
	}
	
	private boolean isCellBlocked(Cell cell) {		
        return cell.getTile() != null && cell.getTile().getProperties().containsKey(blockedKey);
	}
	
	boolean checkCollisionX(TiledMapTileLayer tiledObjects, float previousX) {
		
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
				
		
		return collisionX;
	}
	
	boolean checkCollisionY(TiledMapTileLayer tiledObjects, float previousY) {
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
		
		return collisionY;
	}
	
	void getNewRect() {
		this.rect = new Rectangle(getX(), getY(), getWidth(), getHeight());
	}
	
	public void setSpeed(int speed) {
		
	}
}
