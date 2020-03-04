package com.TwoTierSystem.ClientGui;
import javax.swing.JTextArea;

/*  Name:  Dylan Roberts
 * Course: CNT 4714 – Spring 2020      
 * Assignment title: Project 3 – Two-Tier Application    
 * Date: Saturday February 25, 2020 
*/


public class SqlCmdTextArea extends JTextArea{
	private ClientGui guiViewParent;
	
	public SqlCmdTextArea() {
		super();
	}
    public SqlCmdTextArea(String text) {
		super(text);
	}
	public ClientGui getTextAreaParent() {
		return this.guiViewParent;
	}
	public void setTextAreaParent(ClientGui guiView) {
		this.guiViewParent = guiView;
	}
}
