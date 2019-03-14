package application;


import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.sun.javafx.css.Style;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sun.misc.GC;

public class Controller {

	//login scene variables
	@FXML
	private JFXButton				loginButton;
	@FXML
	private ChoiceBox<String>		rosterList;
	@FXML
	private JFXComboBox<String>		users;
	@FXML
	private JFXPasswordField		adminPass;
	
    
	//netChart variables
	@FXML private Canvas netChartCanvas;
	private GraphicsContext gc1;
	
	//scoringChances variables
	@FXML private Canvas scoringChancesCanvas;
	private GraphicsContext gc2;
	
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
	private final static String[] PERIODS = {"1st", "2nd", "3rd", "OT"};
	private ArrayList<Clip> clips = new ArrayList<Clip>();
	
	//TimeStamp variables
	@FXML private ComboBox<String> TimeStamps;
	@FXML private TextArea TimeStampNotes;
	
	//RinkDiagram variables
	@FXML private Canvas RinkCanvas;
	@FXML private ColorPicker RinkCP;
	@FXML private Slider RinkSlider;
	@FXML private ToggleGroup RinkGroup;
	private GraphicsContext rinkGC;

	//instance variables
	private Scene					scene;
	private Stage					primaryStage;
	private FXMLLoader				fxmlLoader;
	private Parent					parent;

	private boolean	isLogin		= true;

	private final String	LOGIN_SCENE				= "/view/SPAnalytics-Login.fxml";
	private final String	PLAYER_HOME				= "/view/PlayerHome.fxml";
	private final String	GOALIE_HOME				= "/view/GoalieHome.fxml";
	private final String	TEAM_PROFILE			= "/view/TeamProfile.fxml";
	private final String	PLAYER_CARD				= "/view/SPAnalytics-playerCard.fxml";
	private final String	GOALIE_CARD				= "/view/SPAnalytics-goalieCard.fxml";
	private final String	GOALIE_CARD_PERCENT		= "/view/SPAnalytics-goalieCardPercent.fxml";
	private final String	ADMIN_SCORINGCHANCES	= "/view/Admin_ScoringChances.fxml";
	private final String	ADMIN_RINKDIAGRAM		= "/view/Admin_RinkDiagram.fxml";
	private final String	ADMIN_NETCHART			= "/view/Admin_NetChart.fxml";
	private final String	ADMIN_HOME				= "/view/AdminHome.fxml";
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
		try {
			gc1 = netChartCanvas.getGraphicsContext2D();
			gc1.setStroke(Color.RED);
			gc1.setLineWidth(7);
		} catch(Exception e) {}
		
		try {
			gc2 = scoringChancesCanvas.getGraphicsContext2D();
			gc2.setStroke(Color.RED);
			gc2.setLineWidth(7);
		} catch(Exception e) {}
		
		try {
			RinkCP.setValue(Color.BLACK);
			rinkGC = RinkCanvas.getGraphicsContext2D();
			rinkGC.setStroke(RinkCP.getValue());
			rinkGC.setFill(RinkCP.getValue());
			rinkGC.setLineWidth(RinkSlider.getValue());
			rinkGC.setFont(new Font("Verdana", 18));
			TimeStamps.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					int index = TimeStamps.getSelectionModel().getSelectedIndex();
					TimeStampNotes.setText(clips.get(index).getTitle());
				}
				
			});
		} catch(Exception e) {}
	}

	/**
	 * Helper method that converts milliseconds to a stopwatch time format
	 */
	private static String formatTime(final long l)
	{
		final long time;
		
		if(periodIndex <= 2) {
			time = TimeUnit.MINUTES.toMillis(20) - l;
		} else {
			time = TimeUnit.MINUTES.toMillis(5) - l;
		}
		if (time < 0) {
			return String.format("%02d:%02d %s", 0, 0, PERIODS[periodIndex]);
		}
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
		Clip c = new Clip(start + " - " + end, "Untitled");
		TimeStamps.getItems().add(c.getTime());
		clips.add(c);
	}
	
	
	/**
	 * This is the method that will switch to the home screen once login is clicked
	 * If player --> player home screen.
	 * If goalie --> goalie home screen
	 * if admin --> admin home screen
	 */
	@FXML
	public void loginButtonClicked() {
		if(users.getValue().equals("Ryan Larkin")) {
			loadScene(GOALIE_HOME);
		}else if(users.getValue().equals("ADMIN")) {
			adminPass.setVisible(true);
			if(adminPass.getText().equals("test")) {
				loadScene(ADMIN_HOME);
			}		
	}
		else {	
			loadScene(PLAYER_HOME);
		}
	}
	
	//Admin card button functionalities
	/**
	 * This is the method that will go back to the previous scene.
	 */
	@FXML
	public void goBack() {
		loadScene(ADMIN_HOME);
	}
	
	/**
	 * This is the method that will go to the net chart scene.
	 */
	@FXML
	public void NetChartClicked() {
		loadScene(ADMIN_NETCHART);
	}
	
	/**
	 * This is the method that will go to the scoring chances scene.
	 */
	@FXML
	public void ScoringChancesClicked() {
		loadScene(ADMIN_SCORINGCHANCES);
	}
	
	/**
	 * This is the method that will go to the rink diagram scene.
	 */
	@FXML
	public void RinkDiagramClicked() {
		loadScene(ADMIN_RINKDIAGRAM);
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
	 * This is the method that will change the color of the circle to red
	 */
	@FXML
	public void setColorRed() {
		gc1.setStroke(Color.color(196, 33, 52));
	}
	
	/**
	 * This is the method that will change the color of the circle to green
	 */
	@FXML
	public void setColorGreen() {
		gc1.setStroke(Color.GREEN);
	}
	
	/**
	 * This is the method that will draw circles Net chart when mouse released
	 */
	@FXML
	public void drawCircle(MouseEvent e) {
		gc1.strokeOval(e.getX()-20, e.getY()-20, 50, 50);
		gc1.fillText("1", e.getX()+3, e.getY()+10);
	}
	
	
	/**
	 * This is the method that will change the color of the circle to red
	 */
	@FXML
	public void setRed() {
		gc2.setStroke(Color.color(196, 33, 52));
	}
	
	/**
	 * This is the method that will change the color of the circle to green
	 */
	@FXML
	public void setGreen() {
		gc2.setStroke(Color.GREEN);
	}
	
	/**
	 * This is the method that will draw circles Scoring chances when mouse released
	 */
	@FXML
	public void drawCircleScoring(MouseEvent e) {
		gc2.strokeOval(e.getX()-20, e.getY()-20, 50, 50);
		gc2.fillText("2", e.getX()+3, e.getY()+10);
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
		timerStart = System.nanoTime();
		timerPause = 0;
		currentTime = 0;
		Time.setText(formatTime(currentTime));
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
	
	/**
	 * Method displays proper title for selected timestamp
	 */
	@FXML
	public void TimeStampAction() {
		int index = TimeStamps.getSelectionModel().getSelectedIndex();
		TimeStampNotes.setText(clips.get(index).getTitle());
	}
	
	/**
	 * Method Saves the title of selected clip
	 */
	/**
	 * NOTES FROM MEETING: Add drive tab, add numbers to drawing
	 */
	@FXML
	public void SaveNotesButtonClicked() {
		int index = TimeStamps.getSelectionModel().getSelectedIndex();
		if (index == -1) return;
		clips.get(index).setTitle(TimeStampNotes.getText());
	}
	
	/**
	 * Method saves the NCHC link
	 * NOT YET IMPLEMENTED
	 */
	@FXML
	public void NCHCSaveButtonClicked() {
		
	}
	
	/**
	 * Method begins drawing on canvas when mouse pressed
	 */
	@FXML
	public void CanvasMousePressed(MouseEvent e) {
		RadioButton selected = (RadioButton) RinkGroup.getSelectedToggle();
		if(selected.getText().equals("Line")) {
			rinkGC.beginPath();
			rinkGC.lineTo(e.getX(), e.getY());
			rinkGC.stroke();
		} else if(selected.getText().equals("Text")) {
			rinkGC.fillText("11", e.getX()-9, e.getY()+9);
		}
	}
	
	/**
	 * Method draws on canvas with mouse movement
	 */
	@FXML
	public void CanvasMouseDragged(MouseEvent e) {
		RadioButton selected = (RadioButton) RinkGroup.getSelectedToggle();
		if(selected.getText().equals("Line")) {
			rinkGC.lineTo(e.getX(), e.getY());
			rinkGC.stroke();
		}
	}
	
	/**
	 * Method changes selected color
	 */
	@FXML
	public void RinkCPColorChange() {
		rinkGC.setStroke(RinkCP.getValue());
	}
	
	/**
	 * Method updates line width
	 */
	@FXML
	public void RinkSliderDropped() {
		rinkGC.setLineWidth(RinkSlider.getValue());
	}
}


