package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class InventorySlot {
	int xpos;
	int ypos;
	Item item;
	Point pos = new Point(0,0);
	public InventorySlot(int x, int y){
		xpos = x;
		ypos = y;
	}
	public boolean mouseOverThis(){
		if(AppletUI.mousePos.x>=pos.x+this.xpos&&AppletUI.mousePos.x<pos.x+this.xpos+50){
			if(AppletUI.mousePos.y>=pos.y+this.ypos&&AppletUI.mousePos.y<pos.y+this.ypos+50){
				return true;
			}
		}
		return false;
	}
	public void DrawTooltip(Graphics g){
		if(item!=null){
			Font font = new Font("Iwona Heavy",Font.BOLD,14);
			g.setFont(font);
			g.setColor(Color.white);
			FontMetrics metrics = g.getFontMetrics();
			if(item.maxStackSize>1)
				g.drawString(item.stackSize+"", pos.x+xpos+6, pos.y+ypos+2+metrics.getHeight());
			if(mouseOverThis()){
				item.tooltip.Update();
				int tooltipWidth = 300;
				for(int i = 0; i<item.modifiers.size();i++){
					int lineWidth = GamePanel.getStringWidth(20+item.modifiers.get(i).description, new Font("Iwona Heavy",Font.PLAIN,12));
					if(tooltipWidth<lineWidth){
						tooltipWidth = lineWidth;
					}
				}
				//create list of lines to draw for description
				font = new Font("Iwona Heavy",Font.PLAIN,12);
				g.setFont(font);
				ArrayList<String> itemDescription = GamePanel.cutString(item.tooltip.itemDescription, tooltipWidth, font);
				font = new Font("Iwona Heavy",Font.ITALIC,12);
				g.setFont(font);
				ArrayList<String> useDescription = GamePanel.cutString(item.tooltip.useDescription, tooltipWidth, font);
				int implicitHeight = 0;
				if(item.implicit!=null){
					implicitHeight = metrics.getHeight();
				}
				int tooltipHeight=0;
				if(item.tooltip.itemType=="Currency"){
					tooltipHeight = 45+implicitHeight+(itemDescription.size()*metrics.getHeight()+(useDescription.size()*metrics.getHeight()));
				}
				else if(item.tooltip.itemType=="Storage"){
					tooltipHeight = 45+implicitHeight+(itemDescription.size()*metrics.getHeight()+(useDescription.size()*metrics.getHeight()));
				}
				else if(item.tooltip.itemType=="Weapon"){
					tooltipHeight = 45+implicitHeight+(useDescription.size()*metrics.getHeight())+(3*metrics.getHeight())+((item.modifiers.size())*metrics.getHeight());
				}
				else if(item.itemType=="Armor"&&item.tooltip.itemType!="Ring"){
					tooltipHeight = 45+implicitHeight+(useDescription.size()*metrics.getHeight())+((2+item.modifiers.size())*metrics.getHeight());
				}
				else if(item.tooltip.itemType=="Ring"){
					tooltipHeight = 45+implicitHeight+(useDescription.size()*metrics.getHeight())+((item.modifiers.size())*metrics.getHeight());
				}
				GamePanel.cutString(item.tooltip.useDescription,tooltipWidth,font);
				Color currencyColor = new Color(255,208,99);
				Color weaponColor = Color.white;
				if((item.tooltip.itemType==item.tooltip.Weapon||item.itemType=="Armor")&&item.isGodmodeRestricted==false){
					g.setColor(weaponColor);
				}
				else if(item.tooltip.itemType=="Currency"){
					g.setColor(currencyColor);
				}
				else if(item.itemType=="Furniture"){
					g.setColor(Color.pink);
				}
				else{
					g.setColor(Color.red);
				}
				//draw background image
				g.drawImage(GamePanel.tooltipBackground, pos.x+xpos+25-(tooltipWidth/2), pos.y+ypos-(tooltipHeight),tooltipWidth, tooltipHeight, null);
				g.drawRect(pos.x+xpos+25-(tooltipWidth/2), pos.y+ypos-(tooltipHeight+1), tooltipWidth, 30);
				g.drawRect(pos.x+xpos+25-(tooltipWidth/2), pos.y+ypos-(tooltipHeight+1), tooltipWidth, tooltipHeight);

				font = new Font("Iwona Heavy",Font.BOLD,14);
				g.setFont(font);
				//draw item name
				g.drawString(item.name, pos.x+xpos+25-((metrics.stringWidth(item.name)/2)), pos.y+ypos-tooltipHeight-1+metrics.getHeight());
				//if the item is a currency item
				if(item.itemType=="Currency"){
					g.setColor(Color.CYAN);
					font = new Font("Iwona Heavy",Font.PLAIN,12);
					g.setFont(font);
					//draw item description
					for(int i = 0; i<itemDescription.size();i++){
						g.drawString(itemDescription.get(i),pos.x+xpos+25-((GamePanel.getStringWidth(itemDescription.get(i), font)/2)),pos.y+ypos+45+(i*metrics.getHeight())-tooltipHeight);
					}
					g.setColor(Color.gray);
					font = new Font("Iwona Heavy",Font.ITALIC,12);
					g.setFont(font);
					//draw use description
					for(int i = 0; i<useDescription.size();i++){
						GamePanel.getStringWidth(useDescription.get(i), font);
						g.drawString(useDescription.get(i),pos.x+xpos+25-((GamePanel.getStringWidth(useDescription.get(i), font)/2)),pos.y+ypos+50+(itemDescription.size()*metrics.getHeight())+(i*metrics.getHeight())-tooltipHeight);
					}
				}
				//if the item is a furniture item
				else if(item.itemType=="Furniture"){
					g.setColor(Color.CYAN);
					font = new Font("Iwona Heavy",Font.PLAIN,12);
					g.setFont(font);
					//draw item description
					for(int i = 0; i<itemDescription.size();i++){
						g.drawString(itemDescription.get(i),pos.x+xpos+25-((GamePanel.getStringWidth(itemDescription.get(i), font)/2)),pos.y+ypos+45+(i*metrics.getHeight())-tooltipHeight);
					}
					g.setColor(Color.gray);
					font = new Font("Iwona Heavy",Font.ITALIC,12);
					g.setFont(font);
					//draw use description
					for(int i = 0; i<useDescription.size();i++){
						GamePanel.getStringWidth(useDescription.get(i), font);
						g.drawString(useDescription.get(i),pos.x+xpos+25-((GamePanel.getStringWidth(useDescription.get(i), font)/2)),pos.y+ypos+50+(itemDescription.size()*metrics.getHeight())+(i*metrics.getHeight())-tooltipHeight);
					}
				}
				else if(item.itemType=="MagicWeapon"){
					g.setColor(Color.CYAN);
					font = new Font("Iwona Heavy",Font.PLAIN,12);
					g.setFont(font);
					//draw item description
					for(int i = 0; i<itemDescription.size();i++){
						g.drawString(itemDescription.get(i),pos.x+xpos+25-((GamePanel.getStringWidth(itemDescription.get(i), font)/2)),pos.y+ypos+45+(i*metrics.getHeight())-tooltipHeight);
					}
					g.setColor(Color.gray);
					font = new Font("Iwona Heavy",Font.ITALIC,12);
					g.setFont(font);
					//draw use description
					for(int i = 0; i<useDescription.size();i++){
						GamePanel.getStringWidth(useDescription.get(i), font);
						g.drawString(useDescription.get(i),pos.x+xpos+25-((GamePanel.getStringWidth(useDescription.get(i), font)/2)),pos.y+ypos+50+(itemDescription.size()*metrics.getHeight())+(i*metrics.getHeight())-tooltipHeight);
					}
				}
				else if(item.itemType=="Armor"&&item.tooltip.itemType!="Ring"){
					//draw base stats
					g.setColor(Color.white);
					font = new Font("Iwona Heavy",Font.PLAIN,12);
					g.setFont(font);
					g.drawString(item.tooltip.armorCoverage, pos.x+xpos+25-((GamePanel.getStringWidth(item.tooltip.armorCoverage, font)/2)),pos.y+ypos+45+(0*metrics.getHeight())-tooltipHeight);
					g.drawString(item.tooltip.damageReduction, pos.x+xpos+25-((GamePanel.getStringWidth(item.tooltip.damageReduction, font)/2)),pos.y+ypos+45+(1*metrics.getHeight())-tooltipHeight);
					//draw implicit
					if(item.implicit!=null){
						g.drawString(item.implicit.description,pos.x+xpos+25-((GamePanel.getStringWidth(item.implicit.description, font)/2)), pos.y+ypos+45+(2*metrics.getHeight())-tooltipHeight);
					}
					//draw modifiers
					for(int i = 0; i<item.modifiers.size();i++){
						if(item.modifiers.get(i)!=null){
							g.setColor(item.modifiers.get(i).modColor);
							font = new Font("Iwona Heavy",Font.PLAIN,12);
							g.setFont(font);
							g.drawString(item.modifiers.get(i).description, pos.x+xpos+25-((GamePanel.getStringWidth(item.modifiers.get(i).description, font)/2)), pos.y+ypos+45+implicitHeight+(2*metrics.getHeight())+(i*metrics.getHeight())-tooltipHeight);
						}
					}
					//draw use description
					g.setColor(Color.gray);
					font = new Font("Iwona Heavy",Font.ITALIC,12);
					g.setFont(font);
					for(int i = 0; i<useDescription.size();i++){
						GamePanel.getStringWidth(useDescription.get(i), font);
						g.drawString(useDescription.get(i),pos.x+xpos+25-((GamePanel.getStringWidth(useDescription.get(i), font)/2)),pos.y+ypos+50+implicitHeight+(2*metrics.getHeight())+(i*metrics.getHeight())+(item.modifiers.size()*metrics.getHeight())-tooltipHeight);
					}
				}
				else if(item.itemType=="Armor"&&item.tooltip.itemType=="Ring"){
					//draw base stats
					g.setColor(Color.white);
					font = new Font("Iwona Heavy",Font.PLAIN,12);
					g.setFont(font);
					
					if(item.implicit!=null){
						g.drawString(item.implicit.description,pos.x+xpos+25-((GamePanel.getStringWidth(item.implicit.description, font)/2)), pos.y+ypos+45-tooltipHeight);
					}
					
					//draw modifiers
					for(int i = 0; i<item.modifiers.size();i++){
						if(item.modifiers.get(i)!=null){
							g.setColor(item.modifiers.get(i).modColor);
							font = new Font("Iwona Heavy",Font.PLAIN,12);
							g.setFont(font);
							g.drawString(item.modifiers.get(i).description, pos.x+xpos+25-((GamePanel.getStringWidth(item.modifiers.get(i).description, font)/2)), pos.y+ypos+45+implicitHeight+(i*metrics.getHeight())-tooltipHeight);
						}
					}
					//draw use description
					g.setColor(Color.gray);
					font = new Font("Iwona Heavy",Font.ITALIC,12);
					g.setFont(font);
					for(int i = 0; i<useDescription.size();i++){
						GamePanel.getStringWidth(useDescription.get(i), font);
						g.drawString(useDescription.get(i),pos.x+xpos+25-((GamePanel.getStringWidth(useDescription.get(i), font)/2)),pos.y+ypos+50+implicitHeight+(i*metrics.getHeight())+(item.modifiers.size()*metrics.getHeight())-tooltipHeight);
					}
				}
				else {//item is a weapon
					g.setColor(Color.white);
					font = new Font("Iwona Heavy",Font.PLAIN,12);
					g.setFont(font);
					if(item.damageModifier<0){
						g.setColor(new Color(198,85,85));
					}
					else if(item.damageModifier>0){
						g.setColor(new Color(208,116,219));
					}
					//draw weapon damage
					g.drawString(item.tooltip.damage, pos.x+xpos+25-((GamePanel.getStringWidth(item.tooltip.damage, font)/2)),pos.y+ypos+45+(0*metrics.getHeight())-tooltipHeight);
					g.setColor(Color.white);
					//draw weapon attacks per second
					g.drawString(item.tooltip.attacksPerSecond, pos.x+xpos+25-((GamePanel.getStringWidth(item.tooltip.attacksPerSecond, font)/2)),pos.y+ypos+45+(1*metrics.getHeight())-tooltipHeight);
					//draw weapon crit chance
					g.drawString(item.tooltip.critChance, pos.x+xpos+25-((GamePanel.getStringWidth(item.tooltip.critChance, font)/2)), pos.y+ypos+45+(2*metrics.getHeight())-tooltipHeight);
					//draw modifiers
					for(int i = 0; i<item.modifiers.size();i++){
						if(item.modifiers.get(i)!=null){
							g.setColor(item.modifiers.get(i).modColor);
							font = new Font("Iwona Heavy",Font.PLAIN,12);
							g.setFont(font);
							g.drawString(item.modifiers.get(i).description, pos.x+xpos+25-((GamePanel.getStringWidth(item.modifiers.get(i).description, font)/2)), pos.y+ypos+45+(3*metrics.getHeight())+(i*metrics.getHeight())-tooltipHeight);
						}
					}
					//description color
					g.setColor(Color.gray);
					font = new Font("Iwona Heavy",Font.ITALIC,12);
					g.setFont(font);
					//draw use description
					for(int i = 0; i<useDescription.size();i++){
						GamePanel.getStringWidth(useDescription.get(i), font);
						g.drawString(useDescription.get(i),pos.x+xpos+25-((GamePanel.getStringWidth(useDescription.get(i), font)/2)),pos.y+ypos+50+(item.modifiers.size()*metrics.getHeight())+(3*metrics.getHeight())+(i*metrics.getHeight())-tooltipHeight);
					}
				}
			}
		}
	}
	public void Draw(Graphics g){
		pos.x=(AppletUI.windowWidth/2)-362;
		pos.y=(AppletUI.windowHeight/2)-131;
		g.drawImage(GamePanel.inventorySlot, pos.x+xpos, pos.y+ypos,50, 50, null);
		if(item!=null){
			g.drawImage(item.artwork, pos.x+xpos, pos.y+ypos,50, 50, null);
		}

	}
	public void Draw(Graphics g, int x, int y){
		pos.x=(AppletUI.windowWidth/2)-362;
		pos.y=(AppletUI.windowHeight/2)-131;
		g.drawImage(GamePanel.inventorySlot, x, y,50, 50, null);
		if(item!=null){
			g.drawImage(item.artwork, x, y,50, 50, null);
		}

	}
}
