package de.isibboi.proceduralworld.world;

import java.util.Random;

import de.isibboi.proceduralworld.generators.WorldGenerator;
import de.isibboi.proceduralworld.geom.Size;

public class World {
	private final Size size;
	private final WorldLayer heightMap;
	private final WorldGenerator worldGenerator;
	
	public World(Size size, WorldGenerator worldGenerator) {
		this.size = size;
		heightMap = new WorldLayer(size);
		this.worldGenerator = worldGenerator;
	}
	
	public World(World other) {
		size = other.size;
		heightMap = new WorldLayer(other.heightMap);
		worldGenerator = other.worldGenerator;
	}
	
	public void generate(Random r) {
		worldGenerator.generateHeightMap(heightMap, r);
	}
	
	public Size getSize() {
		return size;
	}
	
	public WorldLayer getHeightMap() {
		return heightMap;
	}

	public World emptyCopy() {
		return new World(size, worldGenerator);
	}
}
