package com.Centipede;

public class Spider extends Thing{
	
	 public Spider(int x, int y) {
		super(x, y);
		loadImage("src/resources/spider.png");
		getImageDimensions();
	}
	 
	
	private int dx = 1;
	private int dy = 1;
	//public int lives = 1;
	
	public void move() {
		if(x < 0 || x > App.WIDTH || y > 400 || y < 100)
		{
			dx = dx;
			dy = -dy;
			if(x > 400){
				dx = - dx;
			}
			else if(x < 0) {
				dx = -dx;
			}
		}
		x += dx;
		y += dy;
	}	 
	
	public void moveOffScreen() {
		x = 601;
		y = 601;
	}
}
