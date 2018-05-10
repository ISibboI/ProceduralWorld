package de.isibboi.proceduralworld.world;

import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import de.isibboi.proceduralworld.geom.Point;
import de.isibboi.proceduralworld.geom.Size;
import de.isibboi.proceduralworld.geom.Vector;

public class WorldLayer implements Iterable<WorldLayer.Value> {
	public class Value {
		private final Point location;
		private final double value;
		
		public Value(Point location, double value) {
			this.location = location;
			this.value = value;
		}
		
		public Point getLocation() {
			return location;
		}
		
		public double getValue() {
			return value;
		}
	}

	private final Size size;
	private final double[][] values;

	public WorldLayer(Size size) {
		this.size = size;
		values = new double[size.getWidth()][size.getHeight()];
	}

	public WorldLayer(WorldLayer other) {
		this(other.size);
		fill(new Function<Point, Double>() {
			@Override
			public Double apply(Point p) {
				return other.getValue(p);
			}
		});
	}

	public double getValue(Point p) {
		return getValue(p.getX(), p.getY());
	}
	
	public double getValue(int x, int y) {
		return values[x][y];
	}
	
	public double getValueClamped(Point p) {
		return getValue(p.clamp(size));
	}

	public void setValue(Point p, double d) {
		values[p.getX()][p.getY()] = d;
	}

	public void addValue(Point p, double d) {
		values[p.getX()][p.getY()] += d;
	}
	
	public void addValueClamped(Point p, double d) {
		addValue(p.clamp(size), d);
	}

	public Size getSize() {
		return size;
	}

	public void fill(Function<Point, Double> filler) {
		for (int x = 0; x < getSize().getWidth(); x++) {
			for (int y = 0; y < getSize().getHeight(); y++) {
				Point p = new Point(x, y);
				double value = filler.apply(p);
				if (value < -1 || value > 1) {
//					throw new IllegalArgumentException("value is out of bounds: " + value);
				}
				setValue(p, value);
			}
		}
	}
	
	public Vector getDownwardsGradient(Point p) {
		Vector gradient = new Vector();
		double value = getValue(p);
		for (Point neighbor : p.getNeighbors()) {
			Vector n = neighbor.subtract(p).toVector();
			n = n.multiply(value - getValueClamped(neighbor));
			gradient = gradient.add(n);
		}
		return gradient.multiply(0.125);
	}
	
	public WorldLayer getDownwardsGradientLayerX() {
		WorldLayer result = new WorldLayer(size);
		result.fill(new Function<Point, Double>() {
			@Override
			public Double apply(Point p) {
				return getDownwardsGradient(p).getX();
			}
		});
		return result;
	}
	
	public WorldLayer getDownwardsGradientLayerY() {
		WorldLayer result = new WorldLayer(size);
		result.fill(new Function<Point, Double>() {
			@Override
			public Double apply(Point p) {
				return getDownwardsGradient(p).getY();
			}
		});
		return result;
	}

	@Override
	public Iterator<Value> iterator() {
		return new Iterator<Value>() {
			private Point location = new Point();
			
			@Override
			public boolean hasNext() {
				return location.getY() < getSize().getHeight();
			}

			@Override
			public Value next() {
				Value result = new Value(location, getValue(location));
				location = new Point(location.getX() + 1, location.getY());
				if (location.getX() >= getSize().getWidth()) {
					location = new Point(0, location.getY() + 1);
				}
				return result;
			}
		};
	}
	
	public Stream<Value> stream() {
		Iterable<Value> iterable = () -> iterator();
		return StreamSupport.stream(iterable.spliterator(), false);
	}
	
	public double calculateMin() {
		return stream().map((a) -> a.getValue()).reduce(Double.POSITIVE_INFINITY, Math::min);
	}
	
	public double calculateMax() {
		return stream().map((a) -> a.getValue()).reduce(Double.NEGATIVE_INFINITY, Math::max);
	}
	
	@Override
	public String toString() {
		final double min = calculateMin();
		final double max = calculateMax();
		
		StringBuilder s = new StringBuilder();
		s.append(min).append("/").append(max).append("\n");
		for (int y = 0; y < getSize().getHeight(); y++) {
			for (int x = 0; x < getSize().getWidth(); x++) {
				double value = getValue(x, y);
				value += min;
				value *= 10 / (max - min);
				if (value >= 10) {
					value = 9;
				} else if (value < 0) {
					value = 0;
				}
				s.append((int) value);
				if (x < getSize().getWidth() - 1) {
					s.append(' ');
				}
			}
			if (y < getSize().getHeight() - 1) {
				s.append('\n');
			}
		}
		return s.toString();
	}
}