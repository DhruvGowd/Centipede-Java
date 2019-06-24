package com.Centipede;

public class Mushroom extends Thing{
	
	public Mushroom(int x, int y) {
		super(x,y);
		 loadImage("src/resources/mushroom.png");
		 getImageDimensions();
	}
	public int hitCnt = 0;
}
