package GUIs;

import java.util.Optional;

import Network.Connections;
import Network.People;
import Network.User;
import System.FileReader;
import System.MyGlobals;
import javafx.application.Application;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MiniNet extends Application {

	/*+-----------------+*
	 *| Class Variables |
	 *+-----------------+*/
	
	public static People people = new People();
	public static Connections links = new Connections();
	public static User currentUser;
	
	private MyGlobals glob = new MyGlobals();
	
	/*+---------+*
	 *| Methods |
	 *+---------+*/
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start (Stage primaryStage) throws Exception {
		
		/*+---------+*
		 *| SQLLite |
		 *+---------+*/
		
		// Create Database
		glob.sqlCreateDatabase();
		
		// Validate Data Text File "people.tx and relations.txt"
		
		if(glob.isFileExist(glob.txtfilePeople) && glob.isFileExist(glob.txtfileRelations)) {
			
			// Use the Text File
			FileReader f = new FileReader();
			f.initialiseTempPeople();
			f.initialiseTempRelations();
			
			for (String key : f.getTempPeople().getAllProfiles().keySet()) {
				people.addUser(f.getTempPeople().getAllProfiles().get(key));
			}
			
			for (int i=0; i<f.getTempLinks().getRelationships().size(); i++) {
				links.getRelationships().add(f.getTempLinks().getRelationships().get(i));
			}
		} else {
			Optional<ButtonType> result = glob.myConfirm("Confirm", 
					"Your Data Text File(s) are missing!", 
					"Do you want to continue and use the SQLLite instead? \r\n" +
					"", 
					AlertType.CONFIRMATION);
			if (result.get() == ButtonType.OK){
				
				// Use the SQLite
				glob.myAlert("Use Database here");
				
				
		    	} else {
		    		
		    		// End the program (No Data file was selected)
		    		glob.myAlert("You have choosen to abort");
		    		System.exit(0);
		    		
		    	};
		}
		
		// Main Menu UI
		MainMenu mainMenu = new MainMenu(primaryStage);
		mainMenu.displayMainMenu();
	}

}


