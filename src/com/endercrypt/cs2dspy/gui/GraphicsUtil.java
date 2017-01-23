package com.endercrypt.cs2dspy.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GraphicsUtil
{
	public static BufferedImage loadImage(File file) throws IOException
	{
		return optimizeImage(ImageIO.read(file));
	}

	/*
	public static Image loadFilteredImage(File file, int filterColor) throws IOException
	{
		Image image = optimizeImage(ImageIO.read(file));
		return filterOutColor(image, filterColor);
	}
	
	public static Image loadFilteredImage(File file, int x, int y) throws IOException
	{
		BufferedImage image = optimizeImage(ImageIO.read(file));
		return filterOutColor(image, image.getRGB(x, y));
	}
	*/

	public static <T extends BufferedImage> T optimizeImage(T image)
	{
		BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		convertedImage.getGraphics().drawImage(image, 0, 0, null);
		return image;
	}

	public static Image filterOutColor(Image image, int filterColor)
	{
		ImageFilter filter = new RGBImageFilter()
		{
			@Override
			public final int filterRGB(int x, int y, int rgb)
			{
				if (rgb == filterColor)
					return 0;
				return rgb;
			}
		};

		ImageProducer imageProducer = new FilteredImageSource(image.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(imageProducer);
	}
}
