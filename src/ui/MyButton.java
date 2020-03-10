package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class MyButton extends JButton {
	
	public MyButton(String text, ActionListener action) {
		super(text);
		addActionListener(action);
	}
}
