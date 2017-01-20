package com.endercrypt.cs2dspy.gui.splash;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.endercrypt.cs2dspy.gui.AwtWindow;
import com.endercrypt.cs2dspy.gui.AwtWindow.DrawListener;

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

	private static SplashGui splashGui = new SplashGui();
	private AwtWindow window;
	protected Thread waitingThread;

	private SplashWindow()
	{
		window = new AwtWindow("Welcome to Cs2d Spy!", new Dimension(300, 220), splashGui, true);
		JFrame jFrame = window.getJFrame();
		jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		jFrame.setResizable(false);
		window.getJPanel().addMouseListener(new SplashMouseEventListener(this));
	}

	public void show()
	{
		if (waitingThread != null)
			throw new IllegalStateException("Cannot show splash screen (its already being shown)");
		// show
		window.show();
		// create waiting thread
		waitingThread = new Thread(new SplashWaitingRunnable(window));
		// start waiting thread
		waitingThread.start();
	}

	public void showBlocking()
	{
		show();
		try
		{
			waitingThread.join();
		}
		catch (InterruptedException e)
		{
			// ignore
		}
	}
}
