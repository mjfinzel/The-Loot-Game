package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.text.DecimalFormat;

public class Tooltip {
	String itemName;
	String itemDescription = "";
	String useDescription = "";
	String damage = "";
	String damageReduction = "";
	String armorCoverage = "";
	String attacksPerSecond = "";
	String critChance = "";
	final String BowDescription = "This is a ranged weapon. Equip it in your weapon slot to use it.";
	final String Weapon = "Weapon";
	String itemType;
	Item item;
	int width=0;
	public Tooltip(Item Item){
		item = Item;
		Update();


	}
	public void Update(){
		DecimalFormat df = new DecimalFormat("#.00");
		damage = "Deals "+(int)(item.getBaseDamage()-1)+"-"+(int)(item.getBaseDamage()+1)+" Damage";
		attacksPerSecond = df.format(item.getAttackSpeed())+" Attacks per second";
		itemName = item.name;
		damageReduction = ("Damage reduction: "+item.damageReduction*100)+"%";
		armorCoverage = ("Armor coverage: "+(item.armorCoverage*100)+"%");
		if(GamePanel.player!=null)
			critChance = (df.format((item.critChance*(1.0+item.getCritChanceIncrease()))*(GamePanel.player.getCritChance()+1.0))+"% Critical strike chance");
		if(itemName.equals("Simple Bow")){
			itemType = Weapon;
			itemDescription = BowDescription;
			useDescription = "This is a weapon, equip it in the top row of your inventory and press the number on your keyboard corresponding to the inventory slot you put it in to use it.";
			attacksPerSecond = df.format(item.getAttackSpeed())+" Attacks per second";
		}
		if(itemName.equals("Splinter Bow")){
			itemType = Weapon;
			itemDescription = BowDescription;
			useDescription = "This is a weapon, equip it in the top row of your inventory and press the number on your keyboard corresponding to the inventory slot you put it in to use it.";
			attacksPerSecond = df.format(item.getAttackSpeed())+" Attacks per second";
		}
		if(itemName.equals("Shockwave Staff")){
			itemType = Weapon;
			itemDescription = "Knocks back nearby enemies with a magical shockwave.";
			useDescription = "This is a weapon, equip it in the top row of your inventory and press the number on your keyboard corresponding to the inventory slot you put it in to use it.";
			attacksPerSecond = df.format(item.getAttackSpeed())+" Casts per second";
		}
		if(itemName.equals("Teleport Staff")){
			itemType = Weapon;
			itemDescription = "Magically teleports it's user.";
			useDescription = "This is a weapon, equip it in the top row of your inventory and press the number on your keyboard corresponding to the inventory slot you put it in to use it.";
			attacksPerSecond = df.format(item.getAttackSpeed())+" Casts per second";
		}
		if(itemName.equals("KingCurrency's Bow")){
			itemType = Weapon;
			itemDescription = BowDescription;
			useDescription = "This is a godmode weapon meant for testing game mechanics; it isn't possible to use.";
			attacksPerSecond = df.format(item.getAttackSpeed())+" Attacks per second";
		}
		if(itemName.equals("Sword")){
			itemType = Weapon;
			itemDescription = "Deals damage and stuff.";
			useDescription = "This is a weapon, equip it in the top row of your inventory and press the number on your keyboard corresponding to the inventory slot you put it in to use it.";
			attacksPerSecond = df.format(item.getAttackSpeed())+" Attacks per second";
		}
		if(itemName.equals("Lance")){
			itemType = Weapon;
			itemDescription = "Deals damage and stuff.";
			useDescription = "This is a weapon, equip it in the top row of your inventory and press the number on your keyboard corresponding to the inventory slot you put it in to use it.";
			attacksPerSecond = df.format(item.getAttackSpeed())+" Attacks per second";
		}
		if(itemName.equals("Mario's Sword")){
			itemType = Weapon;
			itemDescription = "Deals damage and stuff.";
			useDescription = "This is a godmode weapon meant for testing game mechanics, it isn't possible to use.";
			attacksPerSecond = df.format(item.getAttackSpeed())+" Attacks per second";
		}
		if(itemName.equals("Dagger")){
			itemType = Weapon;
			itemDescription = "Deals damage and stuff.";
			useDescription = "This is a weapon, equip it in the top row of your inventory and press the number on your keyboard corresponding to the inventory slot you put it in to use it.";
			attacksPerSecond = df.format(item.getAttackSpeed())+" Attacks per second";
		}
		if(itemName.equals("Iron Ring")){
			itemType = "Ring";
			useDescription = "This is a ring, equip it in a ring slot on the right side of your inventory to gain it's benefits.";
		}
		if(itemName.equals("Silver Ring")){
			itemType = "Ring";
			useDescription = "This is a ring, equip it in a ring slot on the right side of your inventory to gain it's benefits.";
		}
		if(itemName.equals("Gold Ring")){
			itemType = "Ring";
			useDescription = "This is a ring, equip it in a ring slot on the right side of your inventory to gain it's benefits.";
		}
		if(itemName.equals("Emerald Ring")){
			itemType = "Ring";
			useDescription = "This is a ring, equip it in a ring slot on the right side of your inventory to gain it's benefits.";
		}
		if(itemName.equals("KingCurrency's Ring")){
			itemType = "Ring";
			useDescription = "This is a godmode ring meant for testing game mechanics, it isn't possible to use.";
		}
		if(itemName.equals("Chain Helmet")){
			itemType = "Helmet";
			useDescription = "This is a helmet, equip it in the helmet slot on the right side of your inventory to gain it's benefits.";
		}
		if(itemName.equals("Chain Vest")){
			itemType = "Chest";
			useDescription = "This is a chest armor, equip it in the chest slot on the right side of your inventory to gain it's benefits.";
		}
		if(itemName.equals("Chain Pants")){
			itemType = "Pants";
			useDescription = "These are pants, equip them in the pants slot on the right side of your inventory to gain it's benefits.";
		}
		if(itemName.equals("Chain Glove")){
			itemType = "Glove";
			useDescription = "This is a glove, equip it in a glove slot on the right side of your inventory to gain it's benefits.";
		}
		if(itemName.equals("Chain Boot")){
			itemType = "Boot";
			useDescription = "This is a boot, equip it in the boot slot on the right side of your inventory to gain it's benefits.";
		}
		//plate armor
		if(itemName.equals("Plate Helmet")){
			itemType = "Helmet";
			useDescription = "This is a helmet, equip it in the helmet slot on the right side of your inventory to gain it's benefits.";
		}
		if(itemName.equals("Plate Vest")){
			itemType = "Chest";
			useDescription = "This is a chest armor, equip it in the chest slot on the right side of your inventory to gain it's benefits.";
		}
		if(itemName.equals("Plate Pants")){
			itemType = "Pants";
			useDescription = "These are pants, equip them in the pants slot on the right side of your inventory to gain it's benefits.";
		}
		if(itemName.equals("Plate Glove")){
			itemType = "Glove";
			useDescription = "This is a glove, equip it in a glove slot on the right side of your inventory to gain it's benefits.";
		}
		if(itemName.equals("Plate Boot")){
			itemType = "Boot";
			useDescription = "This is a boot, equip it in the boot slot on the right side of your inventory to gain it's benefits.";
		}
		//cloth armor
		if(itemName.equals("Cloth Hood")){
			itemType = "Helmet";
			useDescription = "This is a godmode helmet meant for testing game mechanics, it isn't possible to use.";
		}
		if(itemName.equals("Cloth Shirt")){
			itemType = "Chest";
			useDescription = "This is a godmode chest armor meant for testing game mechanics, it isn't possible to use.";
		}
		if(itemName.equals("Cloth Pants")){
			itemType = "Pants";
			useDescription = "These are godmode pants meant for testing game mechanics, it isn't possible to use.";
		}
		if(itemName.equals("Cloth Glove")){
			itemType = "Glove";
			useDescription = "This is a godmode glove meant for testing game mechanics, it isn't possible to use.";
		}
		if(itemName.equals("Cloth Shoe")){
			itemType = "Boot";
			useDescription = "This is a godmode boot meant for testing game mechanics, it isn't possible to use.";
		}
		//kingCurrency's armor
		if(itemName.equals("KingCurrency's Hood")){
			itemType = "Helmet";
			useDescription = "This is a helmet, equip it in the helmet slot on the right side of your inventory to gain it's benefits.";
		}
		if(itemName.equals("KingCurrency's Shirt")){
			itemType = "Chest";
			useDescription = "This is a chest armor, equip it in the chest slot on the right side of your inventory to gain it's benefits.";
		}
		if(itemName.equals("KingCurrency's Pants")){
			itemType = "Pants";
			useDescription = "These are pants, equip them in the pants slot on the right side of your inventory to gain it's benefits.";
		}
		if(itemName.equals("KingCurrency's Glove")){
			itemType = "Glove";
			useDescription = "This is a glove, equip it in a glove slot on the right side of your inventory to gain it's benefits.";
		}
		if(itemName.equals("KingCurrency's Shoe")){
			itemType = "Boot";
			useDescription = "This is a boot, equip it in the boot slot on the right side of your inventory to gain it's benefits.";
		}
		//end of kc's stuff
		if(itemName.equals("Wooden Trunk")){
			itemType = "Storage";
			itemDescription = "Holds up to 20 items.";
			useDescription = "This is a furniture item. Right click it in town and click on the place you would like to set it up to use it.";
		}
		if(itemName.equals("Torch")){
			itemType = "Storage";
			itemDescription = "A weak light.";
			useDescription = "This is a furniture item. Right click it in town and click on the place you would like to set it up to use it.";
		}
		if(itemName.equals("Whetstone")){
			itemType = "Currency";
			itemDescription = "Scraping this on your weapon could increase or decrease it's damage.";
			useDescription = "This is a currency item. Right click it and click on the weapon you wish to apply it to.";
		}
		if(itemName.equals("Greater Whetstone")){
			itemType = "Currency";
			itemDescription = "Scraping this on your weapon will probably increase it's damage but it could also decrease it's damage.";
			useDescription = "This is a currency item. Right click it and click on the weapon you wish to apply it to.";
		}
		if(itemName.equals("Superior Whetstone")){
			itemType = "Currency";
			itemDescription = "Scraping this on your weapon is very likely to increase it's damage but it also has a very small chance to decrease it's damage.";
			useDescription = "This is a currency item. Right click it and click on the weapon you wish to apply it to.";
		}
		if(itemName.equals("Engraving Chisel")){
			itemType = "Currency";
			itemDescription = "Pounding this on a non currency item could add or remove a modifier.";
			useDescription = "This is a currency item. Right click it and click on the weapon you wish to apply it to.";
		}
		if(itemName.equals("Shaping Hammer")){
			itemType = "Currency";
			itemDescription = "Striking a non currency item with this could increase or decrease the numeric value of one of it's modifiers";
			useDescription = "This is a currency item. Right click it and click on the weapon you wish to apply it to.";
		}
	}
	public boolean mouseOverThis(){
		if(AppletUI.mousePos.x>=AppletUI.xoffset+item.xpos-((int)GamePanel.player.xpos)&&AppletUI.mousePos.x<AppletUI.xoffset+item.xpos+width-((int)GamePanel.player.xpos)){
			if(AppletUI.mousePos.y>=AppletUI.yoffset+item.ypos-((int)GamePanel.player.ypos)&&AppletUI.mousePos.y<AppletUI.yoffset+item.ypos+30-((int)GamePanel.player.ypos)){
				return true;
			}
		}
		return false;
	}
	public void Draw(Graphics g, boolean onGround){
		Font font = new Font("Iwona Heavy",Font.BOLD,14);
		g.setFont(font);
		Color currencyColor = new Color(255,208,99);
		Color weaponColor = Color.white;
		if((itemType==Weapon||item.itemType=="Armor")&&item.isGodmodeRestricted==false){
			g.setColor(weaponColor);
		}
		else if(itemType=="Currency"){
			g.setColor(currencyColor);
		}
		else if(item.itemType=="Furniture"){
			g.setColor(Color.pink);
		}
		else{
			g.setColor(Color.red);
		}
		FontMetrics metrics = g.getFontMetrics();
		width = GamePanel.getStringWidth(itemName,font)+40;
		if(onGround){
			//draw background image
			g.drawImage(GamePanel.tooltipBackground, AppletUI.xoffset+item.xpos-1-((int)GamePanel.player.xpos), AppletUI.yoffset+item.ypos-1-((int)GamePanel.player.ypos),metrics.stringWidth(itemName)+43, 33, null);
			//find the highest tier mod this item has
			int highestRarity = 0;
			for(int i = 0; i<item.modifiers.size();i++){
				if(item.modifiers.get(i)!=null){
					if(item.modifiers.get(i).modRarity>highestRarity){
						highestRarity = (int)item.modifiers.get(i).modRarity;
						g.setColor(item.modifiers.get(i).modColor);
					}
				}
			}
			g.drawRect(AppletUI.xoffset+item.xpos-((int)GamePanel.player.xpos), AppletUI.yoffset+item.ypos-((int)GamePanel.player.ypos), metrics.stringWidth(itemName)+40, 30);
			//set color for item name
			if((itemType==Weapon||item.itemType=="Armor")&&item.isGodmodeRestricted==false){
				g.setColor(weaponColor);
			}
			else if(itemType=="Currency"){
				g.setColor(currencyColor);
			}
			else if(item.itemType=="Furniture"){
				g.setColor(Color.pink);
			}
			else{
				g.setColor(Color.red);
			}
			//draw item name
			g.drawString(itemName, AppletUI.xoffset+item.xpos-((int)GamePanel.player.xpos)+20, AppletUI.yoffset+item.ypos-((int)GamePanel.player.ypos)+metrics.getHeight());
			//draw item modifier previews
			int width2 = item.modifiers.size()*3;
			for(int i = 0; i<item.modifiers.size();i++){
				g.drawImage(GamePanel.tooltipModPreviews[(int) (item.modifiers.get(i).modRarity-1)][0],AppletUI.xoffset+item.xpos+(width/2)-width2+(i*6)-((int)GamePanel.player.xpos),AppletUI.yoffset+item.ypos+24-((int)GamePanel.player.ypos),5,5,null);
			}


		}
		else{
			//g.drawImage(GamePanel.tooltipBackground, AppletUI.xoffset+item.xpos-((int)GamePanel.player.xpos), AppletUI.yoffset+item.ypos-((int)GamePanel.player.ypos),metrics.stringWidth(itemName)+40, 30, null);
		}
	}
}
