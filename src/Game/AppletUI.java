package Game;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;



public class AppletUI extends JFrame{

	private static final long serialVersionUID = -6215774992938009947L;
	public static final long milisecInNanosec = 1000000L;
	public static final long secInNanosec = 1000000000L;
	private int GAME_FPS = 600;
	private int GAME_SPEED = 60;
	private final long GAME_UPDATE_RATE = secInNanosec / GAME_SPEED;
	private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;
	public long lastDrawTime = System.currentTimeMillis();
	public long lastLightTime = System.currentTimeMillis();
	public static int windowWidth=1000;
	public static int windowHeight=800;
	public static int xoffset=500;
	public static int yoffset=400;
	public static double delta = 1.0;
	public static JPanel drawPanel;
	public static Container pane;
	public static Point mousePos = new Point(0,0);

	//variables for sparkles
	long lastSparkleUpdateTime = 0;
	int sparkleUpdateDelay = 10;
	int sparkleXpos = 725;//1195


	//detect the fps the game is running at

	double avgXpos = 0;
	double avgYpos = 0;
	static int fps = 0;
	public static ImagePack imgs = new ImagePack();

	//time since last second fps was measured
	long lastFPStimeUpdate = System.currentTimeMillis();

	long lastUpdateTime = System.nanoTime();

	Controller ctrl;
	public static void main(String[] args){
		AppletUI f = new AppletUI ();
		f.setSize(windowWidth,windowHeight);
		f.setVisible(true);
	}

	public AppletUI() {
		System.setProperty("sun.java2d.opengl", "true");
		setSize(windowWidth,windowHeight);
		pane = getContentPane();
		pane.setLayout(new BorderLayout());

		drawPanel = new GamePanel();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		drawPanel.setBackground(Color.black);
		ctrl = new Controller();
		this.addKeyListener(ctrl);
		ctrl.setGamePanel(drawPanel);
		this.setFocusable(true);
		pane.add(drawPanel);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);  
		this.setUndecorated(true);  
		//We start game in new thread.
		Thread gameThread = new Thread() {			
			public void run(){
				gameLoop();
			}

		};
		gameThread.start();

		//start the lighting loop in a new thread
		Thread updateThread = new Thread() {			
			public void run(){
				updateLoop();
			}

		};
		updateThread.start();
		//BattleshorePanel.game_is_running=true;
	}
	public void updateLoop(){
		while(true){
			long beginTime = System.nanoTime();
			//GamePanel.graphics.clearRect(0, 0, 1920, 1080);
			GamePanel.lighting = new BufferedImage(1920,1080,BufferedImage.TYPE_INT_ARGB);
			for(int i = 0; i<GamePanel.lights.size();i++){
				Light light = GamePanel.lights.get(i);
				light.render();
			}
			GamePanel.lighting=Images.invertAlpha(GamePanel.lighting);
//			for(int i = 0; i<GamePanel.lights.size();i++){
//				Light light = GamePanel.lights.get(i);
//				GamePanel.graphics.setColor(light.lightColor);
//				GamePanel.graphics.fillOval(AppletUI.xoffset+light.xpos-(light.radius)-((int)GamePanel.player.xpos), AppletUI.yoffset+light.ypos-light.radius-((int)GamePanel.player.ypos), light.radius*2, light.radius*2);
//			}
			GamePanel.tempLighting=GamePanel.lighting;


			// Here we calculate the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
			long timeTaken = System.nanoTime() - beginTime;

			long timeLeft = (GAME_UPDATE_RATE - timeTaken) / milisecInNanosec; // In milliseconds
			// If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
			if (timeLeft < 10) 
				timeLeft = 10; //set a minimum
			try {
				//Provides the necessary delay and also yields control so that other thread can do work.
				Thread.sleep(timeLeft);
			} catch (InterruptedException ex) { }
		}
	}
	int delay = 0;
	public void gameLoop(){

		// This variables are used for calculating the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
		long beginTime, timeTaken, timeLeft;
		int ticks = 0;
		while(true)
		{	

			mousePos.x=(Controller.mousePosition.x);
			mousePos.y=(Controller.mousePosition.y);

			windowWidth = this.getWidth();
			xoffset = windowWidth/2;
			windowHeight = this.getHeight();
			yoffset = windowHeight/2;
			//System.out.println("1");
			//System.out.println("2");
			//System.out.println("looping!");
			beginTime = System.nanoTime();
			// Repaint the screen 100 times per second.
			repaint();
			GamePanel.zones.get(GamePanel.currentZone).update();
			//System.out.println("3");
			Controller.checkKeys();
			//System.out.println("4");

			// Here we calculate the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
			timeTaken = System.nanoTime() - beginTime;

			timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
			// If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
			if (timeLeft < 10) 
				timeLeft = 10; //set a minimum
			try {
				//Provides the necessary delay and also yields control so that other thread can do work.
				Thread.sleep(timeLeft);
			} catch (InterruptedException ex) { }

		}
	}


	public void Draw(Graphics g){

	}

}

