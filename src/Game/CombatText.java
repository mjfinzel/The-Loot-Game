package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class CombatText {
	String text;
	int xpos;
	int ypos;
	int fontSize = 0;
	int duration = 1500;//1500
	int growthRate = 1;
	int growthLimit = 22;
	int updates = 0;
	Color hitMonster = new Color(255,141,66);
	long appearTime;
	Color color;
	public CombatText(int x, int y, String msg, Color clr){
		text = msg;
		color = clr;
		xpos = x+GamePanel.randomNumber(-30, 30);
		ypos = y-GamePanel.randomNumber(20, 60);
		appearTime = System.currentTimeMillis();
	}
	public void Draw(Graphics g){
		long timePassed = System.currentTimeMillis()-appearTime;
		if(updates%growthRate==0){
			if(fontSize<growthLimit){
				fontSize++;
			}
			else if(timePassed>duration){
				GamePanel.combatText.remove(this);
			}
		}
		Font font = new Font("Iwona Heavy",Font.BOLD,fontSize);
		g.setFont(font);
		g.setColor(color);
		FontMetrics metrics = g.getFontMetrics();
		//center the text on the position specified
		int x = AppletUI.xoffset+xpos-((int)GamePanel.player.xpos)+(metrics.stringWidth(text)/2);
		int y = AppletUI.yoffset+ypos-((int)GamePanel.player.ypos)+metrics.getHeight()/2;
		g.drawString(text, x, y);
		updates++;
	}
}
