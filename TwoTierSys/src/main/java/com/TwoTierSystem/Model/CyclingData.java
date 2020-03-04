package com.TwoTierSystem.Model;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.sql.Connection;

/*  Name:  Dylan Roberts
 * Course: CNT 4714 – Spring 2020      
 * Assignment title: Project 3 – Two-Tier Application    
 * Date: Saturday February 25, 2020 
*/


public class CyclingData {
	Connection myConnection;
	Object[] arrayForReturn = new Object[2];
	
	public boolean checkIfQueryStatement(String sqlStatement) {
		String stringParts[] = new String[sqlStatement.split(" ").length];
		stringParts = sqlStatement.split(" ");
		
		if ((stringParts[0].equals("select")) || (stringParts[0].contentEquals("Select")) || (stringParts[0].contentEquals("SELECT"))) {
			return true;
		}
		
		return false;
	}
	public Object[] runSqlStatement(String sqlStatement) {
		Statement statement = null;

		try {
			if (this.myConnection == null) {
		        JOptionPane.showMessageDialog(null, "Cannot Execute Command. Did you enter credentials?", "Credentials Invalid", JOptionPane.ERROR_MESSAGE);
			}
 			statement = this.myConnection.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			//javax.swing.JOptionPane.showMessageDialog(null, "Cannot Execute Command. Did you enter credentials?");
			e1.printStackTrace();
		}
		
		// Determine if sqlStatement is a select statement or not
		
		// If select statement
		if (checkIfQueryStatement(sqlStatement)) {
			arrayForReturn[0] = true;

			try {
				arrayForReturn[1] = statement.executeQuery(sqlStatement);
				return arrayForReturn;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();
			}
		}
		else {
			arrayForReturn[0] = false;

			// Otherwise, run non-select statement
			try {
				arrayForReturn[1] = statement.executeUpdate(sqlStatement);
				return arrayForReturn;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());

				e.printStackTrace();
			}		
		}
		
		return null;
	}
	public boolean establishConnection(String url, String username, String password) {
		try {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				this.myConnection = DriverManager.getConnection(url, username, password);
				if (myConnection.isValid(10)) {
					System.out.print("Connection is valid");
					return true;
				}
				else {
					System.out.print("Connection is NOT valid");
					return false;
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.print("Connection is NOT valid at (2)");
				e.printStackTrace();
			} // should pull from a config file instead
			return false;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Access Denied");
			//e.printStackTrace();
			return false;
		}
	}
	
}
