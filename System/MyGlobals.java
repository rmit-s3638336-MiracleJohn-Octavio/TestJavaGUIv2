package System;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Screen;

public class MyGlobals {
	
	/*+-----------+*
	 *| Variables |
	 *+-----------+*/
	
	// Window Size
	public final Double WINDOW_W = (double) 650;
	public final Double WINDOW_H = (double) 650;
	
	// Text Files
	public final String txtfilePeople = "people.txt";
	public final String txtfileRelations = "relations.txt";
	
	// SQLLite
	public final String sqlDBFile = "MiniNet.db";
	public final String sqlConnString = "jdbc:sqlite:" + sqlDBFile; // Database Connection String

	/*+------+*
	 *| Data |
	 *+------+*/
	
	public void sqlCreateDatabase() {
		File tmpDir = new File(sqlDBFile);
		if (!tmpDir.exists()) {
			try (Connection conn = DriverManager.getConnection(sqlConnString)) {
	            if (conn != null) {
	                DatabaseMetaData meta = conn.getMetaData();
	                myAlert(
	                		"The driver name is \n" + meta.getDriverName() +
	                		"Database was successfully created!");
	            }
	            
	        } catch (SQLException e) {
	            myAlert(e.getMessage());
	        }

			sqlCreateTable();
		} 
	}
	
	public void sqlCreateTable() {
        // SQL statement for creating a new table
        String sqlQuery = "CREATE TABLE IF NOT EXISTS people "
        		+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
        		+ "name TEXT NOT NULL, "
        		+ "photo TEXT, "
        		+ "status TEXT, "
        		+ "gender TEXT, "
        		+ "age INTEGER, "
        		+ "state TEXT);";
        
        try (Connection conn = DriverManager.getConnection(sqlConnString);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sqlQuery);
        } catch (SQLException e) {
        		myAlert(e.getMessage());
        }
    }
	
	public void sqlExecQuery(String sqlQuery) {
        try (Connection conn = DriverManager.getConnection(sqlConnString);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sqlQuery);
        } catch (SQLException e) {
        		myAlert(e.getMessage());
        }
    }
	
	/*+------+*
	 *| File |
	 *+------+*/
	
	public boolean isFileExist(String fileName) {
		// Declare Variable
		boolean isFileExist = false; 
		
		// Validate
		File tmpDir = new File(fileName);
		if (tmpDir.exists()) {
			isFileExist = true;
		}
		
		// Return the value
		return isFileExist;
	}
	
	/*+-----+*
	 *| UDF |
	 *+-----+*/
	
	public void myAlert(String title, String header, String message, AlertType alertType) {
		Alert alert = new Alert(alertType);
	    	alert.setTitle(title);
	    	Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
//        alert.setX(bounds.getMaxX() / 4);
//        alert.setY(bounds.getMaxY() / 5);
	    	alert.setHeaderText(header);
	    	alert.setContentText(message);
	    	alert.showAndWait();
	}
	public void myAlert(String message) {
		this.myAlert("Message Alert","Header",message, AlertType.INFORMATION);
	}
	public void myAlert() {
		this.myAlert("Message Alert","Header","This is a dummy alert!", AlertType.INFORMATION);
	}

	public Optional<ButtonType> myConfirm(String title, String header, String message, AlertType alertType) {
		Alert alert = new Alert(alertType);
	    	alert.setTitle(title);
	    	Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
	    	alert.setHeaderText(header);
	    	alert.setContentText(message);
	    	
	    	Optional<ButtonType> result = alert.showAndWait();
	    	return result;
	}
	
}
