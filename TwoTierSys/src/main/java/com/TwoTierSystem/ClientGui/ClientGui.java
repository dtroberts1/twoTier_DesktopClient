package com.TwoTierSystem.ClientGui;

import net.proteanit.sql.DbUtils;
import java.util.Vector;
import java.awt.BorderLayout;  
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;

import javax.swing.table.DefaultTableModel;

import java.util.*;

import com.TwoTierSystem.Model.*;
import com.TwoTierSystem.MyListener.MyListener;
import com.TwoTierSystem.MyListener.MyKeyListener;

public class ClientGui {
	Properties myProperties;
	private String[] driverOptions;
	HashMap<String, String> urlOptions;
	
	FileInputStream fileIn;
	boolean connectedStatus;

	
	boolean fieldsChanged;
	
	MyListener myListener;
	MyKeyListener myKeyListener;

	private static JFrame mainFrame;
	private JPanel driverPanel;
	private JPanel sqlCommandPanel;
	private JPanel btnGrid;
	private JLabel dbInfoMainLabel;
	private JLabel sqlCommandMainLabel;
	
	private JComboBox dbDriverInput;
	private JComboBox dbUrlInput;
	private JTextField usernameInput;
	private JPasswordField passwordInput;
	
	private JPanel driverTopPanel;
	private JPanel driverBottomPanel;
		
	private JLabel dbDriverLabel;
	private JLabel dbUrlLabel;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
		
	private JPanel leftSideDriverPanel;
	private JPanel rightSideDriverPanel;
	
	private JTextField connectionStatusField;
	private ButtonWithExtras connectToDbBtn;
	private ButtonWithExtras clearSqlCmdBtn;
	private ButtonWithExtras execSqlCmdBtn;
	private ButtonWithExtras clearResultWindowBtn;
	
	private CyclingData cyclingData;
	
	private JPanel tablePanel;
	
	private JPanel sqlCommandTopPanel;
	private JPanel sqlCommandBottomPanel;
	
	private SqlCmdTextArea sqlCommandInput;
	private JPanel topNonButtonPanel;
	private JTable outputTable;
	
	private JPanel topPanel;
	private JPanel topButtonPanel;
	
	private JScrollPane sp;
	private JPanel bottomPanel;
	
	private JPanel clearResultsBtnPanel;
	
	public void executeSql() {
		clearResults();

		Object [] boolAndResultSet = new Object[2];
		ResultSetMetaData metaData;
		int nbrUpdatedRows = 0;
		int columnCount = 0;
		int rowCount = 0;
		int currentRow = 0;
		String [] headers;
		String [][] arrayForTable;
		ResultSet myResult;
		String outputStr = "";
		boolAndResultSet = this.cyclingData.runSqlStatement(sqlCommandInput.getText());
		
		if ((boolAndResultSet[0]).equals(false)) {
			// Non-Query Statement
			nbrUpdatedRows = (int) boolAndResultSet[1]; 
			clearResults();
		}
		else if((boolAndResultSet[0]).equals(true)) {
			myResult = (ResultSet) boolAndResultSet[1]; 
			try {
				metaData = myResult.getMetaData();
				rowCount = myResult.getFetchSize();
				columnCount = metaData.getColumnCount();
				headers = new String[columnCount];
				for (int currentCol = 1; currentCol < (columnCount + 1); currentCol++)
				{
					headers[currentCol - 1] = metaData.getColumnName(currentCol);
				}
				while (myResult.next()) {
					rowCount++;					
				}
				arrayForTable = new String[rowCount][columnCount];

				myResult.beforeFirst();
				while (myResult.next()) {
					for (int currentCol = 1; currentCol < (columnCount + 1); currentCol++)
					{
						outputStr += myResult.getString(metaData.getColumnName(currentCol));
						arrayForTable[currentRow][currentCol - 1] = myResult.getString(metaData.getColumnName(currentCol));
					}
					currentRow++;
				}
				myResult.beforeFirst();
				

				// Update Table with new Data
				outputTable.setModel(DbUtils.resultSetToTableModel(myResult));			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.out.print("Error, no integer or result set");
		}
	}
	public void clearResults() {
		if (outputTable != null) {
			DefaultTableModel model = (DefaultTableModel) outputTable.getModel();
			model.setRowCount(0);
			model.setColumnCount(0);
		}
	}
	
	public String getUserName() {
		return this.usernameInput.getText();
	}
	public String getPassword() {
		return this.passwordInput.getText();
	}
	public String getUrl() {
		return dbUrlInput.getSelectedItem().toString();
	}
	public String getDriver() {
		return dbDriverInput.getSelectedItem().toString();
	}
	
	public void clearSqlCmd() {
		sqlCommandInput.setText("");
	}
	
	public ClientGui(CyclingData cyclingData) {
		driverOptions = new String[4];
		connectedStatus = false;
		HashMap<String, String> urlOptions = new HashMap<String, String>(10);
		this.cyclingData = cyclingData;
		try {
			fileIn = new FileInputStream("db.properties");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		fieldsChanged = true;
		myProperties = new Properties();
		try {
			myProperties.load(fileIn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myProperties.forEach((k, v) -> {
			urlOptions.put(k.toString(), v.toString());
		});
		urlOptions.remove("MYSQL_DB_DRIVER_CLASS");
		System.out.print("printing urlOptions: " + urlOptions);
		for (String item : urlOptions.values()) {
			
		}
		driverOptions[0] = myProperties.get("MYSQL_DB_DRIVER_CLASS").toString();
		initializeGui(driverOptions, urlOptions);

	}
	public CyclingData getCyclingData() {
		return this.cyclingData;
	}
	public void setConnectionOutput(boolean isSuccessful) {
		if (isSuccessful) {
			connectionStatusField.setText("Connected to " + this.getUrl());
			fieldsChanged = false;
			connectedStatus = true;
		}
		else {
			connectionStatusField.setText("Not Connected");
			connectedStatus = false;
		}
	}
	public void initializeGui(String[] driverOptions, HashMap<String, String> urlOptions) {
		mainFrame = new JFrame();
		myListener = new MyListener();
		myKeyListener = new MyKeyListener();
		mainFrame.setBounds(345, 40, 920, 700);
		
		driverPanel = new JPanel();
		dbInfoMainLabel = new JLabel();
		dbInfoMainLabel.setText("Enter Database Information");

		dbDriverLabel = new JLabel();
		dbDriverLabel.setText("JDBC Driver");
		dbDriverLabel.setFont(new Font("SansSerif", 0, 14));
		dbDriverLabel.setBorder(new EmptyBorder(0,0,5,0));
		dbUrlLabel = new JLabel();
		dbUrlLabel.setText("Database URL");
		dbUrlLabel.setFont(new Font("SansSerif", 0, 14));
		dbUrlLabel.setBorder(new EmptyBorder(0,0,5,0));
		usernameLabel = new JLabel();
		usernameLabel.setText("Username");
		usernameLabel.setFont(new Font("SansSerif", 0, 14));
		usernameLabel.setBorder(new EmptyBorder(0,0,5,0));
		passwordLabel = new JLabel();
		passwordLabel.setText("Password");
		passwordLabel.setFont(new Font("SansSerif", 0, 14));
		passwordLabel.setBorder(new EmptyBorder(0,0,5,0));
		
		dbDriverInput = new JComboBox(driverOptions);
		dbDriverInput.setEditable(true);
		dbDriverInput.setBackground(Color.WHITE);
		
		dbUrlInput = new JComboBox(urlOptions.values().toArray());
		dbUrlInput.setEditable(true);
		dbUrlInput.setBackground(Color.WHITE);
		usernameInput = new JTextField("", 20);
		usernameInput.setFont(new Font("SansSerif", 0, 14));
		passwordInput = new JPasswordField("", 20);
		passwordInput.setEchoChar('*');
		passwordInput.setFont(new Font("SansSerif", 0, 14));

		leftSideDriverPanel = new JPanel();
		driverTopPanel = new JPanel();
		driverBottomPanel = new JPanel();

		leftSideDriverPanel.add(dbDriverLabel);
		leftSideDriverPanel.add(dbUrlLabel);
		leftSideDriverPanel.add(usernameLabel);
		leftSideDriverPanel.add(passwordLabel);
		leftSideDriverPanel.setLayout(new BoxLayout(leftSideDriverPanel, BoxLayout.Y_AXIS));
		leftSideDriverPanel.setBorder(new EmptyBorder(0,0,0,5));
		
		rightSideDriverPanel = new JPanel();
		rightSideDriverPanel.add(dbDriverInput);
		rightSideDriverPanel.add(dbUrlInput);
		rightSideDriverPanel.add(usernameInput);
		rightSideDriverPanel.add(passwordInput);
		rightSideDriverPanel.setLayout(new BoxLayout(rightSideDriverPanel, BoxLayout.Y_AXIS));
		
		driverTopPanel.setLayout(new BoxLayout(driverTopPanel, BoxLayout.X_AXIS));
		driverBottomPanel.setLayout(new BoxLayout(driverBottomPanel, BoxLayout.X_AXIS));
		
		//driverPanel.setLayout(new GridLayout(2, 1));
		driverTopPanel.add(dbInfoMainLabel);
		driverBottomPanel.add(leftSideDriverPanel, BorderLayout.WEST);
		driverBottomPanel.add(rightSideDriverPanel, BorderLayout.EAST);
		driverPanel.add(driverTopPanel, BorderLayout.NORTH);
		driverPanel.add(driverBottomPanel, BorderLayout.SOUTH);
		
		sqlCommandTopPanel = new JPanel();
		sqlCommandTopPanel.setLayout(new BoxLayout(sqlCommandTopPanel, BoxLayout.X_AXIS));
		sqlCommandBottomPanel = new JPanel();
		sqlCommandBottomPanel.setLayout(new BoxLayout(sqlCommandBottomPanel, BoxLayout.X_AXIS));

		sqlCommandPanel = new JPanel();
		sqlCommandMainLabel = new JLabel();
		sqlCommandMainLabel.setText("Enter a SQL Command");
		sqlCommandTopPanel.add(sqlCommandMainLabel);
		
		sqlCommandInput = new SqlCmdTextArea();
		sqlCommandInput.setTextAreaParent(this);
		sqlCommandInput.addKeyListener(myKeyListener);
		sqlCommandInput.setColumns(40);
		sqlCommandInput.setRows(8);

		sqlCommandBottomPanel.add(sqlCommandInput);
		
		sqlCommandPanel.add(sqlCommandTopPanel, BorderLayout.NORTH);
		sqlCommandPanel.add(sqlCommandBottomPanel, BorderLayout.SOUTH);
		
		btnGrid = new JPanel();
		connectionStatusField = new JTextField("", 30); 
		connectionStatusField.setEditable(false);
		connectionStatusField.setBackground(Color.RED);
		connectionStatusField.setText("No Connection");
		connectionStatusField.setFont(new Font("SansSerif", Font.BOLD, 13));
		connectToDbBtn = new ButtonWithExtras();
		connectToDbBtn.setText("Connect To Database");
		connectToDbBtn.setBtnParent(this);
		connectToDbBtn.addActionListener(myListener);
		clearSqlCmdBtn = new ButtonWithExtras();
		clearSqlCmdBtn.setText("Clear SQL Command");
		clearSqlCmdBtn.setBtnParent(this);
		clearSqlCmdBtn.addActionListener(myListener);
		execSqlCmdBtn = new ButtonWithExtras();
		execSqlCmdBtn.setText("Execute SQL Command");
		execSqlCmdBtn.setBtnParent(this);
		execSqlCmdBtn.addActionListener(myListener);
		
		topNonButtonPanel = new JPanel();
		topNonButtonPanel.setLayout(new BoxLayout(topNonButtonPanel, BoxLayout.X_AXIS));
		topButtonPanel = new JPanel();
		topButtonPanel.setLayout(new BoxLayout(topButtonPanel, BoxLayout.X_AXIS));

		btnGrid.add(connectionStatusField);
		btnGrid.add(connectToDbBtn);
		btnGrid.add(clearSqlCmdBtn);
		btnGrid.add(execSqlCmdBtn);
		
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		
		driverPanel.setLayout(new BoxLayout(driverPanel, BoxLayout.Y_AXIS));
		driverPanel.setBorder(new EmptyBorder(10,20,20,20));

		sqlCommandPanel.setLayout(new BoxLayout(sqlCommandPanel, BoxLayout.Y_AXIS));
		sqlCommandPanel.setBorder(new EmptyBorder(10,0,0,20));

		
		
		topNonButtonPanel.add(driverPanel, BorderLayout.WEST);
		topNonButtonPanel.add(sqlCommandPanel, BorderLayout.EAST);
		topButtonPanel.add(btnGrid);
		topButtonPanel.setBorder(new EmptyBorder(10,0,0,0));
		
		topPanel.add(topNonButtonPanel, BorderLayout.NORTH);
		topPanel.add(topButtonPanel, BorderLayout.SOUTH);

		outputTable = new JTable();
		
		//outputTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		outputTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);


		tablePanel = new JPanel();
		tablePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        sp = new JScrollPane(outputTable); 
        sp.setBackground(Color.WHITE);
        tablePanel.add(sp);
        clearResultsBtnPanel = new JPanel();
		bottomPanel = new JPanel();
		
		clearResultWindowBtn = new ButtonWithExtras();
		clearResultWindowBtn.setText("Clear Result Window");
		clearResultWindowBtn.setBackground(Color.YELLOW);

		clearResultWindowBtn.setBtnParent(this);
		clearResultWindowBtn.addActionListener(myListener);
		clearResultsBtnPanel.add(clearResultWindowBtn);
		//clearResultsBtnPanel.setBorder(new EmptyBorder(10,0,0,0));

		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.X_AXIS));
		bottomPanel.add(tablePanel);
		bottomPanel.add(clearResultsBtnPanel);

		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

		mainFrame.add(topPanel, BorderLayout.NORTH);
		mainFrame.add(bottomPanel, BorderLayout.SOUTH);

		mainFrame.setVisible(true);
	}

	
}
