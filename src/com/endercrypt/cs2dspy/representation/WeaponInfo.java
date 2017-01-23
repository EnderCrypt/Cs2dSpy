package com.endercrypt.cs2dspy.representation;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import com.endercrypt.cs2dspy.gui.GraphicsUtil;
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
		catch (IOException e)
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
				BufferedImage image = GraphicsUtil.loadImage(weaponGfxFile);
				int filterColor = image.getRGB(0, 0);
				gfx = Optional.of(GraphicsUtil.filterOutColor(image, filterColor));
			}
		}

		private File getGfxFile(String weaponGfxDirectory) throws FileNotFoundException
		{
			return new File(weaponGfxDirectory + gfxName + ".bmp");
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
