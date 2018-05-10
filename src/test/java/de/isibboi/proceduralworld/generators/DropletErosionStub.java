package de.isibboi.proceduralworld.generators;

import java.util.Random;

import de.isibboi.proceduralworld.world.WorldLayer;

public class DropletErosionStub extends DropletErosion {
	public DropletErosionStub() {
		super(0);
	}

	@Override
	public void erode(Random r, WorldLayer worldLayer) {
		// Disabled
	}
}
