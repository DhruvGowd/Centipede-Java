package com.Centipede;
import java.awt.EventQueue;
import javax.swing.JFrame;

public class App extends JFrame {
	
	public final static int WIDTH = 600;
	public final static int HEIGHT = 600;
	public App() {
		initFrame();
	}
	
	public void initFrame() {
		add (new Board());
		setSize(WIDTH, HEIGHT);
		setTitle("Centipede: Java Edition");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		
		 EventQueue.invokeLater(() -> {
	            App ex = new App();
	            ex.setVisible(true);
	        });
	}

}
