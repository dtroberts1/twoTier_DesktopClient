package com.TwoTierSystem.MyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.TwoTierSystem.ClientGui.ButtonWithExtras;
import com.TwoTierSystem.ClientGui.ClientGui;
import com.TwoTierSystem.ClientGui.SqlCmdTextArea;

/*  Name:  Dylan Roberts
 * Course: CNT 4714 – Spring 2020      
 * Assignment title: Project 3 – Two-Tier Application    
 * Date: Saturday February 25, 2020 
*/

public class MyListener implements ActionListener {
	
    public void actionPerformed(ActionEvent e) {
    	boolean successfulConnection;
		String buttonLabel = e.getActionCommand().replaceAll("\\d", "");
		//System.out.printf("source is %s", conditionString);
		
		if (e.getSource() instanceof ButtonWithExtras) {

			ClientGui clientGui = ((ButtonWithExtras)e.getSource()).getButtonParent();
			
			if(buttonLabel.equals("Connect To Database"))
			{	
				System.out.print("URL IS " + clientGui.getUrl());
				System.out.print("username is " + clientGui.getUserName() + ", password is " + clientGui.getPassword());
				successfulConnection = clientGui.getCyclingData().establishConnection(clientGui.getUrl(), clientGui.getUserName(), clientGui.getPassword());
				if (successfulConnection) {
					clientGui.setConnectionOutput(true);
				}
				else {
					clientGui.setConnectionOutput(false);
				}
			}	
			else if(buttonLabel.equals("Clear SQL Command")) {
				clientGui.clearSqlCmd();
			}
			else if(buttonLabel.equals("Execute SQL Command")) {
				clientGui.executeSql();
			}
			else if(buttonLabel.equals("Clear Result Window")) {
				clientGui.clearResults();
			}

		}
		else if ((e.getSource() instanceof SqlCmdTextArea) && (e.getActionCommand().equals(""))){
			ClientGui clientGui = ((SqlCmdTextArea)e.getSource()).getTextAreaParent();
			System.out.println("");
		}
    }
}
