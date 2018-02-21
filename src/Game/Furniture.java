package Game;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Furniture {
	int xpos;
	int ypos;
	ClickableName clickableName;
	String name;
	BufferedImage artwork;
	InventorySlot[][] items;
	Light light;
	boolean isOpen = false;
	public Furniture(String Name, int x, int y){
		xpos = x;
		ypos = y;
		name = Name;
		Font font = new Font("Iwona Heavy",Font.BOLD,14);
		clickableName = new ClickableName(name,x+25-((GamePanel.getStringWidth(name, font)+20)/2),y-20);
		if(name=="Wooden Trunk"){
			items = new InventorySlot[5][4];
			artwork = GamePanel.chests[0][0];
		}
		if(name=="Torch"){
			artwork = GamePanel.chests[1][0];
			light = new Light(xpos+25, ypos+25, 300, 300, 3, 2);
			GamePanel.lights.add(light);
		}
	}
	public void Draw(Graphics2D g){
		g.drawImage(artwork, AppletUI.xoffset+xpos-(int)GamePanel.player.xpos, AppletUI.yoffset+ypos-(int)GamePanel.player.ypos, 50,50, null);
		clickableName.Draw(g);
		if(isOpen&&items!=null){
			
			for(int i = 0;i<items.length;i++){
				for(int j = 0; j<items[0].length;j++){
					if(items[i][j]!=null){
						items[i][j].Draw(g);
					}
					else{
						items[i][j]=new InventorySlot(1+i*50,1+j*50);
					}
				}
			}
		}
	}
}
