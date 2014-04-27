package org.yomic.LD29.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Boss extends Actor {

	public Boss(Sprite sprite, int x, int y) {
		super(sprite, x, y);
		this.thisType = ActorType.Boss;
		this.harmful = true;
	}

}
