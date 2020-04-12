package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JPanel;

import Models.CharacterModel;
import server.ServerThreadMessage;
import simulator.SimulatorThreadMessage;
import simulator.netobjects.NetObject;
import simulator.netobjects.Player;
import threads.Threads;
import tools.NOQuadTree;
import tools.ObjectBoundingBox;
import tools.OldQuadTree;
import tools.QuadTree;

public class SimulationPane extends JPanel {
	private int x, y, width, height;
	private ArrayList<NetObject> objectsRef; 
	
	public SimulationPane(ArrayList<NetObject> objects) {
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
			 Threads.getSimulatorQueue().offer(new SimulatorThreadMessage(Threads.Main, 
					 SimulatorThreadMessage.Type.None, 
					 "get_view 0"));
			 /*objectsRef.clear();
			 double[] pos = {0.0f, 0.0f};
			 double[] bounds = {10.0f, 10.0f};
			 for(int i = 0; i < 2; i++) {
				 pos[0] = Math.random() * width;
				 pos[1] = Math.random() * height;
				 double size = 5 + Math.random() * 25;
				 bounds[0] = size;
				 bounds[1] = size;
				 Player c = new Player(new CharacterModel(0));
				 c.boundingBox.set(pos, bounds);
				 objectsRef.add(c);
				 System.out.println("\n");
			 }*/
			 /*objectsRef.add(new ObjectBoundingBox(59, 59, 10, 10));
			 objectsRef.add(new ObjectBoundingBox(10, 59, 10, 10));
			 objectsRef.add(new ObjectBoundingBox(40, 59, 10, 10));*/
			 /*objectsRef.add(new ObjectBoundingBox(50, 25, 10, 10));
			 objectsRef.add(new ObjectBoundingBox(25, 10, 10, 10));
			 objectsRef.add(new ObjectBoundingBox(5, 10, 10, 10));
			 objectsRef.add(new ObjectBoundingBox(115, 115, 10, 10));
			 objectsRef.add(new ObjectBoundingBox(79, 79, 10, 10));*/
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
		
		objectsRef.forEach((e) -> {
			g.setColor(new Color((int) Math.floor(Math.random() * (255 * 255 * 255))));
			g.fillRect(x + (int) e.position[0], y + (int) e.position[1], (int) e.bounds[0], (int) e.bounds[1]);
		});
		//JPanel tree = new JPanel();
		
		NOQuadTree tree = new NOQuadTree(7, x, y, 0, 0, width, height, objectsRef, g);
		double[] p = { 16, 16 };
		double[] bd = { 5, 5 };

		HashSet<NetObject> b = tree.get(new ObjectBoundingBox(p, bd, 32.0f), g);
		System.out.println(objectsRef.size());
		if (b.size() > 0)
			System.out.println(b.toArray()[0]);
		//add(tree);
	}
}