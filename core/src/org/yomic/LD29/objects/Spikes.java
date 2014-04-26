package org.yomic.LD29.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Spikes extends Actor {

	public enum SpikeDir {Top, Bottom, Left, Right}
	SpikeDir facing;
	
	public Spikes(Sprite sprite, int x, int y, SpikeDir facing) {
		super(sprite, x, y);
		this.facing = facing;
		this.thisType = ActorType.Spikes;
		this.harmful = true;
		
		int spikeSize = 11;
		int emptySpace = 32 - spikeSize;
		
		if (facing == SpikeDir.Top) {
			this.rect = new Rectangle(getX(), getY() + emptySpace, getWidth(), spikeSize);
		} else if (facing == SpikeDir.Bottom) {
			this.rect = new Rectangle(getX(), getY(), getWidth(), spikeSize);
		} else if (facing == SpikeDir.Left) {
			this.rect = new Rectangle(getX(), getY(), spikeSize, getHeight());
		} else if (facing == SpikeDir.Right) {
			this.rect = new Rectangle(getX() + emptySpace, getY(), spikeSize, getHeight());
		}
			
	}
	
}
