package com.TwoTierSystem.MyListener;

import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import com.TwoTierSystem.ClientGui.ButtonWithExtras;
import com.TwoTierSystem.ClientGui.ClientGui;
import com.TwoTierSystem.ClientGui.SqlCmdTextArea;

/*  Name:  Dylan Roberts
 * Course: CNT 4714 – Spring 2020      
 * Assignment title: Project 3 – Two-Tier Application    
 * Date: Saturday February 25, 2020 
*/


public class MyKeyListener implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() instanceof SqlCmdTextArea) {
			ClientGui clientGui = ((SqlCmdTextArea)e.getSource()).getTextAreaParent();
			if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK)) {
				System.out.print("Enter button is pressed in SqlCmdField");
				clientGui.executeSql();
			}
		}	
	}
}
