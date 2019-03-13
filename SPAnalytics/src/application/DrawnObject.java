package application;

import java.util.ArrayList;

public class DrawnObject {
	private ArrayList<Double> xPoss, yPoss;
	private String text;
	private boolean hasText;
	
	public DrawnObject(double xPos, double yPos) {
		this.xPoss = new ArrayList<Double>();
		this.yPoss = new ArrayList<Double>();
		xPoss.add(xPos);
		yPoss.add(yPos);
		hasText = false;
	}
	
	public DrawnObject(double xPos, double yPos, String text) {
		this(xPos, yPos);
		this.text = text;
		hasText = true;
	}

	public double getxPos(int i) {
		return xPoss.get(i);
	}

	public double getyPos(int i) {
		return yPoss.get(i);
	}
	
	public void addXY(double x, double y) {
		xPoss.add(x);
		yPoss.add(y);
	}
	
	public int getSize() {
		return xPoss.size();
	}
	
	public double getLastX() {
		return xPoss.get(xPoss.size()-1);
	}
	
	public double getLastY() {
		return yPoss.get(yPoss.size()-1);
	}
	
	public void removeLast() {
		xPoss.remove(xPoss.size()-1);
		yPoss.remove(yPoss.size()-1);
	}

	public String getText() {
		if(hasText) {
			return text;
		} else {
			return null;
		}
		
	}
}
