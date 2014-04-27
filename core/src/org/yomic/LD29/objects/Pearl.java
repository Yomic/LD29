package org.yomic.LD29.objects;

import org.yomic.LD29.SoundFX;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Pearl extends Actor {

	public Pearl(Sprite sprite, int x, int y) {
		super(sprite, x, y);
		this.thisType = ActorType.Pearl;
	}
	
	@Override
	public void update(float delta, Player player) {
		
		if (isAlive()) {			
			if (player.rect.overlaps(this.rect)) {
				player.givePearl();
				player.savedPosition.set(player.getX(), player.getY());
				SoundFX.sfx.playGetItem();
				die();				
			}
		}
	}

}
