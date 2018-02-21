package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Monster {
	double xpos;
	double ypos;
	double currentHealth;
	double maxHealth;
	int damage = 6;
	int width = 30;
	int height = 30;
	double speed = 1.2;
	double attackSpeed = 1.0;
	int shootCounter = 0;
	int chargeCounter = 0;
	double angleInDegrees;
	double projectileAngle=0;
	double desiredAngleInDegrees;
	double angleInRadians;
	double knockbackResist = 0;
	double increasedDropRarity = 0;
	boolean canSeePlayer = false;
	//public long lastTimeDamaged;
	long lastAttackTime;
	Item weapon;
	Point anchor;
	Point randomDest = new Point(0,0);
	Animation anim;
	String name;
	int affiliation = 0;//0=attack player, 1 = attack monsters, 3 = attack both
	boolean runsAway = false;
	boolean cantMove = false;
	boolean charging = false;
	double chargeRange = 0;
	double distanceCharged = 0;
	long spawnTime=0;
	long lastHatchTime = 0;
	double lifeRegen = 0;
	int hatchRate = 5000;
	Monster queen;
	ArrayList <StatusEffect> buffs = new ArrayList<StatusEffect>();
	MappedLight bodyLights;
	Light light;
	public Monster(int x, int y, String Name){
		xpos = x;
		ypos = y;
		anchor = new Point(x,y);
		randomDest.x=x;
		randomDest.y=y;
		name = Name;
		if(name=="Bat"){
			anim = new Animation(GamePanel.bat,4,200,1,AppletUI.xoffset, AppletUI.yoffset,true,GamePanel.bat[0][0].getWidth()/2,GamePanel.bat[0][0].getHeight()/2);
			maxHealth = 35;
			damage = 6;
			currentHealth = maxHealth;
			//light = new Light((int)xpos,(int)ypos,1);
		}
		if(name=="Spider Egg"){
			anim = new Animation(GamePanel.spiderEgg,1,2000,GamePanel.randomNumber(0, 2),AppletUI.xoffset, AppletUI.yoffset,true,0,0);
			maxHealth = 20;
			height = 20;
			width = 20;
			speed = 0;
			damage = 0;
			increasedDropRarity = .2;
			knockbackResist = 1.0;
			cantMove = true;
			currentHealth = maxHealth;
		}
		if(name=="Web Spider"){
			anim = new Animation(GamePanel.spider,16,30,1,AppletUI.xoffset, AppletUI.yoffset,true,20,20);
			maxHealth = 50;
			width = 40;
			height = 40;
			speed = .6;
			damage = 9;
			currentHealth = maxHealth;
			weapon = new Item(-1,-1,"Spider Web Shooter",increasedDropRarity);
			weapon.user=this;
			//bodyLights = new MappedLight(xpos,ypos,2,GamePanel.spiderLights[0][0]);
		}
		if(name=="Venom Spider"){
			anim = new Animation(GamePanel.spider,8,30,0,AppletUI.xoffset, AppletUI.yoffset,true,15,15);
			maxHealth = 20;
			width = 30;
			height = 30;
			anim.xScale=width/40.0;
			anim.yScale=height/40.0;
			speed = 1.2;
			damage = 5;
			currentHealth = maxHealth;
			weapon = new Item(-1,-1,"Poison Fang",increasedDropRarity);
			weapon.user=this;
		}
		if(name=="Spider Hatchling"){
			anim = new Animation(GamePanel.spider,8,30,2,AppletUI.xoffset, AppletUI.yoffset,true,10,10);
			maxHealth = 20;
			width = 20;
			height = 20;
			anim.xScale=width/40.0;
			anim.yScale=height/40.0;
			speed = 2.0;
			damage = 5;
			currentHealth = maxHealth;
			weapon = new Item(-1,-1,"Poison Fang",increasedDropRarity);
			weapon.user=this;
		}
		if(name=="Spider Queen"){
			anim = new Animation(GamePanel.spiderQueen,5,800,0,AppletUI.xoffset, AppletUI.yoffset,true,100,100);
			maxHealth = 1000;
			width = 200;
			height = 200;
			speed = .2;
			knockbackResist = 1.0;
			chargeRange = 1500;
			increasedDropRarity = 2;
			damage = 18;
			currentHealth = maxHealth;
			weapon = new Item(-1,-1,"Spider Queen's Charge",increasedDropRarity);
			weapon.user=this;
			lifeRegen = 2/60;
		}
		if(name=="Worm"){
			anim = new Animation(GamePanel.wormSegments,4,200,0,AppletUI.xoffset, AppletUI.yoffset,true,0,0);
			maxHealth = 35;
			damage = 6;
			currentHealth = maxHealth;
		}
		if(name=="Spirit"){
			anim = new Animation(GamePanel.spirit,4,200,0,AppletUI.xoffset, AppletUI.yoffset,true,0,0);
			maxHealth = 1;
			damage = 0;
			currentHealth = maxHealth;
			speed = 1;
			runsAway = true;
		}
		if(name=="NPC"){

		}
		spawnTime = System.currentTimeMillis();
	}
	public void setSize(int w, int h){
		width = w;
		height = h;
		anim.xScale=width/40.0;
		anim.yScale=height/40.0;
	}
	public  double getRegen(){
		double total = lifeRegen;
		for(int i = 0; i<buffs.size();i++){
			total+=buffs.get(i).lifeRegen;
		}
		return total;
	}
	public void takeDamage(double Damage, Item weapon){
		if(weapon!=null){
			boolean queenDead = true;
			if(queen!=null){
				queenDead = false;
				if(queen.currentHealth<=0){
					queenDead = true;
				}
			}
			if(spawnTime+600<System.currentTimeMillis()&&queenDead){
				double dmgTaken = Damage+GamePanel.randomNumber(-1, 1);
				if(dmgTaken<1){
					dmgTaken=1;
				}

				currentHealth-=dmgTaken;
				if(weapon.name=="Sword"){
					if(GamePanel.randomNumber(1, 100)<=3){
						for(int i = 0; i<GamePanel.player.inventory.rings.length;i++){
							if(GamePanel.player.inventory.rings[i].item!=null){
								for(int j = 0; j<GamePanel.player.inventory.rings[i].item.modifiers.size();j++){
									if(GamePanel.player.inventory.rings[i].item.modifiers.get(j).name=="ArmorCoverageOnSwordHit"){
										GamePanel.player.inventory.rings[i].item.modifiers.get(j).effect.moreArmorCoverage=GamePanel.player.inventory.rings[i].item.modifiers.get(j).armorCoverageOnSwordHit;
										if(!GamePanel.player.hasBuff(GamePanel.player.inventory.rings[i].item.modifiers.get(j).effect)){
											GamePanel.player.inventory.rings[i].item.modifiers.get(j).effect.updates.clear();
											GamePanel.player.inventory.rings[i].item.modifiers.get(j).effect.updates.add(0);
											GamePanel.player.buffs.add(GamePanel.player.inventory.rings[i].item.modifiers.get(j).effect);
										}
									}
								}
							}
						}
					}
				}

				if(currentHealth<=0){
					if(queenDead){
						GamePanel.zones.get(GamePanel.currentZone).monsterCount--;
						if(!runsAway){
							dropItem(increasedDropRarity);
						}
						else{
							for(int i = 0; i<10;i++){
								dropItem(increasedDropRarity);
							}
						}
						if(weapon.name=="Dagger"){
							//check all of the player's rings
							for(int i = 0; i<GamePanel.player.inventory.rings.length;i++){
								//if the ring isn't null
								if(GamePanel.player.inventory.rings[i].item!=null){
									//loop through all of the ring's modifiers
									for(int j = 0; j<GamePanel.player.inventory.rings[i].item.modifiers.size();j++){
										//if this mod is the movement speed on dagger kill mod
										if(GamePanel.player.inventory.rings[i].item.modifiers.get(j).name=="MovementSpeedOnDaggerKill"){	
											System.out.println("found ring with movespeed on dagger kill");
											//GamePanel.player.inventory.rings[i].item.modifiers.get(j).effect=new StatusEffect("MoveSpeedOnDaggerKill",GamePanel.player.inventory.rings[i].item.modifiers.get(j).effectDuration);
											GamePanel.player.inventory.rings[i].item.modifiers.get(j).effect.movementSpeed=GamePanel.player.inventory.rings[i].item.modifiers.get(j).moveSpeedOnDaggerKill;
											if(!GamePanel.player.hasBuff(GamePanel.player.inventory.rings[i].item.modifiers.get(j).effect)){
												System.out.println("applied buff");
												GamePanel.player.inventory.rings[i].item.modifiers.get(j).effect.updates.clear();
												GamePanel.player.inventory.rings[i].item.modifiers.get(j).effect.updates.add(0);
												GamePanel.player.buffs.add(GamePanel.player.inventory.rings[i].item.modifiers.get(j).effect);
											}
										}
									}
								}
							}
						}
					}
					else{

					}
					//GamePanel.zones.get(GamePanel.currentZone).monsters.remove(this);
				}

				GamePanel.combatText.add(new CombatText((int)xpos,(int)ypos,(int)dmgTaken+"",new Color(255,141,66)));

				canSeePlayer = true;
			}
		}
		else{//not dealt by a weapon
			System.out.println("CurrentHealth1: "+currentHealth);
			currentHealth-=Damage;
			System.out.println("CurrentHealth2: "+currentHealth);
			if(currentHealth<=0){
				GamePanel.zones.get(GamePanel.currentZone).monsterCount--;
				dropItem(increasedDropRarity);
			}
			//GamePanel.combatText.add(new CombatText((int)xpos,(int)ypos,Damage+"",new Color(255,141,66)));
		}
	}
	public void setPosition(double x, double y){
		xpos = x;
		ypos = y;

	}
	public void dropItem(double increasedRarity){

		int roll = GamePanel.randomNumber(1, 1000);
		Item drop = null;
		int x = (int)xpos;
		int y = (int)ypos;
		int chanceToDropItem = 4;
		if(runsAway){
			chanceToDropItem=4;//80%
		}
		//40% chance to drop anything
		if(GamePanel.randomNumber(1, 10)<=chanceToDropItem){
			if(roll>100&&roll<=1000){//30% chance to drop gear
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
			else if(roll<=100){//10% chance to drop currency
				int rand = GamePanel.randomNumber(1, 100);
				if(rand>60){
					drop = new Item(x,y,"Whetstone",increasedRarity);//40%
				}
				//else if(rand>20){
				//drop = new Item(x,y,"Greater Whetstone",increasedRarity);//20%
				//}
				else if(rand>21){
					drop = new Item(x,y,"Engraving Chisel",increasedRarity);//39%
				}
				else if(rand>1){
					drop = new Item(x,y,"Shaping Hammer",increasedRarity);//20%
				}
				else{
					drop = new Item(x,y,"Superior Whetstone",increasedRarity);//1%
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
	public void attackPlayer(Player player){
		if(shootCounter<=0){
			shootCounter=(int)(60.0/attackSpeed);
			double distanceToPlayer = Math.sqrt(Math.pow(((GamePanel.player.xpos)-this.xpos),2)+Math.pow(((GamePanel.player.ypos)-this.ypos),2));
			if(weapon==null||(weapon.itemType=="ProjectileWeapon"&&distanceToPlayer<28)){
				player.takeDamage(damage);
			}
			else if(weapon!=null){
				shootCounter = (int)(60.0/weapon.getAttackSpeed());
				if(weapon.itemType=="MonsterSwingWeapon"){
					weapon.swingWeapon();
				}
				if(weapon.itemType=="Spider Queen Charge"){
					weapon.swingWeapon();
				}
			}

			if(player.currentHealth<=0){
				player.die();
				player.xpos=GamePanel.zones.get(GamePanel.currentZone).spawnPoint.x*50;
				player.ypos=GamePanel.zones.get(GamePanel.currentZone).spawnPoint.y*50;
			}
			lastAttackTime = System.currentTimeMillis();
		}
	}
	public void update(){
		for(int i = 0; i<buffs.size();i++){
			buffs.get(i).update();
			//System.out.println("updated buff");
		}
		shootCounter--;
		chargeCounter--;
		if(queen!=null){
			if(this.name=="Spider Egg"&&queen.canSeePlayer&&queen.currentHealth>=0){

				if(this.queen!=null){

					if(queen.lastHatchTime+hatchRate<System.currentTimeMillis()){
						hatchRate = GamePanel.randomNumber(2000, 12000);

						//remove this
						currentHealth = 0;
						//add a spider hatchling at this position
						int roll = GamePanel.randomNumber(1, 5);
						Monster temp;
						if(roll<=3){
							temp = new Monster((int)xpos,(int)ypos,"Spider Hatchling");
							//GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.add(new Monster((int)xpos,(int)ypos,"Spider Hatchling"));
						}
						else if (roll==4){
							temp = new Monster((int)xpos,(int)ypos,"Venom Spider");
							//GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.add(new Monster((int)xpos,(int)ypos,"Venom Spider"));
						}
						else{
							temp = new Monster((int)xpos,(int)ypos,"Web Spider");
							//GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.add(new Monster((int)xpos,(int)ypos,"Web Spider"));
						}
						temp.setSize(20, 20);
						GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.add(temp);
						queen.lastHatchTime=System.currentTimeMillis();
					}
				}
			}
		}
		if(cantMove==false){
			projectileAngle = Math.toDegrees(Math.atan2((GamePanel.player.ypos-ypos),((GamePanel.player.xpos-xpos))));
			if(weapon!=null){
				weapon.update();
			}
			if(spawnTime+10000<System.currentTimeMillis()&&runsAway){
				GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.remove(this);
			}
			double moveSpeed = speed;
			//find distance between this and player
			double distanceToPlayer = Math.sqrt(Math.pow(((GamePanel.player.xpos)-this.xpos),2)+Math.pow(((GamePanel.player.ypos)-this.ypos),2));
			double distanceToAnchor = Math.sqrt(Math.pow(((anchor.x)-this.xpos),2)+Math.pow(((anchor.y)-this.ypos),2));
			//if the player has been detected
			if(distanceToPlayer<=300){
				canSeePlayer=true;
			}
			if(canSeePlayer==false){//if the player has not been detected
				//wander to a random position nearby
				if(randomDest.x!=(int)this.xpos||randomDest.y!=(int)this.ypos){//not already at the destination
					//desired angle is equal to the angle between monster and destination
					angleInDegrees=Math.toDegrees(Math.atan2((randomDest.y-this.ypos),(randomDest.x-this.xpos)));

					angleInRadians = Math.toRadians(angleInDegrees);
					double distanceToPos= Math.sqrt(Math.pow((randomDest.x-this.xpos),2)+Math.pow((randomDest.y-this.ypos),2));
					if(distanceToPos<=speed){
						randomDest.x=(int) xpos;
						randomDest.y=(int) ypos;
					}
					else{
						Rectangle playerBox = new Rectangle((int)(xpos-15),(int)(ypos-15),50,50);
						if(GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos+(Math.cos(angleInRadians)*moveSpeed))/50][(int)(ypos/50.0)].collisionBox!=null){
							if(!GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos+(Math.cos(angleInRadians)*moveSpeed))/50][(int)(ypos/50.0)].collisionBox.intersects(playerBox)){
								xpos += (Math.cos(angleInRadians)*moveSpeed);
							}
							else{
								int randomAngle = GamePanel.randomNumber(1, 360);
								int randomDistance = GamePanel.randomNumber(1, 300);
								angleInRadians = Math.toRadians(randomAngle);
								randomDest = new Point(anchor.x+(int)(Math.cos(randomAngle)*(double)randomDistance),anchor.y+(int)(Math.sin(randomAngle)*(double)randomDistance));//generate a new random point to wander to
							}
						}
						else{
							xpos += (Math.cos(angleInRadians)*moveSpeed);
						}
						if(GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos/50)][(int)(ypos + (Math.sin(angleInRadians)*moveSpeed))/50].collisionBox!=null){
							if(!GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos/50)][(int)(ypos + (Math.sin(angleInRadians)*moveSpeed))/50].collisionBox.intersects(playerBox)){
								ypos += (Math.sin(angleInRadians)*moveSpeed);
							}
							else{
								int randomAngle = GamePanel.randomNumber(1, 360);
								int randomDistance = GamePanel.randomNumber(1, 300);
								angleInRadians = Math.toRadians(randomAngle);
								randomDest = new Point(anchor.x+(int)(Math.cos(randomAngle)*(double)randomDistance),anchor.y+(int)(Math.sin(randomAngle)*(double)randomDistance));//generate a new random point to wander to
							}
						}
						else{
							ypos += (Math.sin(angleInRadians)*moveSpeed);
						}
					}
				}
				else{//at the destination already
					int randomAngle = GamePanel.randomNumber(1, 360);
					int randomDistance = GamePanel.randomNumber(1, 300);
					angleInRadians = Math.toRadians(randomAngle);
					randomDest = new Point(anchor.x+(int)(Math.cos(randomAngle)*(double)randomDistance),anchor.y+(int)(Math.sin(randomAngle)*(double)randomDistance));//generate a new random point to wander to
				}
			}
			else{

				//attack player
				if((distanceToPlayer>28||runsAway)){//not already at the destination
					double tempSpeed = moveSpeed*2;
					//if it is time to charge, charge
					if(chargeCounter<=0&&weapon!=null&&charging==false){
						chargeCounter=(int)((double)(60.0/(.1*(double)GamePanel.randomNumber(1, 6))));
						charging = true;
						anim.delay=40;
						anim.timeForNextFrame = anim.LastFrameTime + anim.delay;
						angleInDegrees=Math.toDegrees(Math.atan2((GamePanel.player.ypos-this.ypos),(GamePanel.player.xpos-this.xpos)));
					}
					//if this monster has reached it's maximum charge distance it should stop charging
					if(distanceCharged>=chargeRange){
						stopCharging();
					}
					//if the monster is not charging it can change direction
					if(!charging){
						//desired angle is equal to the angle between monster and player
						if(runsAway)
							angleInDegrees=Math.toDegrees(Math.atan2((GamePanel.player.ypos-this.ypos),(GamePanel.player.xpos-this.xpos)))+180;
						else
							angleInDegrees=Math.toDegrees(Math.atan2((GamePanel.player.ypos-this.ypos),(GamePanel.player.xpos-this.xpos)));
					}
					else{//if the monster is charging make it move fast
						tempSpeed = 9;
					}
					angleInRadians = Math.toRadians(angleInDegrees);
					Rectangle monsterBox = new Rectangle((int)(xpos-(width/2)),(int)(ypos-(height/2)),width,height);
					if(GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos+(Math.cos(angleInRadians)*tempSpeed))/50][(int)(ypos/50.0)].collisionBox!=null){
						if(!GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos+(Math.cos(angleInRadians)*tempSpeed))/50][(int)(ypos/50.0)].collisionBox.intersects(monsterBox)){
							xpos += (Math.cos(angleInRadians)*tempSpeed);
							if(charging){
								//distanceCharged+=(Math.cos(angleInRadians)*tempSpeed);
							}
						}
						else{
							stopCharging();
						}

					}
					else{
						xpos += (Math.cos(angleInRadians)*tempSpeed);
					}
					if(GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos/50)][(int)(ypos + (Math.sin(angleInRadians)*tempSpeed))/50].collisionBox!=null){
						if(!GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos/50)][(int)(ypos + (Math.sin(angleInRadians)*tempSpeed))/50].collisionBox.intersects(monsterBox)){
							ypos += (Math.sin(angleInRadians)*tempSpeed);
							if(charging){
								//distanceCharged+=(Math.sin(angleInRadians)*tempSpeed);
							}
						}
						else{
							stopCharging();
						}
					}
					else{
						ypos += (Math.sin(angleInRadians)*tempSpeed);
					}
					distanceCharged+=tempSpeed;
					if(distanceToPlayer<=400&&distanceToPlayer>40){
						//double projectileAngle = Math.toDegrees(Math.atan2((GamePanel.player.ypos-ypos),((GamePanel.player.xpos-xpos))));
						//GamePanel.projectiles.add(new Projectile((int)xpos, (int)ypos, projectileAngle, 4,null));
						if(weapon!=null){
							if(weapon.itemType=="ProjectileWeapon"){
								weapon.swingWeapon();
							}
						}
					}
				}
				else{
					if(!runsAway){
						attackPlayer(GamePanel.player);
						stopCharging();
					}
				}
			}
		}
	}
	public void stopCharging(){
		charging = false;
		distanceCharged = 0;
		anim.delay=800;
	}
	public void Draw(Graphics2D g){
		for(int i = 0; i<buffs.size();i++){
			if(buffs.get(i).name=="On Fire"){
				int rand = width/30;
				if(rand<=0){
					rand=1;
				}
				for(int j = 0; j<rand;j++){
					Animation temp = new Animation(GamePanel.fireAnim, 20, 200, 0, (int)((xpos+GamePanel.randomNumber((-width/4), (width/4)))), (int)((ypos+GamePanel.randomNumber(-(height/2), 0))), false, -5, 0);
					temp.xScale=GamePanel.randomNumber(1, 3);
					temp.yScale=GamePanel.randomNumber(1, 3);
					GamePanel.effects.add(temp);
				}
			}
		}
		//update();
		double tempRegen = getRegen();
		if(currentHealth+tempRegen<maxHealth){
			currentHealth+=tempRegen;
		}
		else{
			currentHealth=maxHealth;
		}
		if(currentHealth<=0){
			dropItem(increasedDropRarity);
		}

		if(anim!=null){
			anim.angle=angleInRadians;
			if(GamePanel.player!=null){
				anim.xpos=AppletUI.xoffset+(int)xpos-(width/2)-((int)GamePanel.player.xpos);
				anim.ypos=AppletUI.yoffset+(int)ypos-(height/2)-((int)GamePanel.player.ypos);
			}
			anim.Draw(g);
		}
		//draw health bars
		if(this.currentHealth<this.maxHealth){
			g.drawImage(GamePanel.healthBarBackground,AppletUI.xoffset-15+(int)((this.xpos-3))-((int)GamePanel.player.xpos),AppletUI.yoffset-15+(int)((this.ypos+30))-((int)GamePanel.player.ypos),(int)(20),(int)(4),null);
			for(int i = 0; i<((double)currentHealth/(double)maxHealth)*20;i++){
				if(((double)currentHealth/(double)maxHealth)*10>6){
					//green
					g.drawImage(GamePanel.bars[0][0],AppletUI.xoffset-15+(int)((this.xpos-3+i))-((int)GamePanel.player.xpos),AppletUI.yoffset-15+(int)((this.ypos+30))-((int)GamePanel.player.ypos),(int)(1),(int)(4),null);
				}
				else if(((double)currentHealth/(double)maxHealth)*10<=6&&((double)currentHealth/(double)maxHealth)*10>3){
					//yellow
					g.drawImage(GamePanel.bars[1][0],AppletUI.xoffset-15+(int)((this.xpos-3+i))-((int)GamePanel.player.xpos),AppletUI.yoffset-15+(int)((this.ypos+30))-((int)GamePanel.player.ypos),(int)(1),(int)(4),null);
				}
				else if(((double)currentHealth/(double)maxHealth)*10<=3){
					//red
					g.drawImage(GamePanel.bars[2][0],AppletUI.xoffset-15+(int)((this.xpos-3+i))-((int)GamePanel.player.xpos),AppletUI.yoffset-15+(int)((this.ypos+30))-((int)GamePanel.player.ypos),(int)(1),(int)(4),null);
				}
			}
		}
		if(bodyLights!=null&&GamePanel.zones.get(GamePanel.currentZone).hasDarkness){
			bodyLights.xpos=xpos-anim.spriteSheet[0][0].getWidth()/2;
			bodyLights.ypos=ypos-anim.spriteSheet[0][0].getHeight()/2;
			bodyLights.setAngle(Math.toDegrees(angleInRadians));
			bodyLights.render();
		}
		if(light!=null&&GamePanel.zones.get(GamePanel.currentZone).hasDarkness){
			light.xpos=(int)xpos;
			light.ypos=(int)ypos;
			light.render();
		}

	}
}
