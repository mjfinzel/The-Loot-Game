package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Item {
	int maxStackSize = 1;
	int stackSize = 1;
	String itemType;
	double size = 1.0;
	//int swingSpeed = 4;
	double attackSpeed = 2.0;//120
	int shootCounter = 0;
	int swingWidth = 90;//degree angle the weapon will swing over
	int drawAngle=-(swingWidth/2);
	int range;
	int xpos;
	int ypos;
	int damageModifier = 0;
	double critChance = 0;
	double critDamage = 1.5;
	double swingAngle;
	int currentDrawAngle = drawAngle;
	boolean swinging = false;
	int knockback = 0;
	int chargeDistance;
	BufferedImage artwork;
	//	final int SwingWeapon = 1;
	//	final int ProjectileWeapon = 2;
	//	final int Currency = 3;
	//	final int Helmet = 4;
	int baseDamage;
	int health=0;
	ItemModifier implicit;
	//ItemModifier[] modifiers = new ItemModifier[5];
	int modifierLimit = 5;
	ArrayList<ItemModifier> modifiers = new ArrayList<ItemModifier>();
	double damageReduction;
	double armorCoverage;
	double itemRarity = 0;
	Tooltip tooltip;
	String name;
	double projectileSpeed = 10;
	int pierce = 0;
	int projectiles = 1;
	boolean isGodmodeRestricted = false;
	ArrayList<StatusEffect>effects = new ArrayList<StatusEffect>();
	Monster user;
	ArrayList<Monster> monstersHit = new ArrayList<Monster>();
	//int modifierCount = 0;
	public Item(int x, int y, String Name, double increasedRarity){
		xpos = x;
		ypos = y;
		name = Name;
		if(name == "Sword"){
			itemType = "SwingWeapon";
			swingWidth = 140;
			drawAngle=-(swingWidth/2);
			attackSpeed = 2.0;
			baseDamage = 4;
			critChance = 5;
			knockback = 10;
			artwork = Images.load("/Textures/Sword.png");
		}
		if(name == "Lance"){
			itemType = "Spear";
			swingWidth = 140;
			chargeDistance = 200;
			drawAngle=-(swingWidth/2);
			attackSpeed = .5;
			baseDamage = 3;
			critChance = 3.5;
			knockback = 3;
			StatusEffect temp = new StatusEffect("On Fire",5,null,this);
			effects.add(temp);
			artwork = Images.load("/Textures/Spear.png");
		}
		if(name == "Poison Fang"){
			itemType = "MonsterSwingWeapon";
			swingWidth = 40;
			drawAngle=-(swingWidth/2);
			attackSpeed = 1.0;
			baseDamage = 3;
			critChance = 0;
			knockback = 0;
			effects.add(new StatusEffect("Poisoned",60,null,this));
			artwork = Images.load("/Textures/Sword.png");
		}
		if(name == "Spider Queen's Charge"){
			itemType = "MonsterSwingWeapon";
			swingWidth = 40;
			drawAngle=-(swingWidth/2);
			attackSpeed = .5;
			baseDamage = 38;
			critChance = 5;
			knockback = 0;
			effects.add(new StatusEffect("Webbed",3,null,this));
			artwork = Images.load("/Textures/Sword.png");
		}
		if(name == "Shockwave Staff"){
			itemType = "MagicWeapon";
			drawAngle=-(swingWidth/2);
			baseDamage = 0;
			attackSpeed = 3;
			knockback = 15;
			artwork = GamePanel.magicWeapons[0][0];
		}
		if(name == "Teleport Staff"){
			itemType = "MagicWeapon";
			drawAngle=-(swingWidth/2);
			baseDamage = 0;
			attackSpeed = 1.0/60.0;
			knockback = 0;
			artwork = GamePanel.magicWeapons[1][0];
		}
		if(name == "Dagger"){
			itemType = "SwingWeapon";
			swingWidth = 80;
			drawAngle=-(swingWidth/2);
			attackSpeed = 2.5;
			baseDamage = 6;
			critChance = 6;
			knockback = 0;
			artwork = Images.load("/Textures/Dagger.png");
		}
		if(name == "Mario's Sword"){
			itemType = "SwingWeapon";
			swingWidth = 280;
			drawAngle=-(swingWidth/2);
			attackSpeed = .8;
			range = 300;
			baseDamage = 15;
			critChance = 5;
			knockback = 70;
			isGodmodeRestricted = true;
			artwork = Images.load("/Textures/Giraffe.png");
		}
		if(name == "Iron Ring"){
			itemType = "Armor";
			artwork = GamePanel.rings[0][0];
			modifierLimit = 2;
		}
		if(name == "Silver Ring"){
			itemType = "Armor";
			artwork = GamePanel.rings[1][0];
			modifierLimit = 2;
		}
		if(name == "Gold Ring"){
			itemType = "Armor";
			artwork = GamePanel.rings[2][0];
			modifierLimit = 2;
		}
		if(name == "Emerald Ring"){
			itemType = "Armor";
			artwork = GamePanel.rings[3][0];
			modifierLimit = 2;
		}
		if(name == "KingCurrency's Ring"){
			itemType = "Armor";
			artwork = GamePanel.rings[4][0];
			modifierLimit = 2;
			isGodmodeRestricted=true;
			implicit = new ItemModifier("KC_Rarity",6);
		}
		if(name == "Chain Helmet"){
			itemType = "Armor";
			damageReduction = .05;
			armorCoverage = .1;
			artwork = GamePanel.armorTextures[1][0];
		}
		if(name == "Chain Vest"){
			itemType = "Armor";
			damageReduction = .1;
			armorCoverage = .1;
			artwork = GamePanel.armorTextures[1][1];
		}
		if(name == "Chain Pants"){
			itemType = "Armor";
			damageReduction = .05;
			armorCoverage = .1;
			artwork = GamePanel.armorTextures[1][2];
		}
		if(name == "Chain Glove"){
			itemType = "Armor";
			damageReduction = .05;
			armorCoverage = .1;
			artwork = GamePanel.armorTextures[1][3];
		}
		if(name == "Chain Boot"){
			itemType = "Armor";
			damageReduction = .05;
			armorCoverage = .1;
			artwork = GamePanel.armorTextures[1][4];
		}
		//chain armor has 70% armor coverage, 40% damage reduction, 28% total effectiveness
		//plate armor has 50% armor coverage, 80% damage reduction, 40% total effectiveness
		if(name == "Plate Helmet"){
			itemType = "Armor";
			damageReduction = .10;
			armorCoverage = .06;
			artwork = GamePanel.armorTextures[0][0];
			addModifier("Movement Speed Plate",true,0);
		}
		if(name == "Plate Vest"){
			itemType = "Armor";
			damageReduction = .20;
			armorCoverage = .10;
			artwork = GamePanel.armorTextures[0][1];
			addModifier("Movement Speed Plate",true,0);
		}
		if(name == "Plate Pants"){
			itemType = "Armor";
			damageReduction = .10;
			armorCoverage = .10;
			artwork = GamePanel.armorTextures[0][2];
			addModifier("Movement Speed Plate",true,0);
		}
		if(name == "Plate Glove"){
			itemType = "Armor";
			damageReduction = .10;
			armorCoverage = .06;
			artwork = GamePanel.armorTextures[0][3];
			addModifier("Movement Speed Plate",true,0);
		}
		if(name == "Plate Boot"){
			itemType = "Armor";
			damageReduction = .10;
			armorCoverage = .06;
			artwork = GamePanel.armorTextures[0][4];
			addModifier("Movement Speed Plate",true,0);
		}
		//cloth
		if(name == "Cloth Hood"){
			itemType = "Armor";
			damageReduction = .03;
			armorCoverage = .1;
			implicit = new ItemModifier("Movement Speed Cloth",6);
			artwork = GamePanel.armorTextures[2][0];
		}
		if(name == "Cloth Shirt"){
			itemType = "Armor";
			damageReduction = .05;
			armorCoverage = .3;
			implicit = new ItemModifier("Movement Speed Cloth",6);
			artwork = GamePanel.armorTextures[2][1];

		}
		if(name == "Cloth Pants"){
			itemType = "Armor";
			damageReduction = .04;
			armorCoverage = .2;
			implicit = new ItemModifier("Movement Speed Cloth",6);
			artwork = GamePanel.armorTextures[2][2];

		}
		if(name == "Cloth Glove"){
			itemType = "Armor";
			damageReduction = .02;
			armorCoverage = .1;
			implicit = new ItemModifier("Movement Speed Cloth",6);
			artwork = GamePanel.armorTextures[2][3];

		}
		if(name == "Cloth Shoe"){
			itemType = "Armor";
			damageReduction = .02;
			armorCoverage = .1;
			implicit = new ItemModifier("Movement Speed Cloth",6);
			artwork = GamePanel.armorTextures[2][4];

		}
		//end of cloth
		//kingCurrency's robes
		if(name == "KingCurrency's Hood"){
			itemType = "Armor";
			damageReduction = .1;
			armorCoverage = .1;
			implicit = new ItemModifier("Movement Speed KC",6);
			artwork = GamePanel.armorTextures[4][0];
			isGodmodeRestricted=true;
		}
		if(name == "KingCurrency's Shirt"){
			itemType = "Armor";
			damageReduction = .25;
			armorCoverage = .3;
			implicit = new ItemModifier("Movement Speed KC",6);
			artwork = GamePanel.armorTextures[4][1];
			isGodmodeRestricted=true;

		}
		if(name == "KingCurrency's Pants"){
			itemType = "Armor";
			damageReduction = .2;
			armorCoverage = .2;
			implicit = new ItemModifier("Movement Speed KC",6);
			artwork = GamePanel.armorTextures[4][2];
			isGodmodeRestricted=true;
		}
		if(name == "KingCurrency's Glove"){
			itemType = "Armor";
			damageReduction = .1;
			armorCoverage = .1;
			implicit = new ItemModifier("Movement Speed KC",6);
			artwork = GamePanel.armorTextures[4][3];
			isGodmodeRestricted=true;
		}
		if(name == "KingCurrency's Shoe"){
			itemType = "Armor";
			damageReduction = .1;
			armorCoverage = .1;
			implicit = new ItemModifier("Movement Speed KC",6);
			artwork = GamePanel.armorTextures[4][4];
			isGodmodeRestricted=true;
		}
		//end of kingCurrency's robes
		if(name == "Simple Bow"){
			itemType = "ProjectileWeapon";
			range = 500;
			baseDamage = 5;
			attackSpeed = 1;//1
			projectileSpeed = 10.0;
			critChance = 5;
			pierce = 0;
			knockback = 10;
			artwork = Images.load("/Textures/SimpleBow.png");
			
			swinging = true;
		}
		if(name == "Spider Web Shooter"){
			itemType = "ProjectileWeapon";
			range = 400;
			baseDamage = 0;
			attackSpeed = .5;
			projectileSpeed = 10.0;
			critChance = 5;
			pierce = 0;
			knockback = 0;
			artwork = Images.load("/Textures/SimpleBow.png");
			swinging = true;
		}
		if(name == "Splinter Bow"){
			itemType = "ProjectileWeapon";
			range = 500;
			baseDamage = 5;
			attackSpeed = 1;
			projectileSpeed = 10.0;
			critChance = 5;
			projectiles = 3;
			pierce = 0;
			knockback = 8;
			artwork = Images.load("/Textures/SplinterBow.png");
			swinging = true;
		}
		if(name == "KingCurrency's Bow"){
			itemType = "ProjectileWeapon";
			range = 1000;
			baseDamage = 20;
			attackSpeed = 1.5;
			projectileSpeed = 15.0;
			critChance = 5;
			pierce = 3;
			projectiles = 5;
			knockback = 50;
			isGodmodeRestricted = true;
			artwork = Images.load("/Textures/KCBow.png");
			swinging = true;
		}
		if(name == "Whetstone"){
			itemType = "Currency";
			maxStackSize = 20;
			artwork = GamePanel.currency[0][0];
		}
		if(name == "Greater Whetstone"){
			itemType = "Currency";
			maxStackSize = 20;
			artwork = GamePanel.currency[1][0];
		}
		if(name == "Superior Whetstone"){
			itemType = "Currency";
			maxStackSize = 20;
			artwork = GamePanel.currency[2][0];
		}
		if(name == "Engraving Chisel"){
			itemType = "Currency";
			maxStackSize = 10;
			artwork = GamePanel.currency[0][1];
		}
		if(name == "Wooden Trunk"){
			itemType = "Furniture";
			maxStackSize = 1;
			artwork = GamePanel.chests[0][0];
		}
		if(name == "Torch"){
			itemType = "Furniture";
			maxStackSize = 20;
			artwork = GamePanel.chests[1][0];
		}
		if(name == "Shaping Hammer"){
			itemType = "Currency";
			maxStackSize = 10;
			artwork = GamePanel.currency[1][1];
		}
		tooltip = new Tooltip(this);
		int mods = GamePanel.randomNumber(0, 4);
		if(tooltip.itemType=="Ring"){
			mods = GamePanel.randomNumber(0, 2);
		}
		for(int i = 0; i<mods;i++){
			addRandomModifier(increasedRarity);
		}
	}
	public void removeRandomModifier(){
		int whatMod = GamePanel.randomNumber(0, modifiers.size()-1);
		if(modifiers.size()>0){
			modifiers.remove(whatMod);
		}
	}
	public boolean hasModifier(String modName){
		for(int i = 0;i<modifiers.size();i++){

			if(modifiers.get(i).name.equals(modName)){
				return true;
			}

		}
		return false;
	}
	public void addRandomModifier(double increasedRarity){
		boolean addedMod = false;
		if(tooltip.itemType=="Boot"||tooltip.itemType=="Pants"){
			ArrayList<String> triedMods = new ArrayList<String>();
			while(!addedMod&&triedMods.size()<2){
				int roll = GamePanel.randomNumber(1, 2);
				if(roll==1){
					if(!hasModifier("Movement Speed")){
						addModifier("Movement Speed",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Movement Speed");
					}
				}
				else if(roll == 2){
					if(!hasModifier("Health")){
						addModifier("Health",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Health");
					}
				}

			}
		}
		if(tooltip.itemType=="Glove"){
			ArrayList<String> triedMods = new ArrayList<String>();
			while(!addedMod&&triedMods.size()<2){
				int roll = GamePanel.randomNumber(1, 4);
				if(roll==1){
					if(!hasModifier("Health")){
						addModifier("Health",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Health");
					}
				}
				if(roll==2){
					if(!hasModifier("Attack Speed")){
						addModifier("Attack Speed",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Attack Speed");
					}
				}
				if(roll==3){
					if(!hasModifier("Crit chance")){
						addModifier("Crit chance",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Crit chance");
					}
				}
				if(roll==4){
					if(!hasModifier("Crit damage")){
						addModifier("Crit damage",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Crit damage");

					}
				}
			}
		}
		if(tooltip.itemType=="Ring"){
			ArrayList<String> triedMods = new ArrayList<String>();
			while(!addedMod&&triedMods.size()<7){
				int roll = GamePanel.randomNumber(1, 7);
				if(roll==1){
					if(!hasModifier("Global damage")){
						addModifier("Global damage",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Global damage");
					}
				}
				else if(roll==2){
					if(!hasModifier("Health")){
						addModifier("Health",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Health");
					}
				}
				else if(roll==3){
					if(!hasModifier("MovementSpeedOnDaggerKill")){
						addModifier("MovementSpeedOnDaggerKill",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("MovementSpeedOnDaggerKill");
					}
				}
				else if(roll==4){
					if(!hasModifier("ArmorCoverageOnSwordHit")){
						addModifier("ArmorCoverageOnSwordHit",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("ArmorCoverageOnSwordHit");
					}
				}
				else if(roll==5){
					if(!hasModifier("Crit chance")){
						addModifier("Crit chance",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Crit chance");
					}
				}
			}
		}
		if(itemType=="Spear"){
			ArrayList<String> triedMods = new ArrayList<String>();
			while(!addedMod&&triedMods.size()<8){
				int roll = GamePanel.randomNumber(1, 8);
				if(roll==1){
					if(!hasModifier("Knockback")){
						addModifier("Knockback",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Knockback");

					}
				}
				if(roll==2){
					if(!hasModifier("Life on hit")){
						addModifier("Life on hit",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Life on hit");

					}
				}
				if(roll==3){
					if(!hasModifier("Crit chance")){
						addModifier("Crit chance",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Crit chance");

					}
				}
				if(roll==4){
					if(!hasModifier("Crit damage")){
						addModifier("Crit damage",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Crit damage");

					}
				}
				if(roll==5){
					if(!hasModifier("Damage")){
						addModifier("Damage",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Damage");

					}
				}
				if(roll==6){
					if(!hasModifier("Percent Damage")){
						addModifier("Percent Damage",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Percent Damage");

					}
				}
				if(roll==7){
					if(!hasModifier("Percent Damage And Damage")){
						addModifier("Percent Damage And Damage",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Percent Damage And Damage");

					}
				}
			}
		}
		if(itemType=="SwingWeapon"){
			ArrayList<String> triedMods = new ArrayList<String>();
			while(!addedMod&&triedMods.size()<8){
				int roll = GamePanel.randomNumber(1, 8);
				if(roll==1){
					if(!hasModifier("Knockback")){
						addModifier("Knockback",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Knockback");

					}
				}
				if(roll==2){
					if(!hasModifier("Life on hit")){
						addModifier("Life on hit",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Life on hit");

					}
				}
				if(roll==3){
					if(!hasModifier("Swing width")){
						addModifier("Swing width",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Swing width");

					}
				}
				if(roll==4){
					if(!hasModifier("Crit chance")){
						addModifier("Crit chance",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Crit chance");

					}
				}
				if(roll==5){
					if(!hasModifier("Crit damage")){
						addModifier("Crit damage",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Crit damage");

					}
				}
				if(roll==6){
					if(!hasModifier("Damage")){
						addModifier("Damage",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Damage");

					}
				}
				if(roll==7){
					if(!hasModifier("Percent Damage")){
						addModifier("Percent Damage",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Percent Damage");

					}
				}
				if(roll==8){
					if(!hasModifier("Percent Damage And Damage")){
						addModifier("Percent Damage And Damage",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Percent Damage And Damage");

					}
				}
			}
		}
		if(itemType=="ProjectileWeapon"){
			ArrayList<String> triedMods = new ArrayList<String>();
			while(!addedMod&&triedMods.size()<9){
				int roll = GamePanel.randomNumber(1, 9);
				if(roll==1){
					if(!hasModifier("Pierce")){
						addModifier("Pierce",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Pierce");
					}
				}
				else if(roll==2){
					if(!hasModifier("Projectile Speed")){
						addModifier("Projectile Speed",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Projectile Speed");
					}
				}
				else if(roll==3){
					if(!hasModifier("Knockback")){
						addModifier("Knockback",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Knockback");
					}
				}
				else if(roll==4){
					if(!hasModifier("Projectile Range")){
						addModifier("Projectile Range",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("ProjectileRange");
					}
				}
				else if(roll==5){
					if(!hasModifier("Crit chance")){
						addModifier("Crit chance",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Crit chance");

					}
				}
				else if(roll==6){
					if(!hasModifier("Crit damage")){
						addModifier("Crit damage",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Crit damage");

					}
				}
				if(roll==7){
					if(!hasModifier("Damage")){
						addModifier("Damage",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Damage");

					}
				}
				if(roll==8){
					if(!hasModifier("Percent Damage")){
						addModifier("Percent Damage",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Percent Damage");

					}
				}
				if(roll==9){
					if(!hasModifier("Percent Damage And Damage")){
						addModifier("Percent Damage And Damage",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Percent Damage And Damage");

					}
				}
			}
			if(!addedMod){
				removeRandomModifier();
			}
		}
		if(itemType.equals("Armor")&&tooltip.itemType!="Helmet"&&tooltip.itemType!="Chest"&&tooltip.itemType!="Ring"&&tooltip.itemType!="Glove"&&tooltip.itemType!="Pants"&&tooltip.itemType!="Boot"){
			if(!hasModifier("Health")){
				addModifier("Health",false,increasedRarity);
				addedMod = true;

			}
		}
		if(tooltip.itemType=="Helmet"||tooltip.itemType=="Chest"){
			ArrayList<String> triedMods = new ArrayList<String>();
			while(!addedMod&&triedMods.size()<2){
				int roll = GamePanel.randomNumber(1, 2);
				if(roll==1){
					if(!hasModifier("Life Regeneration")){
						addModifier("Life Regeneration",false,increasedRarity);
						addedMod = true;
					}
					else{
						triedMods.add("Life Regeneration");
					}
				}
				if(roll==2){
					if(!hasModifier("Health")){
						addModifier("Health",false,increasedRarity);
						addedMod = true;

					}
					else{
						triedMods.add("lifeRegeneration");
					}
				}
			}
		}

	}
	public void addModifier(String name, boolean implicitMod, double itemRarity){
		int rarity = 0;
		int upperBound = 1000;
		if(GamePanel.player!=null){
			upperBound = (int)(1000.0/(GamePanel.player.getIncreasedRarity()+itemRarity+1.0));
		}

		int roll = GamePanel.randomNumber(1, upperBound);

		//	1/1000 super rare
		if(roll==1){//					1/1000
			rarity = 6;
		}
		//	5/1000 very rare
		else if(roll>1&&roll<=11){//	10/1000
			rarity = 5;
		}
		//	20/1000 rare
		else if(roll>11&&roll<=61){//	50/1000
			rarity = 4;
		}
		//	50/1000 uncommon
		else if(roll>61&&roll<=161){//	100/1000
			rarity = 3;
		}
		//	200/1000 common
		else if(roll>161&&roll<=361){//	200/1000
			rarity = 2;
		}
		//	639/1000 very common
		else{//							639/1000
			rarity = 1;
		}
		if(implicitMod){
			implicit = new ItemModifier(name,rarity);
		}
		else{
			modifiers.add(new ItemModifier(name,rarity));
		}

	}
	public void knockbackMonster(Monster monster){

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
	public void useOn(Item item){
		if(this.itemType=="Furniture"){
			if(this.tooltip.itemType=="Chest"){

			}
		}
		if(this.itemType=="Currency"){
			if(item.itemType!="Currency"){
				if(this.name.equals("Whetstone")){
					int change = 0;
					if(GamePanel.randomNumber(1, 10)<=4&&item.baseDamage+item.damageModifier>0&&item.damageModifier<3){
						change = -1;
					}
					else{
						change = 1;
					}
					item.damageModifier+=change;
					//remove one of these items from the player's inventory
					if(stackSize>1){
						stackSize--;
					}
					else if(item.damageModifier<3){
						GamePanel.player.inventory.removeItem(this);
					}
					GamePanel.player.inventory.useItem=null;
					item.tooltip.Update();
				}
				if(this.name.equals("Greater Whetstone")){
					int change = 0;
					if(GamePanel.randomNumber(1, 10)<=3&&item.baseDamage+item.damageModifier>0&&item.damageModifier<3){
						change = -1;
					}
					else{
						change = 1;
					}
					item.damageModifier+=change;
					//remove one of these items from the player's inventory
					if(stackSize>1){
						stackSize--;
					}
					else if(item.damageModifier<3){
						GamePanel.player.inventory.removeItem(this);
					}
					GamePanel.player.inventory.useItem=null;
					item.tooltip.Update();
				}
				if(this.name.equals("Superior Whetstone")){
					int change = 0;
					if(GamePanel.randomNumber(1, 10)<=1&&item.baseDamage+item.damageModifier>0&&item.damageModifier<3){
						change = -1;
					}
					else if(item.damageModifier<3){
						change = 1;
					}
					item.damageModifier+=change;
					//remove one of these items from the player's inventory
					if(stackSize>1){
						stackSize--;
					}
					else{
						GamePanel.player.inventory.removeItem(this);
					}
					GamePanel.player.inventory.useItem=null;
					item.tooltip.Update();
				}
				if(this.name.equals("Engraving Chisel")){
					System.out.println("using engraving chisel");
					int roll = GamePanel.randomNumber(1, 10);
					if(roll<=3){//remove a mod
						if(item.modifiers.size()>0){
							System.out.println("removed a modifier");
							item.removeRandomModifier();
						}
						else{
							System.out.println("added a modifier");
							item.addRandomModifier(0);
						}
					}
					else if(roll>3){
						if(item.modifiers.size()<item.modifierLimit){
							System.out.println("added a modifier");
							item.addRandomModifier(0);
						}
						else{
							System.out.println("removed a modifier");
							item.removeRandomModifier();
						}
					}
					//remove one of these items from the player's inventory
					if(stackSize>1){
						stackSize--;
					}
					else{
						GamePanel.player.inventory.removeItem(this);
					}
					GamePanel.player.inventory.useItem=null;
					item.tooltip.Update();
				}
				if(this.name.equals("Shaping Hammer")){
					DecimalFormat df = new DecimalFormat("#.00");
					int rand = GamePanel.randomNumber(0, item.modifiers.size()-1);
					if(item.modifiers.size()>0){
						if(item.modifiers.get(rand).name=="Health"){
							item.modifiers.get(rand).health = GamePanel.randomNumber(item.modifiers.get(rand).modRange.x, item.modifiers.get(rand).modRange.y);
							item.modifiers.get(rand).description = "+"+item.modifiers.get(rand).health+" To maximum health";
						}
						if(item.modifiers.get(rand).name=="Life Regeneration"){
							item.modifiers.get(rand).lifeRegen = GamePanel.randomNumber(item.modifiers.get(rand).modRange.x, item.modifiers.get(rand).modRange.y);
							item.modifiers.get(rand).description = "Grants an additional "+item.modifiers.get(rand).lifeRegen+" life regeneration per minute";
						}
						if(item.modifiers.get(rand).name=="Projectile Range"){
							item.modifiers.get(rand).projectileRange = GamePanel.randomNumber(item.modifiers.get(rand).modRange.x, item.modifiers.get(rand).modRange.y);
							item.modifiers.get(rand).description = "+"+item.modifiers.get(rand).projectileRange+" Projectile range";
						}
						if(item.modifiers.get(rand).name=="Pierce"){
							item.modifiers.get(rand).pierce = GamePanel.randomNumber(item.modifiers.get(rand).modRange.x, item.modifiers.get(rand).modRange.y);
							if(item.modifiers.get(rand).pierce==1)
								item.modifiers.get(rand).description = "Arrows pierce "+item.modifiers.get(rand).pierce+" additional time";
							else
								item.modifiers.get(rand).description = "Arrows pierce "+item.modifiers.get(rand).pierce+" additional times";
						}
						if(item.modifiers.get(rand).name=="Movement Speed"){
							item.modifiers.get(rand).moveSpeed = (double)((double)GamePanel.randomNumber(item.modifiers.get(rand).modRange.x, item.modifiers.get(rand).modRange.y)/100.0);
							if(item.modifiers.get(rand).moveSpeed>=0){
								item.modifiers.get(rand).description = df.format(item.modifiers.get(rand).moveSpeed*100)+"% Increased movement speed";
							}
						}
						if(item.modifiers.get(rand).name=="Attack Speed"){
							item.modifiers.get(rand).attackSpeed = (double)((double)GamePanel.randomNumber(item.modifiers.get(rand).modRange.x, item.modifiers.get(rand).modRange.y)/100.0);
							if(item.modifiers.get(rand).attackSpeed>=0){
								item.modifiers.get(rand).description = df.format(item.modifiers.get(rand).attackSpeed*100)+"% Increased attack speed";
							}
						}
						if(item.modifiers.get(rand).name=="Projectile Speed"){
							item.modifiers.get(rand).projectileSpeed = (double)((double)GamePanel.randomNumber(item.modifiers.get(rand).modRange.x, item.modifiers.get(rand).modRange.y)/100.0);
							if(item.modifiers.get(rand).projectileSpeed>=0){
								item.modifiers.get(rand).description = df.format(item.modifiers.get(rand).projectileSpeed*100)+"% Increased projectile speed";
							}
						}
						if(item.modifiers.get(rand).name=="Knockback"){
							item.modifiers.get(rand).knockback = (double)((double)GamePanel.randomNumber(item.modifiers.get(rand).modRange.x, item.modifiers.get(rand).modRange.y)/100.0);
							item.modifiers.get(rand).description = df.format(item.modifiers.get(rand).knockback*100)+"% Increased knockback";
						}
						if(item.modifiers.get(rand).name=="Life on hit"){
							item.modifiers.get(rand).lifeOnHit = (double)GamePanel.randomNumber(item.modifiers.get(rand).modRange.x, item.modifiers.get(rand).modRange.y)/2.0;
							item.modifiers.get(rand).description = "+"+df.format(item.modifiers.get(rand).lifeOnHit)+" Health gained on hit";
						}
						if(item.modifiers.get(rand).name=="Swing width"){
							item.modifiers.get(rand).swingWidth = (double)((double)GamePanel.randomNumber(item.modifiers.get(rand).modRange.x, item.modifiers.get(rand).modRange.y)/100.0);
							item.modifiers.get(rand).description = df.format(item.modifiers.get(rand).swingWidth*100)+"% Increased swing width";
						}
						if(item.modifiers.get(rand).name=="Crit chance"){
							item.modifiers.get(rand).critChance = (double)((double)GamePanel.randomNumber(item.modifiers.get(rand).modRange.x, item.modifiers.get(rand).modRange.y)/100.0);
							item.modifiers.get(rand).description = df.format(item.modifiers.get(rand).critChance*100)+"% Increased critical strike chance";
						}
						if(item.modifiers.get(rand).name=="Crit damage"){
							item.modifiers.get(rand).critDamage = (double)((double)GamePanel.randomNumber(item.modifiers.get(rand).modRange.x, item.modifiers.get(rand).modRange.y)/100.0);
							item.modifiers.get(rand).description = df.format(item.modifiers.get(rand).critDamage*100)+"% Increased critical strike damage";
						}
						if(item.modifiers.get(rand).name=="Global damage"){
							item.modifiers.get(rand).globalDamage = (double)((double)GamePanel.randomNumber(item.modifiers.get(rand).modRange.x, item.modifiers.get(rand).modRange.y)/100.0);
							item.modifiers.get(rand).description = df.format(item.modifiers.get(rand).globalDamage*100)+"% More global damage";
						}
						if(item.modifiers.get(rand).name=="MovementSpeedOnDaggerKill"){	
							item.modifiers.get(rand).moveSpeedOnDaggerKill = (double)((double)GamePanel.randomNumber(item.modifiers.get(rand).modRange.x, item.modifiers.get(rand).modRange.y)/100.0);
							item.modifiers.get(rand).description = df.format(item.modifiers.get(rand).moveSpeedOnDaggerKill*100)+"% Increased movement speed for "+item.modifiers.get(rand).effectDuration+" seconds on dagger kill";
						}
						if(item.modifiers.get(rand).name=="ArmorCoverageOnSwordHit"){
							item.modifiers.get(rand).armorCoverageOnSwordHit = (double)((double)GamePanel.randomNumber(item.modifiers.get(rand).modRange.x, item.modifiers.get(rand).modRange.y)/100.0);
							item.modifiers.get(rand).description = "1% Chance to gain "+df.format(item.modifiers.get(rand).armorCoverageOnSwordHit*100)+"% more armor coverage for 2 seconds on sword hit.";
						}
					}
					//remove one of these items from the player's inventory
					if(stackSize>1){
						stackSize--;
					}
					else{
						GamePanel.player.inventory.removeItem(this);
					}
					GamePanel.player.inventory.useItem=null;
					item.tooltip.Update();
				}
			}
			else{
				GamePanel.player.inventory.useItem=null;
			}
		}
	}
	public int getPierce(){
		int temp = pierce;
		for(int i = 0; i<modifiers.size();i++){
			temp+=modifiers.get(i).pierce;
		}
		return temp;
	}
	public int getProjectiles(){
		int total = projectiles;
		for(int i = 0; i<modifiers.size();i++){
			total+=modifiers.get(i).projectiles;
		}
		return total;
	}
	public double getCritChanceIncrease(){
		double total = 0;
		for(int i = 0; i<modifiers.size();i++){
			total+=modifiers.get(i).critChance;
		}
		return total;
	}
	public double getSwingWidth(){
		double total = 0;
		for(int i = 0; i<modifiers.size();i++){
			total+=modifiers.get(i).swingWidth;
		}
		return ((double)swingWidth*(1.0+total));
	}
	public int getRange(){
		int total = range;
		for(int i = 0; i<modifiers.size();i++){
			total+=modifiers.get(i).projectileRange;
		}
		return total;
	}
	public double getDamage(){
		double dmg = baseDamage;
		if(user==null&&GamePanel.player!=null){
			int addedDamage = 0;
			for(int i = 0; i<modifiers.size();i++){
				addedDamage+=modifiers.get(i).damage;
			}
			double increasedDamage = 0;
			for(int i = 0; i<modifiers.size();i++){
				increasedDamage+=modifiers.get(i).percentDamage;
			}
			dmg = ((double)baseDamage+(double)damageModifier+addedDamage)*(1.0+increasedDamage)*GamePanel.player.getGlobalDamage();
			//crit chance
			double critChanceTemp= critChance*(getCritChanceIncrease()+1.0)*(GamePanel.player.getCritChance()+1.0);
			if(GamePanel.randomNumber(1, 100)<=critChanceTemp){
				dmg=dmg*(critDamage*GamePanel.player.getCritDamage());
			}
		}
		return dmg;
	}
	public double getBaseDamage(){
		double dmg = baseDamage;
		if(user==null&&GamePanel.player!=null){
			int addedDamage = 0;
			for(int i = 0; i<modifiers.size();i++){
				addedDamage+=modifiers.get(i).damage;
			}
			double increasedDamage = 0;
			for(int i = 0; i<modifiers.size();i++){
				increasedDamage+=modifiers.get(i).percentDamage;
			}
			dmg = ((double)baseDamage+(double)damageModifier+addedDamage)*(1.0+increasedDamage)*GamePanel.player.getGlobalDamage();

		}
		return dmg;
	}
	public double getProjectileSpeed(){
		double temp = 0;
		for(int i = 0; i<modifiers.size();i++){
			temp+=modifiers.get(i).projectileSpeed;
		}
		return projectileSpeed*(1.0+temp);
	}
	public double getKnockback(){
		double temp = 0;
		for(int i = 0; i<modifiers.size();i++){
			temp+=modifiers.get(i).knockback;
		}
		return knockback*(1.0+temp);
	}
	public void applyEffectToPlayer(){
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
				StatusEffect temp = new StatusEffect(tempString,(int)effects.get(i).duration,monster,this);
				monster.buffs.add(temp);
			}
		}
	}
	public double getLifeOnHit(){
		double total = 0;
		for(int i = 0; i<modifiers.size();i++){
			total += modifiers.get(i).lifeOnHit;
		}
		return total;
	}
	public double getAttackSpeed(){
		double total = 0;
		if(GamePanel.player!=null&&user==null){
			Item item=null;
			for(int i = 0; i<7;i++){
				item=null;
				if(GamePanel.player.inventory.head.item!=null&&i==0)
					item=GamePanel.player.inventory.head.item;
				if(GamePanel.player.inventory.chest.item!=null&&i==1)
					item=GamePanel.player.inventory.chest.item;
				if(GamePanel.player.inventory.pants.item!=null&&i==2)
					item=GamePanel.player.inventory.pants.item;
				if(GamePanel.player.inventory.leftGlove.item!=null&&i==3)
					item=GamePanel.player.inventory.leftGlove.item;
				if(GamePanel.player.inventory.rightGlove.item!=null&&i==4)
					item=GamePanel.player.inventory.rightGlove.item;
				if(GamePanel.player.inventory.leftBoot.item!=null&&i==5)
					item=GamePanel.player.inventory.leftBoot.item;
				if(GamePanel.player.inventory.rightBoot.item!=null&&i==6)
					item=GamePanel.player.inventory.rightBoot.item;
				if(item!=null){
					for(int j = 0; j<item.modifiers.size();j++){
						if(item.modifiers.get(j)!=null){
							total+=item.modifiers.get(j).attackSpeed;
						}
					}
				}
			}
		}
		//System.out.println("attack speed: "+attackSpeed*(1.0+total));
		return attackSpeed*(1.0+total);
	}
	public void swingWeapon(){
		swinging = true;
		if(this.itemType=="SwingWeapon"){
			if(currentDrawAngle<drawAngle+getSwingWidth()){
				currentDrawAngle+=1;//swingSpeed;

			}
			else{
				currentDrawAngle = drawAngle;
				swinging = false;
				monstersHit.clear();
			}
			if(name=="Mario's Sword"){
				double x = GamePanel.player.xpos-50;//+(Math.cos(GamePanel.player.weaponAngle)*50);
				double y = GamePanel.player.ypos-50;//+(Math.sin(GamePanel.player.weaponAngle)*50);
				GamePanel.projectiles.add(new Projectile((int)x, (int)y, GamePanel.player.weaponAngle, 3,this));
				projectileSpeed = 4;
			}
		}
		else if(this.itemType=="Spear"){
			if(shootCounter<=0){
				shootCounter=(int)(60.0/getAttackSpeed());
				GamePanel.player.isCharging = true;
				GamePanel.player.chargeDistance=this.chargeDistance;
			}
		}
		else if(this.itemType=="MonsterSwingWeapon"){
			if(shootCounter<=0){
				shootCounter=(int)(60.0/getAttackSpeed());
				if(name == "Spider Queen's Charge"){
					GamePanel.player.takeDamage(getDamage());
					applyEffectToPlayer();
				}
				if(name=="Poison Fang"){
					GamePanel.player.takeDamage(getDamage());
					applyEffectToPlayer();
				}
			}
		}
		else if(this.itemType=="ProjectileWeapon"){
			if(shootCounter<=0){
				shootCounter=(int)(60.0/getAttackSpeed());
				//Point dest=new Point(AppletUI.xoffset+(int)(Math.cos(Math.toRadians(GamePanel.player.desiredAngleInDegrees))*range),AppletUI.yoffset+(int)(Math.sin(Math.toRadians(GamePanel.player.desiredAngleInDegrees))*range));
				if(name=="Simple Bow"){
					GamePanel.projectiles.add(new Projectile((int)GamePanel.player.xpos, (int)GamePanel.player.ypos, GamePanel.player.weaponAngle, 1,this));
				}
				if(name=="Spider Web Shooter"){
					if(user!=null)
						GamePanel.projectiles.add(new Projectile((int)user.xpos, (int)user.ypos, user.angleInDegrees, 4,this));//4
					else
						GamePanel.projectiles.add(new Projectile((int)GamePanel.player.xpos, (int)GamePanel.player.ypos, GamePanel.player.weaponAngle, 4,this));
				}
				if(name=="Splinter Bow"){
					//determine the angle to shoot the arrows at
					//get the point the cursor is at
					double x = ((int)GamePanel.player.xpos)-AppletUI.xoffset+AppletUI.mousePos.x;
					double y = ((int)GamePanel.player.ypos)-AppletUI.yoffset+AppletUI.mousePos.y;
					//the distance between projectiles at the cursor position
					double spread = 50.0;
					//the angles perpendicular to the weapon angle
					double angleA = GamePanel.player.weaponAngle+90;
					double angleB = GamePanel.player.weaponAngle-90;
					ArrayList<Double> projectileAngles = new ArrayList<Double>();
					int leftProjectiles = getProjectiles()/2;

					//for each projectile on the left
					for(int i = 1; i<leftProjectiles+1;i++){
						double angleInRadians = Math.toRadians(angleB);
						double newX = x+(Math.cos(angleInRadians)*i*spread)-(Math.cos(angleInRadians)*(spread/2));
						double newY = y+(Math.sin(angleInRadians)*i*spread)-(Math.sin(angleInRadians)*(spread/2));
						if(getProjectiles()%2>0){
							newX = x+(Math.cos(angleInRadians)*i*spread);
							newY = y+(Math.sin(angleInRadians)*i*spread);
						}

						//get the angle between the player and the new x,y position
						double projectileAngle = Math.toDegrees(Math.atan2((newY-GamePanel.player.ypos),((newX-GamePanel.player.xpos))));
						GamePanel.projectiles.add(new Projectile((int)GamePanel.player.xpos, (int)GamePanel.player.ypos, projectileAngle, 1,this));
					}
					//for each projectile on the right
					for(int i = 1; i<leftProjectiles+1;i++){
						double angleInRadians = Math.toRadians(angleA); 
						double newX = x+(Math.cos(angleInRadians)*i*spread)-(Math.cos(angleInRadians)*(spread/2));
						double newY = y+(Math.sin(angleInRadians)*i*spread)-(Math.sin(angleInRadians)*(spread/2));
						if(getProjectiles()%2>0){
							newX = x+(Math.cos(angleInRadians)*i*spread);
							newY = y+(Math.sin(angleInRadians)*i*spread);
						}
						//get the angle between the player and the new x,y position
						double projectileAngle = Math.toDegrees(Math.atan2((newY-GamePanel.player.ypos),((newX-GamePanel.player.xpos))));
						GamePanel.projectiles.add(new Projectile((int)GamePanel.player.xpos, (int)GamePanel.player.ypos, projectileAngle, 1,this));
						//System.out.println("added a projectile at: "+projectileAngle+" degrees");
					}
					if(getProjectiles()%2>0){
						GamePanel.projectiles.add(new Projectile((int)GamePanel.player.xpos, (int)GamePanel.player.ypos, GamePanel.player.weaponAngle, 1,this));
					}
				}
				else if(name=="KingCurrency's Bow"){
					//determine the angle to shoot the arrows at
					//get the point the cursor is at
					double x = ((int)GamePanel.player.xpos)-AppletUI.xoffset+AppletUI.mousePos.x;
					double y = ((int)GamePanel.player.ypos)-AppletUI.yoffset+AppletUI.mousePos.y;
					//the distance between projectiles at the cursor position
					double spread = 50.0;
					//the angles perpendicular to the weapon angle
					double angleA = GamePanel.player.weaponAngle+90;
					double angleB = GamePanel.player.weaponAngle-90;
					ArrayList<Double> projectileAngles = new ArrayList<Double>();
					int leftProjectiles = getProjectiles()/2;

					//for each projectile on the left
					for(int i = 1; i<leftProjectiles+1;i++){
						double angleInRadians = Math.toRadians(angleB);
						double newX = x+(Math.cos(angleInRadians)*i*spread)-(Math.cos(angleInRadians)*(spread/2));
						double newY = y+(Math.sin(angleInRadians)*i*spread)-(Math.sin(angleInRadians)*(spread/2));
						if(getProjectiles()%2>0){
							newX = x+(Math.cos(angleInRadians)*i*spread);
							newY = y+(Math.sin(angleInRadians)*i*spread);
						}

						//get the angle between the player and the new x,y position
						double projectileAngle = Math.toDegrees(Math.atan2((newY-GamePanel.player.ypos),((newX-GamePanel.player.xpos))));
						GamePanel.projectiles.add(new Projectile((int)GamePanel.player.xpos, (int)GamePanel.player.ypos, projectileAngle, 2,this));
					}
					//for each projectile on the right
					for(int i = 1; i<leftProjectiles+1;i++){
						double angleInRadians = Math.toRadians(angleA); 
						double newX = x+(Math.cos(angleInRadians)*i*spread)-(Math.cos(angleInRadians)*(spread/2));
						double newY = y+(Math.sin(angleInRadians)*i*spread)-(Math.sin(angleInRadians)*(spread/2));
						if(getProjectiles()%2>0){
							newX = x+(Math.cos(angleInRadians)*i*spread);
							newY = y+(Math.sin(angleInRadians)*i*spread);
						}
						//get the angle between the player and the new x,y position
						double projectileAngle = Math.toDegrees(Math.atan2((newY-GamePanel.player.ypos),((newX-GamePanel.player.xpos))));
						GamePanel.projectiles.add(new Projectile((int)GamePanel.player.xpos, (int)GamePanel.player.ypos, projectileAngle, 2,this));
						//System.out.println("added a projectile at: "+projectileAngle+" degrees");
					}
					if(getProjectiles()%2>0){
						GamePanel.projectiles.add(new Projectile((int)GamePanel.player.xpos, (int)GamePanel.player.ypos, GamePanel.player.weaponAngle, 2,this));
					}
				}
			}
		}
		else if(this.itemType=="MagicWeapon"){
			if(shootCounter<=0){
				shootCounter=(int)(60.0/getAttackSpeed());
				if(name=="Shockwave Staff"&&GamePanel.player.currentHealth>1){
					GamePanel.player.currentHealth-=1;
					if(GamePanel.player.currentHealth<=0){
						GamePanel.player.die();
					}
					System.out.println("used shockwave staff");
					int radius = 100;
					for(int i = (int)(GamePanel.player.xpos/50)-((radius/50)+1);i<(int)(GamePanel.player.xpos/50)+((radius/50)+1);i++){
						for(int j = (int)(GamePanel.player.ypos/50)-((radius/50)+1);j<(int)(GamePanel.player.ypos/50)+((radius/50)+1);j++){
							if(i>0&&j>0&&i<GamePanel.zones.get(GamePanel.currentZone).levelWidth-1&&j<GamePanel.zones.get(GamePanel.currentZone).levelWidth-1){
								//loop through all the monsters within the radius
								for(int k = 0; k<GamePanel.zones.get(GamePanel.currentZone).map[i][j].monsters.size();k++){
									//get distance between this monster and the player
									double distanceToPlayer = Math.sqrt(Math.pow(((GamePanel.player.xpos)-GamePanel.zones.get(GamePanel.currentZone).map[i][j].monsters.get(k).xpos),2)+Math.pow(((GamePanel.player.ypos)-GamePanel.zones.get(GamePanel.currentZone).map[i][j].monsters.get(k).ypos),2));
									if(distanceToPlayer<400){
										knockbackMonster(GamePanel.zones.get(GamePanel.currentZone).map[i][j].monsters.get(k));
									}
								}
							}
						}
					}

				}
				else if(name=="Teleport Staff"){
					int x = ((int)GamePanel.player.xpos)-AppletUI.xoffset+AppletUI.mousePos.x;
					int y = ((int)GamePanel.player.ypos)-AppletUI.yoffset+AppletUI.mousePos.y;
					int startX = (int)GamePanel.player.xpos;
					int startY = (int)GamePanel.player.ypos;
					if(GamePanel.zones.get(GamePanel.currentZone).map[x/50][y/50].collisionBox==null){
						GamePanel.effects.add(new Animation(GamePanel.nonBattleAnims,9,150,0,startX,startY,false,0,0));
						GamePanel.player.xpos=x;
						GamePanel.player.ypos=y;
						GamePanel.player.desiredPosition.x=x;
						GamePanel.player.desiredPosition.y=y;
						GamePanel.effects.add(new Animation(GamePanel.nonBattleAnims,9,150,1,AppletUI.xoffset+(int)GamePanel.player.xpos-(int)GamePanel.player.xpos,AppletUI.yoffset+(int)GamePanel.player.ypos-(int)GamePanel.player.ypos,false,0,0));
					}
				}
			}
		}
		if(user==null){
			for(int i = 0; i<10;i++){
				if(GamePanel.player.inventory.items[i][0].item!=null){
					if(GamePanel.player.inventory.items[i][0].item!=this)
						GamePanel.player.inventory.items[i][0].item.swinging=false;
				}
			}
		}


	}
	public void update(){
		if(swinging&&itemType=="SwingWeapon"){
			swingWeapon();
		}
		if(shootCounter>0)
			shootCounter--;
	}
	public void Draw(Graphics g){

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		AffineTransform at = new AffineTransform();
		//set position on screen
		if(itemType!="Spear")
			at.translate(AppletUI.xoffset+25, AppletUI.yoffset-((double)((double)(artwork.getHeight())/2.0)));
		//set size of image
		at.scale(size, size);
		if(xpos!=-1&&ypos!=-1&&tooltip!=null){
			tooltip.Draw(g,true);
		}
		if(itemType=="SwingWeapon"){
			if(swinging){
				//rotate image
				at.rotate(Math.toRadians(currentDrawAngle+swingAngle),-20,((double)((double)(artwork.getHeight())/2.0)));
				//draw image
				g2d.drawImage(artwork, at, null);
				//create a line at the sword's position to check for sword collision with stuff
				//determine the position of sword handle
				Point handlePos=new Point(AppletUI.xoffset+(int)(Math.cos(Math.toRadians(currentDrawAngle+swingAngle))*20),AppletUI.yoffset+(int)(Math.sin(Math.toRadians(currentDrawAngle+swingAngle))*20));
				//determine the position of sword tip
				Point tipPos=new Point(AppletUI.xoffset+(int)(Math.cos(Math.toRadians(currentDrawAngle+swingAngle))*(20+artwork.getWidth())),AppletUI.yoffset+(int)(Math.sin(Math.toRadians(currentDrawAngle+swingAngle))*(20+artwork.getWidth())));
				Line2D swordpos = new Line2D.Float(handlePos.x,handlePos.y,tipPos.x,tipPos.y);
				//g.setColor(Color.red);
				//g.drawLine(handlePos.x,handlePos.y,tipPos.x,tipPos.y);
				//check the tiles that are in range of the weapon for collision
				int weaponRadius = 140;
				int checkRange = weaponRadius/50;
				if(weaponRadius%50>0){
					checkRange+=2;
				}
				int startX = (int)(GamePanel.player.xpos/50)-checkRange;
				int startY = (int)(GamePanel.player.ypos/50)-checkRange;
				int endX = (int)(GamePanel.player.xpos/50)+checkRange;
				int endY = (int)(GamePanel.player.ypos/50)+checkRange;
				if(startX<0){
					startX=0;
				}
				if(startY<0){
					startY=0;
				}
				if(endX>GamePanel.zones.get(GamePanel.currentZone).levelWidth-1){
					endX=GamePanel.zones.get(GamePanel.currentZone).levelWidth-1;
				}
				if(endY>GamePanel.zones.get(GamePanel.currentZone).levelWidth-1){
					endY=GamePanel.zones.get(GamePanel.currentZone).levelWidth-1;
				}
				for(int x = startX; x<endX;x++){
					for(int y = startY; y<endY;y++){
						Rectangle tileBox = new Rectangle(AppletUI.xoffset+GamePanel.zones.get(GamePanel.currentZone).map[x][y].xpos-((int)GamePanel.player.xpos),AppletUI.yoffset+GamePanel.zones.get(GamePanel.currentZone).map[x][y].ypos-((int)GamePanel.player.ypos),50,50);
						//g.setColor(Color.red);
						//g.drawRect(AppletUI.xoffset+GamePanel.zones.get(GamePanel.currentZone).map[x][y].xpos-((int)GamePanel.player.xpos),AppletUI.yoffset+GamePanel.zones.get(GamePanel.currentZone).map[x][y].ypos-((int)GamePanel.player.ypos),50,50);
						if(swordpos.intersects(tileBox)){
							if(GamePanel.zones.get(GamePanel.currentZone).map[x][y].vase!=null){
								GamePanel.zones.get(GamePanel.currentZone).map[x][y].vase.breakOpen(this);
							}
						}
						//check for collision with monsters
						for(int i = 0;i<GamePanel.zones.get(GamePanel.currentZone).map[x][y].monsters.size();i++){
							if(!monstersHit.contains(GamePanel.zones.get(GamePanel.currentZone).map[x][y].monsters.get(i))){
								

								Rectangle monsterBox = new Rectangle(AppletUI.xoffset-15+(int)GamePanel.zones.get(GamePanel.currentZone).map[x][y].monsters.get(i).xpos-((int)GamePanel.player.xpos),AppletUI.yoffset-15+(int)GamePanel.zones.get(GamePanel.currentZone).map[x][y].monsters.get(i).ypos-((int)GamePanel.player.ypos),30,30);
								if(swordpos.intersects(monsterBox)){
									//apply effects to the monster that was hit
									for(int k = 0; k<effects.size();k++){
										applyEffectToMonster(GamePanel.zones.get(GamePanel.currentZone).map[(int) (xpos/50)][(int) (ypos/50)].monsters.get(i));
									}
									monstersHit.add(GamePanel.zones.get(GamePanel.currentZone).map[x][y].monsters.get(i));
									GamePanel.zones.get(GamePanel.currentZone).map[x][y].monsters.get(i).takeDamage(getDamage(),this);
									knockbackMonster(GamePanel.zones.get(GamePanel.currentZone).map[x][y].monsters.get(i));
									if(GamePanel.player.currentHealth+getLifeOnHit()<=GamePanel.player.getMaxHealth()){
										GamePanel.player.currentHealth+=getLifeOnHit();
									}
									else{
										GamePanel.player.currentHealth=GamePanel.player.getMaxHealth();
									}
								}
							}
						}
					}
				}
				//check monsters for collision with weapon
				//				for(int i = 0; i<GamePanel.zones.get(GamePanel.currentZone).monsters.size();i++){
				//					Rectangle monsterBox = new Rectangle(AppletUI.xoffset-15+(int)GamePanel.zones.get(GamePanel.currentZone).monsters.get(i).xpos-((int)GamePanel.player.xpos),AppletUI.yoffset-15+(int)GamePanel.zones.get(GamePanel.currentZone).monsters.get(i).ypos-((int)GamePanel.player.ypos),30,30);
				//					//g.setColor(Color.red);
				//					//g.drawRect(AppletUI.xoffset+(int)GamePanel.zones.get(GamePanel.currentZone).monsters.get(i).xpos-((int)GamePanel.player.xpos),AppletUI.yoffset+(int)GamePanel.zones.get(GamePanel.currentZone).monsters.get(i).ypos-((int)GamePanel.player.ypos),30,30);
				//					if(swordpos.intersects(monsterBox)){
				//						GamePanel.zones.get(GamePanel.currentZone).monsters.get(i).takeDamage(baseDamage+damageModifier);
				//						knockbackMonster(GamePanel.zones.get(GamePanel.currentZone).monsters.get(i));
				//					}
				//				}
				//swingWeapon();
			}
		}
		else if((itemType=="ProjectileWeapon"||itemType=="MagicWeapon")&&swinging){
			//rotate image
			at.rotate(Math.toRadians(GamePanel.player.weaponAngle),-25,((double)((double)(artwork.getHeight())/2.0)));
			//draw image
			g2d.drawImage(artwork, at, null);

		}
		else if(itemType=="Spear"&&swinging){
			at.translate(AppletUI.xoffset-25, AppletUI.yoffset-((double)((double)(artwork.getHeight())/2.0)));
			//rotate image
			at.rotate(Math.toRadians(GamePanel.player.weaponAngle),25,((double)((double)(artwork.getHeight())/2.0)));
			//draw image
			g2d.drawImage(artwork, at, null);
			//determine the position of sword handle
			Point handlePos=new Point(AppletUI.xoffset+(int)(Math.cos(Math.toRadians(GamePanel.player.weaponAngle))*20),AppletUI.yoffset+(int)(Math.sin(Math.toRadians(GamePanel.player.weaponAngle))*20));
			//determine the position of sword tip
			Point tipPos=new Point(AppletUI.xoffset+(int)(Math.cos(Math.toRadians(GamePanel.player.weaponAngle))*(-20+artwork.getWidth())),AppletUI.yoffset+(int)(Math.sin(Math.toRadians(GamePanel.player.weaponAngle))*(-20+artwork.getWidth())));
			Line2D swordpos = new Line2D.Float(handlePos.x,handlePos.y,tipPos.x,tipPos.y);
			//g.setColor(Color.red);
			//g.drawLine(handlePos.x,handlePos.y,tipPos.x,tipPos.y);
			if(GamePanel.player.isCharging){
				int weaponRadius = 140;
				int checkRange = weaponRadius/50;
				if(weaponRadius%50>0){
					checkRange+=2;
				}
				int startX = (int)(GamePanel.player.xpos/50)-checkRange;
				int startY = (int)(GamePanel.player.ypos/50)-checkRange;
				int endX = (int)(GamePanel.player.xpos/50)+checkRange;
				int endY = (int)(GamePanel.player.ypos/50)+checkRange;
				if(startX<0){
					startX=0;
				}
				if(startY<0){
					startY=0;
				}
				if(endX>GamePanel.zones.get(GamePanel.currentZone).levelWidth-1){
					endX=GamePanel.zones.get(GamePanel.currentZone).levelWidth-1;
				}
				if(endY>GamePanel.zones.get(GamePanel.currentZone).levelWidth-1){
					endY=GamePanel.zones.get(GamePanel.currentZone).levelWidth-1;
				}
				for(int x = startX; x<endX;x++){
					for(int y = startY; y<endY;y++){
						Rectangle tileBox = new Rectangle(AppletUI.xoffset+GamePanel.zones.get(GamePanel.currentZone).map[x][y].xpos-((int)GamePanel.player.xpos),AppletUI.yoffset+GamePanel.zones.get(GamePanel.currentZone).map[x][y].ypos-((int)GamePanel.player.ypos),50,50);
						//g.setColor(Color.red);
						//g.drawRect(AppletUI.xoffset+GamePanel.zones.get(GamePanel.currentZone).map[x][y].xpos-((int)GamePanel.player.xpos),AppletUI.yoffset+GamePanel.zones.get(GamePanel.currentZone).map[x][y].ypos-((int)GamePanel.player.ypos),50,50);
						if(swordpos.intersects(tileBox)){
							if(GamePanel.zones.get(GamePanel.currentZone).map[x][y].vase!=null){
								GamePanel.zones.get(GamePanel.currentZone).map[x][y].vase.breakOpen(this);
							}
						}
						//check for collision with monsters
						for(int i = 0;i<GamePanel.zones.get(GamePanel.currentZone).map[x][y].monsters.size();i++){

							Rectangle monsterBox = new Rectangle(AppletUI.xoffset-15+(int)GamePanel.zones.get(GamePanel.currentZone).map[x][y].monsters.get(i).xpos-((int)GamePanel.player.xpos),AppletUI.yoffset-15+(int)GamePanel.zones.get(GamePanel.currentZone).map[x][y].monsters.get(i).ypos-((int)GamePanel.player.ypos),30,30);
							if(swordpos.intersects(monsterBox)){
								//apply effects to the monster that was hit
								for(int k = 0; k<effects.size();k++){
									applyEffectToMonster(GamePanel.zones.get(GamePanel.currentZone).map[x][y].monsters.get(i));
								}
								if(!monstersHit.contains(GamePanel.zones.get(GamePanel.currentZone).map[x][y].monsters.get(i))){
									monstersHit.add(GamePanel.zones.get(GamePanel.currentZone).map[x][y].monsters.get(i));
									GamePanel.zones.get(GamePanel.currentZone).map[x][y].monsters.get(i).takeDamage(getDamage(),this);
									if(GamePanel.player.currentHealth+getLifeOnHit()<=GamePanel.player.getMaxHealth()){
										GamePanel.player.currentHealth+=getLifeOnHit();
									}
									else{
										GamePanel.player.currentHealth=GamePanel.player.getMaxHealth();
									}
								}
								knockbackMonster(GamePanel.zones.get(GamePanel.currentZone).map[x][y].monsters.get(i));

							}

						}
					}
				}
			}
			else{
				monstersHit.clear();
			}
		}
		//		else if(itemType=="MagicWeapon"){
		//			at.rotate(Math.toRadians(GamePanel.player.weaponAngle),-25,((double)((double)(artwork.getHeight())/2.0)));
		//			g2d.drawImage(artwork, at, null);
		//		}

	}

}
