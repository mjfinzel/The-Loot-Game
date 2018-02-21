package Game;



import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel{

	private static final long serialVersionUID = 7734877696044080629L;
	public static BufferedImage heart = Images.load("/Textures/Heart.png");

	public static BufferedImage unexploredTexture = Images.load("/Textures/UnexploredTexture.png");

	public static BufferedImage arrow = Images.load("/Textures/Arrow.png");

	public static BufferedImage fireball = Images.load("/Textures/FireBall.png");
	public static BufferedImage [][]ballLightning = Images.cut("/Textures/BallLightning.png",10,10);
	public static BufferedImage webShot = Images.load("/Textures/WebShot.png");
	public static BufferedImage goldenArrow = Images.load("/Textures/GoldenArrow.png");
	public static BufferedImage currency[][] = Images.cut("/Textures/Currency.png",50,50);
	public static BufferedImage magicWeapons[][] = Images.cut("/Textures/MagicWeapons.png",50,50);
	public static BufferedImage inventorySlot = Images.load("/Textures/InventorySlot.png");
	public static BufferedImage tooltipBackground = Images.load("/Textures/TooltipBackground.png");
	public static BufferedImage inventoryBackground = Images.load("/Textures/Inventory.png");
	public static BufferedImage healthBarBackground = Images.load("/Textures/HealthBarBackground.png");
	public static BufferedImage [][]armorTextures = Images.cut("/Textures/ArmorTextures.png", 50, 50);
	public static BufferedImage [][]rings = Images.cut("/Textures/Rings.png", 50, 50);
	public static BufferedImage [][]gearSlots = Images.cut("/Textures/GearSlots.png", 50, 50);
	public static BufferedImage [][]bars = Images.cut("/Textures/Bars.png", 2, 4);
	public static BufferedImage[][]PlayerFaces = Images.cut("/Textures/PlayerHead.png", 50, 50);
	public static BufferedImage[][]tiles = Images.cut("/Textures/CaveTiles.png", 50, 50);
	public static BufferedImage[][]chests = Images.cut("/Textures/Chests.png", 50, 50);
	public static BufferedImage[][]mapTiles = Images.cut("/Textures/MapTiles.png", 5, 5);
	public static BufferedImage[][]vase = Images.cut("/Textures/Vase.png", 50, 50);
	public static BufferedImage [][]bat = Images.cut("/Textures/Bat.png",30,30);
	public static BufferedImage [][]wormSegments = Images.cut("/Textures/Worm.png",30,30);
	public static BufferedImage [][]spiderEgg = Images.cut("/Textures/SpiderEgg.png",20,20);
	public static BufferedImage [][]spider = Images.cut("/Textures/Spider.png",40,40);
	public static BufferedImage [][]spiderLights = Images.cut("/Textures/SpiderLightMap.png",40,40);
	public static BufferedImage [][]spiderQueen = Images.cut("/Textures/QueenSpiderAnim.png",250,200);

	public static BufferedImage [][]spirit = Images.cut("/Textures/Spirit.png",30,30);

	public static BufferedImage [][]tooltipModPreviews = Images.cut("/Textures/TooltipModPreviews.png",5,5);

	public static BufferedImage [][]statusEffects = Images.cut("/Textures/StatusEffects.png",50,50);
	public static BufferedImage [][]fireAnim = Images.cut("/Textures/Fire.png",1,1);
	public static BufferedImage [][]electricArc = Images.cut("/Textures/ElectricArc.png",8,5);

	public static BufferedImage basicLightImg;

	public static BufferedImage [][]nonBattleAnims = Images.cut("/Textures/NonBattleEffects.png",50,50);
	public static ArrayList<Animation> effects = new ArrayList<Animation>();
	public static int chunkSize = 120;
	public static int swarmDisplacement = 1;
	public static ArrayList<Zone> zones = new ArrayList<Zone>();
	public static int currentZone = 0;
	public static int cameraX = 0;
	public static int cameraY = 0;
	public static double zoom = 1;
	public static ArrayList<CombatText> combatText = new ArrayList<CombatText>();
	public static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	public static Player player;
	public static boolean showInventory = false;
	public static boolean showMap = false;
	public static Point mapPos = new Point(0,0);
	public static boolean godmode = false;
	public static Point panelPos = new Point(0,0);
	//current level the player is on

	static long seed = 2;
	static Random rand = new Random(seed);
	public static BufferedImage lighting = new BufferedImage(1920,1080,BufferedImage.TYPE_INT_ARGB);
	public static BufferedImage tempLighting = new BufferedImage(1920,1080,BufferedImage.TYPE_INT_ARGB);
	public static BufferedImage lightingReset = new BufferedImage(1920,1080,BufferedImage.TYPE_INT_ARGB);
	public static boolean doneDrawingLighting = true;

	public static int fpsSum = 0;
	public static long lastFPSupdate = System.nanoTime();
	static int currentFPS = 0;

	public static Graphics2D graphics = lighting.createGraphics();
	public static ArrayList<Light> lights = new ArrayList<Light>();
	public GamePanel(){
		basicLightImg = createBasicLightImage(200,1);
		graphics.setPaint ( new Color ( 0, 0, 0,200 ) );
		graphics.fillRect ( 0, 0, lighting.getWidth(), lighting.getHeight() );
		lightingReset = lighting;
		zones.add(new Zone("testingLevel"));
		//updateTiles();
		currentZone++;
		zones.add(new Zone("Town"));
		//updateTiles();

		zones.add(new Zone("The Cave"));

		this.setDoubleBuffered(true);
		//updateTiles();
		player = new Player(zones.get(currentZone).spawnPoint.x*50,zones.get(currentZone).spawnPoint.y*50);
		GamePanel.graphics.setBackground(new Color(0,0,0,0));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D temp = (Graphics2D)g;
		Draw(temp);
	}
	public BufferedImage createBasicLightImage(int brightness, int smoothness){

		int radius = 960;
		BufferedImage img = new BufferedImage(radius*2,radius*2,BufferedImage.TYPE_INT_ARGB);
		int innerRadius = (radius/10)*3;
		Graphics2D tempGraphics = img.createGraphics();
		tempGraphics.setBackground(new Color(0,0,0,0));
		//loop through all the pixels the light will affect
		for(int i = 0-(smoothness/2)-radius; i<radius*2;i+=smoothness){
			for(int j = 0-(smoothness/2)-radius; j<radius*2;j+=smoothness){
				//determine the distance this pixel is from the source
				double distanceToSource = Math.sqrt(Math.pow(((radius)-i),2)+Math.pow(((radius)-j),2));
				if(distanceToSource<=radius){
					int transparency = 10;
					if(distanceToSource>innerRadius){
						transparency = brightness-((int)((double)(brightness*((distanceToSource-innerRadius)/(radius-innerRadius)))));
					}
					else{
						transparency = brightness;
					}
					if(transparency<0){
						transparency = 0;
					}
					else if(transparency>255){
						transparency = 255;
					}

					//System.out.println("i = "+i+", transparency: "+new Color(GamePanel.lighting.getRGB(i, j),true).getAlpha());
					if(i>=0&&i<radius*2&&j>=0&&j<radius*2){							
						Color temp = new Color (0,0,0,transparency);
						tempGraphics.setColor(temp);
						tempGraphics.clearRect(i, j, smoothness, smoothness);
						tempGraphics.fillRect(i, j, smoothness, smoothness);
					}


				}
				else{
					Color temp = new Color (0,0,0,0);
					tempGraphics.setColor(temp);
					tempGraphics.clearRect(i, j, smoothness, smoothness);
					tempGraphics.fillRect(i, j, smoothness, smoothness);
				}



			}
		}
		return img;
	}
	public static int randomNumber(int min, int max){

		if(max==min){
			return max;
		}
		int randNum = rand.nextInt((max-min)+1) + min;
		//System.out.println("generated random number: "+randNum+" in the range "+min+"-"+max);
		return randNum;
		//return min + (int)(Math.random() * ((max - min) + 1));
	}
	public static void DrawImage(Graphics g,BufferedImage img,double xpos, double ypos, double xScale, double yScale, double angle){
		//xScale = 1;
		//yScale = 1;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		AffineTransform at = new AffineTransform();
		//set position on screen
		at.translate(xpos-((double)((double)(img.getWidth())/2.0)), ypos-((double)((double)(img.getHeight())/2.0)));
		//set size of image
		at.scale(xScale, yScale);
		//rotate image
		at.rotate(angle,((double)((double)(img.getWidth())/2.0)),((double)((double)(img.getHeight())/2.0)));
		//		if(img.getWidth()!=img.getHeight()){
		//			System.out.println(img.getWidth()+","+img.getWidth());
		//		}
		//draw image
		g2d.drawImage(img, at, null);
	}
	public static ArrayList<String> cutString(String msg, int width, Font font){
		//create list of lines to draw for description
		ArrayList<String> lines = new ArrayList<String>();
		char []characters = msg.toCharArray();
		int lineWidth = 0;
		int closestSpace = 0;
		int lineStart = 0;
		FontMetrics metrics = AppletUI.pane.getGraphics().getFontMetrics(font);
		for(int i = 0; i<characters.length;i++){
			if(characters[i]==' '){
				closestSpace = i;
			}
			lineWidth+=metrics.charWidth(characters[i]);
			if(lineWidth>=(width-10)){
				lines.add(new String(msg.substring(lineStart,closestSpace)));
				lineStart = closestSpace+1;
				lineWidth = 0;
				i = closestSpace+1;
			}
			if(i==characters.length-1){//end of description
				lines.add(new String(msg.substring(lineStart,characters.length)));
			}
		}
		return lines;
	}

	public static int getStringWidth(String msg, Font font){
		FontMetrics metrics = AppletUI.pane.getGraphics().getFontMetrics(font);
		char []characters = msg.toCharArray();
		int width = 0;
		for(int i = 0; i<characters.length;i++){
			width+=metrics.charWidth(characters[i]);
		}
		return width;
	}
	public void DrawLight(Graphics g){

	}
	public static void DrawLights(Graphics2D g){

	}
	public void Draw(Graphics2D g){

		//keep track of fps
		fpsSum++;
		if(lastFPSupdate+AppletUI.secInNanosec<=System.nanoTime()){
			currentFPS = fpsSum;
			fpsSum = 0;
			lastFPSupdate = System.nanoTime();
		}

		panelPos.x=getLocation().x;
		panelPos.y=getLocation().y;
		if(!showMap){
			zones.get(currentZone).Draw(g);
		}
		player.Draw(g);
		//draw effects
		for(int i = 0; i<effects.size();i++){

			effects.get(i).xpos=AppletUI.xoffset+(int)effects.get(i).initialX-((int)GamePanel.player.xpos);
			effects.get(i).ypos=AppletUI.yoffset+(int)effects.get(i).initialY-((int)GamePanel.player.ypos);

			effects.get(i).Draw(g);
			if(effects.get(i).loopAnim==false&&effects.get(i).getCurrentFrame()>=effects.get(i).getFrameCount()-1){
				effects.remove(i);
			}
		}

		for(int i = 0; i<projectiles.size();i++){
			projectiles.get(i).Draw(g);
		}
		if(zones.get(currentZone).hasDarkness){

			//draw lighting
			g.drawImage(tempLighting, 0, 0,1920, 1080, null);
			//g.drawImage(GamePanel.lighting, 0, 0,1920, 1080, null);
			//GamePanel.graphics.clearRect(0, 0, 1920, 1080);

		}
		for(int i = 0; i<combatText.size();i++){
			combatText.get(i).Draw(g);
		}
		player.DrawUI(g);
		if(showInventory){
			player.inventory.Draw(g);
		}
		else{
			if (Controller.keyboardKeyState(KeyEvent.VK_SPACE)){
				for(int i = 0; i<zones.get(currentZone).groundItems.size();i++){
					if(zones.get(currentZone).groundItems.get(i).tooltip.mouseOverThis()){
						InventorySlot temp = new InventorySlot(AppletUI.mousePos.x,AppletUI.mousePos.y);
						temp.item=zones.get(currentZone).groundItems.get(i);
						temp.DrawTooltip(g);
					}
				}
			}
			for(int x = 0; x<10; x++){
				player.inventory.items[x][0].Draw(g,(AppletUI.windowWidth/2)-250+(x*50),AppletUI.windowHeight-50);
			}
		}
		if(showMap&&currentZone<zones.size()){
			int size = 3;//3
			int x = AppletUI.xoffset+mapPos.x-(int)(player.xpos/(50.0/(double)size));
			int y = AppletUI.yoffset+mapPos.y-(int)(player.ypos/(50.0/(double)size));
			
			g.drawImage(zones.get(currentZone).miniMap,x,y,(int)(zones.get(currentZone).miniMap.getWidth()*(double)((double)size/5.0)),(int)(zones.get(currentZone).miniMap.getHeight()*(double)((double)size/5.0)),null);
			
			g.drawImage(mapTiles[2][1], x+((int)(player.xpos/50)*size)-size, y+((int)(player.ypos/50)*size)-size,size*2, size*2, null);
		}
		Font font = new Font("Iwona Heavy",Font.BOLD,18);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString((int)player.currentHealth+"/"+(int)player.getMaxHealth(),150,50);
		if(!godmode){
			g.drawString("("+(int)(player.xpos/50.0)+","+(int)(player.ypos/50.0)+")",50,1050);
		}
		else{
			g.drawString("FPS: "+currentFPS, 1800, 30);
			g.drawString("Armor Coverage: "+player.getArmorCoverage()+"%", 50, AppletUI.xoffset);
			g.drawString("Damage reduction: "+(100-(100*player.getDamageReductionMultiplier()))+"%", 50, 990);
			g.drawString("Movement speed: "+(int)((player.getMovementSpeed()/2.0)*100.0)+"%", 50, 1020);
			g.drawString("("+(int)(player.xpos/50.0)+","+(int)(player.ypos/50.0)+") God Mode,"+" There are currently: "+zones.get(currentZone).monsterCount+" monsters in this area, There are: "+zones.get(currentZone).groundItems.size()+" items on the ground.",50,1050);
		}
		if(player.inventory.useItem!=null){
			g.drawImage(player.inventory.useItem.artwork, AppletUI.mousePos.x, AppletUI.mousePos.y,40, 40, null);
		}
		if(player.inventory.cursorItem!=null){
			g.drawImage(player.inventory.cursorItem.artwork, AppletUI.mousePos.x, AppletUI.mousePos.y,40, 40, null);
		}
	}

}
