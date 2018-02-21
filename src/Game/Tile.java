package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Tile {
	int xpos;
	int ypos;
	int xID;
	int yID;
	boolean explored = false;
	int collision= 1;//1 = can walk on, 0 = can't
	public Rectangle collisionBox;
	ArrayList<Monster>monsters = new ArrayList<Monster>();
	Furniture chest;
	Vase vase;
	String name;
	boolean mouseOnThis = false;
	boolean renderLast = false;
	ClickableName clickableName;
	//public BufferedImage lighting = new BufferedImage(50,50,BufferedImage.TYPE_INT_ARGB);
	//public BufferedImage lightingReset = new BufferedImage(50,50,BufferedImage.TYPE_INT_ARGB);
	//Graphics2D graphics = lighting.createGraphics();
	public Tile(int x,int y, int xid, int yid){

		xpos=x;
		ypos=y;
		xID = xid;
		yID = yid;

		//graphics.setPaint ( new Color ( 0, 0, 0,200 ) );
		//graphics.fillRect ( 0, 0, lighting.getWidth(), lighting.getHeight() );
		//graphics.fillRect ( 0, 0, lightingReset.getWidth(), lightingReset.getHeight() );
	}
	public void Update(int index){
		Tile top = null;
		Tile left = null;
		Tile bottom = null;
		Tile right = null;
		Tile topLeft = null;
		Tile topRight = null;
		Tile bottomLeft = null;
		Tile bottomRight = null;
		if((ypos/50)-1>=0){
			top = GamePanel.zones.get(GamePanel.currentZone).map[xpos/50][(ypos/50)-1];
		}
		if((xpos/50)-1>=0){
			left = GamePanel.zones.get(GamePanel.currentZone).map[(xpos/50)-1][(ypos/50)];
		}
		if((ypos/50)+1<GamePanel.zones.get(GamePanel.currentZone).levelHeight){
			bottom = GamePanel.zones.get(GamePanel.currentZone).map[xpos/50][(ypos/50)+1];
		}
		if((xpos/50)+1<GamePanel.zones.get(GamePanel.currentZone).levelWidth){
			right = GamePanel.zones.get(GamePanel.currentZone).map[(xpos/50)+1][(ypos/50)];
		}
		//corners
		if((ypos/50)-1>=0&&(xpos/50)-1>=0){
			topLeft = GamePanel.zones.get(GamePanel.currentZone).map[(xpos/50)-1][(ypos/50)-1];
		}
		if((ypos/50)-1>=0&&(xpos/50)+1<GamePanel.zones.get(GamePanel.currentZone).levelWidth){
			topRight = GamePanel.zones.get(GamePanel.currentZone).map[(xpos/50)+1][(ypos/50)-1];
		}
		if((ypos/50)+1<GamePanel.zones.get(GamePanel.currentZone).levelHeight&&(xpos/50)-1>=0){
			bottomLeft = GamePanel.zones.get(GamePanel.currentZone).map[(xpos/50)-1][(ypos/50)+1];
		}
		if((ypos/50)+1<GamePanel.zones.get(GamePanel.currentZone).levelHeight&&(xpos/50)+1<GamePanel.zones.get(GamePanel.currentZone).levelWidth){
			bottomRight = GamePanel.zones.get(GamePanel.currentZone).map[(xpos/50)+1][(ypos/50)+1];
		}
		if(xID==1&&yID==3){
			collisionBox = new Rectangle(xpos,ypos,50,50);
		}
		if(xID==0&&yID==4){//black tile
			collisionBox = new Rectangle(xpos,ypos,50,50);
			if(index==0){
				if(top!=null&&bottom!=null&&left!=null&&right!=null){
					if(!(top.xID==0&&top.yID==4)&&!(bottom.xID==0&&bottom.yID==4)){

						yID=15;
						collisionBox = null;
					}
					if(!(left.xID==0&&left.yID==4)&&!(right.xID==0&&right.yID==4)){

						yID=15;
						collisionBox = null;
					}
				}
			}
			if(index==1){
				if(topLeft!=null&&top!=null&&left!=null){
					if(top.xID==0&&top.yID==4&&topLeft.yID==15&&left.xID==0&&left.yID==4){
						xID=topLeft.xID;
						yID=7;
						collisionBox = new Rectangle(xpos,ypos,50,50);
					}
				}
			}
			if(index==2){
				if(topRight!=null&&top!=null&&right!=null){
					if(top.xID==0&&top.yID==4&&topRight.yID==15&&right.xID==0&&right.yID==4){
						xID=topRight.xID;
						yID=12;
						collisionBox = new Rectangle(xpos,ypos,50,50);
					}
				}
			}
			if(index==3){
				if(bottomLeft!=null&&bottom!=null&&left!=null){
					if(bottom.xID==0&&bottom.yID==4&&bottomLeft.yID==15&&left.xID==0&&left.yID==4){
						xID=bottomLeft.xID;
						yID=6;
						collisionBox = new Rectangle(xpos,ypos,50,50);
					}
				}
			}
			if(index==4){
				if(bottomRight!=null&&bottom!=null&&right!=null){
					if(bottom.xID==0&&bottom.yID==4&&bottomRight.yID==15&&right.xID==0&&right.yID==4){
						xID=bottomRight.xID;
						yID=11;
						collisionBox = new Rectangle(xpos,ypos,50,50);
					}
				}
			}
			if(index==5){
				if(top!=null&&bottom!=null&&left!=null&&right!=null){
					if(!(top.yID==15)&&!(right.yID==15)&&(bottom.yID==15)&&(left.yID==15)){
						xID=bottom.xID;
						yID=2;
						collisionBox = new Rectangle(xpos+6,ypos,44,50);
					}
				}
			}
			if(index==6){
				if(top!=null&&bottom!=null&&left!=null&&right!=null){
					if(!(top.yID==15)&&(right.yID==15)&&(bottom.yID==15)&&!(left.yID==15)){
						xID=bottom.xID;
						yID=10;
						collisionBox = new Rectangle(xpos,ypos,44,50);
					}
				}
			}
			if(index==7){
				if(top!=null&&bottom!=null&&left!=null&&right!=null){
					if((top.yID==15)&&!(right.yID==15)&&!(bottom.yID==15)&&(left.yID==15)){
						xID=top.xID;
						yID=0;
						collisionBox = new Rectangle(xpos+6,ypos+6,44,44);
					}
				}
			}
			if(index==8){
				if(top!=null&&bottom!=null&&left!=null&&right!=null){
					if((top.yID==15)&&(right.yID==15)&&!(bottom.yID==15)&&!(left.yID==15)){
						xID=top.xID;
						yID=8;
						collisionBox = new Rectangle(xpos,ypos+6,44,44);
					}
				}
			}
			if(index==9){
				//horizontal
				if(top!=null&&bottom!=null&&left!=null&&right!=null){
					if(!(top.yID==15)&&!(right.yID==15)&&(bottom.yID==15)&&!(left.yID==15)){
						xID=bottom.xID;
						yID=5;
						collisionBox = new Rectangle(xpos,ypos,50,50);
					}
				}
			}
			if(index==10){
				if(top!=null&&bottom!=null&&left!=null&&right!=null){
					if((top.yID==15)&&!(right.yID==15)&&!(bottom.yID==15)&&!(left.yID==15)){
						xID=top.xID;
						yID=3;
						collisionBox = new Rectangle(xpos,ypos+6,50,44);
					}
				}
			}
			if(index==11){
				//vertical
				if(top!=null&&bottom!=null&&left!=null&&right!=null){
					if(!(top.yID==15)&&(right.yID==15)&&!(bottom.yID==15)&&!(left.yID==15)){
						xID=right.xID;
						yID=9;
						collisionBox = new Rectangle(xpos,ypos,44,50);
					}
				}
			}
			if(index==12){
				if(top!=null&&bottom!=null&&left!=null&&right!=null){
					if(!(top.yID==15)&&!(right.yID==15)&&!(bottom.yID==15)&&(left.yID==15)){
						xID=left.xID;
						yID=1;
						collisionBox = new Rectangle(xpos+6,ypos,44,50);
					}
				}
			}


			collision=0;
		}
		if(index==13){
			if(yID==14){
				clickableName = new ClickableName(name,xpos+25-((GamePanel.getStringWidth(name, new Font("Iwona Heavy",Font.BOLD,14))+20)/2),ypos-25);
				boolean alreadyExists = false;
				for(int i = 0; i<GamePanel.zones.get(GamePanel.currentZone).clickables.size();i++){
					if(GamePanel.zones.get(GamePanel.currentZone).clickables.get(i).name.equals(clickableName.name)){
						alreadyExists = true;
					}
				}
				if(!alreadyExists){
					GamePanel.zones.get(GamePanel.currentZone).clickables.add(clickableName);
				}
			}
		}
		if(xID==1&&yID==0){//cave ground
			collision=0;
		}
	}
	public void Draw(Graphics2D g){
		explored = true;
		g.drawImage(GamePanel.tiles[xID][yID], AppletUI.xoffset+xpos-((int)GamePanel.player.xpos), AppletUI.yoffset+ypos-((int)GamePanel.player.ypos),50, 50, null);
		if(vase!=null){
			vase.Draw(g);
		}
		if(chest!=null){
			chest.Draw(g);
		}
		//g.drawImage(lighting, AppletUI.xoffset+xpos-((int)GamePanel.player.xpos), AppletUI.yoffset+ypos-((int)GamePanel.player.ypos),50, 50, null);
		//if(graphics!=null){

			//graphics.setPaint ( new Color ( 0, 0, 0,200 ) );
			//graphics.fillRect ( 0, 0, lighting.getWidth(), lighting.getHeight() );

		//}
//		if(collisionBox!=null){
//			//g.setColor(Color.red);
//			//g.drawRect(AppletUI.xoffset+collisionBox.x-((int)GamePanel.player.xpos), AppletUI.yoffset+collisionBox.y-((int)GamePanel.player.ypos), collisionBox.width, collisionBox.height);
//		}
		if(GamePanel.player.inventory.useItem!=null){
			if(GamePanel.player.inventory.useItem.itemType=="Furniture"){
				if(mouseOnThis&&yID==15){
					g.setColor(Color.pink);
					g.drawRect(AppletUI.xoffset+xpos-((int)GamePanel.player.xpos), AppletUI.yoffset+ypos-((int)GamePanel.player.ypos), 49, 49);
				}
				else if(mouseOnThis&&yID!=15){
					g.setColor(Color.red);
					g.drawRect(AppletUI.xoffset+xpos-((int)GamePanel.player.xpos), AppletUI.yoffset+ypos-((int)GamePanel.player.ypos), 49, 49);
				}
			}
		}
		if(monsters.size()>0){
			//g.setColor(Color.blue);
			//g.drawRect(AppletUI.xoffset+xpos-((int)GamePanel.player.xpos), AppletUI.yoffset+ypos-((int)GamePanel.player.ypos), 49, 49);
		}
		mouseOnThis = false;
	}
}
