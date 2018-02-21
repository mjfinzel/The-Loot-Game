package Game;


import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;




import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;



public class Controller extends JPanel implements KeyListener,MouseListener,MouseMotionListener,MouseWheelListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//public static Unit currentUnit;
	public static String currentTileName = "No tiles selected";
	private JPanel gamePanel;
	long starttime = 0;
	private static boolean[] keyboardState = new boolean[525];
	static boolean mouseIsPressed = false;
	public static boolean mouseDragging = false;
	public static boolean busy = false;
	static Point mousePosition = new Point(0,0);
	public Controller(){
		this.setDoubleBuffered(true);

	}
	public static boolean keyboardKeyState(int key)
	{
		return keyboardState[key];
	}
	public void setGamePanel(JPanel panelRef) {
		gamePanel = panelRef;
		gamePanel.addKeyListener(this);
		gamePanel.addMouseListener(this);
		gamePanel.addMouseMotionListener(this);
		gamePanel.addMouseWheelListener(this);
	}
	public void setGamePanelPos(int x, int y){
		gamePanel.setAlignmentX(x);
		gamePanel.setAlignmentX(y);
	}
	public void updateAll(){
		//if (gamePanel != null)
			//gamePanel.getParent().repaint();
	}
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mousePosition.x = arg0.getX();
		mousePosition.y = arg0.getY();
	}
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mousePosition.x = arg0.getX();
		mousePosition.y = arg0.getY();
	}

	public void mouseClicked(MouseEvent arg0) {

	}
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	public void mousePressed(MouseEvent arg0) {
		if(arg0.getButton() == MouseEvent.BUTTON1){//left click
			boolean mouseOverChest = false;
			//check chests
			for(int i = 0; i<GamePanel.zones.get(GamePanel.currentZone).chests.size();i++){
				if(GamePanel.zones.get(GamePanel.currentZone).chests.get(i).clickableName.mouseOverThis()){
					mouseOverChest=true;
					GamePanel.zones.get(GamePanel.currentZone).chests.get(i).isOpen = true;
				}
			}
			if(GamePanel.zones.size()>0){
				//check clickables
				for(int i = 0; i<GamePanel.zones.get(GamePanel.currentZone).clickables.size();i++){
					if(GamePanel.zones.get(GamePanel.currentZone).clickables.get(i).mouseOverThis()){
						double distanceToClickable = Math.sqrt(Math.pow(((GamePanel.player.xpos)-GamePanel.zones.get(GamePanel.currentZone).clickables.get(i).xpos),2)+Math.pow(((GamePanel.player.ypos)-GamePanel.zones.get(GamePanel.currentZone).clickables.get(i).ypos),2));
						if(distanceToClickable<60){
							//if(GamePanel.zones.get(GamePanel.currentZone).clickables.get(i).name.equals("The Cave")){
							//find the latest instance of cave
							boolean foundCave = false;
							for(int j = GamePanel.zones.size()-1;j>0;j--){

								if(GamePanel.zones.get(j).name.equals(GamePanel.zones.get(GamePanel.currentZone).clickables.get(i).name)){
									foundCave = true;
									GamePanel.currentZone=j;
									GamePanel.player.xpos=GamePanel.zones.get(j).spawnPoint.x*50;
									GamePanel.player.ypos=GamePanel.zones.get(j).spawnPoint.y*50;
									break;
								}
							}
							if(!foundCave){
								GamePanel.zones.add(new Zone(GamePanel.zones.get(GamePanel.currentZone).clickables.get(i).name));
								GamePanel.currentZone=GamePanel.zones.size()-1;
								GamePanel.player.xpos=GamePanel.zones.get(GamePanel.currentZone).spawnPoint.x*50;
								GamePanel.player.ypos=GamePanel.zones.get(GamePanel.currentZone).spawnPoint.y*50;
								GamePanel.zones.get(GamePanel.currentZone).updateTiles();
							}
							//}
						}
					}
				}
			}
			if(!mouseOverChest){
				mouseIsPressed=true;
			}

		}

		if(arg0.getButton() == MouseEvent.BUTTON3){//right click
			if(!GamePanel.showInventory){
				for(int i = 0; i<GamePanel.zones.get(GamePanel.currentZone).groundItems.size();i++){
					if(GamePanel.zones.get(GamePanel.currentZone).groundItems.get(i).tooltip.mouseOverThis()){
						GamePanel.player.inventory.pickUpItem(GamePanel.zones.get(GamePanel.currentZone).groundItems.get(i));
					}
				}
			}
			else{//inventory is open
				if(GamePanel.player.inventory.useItem==null){
					for(int x = 0; x<10;x++){
						for(int y = 0; y<5;y++){
							if(GamePanel.player.inventory.items[x][y].mouseOverThis()&&GamePanel.player.inventory.items[x][y].item!=null){
								if(GamePanel.player.inventory.items[x][y].item.itemType=="Currency"){
									GamePanel.player.inventory.useItem=new Item(-1,-1,GamePanel.player.inventory.items[x][y].item.name,0);
								}
								else if(GamePanel.player.inventory.items[x][y].item.itemType=="Furniture"){
									GamePanel.player.inventory.useItem=new Item(-1,-1,GamePanel.player.inventory.items[x][y].item.name,0);
									GamePanel.showInventory=false;
								}
							}
						}
					}
				}
				else{
					GamePanel.player.inventory.useItem=null;
				}
			}
		}
		if(arg0.getButton() == MouseEvent.BUTTON1){//left click
			if(GamePanel.showInventory){
				boolean foundSlot = false;
				//check main inventory
				for(int x = 0; x<10;x++){
					for(int y = 0; y<5;y++){
						if(GamePanel.player.inventory.items[x][y].mouseOverThis()){
							foundSlot = true;
							if(GamePanel.player.inventory.items[x][y].item!=null){
								if(GamePanel.player.inventory.useItem!=null){
									GamePanel.player.inventory.useItem.useOn(GamePanel.player.inventory.items[x][y].item);
								}
								else if(GamePanel.player.inventory.cursorItem!=null){
									if(GamePanel.player.inventory.items[x][y].item!=GamePanel.player.inventory.cursorItem){
										Item temp = GamePanel.player.inventory.items[x][y].item;
										GamePanel.player.inventory.items[x][y].item=GamePanel.player.inventory.cursorItem;
										GamePanel.player.inventory.cursorItem=temp;
									}
									else{
										GamePanel.player.inventory.cursorItem=null;
									}
								}
								else{
									GamePanel.player.inventory.cursorItem=GamePanel.player.inventory.items[x][y].item;
									GamePanel.player.inventory.removeItem(GamePanel.player.inventory.items[x][y].item);
								}
							}
							else{
								GamePanel.player.inventory.removeItem(GamePanel.player.inventory.cursorItem);
								GamePanel.player.inventory.items[x][y].item=GamePanel.player.inventory.cursorItem;
								GamePanel.player.inventory.cursorItem = null;
							}
						}
					}
				}
				//				//check ring slots
				//				for(int i = 0;i<5;i++){
				//					InventorySlot itemslot = null;
				//					itemslot = GamePanel.player.inventory.rings[i];
				//					if(itemslot.mouseOverThis()){
				//						if(GamePanel.player.inventory.useItem!=null){
				//							GamePanel.player.inventory.useItem.useOn(itemslot.item);
				//						}
				//						else if(GamePanel.player.inventory.cursorItem!=null){
				//							if(itemslot.item!=null){
				//								if(!itemslot.item.equals(GamePanel.player.inventory.cursorItem)&&GamePanel.player.inventory.cursorItem.tooltip.itemType.equals("Ring")){
				//									Item temp = itemslot.item;
				//									itemslot.item=GamePanel.player.inventory.cursorItem;
				//									GamePanel.player.inventory.cursorItem=temp;
				//								}
				//							}
				//							else{
				//								itemslot.item=GamePanel.player.inventory.cursorItem;
				//								GamePanel.player.inventory.cursorItem=null;
				//							}
				//						}
				//						else{
				//							GamePanel.player.inventory.cursorItem=itemslot.item;
				//							GamePanel.player.inventory.removeItem(itemslot.item);
				//						}
				//					}
				//
				//
				//				}
				//check gear slots
				for(int i = 0; i<12;i++){
					InventorySlot itemslot = null;

					boolean isRightItemType = false;
					if(i==0){
						itemslot = GamePanel.player.inventory.head;
						//isRightItemType=GamePanel.player.inventory.cursorItem.itemType=="Helmet";
					}
					else if(i==1){
						itemslot = GamePanel.player.inventory.chest;
						//isRightItemType=GamePanel.player.inventory.cursorItem.itemType=="Chest";
					}
					else if(i==2){
						itemslot = GamePanel.player.inventory.pants;
						//isRightItemType=GamePanel.player.inventory.cursorItem.itemType=="Pants";
					}
					else if(i==3){
						itemslot = GamePanel.player.inventory.leftGlove;
						//isRightItemType=GamePanel.player.inventory.cursorItem.itemType=="Glove";
					}
					else if(i==4){
						itemslot = GamePanel.player.inventory.rightGlove;
						//isRightItemType=GamePanel.player.inventory.cursorItem.itemType=="Glove";
					}
					else if(i==5){
						itemslot = GamePanel.player.inventory.leftBoot;
						//isRightItemType=GamePanel.player.inventory.cursorItem.itemType=="Boot";
					}
					else if(i==6){
						itemslot = GamePanel.player.inventory.rightBoot;
						//isRightItemType=GamePanel.player.inventory.cursorItem.itemType=="Boot";
					}
					else{
						itemslot=GamePanel.player.inventory.rings[i-7];
					}
					if(itemslot.mouseOverThis()){
						foundSlot = true;
						//System.out.println("mouse over this");
						if(itemslot.item!=null){
							if(GamePanel.player.inventory.useItem!=null){
								GamePanel.player.inventory.useItem.useOn(itemslot.item);
							}
							else if(GamePanel.player.inventory.cursorItem!=null){
								if(i==0){
									isRightItemType=GamePanel.player.inventory.cursorItem.tooltip.itemType=="Helmet";
								}
								else if(i==1){
									isRightItemType=GamePanel.player.inventory.cursorItem.tooltip.itemType=="Chest";
								}
								else if(i==2){
									isRightItemType=GamePanel.player.inventory.cursorItem.tooltip.itemType=="Pants";
								}
								else if(i==3){
									isRightItemType=GamePanel.player.inventory.cursorItem.tooltip.itemType=="Glove";
								}
								else if(i==4){
									isRightItemType=GamePanel.player.inventory.cursorItem.tooltip.itemType=="Glove";
								}
								else if(i==5){
									isRightItemType=GamePanel.player.inventory.cursorItem.tooltip.itemType=="Boot";
								}
								else if(i==6){
									isRightItemType=GamePanel.player.inventory.cursorItem.tooltip.itemType=="Boot";
								}
								else{
									isRightItemType=GamePanel.player.inventory.cursorItem.tooltip.itemType=="Ring";
								}
								if(!itemslot.item.equals(GamePanel.player.inventory.cursorItem)&&isRightItemType){
									Item temp = itemslot.item;
									itemslot.item=GamePanel.player.inventory.cursorItem;
									GamePanel.player.inventory.cursorItem=temp;
								}
								else{

								}
							}
							else{
								GamePanel.player.inventory.cursorItem=itemslot.item;
								GamePanel.player.inventory.removeItem(itemslot.item);
							}
						}
						else{
							//System.out.println("else");
							if(GamePanel.player.inventory.cursorItem!=null){
								if(i==0){
									isRightItemType=GamePanel.player.inventory.cursorItem.tooltip.itemType=="Helmet";
								}
								else if(i==1){
									isRightItemType=GamePanel.player.inventory.cursorItem.tooltip.itemType=="Chest";
								}
								else if(i==2){
									isRightItemType=GamePanel.player.inventory.cursorItem.tooltip.itemType=="Pants";
								}
								else if(i==3){
									isRightItemType=GamePanel.player.inventory.cursorItem.tooltip.itemType=="Glove";
								}
								else if(i==4){
									isRightItemType=GamePanel.player.inventory.cursorItem.tooltip.itemType=="Glove";
								}
								else if(i==5){
									isRightItemType=GamePanel.player.inventory.cursorItem.tooltip.itemType=="Boot";
								}
								else if(i==6){
									isRightItemType=GamePanel.player.inventory.cursorItem.tooltip.itemType=="Boot";
								}
								else{
									isRightItemType=GamePanel.player.inventory.cursorItem.tooltip.itemType=="Ring";
								}
								//System.out.println(isRightItemType);
								if(isRightItemType){
									GamePanel.player.inventory.removeItem(GamePanel.player.inventory.cursorItem);
									itemslot.item=GamePanel.player.inventory.cursorItem;
									GamePanel.player.inventory.cursorItem = null;
								}
							}
						}
					}
				}
				if(!foundSlot&&GamePanel.player.inventory.cursorItem!=null){//drop item on ground
					//AppletUI.mousePos.x>=AppletUI.xoffset+item.xpos-((int)GamePanel.player.xpos)
					int x = ((int)GamePanel.player.xpos)-AppletUI.xoffset+AppletUI.mousePos.x;
					int y = ((int)GamePanel.player.ypos)-AppletUI.yoffset+AppletUI.mousePos.y;
					boolean foundPlace = false;
					Font font = new Font("Iwona Heavy",Font.BOLD,14);
					int ychange = 30;
					if(GamePanel.randomNumber(1, 2)==1){
						ychange = -30;
					}
					int xChange = 0;
					int rand = GamePanel.randomNumber(1, 3);
					if(rand==1){
						xChange = -30;
					}
					if(rand==3){
						xChange = 30;
					}
					while(!foundPlace){
						foundPlace = true;
						for(int i = 0; i<GamePanel.zones.get(GamePanel.currentZone).groundItems.size();i++){	
							int width = GamePanel.getStringWidth(GamePanel.player.inventory.cursorItem.tooltip.itemName,font)+40;
							Rectangle rect1 = new Rectangle(x,y,width,30);
							int width2 = GamePanel.getStringWidth(GamePanel.zones.get(GamePanel.currentZone).groundItems.get(i).tooltip.itemName,font)+40;
							Rectangle rect2 = new Rectangle(GamePanel.zones.get(GamePanel.currentZone).groundItems.get(i).xpos,GamePanel.zones.get(GamePanel.currentZone).groundItems.get(i).ypos,width2,30);
							if(rect1.intersects(rect2)){
								//there is already an item at this position
								foundPlace = false;
								//check a new position


								y+=ychange;
								x+=xChange;

							}
						}
					}
					GamePanel.player.inventory.cursorItem.xpos=x;
					GamePanel.player.inventory.cursorItem.ypos=y;
					GamePanel.zones.get(GamePanel.currentZone).groundItems.add(GamePanel.player.inventory.cursorItem);
					/////////////
					GamePanel.player.inventory.cursorItem=null;
				}

			}
			else{//inventory is not open
				if(GamePanel.player.inventory.useItem!=null){
					if(GamePanel.player.inventory.useItem.itemType=="Furniture"){
						//set the item on the ground
						if(GamePanel.player.inventory.useItem.tooltip.itemType=="Storage"){
							int x = GamePanel.zones.get(GamePanel.currentZone).map[(((int)GamePanel.player.xpos)-AppletUI.xoffset+AppletUI.mousePos.x)/50][(((int)GamePanel.player.ypos)-AppletUI.yoffset+AppletUI.mousePos.y)/50].xpos;
							int y = GamePanel.zones.get(GamePanel.currentZone).map[(((int)GamePanel.player.xpos)-AppletUI.xoffset+AppletUI.mousePos.x)/50][(((int)GamePanel.player.ypos)-AppletUI.yoffset+AppletUI.mousePos.y)/50].ypos;
							//GamePanel.zones.get(GamePanel.currentZone).map[(((int)GamePanel.player.xpos)-AppletUI.xoffset+AppletUI.mousePos.x)/50][(((int)GamePanel.player.ypos)-AppletUI.yoffset+AppletUI.mousePos.y)/50].chest = new Chest(GamePanel.player.inventory.useItem.name,x,y);
							GamePanel.zones.get(GamePanel.currentZone).chests.add(new Furniture(GamePanel.player.inventory.useItem.name,x,y));
							GamePanel.player.inventory.removeItem(GamePanel.player.inventory.useItem);
							GamePanel.player.inventory.useItem=null;
						}
					}
				}
			}
		}
	}

	public void mouseReleased(MouseEvent arg0) {
		if(arg0.getButton() == MouseEvent.BUTTON1){//left click
			mouseIsPressed=false;
		}

	}
	public void keyPressed(KeyEvent e) {
		keyboardState[e.getKeyCode()] = true;
		if (keyboardKeyState(KeyEvent.VK_I)){
			if(GamePanel.showInventory==false){
				GamePanel.showInventory=true;
			}
			else{
				GamePanel.showInventory=false;
				GamePanel.mapPos.x=0;
				GamePanel.mapPos.y=0;
			}
		}
		if (keyboardKeyState(KeyEvent.VK_M)){
			if(GamePanel.showMap==false){
				GamePanel.showMap=true;
			}
			else{
				GamePanel.showMap=false;
			}
		}
		if (keyboardKeyState(KeyEvent.VK_ESCAPE)){
			GamePanel.showInventory=false;
			GamePanel.showMap=false;
			GamePanel.mapPos.x=0;
			GamePanel.mapPos.y=0;
			for(int i = 0; i<GamePanel.zones.get(GamePanel.currentZone).chests.size();i++){
				GamePanel.zones.get(GamePanel.currentZone).chests.get(i).isOpen=false;
			}
		}
		if (keyboardKeyState(KeyEvent.VK_G)){
			if(GamePanel.godmode==false){
				GamePanel.godmode=true;
			}
			else{
				GamePanel.godmode=false;
			}
		}
		if (keyboardKeyState(KeyEvent.VK_DOWN)&&GamePanel.godmode&&!GamePanel.showMap){
			if(GamePanel.currentZone==GamePanel.zones.size()-1){
				GamePanel.zones.add(new Zone("The Cave"));

				GamePanel.currentZone++;
				GamePanel.zones.get(GamePanel.currentZone).updateTiles();
			}
			else{
				GamePanel.currentZone++;
			}
			GamePanel.player.xpos=GamePanel.zones.get(GamePanel.currentZone).spawnPoint.x*50;
			GamePanel.player.ypos=GamePanel.zones.get(GamePanel.currentZone).spawnPoint.y*50;
			GamePanel.player.desiredPosition.x=GamePanel.zones.get(GamePanel.currentZone).spawnPoint.x*50;
			GamePanel.player.desiredPosition.y=GamePanel.zones.get(GamePanel.currentZone).spawnPoint.y*50;
		}
		if (keyboardKeyState(KeyEvent.VK_UP)&&GamePanel.godmode&&!GamePanel.showMap){
			if(GamePanel.currentZone>0){
				GamePanel.currentZone--;
				GamePanel.player.xpos=GamePanel.zones.get(GamePanel.currentZone).spawnPoint.x*50;
				GamePanel.player.ypos=GamePanel.zones.get(GamePanel.currentZone).spawnPoint.y*50;
				GamePanel.player.desiredPosition.x=GamePanel.zones.get(GamePanel.currentZone).spawnPoint.x*50;
				GamePanel.player.desiredPosition.y=GamePanel.zones.get(GamePanel.currentZone).spawnPoint.y*50;
			}
		}
		if (keyboardKeyState(KeyEvent.VK_9)){
			//GamePanel.player.inventory.getItem(new Item(0, 0, "Whetstone"));
		}
	}
	public static void checkKeys(){
		if(!GamePanel.showInventory){
			if(mouseIsPressed){
				int desiredChangeX = AppletUI.mousePos.x-AppletUI.xoffset;
				int desiredChangeY = AppletUI.mousePos.y-AppletUI.yoffset;
				GamePanel.player.desiredPosition.x=(int)GamePanel.player.xpos+desiredChangeX;
				GamePanel.player.desiredPosition.y=(int)GamePanel.player.ypos+desiredChangeY;
			}
		}
		//if the player is at the position they clicked at last
		if((int)GamePanel.player.xpos==GamePanel.player.desiredPosition.x&&(int)GamePanel.player.ypos==GamePanel.player.desiredPosition.y){
			GamePanel.player.desiredAngleInDegrees=Math.toDegrees(Math.atan2((AppletUI.mousePos.y-AppletUI.yoffset),((AppletUI.mousePos.x-AppletUI.xoffset))));
			GamePanel.player.changeDirection();
		}
		if(GamePanel.player.isCharging==false){
			GamePanel.player.weaponAngle = Math.toDegrees(Math.atan2((AppletUI.mousePos.y-AppletUI.yoffset),((AppletUI.mousePos.x-AppletUI.xoffset))));
		}

		if(true){//!mouseIsPressed){
			if (keyboardKeyState(KeyEvent.VK_DOWN)){
				GamePanel.mapPos.y-=1;
			}
			if (keyboardKeyState(KeyEvent.VK_UP)){
				GamePanel.mapPos.y+=1;
			}
			if (keyboardKeyState(KeyEvent.VK_LEFT)){
				GamePanel.mapPos.x+=1;
			}
			if (keyboardKeyState(KeyEvent.VK_RIGHT)){
				GamePanel.mapPos.x-=1;
			}
			if (keyboardKeyState(KeyEvent.VK_D)){
				//GamePanel.player.angleInDegrees+=1;
			}
			if (keyboardKeyState(KeyEvent.VK_A)){
				//GamePanel.player.angleInDegrees-=1;
			}
			if (keyboardKeyState(KeyEvent.VK_1)){
				useItem(0);
			}
			if (keyboardKeyState(KeyEvent.VK_2)){
				useItem(1);
			}
			if (keyboardKeyState(KeyEvent.VK_3)){
				useItem(2);
			}
			if (keyboardKeyState(KeyEvent.VK_4)){
				useItem(3);
			}
			if (keyboardKeyState(KeyEvent.VK_5)){
				useItem(4);
			}
			if (keyboardKeyState(KeyEvent.VK_6)){
				useItem(5);
			}
			if (keyboardKeyState(KeyEvent.VK_7)){
				useItem(6);
			}
			if (keyboardKeyState(KeyEvent.VK_8)){
				useItem(7);
			}
			if (keyboardKeyState(KeyEvent.VK_9)){
				useItem(8);
			}
			if (keyboardKeyState(KeyEvent.VK_0)){
				useItem(9);
			}
			if (keyboardKeyState(KeyEvent.VK_S)){

			}
		}
		//		if(GamePanel.player.angleInDegrees < 0){
		//			GamePanel.player.angleInDegrees += 360;
		//		}
		//		else if(GamePanel.player.angleInDegrees>360){
		//			GamePanel.player.angleInDegrees-=360;
		//		}
		//		GamePanel.player.angleInRadians = Math.toRadians(GamePanel.player.angleInDegrees);
	}
	public static void useItem(int index){
		if(GamePanel.player.inventory.items[index][0]!=null){
			if(GamePanel.player.inventory.items[index][0].item!=null){
				if(GamePanel.player.inventory.items[index][0].item.itemType=="Furniture"){
					int x = GamePanel.zones.get(GamePanel.currentZone).map[(((int)GamePanel.player.xpos)-AppletUI.xoffset+AppletUI.mousePos.x)/50][(((int)GamePanel.player.ypos)-AppletUI.yoffset+AppletUI.mousePos.y)/50].xpos;
					int y = GamePanel.zones.get(GamePanel.currentZone).map[(((int)GamePanel.player.xpos)-AppletUI.xoffset+AppletUI.mousePos.x)/50][(((int)GamePanel.player.ypos)-AppletUI.yoffset+AppletUI.mousePos.y)/50].ypos;
					//GamePanel.zones.get(GamePanel.currentZone).map[(((int)GamePanel.player.xpos)-AppletUI.xoffset+AppletUI.mousePos.x)/50][(((int)GamePanel.player.ypos)-AppletUI.yoffset+AppletUI.mousePos.y)/50].chest = new Chest(GamePanel.player.inventory.useItem.name,x,y);
					GamePanel.zones.get(GamePanel.currentZone).chests.add(new Furniture(GamePanel.player.inventory.items[index][0].item.name,x,y));
					GamePanel.player.inventory.removeItem(GamePanel.player.inventory.items[index][0].item);
					GamePanel.player.inventory.useItem=null;
				}
				else if(GamePanel.player.inventory.items[index][0].item.itemType=="SwingWeapon"){
					if(GamePanel.player.inventory.items[index][0].item.swinging==false){
						if((GamePanel.player.inventory.items[index][0].item.isGodmodeRestricted)&&GamePanel.godmode){
							GamePanel.player.inventory.items[index][0].item.swingAngle=GamePanel.player.weaponAngle;
							GamePanel.player.inventory.items[index][0].item.swingWeapon();
						}
						else if(GamePanel.player.inventory.items[index][0].item.isGodmodeRestricted==false){
							GamePanel.player.inventory.items[index][0].item.swingAngle=GamePanel.player.weaponAngle;
							GamePanel.player.inventory.items[index][0].item.swingWeapon();
						}
					}
				}
				else if(GamePanel.player.inventory.items[index][0].item.itemType=="ProjectileWeapon"){
					if((GamePanel.player.inventory.items[index][0].item.isGodmodeRestricted)&&GamePanel.godmode){
						GamePanel.player.inventory.items[index][0].item.swingWeapon();
					}
					else if(GamePanel.player.inventory.items[index][0].item.isGodmodeRestricted==false){
						GamePanel.player.inventory.items[index][0].item.swingWeapon();
					}
				}
				else if(GamePanel.player.inventory.items[index][0].item.itemType=="Spear"){
					if((GamePanel.player.inventory.items[index][0].item.isGodmodeRestricted)&&GamePanel.godmode){
						GamePanel.player.inventory.items[index][0].item.swingWeapon();
					}
					else if(GamePanel.player.inventory.items[index][0].item.isGodmodeRestricted==false){
						GamePanel.player.inventory.items[index][0].item.swingWeapon();
					}
				}
				else if(GamePanel.player.inventory.items[index][0].item.itemType=="MagicWeapon"){
					GamePanel.player.inventory.items[index][0].item.swingWeapon();
				}
			}
		}
	}
	public void keyReleased(KeyEvent e) {
		keyboardState[e.getKeyCode()] = false;
		// TODO Auto-generated method stub

	}
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}
	public void mouseWheelMoved(MouseWheelEvent e) {
		//scale down
		//move the center of the map to the mouse position before the scaling

		if(e.getWheelRotation()<0){//mouse wheel moved up (zoom in)
			if(GamePanel.zoom<5){
				//GamePanel.zoom=GamePanel.zoom+.02;
			}
		}
		else{//mouse wheel moved down (zoom out)
			if(GamePanel.zoom>.04){
				//GamePanel.zoom=GamePanel.zoom-.02;
			}
		}

	}
}
