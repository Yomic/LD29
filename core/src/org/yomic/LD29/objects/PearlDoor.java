package org.yomic.LD29.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class PearlDoor extends Actor {

	public PearlDoor(Sprite sprite, boolean blocked, int x, int y) {
		super(sprite, x, y);
		this.blocked = true;
		this.thisType = ActorType.PearlDoor;
	}
	
	@Override
	public void update(Player player) {
		
		if (this.alive && this.rect.overlaps(player.rect)) {
			if (player.pearls > 0) {
				player.pearls -= 1;
				die();
			}
		}
		
	}

}
