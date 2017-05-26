package com.endercrypt.cs2dspy.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

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
public class GraphicsObject
{
	private static final GraphicsEnvironment graphicsEnvironment;
	private static GraphicsConfiguration graphicsConfiguration;

	static
	{
		graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		resetGraphicsConfiguration();
	}

	public static void resetGraphicsConfiguration()
	{
		graphicsConfiguration = graphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration();
	}

	private int transparancy = Transparency.TRANSLUCENT;
	private String name;
	private int width, height;
	private Image image;
	private VolatileImage volatileImage;

	public GraphicsObject(Path path) throws IOException
	{
		this(path.toFile());
	}

	public GraphicsObject(File file) throws IOException
	{
		this(ImageIO.read(file), file.toString());
	}

	public GraphicsObject(Image image)
	{
		this(image, "Un-named volatile image");
	}

	public GraphicsObject(Image image, String name)
	{
		this.name = name;
		this.image = image;
		width = image.getWidth(null);
		height = image.getHeight(null);
		recreateVolatileImage();
		validate();
	}

	private void recreateVolatileImage()
	{
		volatileImage = graphicsConfiguration.createCompatibleVolatileImage(width, height, transparancy);
		validate();
	}

	public void redrawVolatileImage()
	{
		Graphics2D g2d = volatileImage.createGraphics();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		g2d.setColor(new Color(255, 255, 255, 255));
		g2d.fillRect(0, 0, width, height);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
	}

	public void validate()
	{
		do
		{
			int result = volatileImage.validate(graphicsConfiguration);
			String action = null;
			switch (result)
			{
			case VolatileImage.IMAGE_OK:
				// do nothing
				break;
			case VolatileImage.IMAGE_RESTORED:
				action = "IMAGE_RESTORED";
				redrawVolatileImage();
				break;
			case VolatileImage.IMAGE_INCOMPATIBLE:
				action = "IMAGE_INCOMPATIBLE";
				recreateVolatileImage();
				break;
			}
			if (action != null)
			{
				System.out.println("\"" + name + "\" " + action);
			}
		}
		while (volatileImage.contentsLost());
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public double getCenterX()
	{
		return width / 2;
	}

	public double getCenterY()
	{
		return height / 2;
	}

	public Image getImage()
	{
		return image;
	}

	public VolatileImage getVolatileImage()
	{
		return volatileImage;
	}
}
