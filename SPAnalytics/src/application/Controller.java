package application;

import java.awt.Desktop;
import java.net.URL;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {

	//login scene variables
	@FXML
	private JFXPasswordField		password;
	@FXML
	private JFXTextField			userName;
	@FXML
	private JFXButton				loginButton;

	//Video tab scene variables
	@FXML 
	private TextField NCHC_URL;

	//Timer Variables
	@FXML private Label Millisecond;
	@FXML private Label Second;
	@FXML private Label Minute;
	@FXML private Label Hour;
	static int milliseconds = 0;
	static int seconds = 0;
	static int minutes = 0;
	static int hours = 0;

	static boolean timerOn = false;	

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
		loadScene(PLAYER_HOME);
	}


	// player card button functionalities 
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
	public void PlayerHomeButtonClicked() {
		loadScene(PLAYER_HOME);
	}

	// goalie card button functionalities
	/**
	 * This is the method that will go to the player card scene.
	 */
	@FXML
	public void GoalieCardClicked() {
		loadScene(GOALIE_CARD);
	}

	/**
	 * This is the method that will go back the home scene.
	 */
	@FXML
	public void GoalieHomeButtonClicked() {
		loadScene(GOALIE_HOME);
	}

	/**
	 * Method opens NCHC link on default browser
	 */
	@FXML
	public void NCHCOpenButtonClicked() {
		try {
			Desktop.getDesktop().browse(new URL(NCHC_URL.getText()).toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method starts timer
	 */
	@FXML
	public void StartTimerClicked() {
		timerOn = true;

		Thread t = new Thread() {
			public void run() {
				for(;;) {
					if(timerOn) {
						try {
							Thread.sleep(1);

							if(milliseconds>=1000) {
								milliseconds = 0; 
								seconds++;
							}
							if(seconds >= 60) {
								seconds = 0;
								minutes++;
							}
							if(minutes >= 60) {
								minutes = 0;
								hours++;
							}
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									Millisecond.setText(" : " + milliseconds);
									Second.setText(" : " + seconds);
									Minute.setText(" : " + minutes);
									Hour.setText("" + hours);	
								}
							});

							milliseconds++;
						} catch(Exception e) {
							System.out.println(e.getMessage());
						}
					}	
				}
			}
		};
		t.start();
	}


	/**
	 * Method stops timer
	 */
	@FXML
	public void StopTimerClicked() {
		timerOn = false;
	}

	/**
	 * Method restarts timer
	 */
	@FXML
	public void ResetTimerClicked() {
		timerOn = false;
		hours = 0;
		minutes = 0;
		seconds = 0;
		milliseconds = 0;

		Hour.setText("00 : ");
		Minute.setText("00 : ");
		Second.setText("00 : ");
		Millisecond.setText("00");
	}
}


