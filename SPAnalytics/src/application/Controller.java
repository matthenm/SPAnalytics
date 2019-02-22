package application;

import java.awt.Desktop;
import java.net.URL;
import java.util.Optional;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
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

	//Timer variables
	@FXML private Label Time;
	private long timerStart;
	private long timerPause = 0;
	private long currentTime = 0;
	static boolean timerOn = false;	
	private static int periodIndex = 0;
	private final static String[] PERIODS = {"1st", "2nd", "3rd"};
	
	//TimeStamp variables
	@FXML private ComboBox TimeStamps;
	@FXML private TextArea TimeStampNotes;

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
		final long time = TimeUnit.MINUTES.toMillis(20) - l;
		final long min = TimeUnit.MILLISECONDS.toMinutes(time);
		final long sec = TimeUnit.MILLISECONDS.toSeconds(time - TimeUnit.MINUTES.toMillis(min));
		return String.format("%02d:%02d %s", min, sec, PERIODS[periodIndex]);
	}
	
	/**
	 * Returns the current time on the timer
	 */
	public void getTime() {
		long sTime = currentTime - TimeUnit.SECONDS.toMillis(15);
		String start = formatTime(Math.max(sTime, 0));
		String end = formatTime(currentTime);
		//TimeStamps.appendText(start + " - " + end + "\n");
		TimeStampNotes.appendText("Untitled\n");
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
		}else if(rosterList.getValue().equals("ADMIN")) {
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setTitle("Admin Password");
			dialog.setHeaderText("Enter your password:");
			dialog.setContentText("Password:");
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()){
				if(result.get().equals("abc")) {
					loadScene(GOALIE_CARD);
				}
			}
		}
		else {	
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
	
	/**
	 * This is the method that will logout and go back the login scene.
	 */
	@FXML
	public void PlayerLogoutButtonClicked() {
		loadScene(LOGIN_SCENE);
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
	 * This is the method that will logout and go back the login scene.
	 */
	@FXML
	public void GoalieLogoutButtonClicked() {
		loadScene(LOGIN_SCENE);
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
	 * Method increments the period
	 */
	@FXML
	public void NextPeriodClicked() {
		periodIndex = (periodIndex+1) % PERIODS.length;
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
						currentTime = (System.nanoTime() - timerStart) / 1000000 + timerPause;
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
		currentTime = 0;
		periodIndex = 0;
		Time.setText("20:00 1st");
	}
}


