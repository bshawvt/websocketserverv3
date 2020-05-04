package ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JPanel;

import Models.CharacterModel;
import server.ServerThreadMessage;
import shared.BoundingBox;
import shared.SOQuadTree;
import shared.OldQuadTree;
import shared.QuadTree;
import simulator.SimulatorThreadMessage;
import simulator.sceneobjects.SceneObject;
import simulator.sceneobjects.ScenePlayer;
import threads.Threads;

public class SimulationPane extends JPanel {
	private int x, y, width, height;
	private ArrayList<SceneObject> objectsRef; 
	
	public SimulationPane(ArrayList<SceneObject> objects) {
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
		Graphics2D g2 = (Graphics2D) g.create();
		AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f);
		g2.setComposite(alpha);
		
		int scale = 15;
		objectsRef.forEach((e) -> {
			g2.setColor(new Color((int) Math.floor(Math.random() * (255 * 255 * 255))));
			g2.fillRect(x + (int) e.x * scale, y + (int) e.y * scale, (int) e.bb.xscale * scale, (int) e.bb.yscale * scale);
			System.out.println(e.bb.xscale + ", " + e.bb.yscale);
		});
		g2.dispose();
		//JPanel tree = new JPanel();
		
		SOQuadTree tree = new SOQuadTree(6, x, y, 0, 0, width * scale, height * scale, objectsRef, g);
		double[] p = { 16, 16, 0 };
		double[] bd = { 5, 5, 0 };

		HashSet<SceneObject> b = tree.get(new BoundingBox(p, bd, 32.0f), g);
		System.out.println(objectsRef.size());
		//if (b.size() > 0)
			//System.out.println(b.toArray()[0]);
		//add(tree);
	}
}