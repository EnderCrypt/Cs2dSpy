package com.endercrypt.cs2dspy.representation;

import java.io.IOException;

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
		try (AccessSource source = SpyAccess.WEAPONS.access())
		{
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

		public Weapon(int weaponID, AccessSource source) throws IOException
		{
			this.weaponID = weaponID;
			name = source.read();
			if (name.equals(""))
				throw new IllegalArgumentException("Weapon name empty");
		}

		public int getID()
		{
			return weaponID;
		}

		public String getName()
		{
			return name;
		}
	}
}
