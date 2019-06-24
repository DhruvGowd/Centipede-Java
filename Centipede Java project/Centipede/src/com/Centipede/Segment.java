package com.Centipede;

public class Segment extends Thing{
	
	public Segment(int x, int y) {
		super(x, y);
		loadImage("src/resources/centipede.png");
		getImageDimensions();
	}
	
	public int dx = 2, dy = 1;
	public int hitCnt = 0;
	
	public void move() {
		if((x < 0 || x > 565) && y < 496) {
			dx = -dx;
			y +=  16;
			
		}
		if(y >= 496) {
			//dx = -dx;
			y -= 16;
		}
			x -= dx;
	}
	
	public void hit() {
		hitCnt++;
	}
	
}
