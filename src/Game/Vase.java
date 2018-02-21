package Game;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Vase {
	int xpos;
	int ypos;
	int brokenState = 0;
	int id;
	MappedLight testLight;
	public Vase(int x, int y, int ID){
		xpos = x;
		ypos = y;
		id=ID;

		//testLight = new MappedLight(x,y,1,Images.load("/Textures/VaseLightMap.png"));

	}
	public void breakOpen(Item weapon){
		if(brokenState==0){
			brokenState = GamePanel.randomNumber(1, 3);
			if(GamePanel.randomNumber(1, 10)<2){//20% chance to drop an item
				dropItem(0);
			}
			if(id==0){//brown vase
				if(GamePanel.randomNumber(1, 100)<=1){
					Monster m = new Monster(xpos+25,ypos+25,"Spirit");
					GamePanel.zones.get(GamePanel.currentZone).map[xpos/50][ypos/50].monsters.add(m);
				}
			}
			else if(id==1){//explosive vase
				double amount = 50;
				for(int i = 0; i<amount;i++){
					Projectile temp = new Projectile((int)xpos+25, (int)ypos+30, (360/amount)*i, 5,weapon);
					temp.range=GamePanel.randomNumber(20, 200);
					temp.speed=GamePanel.randomNumber(4, 7);
					temp.pierce=999;
					GamePanel.projectiles.add(temp);
				}
			}
		}		
	}
	public void dropItem(double increasedRarity){

		int roll = GamePanel.randomNumber(1, 1000);
		Item drop = null;
		int x = (int)xpos;
		int y = (int)ypos;
		int chanceToDropItem = 4;
		//40% chance to drop anything
		if(GamePanel.randomNumber(1, 10)<=chanceToDropItem){
			if(roll>200&&roll<=1000){//30% chance to drop gear
				int rand = GamePanel.randomNumber(1, 22);
				if(rand==1){
					drop = new Item(x,y,"Sword",increasedRarity);
				}
				else if(rand==2){
					drop = new Item(x,y,"Simple Bow",increasedRarity);
				}
				else if(rand==3){
					drop = new Item(x,y,"Chain Helmet",increasedRarity);
				}
				else if(rand==4){
					drop = new Item(x,y,"Chain Vest",increasedRarity);
				}
				else if(rand==5){
					drop = new Item(x,y,"Chain Pants",increasedRarity);
				}
				else if(rand==6){
					drop = new Item(x,y,"Chain Glove",increasedRarity);
				}
				else if(rand==7){
					drop = new Item(x,y,"Chain Boot",increasedRarity);
				}
				else if(rand==8){
					drop = new Item(x,y,"Plate Helmet",increasedRarity);
				}
				else if(rand==9){
					drop = new Item(x,y,"Plate Vest",increasedRarity);
				}
				else if(rand==10){
					drop = new Item(x,y,"Plate Pants",increasedRarity);
				}
				else if(rand==11){
					drop = new Item(x,y,"Plate Glove",increasedRarity);
				}
				else if(rand==12){
					drop = new Item(x,y,"Plate Boot",increasedRarity);
				}
				else if(rand==13){
					drop = new Item(x,y,"Cloth Hood",increasedRarity);
				}
				else if(rand==14){
					drop = new Item(x,y,"Cloth Shirt",increasedRarity);
				}
				else if(rand==15){
					drop = new Item(x,y,"Cloth Pants",increasedRarity);
				}
				else if(rand==16){
					drop = new Item(x,y,"Cloth Glove",increasedRarity);
				}
				else if(rand==17){
					drop = new Item(x,y,"Cloth Shoe",increasedRarity);
				}
				else if(rand==18){
					drop = new Item(x,y,"Shockwave Staff",increasedRarity);
				}
				else if(rand==19){
					drop = new Item(x,y,"Teleport Staff",increasedRarity);
				}
				else if(rand==20){
					drop = new Item(x,y,"Dagger",increasedRarity);
				}
				else if(rand==21){
					drop = new Item(x,y,"Splinter Bow",increasedRarity);
				}
				else if(rand==22){
					int temp = GamePanel.randomNumber(1, 100);
					if(temp>36){//64%
						drop = new Item(x,y,"Iron Ring",increasedRarity);
					}
					else if(temp>6&&temp<=36){//30%
						drop = new Item(x,y,"Silver Ring",increasedRarity);
					}
					else if(temp>1&&temp<=6){//5%
						drop = new Item(x,y,"Gold Ring",increasedRarity);
					}
					else if(temp==1){//1%
						drop = new Item(x,y,"Emerald Ring",increasedRarity);
					}
				}
			}
			else if(roll<=200){//10% chance to drop currency
				int rand = GamePanel.randomNumber(1, 100);
				if(rand>40){
					drop = new Item(x,y,"Whetstone",increasedRarity);//60%
				}
				else if(rand>20){
					drop = new Item(x,y,"Greater Whetstone",increasedRarity);//20%
				}
				else if(rand>10){
					drop = new Item(x,y,"Engraving Chisel",increasedRarity);//10%
				}
				else if(rand>2){
					drop = new Item(x,y,"Shaping Hammer",increasedRarity);//8%
				}
				else{
					drop = new Item(x,y,"Superior Whetstone",increasedRarity);//2%
				}
			}
			if(drop!=null){
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
						int width = GamePanel.getStringWidth(drop.tooltip.itemName,font)+40;
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
				drop.xpos=x;
				drop.ypos=y;
				GamePanel.zones.get(GamePanel.currentZone).groundItems.add(drop);
			}
		}
	}

	public void Draw(Graphics g){
		g.drawImage(GamePanel.vase[brokenState][id], AppletUI.xoffset+xpos-((int)GamePanel.player.xpos), AppletUI.yoffset+ypos-((int)GamePanel.player.ypos),50, 50, null);
		if(testLight!=null){
			//testLight.render();
			//testLight.setAngle(0);
		}
	}
}
