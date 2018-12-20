package org.imagediff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.idelta.CompareResultPixel;

public class ImageDiffMap {
	private BufferedImage image1;
	private BufferedImage image2;
	private BufferedImage diffImage;
	private Color diffColor;
	
	
	public ImageDiffMap(BufferedImage image1, BufferedImage image2,Color diffColor) {
		super();
		this.image1 = image1;
		this.image2 = image2;
		this.setDiffImage(null);
		this.setDiffColor(diffColor);
		resetData();
	}
	
	public String getInfoString() {
		return("width1="+getImage1().getWidth()+
				":height1="+getImage1().getHeight()+
				":width2="+getImage2().getWidth()+
				":height2="+getImage2().getHeight()+
				":colorSpace.numComponents="+getImage1().getColorModel().getColorSpace().getNumComponents()+
				":colorSpace.pixelSize="+getImage1().getColorModel().getPixelSize()

				);
	}
		
	public void resetData() {

	}
	
	public BufferedImage getScaledImage(BufferedImage toScale,double scaleFactor) {
		int width = new Double(toScale.getWidth()*scaleFactor).intValue();
		int height = new Double(toScale.getHeight()*scaleFactor).intValue();
		Image scaledImage = toScale.getScaledInstance(
				width,
				height,
				Image.SCALE_FAST);
		
		BufferedImage bufferedImage = new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bufferedImage.createGraphics();
	    bGr.drawImage(scaledImage, 0, 0, null);
	    bGr.dispose();
		return(bufferedImage);
	}

	
	public void analyze() throws Exception
	{
		resetData();

	
		
		int width1 = getImage1().getWidth(null);
		int width2 = getImage2().getWidth(null);
		int height1 = getImage1().getHeight(null);
		int height2 = getImage2().getHeight(null);
		int totalPixles = width1*height1;
		int lastPercent = 0;

		BufferedImage diffImage = new BufferedImage(width1,height1, BufferedImage.TYPE_INT_ARGB);
		//Graphics2D bGr = diffImage.createGraphics();
		double maxColorDistance = 0.0;
		double maxScale = 0.0;
		double totalScale = 0.0;

		if((width1 == width2) && (height1 == height2))
		{
						
			for(int x = 0;x < width1;x++)
			{
				for(int y = 0;y < height1;y++)
				{
					PixelCompare pixelCompare = new PixelCompare(
							new Point2D.Double((double)x, (double)y), 
							getImage1().getRGB(x, y), 
							getImage2().getRGB(x, y)
							);	
					double colorDistance = pixelCompare.getColorDistance();
					if(maxColorDistance<colorDistance) maxColorDistance = colorDistance;
					double scale = Math.pow(colorDistance/65536.0,0.5);
					totalScale += scale;
					if(maxScale<scale) maxScale = scale;

					diffImage.setRGB(x, y,getDiffColorScale(scale).getRGB());
				}
			}
		}
		//bGr.dispose();
		this.setDiffImage(diffImage);
		System.out.println("maxColorDistance="+maxColorDistance);
		System.out.println("maxScale="+maxScale);
		System.out.println("averageScale="+totalScale/totalPixles);

		
	}
	
	public Color getDiffColorScale(double scale) throws Exception
    {
		if(scale>1.0) scale = 1.0;
		float transparency = new Double(scale).floatValue();   
		Color diffColor = getDiffColor();
		if(scale==0.0) {
			diffColor = Color.white;
			transparency = 1.0f;
		}
		//transparency = 1.0f;

    	Color color = new Color(
    			(float)diffColor.getRed()/255f,
    			(float)diffColor.getGreen()/255f,
    			(float)diffColor.getBlue()/255f,transparency);
    	return(color);
    }
		
	public BufferedImage getImage1() {
		return image1;
	}
	public void setImage1(BufferedImage image1) {
		this.image1 = image1;
	}
	public BufferedImage getImage2() {
		return image2;
	}
	public void setImage2(BufferedImage image2) {
		this.image2 = image2;
	}

	public BufferedImage getDiffImage() {
		return diffImage;
	}

	public void setDiffImage(BufferedImage diffImage) {
		this.diffImage = diffImage;
	}

	public Color getDiffColor() {
		return diffColor;
	}

	public void setDiffColor(Color diffColor) {
		this.diffColor = diffColor;
	}

	
}