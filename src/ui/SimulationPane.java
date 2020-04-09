package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import simulator.netobjects.NetObject;
import tools.ObjectBoundingBox;
import tools.QuadTree;

public class SimulationPane extends JPanel {
	private int x, y, width, height;
	private ArrayList<ObjectBoundingBox> objectsRef; 
	
	public SimulationPane(ArrayList<ObjectBoundingBox> objects) {
		 //setLayout(null);
		 objectsRef = objects;
		 x = 50;
		 y = 50;
		 width = 350;
		 height = 350;
		 
		 JButton button = new MyButton("test", (e) -> { 
			 repaint(); 
		 });
		 JButton button2 = new MyButton("do thing", (e) -> {
			 objectsRef.clear();
			 double[] pos = {0.0f, 0.0f};
			 double[] bounds = {5.0f, 5.0f};
			 for(int i = 0; i < 5; i++) {
				 pos[0] = 50 + Math.random() * width;
				 pos[1] = 50 + Math.random() * height;
				 objectsRef.add(new ObjectBoundingBox(pos, bounds));
				 System.out.println("\n");
			 }
			 //objectsRef.add(new ObjectBoundingBox(50, 50, 5, 5));
			 //objectsRef.add(new ObjectBoundingBox(45, 45, 5, 5));
			 //objectsRef.add(new ObjectBoundingBox(90, 90, 5, 5));
			 //objectsRef.add(new ObjectBoundingBox(180, 180, 5, 5));
			 //objectsRef.add(new ObjectBoundingBox(40, 70, 5, 5));
			 //objectsRef.add(new ObjectBoundingBox(70, 10, 5, 5));
			 repaint();
		 });
		 add(button);
		 add(button2);

	}
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		drawQuad(g);
	}
	
	public void drawQuad(Graphics g) {
		//g.setColor(new Color((int) Math.floor(Math.random() * (255 * 255 * 255))));
		//g.drawRect(x,  y, width, height);
		
		/*objectsRef.forEach((e) -> {
			g.setColor(new Color((int) Math.floor(Math.random() * (255 * 255 * 255))));
			g.fillRect(x + (int) e.x, y + (int) e.y, (int) e.width, (int) e.height);
		});*/
		
		new QuadTree(x, y, width, height, objectsRef, g);
	}
}