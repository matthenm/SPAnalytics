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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.sun.javafx.css.Style;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
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
	@FXML
	private JFXComboBox<String>		users;
	@FXML
	private JFXPasswordField		adminPass;


	//netChart variables
	@FXML private Canvas AwayNetChartCanvas;
	@FXML private Canvas HomeNetChartCanvas;
	private Canvas netChartCanvas;
	private GraphicsContext awayGC;
	private GraphicsContext homeGC;
	private int ovalWidth;

	private ArrayList<DrawnObject> homeNetChartItems = new ArrayList<DrawnObject>();
	private int homeNetChartIndex = 0;
	private ArrayList<DrawnObject> awayNetChartItems = new ArrayList<DrawnObject>();
	private int awayNetChartIndex = 0;

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
	@FXML private ListView<String> PlayerList;
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
	private final String	PLAYER_CARD				= "/view/SPAnalytics-playerCard.FXML";
	private final String	GOALIE_CARD				= "/view/SPAnalytics-goalieCard.fxml";
	private final String	GOALIE_CARD_PERCENT		= "/view/SPAnalytics-goalieCardPercent.fxml";
	private final String	ADMIN_SCORINGCHANCES	= "/view/Admin_ScoringChances.fxml";
	private final String	ADMIN_RINKDIAGRAM		= "/view/Admin_RinkDiagram.fxml";
	private final String	ADMIN_NETCHART			= "/view/Admin_NetChart.fxml";
	private final String	ADMIN_HOME				= "/view/AdminHome.fxml";
	private final String	CSS						= "/view/application.css";

	//Database connection
	Model m = new Model();

	/**
	 * This is the method that will allow this Controller class to
	 * load new FXML files. 
	 */
	public void setPrimaryStage(Stage inStage) {
		primaryStage = inStage;
		//connect to database
		m.makeDatabase();
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

			//Load the proper player

			JFXTextArea textArea = (JFXTextArea) parent.lookup("#playerInfo");


			String jerseyNo = null;
			if (newScene.equals(GOALIE_CARD)) {
				jerseyNo = m.getJerseyNo("Ryan Larkin");
				//Get the player stats needed
				HashMap<String, HashMap> map = m.getPlayerStats(jerseyNo);
				//make the observable list
				ObservableList<GoalieModel> data = makeGoalieTable(map);
				//find the table needed to be added to
				TableView<GoalieModel> tbData = (TableView<GoalieModel>) parent.lookup("#tbData");
				//add the items to be updated
				tbData.setItems(data);
				makeGoalieCols(tbData); //create the columns


			} else if (newScene.equals(PLAYER_CARD)) {
				jerseyNo = m.getJerseyNo("Alec Mahalak"); //WILL NEED TO CHANGE TO ACCOMODATE MORE PLAYERS
				//Get the player stats
				HashMap<String, HashMap> map = m.getPlayerStats(jerseyNo);
				//make the observable list
				ObservableList<MemberModel> data = makeMemberTable(map);
				//find the table needed to be added to
				TableView<MemberModel> tbData = (TableView<MemberModel>) parent.lookup("#tbData");
				//add the items to be updated
				tbData.setItems(data);
				makeMemberCols(tbData); //create the columns

			}

			if (textArea != null && jerseyNo != null) {

				HashMap playerInfo = m.getPlayer(jerseyNo); //based on jersey no
				Object birthDate = playerInfo.get("birthDate");
				Object name = playerInfo.get("name");
				Object height = playerInfo.get("height");
				Object weight = playerInfo.get("weight");
				Object homeTown = playerInfo.get("homeTown");
				Object position = m.getPosition(jerseyNo); //based on jersey no
				textArea.setText("#"+jerseyNo+" "+name+" "+position+" Height: "+height+"\nWeight: "+weight+" Born: "+birthDate+"\n"+homeTown);
			}
			scene = new Scene(parent, 600, 400);
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
		if(newScene.equals(ADMIN_NETCHART) || newScene.equals(ADMIN_SCORINGCHANCES)) {
			try {
				netChartCanvas = HomeNetChartCanvas;

				homeGC = HomeNetChartCanvas.getGraphicsContext2D();
				homeGC.setStroke(Color.color(.77, .13, .2));
				homeGC.setLineWidth(7);
				homeNetChartItems = new ArrayList<DrawnObject>();
				homeNetChartIndex = 0;

				awayGC = AwayNetChartCanvas.getGraphicsContext2D();
				awayGC.setStroke(Color.color(.77, .13, .2));
				awayGC.setLineWidth(7);
				awayNetChartItems = new ArrayList<DrawnObject>();
				awayNetChartIndex = 0;

				if(newScene.equals(ADMIN_NETCHART)) {
					ovalWidth = 40;
				} else if(newScene.equals(ADMIN_SCORINGCHANCES)) {
					ovalWidth = 20;
				}

			} catch(Exception e) {}
		}

		if(newScene.equals(ADMIN_RINKDIAGRAM)) {
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
						PlayerList.getSelectionModel().clearSelection();
						for(String player : clips.get(index).getPlayers()) {
							PlayerList.getSelectionModel().select(player);
						}

					}

				});

				PlayerList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				PlayerList.getItems().add("Player 1");
				PlayerList.getItems().add("Player 2");
				PlayerList.getItems().add("Player 3");
				for(int i = 4; i <= 20; i++) {
					PlayerList.getItems().add("Player " + i);
				}
			} catch(Exception e) {}
		}
	}

	/**
	 * Helper method to copy rink diagram from Clip object to the drawList
	 */
	private void copyDrawList(List<DrawnObject> list) {
		drawList = new ArrayList<DrawnObject>();
		drawList.addAll(list);
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
	 * Opens NCHC link when clicked
	 */
	@FXML
	public void OpenGameButtonClicked() {
		
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
	 * This is the method that will go to the player card scene.
	 */
	@FXML
	public void GoalieTeamProfileClicked() {
		loadScene(TEAM_PROFILE);
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
		homeGC.setStroke(Color.color(.77, .13, .2));
		awayGC.setStroke(Color.color(.77, .13, .2));
	}

	/**
	 * This is the method that will change the color of the circle to green
	 */
	@FXML
	public void setColorGreen() {
		homeGC.setStroke(Color.GREEN);
		awayGC.setStroke(Color.GREEN);
	}

	/**
	 * This is the method that will draw circles Net chart when mouse released
	 */
	@FXML
	public void drawCircle(MouseEvent e) {
		if(netChartCanvas == HomeNetChartCanvas) {
			Point p1 = new Point(e.getX()-(ovalWidth/2), e.getY()-(ovalWidth/2), homeGC.getStroke());
			homeGC.strokeOval(p1.getX(), p1.getY(), ovalWidth, ovalWidth);
			DrawnObject oval = new DrawnObject(p1, p1.getColor(), ovalWidth);
			homeNetChartItems.add(oval);

			int xOff = 2*(int)(Math.log10(Math.max(1, homeNetChartIndex))+1);
			Point p2 = new Point(e.getX()-(3+xOff), e.getY()+5, homeGC.getFill());
			homeGC.fillText(""+ ++homeNetChartIndex, p2.getX(), p2.getY());
			DrawnObject number = new DrawnObject(p2, p1.getColor(), 0, ""+homeNetChartIndex);
			homeNetChartItems.add(number);
		} else if(netChartCanvas == AwayNetChartCanvas) {
			Point p1 = new Point(e.getX()-(ovalWidth/2), e.getY()-(ovalWidth/2), awayGC.getStroke());
			awayGC.strokeOval(p1.getX(), p1.getY(), ovalWidth, ovalWidth);
			DrawnObject oval = new DrawnObject(p1, p1.getColor(), ovalWidth);
			awayNetChartItems.add(oval);

			int xOff = 2*(int)(Math.log10(Math.max(1, awayNetChartIndex))+1);
			Point p2 = new Point(e.getX()-(3+xOff), e.getY()+5, awayGC.getFill());
			awayGC.fillText(""+ ++awayNetChartIndex, p2.getX(), p2.getY());
			DrawnObject number = new DrawnObject(p2, p1.getColor(), 0, ""+awayNetChartIndex);
			awayNetChartItems.add(number);
		}
	}

	/**
	 * Method changes highlighted canvas to home
	 */
	@FXML
	public void NetChartHomeSelected() {
		netChartCanvas = HomeNetChartCanvas;
	}

	/**
	 * Method changes highlighted canvas to away
	 */
	@FXML
	public void NetChartAwaySelected() {
		netChartCanvas = AwayNetChartCanvas;
	}

	/**
	 * Method undos an item on the NetChartScene
	 */
	@FXML
	public void NetChartUndoPressed() {
		if(netChartCanvas == HomeNetChartCanvas) {
			Color original = (Color) homeGC.getStroke();
			homeGC.clearRect(0, 0, netChartCanvas.getWidth(), netChartCanvas.getHeight());
			if(homeNetChartItems.size() < 2) return;

			homeNetChartItems.remove(homeNetChartItems.size()-1);
			homeNetChartItems.remove(homeNetChartItems.size()-1);
			drawOvals(homeNetChartItems, homeGC);
			homeNetChartIndex--;
			homeGC.setStroke(original);
		} else if(netChartCanvas == AwayNetChartCanvas) {
			Color original = (Color) awayGC.getStroke();
			awayGC.clearRect(0, 0, netChartCanvas.getWidth(), netChartCanvas.getHeight());
			if(awayNetChartItems.size() < 2) return;

			awayNetChartItems.remove(awayNetChartItems.size()-1);
			awayNetChartItems.remove(awayNetChartItems.size()-1);
			drawOvals(awayNetChartItems, awayGC);
			awayNetChartIndex--;
			awayGC.setStroke(original);
		}


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
				for(int xy = 0; xy < obj.size(); xy++) {
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

	@FXML
	/**
	 * Method saves the home chart
	 */
	public void NetChartSaveHomePressed() {
		for(DrawnObject obj : homeNetChartItems) {
			if(obj.getText() != null) {
				System.out.println(obj.getText());
			} else {
				Point p = obj.getLastPoint();
				System.out.print("(" + p.getX() + ", " + p.getY() + "), Color: " + p.getColor() + ", Width: " + obj.getWidth() + ", Text: ");
			}
		}
	}

	@FXML
	/**
	 * Method saves the away chart
	 */
	public void NetChartSaveAwayPressed() {
		for(DrawnObject obj : awayNetChartItems) {
			if(obj.getText() != null) {
				System.out.println(obj.getText());
			} else {
				Point p = obj.getLastPoint();
				System.out.print("(" + p.getX() + ", " + p.getY() + "), Color: " + p.getColor() + ", Width: " + obj.getWidth() + ", Text: ");
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
		ObservableList<String> players = PlayerList.getSelectionModel().getSelectedItems();
		ArrayList<String> playersAL = new ArrayList<String>();
		for(String player : players) {
			playersAL.add(player);
		}
		clips.get(index).addPlayer(playersAL);
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

	/**
	 * Method to initialize table for a player
	 */
	@FXML
	public ObservableList<MemberModel> makeMemberTable(HashMap<String, HashMap> playerData) {
		ObservableList<MemberModel> data = FXCollections.observableArrayList();;

		Iterator it = playerData.entrySet().iterator();
		while (it.hasNext()) {
			MemberModel model = new MemberModel();
			Map.Entry pair = (Map.Entry)it.next();
			HashMap values = (HashMap) pair.getValue();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			Iterator stats = values.entrySet().iterator();
			//each game would be a new row
			while (stats.hasNext()) {
				Map.Entry details = (Map.Entry)stats.next();
				System.out.println(details.getKey() + " = " + details.getValue());
				//check what value it is and make the correct value
				if (details.getKey() == "PPA") {
					model.setPPA(details.getValue());
				}
				else if (details.getKey() == "A") {
					model.setA(details.getValue());
				}
				else if (details.getKey() == "PPG") {
					model.setPPG(details.getValue());
				}
				else if (details.getKey() == "G") {
					model.setG(details.getValue());
				}
				else if (details.getKey() == "GP") {
					model.setGP(details.getValue());
				}
				else if (details.getKey() == "SOG") {
					model.setSOG(details.getValue());
				}
				else if (details.getKey() == "percent") {
					model.setPercent(details.getValue());
				}
				else if (details.getKey() == "PTS") {
					model.setPTS(details.getValue());
				}
				else if (details.getKey() == "PROD") {
					model.setPROD(details.getValue());
				}
				else if (details.getKey() == "SHG") {
					model.setSHG(details.getValue());
				}
				else if (details.getKey() == "GWG") {
					model.setGWG(details.getValue());
				}
				else if (details.getKey() == "winsOrLosses") {
					model.setPlusMinus(details.getValue());
				}
				else if (details.getKey() == "season") {
					model.setSeason(details.getValue());
				}
				else if (details.getKey() == "GTG") {
					model.setGTG(details.getValue());
				}
				else if (details.getKey() == "TOIG") {
					model.setTOI(details.getValue());
				}
				stats.remove();
			}
			it.remove(); // avoids a ConcurrentModificationException
			data.add(model);

			//here model goes out of scope
		}
		return data;
	}
	/**
	 * Method to initialize table for a player
	 */
	@FXML
	public ObservableList<GoalieModel> makeGoalieTable(HashMap<String, HashMap> playerData) {
		ObservableList<GoalieModel> data = FXCollections.observableArrayList();

		Iterator it = playerData.entrySet().iterator();
		while (it.hasNext()) {
			GoalieModel model = new GoalieModel();
			Map.Entry pair = (Map.Entry)it.next();
			HashMap values = (HashMap) pair.getValue();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			Iterator stats = values.entrySet().iterator();
			//each game would be a new row
			while (stats.hasNext()) {
				Map.Entry details = (Map.Entry)stats.next();
				System.out.println(details.getKey() + " = " + details.getValue());
				//check what value it is and make the correct value
				if (details.getKey() == "GP") {
					TableColumn<GoalieModel,String> nameColumn=new TableColumn<>("GP");
					model.setGP(details.getValue());
				}
				else if (details.getKey() == "season") {
					model.setSeason(details.getValue());
				}
				else if (details.getKey() == "SV") {
					model.setSV(details.getValue());
				}
				else if (details.getKey() == "T") {
					model.setT(details.getValue());
				}
				else if (details.getKey() == "GAA" ) {
					model.setGAA(details.getValue());
				}
				else if (details.getKey() == "W") {
					model.setW(details.getValue());
				}
				else if (details.getKey() == "GA") {
					model.setGA(details.getValue());
				}
				else if (details.getKey() == "SA") {
					model.setSA(details.getValue());
				}
				else if (details.getKey() == "SVpercent") {
					model.setSVpercent(details.getValue());
				}
				else if (details.getKey() == "L") {
					model.setL(details.getValue());
				}
				else if (details.getKey() == "SO") {
					model.setSO(details.getValue());
				}

				stats.remove();
			}
			it.remove(); // avoids a ConcurrentModificationException
			data.add(model);

			//here model goes out of scope
		}
		return data;


	}

	public void makeGoalieCols(TableView<GoalieModel> tbData) {

		TableColumn<GoalieModel, String> season = new TableColumn<GoalieModel, String>("Season");
		season.setCellValueFactory(new PropertyValueFactory("season"));
		TableColumn<GoalieModel, String> GP = new TableColumn<GoalieModel, String>("GP");
		GP.setCellValueFactory(new PropertyValueFactory("GP"));
		TableColumn<GoalieModel, String> W = new TableColumn<GoalieModel, String>("W");
		W.setCellValueFactory(new PropertyValueFactory("W"));
		TableColumn<GoalieModel, String> L = new TableColumn<GoalieModel, String>("L");
		L.setCellValueFactory(new PropertyValueFactory("L"));
		TableColumn<GoalieModel, String> T = new TableColumn<GoalieModel, String>("T");
		T.setCellValueFactory(new PropertyValueFactory("T"));
		TableColumn<GoalieModel, String> GA = new TableColumn<GoalieModel, String>("GA");
		GA.setCellValueFactory(new PropertyValueFactory("GA"));
		TableColumn<GoalieModel, String> GAA = new TableColumn<GoalieModel, String>("GAA");
		GAA.setCellValueFactory(new PropertyValueFactory("GAA"));
		TableColumn<GoalieModel, String> SA = new TableColumn<GoalieModel, String>("SA");
		SA.setCellValueFactory(new PropertyValueFactory("SA"));
		TableColumn<GoalieModel, String> SV = new TableColumn<GoalieModel, String>("SV");
		SV.setCellValueFactory(new PropertyValueFactory("SV"));
		TableColumn<GoalieModel, String> SVpercent = new TableColumn<GoalieModel, String>("SV%");
		SVpercent.setCellValueFactory(new PropertyValueFactory("SVpercent"));
		TableColumn<GoalieModel, String> SO = new TableColumn<GoalieModel, String>("SO");
		SO.setCellValueFactory(new PropertyValueFactory("SO"));

		tbData.getColumns().setAll(season,GP,W,L,T,GA,GAA,SA,SV,SVpercent,SO);
	}

	public void makeMemberCols(TableView<MemberModel> tbData) {

		TableColumn<MemberModel, String> season = new TableColumn<MemberModel, String>("Season");
		season.setCellValueFactory(new PropertyValueFactory("season"));
		TableColumn<MemberModel, String> GP = new TableColumn<MemberModel, String>("GP");
		GP.setCellValueFactory(new PropertyValueFactory("GP"));
		TableColumn<MemberModel, String> A = new TableColumn<MemberModel, String>("A");
		A.setCellValueFactory(new PropertyValueFactory("A"));
		TableColumn<MemberModel, String> PPG = new TableColumn<MemberModel, String>("PPG");
		PPG.setCellValueFactory(new PropertyValueFactory("PPG"));
		TableColumn<MemberModel, String> G = new TableColumn<MemberModel, String>("G");
		G.setCellValueFactory(new PropertyValueFactory("G"));
		TableColumn<MemberModel, String> SOG = new TableColumn<MemberModel, String>("SOG");
		SOG.setCellValueFactory(new PropertyValueFactory("SOG"));
		TableColumn<MemberModel, String> percent = new TableColumn<MemberModel, String>("\'%");
		percent.setCellValueFactory(new PropertyValueFactory("percent"));
		TableColumn<MemberModel, String> PTS = new TableColumn<MemberModel, String>("PTS");
		PTS.setCellValueFactory(new PropertyValueFactory("PTS"));
		TableColumn<MemberModel, String> PROD = new TableColumn<MemberModel, String>("PROD");
		PROD.setCellValueFactory(new PropertyValueFactory("PROD"));
		TableColumn<MemberModel, String> SHG = new TableColumn<MemberModel, String>("SHG");
		SHG.setCellValueFactory(new PropertyValueFactory("SHG"));
		TableColumn<MemberModel, String> GWG = new TableColumn<MemberModel, String>("GWG");
		GWG.setCellValueFactory(new PropertyValueFactory("GWG"));
		TableColumn<MemberModel, String> winsOrLosses = new TableColumn<MemberModel, String>("+/-");
		winsOrLosses.setCellValueFactory(new PropertyValueFactory("plusMinus"));
		TableColumn<MemberModel, String> GTG = new TableColumn<MemberModel, String>("GTG");
		GTG.setCellValueFactory(new PropertyValueFactory("GTG"));
		TableColumn<MemberModel, String> TOIG = new TableColumn<MemberModel, String>("TOI/G");
		TOIG.setCellValueFactory(new PropertyValueFactory("TOI"));
		tbData.getColumns().setAll(season,GP,A,PPG,G,SOG,percent,PTS,PROD,SHG,GWG, winsOrLosses, GTG, TOIG);
	}

	public  HashMap<String, HashMap<String, Object>> readMemberCols(TableView<MemberModel> tbData) {
		HashMap<String, Object> values = new HashMap<String, Object>();
		HashMap<String, HashMap<String, Object>> allValues = new  HashMap<String, HashMap<String, Object>>();
		ObservableList<TableColumn<MemberModel, ?>> columns = tbData.getColumns();

		for (Object row : tbData.getItems()) {

			for (TableColumn column : columns) {
				System.out.println(column.getText());
				values.put(column.getText(), (Object) column.
						getCellObservableValue(row).
						getValue());

			}
			Object season = values.get("Season");
			allValues.put(season.toString(), values);
		}

		Iterator stats = allValues.entrySet().iterator();
		//each game would be a new row
		while (stats.hasNext()) {
			Map.Entry details = (Map.Entry)stats.next();
			System.out.println(details.getKey() + " = " + details.getValue());

		}
		return allValues;

	}
	//get
	public  HashMap<String, HashMap<String, Object>> readGoalieCols(TableView<GoalieModel> tbData) {
		HashMap<String, Object> values = new HashMap<String, Object>();
		HashMap<String, HashMap<String, Object>> allValues = new  HashMap<String, HashMap<String, Object>>();
		ObservableList<TableColumn<GoalieModel, ?>> columns = tbData.getColumns();

		for (Object row : tbData.getItems()) {

			for (TableColumn column : columns) {
				System.out.println(column.getText());
				values.put(column.getText(), (Object) column.
						getCellObservableValue(row).
						getValue());

			}
			Object season = values.get("Season");
			allValues.put(season.toString(), values);
		}

		Iterator stats = allValues.entrySet().iterator();
		//each game would be a new row
		while (stats.hasNext()) {
			Map.Entry details = (Map.Entry)stats.next();
			System.out.println(details.getKey() + " = " + details.getValue());

		}
		return allValues;
	}
	//delete player
	public void deletePlayer(int jerseyNo, TableView<?> tbData,JFXTextArea textArea) {
		tbData.getItems().clear();
		textArea.clear();
		m.deletePlayerByJerseyNo(jerseyNo);

	}
}


