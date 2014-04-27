package org.yomic.LD29.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Starfish extends Actor {

	public Starfish(Sprite sprite, int x, int y) {
		super(sprite, x, y);
		this.thisType = ActorType.Starfish;
	}
	
	@Override
	public void update(float delta, Player player) {
		
		if (isAlive()) {			
			if (player.rect.overlaps(this.rect)) {
				player.giveStarfish();
				//player.savedPosition.set(player.getX(), player.getY());
				//We'll make this collectible when in danger so you don't have to worry about spawning next to danger
				die();
				System.out.println("starfish: " + player.starfish);
			}
		}
	}

}
