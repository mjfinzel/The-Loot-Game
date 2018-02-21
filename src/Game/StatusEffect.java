package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class StatusEffect {
	double duration = 10;//duration in seconds
	int stackWithLongestDuration = 0;
	//int updates = 0;
	ArrayList<Integer> updates = new ArrayList<Integer>();
	int xpos=0;
	int ypos=10;
	int stacks = 1;
	int maxStacks = 10;
	double lifeRegen = 0;
	double movementSpeed;
	double moreArmorCoverage;
	long startTime;
	String description = "ERROR: No Description specified.";
	BufferedImage artwork;
	String name;
	Monster monster;
	Item item;
	public StatusEffect(String Name, int Duration, Monster owner, Item itemCause){
		name = Name;
		item = itemCause;
		monster = owner;
		duration = Duration;
		startTime = System.nanoTime();
		updates.add(0);
		if(name=="MoveSpeedOnDaggerKill"){
			description = "Increased movement speed.";
			artwork = GamePanel.statusEffects[0][0];
		}
		if(name=="Webbed"){
			description = "Reduced movement speed.";
			artwork = GamePanel.statusEffects[0][1];
		}
		if(name=="Poisoned"){
			description = "Health slowly degenerates.";
			artwork = GamePanel.statusEffects[1][1];
			maxStacks = 5;
		}
		if(name=="ArmorCoverageOnSwordHit"){
			description = "Increased armor coverage.";
			artwork = GamePanel.statusEffects[1][0];
		}
		if(name=="On Fire"){
			description = "On fire!";
			artwork = GamePanel.statusEffects[2][1];
		}
	}
	public void update(){
		if(name=="Webbed"){
			movementSpeed = -.2*stacks;
		}
		else if(name=="Poisoned"){
			lifeRegen = -.5*stacks;
		}
		else if(name=="On Fire"){
			double dmg = item.getDamage();
			if(dmg<1){
				dmg = 1;
			}
			lifeRegen = -(dmg/(60*duration))*stacks;
		}
		int pos = 0;
		for(int i = 0; i<GamePanel.player.buffs.size();i++){
			if(GamePanel.player.buffs.get(i)==this){
				pos = i;
			}
		}
		xpos = 20+(60*pos);
		ypos = 0;
		for(int i = 0; i<updates.size();i++){
			if(updates.get(i)>updates.get(stackWithLongestDuration)){
				stackWithLongestDuration=i;
			}
			updates.set(i,updates.get(i)+1);
			if(updates.get(i)>duration*60){
				if(stacks==1){
					if(monster==null){
					GamePanel.player.buffs.remove(this);
					}
					else{
						monster.buffs.remove(this);
					}
					
//
//					for(int j = 0; j<GamePanel.player.inventory.rings.length;j++){
//						if(GamePanel.player.inventory.rings[j].item!=null){
//							for(int k = 0; k<GamePanel.player.inventory.rings[j].item.modifiers.size();k++){
//								if(GamePanel.player.inventory.rings[j].item.modifiers.get(k).effect!=null){
//									if(GamePanel.player.inventory.rings[j].item.modifiers.get(k).effect==this){
//										GamePanel.player.inventory.rings[j].item.modifiers.get(k).effect=null;
//									}
//								}
//							}
//						}
//					}
				}
				else{
					stacks--;
				}
				//GamePanel.player.buffs.remove(this);
			}
		}
	}
	public void Draw(Graphics g){
		update();
		if(updates.size()>0){
			g.drawImage(artwork, xpos, ypos, 50, 50, null);
			Font font = new Font("Iwona Heavy",Font.BOLD,12);
			g.setFont(font);
			g.setColor(Color.white);
			g.drawString(stacks+"", xpos+2, ypos+20);
			g.drawString((int)(duration-(updates.get(stackWithLongestDuration)/60.0))+"", xpos+2, ypos+40);
		}
	}
}
