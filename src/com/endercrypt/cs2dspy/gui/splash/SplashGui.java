package com.endercrypt.cs2dspy.gui.splash;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.endercrypt.cs2dspy.gui.AwtWindow.DrawListener;
import com.endercrypt.cs2dspy.network.update.VersionManager;

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
public class SplashGui implements DrawListener
{
	@Override
	public void onDraw(Graphics2D g2d, Dimension screenSize)
	{
		g2d.setColor(new Color(200, 200, 255));
		g2d.fillRect(0, 0, screenSize.width, screenSize.height);
		g2d.setColor(Color.BLACK);
		int location = 2;
		drawCentered(g2d, "Cs2d Spy Version " + VersionManager.getCurrentVersion().toString("."), screenSize.width / 2, 16 * location);
		location++;
		drawCentered(g2d, "Created By EnderCrypt (Magnus G)", screenSize.width / 2, 16 * location);
		location++;
		drawCentered(g2d, "U.S.G.N: 5783", screenSize.width / 2, 16 * location);
		location++;
		drawCentered(g2d, "https://github.com/EnderCrypt/Cs2dSpy", screenSize.width / 2, 16 * location);
		location += 5;
		drawCentered(g2d, "License", screenSize.width / 2, 16 * location);
		location++;
		drawCentered(g2d, "GNU General Public License v3", screenSize.width / 2, 16 * location);
		location++;
		drawCentered(g2d, "Read LICENSE.txt for further info", screenSize.width / 2, 16 * location);
	}

	private static void drawCentered(Graphics2D g2d, String text, int x, int y)
	{
		FontMetrics fontMetrics = g2d.getFontMetrics();
		int stringWidth = fontMetrics.stringWidth(text);
		g2d.drawString(text, x - (stringWidth / 2), y);
	}
}
