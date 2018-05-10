package de.isibboi.proceduralworld.generators;

import java.util.Random;
import java.util.function.Function;

import com.flowpowered.noise.module.modifier.ScaleBias;
import com.flowpowered.noise.module.source.Perlin;

import de.isibboi.proceduralworld.geom.Point;
import de.isibboi.proceduralworld.world.WorldLayer;

public class WorldGenerator {
	public void generateHeightMap(final WorldLayer heightMap, final Random r) {
		final Perlin noise = new Perlin();
		noise.setFrequency(0.01);
		noise.setOctaveCount(2);
		noise.setLacunarity(2);
		noise.setPersistence(0.5);
		noise.setSeed(r.nextInt());
		ScaleBias scaleBias = new ScaleBias();
		scaleBias.setSourceModule(0, noise);
		scaleBias.setBias(0);
		scaleBias.setScale(0.5);
		heightMap.fill(new Function<Point, Double>() {
			@Override
			public Double apply(Point p) {
				return scaleBias.getValue(p.getX(), p.getY(), 0);
			}
		});
	}
}
