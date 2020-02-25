package main;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class UI {

	public UI() {
		
		int x = 25;
		int y = 25;
		int width = 300;
		int height = 300;
		JFrame frame = new JFrame();
		
		JPanel panel = new JPanel() 
		{
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				Color c = new Color(255, 0, 0);
				
				g.setColor(c);
				//g.drawLine(10,  10,  200,  200);
				g.drawRect(x, y, width, height);
				g.setColor(new Color(0, 255, 0));
				g.drawString("Hello world", 1, 25);
			}
			
			
		};
		
		frame.setSize(400, 400);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
		frame.add(panel);
		
		//Graphics g = null;
		//g.drawLine(10,  10,  200,  200);
		//frame.paint();
	}

	public static void main(String[] args) {
		new UI();
	}

}


