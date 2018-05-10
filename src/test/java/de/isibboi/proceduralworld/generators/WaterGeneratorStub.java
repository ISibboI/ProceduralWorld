package de.isibboi.proceduralworld.generators;

import java.util.Random;

import de.isibboi.proceduralworld.world.World;

public class WaterGeneratorStub extends WaterGenerator {
	@Override
	public World generateWater(Random random, World currentWorld) {
		// Disabled
		return currentWorld;
	}
}
