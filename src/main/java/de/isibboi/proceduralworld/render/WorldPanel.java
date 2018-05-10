package de.isibboi.proceduralworld.render;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import de.isibboi.proceduralworld.geom.Point;
import de.isibboi.proceduralworld.world.World;

@SuppressWarnings("serial")
public class WorldPanel extends JPanel {
	private World world;

	public WorldPanel(World world) {
		this.world = world;
		setPreferredSize(world.getSize().toDimension());
	}

	@Override
	protected void paintComponent(Graphics g) {
		BufferedImage img = new BufferedImage(world.getSize().getWidth(), world.getSize().getHeight(),
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				double v = world.getHeightMap().getValue(x, y);
				v += 1;
				v *= 127.75;
				
				int rgb = (int) v;
				if (rgb < 0 || rgb > 255) {
					System.out.println("[" + x + ", " + y + "]: " + world.getHeightMap().getValue(x, y));
				}
				rgb += (rgb << 8) + (rgb << 16);
				img.setRGB(x, y, rgb);
			}
		}
		g.drawImage(img, 0, 0, null);
	}

	public void setWorld(World world) {
		this.world = world;
		repaint();
	}
}
