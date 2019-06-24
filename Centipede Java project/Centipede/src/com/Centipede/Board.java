package com.Centipede;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;
import javafx.util.Pair;
import java.util.HashMap;
import java.util.Map;


public class Board extends JPanel implements  ActionListener {

	//creating a blank cursor
    private BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    private Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

    //other declarations
	private Timer timer;
	private Player player;
	private Spider spider;
	private boolean inGame;
	private List<Mushroom> mushrooms  = new ArrayList<>();
	private Map<Pair<Integer, Integer>, Integer> mushroomMap = new HashMap<>();
	private List<Segment> centipede;
	private int centipedeSize = 15;



	private int spiderHitCnt = 0;
	private int score = 0;
	private final int MAX_HIT_COUNT = 5;
	private int playerLives = 5;
	private int playerHitCnt = 0;
	Robot robot;
	//sounds
	//Sounds shot = new Sounds();
	PlaySound sound = new PlaySound();

	Board() {

		//initialize JFrame and adds listeners
		addMouseMotionListener(new TAdapter());
		addMouseListener(new TAdapter());
		setBackground(Color.BLACK);

		//create Mushroom grid and initialize the centipede
		initMushrooms();
		createCentipede();

		//adds rest of objects and starts the timer
		//hit counters & lives
		try {
		    robot = new Robot();
		} catch (AWTException e) {e.printStackTrace();}
		player = new Player(250,450);
		spider = new Spider(0,150);
		inGame = true;
		timer = new Timer(10, this);
		timer.start();
	}


	private void createCentipede() {
		//initializes the centipede by having an array of segments
		centipede = new ArrayList<>();
		for(int i = 0; i < centipedeSize; i ++) {
			centipede.add(new Segment(304 + 16 * i, 0));
		}
	}

	private void initMushrooms() {
		//randomly generates a grid of mushrooms from top down
		Random r = new Random();
		int numMushrooms = 600;
		int topDist = 48;
		int leftDist = 80;
		int xCoord = 0;
		int yCoord = 0;

		for (int i = 0; i < 14; i++) {//goes through each row 1-15

			int numShrooms = r.nextInt(6);
			for (int j = 0; j < numShrooms; j++) {//goes through each column 1 - 15
				yCoord = i * 32 + topDist;
				//int k = 0;
				do {
					//finds a new point and checks for good placement via a HashMap
					xCoord = r.nextInt(15) * 32 + leftDist;
					//System.out.printf("%d x and %d y for %d line %d\n", xCoord, yCoord, k++, i);
				} while (!checkValidMushroom(xCoord, yCoord));
				mushrooms.add(new Mushroom(xCoord, yCoord));
				if (--numMushrooms == 0) {//breaks when max number of mushrooms has been reached
					i = 15;
					j = 6;
				}
			}
		}
	}

	private boolean checkValidMushroom(int x, int y) {
		Pair<Integer, Integer> p = new Pair<>(x, y);
		if (mushroomMap.containsKey(p)) {//if point has not yet been taken
			return false;
		} else if (mushroomMap.containsKey(new Pair<Integer, Integer>(x-32, y-32))
				|| mushroomMap.containsKey(new Pair<Integer, Integer>(x+32, y-32))) {
			//if the point has mushrooms too close to it diagonally left or right
			return false;
		} else {
			mushroomMap.put(p, 1);
		}
		return true;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if(inGame) {
			doDrawing(g);
		} else {
			gameOver(g);
		}

        Toolkit.getDefaultToolkit().sync();
	}

	private void gameOver(Graphics g) {
		//Game over screen
		String msg = "Game Over";
		Font small = new Font("Helvetica", Font.BOLD, 36);

	    g.setColor(Color.RED);
	    g.setFont(small);
	    g.drawString(msg, 199, App.HEIGHT/2 - 200);
		g.drawString("Score : " + score, 198,  App.HEIGHT/2 -100);
	}

	public void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		//drawing the player
		g2d.drawImage(player.getImage(), player.getX(), player.getY(), this);

		//drawing the mushrooms
		for(Mushroom m : mushrooms) {
			if(m.isVisible()) {
				g2d.drawImage(m.getImage(), m.getX(), m.getY(), this);
			}
		}

		//drawing the missiles
		List<Missile> missiles = player.getMissiles();
		for(Missile missile : missiles) {
			//g2d.drawImage(missile.getImage(), missile.getX(), missile.getY(), this);
			if(missile.isVisible()) {
				g2d.setColor(Color.GREEN);
				g2d.fillRect( missile.getX() - 11, missile.getY() - 10, 6, 10);
			}
		}

		//drawing the spider
		if(spider.isVisible()) {
			g2d.drawImage(spider.getImage(), spider.getX(), spider.getY(), this);
		}

		//drawing the centipede
		for(Segment s : centipede) {
			g2d.setColor(Color.YELLOW);
			g2d.fillOval(s.getX(), s.getY(), 16, 16);
		}

		//draw score & num lives
		g.setColor(Color.WHITE);
		g.drawString("Score : " + score, 510, 25);
		g.drawString("Lives : " + playerLives, 510, 50);
	}

	 @Override
	 public void actionPerformed(ActionEvent e) {
		 //runs whenever timer resets or new event occurred, checks whether or not to
		 //end game or to update all players
		 isPlayerDead();
		 isGameRunning();

	     updatePlayer();
	     updateCentipede();
	     updateMissiles();
		 updateMushrooms();
	     updateSpider();

	     checkCollisions();
	     repaint();
	 }

	 private void isPlayerDead() {
		 if(playerHitCnt > MAX_HIT_COUNT) {
			 inGame = false;
			 playerLives = 0;
		 }
	 }

	 private void isGameRunning() {
		 if(!inGame) { timer.stop(); }
	 }

	 private void updateCentipede() {
		 if(centipede.isEmpty()) {
			 createCentipede();
			 spider = new Spider(0,150);
			 spiderHitCnt = 0;
			 score += 600;
		 }else {
			 for(Segment s : centipede) {
				 s.move();
			 }
		 }
	 }

	 private void updateMushrooms() {
		 for(int i = 0; i < mushrooms.size(); i++) {
			 Mushroom a = mushrooms.get(i);
			 if(!(a.isVisible())) {
				 // Remove mushroom from HashMap
				 mushroomMap.remove(new Pair<Integer, Integer>(a.getX(), a.getY()));
				 mushrooms.remove(i);
			 }
		 }
	 }

	 public void updateSpider() {
		 if(spider.isVisible()) {
			 spider.move();
		 } else {
			 spider.moveOffScreen();
		 }
	 }

	 public void updateMissiles() {

		 List<Missile> missiles = player.getMissiles();

		 for(int i = 0; i < missiles.size(); i++) {
			 Missile missile = missiles.get(i);
			 if(missile.isVisible()) {
				 missile.move();
			 } else {
				 missiles.remove(i);
			 }
		 }
	 }

	 public void updatePlayer() {
		 player.move();
	}

	 public void checkCollisions() {
		 //check if missile hits mushroom
		 List<Missile> missiles = player.getMissiles();
		 for(Missile n : missiles) {
			 Rectangle r1 = n.getBounds();
			 for(Mushroom mush : mushrooms) {
				 Rectangle r2 = mush.getBounds();
				 r2.x += 8;
				 if(r1.intersects(r2)) {
					 mush.hitCnt += 1;
					 if(mush.hitCnt == 3) {
						 score += 5;
						 n.setVisible(false);
						 mush.setVisible(false);
					 } else {
						 score += 1;
						 n.setVisible(false);
					 }
				 }
			 }
		 }

		 //check if Missile hits Centipede
		 //missiles = player.getMissiles();
		 for(Missile m: missiles) {
			 Rectangle rm = m.getBounds();
			 for(int i = 0; i < centipede.size(); i++) {
				 Segment s = centipede.get(i);
				 Rectangle rs = s.getBounds();
				 if(rs.intersects(rm)) {
					s.hit();
					score = (s.hitCnt < 2 ? score +2 : score + 0);
					if(s.hitCnt == 2){
						//split centipede
						score += 5;
						m.setVisible(false);
						centipede.remove(i);
						//mushrooms.add(new Mushroom(s.getX(), s.getY()));
					}else {
						m.setVisible(false);
					}
				 }
			 }
		 }

		 //check if missile hits spider
		 missiles = player.getMissiles();
		 for(Missile m : missiles) {
			 Rectangle mBound = m.getBounds();
			 Rectangle spiderBound = spider.getBounds();
			 if(mBound.intersects(spiderBound)) {
				 spiderHitCnt += 1;
				 if(spiderHitCnt == 2) {
					 m.setVisible(false);
					 spider.setVisible(false);
					 score += 600;
				 } else {
					 score += 100;
					 m.setVisible(false);
				 }
			 }
		 }

		 //check if centipede hits mushroom
		 for(Segment c : centipede) {
			 Rectangle cB = c.getBounds();
			 for(Mushroom m: mushrooms) {
				 Rectangle mB = new Rectangle(m.getX(), m.getY() + 5, m.getWidth(), m.getHeight() - 10);
				 mB.y += 3;
				 if(cB.intersects(mB)) {
					//makes each segment to go downs
					c.y += 16;
					c.dx = -c.dx;
				 }
			 }
		 }

		 //check if Centipede hits Player
		 Rectangle playerBounds = player.getBounds();
		 for(Segment s : centipede) {
			 Rectangle sB = s.getBounds();
			 if(playerBounds.intersects(sB)) {
				 playerLives -= 1;
				 playerHitCnt += 1;
				 if(playerHitCnt > MAX_HIT_COUNT) {
					// System.out.println(playerLives+" "+ x);
					 player.setVisible(false);
					 inGame = false;
				 }else {
						player.x = 707;
						player.y = 708;
						Point location = getLocationOnScreen();
						robot.mouseMove(location.x + 250, location.y + 425);
						player = new Player(300, 400);
						regenMushrooms();
					}
			 }
		 }

		 //check if Spider hits Player
		 playerBounds = player.getBounds();
		 Rectangle spiderBounds = spider.getBounds();
		 if(spiderBounds.intersects(playerBounds)) {
			playerLives -= 1;
			playerHitCnt += 1;
			if(playerLives > MAX_HIT_COUNT) {
				//System.out.println(playerLives+" "+ y);
				player.setVisible(false);
				spider.setVisible(false);
			}else {
				regenMushrooms();
				centipede.clear();
				createCentipede();
				player.x = 707;
				player.y = 708;
				Point location = getLocationOnScreen();
				robot.mouseMove(location.x + 250, location.y + 425);
				player = new Player(300, 400);
			}
		}
	 }

	 public void regenMushrooms() {
		 for(Mushroom m: mushrooms) {
			 if(m.hitCnt < 3 && m.hitCnt > 0) {
				 score += 100;
				 m.hitCnt = 0;
			 }
		 }
	 }

	 private class TAdapter implements MouseMotionListener, MouseListener {
		@Override
		public void mouseMoved(MouseEvent e) {
			if(inGame) {
				setCursor(blankCursor);
			} else {
				setCursor(Cursor.getDefaultCursor());
			}
			player.mouseMoved(e);
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			player.mouseClicked(e);
			//shot.playShot();
			//Toolkit.getDefaultToolkit().beep();
			sound.play("src/resources/shot.wav");
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if(inGame) {
				setCursor(blankCursor);
			} else {
				setCursor(Cursor.getDefaultCursor());
			}
			player.mouseMoved(e);
		}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	  }
}
