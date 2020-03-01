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


public class Form extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int width = 500, height = 500;
	private final static DefaultListModel<String> clientsListModel = new DefaultListModel<>();
	private JPanel serverPanel, databasePanel, simulatorPanel;
	
	public Form() {
		//setLayout(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(width, height);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dimension.width - width)/2, (dimension.height - height)/2);
		setFocusable(true);
		
		createElements();
		
		//addClientToList("Test");
		//addClientToList("Test2");
		
		
	}
	
	public static void addClientToList(String t) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				clientsListModel.addElement(t);
				
			}
		});
	}
	private void createElements() {
		makeTabs();		
		serverPanel.add(makeClientList());
	}
	public void display() {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setVisible(true);
			}
		});
	}
	
	private void makeTabs() {
		JTabbedPane tabs = new JTabbedPane();
		
		serverPanel = new JPanel();
		serverPanel.setLayout(null);
		
		databasePanel = new JPanel();
		simulatorPanel = new JPanel();
		
		tabs.addTab("Server", serverPanel);
		tabs.addTab("Database", databasePanel);
		tabs.addTab("Simulation", simulatorPanel);
		add(tabs);
		//return tabs;
	}
	
	private JScrollPane makeClientList() {
		
		JLabel label = new JLabel("Connected Clients");
		label.setBounds(new Rectangle(new Point(5, 5), label.getPreferredSize()));
		serverPanel.add(label);
		
		JList<String> list = new JList<>(clientsListModel);
		JScrollPane pane = new JScrollPane(list);
		pane.setBounds(5, 25, 150, 50);
		return pane;
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
