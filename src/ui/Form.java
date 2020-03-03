package ui;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

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


public class Form extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int width = 500, height = 500;
	private final static DefaultListModel<String> clientsListModel = new DefaultListModel<>();
	private static boolean active = false;
	//private JPanel serverPanel, databasePanel, simulatorPanel;
	
	public Form() {
		//setLayout(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(width, height);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dimension.width - width)/2, (dimension.height - height)/2);
		setFocusable(true);
		
		JTabbedPane tabs = new JTabbedPane();
		
		tabs.addTab("Server", new ServerPane(clientsListModel));
		tabs.addTab("Database", new DatabasePane());
		tabs.addTab("Simulation", new SimulationPane());
		
		add(tabs);		
		
	}
	public static void addClientToList(String t) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//if (Form.isInUse())
				clientsListModel.addElement(t);
			}
		});
	}
	public static void removeClientToList(String t) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//if (Form.isInUse())
				clientsListModel.remove(clientsListModel.indexOf(t));
				//clientsListModel.addElement(t);
			}
		});
	}
	public static boolean isInUse() {
		return active;
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

	/*
	public static void main(String[] args) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				(new Form()).display();
				
			}
		});
	}
	*/
	
	
	
	
}
