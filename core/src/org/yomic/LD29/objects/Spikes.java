package org.yomic.LD29.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Spikes extends Actor {

	enum SpikeDir {Top, Bottom, Left, Right}
	SpikeDir facing;
	
	public Spikes(Sprite sprite, int x, int y, SpikeDir facing) {
		super(sprite, x, y);
		this.facing = facing;
		this.thisType = ActorType.Spikes;
		this.harmful = true;
		
		if (facing == SpikeDir.Top) {
			
		} else if (facing == SpikeDir.Bottom) {
			
		} else if (facing == SpikeDir.Left) {
			
		} else if (facing == SpikeDir.Right) {
			
		}
			
	}
	
}
