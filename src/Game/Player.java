package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javafx.scene.shape.Circle;

public class Player {
	double xpos;
	double ypos;
	int turnSpeed = 5;
	double angleInDegrees = 0;
	double angleInRadians = 0;
	double lifeRegen = 60;
	double speed = 2.0;
	Point desiredPosition;
	double desiredAngleInDegrees = 0;
	double weaponAngle=0;
	//Item[] weapon = new Item[2];
	double currentHealth = 100;
	double maxHealth = 100;
	boolean recentHit = false;
	int ticks = 0;
	int ticksSinceDamageTaken = 0;
	boolean isCharging = false;
	int chargeDistance=0;
	int distanceCharged = 0;
	boolean canMove = true;
	ArrayList<StatusEffect> buffs = new ArrayList<StatusEffect>();
	Inventory inventory;
	Light light = new Light(0,0,1000,1000,5,1);
	public Player(double x, double y){
		GamePanel.lights.add(light);
		xpos = x;
		ypos = y;
		inventory = new Inventory();
		desiredPosition = new Point((int)xpos,(int)ypos);
		Item sword = new Item(-1,-1,"Sword",0);
		Item dagger = new Item(-1,-1,"Dagger",999);
		Item bow = new Item(-1,-1,"Simple Bow",0);
		Item kcbow = new Item(-1,-1,"KingCurrency's Bow",1.0);
		Item gnuSword = new Item(-1,-1,"Mario's Sword",1.0);
		Item hammer = new Item(-1,-1,"Shaping Hammer",0);
		Item chisel = new Item(-1,-1,"Engraving Chisel",0);
		Item stone = new Item(-1,-1,"Superior Whetstone",0);
		Item kcRing = new Item(-1,-1,"KingCurrency's Ring",1.0);
		Item spear = new Item(-1,-1,"Lance",0);
		chisel.stackSize=9900;
		stone.stackSize=500;
		hammer.stackSize=100;
		kcbow.addModifier("Life on hit kc",true,0);
		inventory.items[0][0].item = sword;
		inventory.items[1][0].item = bow;
		inventory.items[5][0].item = new Item(-1,-1,"Torch",0);
		inventory.items[5][0].item.stackSize = 20;
		//inventory.items[2][0].item=chisel;
		//inventory.items[3][0].item=stone;
		for(int i = 0; i<5;i++){
			//inventory.rings[i].item=new Item(-1,-1,"KingCurrency's Ring",0);
		}
		inventory.items[9][0].item = spear;
		inventory.items[5][1].item=new Item(-1,-1,"Wooden Trunk",0);
		//		inventory.head.item=new Item(-1,-1,"KingCurrency's Hood",999);
		//		inventory.chest.item=new Item(-1,-1,"KingCurrency's Shirt",999);
		//		inventory.pants.item=new Item(-1,-1,"KingCurrency's Pants",999);
		//		inventory.leftGlove.item=new Item(-1,-1,"KingCurrency's Glove",999);
		//		inventory.rightGlove.item=new Item(-1,-1,"KingCurrency's Glove",999);
		//		inventory.leftBoot.item=new Item(-1,-1,"KingCurrency's Shoe",0);
		//		inventory.rightBoot.item=new Item(-1,-1,"KingCurrency's Shoe",0);
		//inventory.items[2][0].item = chisel;
		//inventory.items[3][0].item = hammer;
		for(int j = 1; j<5;j++){
			for(int i = 0; i<10; i++){
				//inventory.items[i][j].item = new Item(-1,-1,"Emerald Ring",0);
			}
		}
		inventory.items[2][0].item = kcbow;
		inventory.items[8][4].item = gnuSword;
		//inventory.items[2][0].item = dagger;
		//inventory.items[2][1].item = chisel;
		//inventory.items[2][2].item = new Item(-1,-1, "Chain Helmet");

	}
	public boolean hasBuff(StatusEffect buff){
		for(int i = 0; i<buffs.size();i++){
			if(buffs.get(i).name.equals(buff.name)&&buffs.get(i).startTime==buff.startTime){
				return true;
			}
		}
		return false;
	}
	public void takeDamage(double damage){
		if(ticksSinceDamageTaken<=0){
			ticksSinceDamageTaken = 2;
			double dmgTaken = damage;
			if(GamePanel.randomNumber(1, 100)<=getArmorCoverage()){
				dmgTaken = (damage*getDamageReductionMultiplier());
			}
			currentHealth-=dmgTaken;
			if(damage>0){
				GamePanel.combatText.add(new CombatText((int)xpos,(int)ypos,(int)dmgTaken+"",Color.red));
			}

		}
	}
	public double getIncreasedRarity(){
		double total = 0;
		for(int i = 0; i<inventory.rings.length;i++){
			if(inventory.rings[i].item!=null){
				for(int j = 0; j<inventory.rings[i].item.modifiers.size();j++){
					total+=inventory.rings[i].item.modifiers.get(j).itemRarity;
				}
				if(inventory.rings[i].item.implicit!=null){
					total+=inventory.rings[i].item.implicit.itemRarity;
				}
			}
		}
		return total;
	}
	public double getMovementSpeed(){
		double total = 1.0;
		Item item = null;
		for(int i = 0; i<12; i++){
			if(i==0){
				item = inventory.head.item;
			}
			else if(i==1){
				item = inventory.chest.item;
			}
			else if(i==2){
				item = inventory.pants.item;
			}
			else if(i==3){
				item = inventory.leftGlove.item;
			}
			else if(i==4){
				item = inventory.rightGlove.item;
			}
			else if(i==5){
				item = inventory.leftBoot.item;
			}
			else if(i==6){
				item = inventory.rightBoot.item;
			}
			else{
				item = inventory.rings[i-7].item;
			}
			if(item!=null){
				for(int j = 0; j<item.modifiers.size();j++){
					if(item.modifiers.get(j)!=null)
						total+=item.modifiers.get(j).moveSpeed;
				}
				if(item.implicit!=null){
					total+=item.implicit.moveSpeed;
				}
			}
		}
		for(int i = 0; i<buffs.size();i++){
			total+=buffs.get(i).movementSpeed;
		}

		double result = speed*total;
		if(result<0){
			result = 0;
		}
		return result;
	}
	public double getPercentMoreArmorCoverage(){
		double total = 1.0;
		for(int i = 0; i<inventory.rings.length;i++){
			if(inventory.rings[i].item!=null){
				for(int j = 0; j<inventory.rings[i].item.modifiers.size();j++){
					if(inventory.rings[i].item.modifiers.get(j).effect!=null){
						total+=inventory.rings[i].item.modifiers.get(j).effect.moreArmorCoverage;
					}
				}
			}
		}
		return total;
	}
	public double getArmorCoverage(){
		double total = 0;
		if(inventory.head.item!=null)
			total+=inventory.head.item.armorCoverage;
		if(inventory.chest.item!=null)
			total+=inventory.chest.item.armorCoverage;
		if(inventory.pants.item!=null)
			total+=inventory.pants.item.armorCoverage;
		if(inventory.leftGlove.item!=null)
			total+=inventory.leftGlove.item.armorCoverage;
		if(inventory.rightGlove.item!=null)
			total+=inventory.rightGlove.item.armorCoverage;
		if(inventory.leftBoot.item!=null)
			total+=inventory.leftBoot.item.armorCoverage;
		if(inventory.rightBoot.item!=null)
			total+=inventory.rightBoot.item.armorCoverage;
		total*=100;
		total = total*getPercentMoreArmorCoverage();
		return total;
	}
	public double getMaxHealth(){
		double total = 0;
		Item item = null;
		for(int i = 0; i<12; i++){
			if(i==0){
				item = inventory.head.item;
			}
			else if(i==1){
				item = inventory.chest.item;
			}
			else if(i==2){
				item = inventory.pants.item;
			}
			else if(i==3){
				item = inventory.leftGlove.item;
			}
			else if(i==4){
				item = inventory.rightGlove.item;
			}
			else if(i==5){
				item = inventory.leftBoot.item;
			}
			else if(i==6){
				item = inventory.rightBoot.item;
			}
			else{
				item = inventory.rings[i-7].item;
			}
			if(item!=null){
				for(int j = 0; j<item.modifiers.size();j++){
					if(item.modifiers.get(j)!=null)
						total+=item.modifiers.get(j).health;
				}
			}
		}
		return maxHealth+total;
	}
	public double getCritDamage(){
		double totalCritChance = 0;
		double totalCritDamage = 1.0;
		double totalDamageIncrease = 0;
		Item item = null;
		for(int i = 0; i<12; i++){
			if(i==0){
				item = inventory.head.item;
			}
			else if(i==1){
				item = inventory.chest.item;
			}
			else if(i==2){
				item = inventory.pants.item;
			}
			else if(i==3){
				item = inventory.leftGlove.item;
			}
			else if(i==4){
				item = inventory.rightGlove.item;
			}
			else if(i==5){
				item = inventory.leftBoot.item;
			}
			else if(i==6){
				item = inventory.rightBoot.item;
			}
			else{
				item = inventory.rings[i-7].item;
			}
			if(item!=null){
				for(int j = 0; j<item.modifiers.size();j++){
					if(item.modifiers.get(j)!=null){
						//totalCritChance+=item.modifiers.get(j).critChance;
						totalCritDamage+=item.modifiers.get(j).critDamage;
						//totalDamageIncrease+=item.modifiers.get(j).globalDamage;
					}
				}
			}
		}
		return totalCritDamage;
	}
	public double getCritChance(){
		double totalCritChance = 0;
		double totalCritDamage = 0;
		double totalDamageIncrease = 0;
		Item item = null;
		for(int i = 0; i<12; i++){
			if(i==0){
				item = inventory.head.item;
			}
			else if(i==1){
				item = inventory.chest.item;
			}
			else if(i==2){
				item = inventory.pants.item;
			}
			else if(i==3){
				item = inventory.leftGlove.item;
			}
			else if(i==4){
				item = inventory.rightGlove.item;
			}
			else if(i==5){
				item = inventory.leftBoot.item;
			}
			else if(i==6){
				item = inventory.rightBoot.item;
			}
			else{
				item = inventory.rings[i-7].item;
			}
			if(item!=null){
				for(int j = 0; j<item.modifiers.size();j++){
					if(item.modifiers.get(j)!=null){
						totalCritChance+=item.modifiers.get(j).critChance;
						//totalCritDamage+=item.modifiers.get(j).critDamage;
						//totalDamageIncrease+=item.modifiers.get(j).globalDamage;
					}
				}
			}
		}
		return totalCritChance;
	}
	public double getGlobalDamage(){
		double totalCritChance = 0;
		double totalCritDamage = 0;
		double totalDamageIncrease = 1.0;
		Item item = null;
		for(int i = 0; i<12; i++){
			if(i==0){
				item = inventory.head.item;
			}
			else if(i==1){
				item = inventory.chest.item;
			}
			else if(i==2){
				item = inventory.pants.item;
			}
			else if(i==3){
				item = inventory.leftGlove.item;
			}
			else if(i==4){
				item = inventory.rightGlove.item;
			}
			else if(i==5){
				item = inventory.leftBoot.item;
			}
			else if(i==6){
				item = inventory.rightBoot.item;
			}
			else{
				item = inventory.rings[i-7].item;
			}
			if(item!=null){
				for(int j = 0; j<item.modifiers.size();j++){
					if(item.modifiers.get(j)!=null){
						//totalCritChance+=item.modifiers.get(j).critChance;
						//totalCritDamage+=item.modifiers.get(j).critDamage;
						totalDamageIncrease+=item.modifiers.get(j).globalDamage;
					}
				}
			}
		}
		return totalDamageIncrease;
	}
	public double getLifeRegeneration(){
		double total = lifeRegen;
		Item item = null;
		for(int i = 0; i<12; i++){
			if(i==0){
				item = inventory.head.item;
			}
			else if(i==1){
				item = inventory.chest.item;
			}
			else if(i==2){
				item = inventory.pants.item;
			}
			else if(i==3){
				item = inventory.leftGlove.item;
			}
			else if(i==4){
				item = inventory.rightGlove.item;
			}
			else if(i==5){
				item = inventory.leftBoot.item;
			}
			else if(i==6){
				item = inventory.rightBoot.item;
			}
			else{
				item = inventory.rings[i-7].item;
			}
			if(item!=null){
				for(int j = 0; j<item.modifiers.size();j++){
					if(item.modifiers.get(j)!=null)
						total+=item.modifiers.get(j).lifeRegen;
				}
			}
		}
		for(int i = 0; i<buffs.size();i++){
			total+=buffs.get(i).lifeRegen;
		}
		return total;
	}
	public double getDamageReductionMultiplier(){
		double sum = 1;
		if(inventory.head.item!=null)
			sum-=inventory.head.item.damageReduction;
		if(inventory.chest.item!=null)
			sum-=inventory.chest.item.damageReduction;
		if(inventory.pants.item!=null)
			sum-=inventory.pants.item.damageReduction;
		if(inventory.leftGlove.item!=null)
			sum-=inventory.leftGlove.item.damageReduction;
		if(inventory.rightGlove.item!=null)
			sum-=inventory.rightGlove.item.damageReduction;
		if(inventory.leftBoot.item!=null)
			sum-=inventory.leftBoot.item.damageReduction;
		if(inventory.rightBoot.item!=null)
			sum-=inventory.rightBoot.item.damageReduction;
		return sum;
	}
	public void moveForwards(){
		double moveSpeed = getMovementSpeed();
		if(distanceCharged>=chargeDistance&&isCharging){
			isCharging=false;
			distanceCharged = 0;
			desiredPosition.x=(int)xpos;
			desiredPosition.y=(int)ypos;
		}
		if(isCharging){
			moveSpeed*=2;
			distanceCharged +=moveSpeed;
		}
		//if player is not at desired position
		if((int)xpos!=desiredPosition.x||(int)ypos!=desiredPosition.y||isCharging){
			//desired angle is equal to the angle between player and destination
			desiredAngleInDegrees=Math.toDegrees(Math.atan2((desiredPosition.y-this.ypos),((desiredPosition.x-this.xpos))));
			if(isCharging==false){
				changeDirection();
			}
			angleInRadians = Math.toRadians(angleInDegrees);
			//Rectangle playerBox = new Rectangle((int)(xpos+(Math.cos(angleInRadians)*speed)),(int)(ypos+(Math.sin(angleInRadians)*speed)),50,50);
			if((int)((double)(xpos+(Math.cos(angleInRadians)*moveSpeed))/50.0)>0&&(int)((double)(xpos+(Math.cos(angleInRadians)*moveSpeed))/50.0)<GamePanel.zones.get(GamePanel.currentZone).levelWidth){
				if((int)((double)(ypos+(Math.sin(angleInRadians)*moveSpeed))/50.0)>0&&(int)((double)(ypos+(Math.sin(angleInRadians)*moveSpeed))/50.0)<GamePanel.zones.get(GamePanel.currentZone).levelHeight){
					Tile dest = GamePanel.zones.get(GamePanel.currentZone).map[(int)((double)(xpos+(Math.cos(angleInRadians)*moveSpeed))/50.0)][(int)((double)(ypos+(Math.sin(angleInRadians)*moveSpeed))/50.0)];
					//Rectangle destBox = new Rectangle(AppletUI.xoffset+GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos+(Math.cos(angleInRadians)*speed))][(int)(ypos+(Math.sin(angleInRadians)*speed))].xpos-(int)(GamePanel.player.xpos),AppletUI.yoffset+GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos+(Math.cos(angleInRadians)*speed))][(int)(ypos+(Math.sin(angleInRadians)*speed))].ypos-(int)(GamePanel.player.ypos),50,50);
					//if(new Rectangle((int)(xpos+(Math.cos(angleInRadians)*speed)),(int)(ypos+(Math.sin(angleInRadians)*speed)),50,50).intersects(new Rectangle(AppletUI.xoffset+GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos+(Math.cos(angleInRadians)*speed))][(int)(ypos+(Math.sin(angleInRadians)*speed))].xpos-(int)(GamePanel.player.xpos),AppletUI.yoffset+GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos+(Math.cos(angleInRadians)*speed))][(int)(ypos+(Math.sin(angleInRadians)*speed))].ypos-(int)(GamePanel.player.ypos),50,50))&&GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos+(Math.cos(angleInRadians)*speed))][(int)(ypos+(Math.sin(angleInRadians)*speed))].collision==1){
					//System.out.println(dest.xpos+","+dest.ypos);
					if(true){//dest.collision==1){
						Rectangle playerBox = new Rectangle((int)(xpos-25),(int)(ypos-25),50,50);
						if(GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos+(Math.cos(angleInRadians)*moveSpeed))/50][(int)(ypos/50.0)].collisionBox!=null){
							if(!GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos+(Math.cos(angleInRadians)*moveSpeed))/50][(int)(ypos/50.0)].collisionBox.intersects(playerBox)){
								xpos += (Math.cos(angleInRadians)*moveSpeed);
							}
							else{
								isCharging=false;
								distanceCharged = 0;
								desiredPosition.x=(int)xpos;
								desiredPosition.y=(int)ypos;
							}
						}
						else{
							xpos += (Math.cos(angleInRadians)*moveSpeed);
						}
						if(GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos/50.0)][(int)(ypos + (Math.sin(angleInRadians)*moveSpeed))/50].collisionBox!=null){
							if(!GamePanel.zones.get(GamePanel.currentZone).map[(int)(xpos/50.0)][(int)(ypos + (Math.sin(angleInRadians)*moveSpeed))/50].collisionBox.intersects(playerBox)){
								ypos += (Math.sin(angleInRadians)*moveSpeed);
							}
							else{
								isCharging=false;
								distanceCharged = 0;
								desiredPosition.x=(int)xpos;
								desiredPosition.y=(int)ypos;
							}
						}
						else{
							ypos += (Math.sin(angleInRadians)*moveSpeed);
						}

					}
				}
			}
			//}
		}
		double distanceToDestination = Math.sqrt(Math.pow((desiredPosition.x-this.xpos),2)+Math.pow((desiredPosition.y-this.ypos),2));
		if(distanceToDestination<=moveSpeed&&!isCharging){
			isCharging=false;
			distanceCharged = 0;
			desiredPosition.x=(int) xpos;
			desiredPosition.y=(int) ypos;
		}
		light.xpos=(int)xpos;
		light.ypos=(int)ypos;


	}

	public void changeDirection(){//-1 means turn left 1 degree, 1 means turn right 1 degree	
		angleInDegrees=desiredAngleInDegrees;
	}
	public void Update(){

		for(int i = 0; i<buffs.size();i++){
			buffs.get(i).update();
		}
		if(canMove)
			moveForwards();

		if(ticksSinceDamageTaken>0)
			ticksSinceDamageTaken--;

	}
	public void die(){
		currentHealth = (int)(getMaxHealth()/2.0);
		desiredPosition.x=(int) xpos;
		desiredPosition.y=(int) ypos;
	}
	public void Draw(Graphics g){
		if(currentHealth<getMaxHealth()){
			currentHealth+=getLifeRegeneration()/3600.0;
		}

		int direction = 0;
		while(angleInDegrees<0){
			angleInDegrees+=360;
		}
		if(angleInDegrees>315&&angleInDegrees<360||angleInDegrees>=0&&angleInDegrees<=45){
			direction = 2;
		}
		if(angleInDegrees>45&&angleInDegrees<=135){
			direction = 0;
		}
		if(angleInDegrees>135&&angleInDegrees<=225){
			direction = 1;
		}
		if(angleInDegrees>225&&angleInDegrees<=315){
			direction = 3;
		}
		g.drawImage(GamePanel.PlayerFaces[direction][0], AppletUI.xoffset-25, AppletUI.yoffset-25,50, 50, null);
		recentHit = false;
		for(int j = 0; j<inventory.items.length;j++){
			if(inventory.items[j][0]!=null){
				if(inventory.items[j][0].item!=null){
					for(int i = 0; i<(((double)inventory.items[j][0].item.swingWidth/60.0)*inventory.items[j][0].item.getAttackSpeed());i++){//= (swingwidth/60)*attacksPerSecond
						inventory.items[j][0].item.Draw(g);
						inventory.items[j][0].item.update();
					}
				}
			}
		}

		//weapon.update();
		ticks++;
		if(ticks>60){
			ticks=0;
		}
		if(GamePanel.zones.get(GamePanel.currentZone).hasDarkness){
			//light.render();
		}
	}
	public void DrawUI(Graphics g){
		//draw player health hearts
		for(int i = 0; i<((int)currentHealth/20);i++){
			g.drawImage(GamePanel.heart, 50+(55*i), 50 ,50, 50, null);
		}
		for(int i = 0; i<buffs.size();i++){
			buffs.get(i).Draw(g);
		}

		//draw partially damaged heart if there is one
		if(currentHealth%20>0){
			g.drawImage(GamePanel.heart, (int)(50+(((int)currentHealth/20)*55)), 50 ,(int)(50*(((double)currentHealth%20.0)/20.0)), (int)(50*(((double)currentHealth%20.0)/20.0)), null);
		}
	}
}
