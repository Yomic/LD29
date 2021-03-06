package org.yomic.LD29.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class TiledObject extends Sprite {

	boolean blocked;
	Rectangle rect;
	boolean alive;
	
	public TiledObject(Sprite sprite, boolean blocked, int x, int y) {
		super(sprite);
		this.blocked = blocked;
		
		setX(x*32);
		setY(y*32);
		this.rect = new Rectangle(getX(), getY(), getWidth(), getHeight());
	}
	
	public void drawRect(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rect(this.rect.x, this.rect.y, this.rect.width, this.rect.height);
		shapeRenderer.end();
	}
	
	public void die() {
		this.alive = false;
	}
	
	public void revive() {
		this.alive = true;
	}
	
	public boolean isAlive() {
		return this.alive;
	}

	public void update(Player player) {
		
	}
}
