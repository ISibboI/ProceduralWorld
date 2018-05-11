package de.isibboi.proceduralworld.geom;

public class Vector {
	private final double x, y;

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector() {
		this(0, 0);
	}

	public Vector(Size size) {
		x = size.getWidth();
		y = size.getHeight();
	}

	public Vector(java.awt.Point p) {
		x = p.x;
		y = p.y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getXSign() {
		return (int) Math.signum(x);
	}

	public int getYSign() {
		return (int) Math.signum(y);
	}

	public Vector add(double d) {
		return new Vector(x + d, y + d);
	}

	public Vector add(Vector v) {
		return new Vector(x + v.x, y + v.y);
	}

	public Vector add(Point p) {
		return new Vector(x + p.getX(), y + p.getY());
	}

	public Vector subtract(Vector v) {
		return new Vector(x - v.x, y - v.y);
	}

	public Vector multiply(double d) {
		return new Vector(x * d, y * d);
	}

	public Vector divide(double d) {
		return new Vector(x / d, y / d);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public double sumNorm() {
		return Math.abs(x) + Math.abs(y);
	}

	public double l2Norm() {
		return Math.sqrt(x * x + y * y);
	}
}
