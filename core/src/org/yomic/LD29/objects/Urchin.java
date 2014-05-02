package org.yomic.LD29.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Urchin extends Actor {
	
	enum UrchinFacing {Left, Right, Up, Down}
	UrchinFacing currentFacing;
	Vector2 velocity = new Vector2();
	int speed = 100;
	
	TiledMapTileLayer collisionLayer;

	public Urchin(Sprite sprite, int x, int y, boolean UD, TiledMapTileLayer collisionMap) {
		super(sprite, x, y);
		this.thisType = ActorType.Urchin;
		this.harmful = true;
		this.collisionLayer = collisionMap;
		if (UD) {
			currentFacing = UrchinFacing.Up;
		} else {
			currentFacing = UrchinFacing.Right;
		}
		
	}
	
	@Override
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	@Override
	public void update(float delta, Player player) {
		
		if (isAlive()) {
			
			float previousX = getX();
			float previousY = getY();
			boolean changeDir = false;
			
			if (currentFacing == UrchinFacing.Up) {
				velocity.y = speed;
			} else if (currentFacing == UrchinFacing.Down) {
				velocity.y = -speed;
			} else if (currentFacing == UrchinFacing.Left) {
				velocity.x = -speed;
			} else if (currentFacing == UrchinFacing.Right) {
				velocity.x = speed;
			}
			
			if (Math.abs(velocity.x) > 0) {
				setX(getX() + velocity.x * .0175f);
				this.rect.x = getX();
				changeDir = checkCollisionX(collisionLayer, previousX);
			}			
			
			if (Math.abs(velocity.y) > 0) {
				setY(getY() + velocity.y * .0175f);
				this.rect.y = getY();
				changeDir = checkCollisionY(collisionLayer, previousY);
			}			
			
			if (changeDir) {
				if (currentFacing == UrchinFacing.Up) { 
					currentFacing = UrchinFacing.Down;
				} else if (currentFacing == UrchinFacing.Down) {
					currentFacing = UrchinFacing.Up;
				}				
				
				if (currentFacing == UrchinFacing.Left) {
					currentFacing = UrchinFacing.Right;					
				} else if (currentFacing == UrchinFacing.Right) {
					currentFacing = UrchinFacing.Left;					
				}
			}
			
		}
	}	
	
}
