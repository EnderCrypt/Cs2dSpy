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
		SplashPrinter printer = new SplashPrinter(g2d, screenSize.width / 2, 20);
		printer.println("Cs2d Spy Version " + VersionManager.getCurrentVersion().toString("."));
		printer.println("Created By EnderCrypt (Magnus G)");
		printer.println("U.S.G.N: 5783");
		printer.println("https://github.com/EnderCrypt/Cs2dSpy");
		printer.skipln(3);
		printer.println("License");
		printer.println("GNU General Public License v3");
		printer.println("Read LICENSE.txt for further info");
	}

	private class SplashPrinter
	{
		private final Graphics2D g2d;
		private final FontMetrics fontMetrics;
		private int x;
		private int y;

		protected SplashPrinter(Graphics2D g2d, int x, int y)
		{
			this.g2d = g2d;
			this.fontMetrics = g2d.getFontMetrics();
			this.x = x;
			this.y = y;
		}

		public void skipln()
		{
			skipln(1);
		}

		public void skipln(int times)
		{
			y += (fontMetrics.getHeight() + 2) * times;
		}

		public void println(String text)
		{
			int xPos = x - (fontMetrics.stringWidth(text) / 2);
			g2d.drawString(text, xPos, y);
			skipln();
		}
	}
}
