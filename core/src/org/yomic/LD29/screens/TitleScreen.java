package org.yomic.LD29.screens;

import org.yomic.LD29.LD29;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TitleScreen implements Screen, InputProcessor{
	
	Game game;
	private OrthographicCamera camera;
	private SpriteBatch spriteBatch;
	
	Texture creditsTexture, intro1Texture, intro2Texture;
	
	int page = 0;
	
	public TitleScreen(Game game) {
		this.game = game;
	}

	@Override
	public void show() {
		spriteBatch = new SpriteBatch();
		
		creditsTexture = new Texture(Gdx.files.internal("Credits.png"));
		intro1Texture = new Texture(Gdx.files.internal("Intro1.png"));
		intro2Texture = new Texture(Gdx.files.internal("Intro2.png"));
		
		Gdx.input.setInputProcessor(this);
	}
	
	@Override
	public void resize(int width, int height) {
		this.camera = new OrthographicCamera(LD29.CAMERA_WIDTH, LD29.CAMERA_HEIGHT);
		this.camera.position.set(LD29.CAMERA_WIDTH / 2f, LD29.CAMERA_HEIGHT / 2f, 0);
		this.camera.update();
	}
	
	@Override
	public void render(float delta) {
		
		camera.update();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		spriteBatch.setProjectionMatrix(camera.combined); 
		spriteBatch.begin();
		
		if (page == 0) {
			spriteBatch.draw(creditsTexture, 0, 0);
		} else if (page == 1) {
			spriteBatch.draw(intro1Texture, 0, 0);
		} else {
			spriteBatch.draw(intro2Texture, 0, 0);
		}
		
		spriteBatch.end();
		
		if (page >= 3) {
			game.setScreen(new GameScreen(game));
		}
	}

	@Override
	public boolean keyDown(int keycode) {		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		page += 1;
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		page += 1;
		return true;
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
		creditsTexture.dispose();
		intro1Texture.dispose();
		intro2Texture.dispose();
	}

}
