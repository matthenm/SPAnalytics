package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {
 public String gameName;
 //game has multiple timestamp, each timestamp can have multiple players (no url)
 //game will have one url (string)
 //timestamp will have the clip (String), name (String)
 //players will have multiple timestamps
 //for every game, has net chart image and scoring chance image
 //offense scoring chance image and defense scoring chance image
 //Points, x, y, width, height, color
 public TeamScore offense;
 public TeamScore defense;
 public String finalScore;
 public Map<String, Timestamp> timestamp= new HashMap<>();
 public String url;
 public String netChart;
 public String scoringChart;

public Game() {}

public Game(String gameName, TeamScore offense, TeamScore defense, String finalScore,
		Map<String, Timestamp> timestamp) {
	this.gameName = gameName;
	this.offense = offense;
	this.defense = defense;
	this.finalScore = finalScore;
	this.timestamp = timestamp;
}
 
	
}
