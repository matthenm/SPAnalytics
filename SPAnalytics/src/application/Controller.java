package application;

import java.awt.Desktop;
import java.net.URL;
import java.util.concurrent.TimeUnit;

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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {

	//login scene variables
	/*
	@FXML
	private JFXPasswordField		password;
	@FXML
	private JFXTextField			userName;
	*/
	@FXML
	private JFXButton				loginButton;
	@FXML
	private ChoiceBox<String>		rosterList;

	
	//Video tab scene variables
	@FXML 
	private TextField NCHC_URL;

	//Timer Variables
	@FXML private Label Time;
	private long timerStart;
	private long timerPause = 0;
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
	 * Helper method that converts milliseconds to a stopwatch time format
	 */
	private static String formatTime(final long l)
	{
		final long hr = TimeUnit.MILLISECONDS.toHours(l);
		final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
		final long sec = TimeUnit.MILLISECONDS.toSeconds(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
		final long ms = TimeUnit.MILLISECONDS.toMillis(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));
		return String.format("%01d:%02d:%02d.%03d", hr, min, sec, ms);
	}
	
	
	/**
	 * This is the method that will switch to the home screen once login is clicked
	 * If player --> player home screen.
	 * If goalie --> goalie home screen
	 */
	@FXML
	public void loginButtonClicked() {
		if(rosterList.getValue().equals("Ryan Larkin")) {
			loadScene(GOALIE_HOME);
		}else {
			loadScene(PLAYER_HOME);
		}
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
		timerStart = System.nanoTime();

		Thread t = new Thread() {
			public void run() {
				while(timerOn) {
					try {
						long currentTime = (System.nanoTime() - timerStart) / 1000000 + timerPause;
						String formattedTime = formatTime(currentTime);

						Thread.sleep(10);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								Time.setText(formattedTime);
							}
						});

					} catch(Exception e) {
						System.out.println(e.getMessage());
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
		timerPause += (System.nanoTime() - timerStart) / 1000000;
	}

	/**
	 * Method restarts timer
	 */
	@FXML
	public void ResetTimerClicked() {
		timerStart = System.nanoTime();
		timerPause = 0;
		Time.setText("0:00:00.000");
	}
}


