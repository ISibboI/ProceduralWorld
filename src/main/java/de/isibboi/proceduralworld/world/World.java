package de.isibboi.proceduralworld.world;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import de.isibboi.proceduralworld.generators.WorldGenerator;
import de.isibboi.proceduralworld.geom.Point;
import de.isibboi.proceduralworld.geom.Size;

public class World {
	private final Size size;
	private final WorldLayer heightMap;
	private final WorldLayer waterLayer;
	private final Set<Point> waterSprings;
	private final ObjectLayer<Biome> biomeLayer;
	
	public World(Size size) {
		this.size = size;
		heightMap = new WorldLayer(size);
		waterLayer = new WorldLayer(size);
		waterSprings = new HashSet<>();
		biomeLayer = new ObjectLayer<>(size);
	}
	
	public World(World other) {
		size = other.size;
		heightMap = new WorldLayer(other.heightMap);
		waterLayer = new WorldLayer(other.waterLayer);
		waterSprings = new HashSet<>(other.waterSprings);
		biomeLayer = new ObjectLayer<>(other.biomeLayer);
	}
	
	public Size getSize() {
		return size;
	}
	
	public WorldLayer getHeightMap() {
		return heightMap;
	}
	
	public WorldLayer getWaterLayer() {
		return waterLayer;
	}
	
	public Set<Point> getWaterSprings() {
		return waterSprings;
	}
	
	public ObjectLayer<Biome> getBiomeLayer() {
		return biomeLayer;
	}
}
