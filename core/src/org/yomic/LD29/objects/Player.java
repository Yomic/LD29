package org.yomic.LD29.objects;

import java.util.ArrayList;

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
	
	Sprite sprite;
	Vector2 acceleration = new Vector2();
	Vector2 velocity = new Vector2();
	Rectangle rect;
	
	private float moveSpeed = 240;
	
	public Player (Sprite sprite) {
		super(sprite);
		
		this.currentState = STATE.Idle;
		this.facing = FACING.RIGHT;
		rect = new Rectangle();
	}
	
	public void update(float delta, ArrayList<TiledObject> tiledObjects) {
		
		float previousX = getX();
		float previousY = getY();
		
		setX(getX() + velocity.x * delta);
		getNewRect();
		checkCollisionX(tiledObjects, previousX);
		
		setY(getY() + velocity.y * delta);
		getNewRect();
		checkCollisionY(tiledObjects, previousY);
		
		if (velocity.x == 0 && velocity.y == 0) {
			this.currentState = STATE.Idle;
		}
		
	}

	private void checkCollisionX(ArrayList<TiledObject> tiledObjects, float previousX) {
		
		for (TiledObject o : tiledObjects) {
			if (this.rect.overlaps(o.rect) && o.blocked) {
				setX(previousX);
				getNewRect();
				break;
			}
		}
		
	}
	
	private void checkCollisionY(ArrayList<TiledObject> tiledObjects, float previousY) {
		
		for (TiledObject o : tiledObjects) {
			if (this.rect.overlaps(o.rect) && o.blocked) {
				
				setY(previousY);
				getNewRect();
				break;
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
