package com.Centipede;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Player extends Thing{

	 private int dx;
	 private int dy;
	 public List<Missile> missiles;
	 
	 public Player(int x, int y) {
		 super(x, y);
		 missiles = new ArrayList<>();
		 loadImage("src/resources/ship.png");
		 getImageDimensions();
	 }
	 
	 public void move() {
	        x = dx;
	        y = dy;
	 }
	 
	 public void fire() {
	        missiles.add(new Missile(x + width, y + height / 2));
	 }
	 
	public void mouseDragged(MouseEvent e) {
		missiles.add(new Missile(x, y));
	}

	public void mouseMoved(MouseEvent e) {
		setdx(e.getX());
		setdy(e.getY());
	}
	
	public void mouseClicked(MouseEvent E) {
		fire();
	}
	 
	 
	 public void setdx(int r) {this.dx = r;}
	 public void setdy(int r) {this.dy = r;}
	 public void setx(int i)  {this.x = i;}
	 public void sety(int i)  {this.y = i;}
	 public List<Missile> getMissiles() {return missiles;}
	 

}
