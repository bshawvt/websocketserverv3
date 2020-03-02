package ui;


import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import server.ServerThreadMessage;
import threads.Threads;

public class ServerPane extends JPanel {
	
	private JList<String> list;
	private JButton kick;
	public ServerPane(DefaultListModel<String> model) {
		 setLayout(null);
		 
		 JLabel label = new JLabel("Connected Clients");
		 label.setBounds(new Rectangle( new Point(5, 5), label.getPreferredSize()));
		 
		 kick = new JButton("Kick user");
		 kick.setEnabled(false);
		 kick.setBounds(new Rectangle( new Point(160, 25), kick.getPreferredSize()));
		 kick.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				if (kick.isEnabled())
					Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Main, ServerThreadMessage.Type.None, "kick "+list.getSelectedValue()));
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		 
		 
		 list = new JList<>(model);
		 list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if (!e.getValueIsAdjusting()) {
					String selected = list.getSelectedValue();
					if (selected!=null && !selected.equals("null")) {
						kick.setEnabled(true);
					}
					else {
						kick.setEnabled(false);
					}
					kick.setText("Kick " + list.getSelectedValue());
					kick.setBounds(new Rectangle( new Point(160, 25), kick.getPreferredSize()));
				}
			}
			 
		 });
		 
		 JScrollPane scroll = new JScrollPane(list);
		 scroll.setBounds(5, 25, 150, 200);
		 
		 add(label);
		 add(scroll);
		 add(kick);

	}

}
