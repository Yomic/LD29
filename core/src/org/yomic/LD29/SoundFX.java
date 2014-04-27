package org.yomic.LD29;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundFX {

	public static SoundFX sfx = new SoundFX();
	
	public SoundFX() {
		loadSFX();
	}
	
	private static Sound getItem;
	private static Sound hurt;
	private static Sound save;
	private static Sound shakeScreen;
	private static Sound unlock;
	
	private void loadSFX() {
		getItem = Gdx.audio.newSound(Gdx.files.internal("sfx/itemGet.wav"));
		shakeScreen = Gdx.audio.newSound(Gdx.files.internal("sfx/shakeScreen.wav"));
		hurt = Gdx.audio.newSound(Gdx.files.internal("sfx/hurt.wav"));
		save = Gdx.audio.newSound(Gdx.files.internal("sfx/save.wav"));
		unlock = Gdx.audio.newSound(Gdx.files.internal("sfx/unlock.wav"));
	}
	
	public void dispose() {
		getItem.dispose();
		shakeScreen.dispose();
		hurt.dispose();
		save.dispose();
		unlock.dispose();
	}
	
	public void playGetItem() {
		getItem.play(0.5f);
	}
	
	public void playShakeScreen() {
		shakeScreen.play(0.5f);
	}
	
	public void playHurt() {
		hurt.play(0.5f);
	}
	
	public void playSave() {
		save.play(0.5f);
	}
	
	public void playUnlock() {
		unlock.play(0.5f);
	}
}
