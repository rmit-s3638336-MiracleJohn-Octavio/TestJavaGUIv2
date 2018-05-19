package GUIs;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// Current Version

import FileHandling.FileReader;
import Network.Adult;
import Network.Child;
import Network.Connections;
import Network.People;
import Network.User;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MiniNet extends Application {

	// dummy profiles remember to delete this after
	public static People people = new People();
	
	
	public static Connections links = new Connections();
	public static User currentUser;
	
	public void createDummy() throws Exception {
		Adult Alice 	 = new Adult("Alice", 21, "Working", "Alice.png", "F", "");
		Adult Bob	 = new Adult("Bob", 22, "Working in RMIT", "Bob.png", "M", "");
		Adult Don 	 = new Adult("Don", 22);
		Adult Cathy 	 = new Adult("Cathy");
		Adult Adel 	 = new Adult("Adel", 24);
		Adult Deb 	 = new Adult("Deb", 19);
		Adult Ben 	 = new Adult("Ben", 19);
		Adult Philip = new Adult("Philip", 19);
		Adult May 	 = new Adult("May", 19);
		Adult Maya 	 = new Adult("Maya", 19);
		Adult Kim 	 = new Adult("Kim", 19);
		Adult Ali 	 = new Adult("Ali", 19);
		Adult Syl 	 = new Adult("Syl", 19);
		Adult Chia	 = new Adult("Chia", 19);
		
		Alice.addFriend(Bob);
		links.addRelationship("Alice", "Bob", "friend");
		
		Alice.addFriend(Don);
		links.addRelationship("Alice", "Don", "friend");
					
		Child Jane = new Child("Jane", 12, Alice, Bob);
		links.addRelationship("Alice", "Jane", "parent");
		links.addRelationship("Bob", "Jane", "parent");
		links.changeRelationship("Alice", "Bob", "couple");
		
		Child Mike = new Child("Mike", 12, Alice, Bob);
		links.addRelationship("Alice", "Mike", "parent");
		links.addRelationship("Bob", "Mike", "parent");
		
		Child John = new Child("John", 12, Cathy, Don);
		links.addRelationship("Cathy", "John", "parent");
		links.addRelationship("Don", "John", "parent");
		links.changeRelationship("Cathy", "Don", "couple");
			
		people.addUser(Alice);
		people.addUser(Bob);
		people.addUser(Don);
		people.addUser(Cathy);
		people.addUser(Jane);
		people.addUser(Mike);
		people.addUser(John);
		people.addUser(Adel);
		people.addUser(Deb);
		people.addUser(Ben);
		people.addUser(Philip);
		people.addUser(May);
		people.addUser(Maya);
		people.addUser(Kim);
		people.addUser(Ali);
		people.addUser(Syl);
		people.addUser(Chia);
	}
	
	@Override
	public void start (Stage primaryStage) throws Exception {
		
		// instantiate the users
		FileReader f = new FileReader();
		f.initialiseTempPeople();
		f.initialiseTempRelations();
		
		for (String key : f.getTempPeople().getAllProfiles().keySet()) {
			people.addUser(f.getTempPeople().getAllProfiles().get(key));
		}
		
		for (int i=0; i<f.getTempLinks().getRelationships().size(); i++) {
			links.getRelationships().add(f.getTempLinks().getRelationships().get(i));
		}
		
		/*+---------+*
		 *| SQLLite |
		 *+---------+*/
		
		// Create Database
		createDatabase();
		
		// Main Menu UI
		MainMenu mainMenu = new MainMenu(primaryStage);
		mainMenu.displayMainMenu();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	/*+------+*
	 *| Data |
	 *+------+*/
	
	String dbFile = "MiniNet.db";
	String dbConnectionString = "jdbc:sqlite:" + dbFile; // Database Connection String
	
	private void createDatabase() {
		
		myAlert("This is a test");
		
		File tmpDir = new File(dbFile);
		if (!tmpDir.exists()) {
			try (Connection conn = DriverManager.getConnection(dbConnectionString)) {
	            if (conn != null) {
	                DatabaseMetaData meta = conn.getMetaData();
	                myAlert(
	                		"The driver name is \n" + meta.getDriverName() +
	                		"Database was successfully created!");
	            }
	            
	        } catch (SQLException e) {
	            myAlert(e.getMessage());
	        }

			// Added to Git
			
			createTable();
		} else {
			myAlert("The database already exist!");
		}
	}
	
    private void createTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS people "
        		+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
        		+ "name TEXT NOT NULL, "
        		+ "photo TEXT, "
        		+ "status TEXT, "
        		+ "gender TEXT, "
        		+ "age INTEGER, "
        		+ "state TEXT);";
        
        try (Connection conn = DriverManager.getConnection(dbConnectionString);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
        		myAlert(e.getMessage());
        }
    }
	
	/*+-----+*
	 *| UDF |
	 *+-----+*/
	
	public void myAlert(String title, String header, String message, AlertType alertType) {
		Alert alert = new Alert(alertType);
	    	alert.setTitle(title);
	    	Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        alert.setX(bounds.getMaxX() / 4);
        alert.setY(bounds.getMaxY() / 5);
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
	
}


