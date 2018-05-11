package de.isibboi.proceduralworld.render;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import de.isibboi.proceduralworld.geom.Point;
import de.isibboi.proceduralworld.geom.Vector;
import de.isibboi.proceduralworld.world.World;

@SuppressWarnings("serial")
public class WorldPanel extends JPanel implements MouseWheelListener, MouseMotionListener, MouseListener {
	private World world;
	private BufferedImage paintedWorld;

	private static final int DRAG_BUTTON = MouseEvent.BUTTON1;
	private Vector viewpoint;
	private int zoom;
	private double[] zoomLevels;
	private Point dragOrigin;

	public WorldPanel(World world) {
		this.world = world;
		setPreferredSize(world.getSize().toDimension());
		addMouseWheelListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);

		viewpoint = new Vector();
		zoom = 0;
		zoomLevels = new double[20];
		for (int i = 0; i < zoomLevels.length; i++) {
			zoomLevels[i] = Math.pow(1.1, i);
		}
	}

	private void paintWorld() {
		paintedWorld = new BufferedImage(world.getSize().getWidth(), world.getSize().getHeight(),
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < paintedWorld.getWidth(); x++) {
			for (int y = 0; y < paintedWorld.getHeight(); y++) {
				double v = world.getHeightMap().getValue(x, y);
				v += 1;
				v *= 127.75;

				int rgb = (int) v;
				if (rgb < 0 || rgb > 255) {
					System.out.println("[" + x + ", " + y + "]: " + world.getHeightMap().getValue(x, y));
				}
				rgb += (rgb << 8) + (rgb << 16);
				paintedWorld.setRGB(x, y, rgb);
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (paintedWorld == null) {
			paintWorld();
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.setBackground(Color.BLACK);
		g2d.clearRect(0, 0, (int) g2d.getClipBounds().getWidth() + 1, (int) g2d.getClipBounds().getHeight() + 1);
		final double scale = zoomLevels[zoom];
		AffineTransform imageTransform = AffineTransform.getScaleInstance(1, 1);
		imageTransform.scale(scale, scale);
		imageTransform.translate(-viewpoint.getX(), -viewpoint.getY());
		g2d.drawImage(paintedWorld, imageTransform, null);
	}

	public void setWorld(World world) {
		this.world = world;
		paintedWorld = null;
		repaint();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		final double originalZoom = zoomLevels[zoom];
		zoom = Math.max(0, Math.min(zoomLevels.length - 1, zoom - e.getWheelRotation()));
		final double newZoom = zoomLevels[zoom];
		final double deltaZoom = newZoom / originalZoom;
		final Vector originalZoompoint = new Vector(e.getPoint()).add(viewpoint.multiply(originalZoom)).divide(originalZoom);
		viewpoint = viewpoint.subtract(originalZoompoint);
		viewpoint = viewpoint.divide(deltaZoom);
		viewpoint = viewpoint.add(originalZoompoint);
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (dragOrigin != null) {
			Point dragTarget = new Point(e.getPoint());
			Vector deltaDrag = dragTarget.subtract(dragOrigin).toVector();
			deltaDrag = deltaDrag.divide(zoomLevels[zoom]);
			viewpoint = viewpoint.subtract(deltaDrag);
			dragOrigin = dragTarget;
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == DRAG_BUTTON) {
			dragOrigin = new Point(e.getPoint());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == DRAG_BUTTON) {
			dragOrigin = null;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
