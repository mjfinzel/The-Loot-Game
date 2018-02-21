package Game;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Animation{
	public BufferedImage[][]spriteSheet;

	private int frameCount=0;
	int xpos =0;
	int ypos =0;
	int initialX;
	int initialY;
	double xScale = 1;
	double yScale = 1;
	int xRotation;
	int yRotation;
	double angle = 0;
	int delay = 0;
	int index = 0;
	private int currentFrame = 0;
	int cutWidth = 0;
	long timeForNextFrame=0;
	long LastFrameTime = 0;
	boolean additive = false;
	boolean loopAnim = false;
	public Animation(BufferedImage[][] sprites, int numFrames, int frameDelay, int spriteIndex, int x, int y, boolean loop, int rotationX, int rotationY){
		xRotation = rotationX;
		yRotation = rotationY;

		spriteSheet=sprites;
		index = spriteIndex;
		frameCount=numFrames;
		delay=frameDelay;
		xpos=x;
		ypos=y;
		initialX = x;
		initialY = y;
		loopAnim=loop;
		//LastFrameTime=System.currentTimeMillis();
	}

	public void update(){

		if(this.timeForNextFrame <= System.currentTimeMillis()){
			if(this.currentFrame<this.getFrameCount()-1){
				this.currentFrame++;
			}
			if(this.currentFrame>=this.frameCount){
				if(loopAnim){
					this.currentFrame=0;
				}
				else{
					//this.currentFrame=this.frameCount-1;
				}
			}
			//LastFrameTime = System.currentTimeMillis();
			timeForNextFrame = LastFrameTime + delay;
		}
	}
	public int getCurrentFrame(){
		return this.currentFrame;
	}
	public void setAngle(double angl){
		this.angle = angl;
	}
	public void updatePosition(int x, int y){
		this.xpos=x;
		this.ypos=y;
	}
	public int getFrameCount(){
		return this.frameCount;
	}
	public void Draw(Graphics g){

		this.update();
		//System.out.println("FrameCount="+this.frameCount);

		//if(this.LastFrameTime+delay >= System.currentTimeMillis()){
		if(true){//this.currentFrame<=this.frameCount){
			if(!additive){
				if(angle==0){
					g.drawImage(spriteSheet[currentFrame][index], GamePanel.cameraX+xpos, GamePanel.cameraY+ypos, (int)(spriteSheet[currentFrame][0].getWidth()*xScale*GamePanel.zoom), (int)(spriteSheet[currentFrame][0].getHeight()*yScale*GamePanel.zoom), null);
				}
				else{
					Graphics2D g2d = (Graphics2D) g;
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					AffineTransform at = new AffineTransform();
					at.translate(xpos*GamePanel.zoom, ypos*GamePanel.zoom);
					at.scale(xScale*GamePanel.zoom, yScale*GamePanel.zoom);
					at.rotate(angle,xRotation,yRotation);
					
					g2d.drawImage(spriteSheet[currentFrame][index], at, null);
				}
			}
			else{
				for(int i = currentFrame; i>0;i--){
					g.drawImage(spriteSheet[i][index], xpos+(int)(i*(spriteSheet[currentFrame][0].getWidth()*xScale)), ypos, (int)(spriteSheet[currentFrame][0].getWidth()*xScale), (int)(spriteSheet[currentFrame][0].getHeight()*yScale), null);
				}
			}


			this.LastFrameTime=System.currentTimeMillis();
		}
		//}

	}
}
