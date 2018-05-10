package de.isibboi.proceduralworld.geom;

import java.awt.Dimension;

public class Size {
	private final int width, height;
	
	public Size(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public Size() {
		this(0, 0);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Dimension toDimension() {
		return new Dimension(width, height);
	}

	public boolean contains(Point p) {
		return p.getX() >= 0 && p.getY() >= 0 && p.getX() < width && p.getY() < height;
	}
}
