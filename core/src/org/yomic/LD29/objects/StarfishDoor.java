package org.yomic.LD29.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class StarfishDoor extends Actor {

	public StarfishDoor(Sprite sprite, int x, int y) {
		super(sprite, x, y);
		this.blocked = true;
		this.thisType = ActorType.StarfishDoor;
	}
	
	@Override
	public void update(Player player) {
		
		if (this.alive && this.rect.overlaps(player.rect)) {
			if (player.starfish > 0) {
				player.starfish -= 1;
				die();
				player.addDoorCounter();
			}
		}
		
	}

}
