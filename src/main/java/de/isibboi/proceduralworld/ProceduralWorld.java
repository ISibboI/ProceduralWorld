package de.isibboi.proceduralworld;

import java.util.Random;

import de.isibboi.proceduralworld.generators.WorldGenerator;
import de.isibboi.proceduralworld.generators.AgeGenerator;
import de.isibboi.proceduralworld.generators.DropletErosion;
import de.isibboi.proceduralworld.generators.WaterGenerator;
import de.isibboi.proceduralworld.geom.Size;
import de.isibboi.proceduralworld.render.TimelineFrame;
import de.isibboi.proceduralworld.world.Timeline;
import de.isibboi.proceduralworld.world.World;

public class ProceduralWorld {
	public static void main(String[] args) {
		Random random = new Random();
		World w = new WorldGenerator().generateWorld(new Size(1000, 1000), random);
		Timeline timeline = new Timeline(w, new AgeGenerator(0.05, new DropletErosion(2000)), new WaterGenerator());
		
		TimelineFrame timelineFrame = new TimelineFrame(timeline, random);
		timelineFrame.setVisible(true);
	}
}
