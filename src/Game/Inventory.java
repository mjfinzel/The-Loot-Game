package Game;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Inventory {
	Point pos = new Point(0,0);
	InventorySlot[][] items = new InventorySlot[10][5];
	InventorySlot[] rings = new InventorySlot[5];
	//ArrayList<Item> gear = new ArrayList<Item>();
	InventorySlot head = new InventorySlot(561,31);
	InventorySlot chest = new InventorySlot(561,81);
	InventorySlot leftGlove = new InventorySlot(511,81);
	InventorySlot rightGlove = new InventorySlot(611,81);
	InventorySlot pants = new InventorySlot(561,131);
	InventorySlot leftBoot = new InventorySlot(536,181);
	InventorySlot rightBoot = new InventorySlot(586,181);
	Item cursorItem;
	Item useItem;

	public Inventory(){
		for(int x = 0; x<10;x++){
			for(int y = 0; y<5;y++){
				if(y==0){
					items[x][y] = new InventorySlot(1+(x*50),1+(y*50));
				}
				else{
					items[x][y] = new InventorySlot(1+(x*50),11+(y*50));
				}
			}
		}
		for(int i = 0; i<5;i++){
			rings[i]=new InventorySlot(663,2+(i*52));
		}
	}
	public void removeItem(Item item){
		boolean removed = false;
		for(int x = 0; x<10; x++){
			for(int y = 0; y<5; y++){
				if(items[x][y].item==item){
					items[x][y].item=null;
					removed = true;
				}

			}
		}
		if(head.item==item){
			head.item=null;
			removed=true;
		}
		if(chest.item==item){
			chest.item=null;
			removed=true;
		}
		if(pants.item==item){
			pants.item=null;
			removed = true;
		}
		if(leftGlove.item==item){
			leftGlove.item=null;
			removed = true;
		}
		if(rightGlove.item==item){
			rightGlove.item=null;
			removed = true;
		}
		if(leftBoot.item==item){
			leftBoot.item=null;
			removed = true;
		}
		if(rightBoot.item==item){
			rightBoot.item=null;
			removed = true;
		}
		for(int i = 0; i<5;i++){
			if(rings[i].item==item){
				rings[i].item=null;
				removed = true;
			}
		}
		//		if(removed==false){//item wasn't found
		//			for(int x = 0; x<10; x++){
		//				for(int y = 0; y<5; y++){
		//					if(items[x][y].item!=null){
		//						if(items[x][y].item.name.equals(item.name)){
		//							items[x][y].item.stackSize--;
		//							if(items[x][y].item.stackSize<1){
		//								items[x][y].item = null;
		//							}
		//						}
		//					}
		//				}
		//			}
		//		}
	}
	public void pickUpItem(Item item){

		//if item is stackable
		if(item.maxStackSize>1){
			//check if there is already a partial stack in the inventory in the inventory
			for(int i = 0; i<10;i++){
				for(int j = 0; j<5;j++){
					if(items[i][j].item!=null){
						if(items[i][j].item.name.equals(item.name)){
							if(items[i][j].item.stackSize<items[i][j].item.maxStackSize){
								item.xpos=-1;
								item.ypos=-1;
								GamePanel.zones.get(GamePanel.currentZone).groundItems.remove(item);
								items[i][j].item.stackSize++;
								return;
							}
						}
					}
				}
			}
		}
		//find an empty spot in the inventory
		for(int i = 0; i<10;i++){
			for(int j = 0; j<5;j++){
				if(items[i][j].item==null){
					item.xpos=-1;
					item.ypos=-1;
					items[i][j].item=item;
					GamePanel.zones.get(GamePanel.currentZone).groundItems.remove(item);			
					return;
				}
			}
		}
	}
	public void getItem(Item item){
		//if item is stackable
		if(item.maxStackSize>1){
			//check if there is already a partial stack in the inventory in the inventory
			for(int i = 0; i<10;i++){
				for(int j = 0; j<5;j++){
					if(items[i][j].item!=null){
						if(items[i][j].item.stackSize<items[i][j].item.maxStackSize){

							items[i][j].item.stackSize++;
							return;
						}
					}
				}
			}
		}
		//find an empty spot in the inventory
		for(int i = 0; i<10;i++){
			for(int j = 0; j<5;j++){
				if(items[i][j].item==null){
					items[i][j].item=item;
					return;
				}
			}
		}
	}
	public void Draw(Graphics g){
		pos.x=(AppletUI.windowWidth/2)-362;
		pos.y=(AppletUI.windowHeight/2)-131;
		g.drawImage(GamePanel.inventoryBackground, pos.x, pos.y,724, 262, null);
		for(int i = 0; i<10;i++){
			for(int j = 0; j<5; j++){
				items[i][j].Draw(g);
			}
		}
		head.Draw(g);
		if(head.item==null){
			g.drawImage(GamePanel.gearSlots[1][0], pos.x+head.xpos, pos.y+head.ypos,50, 50, null);
		}
		chest.Draw(g);
		if(chest.item==null){
			g.drawImage(GamePanel.gearSlots[0][1], pos.x+chest.xpos, pos.y+chest.ypos,50, 50, null);
		}
		leftGlove.Draw(g);
		if(leftGlove.item==null){
			g.drawImage(GamePanel.gearSlots[1][2], pos.x+leftGlove.xpos, pos.y+leftGlove.ypos,50, 50, null);
		}
		rightGlove.Draw(g);
		if(rightGlove.item==null){
			g.drawImage(GamePanel.gearSlots[1][2], pos.x+rightGlove.xpos, pos.y+rightGlove.ypos,50, 50, null);
		}
		pants.Draw(g);
		if(pants.item==null){
			g.drawImage(GamePanel.gearSlots[1][1], pos.x+pants.xpos, pos.y+pants.ypos,50, 50, null);
		}
		leftBoot.Draw(g);
		if(leftBoot.item==null){
			g.drawImage(GamePanel.gearSlots[0][2], pos.x+leftBoot.xpos, pos.y+leftBoot.ypos,50, 50, null);
		}
		rightBoot.Draw(g);
		if(rightBoot.item==null){
			g.drawImage(GamePanel.gearSlots[0][2], pos.x+rightBoot.xpos, pos.y+rightBoot.ypos,50, 50, null);
		}
		for(int i = 0; i<rings.length;i++){
			rings[i].Draw(g);
			if(rings[i].item==null){
				g.drawImage(GamePanel.gearSlots[0][0], pos.x+rings[i].xpos, pos.y+rings[i].ypos,50, 50, null);
			}
		}
		//draw tooltips
		for(int i = 0; i<10;i++){
			for(int j = 0; j<5; j++){
				items[i][j].DrawTooltip(g);
			}
		}
		head.DrawTooltip(g);
		chest.DrawTooltip(g);
		leftGlove.DrawTooltip(g);
		rightGlove.DrawTooltip(g);
		pants.DrawTooltip(g);
		leftBoot.DrawTooltip(g);
		rightBoot.DrawTooltip(g);
		for(int i = 0; i<rings.length;i++){
			rings[i].DrawTooltip(g);
		}
	}
}

