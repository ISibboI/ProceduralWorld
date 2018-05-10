package de.isibboi.proceduralworld.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.isibboi.proceduralworld.generators.AgeGenerator;
import de.isibboi.proceduralworld.generators.WaterGenerator;

public class Timeline {
	private final List<World> stages = new ArrayList<>();
	private final AgeGenerator ageGenerator;
	private final WaterGenerator waterGenerator;

	public Timeline(World initial, AgeGenerator ageGenerator, WaterGenerator waterGenerator) {
		stages.add(initial);
		this.ageGenerator = ageGenerator;
		this.waterGenerator = waterGenerator;
	}

	private void generateAge(Random random) {
		if (stages.size() < 100) {
			stages.add(ageGenerator.generateAge(random, getCurrentWorld()));
		} else if (stages.size() < 200) {
			stages.add(waterGenerator.generateWater(random, getCurrentWorld()));
		}
	}

	public World getWorld(int age, Random random) {
		if (age < 0) {
			throw new IllegalArgumentException("Age must not be negative");
		}

		while (age > getCurrentAge()) {
			generateAge(random);
		}

		return stages.get(age);
	}

	public World getCurrentWorld() {
		return stages.get(stages.size() - 1);
	}

	public int getCurrentAge() {
		return stages.size() - 1;
	}
}
