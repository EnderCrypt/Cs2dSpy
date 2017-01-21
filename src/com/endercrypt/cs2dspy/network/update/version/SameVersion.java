package com.endercrypt.cs2dspy.network.update.version;

import java.awt.Color;
import java.awt.Graphics2D;

import com.endercrypt.cs2dspy.gui.GuiText.Alignment;

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
public class SameVersion extends GenericVersion
{
	private double alpha = 1.0;

	public SameVersion()
	{
		super("Version up to date!", getColor(1.0));
	}

	private static Color getColor(double alpha)
	{
		return new Color(0, 255, 0, (int) (255 * alpha));
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y, Alignment alignment)
	{
		alpha -= 0.005;
		double xBack = (0.25 - alpha);
		if (xBack < 0)
			xBack = 0;
		xBack = (xBack / 0.25) * 20;
		xBack *= xBack;
		x -= (int) xBack;
		if (alpha > 0)
		{
			color = getColor(alpha);
			super.draw(g2d, x, y, alignment);
		}
	}
}
