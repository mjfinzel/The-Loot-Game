package Game;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;





public class Images implements Serializable{
	public static BufferedImage invertAlpha(BufferedImage img){
		BufferedImage b = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = b.getGraphics();
		g.drawImage(img,0,0,null);
	    
		DataBufferByte buf = (DataBufferByte)b.getRaster().getDataBuffer();
		byte[] values = buf.getData();
		for(int i = 0; i < values.length; i += 4) values[i] = (byte)(values[i] ^ 0xff);
		return b;
	}
	public static BufferedImage load(String imageName)
    {
        BufferedImage image;
        try
        {
            image = ImageIO.read(Images.class.getResourceAsStream(imageName));
            BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = img.getGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            image = img;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        return image;
    }
	public static BufferedImage combine(BufferedImage[][]img){
		BufferedImage result = new BufferedImage( img.length*(img[0][0].getHeight()),img[0].length*(img[0][0].getWidth()), BufferedImage.TYPE_INT_ARGB);
		Graphics g = result.getGraphics();
		
		for(int x = 0; x<img.length;x++){
			for(int y = 0; y<img[0].length;y++){
				g.drawImage(img[x][y], x*(img[x][y].getWidth()), y*(img[x][y].getHeight()), null);
			}
		}
		g.dispose();
		//System.out.println(result.getWidth()+","+result.getHeight());
		return result;
	}
	public static BufferedImage[][] cut(String imageName, int sliceWidth, int sliceHeight)
	{
		//System.out.println("Starting image cut for "+imageName);
		long temp = System.currentTimeMillis();
		BufferedImage sheet;
		try
		{
			sheet = ImageIO.read(Images.class.getResourceAsStream(imageName));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}

		int w = sheet.getWidth();
		int h = sheet.getHeight();

		int xSlices = w/sliceWidth;
		int ySlices = h/sliceHeight;

		BufferedImage[][] images = new BufferedImage[xSlices][ySlices];
		for (int x=0; x<xSlices; x++)
			for (int y=0; y<ySlices; y++)
			{
				BufferedImage img = new BufferedImage(sliceWidth, sliceHeight, BufferedImage.TYPE_INT_ARGB);
				Graphics g = img.getGraphics();
				g.drawImage(sheet, -x*sliceWidth, -y*sliceHeight, null);
				g.dispose();
				images[x][y] = img;
			}
		
		//System.out.println("Done cutting image for "+imageName+". Total time spent on cut = "+(System.currentTimeMillis()-temp));
		return images;
	}
}
