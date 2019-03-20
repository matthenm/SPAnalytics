package application;
import java.util.ArrayList;
import java.util.List;

public class Clip {

	private String time;
	private String title;
	private List<String> players;
	private List<DrawnObject> rinkDiagram;
	
	public Clip() {
		//required for DB
	}
	
	public Clip(String time, String title, List<String> players, List<DrawnObject> rinkDiagram) {
		this.time = time;
		this.title = title;
		this.players = players;
		this.rinkDiagram = rinkDiagram;
	}
	
	public Clip(String time, String title) {
		this.time = time;
		this.title = title;
		players = new ArrayList<String>();
		rinkDiagram = new ArrayList<DrawnObject>();
	}
	
	public void addPlayer(String p) {
		players.add(p);
	}
	
	public void addPlayer(List<String> p) {
		players.addAll(p);
	}
	
	public List<DrawnObject> getRinkDiagram() {
		return rinkDiagram;
	}
	
	public void setRinkDiagram(List<DrawnObject> drawing) {
		rinkDiagram = drawing;
	}
	
	public List<String> getPlayers() {
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
