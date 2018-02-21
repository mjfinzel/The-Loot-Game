package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Zone {
	int levelWidth = 250;//250
	int levelHeight = 250;//250
	int xViewDistance = 21;//21
	int yViewDistance = 12;//12
	int monsterCount = 0;
	String name;
	Point spawnPoint = new Point (2,2);
	BufferedImage miniMap;
	Tile[][] map = new Tile[levelWidth][levelHeight];
	//ArrayList<Monster> monsters = new ArrayList<Monster>();
	ArrayList<Furniture> chests = new ArrayList<Furniture>();
	ArrayList<ClickableName> clickables = new ArrayList<ClickableName>();
	ArrayList<Item> groundItems = new ArrayList<Item>();
	ArrayList<Light> lights = new ArrayList<Light>();
	ArrayList<Monster> monstersOnScreen = new ArrayList<Monster>();
	long startDraw=0;
	long startDrawMonsters = 0;
	long startMoveMonsters = 0;
	boolean updated = false;
	boolean hasDarkness = false;
	public Zone(String type){
		name = type;
		if(type=="The Cave"){
			generateCave();
			//hasDarkness = true;
		}
		else if (type=="Town"){
			generateTown();
			//hasDarkness = true;
		}
		else if (type == "testingLevel"){
			levelWidth = 20;
			levelHeight = 20;
			generateTestingLevel();
		}

	}

	public void updateTiles(){
		if(!updated){
			updated = true;
			for(int index = 0;index<20;index++){
				for(int i = 0; i<levelWidth;i++){
					for(int j = 0; j<levelHeight;j++){
						map[i][j].Update(index);
					}
				}
			}
			BufferedImage[][]temp = new BufferedImage[levelWidth][levelHeight];
			for(int i = 0; i<levelWidth;i++){
				for(int j = 0; j<levelWidth;j++){
					temp[i][j]=GamePanel.mapTiles[map[i][j].xID][map[i][j].yID];
				}
			}
			miniMap = Images.combine(temp);
			updated = true;
		}
	}
	public BufferedImage generateMap(){
		BufferedImage[][] mapImage = new BufferedImage[levelWidth][levelHeight];
		for(int i = -1;i<levelWidth+1;i++){
			for(int j = -1;j<levelHeight+1;j++){
				if(i>=0&&j>=0&&i<levelWidth&&j<levelHeight){
					//g.drawImage(mapTiles[GamePanel.zones.get(GamePanel.currentZone).map[i][j].xID][GamePanel.zones.get(GamePanel.currentZone).map[i][j].yID], x+(i*size), y+(j*size),size, size, null);
//					if(map[i][j].explored==false){
//						g.drawImage(unexploredTexture, x+(i*size), y+(j*size),size, size, null);			
//					}
					mapImage[i][j]=GamePanel.mapTiles[GamePanel.zones.get(GamePanel.currentZone).map[i][j].xID][GamePanel.zones.get(GamePanel.currentZone).map[i][j].yID];
				}
//				else{
//					g.drawImage(mapTiles[1][3], x+(i*size), y+(j*size),size, size, null);
//				}
			}
		}
		return Images.combine(mapImage);
	}
	public void generateTestingLevel(){
		for(int i = 0; i<levelWidth;i++){
			for(int j = 0; j<levelHeight; j++){
				if(i<=1||i>=levelWidth-2||j<=1||j>=levelHeight-2){
					map[i][j]=new Tile(i*50,j*50,0,4);
				}
				else{
					map[i][j]=new Tile(i*50,j*50,1,0);
				}
			}
		}
		map[10][10] = new Tile(10*50,10*50,1,3);
		map[10][11] = new Tile(10*50,11*50,1,3);
	}
	public void generateTown(){
		for(int i = 0; i<levelWidth;i++){
			for(int j = 0; j<levelHeight; j++){
				map[i][j]=new Tile(i*50,j*50,0,4);
			}
		}
		int x = levelWidth/2;
		int y = levelHeight/2;
		int xWidth = GamePanel.randomNumber(15, 15);
		int yWidth = GamePanel.randomNumber(16, 16);
		for(int k = x; k<x+xWidth;k++){
			for(int l = y; l<y+yWidth; l++){
				map[k][l].xID=2;
				map[k][l].yID=15;
			}
		}
		map[x+4][y-1].xID=2;
		map[x+4][y-1].yID=14;
		map[x+4][y-1].name="The Cave";
		spawnPoint = new Point(x+(xWidth/2),y+(yWidth/2));

		//GamePanel.lights.add(new Light(150,400,100));
	}
	public void generateCave(){
		int queenRoom = 50;
		for(int i = 0; i<levelWidth;i++){
			for(int j = 0; j<levelHeight; j++){
				map[i][j]=new Tile(i*50,j*50,0,4);
			}
		}
		//generate initial room
		int xWidth = GamePanel.randomNumber(5, 15);
		int yWidth = GamePanel.randomNumber(6, 16);
		int x = 20-yWidth;//levelWidth/2;
		int y = levelHeight-20;//levelHeight/2;

		ArrayList<Rectangle> rooms = new ArrayList<Rectangle>();
		for(int k = x; k<x+xWidth;k++){
			for(int l = y; l<y+yWidth; l++){

				map[k][l].xID=0;
				map[k][l].yID=15;
				if(GamePanel.randomNumber(1, 20)==1){
					map[k][l].yID=GamePanel.randomNumber(16, 17);
				}

			}
		}
		//create a door to town
		int randX = GamePanel.randomNumber(x+1, x+xWidth-1);
		int spawnDoorTries = 0;
		while(map[randX][y+yWidth].yID==15&&spawnDoorTries<200){
			spawnDoorTries++;
			randX = GamePanel.randomNumber(x+1, x+xWidth-1);
		}
		if(spawnDoorTries>=200){//if there is no valid place to spawn a door in the queen's room
			//create a valid space
			for(int xTemp = randX-1;xTemp<randX+1;xTemp++){
				for(int yTemp = y+yWidth;yTemp<y+yWidth+1;yTemp++){
					map[xTemp][yTemp].xID=0;
					map[xTemp][yTemp].yID=15;
				}
			}
			//create door
			map[randX][y+yWidth].xID=0;
			map[randX][y+yWidth].yID=14;
			map[randX][y+yWidth].name="Town";
		}
		else{
			map[randX][y+yWidth].xID=0;
			map[randX][y+yWidth].yID=14;
			map[randX][y+yWidth].name="Town";
		}
		spawnPoint = new Point(randX,y+yWidth);
		rooms.add(new Rectangle(x-1,y-1,xWidth+1,yWidth+1));
		int tries = 0;
		//generate all the other rooms
		for(int i = 0;i<queenRoom+1;i++){
			tries = 0;
			boolean success = false;
			int roll = GamePanel.randomNumber(1, 4);
			Rectangle rect = null;
			System.out.println("i= "+i);
			while(!success&&tries<100){
				//System.out.println(tries);
				tries++;
				success = true;
				//pick a random room from the list of rooms
				int randRoom = rooms.size()-1;//GamePanel.randomNumber(0, rooms.size()-1);
				if(tries>50){
					randRoom = GamePanel.randomNumber(0, rooms.size()-1);
				}
				//pick a random side of this room
				roll = GamePanel.randomNumber(1, 4);
				if(i==queenRoom){
					xWidth = 30;
					yWidth = 20;
				}
				else{
					xWidth = GamePanel.randomNumber(5, 15);
					yWidth = GamePanel.randomNumber(6, 16);
					if(GamePanel.randomNumber(1, 20)==1){
						xWidth = GamePanel.randomNumber(15, 35);
						yWidth = GamePanel.randomNumber(16, 36);
					}
				}
				//generate a random room on this side
				if(roll == 1){//top

					x = GamePanel.randomNumber(rooms.get(randRoom).x-(xWidth-2), rooms.get(randRoom).x+(rooms.get(randRoom).width-2));
					y = rooms.get(randRoom).y-(yWidth)+2;
					rect = new Rectangle(x-2,y-2,xWidth+2,yWidth-1);
					if(x>2&&y>2&&x+xWidth<levelWidth-2&&y+yWidth<levelHeight-2){
						//check all the existing rooms for collision with the new room
						for(int j = 0; j<rooms.size();j++){
							if(rooms.get(j).intersects(rect)){
								//System.out.println("1");
								success = false;
							}
						}
					}
					else{
						//System.out.println("2");
						success = false;
					}
					if(success){

					}
				}
				if(roll == 2){//right

					x = rooms.get(randRoom).x+rooms.get(randRoom).width;
					y = GamePanel.randomNumber(rooms.get(randRoom).y-(yWidth-2), rooms.get(randRoom).y+(rooms.get(randRoom).height-2));
					rect = new Rectangle(x+2,y+2,xWidth+2,yWidth+2);
					if(x>2&&y>2&&x+xWidth+2<levelWidth-2&&y+yWidth<levelHeight-2){
						//check all the existing rooms for collision with the new room
						for(int j = 0; j<rooms.size();j++){
							if(rooms.get(j).intersects(rect)){
								success = false;

							}
						}
					}
					else{
						success = false;
					}
				}
				if(roll == 3){//bottom

					x = GamePanel.randomNumber(rooms.get(randRoom).x-(xWidth-2), rooms.get(randRoom).x+(rooms.get(randRoom).width-2));
					y = rooms.get(randRoom).y+(rooms.get(randRoom).height);
					rect = new Rectangle(x-2,y,xWidth+2,yWidth+2);
					if(x>2&&y>2&&x+xWidth<levelWidth-2&&y+yWidth<levelHeight-2){
						//check all the existing rooms for collision with the new room
						for(int j = 0; j<rooms.size();j++){
							if(rooms.get(j).intersects(rect)){
								success = false;

							}
						}
					}
					else{
						success = false;
					}
				}
				if(roll == 4){//left

					x = rooms.get(randRoom).x-xWidth+1;
					y = GamePanel.randomNumber(rooms.get(randRoom).y-(yWidth-2), rooms.get(randRoom).y+(rooms.get(randRoom).height-2));
					rect = new Rectangle(x-2,y-2,xWidth-1,yWidth+2);
					if(x>2&&y>2&&x+xWidth<levelWidth-2&&y+yWidth<levelHeight-2){
						//check all the existing rooms for collision with the new room
						for(int j = 0; j<rooms.size();j++){
							if(rooms.get(j).intersects(rect)){
								success = false;

							}
						}
					}
					else{
						success = false;
					}
				}

			}
			//System.out.println("roll = "+roll+", x = "+x+", y = "+y);
			rect = new Rectangle(x-1,y-1,xWidth+1,yWidth+1);
			if(tries<100){
				rooms.add(rect);
				int rand = GamePanel.randomNumber(1, 100);
				for(int k = x; k<x+xWidth;k++){
					for(int l = y; l<y+yWidth; l++){
						int temp = i/2;
						if(temp>90){
							temp=90;
						}
						//chance of normal tiles
						if((rand<=100-i||GamePanel.randomNumber(1, 100)>90)&&i!=queenRoom){
							map[k][l].xID=0;
							map[k][l].yID=15;
							if(GamePanel.randomNumber(1, 20)==1){
								map[k][l].yID=GamePanel.randomNumber(16, 17);
							}
							if(GamePanel.randomNumber(1, 15)==1){//20
								int vaseType = 0;
								if(GamePanel.randomNumber(1, 20)==1){
									vaseType = 1;
								}
								map[k][l].vase = new Vase(map[k][l].xpos,map[k][l].ypos,vaseType);
							}
						}
						else{//web covered tiles
							map[k][l].xID=3;
							map[k][l].yID=15;
							if(GamePanel.randomNumber(1, 20)==1&&i!=queenRoom){
								int vaseType = 0;
								if(GamePanel.randomNumber(1, 20)==1){//20
									vaseType = 1;
								}
								map[k][l].vase = new Vase(map[k][l].xpos,map[k][l].ypos,vaseType);

							}
						}
					}
				}
				if(i==queenRoom){
					randX = randX = GamePanel.randomNumber(x+1, x+xWidth-1);
					spawnDoorTries = 0;
					while(map[randX][y+yWidth].yID==15&&spawnDoorTries<200){
						System.out.println("trying to find valid position for door, tried: "+spawnDoorTries+" times.");
						spawnDoorTries++;
						randX = GamePanel.randomNumber(x+1, x+xWidth-1);
					}
					if(spawnDoorTries>=200){//if there is no valid place to spawn a door in the queen's room
						//create a valid space
						for(int xTemp = randX-1;xTemp<randX+1;xTemp++){
							for(int yTemp = y+yWidth;yTemp<y+yWidth+1;yTemp++){
								map[xTemp][yTemp].xID=3;
								map[xTemp][yTemp].yID=15;
							}
						}
						//create door
						map[randX][y+yWidth].xID=3;
						map[randX][y+yWidth].yID=14;
						map[randX][y+yWidth].name="The Cave";
					}
					else{
						map[randX][y+yWidth].xID=3;
						map[randX][y+yWidth].yID=14;
						map[randX][y+yWidth].name="The Cave";
					}
				}
			}
			Monster queen = new Monster((int)(rect.x+(rect.width/2))*50,(int)(rect.y+(rect.height/2))*50,"Spider Queen");
			if(i==queenRoom){
				map[(int)(rect.x+(rect.width/2))][(int)(rect.y+(rect.height/2))].monsters.add(queen);
				monsterCount++;
			}
			//80% chance for room to have monsters
			if(GamePanel.randomNumber(1, 10)<=8||i==queenRoom){
				int spidersAdded = 0;
				while((spidersAdded<100)){
					System.out.println("added spider");
					if(i!=queenRoom){
						System.out.println("queen room");
						spidersAdded = 100;
					}
					spidersAdded++;
					//1 in 10 tiles has a monster in this room
					for(int i2 = rect.x+1;i2<rect.x+rect.width-1;i2++){
						for(int j2 = rect.y+1;j2<rect.y+rect.width-1;j2++){
							if(i2>=0&&j2>=0&&i2<levelWidth&&j2<levelHeight){
								int packSize = GamePanel.randomNumber(10, 20);
								if(i==queenRoom){

									//System.out.println("sds "+spidersAdded);
									if(GamePanel.randomNumber(1, 100)<=20){
										//System.out.println("1");
										if(map[i2][j2].yID==15&&map[i2][j2].xID==3){
											//System.out.println("2");
											int eggCount = GamePanel.randomNumber(1, 3);
											for(int f = 0; f<eggCount;f++){
												//System.out.println("3");
												Monster m = new Monster((i2*50)+GamePanel.randomNumber(0, 30),(j2*50)+GamePanel.randomNumber(0, 30),"Spider Egg");
												double distanceToQueen= Math.sqrt(Math.pow((queen.xpos-m.xpos),2)+Math.pow((queen.ypos-m.ypos),2));
												if(distanceToQueen<=400-GamePanel.randomNumber(1, 400)){
													spidersAdded++;
													m.queen=queen;
													map[i2][j2].monsters.add(m);
													monsterCount++;
												}
												else{
													//System.out.println("distanceToQueen: "+distanceToQueen);
												}
											}
										}

									}
								}
								else{
									if(GamePanel.randomNumber(1, 100)<=packSize){//20
										if(map[i2][j2].yID==15&&map[i2][j2].xID==3){
											//for(int u = 0; u<30;u++){
											Monster m = new Monster((i2*50)+GamePanel.randomNumber(0, 30),(j2*50)+GamePanel.randomNumber(0, 30),"Web Spider");
											if(GamePanel.randomNumber(1, 10)==1){
												m = new Monster(i2*50,j2*50,"Venom Spider");
											}
											map[i2][j2].monsters.add(m);
											//}
										}
										if(map[i2][j2].yID==15&&map[i2][j2].xID==0){
											//for(int u = 0; u<30;u++){
											Monster m = new Monster(i2*50,j2*50,"Bat");
											map[i2][j2].monsters.add(m);
											monsterCount++;
											//}
										}
									}
								}

							}
						}
					}
				}
			}
		}

		for(int i = 0; i<500;i++){
			int x2 = GamePanel.randomNumber(50, 2450);
			int y2 = GamePanel.randomNumber(100, 2400);
			while(x2<800&&y2<800){
				x2 = GamePanel.randomNumber(50, 2450);
				y2 = GamePanel.randomNumber(100, 2400);
			}
			//monsters.add(new Monster(x,y,"Bat"));
		}
	}
	public void update(){
		//update monsters
		monstersOnScreen.clear();
		//loop over the area on the map that is visible to the player to deal with monsters
		for(int i = (int)((GamePanel.player.xpos/50)-xViewDistance); i<(int)((GamePanel.player.xpos/50)+xViewDistance);i++){
			for(int j = (int)((GamePanel.player.ypos/50)-yViewDistance); j<(int)((GamePanel.player.ypos/50)+yViewDistance);j++){
				//make sure not to loop over part of the arrayList that is out of bounds
				if(i>=0&&j>=0&&i<levelWidth&&j<levelHeight){
					//loop through each monster on this tile
					for(int k = map[i][j].monsters.size()-1; k>=0;k--){
						//remove dead monsters
						if(map[i][j].monsters.get(k).currentHealth<=0){
							map[i][j].monsters.remove(k);
						}
						else{//if the monster is not dead
							int count = 0;
							//add the monster to all the tiles it is on
							for(int x = (int) ((map[i][j].monsters.get(k).xpos-map[i][j].monsters.get(k).width/2)/50);x<=(int)((map[i][j].monsters.get(k).xpos+map[i][j].monsters.get(k).width/2)/50);x++){
								for(int y = (int) ((map[i][j].monsters.get(k).ypos-map[i][j].monsters.get(k).height/2)/50);y<=(int)((map[i][j].monsters.get(k).ypos+map[i][j].monsters.get(k).height/2)/50);y++){
									//System.out.println("count: "+count);
									count++;
									//make sure this loop stays within the bounds of the map array
									if(x>=0&&y>=0&&x<levelWidth&&y<levelHeight){
										//if this tile doesn't already contain the monster
										if(!map[x][y].monsters.contains(map[i][j].monsters.get(k))){
											//add the monster
											map[x][y].monsters.add(map[i][j].monsters.get(k));
											//System.out.println("added a monster ------------");
										}
									}
								}
							}
							//if the monster isn't touching this tile remove it from this tile
							if(map[i][j].monsters.get(k).xpos+(map[i][j].monsters.get(k).width/2)<i*50||map[i][j].monsters.get(k).xpos-(map[i][j].monsters.get(k).width/2)>(i*50)+50||map[i][j].monsters.get(k).ypos+(map[i][j].monsters.get(k).height/2)<(j*50)||map[i][j].monsters.get(k).ypos-(map[i][j].monsters.get(k).height/2)>(j*50)+50){
								//remove the monster from this tile
								map[i][j].monsters.remove(k);
							}
							else{
								if(!monstersOnScreen.contains(map[i][j].monsters.get(k))){
									monstersOnScreen.add(map[i][j].monsters.get(k));
									//map[i][j].monsters.get(k).Draw(g);				
								}
							}

						}
					}
				}
			}
		}
		//update monsters
		for(int i = 0; i<monstersOnScreen.size();i++){
			monstersOnScreen.get(i).update();
		}
		//update player
		GamePanel.player.Update();
		//update projectiles
		
	}
	public void Draw(Graphics2D g){

		startDraw = System.nanoTime();
		int x2 = (((int)GamePanel.player.xpos)-AppletUI.xoffset+AppletUI.mousePos.x)/50;
		int y2 = (((int)GamePanel.player.ypos)-AppletUI.yoffset+AppletUI.mousePos.y)/50;
		if(x2>=0&&y2>=0&&x2<GamePanel.zones.get(GamePanel.currentZone).levelWidth&&y2<GamePanel.zones.get(GamePanel.currentZone).levelHeight){
			map[(((int)GamePanel.player.xpos)-AppletUI.xoffset+AppletUI.mousePos.x)/50][(((int)GamePanel.player.ypos)-AppletUI.yoffset+AppletUI.mousePos.y)/50].mouseOnThis=true;
		}
		if(!updated){
			updateTiles();
			System.out.println("updated tiles");
		}


		this.startMoveMonsters=System.nanoTime();
		
		//loop over the area on the map that is visible to the player
		GamePanel.player.canMove=false;
		for(int i = (int)((GamePanel.player.xpos/50)-xViewDistance); i<(int)((GamePanel.player.xpos/50)+xViewDistance);i++){
			for(int j = (int)((GamePanel.player.ypos/50)-yViewDistance); j<(int)((GamePanel.player.ypos/50)+yViewDistance);j++){
				if(i>=0&&i<levelWidth&&j>=0&&j<levelHeight)
					map[i][j].Draw(g);
				
			}
		}
		GamePanel.player.canMove = true;
		
		ArrayList<Monster> tempMonsters = new ArrayList<Monster>();
		for(int i = 0; i<monstersOnScreen.size();i++){
			if(monstersOnScreen.get(i)!=null){
			if(monstersOnScreen.get(i).name=="Spider Egg"){
				monstersOnScreen.get(i).Draw(g);
			}
			else{
				tempMonsters.add(monstersOnScreen.get(i));
			}
			}
			else{
				
			}
		}
		for(int i = 0; i<tempMonsters.size();i++){
			tempMonsters.get(i).Draw(g);
		}
		for(int i = 0; i<chests.size();i++){
			chests.get(i).Draw(g);
		}
		for(int i = 0; i<clickables.size();i++){
			clickables.get(i).Draw(g);
		}
		for(int i = 0; i<groundItems.size();i++){
			groundItems.get(i).tooltip.Draw(g,true);
		}
		this.startDrawMonsters=System.nanoTime();
		

		//long monsterUpdateTime = (System.nanoTime()-startMoveMonsters);
		//long monsterDrawTime = (System.nanoTime()-startDrawMonsters);
		//long totalTime = (System.nanoTime()-startDraw);
		//long endMonsterUpdateTime = (int)((double)((double)monsterUpdateTime/(double)totalTime)*100);
		//long endMonsterDrawTime = (int)((double)((double)monsterDrawTime/(double)totalTime)*100);
		//System.out.println("times: "+monsterUpdateTime+" , "+monsterDrawTime+" , "+totalTime);
		//System.out.println("Monster update and tile draw time: "+endMonsterUpdateTime+"%, Monster draw Time: "+endMonsterDrawTime+"% ,"+ "Other: "+(100-endMonsterUpdateTime-endMonsterDrawTime)+"%");
	}
}
