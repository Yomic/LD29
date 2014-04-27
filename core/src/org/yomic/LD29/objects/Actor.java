package org.yomic.LD29.objects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Actor extends TiledObject implements ActorInterface {
	
	boolean harmful;
	public enum ActorType {Urchin, Pearl, PearlDoor, SavePoint, Spikes, Starfish, StarfishDoor, Boss}
	public ActorType thisType;

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
		
	}

	@Override
	public void update(float delta) {
		
	}

	public void update(float delta, ArrayList<TiledObject> tiles, Player player) {
		// TODO Auto-generated method stub
		
	}
	
	boolean checkCollisionX(ArrayList<TiledObject> tiledObjects, float previousX) {
		
		for (TiledObject o : tiledObjects) {
			if (this.rect.overlaps(o.rect) && o.blocked) {				
				setX(previousX);
				getNewRect();
				return true;
			}
		}
		return false;
	}
	
	boolean checkCollisionY(ArrayList<TiledObject> tiledObjects, float previousY) {
		
		for (TiledObject o : tiledObjects) {
			if (this.rect.overlaps(o.rect) && o.blocked) {				
				setY(previousY);
				getNewRect();
				return true;
			}
		}
		return false;
	}
	
	void getNewRect() {
		this.rect = new Rectangle(getX(), getY(), getWidth(), getHeight());
	}
}
