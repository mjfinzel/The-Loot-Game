package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Light {
	int xpos;
	int ypos;
	//int radius;
	int width;
	int height;
	int minWidth;
	int minHeight;
	int minRadius;
	double angleInDegrees = 0;
	int innerWidth;
	int innerHeight;
	int smoothness;
	int brightness = 200;
	int flickerDirection = 1;//sets whether the light radius is shrinking or growing
	int flickerFrames;
	int framesSinceFlicker = 0;
	int framesPerFlicker;//number of frames between flickers
	int currentFrame = 0;//current frame
	//BufferedImage[] lightImg;
	Color lightColor;
	ArrayList<Point> affectedPixels = new ArrayList<Point>();
	public Light(int x, int y, int w, int h, int FlickerFrames, int flickerRate){
		xpos = x;
		ypos = y;
		minWidth = w;
		width = w;
		minHeight = h;
		height = h;
		innerWidth = (width/10)*8;
		innerHeight = (height/10)*8;
		flickerFrames = FlickerFrames;
		framesPerFlicker = flickerRate;
		//lightImg = new BufferedImage[flickerFrames];
		smoothness=1;
		lightColor = new Color(0,0,0,55);//new Color(102,255,190,200);

		//		if(radius<smoothness){
		//			smoothness=radius;
		//		}

	}

	public void render(){

		//		else {//if lightImg[currentFrame] is not null
		//System.out.println("drawing light!");
		Graphics2D tempGraphics = GamePanel.lighting.createGraphics();
		tempGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		AffineTransform at = new AffineTransform();
		
		
		//draw the light onto the lighting image where it belongs
		
		tempGraphics.drawImage(GamePanel.basicLightImg, AppletUI.xoffset+xpos-(width/2)-((int)GamePanel.player.xpos), AppletUI.yoffset+ypos-(height/2)-((int)GamePanel.player.ypos), width, height, null);

		//}

		//		}
		framesSinceFlicker++;
		if(framesSinceFlicker>=framesPerFlicker&&flickerFrames>0){
			currentFrame+=flickerDirection;
			width+=(flickerDirection*2);
			height+=(flickerDirection*2);
			framesSinceFlicker = 0;
		}
		if(currentFrame>=flickerFrames||currentFrame<0){
			if(flickerDirection==1){
				flickerDirection = -1;
			}
			else{
				flickerDirection = 1;
			}
			currentFrame+=flickerDirection;
		}
		//System.out.println("currentFrame: "+currentFrame);
	}
}
