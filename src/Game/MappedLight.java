package Game;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MappedLight {
	double xpos;
	double ypos;
	int radius;
	double angleInDegrees;
	double angleInRadians;
	BufferedImage lightmap;
	ArrayList<Light> lights = new ArrayList<Light>();
	ArrayList<Point> lightPositions = new ArrayList<Point>();
	public MappedLight(double x,double y, int rad, BufferedImage map){
		xpos = x;
		ypos = y;
		radius = rad;
		lightmap = map;
		buildMap();
	}
	public void buildMap(){//build a list of all the lights to create from the map
		for(int i = 0; i<lightmap.getHeight();i++){
			for(int j = 0; j<lightmap.getWidth();j++){
				Color currentColor = new Color(lightmap.getRGB(i, j));
				if(currentColor.getRed()!=0||currentColor.getGreen()!=0||currentColor.getBlue()!=0){
					lights.add(new Light((int)xpos+i,(int)ypos+j,radius,radius,0,0));
					lightPositions.add(new Point(i,j));
				}
			}
		}
	}
	public void setAngle(double angle){//set the angle of the lights relative to the center of the map
		//loop through all the lights
		for(int i = 0; i<lightPositions.size();i++){
			//Point handlePos=new Point(AppletUI.xoffset+(int)(Math.cos(Math.toRadians(currentDrawAngle+swingAngle))*20),AppletUI.yoffset+(int)(Math.sin(Math.toRadians(currentDrawAngle+swingAngle))*20));
			//double distanceToPlayer = Math.sqrt(Math.pow(((GamePanel.player.xpos)-this.xpos),2)+Math.pow(((GamePanel.player.ypos)-this.ypos),2));
			double distanceToCenter = Math.sqrt(Math.pow(((lightPositions.get(i).x)-(lightmap.getWidth()/2)),2)+Math.pow(((lightPositions.get(i).y)-(lightmap.getHeight()/2)),2));
			//projectileAngle = Math.toDegrees(Math.atan2((GamePanel.player.ypos-ypos),((GamePanel.player.xpos-xpos))));
			double initialAngle = Math.toDegrees(Math.atan2((lightPositions.get(i).y-(lightmap.getHeight()/2)),((lightPositions.get(i).x-(lightmap.getWidth()/2)))));
			//System.out.println("initial angle: "+initialAngle+", angle: "+angle);
			double newAngle = angle+initialAngle;
			while(newAngle<0){
				newAngle+=360;
			}
			while(newAngle>=360){
				newAngle-=360;
			}
			//new angle is initial angle + spider angle
			
			//System.out.println("new angle: "+(int)newAngle+", distance to center = "+distanceToCenter);
			lights.get(i).xpos=(int) (xpos+(lightmap.getWidth()/2)+(double)(Math.cos(Math.toRadians(newAngle))*distanceToCenter));
			lights.get(i).ypos=(int) (ypos+(lightmap.getHeight()/2)+(double)(Math.sin(Math.toRadians(newAngle))*distanceToCenter));
			//System.out.println("set light xpos to "+lights.get(i).xpos);
			//System.out.println("set light ypos to "+lights.get(i).ypos);
			//lights.get(i).xpos=(int) xpos;
			//lights.get(i).ypos=(int) ypos;
		}
	}
	public void render(){//render the lighting to the darkness image drawn in the game panel
		for(int i = 0; i<lights.size();i++){
			lights.get(i).render();
			//System.out.println("rendered, size = "+lights.size());
		}
	}
}
