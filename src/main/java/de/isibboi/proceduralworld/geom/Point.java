package de.isibboi.proceduralworld.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Point {
	private static final List<Point> neighbors;
	static {
		neighbors = new ArrayList<>(8);
		neighbors.add(new Point(-1, 0));
		neighbors.add(new Point(1, 0));
		neighbors.add(new Point(0, 1));
		neighbors.add(new Point(0, -1));
	}
	
	private final int x, y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point() {
		this(0, 0);
	}

	public Point(Size size, Random r) {
		this(r.nextInt(size.getWidth()), r.nextInt(size.getHeight()));
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Point clamp(Size size) {
		int x = this.x;
		int y = this.y;
		x = x < 0 ? 0 : x;
		y = y < 0 ? 0 : y;
		x = x >= size.getWidth() ? size.getWidth() - 1 : x;
		y = y >= size.getHeight() ? size.getHeight() - 1 : y;
		return new Point(x, y);
	}
	
	public Collection<Point> getNeighbors() {
		Collection<Point> neighbors = new ArrayList<>(8);
		for (Point neighbor : Point.neighbors) {
			neighbors.add(add(neighbor));
		}
		return neighbors;
	}

	public Point add(Point point) {
		return new Point(x + point.x, y + point.y);
	}

	public Point subtract(Point p) {
		return new Point(x - p.x, y - p.y);
	}

	public Vector toVector() {
		return new Vector(x, y);
	}
	
	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}
}
