package de.isibboi.proceduralworld.generators;

import java.util.Random;

import de.isibboi.proceduralworld.geom.Point;
import de.isibboi.proceduralworld.geom.Vector;
import de.isibboi.proceduralworld.world.WorldLayer;

public class DropletErosion {
	private static class Droplet {
		Point location;
		Vector momentum;
		double water;
		double soil;
		
		Droplet(Point location) {
			this.location = location;
			momentum = new Vector();
			water = 1;
			soil = 0;
		}
	}
	
	private final int rounds;
	
	public DropletErosion(int rounds) {
		this.rounds = rounds;
	}

	public void erode(Random random, WorldLayer worldLayer) {
		for (int i = 0; i < rounds; i++) {
			doErosionRound(random, worldLayer);
		}
	}
	
	public void doErosionRound(Random random, WorldLayer worldLayer) {
		Droplet droplet = new Droplet(new Point(worldLayer.getSize(), random));
		
		while (droplet.water > 0) {
			droplet.water *= 0.99;
			droplet.water = Math.max(0, droplet.water - 1e-3);
			depositUncarryableSoil(droplet, worldLayer);
			final Vector gradient = worldLayer.getDownwardsGradient(droplet.location).add(droplet.momentum);
			final Point direction = gradient.pickDirection(random);
			final Point newLocation = droplet.location.add(direction);
			if (!worldLayer.getSize().contains(newLocation)) {
				break;
			}
			final double maxTerrainTake = worldLayer.getValue(droplet.location) - worldLayer.getValue(newLocation);
			if (maxTerrainTake >= 0) {
				final double maxWaterTake = droplet.water - droplet.soil;
				final double deltaSoil = maxWaterTake * maxTerrainTake / Math.max(1, gradient.l2Norm());
				droplet.soil += deltaSoil;
				worldLayer.addValue(droplet.location, -deltaSoil);
			} else {
				droplet.water = Math.max(0, droplet.water + maxTerrainTake);
				droplet.soil = Math.max(0, droplet.soil + maxTerrainTake);
			}
			droplet.location = newLocation;
			droplet.momentum = droplet.momentum.add(gradient).multiply(maxTerrainTake);
		}
	}
	
	private void depositUncarryableSoil(Droplet droplet, WorldLayer worldLayer) {
		if (droplet.water < droplet.soil) {
			worldLayer.addValue(droplet.location, droplet.soil - droplet.water);
			droplet.soil = droplet.water;
		}
	}
}