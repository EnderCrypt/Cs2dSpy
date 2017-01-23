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
import java.util.Optional;

import javax.imageio.ImageIO;

import com.endercrypt.cs2dspy.link.AccessSource;
import com.endercrypt.cs2dspy.link.SpyAccess;

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

	private final Weapon[] weapons;

	private WeaponInfo()
	{
		String weaponGfxDirectory = SpyCs2dInfo.get().getCs2dDirectory("gfx/weapons/");
		try (AccessSource source = SpyAccess.WEAPONS.access())
		{
			weapons = new Weapon[source.readInt()];
			for (int i = 0; i < weapons.length; i++)
			{
				try
				{
					weapons[i] = new Weapon(i + 1, source, weaponGfxDirectory);
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
		private String gfxName;
		private Optional<Image> gfx = Optional.empty();

		public Weapon(int weaponID, AccessSource source, String weaponGfxDirectory) throws IOException
		{
			this.weaponID = weaponID;
			name = source.read();
			gfxName = name.replaceAll("-", "").replaceAll(" ", "").toLowerCase();
			if (name.equals(""))
				throw new IllegalArgumentException("Weapon name empty");
			File weaponGfxFile = getGfxFile(weaponGfxDirectory);
			if (weaponGfxFile.exists())
			{
				gfx = Optional.of(addTransparancy(new Color(0, 0, 0).getRGB(), readImage(weaponGfxFile)));
			}
		}

		private File getGfxFile(String weaponGfxDirectory) throws FileNotFoundException
		{
			return new File(weaponGfxDirectory + gfxName + ".bmp");
		}

		private BufferedImage readImage(File file) throws IOException
		{
			BufferedImage image = ImageIO.read(file);
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

		public Optional<Image> getGfx()
		{
			return gfx;
		}
	}
}
