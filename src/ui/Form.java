package ui;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import server.Client;
import simulator.netobjects.NetObject;


public class Form extends JFrame {

	private final int width = 500, height = 500;
	private final static DefaultListModel<String> clientsListModel = new DefaultListModel<>();
	private static boolean active = false;
	private static final ArrayList<ObjectBoundingBox> quadPoints = new ArrayList<>();
	
	public Form() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(width, height);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dimension.width - width)/2, (dimension.height - height)/2);
		setFocusable(true);
		setAlwaysOnTop(true);
		
		JTabbedPane tabs = new JTabbedPane();
		
		tabs.addTab("Server", new ServerPane(clientsListModel));
		tabs.addTab("Database", new DatabasePane());
		tabs.addTab("Simulation", new SimulationPane(quadPoints));
		tabs.setSelectedIndex(2);
				
		add(tabs);
	}
	
	public static void UpdateClientList(ArrayList<Client> list) {
		
		final ArrayList<String> copy = new ArrayList<>();
		list.forEach((e) -> {
			if (e.isReady())
				copy.add(e.getUserAccount().getUsername());
		});
		
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				clientsListModel.clear();
				copy.forEach((e) -> {
					clientsListModel.addElement(e);
				});
			}
		});
	}
	public static boolean IsInUse() {
		return active;
	}	
	public static void UpdateQuadPoints(ArrayList<NetObject> objs) {

		final ArrayList<ObjectBoundingBox> tmpObjs = new ArrayList<>();
		objs.forEach((e) -> {
			tmpObjs.add(new ObjectBoundingBox(e.position, e.bounds));
		});
		
		SwingUtilities.invokeLater( new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				tmpObjs.forEach((e) -> {
					quadPoints.add(e);
				});
			}
			
		});
	}
	
	public void display(boolean v) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setVisible(v);
				active = v;
			}
		});
	}
}
