package com.endercrypt.cs2dspy.gui.fps;

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
public class FpsFrame
{
	private long startTime;
	private int delay;
	private long endTime;

	public FpsFrame()
	{
		startTime = System.currentTimeMillis();
	}

	protected void finish()
	{
		endTime = System.currentTimeMillis();
		delay = (int) (endTime - startTime);
	}

	public long getStartTime()
	{
		return startTime;
	}

	public int getDelay()
	{
		return delay;
	}

	public long getEndTime()
	{
		return endTime;
	}
}
