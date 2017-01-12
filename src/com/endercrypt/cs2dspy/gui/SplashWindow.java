package com.endercrypt.cs2dspy.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

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
public class SplashWindow
{
	public static SplashWindow create()
	{
		return new SplashWindow();
	}

	private AwtWindow window;
	private Thread waitingThread;

	private SplashWindow()
	{
		window = new AwtWindow("Welcome to Cs2d Spy!", new Dimension(300, 220), new SplashGui(), true);
		JFrame jFrame = window.getJFrame();
		jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		jFrame.setResizable(false);
		window.getJPanel().addMouseListener(new MouseEventListener());
	}

	public void show()
	{
		if (waitingThread != null)
			throw new IllegalStateException("Cannot show splash screen (its already being shown)");
		// show
		window.show();
		// create waiting thread
		waitingThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(10000);
				}
				catch (InterruptedException e)
				{
					// ignore
				}
				window.dispose();
			}
		});
		// start waiting thread
		waitingThread.start();
	}

	private class SplashGui implements AwtWindow.DrawListener
	{
		@Override
		public void onDraw(Graphics2D g2d, Dimension screenSize)
		{
			g2d.setColor(new Color(200, 200, 255));
			g2d.fillRect(0, 0, screenSize.width, screenSize.height);
			g2d.setColor(Color.BLACK);
			int location = 2;
			drawCentered(g2d, "Cs2d Spy V0.1", screenSize.width / 2, 16 * location);
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

		private void drawCentered(Graphics2D g2d, String text, int x, int y)
		{
			FontMetrics fontMetrics = g2d.getFontMetrics();
			int stringWidth = fontMetrics.stringWidth(text);
			g2d.drawString(text, x - (stringWidth / 2), y);
		}
	}

	private class MouseEventListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e)
		{
			waitingThread.interrupt();
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
			// TODO Auto-generated method stub
		}
	}
}
