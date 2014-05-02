package org.yomic.LD29;

import org.yomic.LD29.screens.TitleScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LD29 extends Game {
	SpriteBatch spriteBatch;
	Texture img;
	public final static int CAMERA_WIDTH = 640;
	public final static int CAMERA_HEIGHT = 480;
	public final static int TILE_WIDTH = 32;
	public final static int TILE_HEIGHT = 32;
	
	@Override
	public void create () {
		spriteBatch = new SpriteBatch();
		
		setScreen(new TitleScreen(this));
	}
}
