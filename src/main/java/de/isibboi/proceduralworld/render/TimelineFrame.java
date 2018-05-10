package de.isibboi.proceduralworld.render;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import de.isibboi.proceduralworld.world.Timeline;

@SuppressWarnings("serial")
public class TimelineFrame extends JFrame implements KeyListener {
	private final Timeline timeline;
	private final Random random;
	
	private final WorldPanel worldPanel;
	private final JLabel ageLabel;
	
	private int currentAge;

	public TimelineFrame(Timeline timeline, Random random) {
		super("TimelineFrame");
		this.timeline = timeline;
		this.random = random;
		
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		worldPanel = new WorldPanel(timeline.getCurrentWorld());
		getContentPane().add(worldPanel, gbc);
		
		currentAge = timeline.getCurrentAge();
		ageLabel = new JLabel();
		gbc.gridy = 1;
		getContentPane().add(ageLabel, gbc);
		
		updateWorld();
		addKeyListener(this);
		pack();
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	public void nextAge() {
		currentAge++;
		updateWorld();
	}
	
	public void previousAge() {
		currentAge = Math.max(currentAge - 1, 0);
		updateWorld();
	}
	
	public void nextPhase() {
		currentAge += 100;
		currentAge = (currentAge / 100) * 100;
		updateWorld();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			nextAge();
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			previousAge();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == ' ') {
			nextAge();
		} else if (e.getKeyChar() == 'p') {
			nextPhase();
		}
	}

	private void updateWorld() {
		worldPanel.setWorld(timeline.getWorld(currentAge, random));
		ageLabel.setText("Age: " + currentAge);
	}
}
