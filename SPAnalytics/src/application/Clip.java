package application;
import java.util.ArrayList;

public class Clip {

	private String time;
	private String title;
	private ArrayList<String> players;
	
	public Clip(String time, String title) {
		this.time = time;
		this.title = title;
	}
	
	public void addPlayer(String p) {
		players.add(p);
	}
	
	public void addPlayer(ArrayList<String> p) {
		players.addAll(p);
	}
	
	public ArrayList<String> getPlayers() {
		return players;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
}
