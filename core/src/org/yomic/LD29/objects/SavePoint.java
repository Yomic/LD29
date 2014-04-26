package org.yomic.LD29.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class SavePoint extends Actor {

	public SavePoint(Sprite sprite, int x, int y) {
		super(sprite, x, y);
		this.thisType = ActorType.SavePoint;
	}

}
