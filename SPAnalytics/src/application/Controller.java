package application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {
	//login scene variables
	@FXML
	private JFXPasswordField		password;
	@FXML
	private JFXTextField			userName;
	@FXML
	private JFXButton				loginButton;
	
	
	
	//instance variables
	private Scene					scene;
	private Stage					primaryStage;
	private FXMLLoader				fxmlLoader;
	private Parent					parent;
	
	private boolean	isLogin		= true;
	
	private final String	LOGIN_SCENE				= "/view/SPAnalytics-Login.fxml";
	private final String	PLAYER_HOME				= "/view/PlayerHome.fxml";
	private final String	GOALIE_HOME				= "/view/GoalieHome.fxml";
	private final String	PLAYER_CARD				= "/view/SPAnalytics-playerCard.fxml";
	private final String	GOALIE_CARD				= "/view/SPAnalytics-goalieCard.fxml";
	private final String	GOALIE_CARD_PERCENT		= "/view/SPAnalytics-goalieCardPercent.fxml";
	private final String	CSS						= "/view/application.css";
	
	/**
	 * This is the method that will allow this Controller class to
	 * load new FXML files. 
	 */
	public void setPrimaryStage(Stage inStage) {
		primaryStage = inStage;
	}
	
	
	/**
	 * Helper method that will load scene
	 */
	private void loadScene(String newScene) {
		if (newScene.equals(LOGIN_SCENE)) {
			isLogin = true;
		} else {
			isLogin = false;
		}
		try {
			// Switch to player card scene
			fxmlLoader = new FXMLLoader(
					getClass().getResource(newScene));

			// To keep the states of everything in this controller
			fxmlLoader.setController(this);

			// Loading the new FXML file
			parent = fxmlLoader.load();

			scene = new Scene(parent, 600, 400);
			scene.getStylesheets().add(getClass().getResource(CSS).toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("SP Analytics");
			primaryStage.setMaximized(true);
			primaryStage.setFullScreen(true);
			primaryStage.show();
		} catch (Exception err) {
			System.out.println(err);
		}
	}
	
	
	/**
	 * This is the method that will switch to the home screen once login is clicked
	 * Needs error handling for user login. If player --> player home screen.
	 * If goalie --> goalie home screen
	 */
	@FXML
	public void loginButtonClicked() {
		//if(userName.getText() == "test" && password.getText() == "test") {
				loadScene(PLAYER_HOME);
		//} else {
			//loadScene(GOALIE_HOME);
		//}
	}



	/**
	 * This is the method that will go to the player card scene.
	 */
	@FXML
	public void PlayerCardClicked() {
		loadScene(PLAYER_CARD);
	}
	
	/**
	 * This is the method that will go back the home scene.
	 */
	@FXML
	public void HomeButtonClicked() {
		loadScene(PLAYER_HOME);
	}
}

