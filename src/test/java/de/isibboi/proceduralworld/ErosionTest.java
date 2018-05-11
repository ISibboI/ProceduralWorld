package de.isibboi.proceduralworld;

import static org.junit.Assert.*;

import java.util.Random;
import java.util.function.Function;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.isibboi.proceduralworld.generators.AgeGenerator;
import de.isibboi.proceduralworld.generators.DropletErosionStub;
import de.isibboi.proceduralworld.generators.WaterGeneratorStub;
import de.isibboi.proceduralworld.generators.WorldGenerator;
import de.isibboi.proceduralworld.geom.Point;
import de.isibboi.proceduralworld.geom.Size;
import de.isibboi.proceduralworld.world.Timeline;
import de.isibboi.proceduralworld.world.World;
import de.isibboi.proceduralworld.world.WorldLayer;

public class ErosionTest {
	private Random r;

	@Before
	public void before() {
		r = new Random(111L);
	}

	private Timeline getSimpleTimeline(Size size) {
		World world = new World(size, new WorldGenerator());
		world.getHeightMap().fill(new Function<Point, Double>() {
			@Override
			public Double apply(Point p) {
				return (double) p.getY() / (size.getHeight() - 1);
			}
		});
		return new Timeline(world, new AgeGenerator(0.5, new DropletErosionStub()), new WaterGeneratorStub());
	}

	private Timeline getTimeline(Size size) {
		World world = new World(size, new WorldGenerator());
		world.generate(new Random());
		return new Timeline(world, new AgeGenerator(0.25, new DropletErosionStub()), new WaterGeneratorStub());
	}

	@Test
	public void testSimple2x2() {
		Timeline timeline = getSimpleTimeline(new Size(2, 2));

		for (int age = 1; age <= 2; age++) {
			for (WorldLayer.Value value : timeline.getWorld(age, r).getHeightMap()) {
				assertEquals(0.5, value.getValue(), 1e-6);
			}
		}
	}

	@Test
	public void testSimple3x3() {
		Timeline timeline = getSimpleTimeline(new Size(3, 3));

		for (WorldLayer.Value value : timeline.getWorld(1, r).getHeightMap()) {
			assertEquals(value.getLocation().getY() * 0.25 + 0.25, value.getValue(), 1e-6);
		}

		for (WorldLayer.Value value : timeline.getWorld(2, r).getHeightMap()) {
			assertEquals(value.getLocation().getY() * 0.125 + 0.375, value.getValue(), 1e-6);
		}
	}

	@Test
	public void test3x3() {
		Size size = new Size(3, 3);
		Timeline timeline = getTimeline(size);
		final double average = timeline.getCurrentWorld().getHeightMap().stream().map(a -> a.getValue()).reduce(0.,
				Double::sum) / (size.getWidth() * size.getHeight());
		World last = timeline.getWorld(0, r);
		double lastMin = last.getHeightMap().calculateMin();
		double lastMax = last.getHeightMap().calculateMax();
		for (int age = 1; age <= 100; age++) {
			World current = timeline.getWorld(age, r);
			double currentMin = current.getHeightMap().calculateMin();
			double currentMax = current.getHeightMap().calculateMax();
			assertTrue("Min not monotonous increasing", currentMin >= lastMin);
			assertTrue("Max not monotonous decreasing", currentMax <= lastMax);
			last = current;
			lastMin = currentMin;
			lastMax = currentMax;
		}
		for (WorldLayer.Value value : timeline.getWorld(100, r).getHeightMap()) {
			assertEquals(average, value.getValue(), 1e-6);
		}
	}
}