package com.endercrypt.cs2dspy.representation;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.endercrypt.cs2dspy.AccessSource;
import com.endercrypt.cs2dspy.SpyAccess;

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
public class WeaponInfo
{
	private static WeaponInfo instance = new WeaponInfo();

	public static WeaponInfo getInstance()
	{
		return instance;
	}

	private final String weaponsGfxDirectory;
	private final Weapon[] weapons;

	private WeaponInfo()
	{
		try (AccessSource source = SpyAccess.WEAPONS.access())
		{
			weaponsGfxDirectory = source.read();
			weapons = new Weapon[source.readInt()];
			for (int i = 0; i < weapons.length; i++)
			{
				try
				{
					weapons[i] = new Weapon(i + 1, source);
				}
				catch (IllegalArgumentException e)
				{
					// not a weapon, ignore
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public Weapon weapon(int id)
	{
		return weapons[id - 1];
	}

	public class Weapon
	{
		private int weaponID;
		private String name;
		private Image gfx;

		public Weapon(int weaponID, AccessSource source) throws IOException
		{
			this.weaponID = weaponID;
			name = source.read();
			if (name.equals(""))
				throw new IllegalArgumentException("Weapon name empty");
			gfx = addTransparancy(new Color(0, 0, 0).getRGB(), readImage(getGfxFile()));
		}

		private File getGfxFile() throws FileNotFoundException
		{
			String filename = weaponsGfxDirectory + name.replaceAll("-", "").replaceAll(" ", "").toLowerCase();
			File file_d = new File(filename + "_d.bmp");
			if (file_d.exists())
				return file_d;
			File file = new File(filename + ".bmp");
			if (file.exists() == false)
				throw new FileNotFoundException(file.toString());
			return file;
		}

		private BufferedImage readImage(File tilesetFile) throws IOException
		{
			BufferedImage image = ImageIO.read(tilesetFile);
			BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
			convertedImage.getGraphics().drawImage(image, 0, 0, null);
			return convertedImage;
		}

		private Image addTransparancy(int filterColor, BufferedImage image)
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

		public int getID()
		{
			return weaponID;
		}

		public String getName()
		{
			return name;
		}

		public Image getGfx()
		{
			return gfx;
		}
	}
}
