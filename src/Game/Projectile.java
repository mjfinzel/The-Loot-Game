package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Projectile {
	double xpos;
	double ypos;
	double rotationRate = 0;
	double damage;
	int pierces = 0;
	int chains = 0;
	int updatesSinceLastMove = 0;
	int bounces = 0;
	int turnSpeed = 0;
	double distanceTraveled = 0;
	double angleInDegrees;
	double angleInRadians;
	double drawAngleInDegrees;
	ArrayList<StatusEffect> effects = new ArrayList<StatusEffect>();
	Explosion explosion;
	double speed = 10.0;
	Point destination;
	int ID;
	BufferedImage artwork;
	int pierce = 0;//-1 = infinite, 0 = does not pierce, 1
	int knockback;
	int range;
	Animation anim = null;
	Item item;
	int updates = 0;
	ArrayList<Monster> monstersHit = new ArrayList<Monster>();
	ArrayList<Animation> particles = new ArrayList<Animation>();
	boolean breakable = false;
	boolean followPlayer = false;
	Light light;
	public Projectile(int x, int y, double angle, int id, Item weapon){
		xpos = x;
		ypos = y;
		item = weapon;
		knockback = weapon.knockback;
		ID = id;
		if(weapon!=null){
			pierce = weapon.getPierce();
			speed = weapon.getProjectileSpeed();
		}
		//destination = dest;
		angleInDegrees = angle;
		drawAngleInDegrees = angle;
		if(id==1){//arrow
			//pierce = 0;
			artwork = GamePanel.arrow;
			range = weapon.getRange();
			speed = 10;
			damage = weapon.getDamage(); 
			//light = new Light((int)xpos,(int)ypos,10,10,10,2);
			
		}
		if(id==2){//golden arrow
			//pierce = 3;
			artwork = GamePanel.goldenArrow;
			speed = 5.0;
			bounces = 3;
			range = weapon.getRange();
			damage = weapon.getDamage();
			//light = new Light((int)xpos,(int)ypos,40,40,-1,2);
			//light.lightColor=new Color(0,255,0,55);
			//GamePanel.lights.add(light);
		}
		if(id==3){//giraffe
			artwork = Images.load("/Textures/Giraffe.png");
			range = weapon.getRange();
			damage = weapon.getDamage(); 
			rotationRate = 2;
			bounces = 2;
		}
		if(id==4){//web shot
			//pierce = 0;
			artwork = GamePanel.webShot;
			range = 400;
			speed = 10;
			damage = weapon.getDamage(); 
			effects.add(new StatusEffect("Webbed",3,null,item));
		}
		if(id==5){//fireball
			artwork = GamePanel.fireball;
			range = weapon.getRange();
			speed = 6;
			bounces = 1;
			knockback = 150;
			damage = weapon.getDamage();
			if(damage<=1){
				damage = 3;
			}
			effects.add(new StatusEffect("On Fire",5,null,item));
			light = new Light((int)xpos,(int)ypos,100,100,2,2);
			GamePanel.lights.add(light);
		}
		if(id==6){//ball lightning
			artwork = GamePanel.ballLightning[0][0];
			range = 1500;
			bounces = 0;
			speed = 3;
			turnSpeed = 1;
			knockback = 0;
			breakable = true;
			followPlayer = true;
			damage = weapon.getDamage();
			anim = new Animation(GamePanel.ballLightning, 4, 100, 0, (int)xpos, (int)ypos, true, artwork.getWidth()/2, artwork.getHeight()/2);
		}
	}
	public void knockbackMonster(Monster monster){
		if(item!=null){
			double knockbackAngle = Math.toDegrees(Math.atan2((monster.ypos-GamePanel.player.ypos),(monster.xpos-GamePanel.player.xpos)));
			Point knockbackPos = new Point((int)(Math.cos(Math.toRadians(knockbackAngle))*knockback*(1.0-monster.knockbackResist)),(int)(Math.sin(Math.toRadians(knockbackAngle))*knockback*(1.0-monster.knockbackResist)));
			Rectangle monsterBox = new Rectangle(-15+(int)monster.xpos+knockbackPos.x,-15+(int)monster.ypos+knockbackPos.y,30,30);
			if(GamePanel.zones.get(GamePanel.currentZone).map[(int)(monster.xpos+knockbackPos.x)/50][(int)(monster.ypos+knockbackPos.y)/50].collisionBox!=null){
				if(!GamePanel.zones.get(GamePanel.currentZone).map[(int)(monster.xpos+knockbackPos.x)/50][(int)(monster.ypos+knockbackPos.y)/50].collisionBox.intersects(monsterBox)){
					monster.xpos=monster.xpos+knockbackPos.x;
					monster.ypos=monster.ypos+knockbackPos.y;
				}
			}
			else{
				monster.xpos=monster.xpos+knockbackPos.x;
				monster.ypos=monster.ypos+knockbackPos.y;
			}
		}
	}
	public void applyEffectToMonster(Monster monster){
		for(int i = 0; i<effects.size();i++){
			boolean hadBuff = false;
			for(int j = 0; j<monster.buffs.size();j++){

				if(monster.buffs.get(j).name==effects.get(i).name){
					if(monster.buffs.get(j).stacks<monster.buffs.get(j).maxStacks){
						hadBuff=true;
						monster.buffs.get(j).stacks++;
					}
					else{
						hadBuff=true;
						//find the stack that expires soonest and reset it's update counter
						int largest = 0;
						int largestIndex = 0;
						for(int k = 0; k<monster.buffs.get(j).updates.size();k++){
							if(monster.buffs.get(j).updates.get(k)>largest){
								largest = monster.buffs.get(j).updates.get(k);
								largestIndex = k;
							}
						}
						monster.buffs.get(j).updates.set(largestIndex,0);
					}
				}

			}
			if(!hadBuff){
				String tempString =effects.get(i).name;
				StatusEffect temp = new StatusEffect(tempString,(int)effects.get(i).duration,monster,item);
				monster.buffs.add(temp);
			}
		}
	}
	public void changeDirection(double desiredAngleInDegrees){//-1 means turn left 1 degree, 1 means turn right 1 degree
		double direction=0;
		if(angleInDegrees>desiredAngleInDegrees){
			double difference = angleInDegrees-desiredAngleInDegrees;

			if(difference<180){
				direction = -turnSpeed;
			}
			else{
				direction = turnSpeed;
			}
		}
		else{
			double difference = desiredAngleInDegrees-angleInDegrees;
			if(difference<180){
				direction=turnSpeed;
			}
			else{
				direction=-turnSpeed;
			}
		}
		if(desiredAngleInDegrees!=this.angleInDegrees){
			angleInDegrees+=direction;
			if(angleInDegrees < 0){
				angleInDegrees += 360;
			}
			else if(angleInDegrees>360){
				angleInDegrees-=360;
			}
		}
		else if(desiredAngleInDegrees!=this.angleInDegrees&&Math.abs(desiredAngleInDegrees-angleInDegrees)<=this.turnSpeed){
			angleInDegrees=desiredAngleInDegrees;
		}
	}
	public void Draw(Graphics2D g){
		if(light!=null&&GamePanel.zones.get(GamePanel.currentZone).hasDarkness){
			light.xpos=(int)xpos;
			light.ypos=(int)ypos;
			//light.render();
		}
		if(updates>=10&&ID==6){
			Animation temp = new Animation(GamePanel.electricArc, 4, 400, 0, (int)xpos, (int)ypos, false, artwork.getWidth()/2, artwork.getHeight()/2);
			temp.angle=GamePanel.randomNumber(1, 360);
			particles.add(temp);
			temp = new Animation(GamePanel.electricArc, 4, 400, 0, (int)xpos, (int)ypos, false, artwork.getWidth()/2, artwork.getHeight()/2);
			temp.angle=GamePanel.randomNumber(1, 360);
			particles.add(temp);
			updates=0;
		}
		updates++;
		//draw particles
		for(int i = 0; i<particles.size();i++){
			particles.get(i).xpos=AppletUI.xoffset+(int)(xpos)-((int)GamePanel.player.xpos);
			particles.get(i).ypos=AppletUI.yoffset+(int)(ypos-(artwork.getHeight()/2))-((int)GamePanel.player.ypos);
			particles.get(i).Draw(g);
			if(particles.get(i).getCurrentFrame()>=particles.get(i).getFrameCount()-1){
				particles.remove(i);
			}
		}
		if(this.ID==5){//fire arrow
			for(int i = 0;i<speed;i++){
				int tempWidth = GamePanel.randomNumber(1, 3);
				int tempHeight = GamePanel.randomNumber(1, 7);
				//add a fire particle to the current location
				Animation temp = new Animation(GamePanel.fireAnim, 20, 5, 0, (int)((double)(xpos-(tempWidth/2)+((Math.cos(angleInRadians)*(double)i)))), (int)((double)(ypos-(tempHeight/2)+((Math.sin(angleInRadians)*(double)i)))), false, 0, 0);
				temp.xScale=tempWidth;
				temp.yScale=tempHeight;
				GamePanel.effects.add(temp);
			}
		}
		//angleInDegrees=Math.toDegrees(Math.atan2((destination.y-(AppletUI.yoffset+ypos-(int)GamePanel.player.ypos)),((destination.x-(AppletUI.xoffset+xpos-(int)GamePanel.player.xpos)))));
		if(followPlayer==true){
			changeDirection(Math.toDegrees(Math.atan2((GamePanel.player.ypos-ypos),((GamePanel.player.xpos-xpos)))));
			angleInRadians = Math.toRadians(angleInDegrees);
		}
		else{
			angleInRadians = Math.toRadians(angleInDegrees);
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(anim==null){
			AffineTransform at = new AffineTransform();
			//set position on screen
			at.translate(AppletUI.xoffset+xpos-(int)GamePanel.player.xpos-((double)((double)(artwork.getWidth())/2.0)), AppletUI.yoffset+ypos-(int)GamePanel.player.ypos-((double)((double)(artwork.getHeight())/2.0)));
			//set size of image
			at.scale(1.0, 1.0);
			drawAngleInDegrees+=rotationRate;
			//rotate image
			at.rotate(Math.toRadians(drawAngleInDegrees),(double)((double)artwork.getWidth()/2.0),((double)((double)(artwork.getHeight())/2.0)));
			//draw image
			g2d.drawImage(artwork, at, null);
		}
		else{
			anim.angle=angleInDegrees;
			anim.xpos=AppletUI.xoffset+(int)(xpos-(int)GamePanel.player.xpos)-(int)((double)((double)(artwork.getWidth())/2.0));
			anim.ypos=AppletUI.yoffset+(int)(ypos)-(int)GamePanel.player.ypos-(int)((double)((double)(artwork.getHeight())/2.0));
			anim.Draw(g2d);
		}

		double oldx = xpos;
		double oldy = ypos;
		double newX = xpos+(Math.cos(angleInRadians)*speed);
		double newY = ypos+(Math.sin(angleInRadians)*speed);
		if(newX>=0&&newY>=0&&newX<GamePanel.zones.get(GamePanel.currentZone).levelWidth*50&&newY<GamePanel.zones.get(GamePanel.currentZone).levelHeight*50){
			//xpos += (Math.cos(angleInRadians)*speed);
			//ypos += (Math.sin(angleInRadians)*speed);
			////////////////////////////
			double moveSpeed = speed;
			Line2D line = new Line2D.Double(oldx,oldy,newX,newY);
			//g.setColor(Color.red);
			//g.drawLine((int)(AppletUI.xoffset+oldx-(int)GamePanel.player.xpos),(int)(AppletUI.yoffset+oldy-(int)GamePanel.player.ypos),(int)(AppletUI.xoffset+newX-(int)GamePanel.player.xpos),(int)(AppletUI.yoffset+newY-(int)GamePanel.player.ypos));
			//if the collision box at the new position is not null
			//if(GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos+(Math.cos(angleInRadians)*moveSpeed))/50][(int)(ypos+(Math.sin(angleInRadians)*moveSpeed))/50].collisionBox!=null){
			//determine which box was hit first if more than one was hit
			int startX = (int) ((int)oldx/50);
			int startY = (int) ((int)oldy/50);
			int endX = (int) ((int)newX/50);
			int endY = (int) ((int)newY/50);
			if(oldy>newY){
				startY = (int) ((int)newY/50);
				endY = (int) ((int)oldy/50);
			}
			if(oldx>newX){
				startX = (int) ((int)newX/50);
				endX = (int) ((int)oldx/50);
			}
			Rectangle closestBox = null;//GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos+(Math.cos(angleInRadians)*moveSpeed))/50][(int)(ypos+(Math.sin(angleInRadians)*moveSpeed))/50].collisionBox;
			for(int i = startX; i<=endX;i++){
				for(int j = startY; j<=endY;j++){
					if(GamePanel.zones.get(GamePanel.currentZone).map[i][j].collisionBox!=null){
						if(closestBox==null&&line.intersects(GamePanel.zones.get(GamePanel.currentZone).map[i][j].collisionBox)){
							closestBox = GamePanel.zones.get(GamePanel.currentZone).map[i][j].collisionBox;
						}
						if(closestBox!=null){
							Rectangle temp = GamePanel.zones.get(GamePanel.currentZone).map[i][j].collisionBox;
							if(line.intersects(temp)){
								//g.setColor(Color.blue);
								//g.drawRect(AppletUI.xoffset+temp.x-((int)GamePanel.player.xpos), AppletUI.yoffset+temp.y-((int)GamePanel.player.ypos), temp.width, temp.height);

								double distanceToTemp= Math.sqrt(Math.pow(((temp.x+((double)temp.getWidth()/2.0))-oldx),2)+Math.pow(((temp.y+((double)temp.getHeight()/2.0))-oldy),2));
								double distanceToClosest= Math.sqrt(Math.pow(((closestBox.x+((double)closestBox.getWidth()/2.0))-oldx),2)+Math.pow(((closestBox.y+((double)closestBox.getHeight()/2.0))-oldy),2));

								if(distanceToTemp<distanceToClosest){
									closestBox = temp;
								}
							}
						}

					}
				}
			}
			if(closestBox!=null){ 
				//if the line between it's current position and new position will not intercept the collision box at the new position
				if(!line.intersects(closestBox)){
					xpos += (Math.cos(angleInRadians)*moveSpeed);
					ypos += (Math.sin(angleInRadians)*moveSpeed);
				}
				else if(bounces>0){



					double wallAngle = 90;

					Rectangle box = closestBox;//GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos+(Math.cos(angleInRadians)*moveSpeed))/50][(int)(ypos+(Math.sin(angleInRadians)*moveSpeed))/50].collisionBox;
					double angleBetweenArrowAndWall=(Math.atan2((((double)box.y+((double)box.height/2.0))-oldy),(((double)box.x+((double)box.width/2.0))-oldx)));
					double halfPi = Math.PI/2.0;
					double theta = Math.atan2(box.getHeight(), box.getWidth());
					if (angleBetweenArrowAndWall >= theta+halfPi || angleBetweenArrowAndWall <= -theta-halfPi) {
						wallAngle = 0;
					} else if (angleBetweenArrowAndWall >= theta) {
						wallAngle = 90;
					} else if (angleBetweenArrowAndWall >= -theta) {
						wallAngle = 0;
					} else {
						wallAngle = 90;
					}
					//wallAngle = 90;
					//System.out.println("boing "+bounces);

					angleInDegrees = (2*wallAngle)-angleInDegrees-180;
					if(angleInDegrees<0){
						angleInDegrees+=360;
					}
					drawAngleInDegrees = angleInDegrees;
					//System.out.println("angle: "+angleInDegrees);
					angleInRadians = Math.toRadians(angleInDegrees);
					//xpos += (Math.cos(angleInRadians)*moveSpeed);
					//ypos += (Math.sin(angleInRadians)*moveSpeed);
					bounces--;

				}
				//				Rectangle temp = GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos+(Math.cos(angleInRadians)*moveSpeed))/50][(int)(ypos+(Math.sin(angleInRadians)*moveSpeed))/50].collisionBox;
				//				g.setColor(Color.red);
				//				g.drawRect((int)(AppletUI.xoffset+temp.x-GamePanel.player.xpos), (int)(AppletUI.yoffset+temp.y-GamePanel.player.ypos), temp.width, temp.height);
			}
			else{
				xpos += (Math.cos(angleInRadians)*moveSpeed);
				ypos += (Math.sin(angleInRadians)*moveSpeed);
			}

		}


		distanceTraveled+=Math.sqrt(Math.pow((Math.cos(angleInRadians)*speed), 2)+Math.pow((Math.sin(angleInRadians)*speed), 2));
		if(distanceTraveled<range){
			Line2D line = new Line2D.Double(oldx,oldy,xpos,ypos);
			//g.setColor(Color.red);
			//g.drawLine(AppletUI.xoffset+(int)oldx-(int)GamePanel.player.xpos,AppletUI.yoffset+(int)oldy-(int)GamePanel.player.ypos,(int)(AppletUI.xoffset+xpos-(int)GamePanel.player.xpos),(int)(AppletUI.yoffset+ypos-(int)GamePanel.player.ypos));
			//check monsters for collision with projectile
			if(item.user==null||ID==5){

				for(int i = 0; i<GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.size();i++){
					boolean queenDead = true;
					if(GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.get(i).name=="Spider Egg"){
						if(GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.get(i).queen.currentHealth<=0){
							queenDead = true;
						}
						else{
							queenDead = false;
						}
					}
					if(monstersHit.size()<=pierces&&queenDead){
						int monsterWidth = GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.get(i).width;
						int monsterHeight = GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.get(i).height;
						Rectangle monsterBox = new Rectangle(-(monsterWidth/2)+(int)GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.get(i).xpos,-(monsterHeight/2)+(int)GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.get(i).ypos,monsterWidth,monsterHeight);
						//g.setColor(Color.red);
						//g.drawRect(AppletUI.xoffset+(int)GamePanel.zones.get(GamePanel.currentZone).monsters.get(i).xpos-((int)GamePanel.player.xpos),AppletUI.yoffset+(int)GamePanel.zones.get(GamePanel.currentZone).monsters.get(i).ypos-((int)GamePanel.player.ypos),30,30);

						if(line.intersects(monsterBox)&&!monstersHit.contains(GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.get(i))){
							//apply effects to the monster that was hit
							//for(int k = 0; k<item.effects.size();k++){
							applyEffectToMonster(GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.get(i));
							item.applyEffectToMonster(GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.get(i));
							//}
							//for(int k = 0; k<effects.size();k++){

							//}
							monstersHit.add(GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.get(i));
							if(ID==2){
								//create a few more projectiles
								for(int j = 0; j<1;j++){
									double randomAngle = GamePanel.randomNumber(1, 360);
									GamePanel.projectiles.add(new Projectile((int)xpos, (int)ypos, randomAngle, 1,item));
								}
							}
							if(item!=null){
								if(item.getProjectiles()>1){
									GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.get(i).takeDamage((damage*1.5)/item.getProjectiles(),item);
								}
								else{
									GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.get(i).takeDamage((damage)/item.getProjectiles(),item);
								}
								if(GamePanel.player.currentHealth<GamePanel.player.getMaxHealth()){
									GamePanel.player.currentHealth+=item.getLifeOnHit();
								}
							}
							//double knockback = item.knockback;
							//double knockbackAngle = Math.toDegrees(Math.atan2((GamePanel.zones.get(GamePanel.currentZone).monsters.get(i).ypos-GamePanel.player.ypos),(GamePanel.zones.get(GamePanel.currentZone).monsters.get(i).xpos-GamePanel.player.xpos)));
							//Point knockbackPos = new Point((int)(Math.cos(Math.toRadians(knockbackAngle))*knockback),(int)(Math.sin(Math.toRadians(knockbackAngle))*knockback));
							//GamePanel.zones.get(GamePanel.currentZone).monsters.get(i).xpos=GamePanel.zones.get(GamePanel.currentZone).monsters.get(i).xpos+knockbackPos.x;
							//GamePanel.zones.get(GamePanel.currentZone).monsters.get(i).ypos=GamePanel.zones.get(GamePanel.currentZone).monsters.get(i).ypos+knockbackPos.y;
							if(i<GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.size()){
								knockbackMonster(GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.get(i));
							}
							if(chains==0){
								if(pierces>=pierce){
									GamePanel.lights.remove(light);
									GamePanel.projectiles.remove(this);
								}
								else{
									pierces++;
								}
							}

						}
					}
				}
			}
			else{

				Rectangle playerBox = new Rectangle((int)GamePanel.player.xpos-25,(int)GamePanel.player.ypos-25,50,50);
				if(line.intersects(playerBox)){

					for(int i = 0; i<effects.size();i++){
						boolean hadBuff = false;
						for(int j = 0; j<GamePanel.player.buffs.size();j++){

							if(GamePanel.player.buffs.get(j).name==effects.get(i).name){
								if(GamePanel.player.buffs.get(j).stacks<GamePanel.player.buffs.get(j).maxStacks){
									hadBuff=true;
									GamePanel.player.buffs.get(j).stacks++;
								}
								else{
									hadBuff=true;
									//find the stack that expires soonest and reset it's update counter
									int largest = 0;
									int largestIndex = 0;
									for(int k = 0; k<GamePanel.player.buffs.get(j).updates.size();k++){
										if(GamePanel.player.buffs.get(j).updates.get(k)>largest){
											largest = GamePanel.player.buffs.get(j).updates.get(k);
											largestIndex = k;
										}
									}
									GamePanel.player.buffs.get(j).updates.set(largestIndex,0);
								}
							}

						}
						if(!hadBuff){
							GamePanel.player.buffs.add(effects.get(i));
						}
					}


					GamePanel.player.takeDamage(damage);
					if(chains==0){
						if(pierces>=pierce){
							GamePanel.lights.remove(light);
							GamePanel.projectiles.remove(this);
						}
						else{
							pierces++;
						}
					}
				}
			}
			double currx = AppletUI.xoffset+xpos-(int)GamePanel.player.xpos;
			double curry = AppletUI.yoffset+ypos-(int)GamePanel.player.ypos;

			//check for collision with vases
			for(int x = (int)(oldx/50.0); x<xpos/50;x++){
				for(int y = (int)(oldy/50.0); y<ypos/50;y++){

					if(GamePanel.zones.get(GamePanel.currentZone).map[x][y].vase!=null&&GamePanel.zones.get(GamePanel.currentZone).map[x][y].vase.brokenState==0){
						Rectangle vaseBox = new Rectangle(GamePanel.zones.get(GamePanel.currentZone).map[x][y].xpos,GamePanel.zones.get(GamePanel.currentZone).map[x][y].ypos,50,50);
						//g.setColor(Color.red);
						//g.drawRect(AppletUI.xoffset+GamePanel.zones.get(GamePanel.currentZone).map[x][y].xpos-((int)GamePanel.player.xpos),AppletUI.yoffset+GamePanel.zones.get(GamePanel.currentZone).map[x][y].ypos-((int)GamePanel.player.ypos),50,50);
						if(line.intersects(vaseBox)){
							if(ID==2){
								//create a few more projectiles
								for(int j = 0; j<5;j++){
									double randomAngle = GamePanel.randomNumber(1, 360);
									GamePanel.projectiles.add(new Projectile((int)xpos, (int)ypos, randomAngle, 1,item));
								}
							}
							GamePanel.zones.get(GamePanel.currentZone).map[x][y].vase.breakOpen(item);
							if(pierces>=pierce){
								GamePanel.lights.remove(light);
								GamePanel.projectiles.remove(this);
							}
							else{
								pierces++;
							}
						}
					}
				}
			}

		}
		else{
			GamePanel.lights.remove(light);
			GamePanel.projectiles.remove(this);
		}
		if(light!=null&&GamePanel.zones.get(GamePanel.currentZone).hasDarkness){
			light.xpos=(int)xpos;
			light.ypos=(int)ypos;
			//light.render();
		}
	}
}
