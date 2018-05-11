package de.isibboi.proceduralworld.generators;

import java.util.Random;

import de.isibboi.proceduralworld.geom.Point;
import de.isibboi.proceduralworld.world.Biome;
import de.isibboi.proceduralworld.world.World;

public class WaterGenerator {
	public World generateWater(Random random, World currentWorld) {
		World next = new World(currentWorld);
		generateSpring(random, next);
		simulateWater(random, next);
		return next;
	}

	private void generateSpring(Random random, World world) {
		if (random.nextDouble() < 1.0 / world.getWaterSprings().size()) {
			world.getWaterSprings().add(new Point(world.getSize(), random));
		}
	}

	private void simulateWater(Random random, World next) {
		for (Point spring : next.getWaterSprings()) {
			addWater(random, next, spring);
		}
	}

	private void addWater(Random random, World world, final Point spring) {
		Point currentLocation = spring;
		double currentHeight = world.getHeightMap().getValue(currentLocation);
		int age = 0;
		boolean runRiver = true;
		while (runRiver) {
			final Point direction = world.getHeightMap().getDownwardsGradient(currentLocation).pickDirection(random);
			final Point nextLocation = currentLocation.add(direction);
			final double nextHeight = world.getHeightMap().getValue(nextLocation);
			for (Point neighbor : currentLocation.getNeighbors()) {
				if (!neighbor.equals(nextLocation)) {
					currentHeight = Math.min(currentHeight, world.getHeightMap().getValue(neighbor) - 1e-2);
				}
			}
			world.getHeightMap().setValue(currentLocation, currentHeight);

			if (nextHeight > currentHeight || world.getBiomeLayer().getValue(nextLocation) == Biome.LAKE) {
				runRiver = false;;
			}
			currentLocation = nextLocation;
			currentHeight = nextHeight;
		}
	}
}