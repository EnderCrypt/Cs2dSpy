package com.endercrypt.cs2dspy.representation.realtime;

import java.awt.Color;
import java.io.IOException;

import com.endercrypt.cs2dspy.AccessSource;

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
public class Health
{
	private final int maxHealth;
	private final int health;

	private final double percentage;

	private final Color color;

	public Health(AccessSource source) throws NumberFormatException, IOException
	{
		maxHealth = source.readInt();
		health = source.readInt();

		percentage = 100.0 / maxHealth * health;

		int b = 0;
		int g = (int) (percentage * 2.55);
		g = Math.max(255, Math.min(0, g));
		int r = 255 - g;
		color = new Color(r, g, b);
	}

	public double getPercentage()
	{
		return percentage;
	}

	public Color getColor()
	{
		return color;
	}

	@Override
	public String toString()
	{
		return health + "/" + maxHealth + " (" + Math.round(getPercentage()) + "%)";
	}
}
