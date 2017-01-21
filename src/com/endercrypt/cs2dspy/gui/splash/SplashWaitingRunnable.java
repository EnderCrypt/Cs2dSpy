package com.endercrypt.cs2dspy.gui.splash;

import com.endercrypt.cs2dspy.gui.AwtWindow;

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
public class SplashWaitingRunnable implements Runnable
{
	private static int DELAY_SECONDS = 10;
	private AwtWindow window;

	public SplashWaitingRunnable(AwtWindow window)
	{
		this.window = window;
	}

	@Override
	public void run()
	{
		try
		{
			Thread.sleep(DELAY_SECONDS * 1000);
		}
		catch (InterruptedException e)
		{
			// ignore and dispose splash screen
		}
		window.dispose();
	}
}
