package Game;

import java.awt.Color;
import java.awt.Point;
import java.text.DecimalFormat;

public class ItemModifier {
	String name = "";
	int health = 0;
	double lifeOnHit = 0;
	int pierce = 0;
	double swingWidth = 0;
	int projectiles = 0;
	int damage = 0;
	double percentDamage = 0;
	int projectileRange = 0;
	int lifeRegen = 0;
	int effectDuration = 0;
	double moveSpeedOnDaggerKill = 0;
	double armorCoverageOnSwordHit = 0;
	StatusEffect effect;
	double critChance = 0;
	double critDamage = 0;
	double globalDamage;
	double knockback = 0;
	double itemRarity = 0;
	double projectileSpeed=0;
	double attackSpeed = 0;
	double modRarity = 0;
	boolean isImplicit = false;
	Color goodColor = new Color(48,160,0);
	Color badColor = new Color(198,85,85);
	Color modColor = goodColor;
	double moveSpeed = 0;
	Point modRange = new Point(0,0);
	String description = "ERROR: This modifier does nothing.";
	public ItemModifier(String Name, double rarity){
		name = Name;
		modRarity = rarity;
		if(rarity==1){//very common
			modColor = new Color(211,190,154);
		}
		else if(rarity==2){//common
			modColor = new Color(133,158,123);
		}
		else if(rarity==3){//uncommon
			modColor = goodColor;
		}
		else if(rarity==4){//rare
			modColor = new Color(88,157,206);
		}
		else if(rarity==5){//very rare
			modColor = new Color(151,80,124);
		}
		else if(rarity==6){//super rare
			modColor = new Color(239,128,55);//new Color(198,196,69);
		}
		updateModifier();
	}
	public void updateModifier(){
		DecimalFormat df = new DecimalFormat("#.00");
		if(name=="Health"){
			modRange = new Point((int)(modRarity*4)-3,(int)(modRarity*4));
			health = GamePanel.randomNumber(modRange.x, modRange.y);
			description = "+"+health+" To maximum health";
		}
		if(name=="Damage"){
			modRange = new Point((int)(modRarity),(int)(modRarity));
			damage = GamePanel.randomNumber(modRange.x, modRange.y);
			description = "+"+damage+" Weapon damage";
		}
		if(name=="Percent Damage"){
			modRange = new Point((int)(modRarity*15)-14,(int)(modRarity)*15);
			percentDamage = (double)GamePanel.randomNumber(modRange.x, modRange.y)/100.0;
			description = (int)(percentDamage*100)+"% Increased weapon damage";
		}
		if(name=="Percent Damage And Damage"){
			modRange = new Point((int)(modRarity*5)-4,(int)(modRarity)*5);
			percentDamage = (double)GamePanel.randomNumber(modRange.x, modRange.y)/100.0;
			damage = (int)((double)(percentDamage*100.0)/5.0);
			description = (int)(percentDamage*100)+"% Increased weapon damage and +"+damage+" damage.";
		}
		if(name=="Effect on kill"){
			//modRange = new Point((int)(modRarity*4)-3,(int)(modRarity*4));
			health = GamePanel.randomNumber(modRange.x, modRange.y);
			description = "+"+health+" To maximum health";
		}
		if(name=="KC_Rarity"){
			itemRarity = 1.0;
			description = (int)(itemRarity*100.0)+"% Increased rarity of modifiers found on items.";
		}
		if(name=="Movement Speed Cloth"){
			moveSpeed = .06;
			description = df.format(moveSpeed*100)+"% Increased movement speed";
		}
		if(name=="MovementSpeedOnDaggerKill"){
			effectDuration = 4;
			effect=new StatusEffect("MoveSpeedOnDaggerKill",effectDuration,null,null);
			modRange = new Point((int)(modRarity*5)-4,(int)(modRarity*5));
			moveSpeedOnDaggerKill = ((double)((double)GamePanel.randomNumber(modRange.x, modRange.y)/100.0));
			description = df.format((moveSpeedOnDaggerKill*100))+"% Increased movement speed for "+effectDuration+" seconds on dagger kill";
		}
		if(name=="ArmorCoverageOnSwordHit"){
			effectDuration = 4;
			effect=new StatusEffect("ArmorCoverageOnSwordHit",effectDuration,null,null);
			modRange = new Point((int)(modRarity*3)-2,(int)(modRarity*3));
			armorCoverageOnSwordHit = (double)((double)GamePanel.randomNumber(modRange.x, modRange.y)/100.0);
			description = "3% Chance to gain "+df.format(armorCoverageOnSwordHit*100)+"% more armor coverage for "+effectDuration+" seconds on sword hit.";
		}
		if(name=="Life on hit"){
			modRange = new Point(1,(int)(modRarity));
			lifeOnHit = (double)GamePanel.randomNumber(modRange.x, modRange.y)/2.0;
			description = "+"+df.format(lifeOnHit)+" Health gained on hit";
		}
		if(name=="Life Regeneration"){
			modRange = new Point((int)(modRarity*10)-9,(int)(modRarity*10));
			lifeRegen = GamePanel.randomNumber(modRange.x, modRange.y);
			description = "Grants an additional "+lifeRegen+" life regeneration per minute";
		}
		if(name=="Knockback"){
			modRange = new Point((int)(modRarity*6)-5,(int)(modRarity*6));
			knockback = (double)((double)GamePanel.randomNumber(modRange.x, modRange.y)/100.0);
			description = df.format(knockback*100)+"% Increased knockback";
		}
		if(name=="Projectile Speed"){
			modRange = new Point((int)(modRarity*6)-5,(int)(modRarity*6));
			projectileSpeed = (double)((double)GamePanel.randomNumber(modRange.x, modRange.y)/100.0);
			description = df.format(projectileSpeed*100)+"% Increased projectile speed";
		}
		if(name=="Swing width"){
			modRange = new Point((int)(modRarity*4)-3,(int)(modRarity*4));
			swingWidth = (double)((double)GamePanel.randomNumber(modRange.x, modRange.y)/100.0);
			description = df.format(swingWidth*100)+"% Increased swing width";
		}
		if(name=="Crit chance"){
			modRange = new Point((int)(modRarity*20)-19,(int)(modRarity*20));
			critChance = (double)((double)GamePanel.randomNumber(modRange.x, modRange.y)/100.0);
			description = df.format(critChance*100)+"% Increased critical strike chance";
		}
		if(name=="Crit damage"){
			modRange = new Point((int)(modRarity*5)-4,(int)(modRarity*5));
			critDamage = (double)((double)GamePanel.randomNumber(modRange.x, modRange.y)/100.0);
			description = df.format(critDamage*100)+"% Increased critical strike damage";
		}
		if(name=="Global damage"){
			modRange = new Point(1,(int)(modRarity));
			globalDamage = (double)((double)GamePanel.randomNumber(modRange.x, modRange.y)/100.0);
			description = df.format(globalDamage*100)+"% More global damage";
		}
		if(name=="Pierce"){
			modRange = new Point(1,(int)(modRarity));
			pierce = GamePanel.randomNumber(modRange.x, modRange.y);
			if(pierce<=1)
				description = "Arrows pierce "+pierce+" additional time";
			else
				description = "Arrows pierce "+pierce+" additional times";
		}
		if(name=="Projectile Range"){
			modRange = new Point((int)(modRarity*50)-49,(int)(modRarity*50));
			projectileRange = GamePanel.randomNumber(modRange.x, modRange.y);
			description = "+"+projectileRange+" Projectile range";
		}
		if(name=="Life on hit 1"){
			lifeOnHit = GamePanel.randomNumber(1, 2);
			description = "+"+lifeOnHit+" health gained on hit";
			modColor = goodColor;
		}
		if(name=="Life on hit kc"){
			lifeOnHit = 3;
			description = "+"+lifeOnHit+" health gained on hit";
			modColor = goodColor;
		}
		if(name=="Movement Speed"){
			modRange = new Point((int)(modRarity*10)-9,(int)(modRarity*10));
			moveSpeed = (double)((double)GamePanel.randomNumber(modRange.x, modRange.y)/100.0);
			if(moveSpeed>=0){
				description = df.format((moveSpeed*100))+"% Increased movement speed";
			}

		}
		if(name=="Attack Speed"){
			modRange = new Point((int)(modRarity*3)-2,(int)(modRarity*3));
			attackSpeed = (double)((double)GamePanel.randomNumber(modRange.x, modRange.y)/100.0);
			if(attackSpeed>=0){
				description = df.format(attackSpeed*100)+"% Increased attack speed";
			}

		}
		if(name=="Movement Speed Plate"){
			isImplicit = true;
			moveSpeed = -.06;
			modColor = Color.white;
			description = moveSpeed*-100+"% Reduced movement speed";
		}
		if(name=="Movement Speed KC"){
			isImplicit = true;
			moveSpeed = .1;
			modColor = Color.white;
			description = moveSpeed*100+"% Increased movement speed";
		}
	}
}
