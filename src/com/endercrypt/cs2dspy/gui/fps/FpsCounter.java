package com.endercrypt.cs2dspy.gui.fps;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

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
public class FpsCounter
{
	private Deque<FpsFrame> frames = new ArrayDeque<>();

	private int totalTime = 0;

	public FpsCounter()
	{

	}

	public int getFps()
	{
		validate();
		synchronized (frames)
		{
			return frames.size();
		}
	}

	public double averageFrameMs()
	{
		return ((double) totalTime / getFps());
	}

	public void add(FpsFrame frame)
	{
		frame.finish();
		synchronized (frames)
		{
			frames.add(frame);
			totalTime += frame.getDelay();
		}
	}

	private void validate()
	{
		long currentTime = System.currentTimeMillis();
		long lastSecond = currentTime - 1000;
		Iterator<FpsFrame> iterator = frames.iterator();
		while (iterator.hasNext())
		{
			FpsFrame frame = iterator.next();
			if (frame.getEndTime() < lastSecond)
			{
				synchronized (frames)
				{
					totalTime -= frame.getDelay();
					iterator.remove();
				}
			}
			else
			{
				break;
			}
		}
	}
}
