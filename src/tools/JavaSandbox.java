package tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import shared.ObjectList;
import ui.Form;

public class JavaSandbox extends JFrame {
	JPanel panel = null;
	public JavaSandbox() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(250, 250);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dimension.width - 250)/2, (dimension.height - 250) / 2);
		setFocusable(true);
		setAlwaysOnTop(true);
		setVisible(true);
		panel = new JPanel() {
			@Override
			public void paintComponents(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponents(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setColor(new Color((float) Math.random(), (float) Math.random(), (float) Math.random()));
				g2.fillRect(0,  0,  250,  250);
				System.out.println("??");
			}
		};
		add(panel);
		panel.repaint();
		repaint();
	}
	public void render() {
		panel.repaint();
	}
	
	/**
	 * @param args
	 * @throws InterruptedException
	 */
	static public void main(String[] args) throws InterruptedException {
		JavaSandbox n = new JavaSandbox();
		
		
		boolean running = true;
		double dt = 0;
		double step = 1000/30;
		
		
		while(running) {
			Thread.sleep(1);
			double now = System.nanoTime()/1000000;
			while (dt < now) {
				dt += step;
				// todo update
			}
			//todo render
			n.render();
		}
		
		
	}
}
