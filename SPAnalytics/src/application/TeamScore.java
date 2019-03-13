package application;

public class TeamScore {
public String teamName;
public int goalsFor;
public int goalsAgainst;
public int shotsFor;
public int shotsAgainst;

public TeamScore() {}

public TeamScore(String teamName, int goalsFor, int goalsAgainst, int shotsFor, int shotsAgainst) {
	this.teamName = teamName;
	this.goalsFor = goalsFor;
	this.goalsAgainst = goalsAgainst;
	this.shotsFor = shotsFor;
	this.shotsAgainst = shotsAgainst;
}

}
