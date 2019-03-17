package application;


import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.sun.javafx.css.Style;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sun.misc.GC;

public class Controller {

	@FXML BorderPane bp;
	
	//login scene variables
	@FXML
	private JFXButton				loginButton;
	@FXML
	private ChoiceBox<String>		rosterList;
	
    
	//netChart variables
	@FXML private Canvas netChartCanvas;
	private GraphicsContext gc1;
	private ArrayList<DrawnObject> netChartItems = new ArrayList<DrawnObject>();
	private int netChartIndex = 0;

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
	@FXML private TextArea RinkDiagramText;
	private GraphicsContext rinkGC;
	private ArrayList<DrawnObject> drawList = new ArrayList<DrawnObject>();
	private DrawnObject line;
	
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
			Pane root = (Pane) parent;
			scene = new Scene(new Group(root), 600, 400);
			
			//setting scaling
			Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
			double width = resolution.getWidth();
			double height = resolution.getHeight();
			double w = width/bp.getPrefWidth();
			double h = height/bp.getPrefHeight();
			Scale scale = new Scale(w,h,0,0);
			root.getTransforms().add(scale);
			
			//Setting a Scene KeyListener
			scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent event) {
					switch (event.getCode()) {
					case S: 
						getTime();
					break;
					}
				}
			});
			
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
			gc1.setStroke(Color.color(.77, .13, .2));
			gc1.setLineWidth(7);
		} catch(Exception e) {}
		
		try {
			RinkCP.setValue(Color.BLACK);
			rinkGC = RinkCanvas.getGraphicsContext2D();
			rinkGC.setStroke(RinkCP.getValue());
			rinkGC.setFill(RinkCP.getValue());
			rinkGC.setLineWidth(RinkSlider.getValue());
			rinkGC.setFont(new Font("Verdana", 24));
			TimeStamps.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					int index = TimeStamps.getSelectionModel().getSelectedIndex();
					TimeStampNotes.setText(clips.get(index).getTitle());
					copyDrawList(clips.get(index).getRinkDiagram());
					rinkGC.clearRect(0, 0, RinkCanvas.getWidth(), RinkCanvas.getHeight());
					drawLinesAndNumbers(drawList, rinkGC);
				}
				
			});
		} catch(Exception e) {}
	}

	/**
	 * Helper method to copy rink diagram from Clip object to the drawList
	 */
	private void copyDrawList(ArrayList<DrawnObject> dl) {
		drawList = new ArrayList<DrawnObject>();
		drawList.addAll(dl);
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
	private void getTime() {
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
		if(rosterList.getValue().equals("Ryan Larkin")) {
			loadScene(GOALIE_HOME);
		}else if(rosterList.getValue().equals("ADMIN")) {
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setTitle("Admin Password");
			dialog.setHeaderText("Enter your password:");
			dialog.setContentText("Password:");
			Optional<String>result = dialog.showAndWait();
			if (result.isPresent()){
				if(result.get().equals("test")) {
					loadScene(ADMIN_HOME);
				}
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
		gc1.setStroke(Color.color(.77, .13, .2));
	}
	
	/**
	 * This is the method that will change the color of the circle to green
	 */
	@FXML
	public void setColorGreen() {
		gc1.setStroke(Color.GREEN);
	}
	
	/**
	 * This is the method that will draw circles when mouse released
	 */
	@FXML
	public void drawCircle(MouseEvent e) {
		Point p1 = new Point(e.getX()-20, e.getY()-20, gc1.getStroke());
		gc1.strokeOval(p1.getX(), p1.getY(), 50, 50);
		DrawnObject oval = new DrawnObject(p1, p1.getColor(), 50);
		netChartItems.add(oval);
				
		Point p2 = new Point(e.getX()+3, e.getY()+10, gc1.getFill());
		gc1.fillText(""+ ++netChartIndex, p2.getX(), p2.getY());
		DrawnObject number = new DrawnObject(p2, p1.getColor(), 0, ""+netChartIndex);
		netChartItems.add(number);
	}
	
	/**
	 * Method undos an item on the NetChartScene
	 */
	@FXML
	public void NetChartUndoPressed() {
		Color original = (Color) gc1.getStroke();
		gc1.clearRect(0, 0, netChartCanvas.getWidth(), netChartCanvas.getHeight());
		if(netChartItems.size() < 2) return;
		
		netChartItems.remove(netChartItems.size()-1);
		netChartItems.remove(netChartItems.size()-1);
		drawOvals(netChartItems, gc1);
		netChartIndex--;
		gc1.setStroke(original);
		
	}
	
	/**
	 * Helper drawing method for any canvas using ovals
	 */
	private static void drawOvals(ArrayList<DrawnObject> d, GraphicsContext gc) {
		for(int i = 0; i < d.size(); i++) {
			DrawnObject obj = d.get(i);
			if(obj.getText() == null) {
				Point p = obj.getPoint(0);
				gc.setStroke(p.getColor());
				gc.strokeOval(p.getX(), p.getY(), obj.getWidth(), obj.getWidth());
			} else {
				Point p = obj.getPoint(0);;
				gc.fillText(obj.getText(), p.getX(), p.getY());
			}
		}
	}
	
	/**
	 * Helper method to draw lines and numbers
	 */
	private static void drawLinesAndNumbers(ArrayList<DrawnObject> d, GraphicsContext gc) {
		for(int i = 0; i < d.size(); i++) {
			DrawnObject obj = d.get(i);
			if(obj.getText() == null) {
				gc.beginPath();
				gc.setLineWidth(obj.getWidth());
				for(int xy = 0; xy < obj.getSize(); xy++) {
					Point p = obj.getPoint(xy);
					gc.setStroke(p.getColor());
					gc.lineTo(p.getX(), p.getY());
					gc.stroke();
				}
			} else {
				Point p = obj.getPoint(0);
				gc.setFill(p.getColor());
				gc.fillText(obj.getText(), p.getX(), p.getY());
			}
		}
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
	 * Method saves diagram to current clip
	 */
	@FXML
	public void SaveRinkClicked() {
		int index = TimeStamps.getSelectionModel().getSelectedIndex();
		if (index == -1) return;
		clips.get(index).setRinkDiagram(drawList);
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Saved");
		alert.setHeaderText("Diagram Saved");
		alert.setContentText("Rink Diagram saved to clip");
		alert.show();
	}

	/**
	 * Method Saves the title of selected clip
	 */
	@FXML
	public void SaveNotesButtonClicked() {
		int index = TimeStamps.getSelectionModel().getSelectedIndex();
		if (index == -1) return;
		clips.get(index).setTitle(TimeStampNotes.getText());
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Saved");
		alert.setHeaderText("Title Saved");
		alert.setContentText("Title of clip saved");
		alert.show();
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
			line = new DrawnObject(e.getX(), e.getY(), rinkGC.getStroke(), rinkGC.getLineWidth());
			rinkGC.beginPath();
			rinkGC.lineTo(line.getLastPoint().getX(), line.getLastPoint().getY());
			rinkGC.stroke();
			drawList.add(line);
		} else if(selected.getText().equals("Text")) {
			DrawnObject text = new DrawnObject(e.getX()-12, e.getY()+12, rinkGC.getFill(), 0, RinkDiagramText.getText());
			rinkGC.fillText(text.getText(), text.getLastPoint().getX(), text.getLastPoint().getY());
			drawList.add(text);
		}
	}
	
	/**
	 * Method draws on canvas with mouse movement
	 */
	@FXML
	public void CanvasMouseDragged(MouseEvent e) {
		RadioButton selected = (RadioButton) RinkGroup.getSelectedToggle();
		if(selected.getText().equals("Line")) {
			line.addPoint(e.getX(), e.getY(), rinkGC.getStroke());
			rinkGC.lineTo(line.getLastPoint().getX(), line.getLastPoint().getY());
			rinkGC.stroke();
		}
	}
	
	/**
	 * Method undos the last drawing action
	 */
	@FXML
	public void UndoRinkClicked() {
		rinkGC.clearRect(0, 0, RinkCanvas.getWidth(), RinkCanvas.getHeight());
		if(drawList.size() < 1) return;
		
		drawList.remove(drawList.size()-1);
		drawLinesAndNumbers(drawList, rinkGC);
		
		rinkGC.setStroke(RinkCP.getValue());
		rinkGC.setFill(RinkCP.getValue());
		rinkGC.setLineWidth(RinkSlider.getValue());
		//drawList.clear();
	}
	
	/**
	 * Method changes selected color
	 */
	@FXML
	public void RinkCPColorChange() {
		rinkGC.setStroke(RinkCP.getValue());
		rinkGC.setFill(RinkCP.getValue());
	}
	
	/**
	 * Method updates line width
	 */
	@FXML
	public void RinkSliderDropped() {
		rinkGC.setLineWidth(RinkSlider.getValue());
	}
}


