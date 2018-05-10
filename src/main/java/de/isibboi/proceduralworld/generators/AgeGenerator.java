package de.isibboi.proceduralworld.generators;

import java.util.Random;

import de.isibboi.proceduralworld.geom.Point;
import de.isibboi.proceduralworld.geom.Vector;
import de.isibboi.proceduralworld.world.World;
import de.isibboi.proceduralworld.world.WorldLayer;

public class AgeGenerator {
	private final double erosionStrength;
	private final DropletErosion dropletErosion;
	
	public AgeGenerator(double erosionStrength, DropletErosion dropletErosion) {
		this.erosionStrength = erosionStrength;
		this.dropletErosion = dropletErosion;
	}

	public World generateAge(Random r, World world) {
		World next = new World(world);
		simulateMacroErosion(r, world, next);
		dropletErosion.erode(r, next.getHeightMap());
		return next;
	}

	private void simulateMacroErosion(Random r, World world, World next) {
		for (WorldLayer.Value value : world.getHeightMap()) {
			Vector gradient = world.getHeightMap().getDownwardsGradient(value.getLocation());
			Point applicationX = value.getLocation().add(new Point(gradient.getXSign(), 0));
			Point applicationY = value.getLocation().add(new Point(0, gradient.getYSign()));
			double dx = world.getHeightMap().getValue(value.getLocation())
					- world.getHeightMap().getValueClamped(applicationX);
			double dy = world.getHeightMap().getValue(value.getLocation())
					- world.getHeightMap().getValueClamped(applicationY);
			dx *= erosionStrength;
			dy *= erosionStrength;
			next.getHeightMap().addValue(value.getLocation(), -(dx + dy));
			next.getHeightMap().addValueClamped(applicationX, dx);
			next.getHeightMap().addValueClamped(applicationY, dy);
		}
	}
}
