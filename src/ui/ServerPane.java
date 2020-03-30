package ui;


import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import server.ServerThreadMessage;
import threads.Threads;

public class ServerPane extends JPanel {
	
	private JList<String> list;
	private JButton kick, kickMsg;
	public ServerPane(DefaultListModel<String> model) {
		 setLayout(null);
		 
		 JLabel label = new JLabel("Connected Clients");
		 label.setBounds(new Rectangle( new Point(5, 5), label.getPreferredSize()));
		 
		 kick = new MyButton("Kick", (e) -> {
			 if (kick.isEnabled())
				 Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Main, ServerThreadMessage.Type.None, "kick "+list.getSelectedValue()));
		 } );
		 kick.setEnabled(false);
		 kick.setBounds(new Rectangle( new Point(160, 25), kick.getPreferredSize()));
		 
		 kickMsg = new MyButton("Kick message", (e) ->  {
			if (kickMsg.isEnabled()) {
				 String reason = JOptionPane.showInputDialog("sum fuk?");
				 if (reason!=null) {
					 Threads.getServerQueue().offer(new ServerThreadMessage(Threads.Main, ServerThreadMessage.Type.None, "kick " + list.getSelectedValue() +
						 " ``" + reason + "``")); // `` is a separator for strings 
				 }
			}
		 });
		 kickMsg.setEnabled(false);
		 kickMsg.setBounds(new Rectangle( new Point(225, 25), kickMsg.getPreferredSize()));
		 
		 list = new JList<>(model);
		 list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				// TODO Auto-generated method stub
				if (!e.getValueIsAdjusting()) {
					String selected = list.getSelectedValue();
					if (selected!=null && !selected.equals("null")) {
						kick.setEnabled(true);
						kickMsg.setEnabled(true);
					}
					else {
						kick.setEnabled(false);
						kickMsg.setEnabled(false);
					}
					//kick.setText("Kick user" + list.getSelectedValue());
					//kick.setBounds(new Rectangle( new Point(160, 25), kick.getPreferredSize()));
				}
			}
			 
		 });
		 
		 JScrollPane scroll = new JScrollPane(list);
		 scroll.setBounds(5, 25, 150, 200);
		 
		 add(label);
		 add(scroll);
		 add(kick);
		 add(kickMsg);

	}

}
