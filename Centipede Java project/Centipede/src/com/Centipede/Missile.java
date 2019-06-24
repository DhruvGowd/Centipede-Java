package com.Centipede;

public class Missile extends Thing {
	
	public Missile(int x, int y) {
	 super(x, y);
	 loadImage("src/resources/missile.png");
	 //getImageDimensions();
	 setDimensions(6, 10);
	}
 
	public void move() {
     y -= 10;
     visible = (x > App.HEIGHT) ? false : true;
	}
}
