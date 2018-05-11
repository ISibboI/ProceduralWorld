package de.isibboi.proceduralworld.world;

import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import de.isibboi.proceduralworld.geom.Point;
import de.isibboi.proceduralworld.geom.Size;

public class ObjectLayer<T> implements Iterable<ObjectLayer<T>.Value> {
	public class Value {
		private final Point location;
		private final T value;
		
		public Value(Point location, T value) {
			this.location = location;
			this.value = value;
		}
		
		public Point getLocation() {
			return location;
		}
		
		public T getValue() {
			return value;
		}
	}

	private final Size size;
	private final T[][] values;

	@SuppressWarnings("unchecked")
	public ObjectLayer(Size size) {
		this.size = size;
		values = (T[][]) new Object[size.getWidth()][size.getHeight()];
	}

	public ObjectLayer(ObjectLayer<? extends T> other) {
		this(other.size);
		fill(new Function<Point, T>() {
			@Override
			public T apply(Point p) {
				return other.getValue(p);
			}
		});
	}

	public T getValue(Point p) {
		return getValue(p.getX(), p.getY());
	}
	
	public T getValue(int x, int y) {
		return values[x][y];
	}
	
	public T getValueClamped(Point p) {
		return getValue(p.clamp(size));
	}

	public void setValue(Point p, T d) {
		values[p.getX()][p.getY()] = d;
	}

	public Size getSize() {
		return size;
	}

	public void fill(Function<Point, T> filler) {
		for (int x = 0; x < getSize().getWidth(); x++) {
			for (int y = 0; y < getSize().getHeight(); y++) {
				Point p = new Point(x, y);
				T value = filler.apply(p);
				setValue(p, value);
			}
		}
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
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int y = 0; y < getSize().getHeight(); y++) {
			for (int x = 0; x < getSize().getWidth(); x++) {
				T value = getValue(x, y);
				s.append(value);
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
