package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Rectangle;

public class ClickableName {
	int xpos;
	int ypos;
	String name;
	Rectangle box;
	public ClickableName(String Name, int x, int y){
		xpos = x;
		ypos = y;
		name = Name;
	}
	public boolean mouseOverThis(){
		if(box!=null){
			if(AppletUI.mousePos.x>=AppletUI.xoffset+box.x-((int)GamePanel.player.xpos)&&AppletUI.mousePos.x<AppletUI.xoffset+box.x+box.width-((int)GamePanel.player.xpos)){
				if(AppletUI.mousePos.y>=AppletUI.yoffset+box.y-((int)GamePanel.player.ypos)&&AppletUI.mousePos.y<AppletUI.yoffset+box.y+box.height-((int)GamePanel.player.ypos)){
					return true;
				}
			}
		}
		return false;
	}
	public void Draw(Graphics g){
		Font font = new Font("Iwona Heavy",Font.BOLD,14);
		g.setFont(font);
		g.setColor(Color.pink);
		if(mouseOverThis()){
			g.setColor(Color.white);
		}
		FontMetrics metrics = g.getFontMetrics();
		int width = 20+GamePanel.getStringWidth(name, font);
		int height = 10+metrics.getHeight();
		box = new Rectangle(xpos,ypos,width,height);
		g.drawImage(GamePanel.tooltipBackground, AppletUI.xoffset+box.x-((int)GamePanel.player.xpos), AppletUI.yoffset+box.y-((int)GamePanel.player.ypos),box.width, box.height, null);
		g.drawString(name, AppletUI.xoffset+(xpos+10)-((int)GamePanel.player.xpos), AppletUI.yoffset+(box.y+20)-((int)GamePanel.player.ypos));

	}

}
