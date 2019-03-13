package application;

import java.util.ArrayList;

public class Game {
 public String gameName;
 public TeamScore offense;
 public TeamScore defense;
 public String finalScore;

public Game() {}

public Game(String gameName, TeamScore offense, TeamScore defense, String finalScore) {
	this.gameName = gameName;
	this.offense = offense;
	this.defense = defense;
	this.finalScore = finalScore;
}
 
	
}
