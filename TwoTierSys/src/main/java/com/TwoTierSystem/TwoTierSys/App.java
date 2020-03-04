package com.TwoTierSystem.TwoTierSys;

import com.TwoTierSystem.ClientGui.ClientGui; 
import com.TwoTierSystem.Model.CyclingData;

/*  Name:  Dylan Roberts
 * Course: CNT 4714 – Spring 2020      
 * Assignment title: Project 3 – Two-Tier Application    
 * Date: Saturday February 25, 2020 
*/

public class App {

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // should pull from a config file instead
			CyclingData cyclingData = new CyclingData();
			ClientGui gui = new ClientGui(cyclingData);
			System.out.print("testing..");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		

	}

}
