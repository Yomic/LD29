package org.yomic.LD29.objects;

import java.util.ArrayList;

import org.yomic.LD29.objects.Actor.ActorType;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor {
	
	enum STATE {Idle, Moving}
	@SuppressWarnings("unused")
	private STATE currentState;
	enum FACING {LEFT, RIGHT};
	private FACING facing;
	Vector2 savedPosition;
	
	Sprite sprite;
	Vector2 acceleration = new Vector2();
	Vector2 velocity = new Vector2();
	Rectangle rect;
	
	int pearls;
	
	private float moveSpeed = 240;
	
	public Player (Sprite sprite, int x, int y) {
		super(sprite);
		this.pearls = 0;
		savedPosition = new Vector2(x, y);
		reset();
	}
	
	public void reset() {
		this.currentState = STATE.Idle;
		if (facing == FACING.LEFT) this.flip(true, false);
		this.facing = FACING.RIGHT;
		rect = new Rectangle();
		this.setPosition(savedPosition.x, savedPosition.y);
	}
	
	public void update(float delta, ArrayList<TiledObject> tiledObjects, ArrayList<Actor> actors) {
		
		float previousX = getX();
		float previousY = getY();
		
		setX(getX() + velocity.x * delta);
		getNewRect();
		checkCollisionX(tiledObjects, actors, previousX);
		
		setY(getY() + velocity.y * delta);
		getNewRect();
		checkCollisionY(tiledObjects, actors, previousY);
		
		if (velocity.x == 0 && velocity.y == 0) {
			this.currentState = STATE.Idle;
		}
		
		for (Actor a : actors) {
			if (a.isHarmful() && this.rect.overlaps(a.rect)) {
				//TODO play SFX; let player know that was a bad thing to touch
				reset();
			}			
		}
		
	}
	
	public void givePearl() {
		this.pearls += 1;
	}

	private void checkCollisionX(ArrayList<TiledObject> tiledObjects, ArrayList<Actor> actors, float previousX) {
		
		for (TiledObject o : tiledObjects) {
			if (this.rect.overlaps(o.rect) && o.blocked) {
				setX(previousX);
				getNewRect();
				break;
			}
		}
		
		for (Actor a : actors) {
			if (a.blocked && this.rect.overlaps(a.rect)) {
				if (a.thisType == ActorType.PearlDoor && this.pearls > 0 || !a.alive) {					
					//go through
				} else {					
					setX(previousX);
					getNewRect();
					break;
				}				
			}
			
			if (a.thisType == ActorType.SavePoint && this.rect.overlaps(a.rect)) {
				savedPosition.set(getX(), getY());
			}
		}
		
	}
	
	private void checkCollisionY(ArrayList<TiledObject> tiledObjects, ArrayList<Actor> actors, float previousY) {
		
		for (TiledObject o : tiledObjects) {
			if (this.rect.overlaps(o.rect) && o.blocked) {				
				setY(previousY);
				getNewRect();
				break;
			}			
		}
		
		for (Actor a : actors) {
			if (a.blocked && this.rect.overlaps(a.rect)) {
				if (a.thisType == ActorType.PearlDoor && pearls > 0 || !a.alive) {
					//go through
				} else {
					setY(previousY);
					getNewRect();
					break;
				}				
			}
			
			if (a.thisType == ActorType.SavePoint && this.rect.overlaps(a.rect)) {
				savedPosition.set(getX(), getY());
			}
		}
		
	}
	
	private void getNewRect() {
		this.rect = new Rectangle(getX(), getY(), getWidth(), getHeight());
	}
	
	public void drawRect(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rect(this.rect.x, this.rect.y, this.rect.width, this.rect.height);
		shapeRenderer.end();
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
		return true;
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
