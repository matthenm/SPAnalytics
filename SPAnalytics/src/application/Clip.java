package application;
import java.util.ArrayList;

public class Clip {

	private String time;
	private String title;
	private ArrayList<String> players;
	private ArrayList<DrawnObject> rinkDiagram;
	
	public Clip(String time, String title) {
		this.time = time;
		this.title = title;
		players = new ArrayList<String>();
		rinkDiagram = new ArrayList<DrawnObject>();
	}
	
	public void addPlayer(String p) {
		players.add(p);
	}
	
	public void addPlayer(ArrayList<String> p) {
		players.addAll(p);
	}
	
	public ArrayList<DrawnObject> getRinkDiagram() {
		return rinkDiagram;
	}
	
	public void setRinkDiagram(ArrayList<DrawnObject> drawing) {
		rinkDiagram = drawing;
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
