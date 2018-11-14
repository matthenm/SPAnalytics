package application;


import javafx.stage.Stage;

public class Controller {
	
	private Stage					primaryStage;
	
	/**
	 * This is the method that will allow this Controller class to
	 * load new FXML files. Essentially switching the scenes when
	 * needed.
	 * 
	 * @param loader
	 */
	public void setPrimaryStage(Stage inStage) {
		primaryStage = inStage;
	}// End of the 'setFxmlLoader' method


}
