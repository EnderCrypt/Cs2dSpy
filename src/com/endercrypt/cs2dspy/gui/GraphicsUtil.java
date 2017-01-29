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

/**
 *	This file is part of Cs2dSpy and was created by Magnus Gunnarsson
 *
 *	Copyright (C) 2017  Magnus Gunnarsson (EnderCrypt)
 *
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class GraphicsUtil
{
	public static BufferedImage loadImage(File file) throws IOException
	{
		return optimizeImage(ImageIO.read(file));
	}

	public static BufferedImage optimizeImage(BufferedImage image)
	{
		BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		convertedImage.getGraphics().drawImage(image, 0, 0, null);
		return convertedImage;
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
