package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Paint;

public class DrawnObject {
	private List<Point> points;
	private String text;
	private double width;
	private boolean hasText;
	
	public DrawnObject() {
		//required for db
	}
	
	public DrawnObject(List<Point> points, String text, double width, boolean hasText) {
		this.points = points;
		this.text = text;
		this.width = width;
		this.hasText = hasText;
	}
	
	public DrawnObject(double xPos, double yPos, Paint color, double width) {
		points = new ArrayList<Point>();
		Point p = new Point(xPos, yPos, color);
		points.add(p);
		this.width = width;
		hasText = false;
	}
	
	public DrawnObject(Point p, Paint color, double width) {
		points = new ArrayList<Point>();
		points.add(p);
		this.width = width;
		hasText = false;
	}
	
	public DrawnObject(Point p, Paint color, double width, String text) {
		this(p, color, width);
		this.text = text;
		hasText = true;
	}
	
	public DrawnObject(double xPos, double yPos, Paint color, double width, String text) {
		this(xPos, yPos, color, width);
		this.text = text;
		hasText = true;
	}

	public Point getPoint(int i) {
		return points.get(i);
	}
	
	public void addPoint(double x, double y, Paint color) {
		Point p = new Point(x, y, color);
		points.add(p);
	}
	
	public int getSize() {
		return points.size();
	}
	
	public Point getLastPoint() {
		return points.get(points.size()-1);
	}
	
	public void removeLast() {
		points.remove(points.size()-1);
	}

	public String getText() {
		if(hasText) {
			return text;
		} else {
			return null;
		}
	}
	
	public double getWidth() {
		return width;
	}

	public List<Point> getPoints() {
		return points;
	}

	public boolean isHasText() {
		return hasText;
	}
}
